package org.light.serialize.core;

import org.junit.Test;
import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;

/**
 * @date 2021/01/10
 */
public class EnumTest {

    @Test
    public void testEnum() throws IOException {
        MyEnum e1 = MyEnum.E1;
        byte[] serializeData = SerializeUtil.serializeToBytes(e1);

        MyEnum deserializeE1 = (MyEnum)SerializeUtil.deserialize(serializeData);
        System.out.println(deserializeE1);
    }


    public static enum MyEnum {
        E1,
        E2;
    }
}
