package com.cat.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

public class RoundRobinLoadBalancer implements LoadBalancer {
    private int index = 0;

    @Override
    public Instance select(List<Instance> instances) {
        if (this.index >= instances.size()) {
            this.index %= instances.size();
        }
        return instances.get(index++);
    }
}
