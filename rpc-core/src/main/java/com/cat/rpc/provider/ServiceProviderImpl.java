package com.cat.rpc.provider;

import com.cat.rpc.enumeration.RpcError;
import com.cat.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的服务注册表，保存服务端本地服务。
 */
public class ServiceProviderImpl implements ServiceProvider {
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);
    /**
     * 映射服务名及服务实例，用 Map 实现表明某个接口只能有一个具体对象提供服务。
     */
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    /**
     * 已提供的服务。
     */
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T service, Class<T> serviceClass) {
        String serviceName = serviceClass.getCanonicalName();
        if (registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        serviceMap.put(serviceName, service);
        logger.info("向接口: {} 注册服务: {}", service.getClass().getInterfaces(), serviceName);
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
