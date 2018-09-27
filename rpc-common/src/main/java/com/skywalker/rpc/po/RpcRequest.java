package com.skywalker.rpc.po;

import lombok.Data;

/**
 * @author longchao
 * @date 2018/8/20.
 */
@Data
public class RpcRequest {

    private String requestId;

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;
}
