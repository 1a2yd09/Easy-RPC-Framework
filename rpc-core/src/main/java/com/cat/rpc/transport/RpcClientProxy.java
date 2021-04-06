package com.cat.rpc.transport;

import com.cat.rpc.entity.RpcRequest;
import com.cat.rpc.entity.RpcResponse;
import com.cat.rpc.transport.netty.client.NettyClient;
import com.cat.rpc.transport.socket.client.SocketClient;
import com.cat.rpc.util.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * RPC客户端动态代理。
 */
public class RpcClientProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);

    private final RpcClient client;

    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }

    /**
     * 获取服务接口的代理对象，使用代理对象的目的是为了封装复杂的请求对象构建以及请求发送流程，使客户端使用服务时简单易用。
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * 调用服务接口方法时，将间接调用该方法。
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        logger.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(), method.getDeclaringClass().getName(),
                method.getName(), method.getParameterTypes(), args, false);
        RpcResponse rpcResponse = null;
        if (client instanceof NettyClient) {
            // CompletableFuture 可以简单地理解为它就是一个数据的容器，在多个线程之间传递数据:
            CompletableFuture<RpcResponse> completableFuture = (CompletableFuture<RpcResponse>) client.sendRequest(rpcRequest);
            try {
                // Netty 客户端在此处同步等待远程调用的结果返回:
                rpcResponse = completableFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("方法调用请求发送失败", e);
                return null;
            }
        }
        if (client instanceof SocketClient) {
            rpcResponse = (RpcResponse) client.sendRequest(rpcRequest);
        }
        RpcMessageChecker.check(rpcRequest, rpcResponse);
        return rpcResponse.getData();
    }
}
