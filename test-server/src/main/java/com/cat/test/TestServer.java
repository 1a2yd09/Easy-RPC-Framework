package com.cat.test;

import com.cat.rpc.api.HelloService;
import com.cat.rpc.registry.DefaultServiceRegistry;
import com.cat.rpc.registry.ServiceRegistry;
import com.cat.rpc.server.RpcServer;

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
