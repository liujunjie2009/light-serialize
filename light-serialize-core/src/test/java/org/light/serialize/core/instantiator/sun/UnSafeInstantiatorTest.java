package org.light.serialize.core.instantiator.sun;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * UnSafeInstantiator test
 *
 * @author alex
 */
public class UnSafeInstantiatorTest extends TestCase {

    @Test
    public void testNewInstance() {
        UnSafeInstantiator<MyTest> myTestUnSafeInstantiator = new UnSafeInstantiator<>(MyTest.class);
        MyTest myTest = myTestUnSafeInstantiator.newInstance();
        Assert.assertNotNull(myTest);

        UnSafeInstantiator<MyTest.MyTestInner> myTestinnerUnSafeInstantiator = new UnSafeInstantiator<>(MyTest.MyTestInner.class);
        MyTest.MyTestInner myTestInner = myTestinnerUnSafeInstantiator.newInstance();
        Assert.assertNotNull(myTestInner);

        Throwable throwable = Assert.assertThrows(Throwable.class, () -> {
            UnSafeInstantiator<?> unSafeInstantiator = new UnSafeInstantiator<>(MyInterface.class);
            unSafeInstantiator.newInstance();
        });

        Assert.assertTrue(throwable.getCause().getClass().isAssignableFrom(InstantiationException.class));
    }

}

class MyTest {

    private String strVal;
    private int intVal;

    private MyTest() {
    }

    public String getStrVal() {
        return strVal;
    }

    public void setStrVal(String strVal) {
        this.strVal = strVal;
    }

    public int getIntVal() {
        return intVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }

    class MyTestInner {
        private String innerStrVal;
        private int innerIntVal;

        public MyTestInner() {
        }

        public String getInnerStrVal() {
            return innerStrVal;
        }

        public void setInnerStrVal(String innerStrVal) {
            this.innerStrVal = innerStrVal;
        }

        public int getInnerIntVal() {
            return innerIntVal;
        }

        public void setInnerIntVal(int innerIntVal) {
            this.innerIntVal = innerIntVal;
        }
    }
}

interface MyInterface {

}