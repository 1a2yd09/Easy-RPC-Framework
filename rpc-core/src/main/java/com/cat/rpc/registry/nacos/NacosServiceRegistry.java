package com.cat.rpc.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.cat.rpc.enumeration.RpcError;
import com.cat.rpc.exception.RpcException;
import com.cat.rpc.registry.ServiceRegistry;
import com.cat.rpc.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NacosServiceRegistry implements ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            logger.error("注册服务时有错误发生: ", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }
}
