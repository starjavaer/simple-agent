package com.ixingji.agent.client;


import com.ixingji.agent.grpc.ActionRequest;
import com.ixingji.agent.grpc.ActionResponse;
import com.ixingji.agent.grpc.ActorGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ActorClient implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActorClient.class);

    private final ActorGrpc.ActorBlockingStub blockingStub;

    private final ManagedChannel channel;

    public ActorClient(String target) {
        ManagedChannel channel = NettyChannelBuilder
                .forTarget(target)
                /*.negotiationType(NegotiationType.TLS)
                .sslContext(GrpcSslContexts.forClient().trustManager(file).build())*/
                .usePlaintext()
                .build();
        this.channel = channel;
        this.blockingStub = ActorGrpc.newBlockingStub(channel);
    }

    public ActionResponse doAction(String secretId, String action, String data) {
        LOGGER.info("Will try to do {}...", action);

        ActionRequest request = ActionRequest.newBuilder()
                .setSecretId(secretId)
                .setAction(action)
                .setData(data)
                .build();

        ActionResponse response;

        try {
            response = blockingStub.doAction(request);
        } catch (StatusRuntimeException e) {
            LOGGER.error("RPC failed: {}", e.getStatus());
            throw e;
        }

        LOGGER.info("Action response: {}", response);

        return response;
    }

    public static void main(String[] args) throws InterruptedException {
        String target = "";

        try (ActorClient client = new ActorClient(target)) {
            client.doAction("", "schedule", "hello");
        }
    }

    @Override
    public void close() throws InterruptedException {
        this.channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }

}
