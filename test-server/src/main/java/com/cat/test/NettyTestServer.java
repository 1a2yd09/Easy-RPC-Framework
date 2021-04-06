package com.cat.test;

import com.cat.rpc.api.HelloService;
import com.cat.rpc.serializer.CommonSerializer;
import com.cat.rpc.transport.netty.server.NettyServer;

public class NettyTestServer {
    public static void main(String[] args) {
        // 1. 准备服务实例。
        HelloService helloService = new HelloServiceImpl();
        // 2. 准备服务端对象并指定序列化方式。
        NettyServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
        // 3. 服务端注册准备好的服务实例提供服务。
        server.publishService(helloService, HelloService.class);
    }
}
