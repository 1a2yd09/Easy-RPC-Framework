package com.cat.test;

import com.cat.rpc.RpcClient;
import com.cat.rpc.RpcClientProxy;
import com.cat.rpc.api.HelloObject;
import com.cat.rpc.api.HelloService;
import com.cat.rpc.netty.client.NettyClient;

public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message.");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
