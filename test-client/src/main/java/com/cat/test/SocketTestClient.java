package com.cat.test;

import com.cat.rpc.api.ByeService;
import com.cat.rpc.api.HelloObject;
import com.cat.rpc.api.HelloService;
import com.cat.rpc.registry.ServiceDiscovery;
import com.cat.rpc.serializer.CommonSerializer;
import com.cat.rpc.transport.RpcClientProxy;
import com.cat.rpc.transport.socket.client.SocketClient;

public class SocketTestClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient(CommonSerializer.KRYO_SERIALIZER, ServiceDiscovery.REDIS_DISCOVERY);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        System.out.println(helloService.hello(object));
        ByeService byeService = proxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Socket"));
    }
}
