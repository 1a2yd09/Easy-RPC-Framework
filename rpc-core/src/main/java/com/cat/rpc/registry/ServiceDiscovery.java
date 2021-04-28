package com.cat.rpc.registry;

import com.cat.rpc.loadbalancer.LoadBalancer;
import com.cat.rpc.registry.nacos.NacosServiceDiscovery;
import com.cat.rpc.registry.redis.RedisServiceDiscovery;

import java.net.InetSocketAddress;

/**
 * 服务发现接口。
 */
public interface ServiceDiscovery {
    /**
     * 根据服务名称获取提供服务的服务端实例主机地址及端口。
     */
    InetSocketAddress lookupService(String serviceName);

    Integer NACOS_DISCOVERY = 0;
    Integer REDIS_DISCOVERY = 1;

    Integer DEFAULT_DISCOVERY = NACOS_DISCOVERY;

    static ServiceDiscovery getByCode(int code, LoadBalancer loadBalancer) {
        switch (code) {
            case 0:
                return new NacosServiceDiscovery(loadBalancer);
            case 1:
                return new RedisServiceDiscovery(loadBalancer);
            default:
                return null;
        }
    }
}
