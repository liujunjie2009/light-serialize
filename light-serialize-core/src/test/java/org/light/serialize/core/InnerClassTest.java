package org.light.serialize.core;

import org.junit.Test;
import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;

/**
 * @date 2021/02/03
 */
public class InnerClassTest {

    @Test
    public void test1() throws IOException {
        PsDataStructure1 dataStructure1 = new PsDataStructure1("xxx", 100);
        byte[] serializeBytes = SerializeUtil.serializeToBytes(dataStructure1);
        PsDataStructure1 deserialize = (PsDataStructure1)SerializeUtil.deserialize(serializeBytes);
        System.out.println(deserialize);

    }

    @Test
    public void test2() throws IOException {
        PsDataStructure2 dataStructure1 = new PsDataStructure2("xxx", 100);
        byte[] serializeBytes = SerializeUtil.serializeToBytes(dataStructure1);
        PsDataStructure2 deserialize = (PsDataStructure2)SerializeUtil.deserialize(serializeBytes);
        System.out.println(deserialize);
    }

    @Test
    public void test3() throws IOException {
        DataStructure3 dataStructure1 = new DataStructure3("xxx", 100);
        byte[] serializeBytes = SerializeUtil.serializeToBytes(dataStructure1);
        DataStructure3 deserialize = (DataStructure3)SerializeUtil.deserialize(serializeBytes);
        System.out.println(deserialize);


        DataStructure3.DataStructure3Inner dataStructure3Inner = deserialize.new DataStructure3Inner(){
            private String xxx = "liushuai";
            private Object xxxObj = new Object();
        };
        byte[] dataStructure3InnerBytes = SerializeUtil.serializeToBytes(dataStructure3Inner);
        DataStructure3.DataStructure3Inner deserializeDataStructure3Inner = (DataStructure3.DataStructure3Inner)SerializeUtil.deserialize(dataStructure3InnerBytes);
        System.out.println(deserializeDataStructure3Inner);
    }

    private static class PsDataStructure1 {

        private String name;
        private int age;

        public PsDataStructure1(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public PsDataStructure1() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "DataStructure1{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    class DataStructure3 {

        private String name;
        private int age;

        public DataStructure3(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public DataStructure3() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "DataStructure3{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

        private class DataStructure3Inner {
            private String name;
            private int age;

            public DataStructure3Inner(String name, int age) {
                this.name = name;
                this.age = age;
            }

            public DataStructure3Inner() {
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }

            @Override
            public String toString() {
                return "DataStructure3Inner{" +
                        "name='" + name + '\'' +
                        ", age=" + age +
                        '}';
            }
        }
    }

}

class PsDataStructure2 {

    private String name;
    private int age;

    public PsDataStructure2(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public PsDataStructure2() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "DataStructure2{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}