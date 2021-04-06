package com.cat.test;

import com.cat.rpc.api.HelloService;
import com.cat.rpc.serializer.CommonSerializer;
import com.cat.rpc.transport.socket.server.SocketServer;

public class SocketTestServer {
    public static void main(String[] args) {
        // 1. 准备服务实例。
        HelloService helloService = new HelloServiceImpl2();
        // 2. 准备服务端对象并指定序列化方式。
        SocketServer socketServer = new SocketServer("127.0.0.1", 9998, CommonSerializer.HESSIAN_SERIALIZER);
        // 3. 服务端注册准备好的服务实例提供服务。
        socketServer.publishService(helloService, HelloService.class);
    }
}
