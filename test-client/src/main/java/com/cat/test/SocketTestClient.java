package com.cat.test;

import com.cat.rpc.RpcClientProxy;
import com.cat.rpc.api.HelloObject;
import com.cat.rpc.api.HelloService;
import com.cat.rpc.socket.client.SocketClient;

public class SocketTestClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient("127.0.0.1", 9000);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message.");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}