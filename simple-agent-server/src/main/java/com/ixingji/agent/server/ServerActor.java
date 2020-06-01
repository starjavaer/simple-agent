package com.ixingji.agent.server;

import com.google.gson.internal.$Gson$Preconditions;
import com.ixingji.agent.grpc.ActionRequest;
import com.ixingji.agent.grpc.ActionResponse;
import com.ixingji.agent.grpc.ActorGrpc;
import com.ixingji.agent.server.action.ActionHandler;
import com.ixingji.agent.server.action.ActionManager;
import com.ixingji.agent.server.exception.SecretMismatchException;
import com.ixingji.agent.server.util.AgentServerUtils;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ServerActor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerActor.class);

    private Server server;

    public void start(int port) throws IOException {
//        File publicKeyPem = File.createTempFile("public-key-cert", ".pem");
//        Files.write(publicKey.getBytes(StandardCharsets.UTF_8), publicKeyPem);

//        File privateKeyPem = File.createTempFile("private-key-cert", ".pem");
//        Files.write(privateKey.getBytes(StandardCharsets.UTF_8), privateKeyPem);

        this.server = ServerBuilder.forPort(port)
//                .useTransportSecurity(new File("D:\\my-public-key-cert.pem"), new File("D:\\my-private-key.pem"))
                .addService(new ActorImpl())
                .build();

        server.start();

        LOGGER.info("Server started, listening on {}", port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOGGER.info("*** shutting down gRPC server since JVM is shutting down");
                try {
                    ServerActor.this.stop();
                } catch (InterruptedException e) {
                    LOGGER.error("shutdown error", e);
                }
                LOGGER.info("*** server shutdown");
            }
        });
    }

    private void stop() throws InterruptedException {
        if (this.server == null) {
            return;
        }

        this.server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (this.server == null) {
            return;
        }

        this.server.awaitTermination();
    }

    static class ActorImpl extends ActorGrpc.ActorImplBase {

        @Override
        public void doAction(ActionRequest request, StreamObserver<ActionResponse> responseObserver) {
            LOGGER.info("receive action request: {}", request);

            try {
                AgentServerUtils.checkRequest(request);
            } catch (SecretMismatchException e) {
                response(responseObserver, 1, e.getMessage().toUpperCase(), null);
                return;
            }

            /*
             code 0 SUCCESS
             code 1 SECRET MISMATCH
             code -1 FAILURE
             code 2 ACTION NOT FOUND
             */
            int code = 0;
            String message = null;

            ActionHandler actionHandler = ActionManager.getHandler(request.getAction().toUpperCase());

            String data = null;
            if (actionHandler != null) {
                try {
                    data = actionHandler.doHandle(request.getData());
                } catch (Exception e) {
                    LOGGER.error("do action handle error", e);
                    code = -1;
                    message = e.getMessage();
                } finally {
                    if (message == null) {
                        message = "SUCCESS";
                    }
                }
            } else {
                code = 2;
                message = "ACTION NOT FOUND";
            }

            response(responseObserver, code, message, data);
        }

        private void response(StreamObserver<ActionResponse> responseObserver, int code, String message, String data) {
            ActionResponse response = ActionResponse.newBuilder()
                    .setCode(code)
                    .setMessage(message)
                    .setData(data == null ? "" : data)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

    }

}
