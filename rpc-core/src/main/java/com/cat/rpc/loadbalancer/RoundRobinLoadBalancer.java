package com.cat.rpc.loadbalancer;

import com.cat.rpc.entity.RpcInstance;

import java.util.List;

public class RoundRobinLoadBalancer implements LoadBalancer {
    private int index = 0;

    @Override
    public RpcInstance select(List<RpcInstance> instances) {
        if (this.index >= instances.size()) {
            this.index %= instances.size();
        }
        return instances.get(index++);
    }
}
