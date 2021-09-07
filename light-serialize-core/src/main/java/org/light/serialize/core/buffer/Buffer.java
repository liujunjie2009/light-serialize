package org.light.serialize.core.buffer;

/**
 * {@link Buffer} provides two pointer variables to support sequential
 * read and write operations - {@link #readerIndex() readerIndex} for a read
 * operation and {@link #writerIndex() writerIndex} for a write operation
 * respectively.  The following diagram shows how a buffer is segmented into
 * three areas by the two pointers:
 * <pre>
 *      +-------------------+------------------+------------------+
 *      | discardable bytes |  readable bytes  |  writable bytes  |
 *      |                   |     (CONTENT)    |                  |
 *      +-------------------+------------------+------------------+
 *      |                   |                  |                  |
 *      0      <=      readerIndex   <=   writerIndex    <=    capacity
 * </pre>
 *
 * reference netty4 {@code io.netty.buffer.ByteBuf}
 *
 * @author alex
 */
public interface Buffer {

    /**
     * Returns the number of bytes this buffer can contain.
     */
    int capacity();

    /**
     * Returns the maximum allowed capacity of this buffer. If a user attempts to increase the
     * capacity of this buffer beyond the maximum capacity using {@link #ensureWritable(int)},
     * it will raise an {@link IllegalArgumentException}.
     */
    int maxCapacity();

    /**
     * Returns the {@code readerIndex} of this buffer.
     */
    int readerIndex();

    /**
     * Returns the {@code writerIndex} of this buffer.
     */
    int writerIndex();

    /**
     * Returns the number of readable bytes which is equal to {@code (this.writerIndex - this.readerIndex)}.
     */
    int readableBytes();

    /**
     * Returns the number of writable bytes which is equal to {@code (this.capacity - this.writerIndex)}.
     */
    int writableBytes();

    /**
     * Returns {@code true}
     * if and only if {@code (this.writerIndex - this.readerIndex)} is greater than {@code 0}.
     */
    boolean isReadable();

    /**
     * Returns {@code true}
     * if and only if this buffer contains equal to or more than the specified number of elements.
     */
    boolean isReadable(int size);

    /**
     * Returns {@code true}
     * if and only if {@code (this.capacity - this.writerIndex)} is greater than {@code 0}.
     */
    boolean isWritable();

    /**
     * Returns {@code true}
     * if and only if this buffer has enough room to allow writing the specified number of elements.
     */
    boolean isWritable(int size);

    /**
     * Makes sure the number of {@linkplain #writableBytes() the writable bytes} is equal to or greater than
     * the specified value. If there is enough writable bytes in this buffer, this method returns with no side effect.
     * Otherwise, it raises an {@link IllegalArgumentException}.
     *
     * @param minWritableBytes the expected minimum number of writable bytes
     * @throws IndexOutOfBoundsException if {@link #writerIndex()} + {@code minWritableBytes} &gt; {@link #maxCapacity()} ()}
     */
    Buffer ensureWritable(int minWritableBytes);

    /**
     * Sets the {@code readerIndex} and {@code writerIndex} of this buffer to {@code 0}.
     *
     *  before reset()
     *
     *      +-------------------+------------------+------------------+
     *      | discardable bytes |  readable bytes  |  writable bytes  |
     *      +-------------------+------------------+------------------+
     *      |                   |                  |                  |
     *      0      <=      readerIndex   <=   writerIndex    <=    capacity
     *
     *
     *  after reset()
     *
     *      +---------------------------------------------------------+
     *      |             writable bytes (got more space)             |
     *      +---------------------------------------------------------+
     *      |                                                         |
     *      0 = readerIndex = writerIndex            <=            capacity
     * </pre>
     *
     */
    Buffer reset();

    /**
     * Gets a byte at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 1} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 1}
     */
    byte readByte();

    /**
     * Transfers this buffer's data to a newly created buffer starting at
     * the current {@code readerIndex} and increases the {@code readerIndex}
     * by the number of the transferred bytes (= {@code length}).
     *
     * @param length the number of bytes to transfer
     *
     * @return the newly created buffer which contains the transferred bytes
     *
     * @throws IndexOutOfBoundsException
     *         if {@code length} is greater than {@code this.readableBytes}
     */
    byte[] readBytes(int length);

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code readerIndex} and increases the {@code readerIndex}
     * by the number of the transferred bytes (= {@code dest.length}).
     *
     * @throws IndexOutOfBoundsException
     *         if {@code dest.length} is greater than {@code this.readableBytes}
     */
    Buffer readBytes(byte[] dest);

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code readerIndex} and increases the {@code readerIndex}
     * by the number of the transferred bytes (= {@code length}).
     *
     * @param destIndex the first index of the destination
     * @param length   the number of bytes to transfer
     *
     * @throws IndexOutOfBoundsException
     *         if the specified {@code destIndex} is less than {@code 0},
     *         if {@code length} is greater than {@code this.readableBytes}, or
     *         if {@code destIndex + length} is greater than {@code dest.length}
     */
    Buffer readBytes(byte[] dest, int destIndex, int length);

    /**
     * Sets the specified byte at the current {@code writerIndex}
     * and increases the {@code writerIndex} by {@code 1} in this buffer.
     * The 24 high-order bits of the specified value are ignored.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code maxCapacity - writerIndex} is less than {@code 1}
     */
    Buffer writeByte(int value);

    /**
     * Transfers the specified source array's data to this buffer starting at
     * the current {@code writerIndex} and increases the {@code writerIndex}
     * by the number of the transferred bytes (= {@code src.length}).
     *
     * @throws IndexOutOfBoundsException
     *         if {@code maxCapacity - writerIndex} is less than {@code src.length}
     */
    Buffer writeBytes(byte[] src);

    /**
     * Transfers the specified source array's data to this buffer starting at
     * the current {@code writerIndex} and increases the {@code writerIndex}
     * by the number of the transferred bytes (= {@code length}).
     *
     * @param srcIndex the first index of the source
     * @param length   the number of bytes to transfer
     *
     * @throws IndexOutOfBoundsException
     *         if the specified {@code srcIndex} is less than {@code 0},
     *         if {@code srcIndex + length} is greater than {@code src.length},
     *         or if {@code maxCapacity - writerIndex} is less than {@code length}
     */
    Buffer writeBytes(byte[] src, int srcIndex, int length);

    /**
     * This method might discard some, all, or none of read bytes depending on
     * its internal implementation to reduce overall memory bandwidth consumption
     * at the cost of potentially additional memory consumption.
     */
    Buffer discardSomeReadBytes();

    /**
     * Returns the string representation of this buffer. This method does not
     * necessarily return the whole content of the buffer but returns
     * the values of the key properties such as {@link #readerIndex()},
     * {@link #writerIndex()}, {@link #maxCapacity()} ()} and {@link #capacity()}.
     */
    String toString();
}
