package com.cat.rpc.registry.redis;

import com.cat.rpc.entity.RedisInstance;
import com.cat.rpc.entity.RpcInstance;
import com.cat.rpc.enumeration.RpcError;
import com.cat.rpc.exception.RpcException;
import com.cat.rpc.loadbalancer.LoadBalancer;
import com.cat.rpc.loadbalancer.RandomLoadBalancer;
import com.cat.rpc.registry.ServiceDiscovery;
import com.cat.rpc.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

public class RedisServiceDiscovery implements ServiceDiscovery {
    private static final Logger logger = LoggerFactory.getLogger(RedisServiceDiscovery.class);

    private final LoadBalancer loadBalancer;

    public RedisServiceDiscovery(LoadBalancer loadBalancer) {
        this.loadBalancer = (loadBalancer == null) ? new RandomLoadBalancer() : loadBalancer;
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<RpcInstance> instances = RedisUtil.getAllInstances(serviceName)
                    .stream()
                    .map(RedisInstance::toInstance)
                    .collect(Collectors.toList());
            if (instances.isEmpty()) {
                logger.error("找不到对应的服务: {}", serviceName);
                throw new RpcException(RpcError.SERVICE_NOT_FOUND);
            }
            RpcInstance instance = loadBalancer.select(instances);
            return new InetSocketAddress(instance.getHost(), instance.getPort());
        } catch (Exception e) {
            logger.error("获取服务时有错误发生: ", e);
        }
        return null;
    }
}
