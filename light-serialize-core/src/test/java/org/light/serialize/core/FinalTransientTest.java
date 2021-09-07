package org.light.serialize.core;

import org.junit.Test;
import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;

/**
 * @date 2021/03/16
 */
public class FinalTransientTest {

    @Test
    public void testFinal() throws IOException {
        FinalTransient obj = new FinalTransient("lsl", "男", "123456");
        byte[] serialize = SerializeUtil.serializeToBytes(obj);
        Object desObj = SerializeUtil.deserialize(serialize);
        System.out.println(desObj);
    }
}

class FinalTransient {
    private final String name;
    private String sex;
    private transient String password;

    public FinalTransient(String name, String sex, String password) {
        this.name = name;
        this.sex = sex;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "FinalTransient{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
