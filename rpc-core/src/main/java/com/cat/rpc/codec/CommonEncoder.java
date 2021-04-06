package com.cat.rpc.codec;

import com.cat.rpc.entity.RpcRequest;
import com.cat.rpc.enumeration.PackageType;
import com.cat.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 消息出站时将目标类型进行encode转为ByteBuf
 */
public class CommonEncoder extends MessageToByteEncoder {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 四个字节的魔数:
        out.writeInt(MAGIC_NUMBER);
        // 四个字节的请求(响应)包代码:
        if (msg instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        } else {
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        }
        // 四个字节的(反)序列化器代码:
        out.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(msg);
        // 四个字节的数据长度:
        out.writeInt(bytes.length);
        // bytes.length 个字节的数据内容
        out.writeBytes(bytes);
    }
}
