package org.light.serialize.core.util;

import java.nio.charset.StandardCharsets;

/**
 * xxHash
 *
 * https://github.com/koron/java-xxhash/blob/master/src/main/java/net/kaoriya/xxhash/XXHash.java
 * https://github.com/Cyan4973/xxHash/blob/dev/xxhash.h
 * https://github.com/OpenHFT/Zero-Allocation-Hashing/blob/master/src/main/java/net/openhft/hashing/XxHash.java
 *
 * @author alex
 */
public class XxHashUtil {

    private static final int DEFAULT_INT_SEED = 0;
    private static final long DEFAULT_LONG_SEED = 0L;

    private static final int PRIME32_1 = 0x9E3779B1;
    private static final int PRIME32_2 = 0x85EBCA77;
    private static final int PRIME32_3 = 0xC2B2AE3D;
    private static final int PRIME32_4 = 0x27D4EB2F;
    private static final int PRIME32_5 = 0x165667B1;

    private static final long PRIME64_1 = 0x9E3779B185EBCA87L;
    private static final long PRIME64_2 = 0xC2B2AE3D27D4EB4FL;
    private static final long PRIME64_3 = 0x165667B19E3779F9L;
    private static final long PRIME64_4 = 0x85EBCA77C2B2AE63L;
    private static final long PRIME64_5 = 0x27D4EB2F165667C5L;

    private XxHashUtil() {
    }

    /**
     * Hash the string to int hash code.
     */
    public static int hashInt(String str) {
        if (str == null) {
            throw new IllegalArgumentException();
        }

        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        return hashInt(bytes, 0, bytes.length, DEFAULT_INT_SEED);
    }

    /**
     * Hash the string to long hash code.
     */
    public static long hashLong(String str) {
        if (str == null) {
            throw new IllegalArgumentException();
        }

        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        return hashLong(bytes, 0, bytes.length, DEFAULT_LONG_SEED);
    }

    /**
     * Hash the byte array to int hash code.
     */
    public static int hashInt(byte[] bytes, int off, int len, int seed) {
        int h32;
        int p = off;
        int end = off + len;

        if (len >= 16) {
            int v1 = seed + PRIME32_1 + PRIME32_2;
            int v2 = seed + PRIME32_2;
            int v3 = seed;
            int v4 = seed - PRIME32_1;

            for (int limit = end - 16; p <= limit; ) {
                v1 = round(v1, getInt(bytes, p));
                v2 = round(v2, getInt(bytes, p + 4));
                v3 = round(v3, getInt(bytes, p + 8));
                v4 = round(v4, getInt(bytes, p + 12));
                p += 16;
            }

            h32 = Integer.rotateLeft(v1, 1) + Integer.rotateLeft(v2, 7) + Integer.rotateLeft(v3, 12)
                    + Integer.rotateLeft(v4, 18);
        } else {
            h32 = seed + PRIME32_5;
        }

        h32 += len;

        /*
         * finalize
         */
        while (p + 4 <= end) {
            h32 += get32bits(bytes, p) * PRIME32_3;
            h32 = Integer.rotateLeft(h32, 17) * PRIME32_4;
            p += 4;
        }

        while (p < end) {
            h32 += ((int)bytes[p] & 0xFF) * PRIME32_5;
            h32 = Integer.rotateLeft(h32, 11) * PRIME32_1;
            p += 1;
        }

        h32 ^= h32 >>> 15;
        h32 *= PRIME32_2;
        h32 ^= h32 >>> 13;
        h32 *= PRIME32_3;
        h32 ^= h32 >>> 16;

        return h32;
    }

    public static int get32bits(byte[] b, int off) {
        return (((int)b[off + 3] & 0xFF) << 24)
                |  (((int)b[off + 2] & 0xFF) << 16)
                |  (((int)b[off + 1] & 0xFF) <<  8)
                |   ((int)b[off    ] & 0xFF);
    }

    /**
     * Hash the byte array to long hash code.
     */
    public static long hashLong(byte[] bytes, int off, int length, long seed) {
        if (bytes == null || off < 0 || length < 0 || off + length < 0 || off + length > bytes.length) {
            throw new IllegalArgumentException();
        }

        long h64;
        int p = off, end = off + length;

        if (length >= 32) {
            long v1 = seed + PRIME64_1 + PRIME64_2;
            long v2 = seed + PRIME64_2;
            long v3 = seed;
            long v4 = seed - PRIME64_1;

            for (int limit = end - 32; p <= limit; ) {
                v1 = round(v1, getLong(bytes, p));
                v2 = round(v2, getLong(bytes, p + 8));
                v3 = round(v3, getLong(bytes, p + 16));
                v4 = round(v4, getLong(bytes, p + 24));
                p += 32;
            }

            h64 = Long.rotateLeft(v1, 1) + Long.rotateLeft(v2, 7) + Long.rotateLeft(v3, 12)
                    + Long.rotateLeft(v4, 18);

            h64 = merge(round(v1), h64);
            h64 = merge(round(v2), h64);
            h64 = merge(round(v3), h64);
            h64 = merge(round(v4), h64);
        } else {
            h64 = seed + PRIME64_5;
        }

        h64 += length;

        /*
         * finalize
         */
        while (p + 8 <= end) {
            long k1 = round(getLong(bytes, p));
            h64 ^= k1;
            h64 = Long.rotateLeft(h64, 27) * PRIME64_1 + PRIME64_4;
            p += 8;
        }

        if (p + 4 <= end) {
            h64 ^= ((long) getInt(bytes, p) & 0xFFFFFFFFL) * PRIME64_1;
            h64 = Long.rotateLeft(h64, 23) * PRIME64_2 + PRIME64_3;
            p += 4;
        }

        while (p < end) {
            h64 ^= ((long) bytes[p] & 0xFFL) * PRIME64_5;
            h64 = Long.rotateLeft(h64, 11) * PRIME64_1;
            p += 1;
        }

        h64 ^= h64 >>> 33;
        h64 *= PRIME64_2;
        h64 ^= h64 >>> 29;
        h64 *= PRIME64_3;
        h64 ^= h64 >>> 32;

        return h64;
    }

    /**
     * round long
     */
    private static long round(long val, long input) {
        val += input * PRIME64_2;
        val = Long.rotateLeft(val, 31);
        return val * PRIME64_1;
    }

    /**
     * round int
     */
    private static int round(int val, int input) {
        val += input * PRIME32_2;
        val = Integer.rotateLeft(val, 13);
        return val * PRIME32_1;
    }

    /**
     * round
     */
    private static long round(long val) {
        val *= PRIME64_2;
        val = Long.rotateLeft(val, 31);
        return val * PRIME64_1;
    }

    /**
     * merge
     */
    private static long merge(long val, long hash) {
        hash ^= val;
        return hash * PRIME64_1 + PRIME64_4;
    }

    /**
     * get int
     */
    private static int getInt(byte[] bytes, int off) {
        return (((int)bytes[off + 3] & 0xFF) << 24)
                |  (((int)bytes[off + 2] & 0xFF) << 16)
                |  (((int)bytes[off + 1] & 0xFF) <<  8)
                |   ((int)bytes[off    ] & 0xFF);
    }

    /**
     * get long
     */
    private static long getLong(byte[] bytes, int off) {
        return (((long)bytes[off + 7] & 0xFF) << 56)
                |  (((long)bytes[off + 6] & 0xFF) << 48)
                |  (((long)bytes[off + 5] & 0xFF) << 40)
                |  (((long)bytes[off + 4] & 0xFF) << 32)
                |  (((long)bytes[off + 3] & 0xFF) << 24)
                |  (((long)bytes[off + 2] & 0xFF) << 16)
                |  (((long)bytes[off + 1] & 0xFF) <<  8)
                |   ((long)bytes[off    ] & 0xFF);
    }

}
