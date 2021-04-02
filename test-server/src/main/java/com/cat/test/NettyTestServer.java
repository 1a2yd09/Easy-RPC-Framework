package com.cat.test;

import com.cat.rpc.api.HelloService;
import com.cat.rpc.serializer.ProtobufSerializer;
import com.cat.rpc.transport.netty.server.NettyServer;

public class NettyTestServer {
    public static void main(String[] args) {
        // 1. 准备服务实例:
        HelloService helloService = new HelloServiceImpl();
        // 2. 准备服务提供端实例:
        NettyServer server = new NettyServer("127.0.0.1", 9999);
        // 3. 设置数据传输序列化方式:
        server.setSerializer(new ProtobufSerializer());
        // 4. 启动服务提供端并将服务信息注册到注册中心:
        server.publishService(helloService, HelloService.class);
    }
}
