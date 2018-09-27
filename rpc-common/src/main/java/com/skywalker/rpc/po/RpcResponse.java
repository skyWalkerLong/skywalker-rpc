package com.skywalker.rpc.po;

import lombok.Data;

/**
 * @author longchao
 * @date 2018/8/20.
 */
@Data
public class RpcResponse {

    private String requestId;

    private Throwable error;

    private Object result;
}
