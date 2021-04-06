package com.cat.test;

import com.cat.rpc.annotation.ServiceScan;
import com.cat.rpc.serializer.CommonSerializer;
import com.cat.rpc.transport.RpcServer;
import com.cat.rpc.transport.netty.server.NettyServer;

@ServiceScan
public class NettyTestServer {
    public static void main(String[] args) {
        RpcServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
        server.start();
    }
}
