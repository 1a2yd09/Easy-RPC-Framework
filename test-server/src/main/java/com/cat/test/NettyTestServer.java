package com.cat.test;

import com.cat.rpc.api.HelloService;
import com.cat.rpc.netty.server.NettyServer;
import com.cat.rpc.registry.DefaultServiceRegistry;
import com.cat.rpc.registry.ServiceRegistry;

public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }
}
