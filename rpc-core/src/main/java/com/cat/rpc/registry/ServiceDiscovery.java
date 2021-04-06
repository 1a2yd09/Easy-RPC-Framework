package com.cat.rpc.registry;

import java.net.InetSocketAddress;

/**
 * 服务发现接口。
 */
public interface ServiceDiscovery {
    /**
     * 根据服务名称获取提供服务的服务端实例主机地址及端口。
     */
    InetSocketAddress lookupService(String serviceName);
}
