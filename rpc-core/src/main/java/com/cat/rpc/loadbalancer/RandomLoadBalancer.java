package com.cat.rpc.loadbalancer;

import com.cat.rpc.entity.RpcInstance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public RpcInstance select(List<RpcInstance> instances) {
        return instances.get(new Random().nextInt(instances.size()));
    }
}
