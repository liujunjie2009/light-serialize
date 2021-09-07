package org.light.serialize.example.netty;

import io.netty.buffer.ByteBuf;
import org.light.serialize.core.buffer.Buffer;

public class NettyBufferWrapper implements Buffer {

    private final ByteBuf byteBuf;

    public NettyBufferWrapper(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public int capacity() {
        return byteBuf.capacity();
    }

    @Override
    public int maxCapacity() {
        return byteBuf.maxCapacity();
    }

    @Override
    public int readerIndex() {
        return byteBuf.readerIndex();
    }

    @Override
    public int writerIndex() {
        return byteBuf.writerIndex();
    }

    @Override
    public int readableBytes() {
        return byteBuf.readableBytes();
    }

    @Override
    public int writableBytes() {
        return byteBuf.writableBytes();
    }

    @Override
    public boolean isReadable() {
        return byteBuf.isReadable();
    }

    @Override
    public boolean isReadable(int size) {
        return byteBuf.isReadable(size);
    }

    @Override
    public boolean isWritable() {
        return byteBuf.isWritable();
    }

    @Override
    public boolean isWritable(int size) {
        return byteBuf.isWritable(size);
    }

    @Override
    public Buffer ensureWritable(int minWritableBytes) {
        byteBuf.ensureWritable(minWritableBytes);
        return this;
    }

    @Override
    public Buffer reset() {
        byteBuf.setIndex(0, 0);
        return this;
    }

    @Override
    public byte readByte() {
        return byteBuf.readByte();
    }

    @Override
    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    @Override
    public Buffer readBytes(byte[] dest) {
        byteBuf.readBytes(dest);
        return this;
    }

    @Override
    public Buffer readBytes(byte[] dest, int destIndex, int length) {
        byteBuf.readBytes(dest, destIndex, length);
        return this;
    }

    @Override
    public Buffer writeByte(int value) {
        byteBuf.writeByte(value);
        return this;
    }

    @Override
    public Buffer writeBytes(byte[] src) {
        byteBuf.writeBytes(src);
        return this;
    }

    @Override
    public Buffer writeBytes(byte[] src, int srcIndex, int length) {
        byteBuf.writeBytes(src, srcIndex, length);
        return this;
    }

    @Override
    public Buffer discardSomeReadBytes() {
        byteBuf.discardSomeReadBytes();
        return this;
    }
}
