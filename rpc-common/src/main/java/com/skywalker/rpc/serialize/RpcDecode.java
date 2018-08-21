package com.skywalker.rpc.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器
 * @author longchao
 * @date 2018/8/20.
 */
public class RpcDecode extends ByteToMessageDecoder {

    /**
     * 解码模板
     */
    private Class<?> decodeClass;

    private static final Integer length = 4;

    public RpcDecode(Class<?> decodeClass) {
        this.decodeClass = decodeClass;
    }

    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < length) {
            return;
        }

        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj =

    }
}
