package com.cat.rpc;

import com.cat.rpc.entity.RpcRequest;
import com.cat.rpc.serializer.CommonSerializer;

public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);

    void setSerializer(CommonSerializer serializer);
}
