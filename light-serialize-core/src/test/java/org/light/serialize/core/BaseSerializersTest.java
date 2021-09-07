package org.light.serialize.core;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alex
 * @date 2020/09/14
 */
public class BaseSerializersTest {

    @Test
    public void floatTest() throws IOException {
        float f = 10;
        byte[] floatData = SerializeUtil.serializeToBytes(f);
        System.out.println(SerializeUtil.deserialize(floatData));

    }



    @Test
    public void intTest() throws IOException {
        int intVal = 100;

        byte[] intData = SerializeUtil.serializeToBytes(intVal);
        System.out.println(SerializeUtil.deserialize(intData));

        long longVal = 200;

        byte[] longData = SerializeUtil.serializeToBytes(longVal);
        System.out.println(SerializeUtil.deserialize(longData));


        String stringVal = "少龙30";

        byte[] stringData = SerializeUtil.serializeToBytes(stringVal);
        System.out.println(SerializeUtil.deserialize(stringData));




    }

    @Test
    public void testSimple() {
        Map<String, String> data = new ConcurrentHashMap<>(16);
        data.put("name", "lsl");


        Stuff stuff = JSON.parseObject(JSON.toJSONString(data), Stuff.class);

        System.out.println(stuff);

    }

    public static class Stuff {
        private Map<String, String> data;

        public Map<String, String> getData() {
            return data;
        }

        public void setData(Map<String, String> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Stuff{" +
                    "data=" + data +
                    '}';
        }
    }
}
