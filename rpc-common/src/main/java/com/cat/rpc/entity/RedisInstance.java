package com.cat.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisInstance {
    private String host;
    private Integer port;

    public RpcInstance toInstance() {
        return RpcInstance.builder().host(host).port(port).build();
    }
}
