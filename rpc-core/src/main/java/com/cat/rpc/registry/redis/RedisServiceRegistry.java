package com.cat.rpc.registry.redis;

import com.cat.rpc.enumeration.RpcError;
import com.cat.rpc.exception.RpcException;
import com.cat.rpc.registry.ServiceRegistry;
import com.cat.rpc.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class RedisServiceRegistry implements ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(RedisServiceRegistry.class);

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            RedisUtil.registerService(serviceName, inetSocketAddress);
        } catch (JsonProcessingException e) {
            logger.error("注册服务时有错误发生: ", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }
}
