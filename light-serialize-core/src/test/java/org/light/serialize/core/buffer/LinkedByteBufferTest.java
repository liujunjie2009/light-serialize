package org.light.serialize.core.buffer;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.TestCase;

import static org.light.serialize.core.buffer.LinkedByteBuffer.DEFAULT_INITIAL_CAPACITY;
import static org.light.serialize.core.buffer.LinkedByteBuffer.NODE_STEPPER_BYTES;

/**
 * LinkedByteBuffer test
 *
 * @author alex
 */
public class LinkedByteBufferTest extends TestCase {

    @Test
    public void testAlloc() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        Assert.assertEquals(buffer.capacity(), DEFAULT_INITIAL_CAPACITY);

        buffer = LinkedByteBuffer.alloc(128);
        Assert.assertEquals(buffer.capacity(), 128);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            LinkedByteBuffer.alloc(-1);
        });
    }

    @Test
    public void testWrap() {
        LinkedByteBuffer buffer = LinkedByteBuffer.wrap(new byte[16], 0, 16);
        Assert.assertEquals(buffer.capacity(), 16);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            LinkedByteBuffer.wrap(null, 0, 16);
        });

        Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
            LinkedByteBuffer.wrap(new byte[16], -1, 16);
        });

        Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
            LinkedByteBuffer.wrap(new byte[16], 0, -1);
        });

        Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
            LinkedByteBuffer.wrap(new byte[16], 5, 3);
        });

        Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
            LinkedByteBuffer.wrap(new byte[16], 5, 18);
        });

    }

    @Test
    public void testCapacity() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        Assert.assertEquals(buffer.capacity(), DEFAULT_INITIAL_CAPACITY);
        buffer = LinkedByteBuffer.alloc(128);
        Assert.assertEquals(buffer.capacity(), 128);
    }

    @Test
    public void testMaxCapacity() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        Assert.assertEquals(buffer.maxCapacity(), Integer.MAX_VALUE);
    }

    @Test
    public void testWriterIndex() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = 0; i < 10000; i++) {
            buffer.writeByte(i);
            Assert.assertEquals(buffer.writerIndex(), i + 1);
        }
    }

    @Test
    public void testReaderIndex() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = 0; i < Byte.MAX_VALUE; i++) {
            buffer.writeByte(i);
        }

        for (int i = 0; i < Byte.MAX_VALUE; i++) {
            Assert.assertEquals(buffer.readerIndex(), i);
            buffer.readByte();
        }
    }

    @Test
    public void testReadableBytes() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            buffer.writeByte(i);
        }

        Assert.assertEquals(buffer.readableBytes(), Short.MAX_VALUE);
    }

    @Test
    public void testWritableBytes() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        Assert.assertEquals(buffer.writableBytes(), buffer.capacity);
        for (int i = 0; i < Byte.MAX_VALUE; i++) {
            Assert.assertEquals(buffer.writableBytes(), buffer.capacity - i);
            buffer.writeByte(i);

        }

    }

    @Test
    public void testIsReadable() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        Assert.assertFalse(buffer.isReadable());
        for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++) {
            buffer.writeByte(i);
        }
        Assert.assertTrue(buffer.isReadable(DEFAULT_INITIAL_CAPACITY));
        Assert.assertFalse(buffer.isReadable(DEFAULT_INITIAL_CAPACITY + 1));
        buffer.readBytes(buffer.capacity());
        Assert.assertFalse(buffer.isReadable());
        buffer.writeByte(100);
        Assert.assertTrue(buffer.isReadable());
    }

    @Test
    public void testIsWritable() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        Assert.assertTrue(buffer.isWritable());
        int capacity = buffer.capacity();
        Assert.assertTrue(buffer.isWritable(capacity));
        Assert.assertFalse(buffer.isWritable(capacity + 1));
        buffer.writeByte(100);
        Assert.assertTrue(buffer.isWritable(capacity - 1));
        Assert.assertFalse(buffer.isWritable(capacity));
    }

    @Test
    public void testEnsureWritable() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            buffer.ensureWritable(-1);
        });

        Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
            buffer.writeByte(1);
            buffer.ensureWritable(Integer.MAX_VALUE);
        });

        int writableBytes = buffer.writableBytes();
        buffer.ensureWritable(writableBytes + 128);
        Assert.assertTrue(buffer.capacity() >= buffer.writableBytes());
    }

    @Test
    public void testReset() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        buffer.writeBytes(new byte[Short.MAX_VALUE]);
        buffer.reset();
        Assert.assertTrue(buffer.readableBytes() == 0);
        Assert.assertTrue(buffer.writableBytes() == buffer.capacity());
    }

    @Test
    public void testReadByte() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = 0; i < Byte.MAX_VALUE; i++) {
            buffer.writeByte(i);
        }

        for (int i = 0; i < Byte.MAX_VALUE; i++) {
            Assert.assertTrue(buffer.readByte() == i);
        }
    }

    @Test
    public void testReadBytes() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        buffer.readBytes(0);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            buffer.readBytes(-1);
        });

        Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
            buffer.readBytes(1);
        });

        byte[] bytes = new byte[Short.MAX_VALUE];
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            bytes[i] = (byte) i;
        }

        buffer.writeBytes(bytes);
        Assert.assertArrayEquals(bytes, buffer.readBytes(Short.MAX_VALUE));

        buffer.writeBytes(bytes);
        byte[] dest1 = new byte[Short.MAX_VALUE];
        buffer.readBytes(dest1);
        Assert.assertArrayEquals(bytes, dest1);

        Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
            buffer.readBytes(new byte[Short.MAX_VALUE + 1]);
        });


        buffer.writeBytes(bytes);
        byte[] dest2 = new byte[Short.MAX_VALUE + 1];

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            buffer.readBytes(null, 1, Short.MAX_VALUE);
        });

        Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
            buffer.readBytes(dest2, 1, Short.MAX_VALUE + 2);
        });

        Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
            buffer.readBytes(dest2, 0, Short.MAX_VALUE + 1);
        });


        buffer.readBytes(dest2, 0, Short.MAX_VALUE);

        for (int i = 0; i < Short.MAX_VALUE; i++) {
            Assert.assertEquals(bytes[i], dest2[i]);
        }

        Assert.assertEquals(dest2[Short.MAX_VALUE], 0);
    }

    @Test
    public void testWriteByte() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            buffer.writeByte(i);
        }

        for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            Assert.assertEquals(buffer.readByte(), i);
        }
    }

    @Test
    public void testWriteBytes() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        byte[] bytes = new byte[Short.MAX_VALUE];

        for (int i = 0; i < Short.MAX_VALUE; i++) {
            bytes[i] = (byte) i;
        }

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            buffer.writeBytes(null);
        });

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            buffer.writeBytes(null, 0 , 100);
        });

        Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
            buffer.writeBytes(bytes, 0 , Short.MAX_VALUE + 1);
        });

        Node firstNode = buffer.firstNode;
        int firstNodeBytesLength = firstNode.bytes.length;
        buffer.writeBytes(bytes, 0, firstNodeBytesLength);
        buffer.writeBytes(bytes, firstNodeBytesLength, Short.MAX_VALUE - firstNodeBytesLength);
        byte[] dest1 = new byte[Short.MAX_VALUE];
        buffer.readBytes(dest1);

        Assert.assertArrayEquals(bytes, dest1);

        buffer.writeBytes(bytes);
        byte[] dest2 = new byte[Short.MAX_VALUE + 100];
        buffer.readBytes(dest2, 1, Short.MAX_VALUE);

        for (int i = 0; i < Short.MAX_VALUE; i++) {
            Assert.assertEquals(bytes[i], dest2[i + 1]);
        }

        Assert.assertEquals(dest2[Short.MAX_VALUE + 1], 0);
    }

    @Test
    public void testDiscardSomeReadBytes() {
        LinkedByteBuffer buffer = LinkedByteBuffer.alloc();
        Node firstNode = buffer.firstNode;

        byte[] firstNodeBytes = firstNode.bytes;
        int firstNodeBytesLength = firstNodeBytes.length;


        for (int i = 0; i < firstNodeBytesLength; i++) {
            buffer.writeByte(i);
        }

        Assert.assertEquals(buffer.readerIndex(), 0);
        Assert.assertNotEquals(buffer.writerIndex(), 0);

        buffer.readBytes(firstNodeBytesLength);

        // "readerIndex == writerIndex" will discard all bytes,this is identical to "reset" method.
        buffer.discardSomeReadBytes();

        Assert.assertEquals(buffer.readerIndex(), 0);
        Assert.assertEquals(buffer.writerIndex(), 0);
        Assert.assertEquals(buffer.firstNode, firstNode);

        for (int i = 0; i < DEFAULT_INITIAL_CAPACITY + NODE_STEPPER_BYTES * 2; i++) {
            buffer.writeByte(i);
        }

        buffer.readBytes(DEFAULT_INITIAL_CAPACITY - 1);
        // will discard nothing
        buffer.discardSomeReadBytes();
        Assert.assertEquals(buffer.firstNode, firstNode);

        buffer.readBytes(1);
        // will discard nothing
        buffer.discardSomeReadBytes();
        Assert.assertEquals(buffer.firstNode, firstNode);

        buffer.readBytes(1);
        Node secondNode = buffer.firstNode.next;
        Node lastNode = buffer.lastNode;
        // will discard firs node，and link firs node to the last.
        buffer.discardSomeReadBytes();
        Assert.assertNotEquals(buffer.firstNode, firstNode);
        Assert.assertEquals(buffer.firstNode, lastNode);
        Assert.assertEquals(secondNode, buffer.firstNode);
        Assert.assertEquals(buffer.readerIndex(), 1);
    }

}
