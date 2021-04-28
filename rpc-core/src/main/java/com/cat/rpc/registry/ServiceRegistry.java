package com.cat.rpc.registry;

import com.cat.rpc.registry.nacos.NacosServiceRegistry;
import com.cat.rpc.registry.redis.RedisServiceRegistry;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 */
public interface ServiceRegistry {
    /**
     * 将一个服务注册到注册中心。
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    Integer NACOS_REGISTRY = 0;
    Integer REDIS_REGISTRY = 1;

    static ServiceRegistry getByCode(int code) {
        switch (code) {
            case 0:
                return new NacosServiceRegistry();
            case 1:
                return new RedisServiceRegistry();
            default:
                return null;
        }
    }
}
