package com.cat.rpc.registry;

public interface ServiceRegistry {
    <T> void register(T service);

    Object getService(String serviceName);
}
