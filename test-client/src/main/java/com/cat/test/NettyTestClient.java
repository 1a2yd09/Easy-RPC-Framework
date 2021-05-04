package com.cat.test;

import com.cat.rpc.api.ByeService;
import com.cat.rpc.api.HelloObject;
import com.cat.rpc.api.HelloService;
import com.cat.rpc.loadbalancer.RandomLoadBalancer;
import com.cat.rpc.registry.ServiceDiscovery;
import com.cat.rpc.serializer.CommonSerializer;
import com.cat.rpc.transport.RpcClient;
import com.cat.rpc.transport.RpcClientProxy;
import com.cat.rpc.transport.netty.client.NettyClient;

public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.PROTOBUF_SERIALIZER, ServiceDiscovery.NACOS_DISCOVERY, new RandomLoadBalancer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        System.out.println(helloService.hello(object));
        ByeService byeService = rpcClientProxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));
    }
}
