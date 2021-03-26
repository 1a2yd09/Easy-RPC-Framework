package com.cat.test;

import com.cat.rpc.api.HelloObject;
import com.cat.rpc.api.HelloService;
import com.cat.rpc.client.RpcClientProxy;

public class TestClient {
    public static void main(String[] args) {
        // 封装一个代理对象:
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        // 运行时创建接口实例:
        HelloService helloService = proxy.getProxy(HelloService.class);
        // 构造方法参数:
        HelloObject object = new HelloObject(12, "This is a message.");
        // 调用接口方法，传入参数，转而调用InvocationHandler的invoke()方法:
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
