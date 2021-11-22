package org.light.serialize.core.buffer;

import java.util.Objects;

/**
 * A buffer that based on linked list, you can dynamic append byte array node to the last
 * rather than copy byte array when the buffer is not enough.
 * Every {@link Node} provides two pointer variables to support sequential
 * read and write operations also. {@link LinkedByteBuffer} has four node references:
 * {@link #firstNode},{@link #lastNode},{@link #readerNode} and {@link #writerNode}.
 *
 * <pre>
 *      +--------------------+-------------------+-------------------+
 *      | discardable bytes  |  readable bytes   |  writable bytes  |
 *      |                    |     (CONTENT)     |                  |
 *      |   first             reader              writer       last |
 *      +--------|-----------+--------|----------+--------|----------+
 *      | node1    node2     | node3    node4    | node5    node6   |
 *      0      <=      readerIndex   <=   writerIndex    <=    capacity
 * </pre>
 *
 * @author alex
 */
public class ByteBuffer implements Buffer {

    /**
     * The default initial capacity of the first node if not specified buffer size.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1024;

    /**
     * If not over {@code NODE_UPPER_BOUND_BYTES}, increase by  {@code NODE_STEPER_BYTES} multiplied by the {@code nodeSize - 1} power of two
     * eg: node1: initBytes, node2: 64 = (32 * 2) , node3: 128 = (32 * 4), node4: 256 = (32 * 8) ...
     */
    static final int NODE_STEPPER_BYTES = 32;

    /**
     * If over {@code NODE_UPPER_BOUND_BYTES}, increase by  {@code NODE_UPPER_BOUND_BYTES}.
     */
    static final int NODE_UPPER_BOUND_BYTES = 8 * 1024 * 1024;

    /**
     * The node length between {@code NODE_STEPPER_BYTES} and {@code NODE_UPPER_BOUND_BYTES}.
     */
    static final int NODE_STEPPER_LENGTH_THRESHOLD =
            Integer.numberOfLeadingZeros(NODE_STEPPER_BYTES) - Integer.numberOfLeadingZeros(NODE_UPPER_BOUND_BYTES);

    /**
     * Shared empty byte array instance used for empty instances.
     */
    private static final byte[] EMPTY_BYTES_ARRAY = {};

    /**
     * Node size of linked list.
     */
    int nodeSize;

    /**
     * The total byte size of buffer.
     */
    int capacity;

    /**
     * The maximum allowed capacity of this buffer.
     */
    final int maxCapacity = Integer.MAX_VALUE;

    /**
     * Reading index.
     */
    int readerIndex;

    /**
     * writing index.
     */
    int writerIndex;

    private byte[] bytes;

    private ByteBuffer(byte[] bytes) {
        Objects.requireNonNull(bytes);
        this.bytes = bytes;
    }

    private ByteBuffer(int size) {
        this(new byte[size]);
    }

    /**
     * Create a {@link LinkedByteBuffer} with the default capacity.
     */
    public static ByteBuffer alloc() {
        return new ByteBuffer(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Create a {@link LinkedByteBuffer} with the given initial capacity.
     */
    public static ByteBuffer alloc(int initCapacity) {
        if (initCapacity < 0) {
            throw new IllegalArgumentException(String.format("initial capacity: %d (expected: >= 0)", initCapacity));
        }

        return new ByteBuffer(initCapacity);
    }

    /**
     * Wrap a {@link LinkedByteBuffer} with the given byte array, readerIndex and readerIndex
     * to avoid double memory allocation.
     *
     * @throws IndexOutOfBoundsException if the specified {@code readerIndex} is less than 0,
     *                                   if the specified {@code writerIndex} is less than the specified
     *                                   {@code readerIndex} or if the specified {@code writerIndex} is
     *                                   greater than {@code this.capacity}
     *
     */
    public static ByteBuffer wrap(byte[] array, int readerIndex, int writerIndex) {
        if (array == null) {
            throw new IllegalArgumentException("array can not be null");
        }

        int capacity = array.length;
        if (readerIndex < 0 || readerIndex > writerIndex || writerIndex > capacity) {
            throw new IndexOutOfBoundsException(String.format(
                    "readerIndex: %d, writerIndex: %d (expected: 0 <= readerIndex <= writerIndex <= capacity(%d))",
                    readerIndex, writerIndex, capacity));
        }
        ByteBuffer buffer = new ByteBuffer(array);

        buffer.readerIndex = readerIndex;
        buffer.writerIndex = writerIndex;
        return buffer;
    }

    private static void checkRangeBounds(int index, int length, int capacity) {
        if ((index | length | index + length | capacity - (index + length)) < 0) {
            throw new IndexOutOfBoundsException(String.format(
                    "index: %d, length: %d (expected: range(0, %d))", index, length, capacity));
        }
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public int maxCapacity() {
        return maxCapacity;
    }

    @Override
    public int readerIndex() {
        return readerIndex;
    }

    @Override
    public int writerIndex() {
        return writerIndex;
    }

    @Override
    public int readableBytes() {
        return writerIndex - readerIndex;
    }

    @Override
    public int writableBytes() {
        return capacity() - writerIndex;
    }

    @Override
    public boolean isReadable() {
        return writerIndex > readerIndex;
    }

    @Override
    public boolean isReadable(int size) {
        return writerIndex - readerIndex >= size;
    }

    @Override
    public boolean isWritable() {
        return capacity() > writerIndex;
    }

    @Override
    public boolean isWritable(int size) {
        return capacity() - writerIndex >= size;
    }

    @Override
    public ByteBuffer ensureWritable(int minWritableBytes) {
        if (minWritableBytes < 0) {
            throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", minWritableBytes));
        }

        int writableBytes = writableBytes();
        if (minWritableBytes <= writableBytes) {
            return this;
        }

        if (minWritableBytes > maxCapacity - writerIndex) {
            throw new IndexOutOfBoundsException(String.format(
                    "writerIndex(%d) + minWritableBytes(%d) exceeds maxCapacity(%d): %s",
                    writerIndex, minWritableBytes, maxCapacity, this));
        }

        /*
         * If the nodeSize doesn't exceeds {@code NODE_STEPPER_LENGTH_THRESHOLD},
         * increase by {@code NODE_STEPER_BYTES} multiplied by the {@code nodeSize} power of two
         */
        int nodeSize = this.nodeSize;
        int increaseCapacity = nodeSize > NODE_STEPPER_LENGTH_THRESHOLD ?
                NODE_UPPER_BOUND_BYTES : (NODE_STEPPER_BYTES << nodeSize);

        if (writableBytes + increaseCapacity >= minWritableBytes) {
            return this;
        }

        // recursion ensureWritable
        return this;
    }

    /**
     * reset readerIndex,writerIndex,writerNode,readerNode and all nodes.
     */
    @Override
    public ByteBuffer reset() {
        readerIndex = writerIndex = 0;
        return this;
    }

    @Override
    public byte readByte() {
        checkReadableBytes(1);
        return doReadByte();
    }

    private byte doReadByte() {
        return bytes[readerIndex++];
    }

    @Override
    public Buffer readBytes(byte[] dest, int destIndex, int length) {
        if (dest == null) {
            throw new IllegalArgumentException("dest can not be null");
        }

        checkRangeBounds(destIndex, length, dest.length);
        checkReadableBytes(length);
        return doReadBytes(dest, destIndex, length);
    }

    ByteBuffer doReadBytes(byte[] dest, int destIndex, int length) {
        System.arraycopy(bytes, readerIndex, dest, destIndex, length);
        readerIndex += length;
        return this;
    }

    @Override
    public byte[] readBytes(int length) {
        if (length < 0) {
            throw new IllegalArgumentException(String.format("length: %d, (expected: length >= 0)", length));
        }

        if (length == 0) {
            return EMPTY_BYTES_ARRAY;
        }

        checkReadableBytes(length);

        byte[] dest = new byte[length];
        doReadBytes(dest, 0, length);
        return dest;
    }

    @Override
    public ByteBuffer readBytes(byte[] dest) {
        readBytes(dest, 0, dest.length);
        return this;
    }

    /**
     * Throws an {@link IndexOutOfBoundsException} if the current
     * {@linkplain #readableBytes() readable bytes} of this buffer is less
     * than the specified value.
     */
    void checkReadableBytes(int minReadableBytes) {
        if (readerIndex > writerIndex - minReadableBytes) {
            throw new IndexOutOfBoundsException(String.format(
                    "readerIndex(%d) + length(%d) exceeds writerIndex(%d): %s",
                    readerIndex, minReadableBytes, writerIndex, this));
        }
    }

    @Override
    public ByteBuffer writeByte(int value) {
        ensureWritable(1);
        return doWriteByte(value);
    }

    private ByteBuffer doWriteByte(int value) {
        bytes[writerIndex++] = (byte) value;
        return this;
    }

    @Override
    public ByteBuffer writeBytes(byte[] src) {
        if (src == null) {
            throw new IllegalArgumentException("src can not be null");
        }

        int length = src.length;
        ensureWritable(length);
        doWriteBytes(src, 0, length);
        return this;
    }

    @Override
    public ByteBuffer writeBytes(byte[] src, int srcIndex, int length) {
        if (src == null) {
            throw new IllegalArgumentException("src can not be null");
        }

        checkRangeBounds(srcIndex, length, src.length);
        ensureWritable(length);
        doWriteBytes(src, srcIndex, length);
        return this;
    }

    /**
     * Transfers the specified source array's data to this buffer's {@code writerNode},
     * if the {@code writerNode} has not enough capacity, will recursion run {@link #doWriteBytes}
     * for writing bytes to the next {@code writerNode}.
     */
    private ByteBuffer doWriteBytes(byte[] src, int srcIndex, int length) {
        System.arraycopy(src, srcIndex, bytes, writerIndex, length);
        writerIndex += length;
        return this;
    }

    /**
     * Reset the nodes before {@link #readerNode}, the {@link #capacity} will remain unchanged,
     * just modify {@link #readerIndex}, {@link #writerIndex}, {@link #readerNode}, {@link #writerNode}
     * and the nodes link order.
     * before:
     *
     *     first                                                        last  *
     *      |                                                            |    *
     * +--node1--+--node2--+--node3--+--node4--+--node5--+--node6--+--node7--+*
     *                         |                   |                          *
     *                       reader              writer                       *
     *
     * after:
     *
     *     first                                          *              last
     *      |                                             *               |
     * +--node3--+--node4--+--node5--+--node6--+--node7--+*--node1--+--node2--+
     *      |                   |                         *
     *    reader             writer                       *
     */
    @Override
    public ByteBuffer discardSomeReadBytes() {
        if (readerIndex == writerIndex) {
            return reset();
        }

        int resetCapacity = 0;

        this.readerIndex -= resetCapacity;
        this.writerIndex -= resetCapacity;

        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append(getClass().getSimpleName())
                .append("(ridx: ").append(readerIndex)
                .append(", widx: ").append(writerIndex)
                .append(", cap: ").append(capacity())
                .append(", macCap: ").append(maxCapacity())
                .append(", nsize: ").append(nodeSize);
        builder.append(')');
        return builder.toString();
    }

}
