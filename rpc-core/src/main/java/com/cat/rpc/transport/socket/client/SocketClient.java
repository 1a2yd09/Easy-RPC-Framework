package com.cat.rpc.transport.socket.client;

import com.cat.rpc.entity.RpcRequest;
import com.cat.rpc.entity.RpcResponse;
import com.cat.rpc.enumeration.RpcError;
import com.cat.rpc.exception.RpcException;
import com.cat.rpc.loadbalancer.LoadBalancer;
import com.cat.rpc.loadbalancer.RandomLoadBalancer;
import com.cat.rpc.registry.ServiceDiscovery;
import com.cat.rpc.serializer.CommonSerializer;
import com.cat.rpc.transport.RpcClient;
import com.cat.rpc.transport.socket.util.ObjectReader;
import com.cat.rpc.transport.socket.util.ObjectWriter;
import com.cat.rpc.util.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private final ServiceDiscovery serviceDiscovery;

    private final CommonSerializer serializer;

    public SocketClient() {
        this(DEFAULT_SERIALIZER, DEFAULT_DISCOVERY, new RandomLoadBalancer());
    }

    public SocketClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, DEFAULT_DISCOVERY, loadBalancer);
    }

    public SocketClient(Integer serializer, Integer discovery) {
        this(serializer, discovery, new RandomLoadBalancer());
    }

    public SocketClient(Integer serializer, Integer discovery, LoadBalancer loadBalancer) {
        this.serializer = CommonSerializer.getByCode(serializer);
        this.serviceDiscovery = ServiceDiscovery.getByCode(discovery, loadBalancer);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            RpcMessageChecker.check(rpcRequest, rpcResponse);
            return rpcResponse;
        } catch (IOException e) {
            logger.error("调用时有错误发生: ", e);
            throw new RpcException("服务调用失败: ", e);
        }
    }
}
