package com.cat.rpc.util;

import com.cat.rpc.entity.RedisInstance;
import com.cat.rpc.enumeration.RpcError;
import com.cat.rpc.exception.RpcException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RedisUtil {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private static final Jedis jedis;
    private static final Set<String> serviceNames = new HashSet<>();
    private static InetSocketAddress address;

    private static final String SERVER_ADDR = "192.168.136.128";
    private static final String SERVER_NAME_PREFIX = "service:";

    static {
        jedis = getJedisInstance();
    }

    public static Jedis getJedisInstance() {
        try {
            Jedis jedis = new Jedis(SERVER_ADDR);
            jedis.ping();
            return jedis;
        } catch (Exception e) {
            logger.error("连接到Redis时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    public static void registerService(String serviceName, InetSocketAddress address) throws JsonProcessingException {
        RedisInstance redisInstance = RedisInstance
                .builder()
                .host(address.getHostString())
                .port(address.getPort())
                .build();
        jedis.hset(SERVER_NAME_PREFIX + serviceName, address.getHostString() + ":" + address.getPort(), new ObjectMapper().writeValueAsString(redisInstance));
        serviceNames.add(serviceName);
        RedisUtil.address = address;
    }

    public static List<RedisInstance> getAllInstances(String serviceName) {
        return jedis.hgetAll(SERVER_NAME_PREFIX + serviceName).values()
                .stream()
                .map(json -> {
                    try {
                        return new ObjectMapper().readValue(json, RedisInstance.class);
                    } catch (JsonProcessingException e) {
                        logger.error("反序列化 RedisInstance 对象失败: ", e);
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    public static void clearRegistry() {
        if (!serviceNames.isEmpty() && address != null) {
            String host = address.getHostName();
            int port = address.getPort();
            for (String serviceName : serviceNames) {
                try {
                    jedis.hdel(SERVER_NAME_PREFIX + serviceName, host + ":" + port);
                } catch (Exception e) {
                    logger.error("注销服务 {} 失败", serviceName, e);
                }
            }
        }
    }
}
