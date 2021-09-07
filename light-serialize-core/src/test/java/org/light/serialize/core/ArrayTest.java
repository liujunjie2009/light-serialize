package org.light.serialize.core;

import org.junit.Test;
import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;
import java.util.Arrays;

/**
 * @date 2020/12/27
 */
public class ArrayTest {

    @Test
    public void testBytesArray() throws IOException {
        // byte[] object = {1, 2, 3, 4};
        // byte[] object = new byte[1024 * 1024 * 8];
        byte[] object = new byte[64 * 2];

        long now = System.currentTimeMillis();
        byte[] serializeData = SerializeUtil.serializeToBytes(object);

        byte[] deserializeData = (byte[])SerializeUtil.deserialize(serializeData);
        System.out.println(deserializeData.length);
        System.out.println(System.currentTimeMillis() - now);
        // -- 50 60 70 100
    }

    @Test
    public void testObjectArray() throws IOException {
        Object[] objects = {1, new Student(2), new Student(3)};
        byte[] serializeData = SerializeUtil.serializeToBytes(objects);

        Object[] deserializeData = (Object[])SerializeUtil.deserialize(serializeData);
        System.out.println(Arrays.toString(deserializeData));
    }

    @Test
    public void testMultyObjectArray() throws IOException {
        int[][] a = new int[1][];
        a[0] = null;
        String xx = "";

        Integer[] ints = {1, 2, 3};
        Object[] objects1 = ints;
        Object[] objects2 = {1, 2L};

        System.out.println(ints.getClass().getComponentType());
        System.out.println(objects1.getClass().getComponentType());
        System.out.println(objects2.getClass().getComponentType());



        Object[][][] objects = {{{1, new Student(10)}}, {{2, new Student(20)}}, {{3, new Student(30)}},
                {{11, new Student(110)}}, {{22, new Student(220)}}, {{33, new Student(330)}}};
        System.out.println(objects.length);
        System.out.println(objects[0].getClass().isArray());
        byte[] serializeData = SerializeUtil.serializeToBytes(objects);

        Object[][][] deserializeData = (Object[][][])SerializeUtil.deserialize(serializeData);
        System.out.println(Arrays.toString(deserializeData));
    }

    public static class Student {
        private Integer age;

        public Student() {
        }

        public Student(Integer age) {
            this.age = age;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "age=" + age +
                    '}';
        }
    }
}
