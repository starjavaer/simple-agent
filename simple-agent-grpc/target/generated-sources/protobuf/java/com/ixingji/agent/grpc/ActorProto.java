// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: action.proto

package com.ixingji.agent.grpc;

public final class ActorProto {
  private ActorProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ActionRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ActionRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ActionResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ActionResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014action.proto\"?\n\rActionRequest\022\020\n\010secre" +
      "tId\030\001 \001(\t\022\016\n\006action\030\002 \001(\t\022\014\n\004data\030\003 \001(\t\"" +
      "=\n\016ActionResponse\022\014\n\004code\030\001 \001(\005\022\017\n\007messa" +
      "ge\030\002 \001(\t\022\014\n\004data\030\003 \001(\t26\n\005Actor\022-\n\010doAct" +
      "ion\022\016.ActionRequest\032\017.ActionResponse\"\000B," +
      "\n\026com.ixingji.agent.grpcB\nActorProtoP\001\242\002" +
      "\003Actb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_ActionRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_ActionRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ActionRequest_descriptor,
        new java.lang.String[] { "SecretId", "Action", "Data", });
    internal_static_ActionResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_ActionResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ActionResponse_descriptor,
        new java.lang.String[] { "Code", "Message", "Data", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
