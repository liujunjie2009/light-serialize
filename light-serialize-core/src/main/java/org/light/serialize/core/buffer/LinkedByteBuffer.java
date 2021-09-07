package org.light.serialize.core.buffer;

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
public class LinkedByteBuffer implements Buffer {

    /**
     * The default initial capacity of the first node if not specified buffer size.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 64;

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
     * Pointer to first node.
     * Invariant: (first == null && last == null) || (first.prev == null && first.item != null)
     */
    Node firstNode;

    /**
     * Pointer to last node.
     * Invariant: (first == null && last == null) || (last.next == null && last.item != null)
     */
    Node lastNode;

    /**
     * Pointer to the reading node.
     */
    Node readerNode;

    /**
     * Pointer to the writing node.
     */
    Node writerNode;

    /**
     * Reading index.
     */
    int readerIndex;

    /**
     * writing index.
     */
    int writerIndex;

    private LinkedByteBuffer(byte[] bytes) {
        Node node = linkLast(bytes);
        this.readerNode = node;
        this.writerNode = node;
    }

    private LinkedByteBuffer(int size) {
        this(new byte[size]);
    }

    /**
     * Create a {@link LinkedByteBuffer} with the default capacity.
     */
    public static LinkedByteBuffer alloc() {
        return new LinkedByteBuffer(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Create a {@link LinkedByteBuffer} with the given initial capacity.
     */
    public static LinkedByteBuffer alloc(int initCapacity) {
        if (initCapacity < 0) {
            throw new IllegalArgumentException(String.format("initial capacity: %d (expected: >= 0)", initCapacity));
        }

        return new LinkedByteBuffer(initCapacity);
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
    public static LinkedByteBuffer wrap(byte[] array, int readerIndex, int writerIndex) {
        if (array == null) {
            throw new IllegalArgumentException("array can not be null");
        }

        int capacity = array.length;
        if (readerIndex < 0 || readerIndex > writerIndex || writerIndex > capacity) {
            throw new IndexOutOfBoundsException(String.format(
                    "readerIndex: %d, writerIndex: %d (expected: 0 <= readerIndex <= writerIndex <= capacity(%d))",
                    readerIndex, writerIndex, capacity));
        }

        LinkedByteBuffer buffer = new LinkedByteBuffer(array);
        Node firstNode = buffer.firstNode;

        buffer.readerIndex = readerIndex;
        buffer.writerIndex = writerIndex;
        firstNode.readerIndex = readerIndex;
        firstNode.writerIndex = writerIndex;

        return buffer;
    }

    private static void checkRangeBounds(int index, int length, int capacity) {
        if ((index | length | index + length | capacity - (index + length)) < 0) {
            throw new IndexOutOfBoundsException(String.format(
                    "index: %d, length: %d (expected: range(0, %d))", index, length, capacity));
        }
    }

    /**
     * Links bytes as last element.
     */
    Node linkLast(byte[] bytes) {
        final Node l = lastNode;
        final Node newNode = new Node(l, bytes, null);
        lastNode = newNode;
        if (l == null) {
            firstNode = newNode;
        } else {
            l.next = newNode;
        }

        nodeSize++;
        capacity += bytes.length;
        return newNode;
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
    public LinkedByteBuffer ensureWritable(int minWritableBytes) {
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

        // append byte array node
        linkLast(new byte[increaseCapacity]);

        if (writableBytes + increaseCapacity >= minWritableBytes) {
            return this;
        }

        // recursion ensureWritable
        return ensureWritable(minWritableBytes);
    }

    /**
     * reset readerIndex,writerIndex,writerNode,readerNode and all nodes.
     */
    @Override
    public LinkedByteBuffer reset() {
        Node node = writerNode;
        do {
            node.writerIndex = 0;
            node.readerIndex = 0;
        } while ((node = node.prev) != null);

        readerIndex = writerIndex = 0;
        writerNode = readerNode = firstNode;
        return this;
    }

    @Override
    public byte readByte() {
        checkReadableBytes(1);
        return doReadByte();
    }

    private byte doReadByte() {
        Node readerNode = this.readerNode;
        if (readerNode.readableBytes() > 0) {
            byte val = readerNode.readByte();
            this.readerIndex++;
            return val;
        }

        // next readerNode continue read
        this.readerNode = readerNode.next;
        return doReadByte();
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

    LinkedByteBuffer doReadBytes(byte[] dest, int destIndex, int length) {
        Node readerNode = this.readerNode;
        int readableNodeBytes = readerNode.readableBytes();

        if (readableNodeBytes == 0) {
            this.readerNode = readerNode.next;
            // recursion doReadBytes
            return doReadBytes(dest, destIndex, length);
        }

        if (readableNodeBytes < length) {
            readerNode.readBytes(dest, destIndex, readableNodeBytes);
            this.readerIndex += readableNodeBytes;
            this.readerNode = readerNode.next;
            // recursion doReadBytes
            return doReadBytes(dest, destIndex + readableNodeBytes, length - readableNodeBytes);
        }

        readerNode.readBytes(dest, destIndex, length);
        this.readerIndex += length;
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
    public LinkedByteBuffer readBytes(byte[] dest) {
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
    public LinkedByteBuffer writeByte(int value) {
        ensureWritable(1);
        return doWriteByte(value);
    }

    private LinkedByteBuffer doWriteByte(int value) {
        Node writerNode = this.writerNode;

        if (writerNode.writableBytes() > 0) {
            writerNode.writeByte(value);
            this.writerIndex++;
            return this;
        }

        // next writerNode continue writing
        this.writerNode = writerNode.next;
        return doWriteByte(value);
    }

    @Override
    public LinkedByteBuffer writeBytes(byte[] src) {
        if (src == null) {
            throw new IllegalArgumentException("src can not be null");
        }

        int length = src.length;
        ensureWritable(length);
        doWriteBytes(src, 0, length);
        return this;
    }

    @Override
    public LinkedByteBuffer writeBytes(byte[] src, int srcIndex, int length) {
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
    private LinkedByteBuffer doWriteBytes(byte[] src, int srcIndex, int length) {
        Node writerNode = this.writerNode;
        int writableNodeBytes = writerNode.writableBytes();

        if (writableNodeBytes == 0) {
            this.writerNode = writerNode.next;
            // recursion doWriteBytes
            return doWriteBytes(src, srcIndex, length);
        }

        if (writableNodeBytes < length) {
            writerNode.writeBytes(src, srcIndex, writableNodeBytes);
            this.writerIndex += writableNodeBytes;
            this.writerNode = writerNode.next;
            // recursion doWriteBytes
            return doWriteBytes(src, srcIndex + writableNodeBytes, length - writableNodeBytes);
        }

        writerNode.writeBytes(src, srcIndex, length);
        this.writerIndex += length;
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
    public LinkedByteBuffer discardSomeReadBytes() {
        Node readerNode = this.readerNode;
        Node firstNode = this.firstNode;
        Node lastNode = this.lastNode;

        if (readerIndex == writerIndex) {
            return reset();
        }

        if (readerNode == firstNode) {
            return this;
        }

        int resetCapacity = 0;

        // reset the nodes before {@link #readerNode}
        Node node = readerNode.prev;
        do {
            node.writerIndex = 0;
            node.readerIndex = 0;
            resetCapacity += node.bytes.length;
        } while((node = node.prev) != null);

        this.readerIndex -= resetCapacity;
        this.writerIndex -= resetCapacity;

        Node readerNodePrev = readerNode.prev;
        readerNode.prev = null;
        readerNodePrev.next = null;
        lastNode.next = firstNode;
        firstNode.prev = lastNode;

        this.firstNode = readerNode;
        this.lastNode = readerNodePrev;

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

/**
 * byte array node
 */
class Node {
    byte[] bytes;
    int readerIndex;
    int writerIndex;
    Node next;
    Node prev;

    Node(Node prev, byte[] bytes, Node next) {
        this.bytes = bytes;
        this.next = next;
        this.prev = prev;
    }

    int readableBytes() {
        return writerIndex - readerIndex;
    }

    int writableBytes() {
        return bytes.length - writerIndex;
    }
    // TODO: 优化？？
    void writeByte(int val) {
        bytes[writerIndex++] = (byte)val;
    }

    void writeBytes(byte[] src, int srcIndex, int length) {
        System.arraycopy(src, srcIndex, bytes, writerIndex, length);
        writerIndex += length;
    }

    byte readByte() {
        return bytes[readerIndex++];
    }

    void readBytes(byte[] dest, int destIndex, int length) {
        System.arraycopy(bytes, readerIndex, dest, destIndex, length);
        readerIndex += length;
    }

}
