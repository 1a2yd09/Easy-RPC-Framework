package com.cat.rpc.loadbalancer;

import com.cat.rpc.entity.RpcInstance;

import java.util.List;

public interface LoadBalancer {
    RpcInstance select(List<RpcInstance> instances);
}
