package com.cat.test;

import com.cat.rpc.RpcClient;
import com.cat.rpc.RpcClientProxy;
import com.cat.rpc.api.HelloObject;
import com.cat.rpc.api.HelloService;
import com.cat.rpc.serializer.ProtobufSerializer;
import com.cat.rpc.transport.netty.client.NettyClient;

public class NettyTestClient {
    public static void main(String[] args) {
        // 1. 准备客户端实例:
        RpcClient client = new NettyClient();
        // 2. 设置数据传输序列化方式:
        client.setSerializer(new ProtobufSerializer());
        // 3. 设置代理对象，避免服务使用者面对复杂的请求消息对象以及请求过程:
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        // 4. 调用具体服务获取结果:
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
