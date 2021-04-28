package com.cat.rpc.transport;

import com.cat.rpc.entity.RpcRequest;
import com.cat.rpc.registry.ServiceDiscovery;
import com.cat.rpc.serializer.CommonSerializer;

/**
 * 客户端类通用接口。
 */
public interface RpcClient {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;
    int DEFAULT_DISCOVERY = ServiceDiscovery.NACOS_DISCOVERY;

    Object sendRequest(RpcRequest rpcRequest);
}
