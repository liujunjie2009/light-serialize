package org.light.serialize.core;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

/**
 * @date 2021/03/08
 */
public class InnerTest {

    @Test
    public void test() {
        String xx = JSON.toJSONString(new My("xx"));
        System.out.println(xx);
    }

}

class  My {
    private String name;

    public My(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
