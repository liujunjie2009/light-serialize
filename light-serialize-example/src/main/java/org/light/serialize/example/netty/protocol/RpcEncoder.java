package org.light.serialize.example.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.example.netty.NettyBufferWrapper;

/**
 * RpcEncoder
 * 
 * @author Alex
 */
public class RpcEncoder extends MessageToByteEncoder<Object> {

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        int lengthWriterIndex = out.writerIndex();
        out.writeInt(0);
        new ObjectOutput(new NettyBufferWrapper(out)).writeObject(in);
        int length = out.writerIndex() - lengthWriterIndex - 4;
        out.markWriterIndex();
        out.writerIndex(lengthWriterIndex).writeInt(length);
        out.resetWriterIndex();
    }
}
