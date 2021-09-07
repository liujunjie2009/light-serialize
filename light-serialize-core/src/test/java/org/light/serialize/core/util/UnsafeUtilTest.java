package org.light.serialize.core.util;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import sun.misc.Unsafe;

/**
 * UnsafeUtil test
 *
 * @author alex
 */
public class UnsafeUtilTest extends TestCase {

    @Test
    public void testGetUnsafe() {
        Unsafe unsafe = UnsafeUtil.getUnsafe();
        Assert.assertNotNull(unsafe);
    }
}