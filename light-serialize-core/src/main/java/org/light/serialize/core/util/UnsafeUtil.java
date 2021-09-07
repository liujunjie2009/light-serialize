package org.light.serialize.core.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * UnsafeUtil
 *
 * @author alex
 */
public class UnsafeUtil {

    private static final Unsafe unsafe;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private UnsafeUtil() {}

    public static Unsafe getUnsafe() {
        return unsafe;
    }

}
