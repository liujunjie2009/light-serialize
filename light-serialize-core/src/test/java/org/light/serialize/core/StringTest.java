package org.light.serialize.core;

import org.junit.Test;
import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;

/**
 * @date 2020/12/27
 */
public class StringTest {

    @Test
    public void testString() throws IOException {
        Integer a = 100;
        System.out.println((long) a);
        StringBean stringBean = new StringBean();
        String userName = "lsl";
        stringBean.setUserName(userName);
        stringBean.setUserNameRef(userName);
        byte[] serializeData = SerializeUtil.serializeToBytes(stringBean);

        StringBean deserializeStringBean = (StringBean)SerializeUtil.deserialize(serializeData);
        System.out.println(deserializeStringBean);
        System.out.println(deserializeStringBean.userName == deserializeStringBean.userNameRef);
    }


    public static class StringBean {
        private String userName;
        private String userNameRef;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserNameRef() {
            return userNameRef;
        }

        public void setUserNameRef(String userNameRef) {
            this.userNameRef = userNameRef;
        }

        @Override
        public String toString() {
            return "StringBean{" +
                    "userName='" + userName + '\'' +
                    ", userNameRef='" + userNameRef + '\'' +
                    '}';
        }
    }
}
