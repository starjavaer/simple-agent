package com.ixingji.agent.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.28.0)",
    comments = "Source: action.proto")
public final class ActorGrpc {

  private ActorGrpc() {}

  public static final String SERVICE_NAME = "Actor";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.ixingji.agent.grpc.ActionRequest,
      com.ixingji.agent.grpc.ActionResponse> getDoActionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "doAction",
      requestType = com.ixingji.agent.grpc.ActionRequest.class,
      responseType = com.ixingji.agent.grpc.ActionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.ixingji.agent.grpc.ActionRequest,
      com.ixingji.agent.grpc.ActionResponse> getDoActionMethod() {
    io.grpc.MethodDescriptor<com.ixingji.agent.grpc.ActionRequest, com.ixingji.agent.grpc.ActionResponse> getDoActionMethod;
    if ((getDoActionMethod = ActorGrpc.getDoActionMethod) == null) {
      synchronized (ActorGrpc.class) {
        if ((getDoActionMethod = ActorGrpc.getDoActionMethod) == null) {
          ActorGrpc.getDoActionMethod = getDoActionMethod =
              io.grpc.MethodDescriptor.<com.ixingji.agent.grpc.ActionRequest, com.ixingji.agent.grpc.ActionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "doAction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ixingji.agent.grpc.ActionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ixingji.agent.grpc.ActionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ActorMethodDescriptorSupplier("doAction"))
              .build();
        }
      }
    }
    return getDoActionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ActorStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ActorStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ActorStub>() {
        @java.lang.Override
        public ActorStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ActorStub(channel, callOptions);
        }
      };
    return ActorStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ActorBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ActorBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ActorBlockingStub>() {
        @java.lang.Override
        public ActorBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ActorBlockingStub(channel, callOptions);
        }
      };
    return ActorBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ActorFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ActorFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ActorFutureStub>() {
        @java.lang.Override
        public ActorFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ActorFutureStub(channel, callOptions);
        }
      };
    return ActorFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class ActorImplBase implements io.grpc.BindableService {

    /**
     */
    public void doAction(com.ixingji.agent.grpc.ActionRequest request,
        io.grpc.stub.StreamObserver<com.ixingji.agent.grpc.ActionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getDoActionMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getDoActionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.ixingji.agent.grpc.ActionRequest,
                com.ixingji.agent.grpc.ActionResponse>(
                  this, METHODID_DO_ACTION)))
          .build();
    }
  }

  /**
   */
  public static final class ActorStub extends io.grpc.stub.AbstractAsyncStub<ActorStub> {
    private ActorStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ActorStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ActorStub(channel, callOptions);
    }

    /**
     */
    public void doAction(com.ixingji.agent.grpc.ActionRequest request,
        io.grpc.stub.StreamObserver<com.ixingji.agent.grpc.ActionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDoActionMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ActorBlockingStub extends io.grpc.stub.AbstractBlockingStub<ActorBlockingStub> {
    private ActorBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ActorBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ActorBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.ixingji.agent.grpc.ActionResponse doAction(com.ixingji.agent.grpc.ActionRequest request) {
      return blockingUnaryCall(
          getChannel(), getDoActionMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ActorFutureStub extends io.grpc.stub.AbstractFutureStub<ActorFutureStub> {
    private ActorFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ActorFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ActorFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.ixingji.agent.grpc.ActionResponse> doAction(
        com.ixingji.agent.grpc.ActionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDoActionMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_DO_ACTION = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ActorImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ActorImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_DO_ACTION:
          serviceImpl.doAction((com.ixingji.agent.grpc.ActionRequest) request,
              (io.grpc.stub.StreamObserver<com.ixingji.agent.grpc.ActionResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ActorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ActorBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.ixingji.agent.grpc.ActorProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Actor");
    }
  }

  private static final class ActorFileDescriptorSupplier
      extends ActorBaseDescriptorSupplier {
    ActorFileDescriptorSupplier() {}
  }

  private static final class ActorMethodDescriptorSupplier
      extends ActorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ActorMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ActorGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ActorFileDescriptorSupplier())
              .addMethod(getDoActionMethod())
              .build();
        }
      }
    }
    return result;
  }
}
