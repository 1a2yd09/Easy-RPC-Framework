package com.cat.test;

import com.cat.rpc.api.HelloService;
import com.cat.rpc.registry.DefaultServiceRegistry;
import com.cat.rpc.registry.ServiceRegistry;
import com.cat.rpc.socket.server.SocketServer;

public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.start(9000);
    }
}
