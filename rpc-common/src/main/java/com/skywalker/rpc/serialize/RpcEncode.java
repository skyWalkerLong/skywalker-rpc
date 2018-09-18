package com.skywalker.rpc.serialize;

import com.caucho.hessian.io.HessianOutput;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * @author longchao
 * @date 2018/8/20.
 */
public class RpcEncode extends MessageToByteEncoder {

    private Class<?> genericClass;

    // 构造函数传入向反序列化的class
    public RpcEncode(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object inob, ByteBuf out)
            throws Exception {
        //序列化
        if (genericClass.isInstance(inob)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            HessianOutput ho = new HessianOutput(bos);
            ho.writeObject(inob);
            byte[] data = bos.toByteArray();
            System.out.println("--" + Arrays.toString(data));
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
