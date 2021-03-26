package com.cat.test;

import com.cat.rpc.api.HelloService;
import com.cat.rpc.server.RpcServer;

public class TestServer {
    public static void main(String[] args) {
        // 实例化服务接口的具体实现类:
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        // 将服务注册到服务端中，此处暂定只注册一个接口，即对外提供一个接口的调用服务，注册一个服务后立即开始监听请求:
        rpcServer.register(helloService, 9000);
    }
}
