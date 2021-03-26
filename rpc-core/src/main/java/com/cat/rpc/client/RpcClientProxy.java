package com.cat.rpc.client;

import com.cat.rpc.entity.RpcRequest;
import com.cat.rpc.entity.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {
    private String host;
    private int port;

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构造一个请求参数对象:
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        // 实例化客户端对象，调用请求发送的方法:
        RpcClient rpcClient = new RpcClient();
        // 传入请求参数对象、地址、端口，将返回的结果封装为响应对象，调用方法获取数据:
        return ((RpcResponse) rpcClient.sendRequest(rpcRequest, this.host, this.port)).getData();
    }
}
