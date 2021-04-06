package com.cat.rpc.registry;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 */
public interface ServiceRegistry {
    /**
     * 将一个服务注册到注册中心。
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);
}
