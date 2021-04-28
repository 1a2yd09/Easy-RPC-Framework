package com.cat.test;

import com.cat.rpc.annotation.ServiceScan;
import com.cat.rpc.registry.ServiceRegistry;
import com.cat.rpc.serializer.CommonSerializer;
import com.cat.rpc.transport.RpcServer;
import com.cat.rpc.transport.socket.server.SocketServer;

@ServiceScan
public class SocketTestServer {
    public static void main(String[] args) {
        RpcServer server = new SocketServer("127.0.0.1", 9998, CommonSerializer.HESSIAN_SERIALIZER, ServiceRegistry.REDIS_REGISTRY);
        server.start();
    }
}
