package com.cat.test;

import com.cat.rpc.api.HelloObject;
import com.cat.rpc.api.HelloService;
import com.cat.rpc.serializer.CommonSerializer;
import com.cat.rpc.transport.RpcClientProxy;
import com.cat.rpc.transport.socket.client.SocketClient;

public class SocketTestClient {
    public static void main(String[] args) {
        // 1. 准备客户端对象并指定序列化方式。
        SocketClient client = new SocketClient(CommonSerializer.KRYO_SERIALIZER);
        // 2. 获取客户端的代理对象，动态创建接口对象。
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        // 3. 进行服务调用。
        HelloObject object = new HelloObject(12, "This is a message");
        for (int i = 0; i < 20; i++) {
            String res = helloService.hello(object);
            System.out.println(res);
        }
    }
}
