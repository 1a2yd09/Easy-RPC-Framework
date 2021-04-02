package com.cat.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RpcRequest implements Serializable {
    private String interfaceName;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] parameters;

    public RpcRequest() {
    }
}
