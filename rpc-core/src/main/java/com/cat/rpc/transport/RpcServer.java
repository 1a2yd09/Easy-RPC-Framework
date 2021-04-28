package com.cat.rpc.transport;

import com.cat.rpc.registry.ServiceRegistry;
import com.cat.rpc.serializer.CommonSerializer;

/**
 * 服务端类通用接口。
 */
public interface RpcServer {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;
    int DEFAULT_REGISTRY = ServiceRegistry.NACOS_REGISTRY;

    void start();

    <T> void publishService(T service, String serviceName);
}
