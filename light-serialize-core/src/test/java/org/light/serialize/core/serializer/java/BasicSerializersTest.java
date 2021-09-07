package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.DefaultSerializerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * BasicSerializers test
 * TODO: 3种模式
 * @author alex
 */
public class BasicSerializersTest {

    @Test
    public void testBasicSerializers() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        MyBasicClass myBasicClass = new MyBasicClass();
        output.writeObject(myBasicClass);

        Object actual = input.readObject();
        Assert.assertEquals(myBasicClass, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());
    }

}

class MyBasicClass {
    private byte byteVal1 = -1;
    private byte byteVal2 = 0;
    private byte byteVal3 = 1;
    private byte byteVal4 = Byte.MAX_VALUE;
    private byte byteVal5 = Byte.MIN_VALUE;
    private Byte byteVal6 = -1;
    private Byte byteVal7 = 0;
    private Byte byteVal8 = 1;
    private Byte byteVal9 = Byte.MAX_VALUE;
    private Byte byteVal10 = Byte.MIN_VALUE;
    private Byte byteVal11 = null;

    private boolean bool1 = false;
    private boolean bool2 = true;
    private Boolean bool3 = false;
    private Boolean bool4 = true;
    private Boolean bool5 = null;

    private char charVal1 = '1';
    private char charVal2 = '0';
    private char charVal4 = Character.MIN_VALUE;
    private char charVal5 = Character.MAX_VALUE;
    private Character charVal6 = '1';
    private Character charVal7 = '0';
    private Character charVal8 = Character.MIN_VALUE;
    private Character charVal9 = Character.MAX_VALUE;
    private Character charVal10 = null;

    private short shortVal1 = -1;
    private short shortVal2 = 0;
    private short shortVal3 = 1;
    private short shortVal4 = Short.MAX_VALUE;
    private short shortVal5 = Short.MIN_VALUE;
    private Short shortVal6 = -1;
    private Short shortVal7 = 0;
    private Short shortVal8 = 1;
    private Short shortVal9 = Short.MAX_VALUE;
    private Short shortVal10 = Short.MIN_VALUE;
    private Short shortVal11 = null;
    // private short shortVal12 = DefaultSerializerFactory.VAR_SHORT_ADVANTAGE_UPPER_BOUND;
    // private short shortVal13 = DefaultSerializerFactory.VAR_SHORT_ADVANTAGE_UPPER_BOUND + 1;
    // private short shortVal14 = DefaultSerializerFactory.VAR_SHORT_ADVANTAGE_UPPER_BOUND - 1;
    // private short shortVal15 = DefaultSerializerFactory.VAR_SHORT_ADVANTAGE_LOWER_BOUND;
    // private short shortVal16 = DefaultSerializerFactory.VAR_SHORT_ADVANTAGE_LOWER_BOUND + 1;
    // private Short shortVal17 = DefaultSerializerFactory.VAR_SHORT_ADVANTAGE_LOWER_BOUND - 1;
    // private Short shortVal18 = DefaultSerializerFactory.COMPLEMENT_VAR_SHORT_ADVANTAGE_UPPER_BOUND;
    // private Short shortVal19 = DefaultSerializerFactory.COMPLEMENT_VAR_SHORT_ADVANTAGE_UPPER_BOUND + 1;
    // private Short shortVal20 = DefaultSerializerFactory.COMPLEMENT_VAR_SHORT_ADVANTAGE_UPPER_BOUND - 1;
    // private Short shortVal21 = DefaultSerializerFactory.COMPLEMENT_VAR_SHORT_ADVANTAGE_LOWER_BOUND;
    // private Short shortVal22 = DefaultSerializerFactory.COMPLEMENT_VAR_SHORT_ADVANTAGE_LOWER_BOUND + 1;
    // private Short shortVal23 = DefaultSerializerFactory.COMPLEMENT_VAR_SHORT_ADVANTAGE_LOWER_BOUND - 1;


    private int intVal1 = -1;
    private int intVal2 = 0;
    private int intVal3 = 1;
    private int intVal4 = Integer.MAX_VALUE;
    private int intVal5 = Integer.MIN_VALUE;
    private Integer intVal6 = -1;
    private Integer intVal7 = 0;
    private Integer intVal8 = 1;
    private Integer intVal9 = Integer.MAX_VALUE;
    private Integer intVal10 = Integer.MIN_VALUE;
    private Integer intVal11 = null;
    // private int intVal12 = DefaultSerializerFactory.VAR_INT_ADVANTAGE_UPPER_BOUND;
    // private int intVal13 = DefaultSerializerFactory.VAR_INT_ADVANTAGE_UPPER_BOUND + 1;
    // private int intVal14 = DefaultSerializerFactory.VAR_INT_ADVANTAGE_UPPER_BOUND - 1;
    // private int intVal15 = DefaultSerializerFactory.VAR_INT_ADVANTAGE_LOWER_BOUND;
    // private int intVal16 = DefaultSerializerFactory.VAR_INT_ADVANTAGE_LOWER_BOUND + 1;
    // private int intVal17 = DefaultSerializerFactory.VAR_INT_ADVANTAGE_LOWER_BOUND - 1;
    // private int intVal18 = DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND;
    // private int intVal19 = DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND + 1;
    // private Integer intVal20 = DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND - 1;
    // private Integer intVal21 = DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND;
    // private Integer intVal22 = DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND + 1;
    // private Integer intVal23 = DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND - 1;

    private long longVal1 = -1;
    private long longVal2 = 0;
    private long longVal3 = 1;
    private long longVal4 = Long.MAX_VALUE;
    private long longVal5 = Long.MIN_VALUE;
    private Long longVal6 = -1L;
    private Long longVal7 = 0L;
    private Long longVal8 = 1L;
    private Long longVal9 = Long.MAX_VALUE;
    private Long longVal10 = Long.MIN_VALUE;
    private Long longVal11 = null;
    // private long longVal12 = DefaultSerializerFactory.VAR_LONG_ADVANTAGE_UPPER_BOUND;
    // private long longVal13 = DefaultSerializerFactory.VAR_LONG_ADVANTAGE_UPPER_BOUND + 1;
    // private long longVal14 = DefaultSerializerFactory.VAR_LONG_ADVANTAGE_UPPER_BOUND - 1;
    // private long longVal15 = DefaultSerializerFactory.VAR_LONG_ADVANTAGE_LOWER_BOUND;
    // private long longVal16 = DefaultSerializerFactory.VAR_LONG_ADVANTAGE_LOWER_BOUND + 1;
    // private long longVal17 = DefaultSerializerFactory.VAR_LONG_ADVANTAGE_LOWER_BOUND - 1;
    // private long longVal18 = DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_UPPER_BOUND;
    // private long longVal19 = DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_UPPER_BOUND + 1;
    // private Long longVal20 = DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_UPPER_BOUND - 1;
    // private Long longVal21 = DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND;
    // private Long longVal22 = DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND + 1;
    // private Long longVal23 = DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND - 1;


    private float floatVal1 = -1;
    private float floatVal2 = 0;
    private float floatVal3 = 1;
    private float floatVal4 = Float.MAX_VALUE;
    private float floatVal5 = Float.MIN_VALUE;
    private float floatVal51 = Float.NaN;
    private Float floatVal6 = -1F;
    private Float floatVal7 = 0F;
    private Float floatVal8 = 1F;
    private Float floatVal9 = Float.MAX_VALUE;
    private Float floatVal10 = Float.MIN_VALUE;
    private Float floatVal101 = Float.NaN;
    private Float floatVal11 = null;
    // private float floatVal12 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.VAR_INT_ADVANTAGE_UPPER_BOUND));
    // private float floatVal13 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.VAR_INT_ADVANTAGE_UPPER_BOUND + 1));
    // private float floatVal14 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.VAR_INT_ADVANTAGE_UPPER_BOUND - 1));
    // private float floatVal15 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.VAR_INT_ADVANTAGE_LOWER_BOUND));
    // private float floatVal16 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.VAR_INT_ADVANTAGE_LOWER_BOUND + 1));
    // private float floatVal17 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.VAR_INT_ADVANTAGE_LOWER_BOUND - 1));
    // private Float floatVal18 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.VAR_INT_ADVANTAGE_UPPER_BOUND));
    // private Float floatVal19 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.VAR_INT_ADVANTAGE_UPPER_BOUND + 1));
    // private Float floatVal20 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.VAR_INT_ADVANTAGE_UPPER_BOUND - 1));
    // private Float floatVal21 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.VAR_INT_ADVANTAGE_LOWER_BOUND));
    // private Float floatVal22 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.VAR_INT_ADVANTAGE_LOWER_BOUND + 1));
    // private Float floatVal23 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.VAR_INT_ADVANTAGE_LOWER_BOUND - 1));
    // private float floatVal24 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND));
    // private float floatVal25 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND + 1));
    // private float floatVal26 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND - 1));
    // private float floatVal27 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND));
    // private float floatVal28 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND + 1));
    // private float floatVal29 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND - 1));
    // private Float floatVal30 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND));
    // private Float floatVal31 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND + 1));
    // private Float floatVal32 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_UPPER_BOUND - 1));
    // private Float floatVal33 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND));
    // private Float floatVal34 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND + 1));
    // private Float floatVal35 = Float.intBitsToFloat(Integer.reverse(DefaultSerializerFactory.COMPLEMENT_VAR_INT_ADVANTAGE_LOWER_BOUND - 1));

    private double doubleVal1 = -1;
    private double doubleVal2 = 0;
    private double doubleVal3 = 1;
    private double doubleVal4 = Double.MAX_VALUE;
    private double doubleVal5 = Double.MIN_VALUE;
    private double doubleVal51 = Double.NaN;
    private Double doubleVal6 = -1D;
    private Double doubleVal7 = 0D;
    private Double doubleVal8 = 1D;
    private Double doubleVal9 = Double.MAX_VALUE;
    private Double doubleVal10 = Double.MIN_VALUE;
    private Double doubleVal101 = Double.NaN;
    private Double doubleVal11 = null;
    // private double doubleVal12 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.VAR_LONG_ADVANTAGE_UPPER_BOUND));
    // private double doubleVal13 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.VAR_LONG_ADVANTAGE_UPPER_BOUND + 1));
    // private double doubleVal14 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.VAR_LONG_ADVANTAGE_UPPER_BOUND - 1));
    // private double doubleVal15 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.VAR_LONG_ADVANTAGE_LOWER_BOUND));
    // private double doubleVal16 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.VAR_LONG_ADVANTAGE_LOWER_BOUND + 1));
    // private double doubleVal17 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.VAR_LONG_ADVANTAGE_LOWER_BOUND - 1));
    // private Double doubleVal18 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.VAR_LONG_ADVANTAGE_UPPER_BOUND));
    // private Double doubleVal19 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.VAR_LONG_ADVANTAGE_UPPER_BOUND + 1));
    // private Double doubleVal20 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.VAR_LONG_ADVANTAGE_UPPER_BOUND - 1));
    // private Double doubleVal21 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.VAR_LONG_ADVANTAGE_LOWER_BOUND));
    // private Double doubleVal22 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.VAR_LONG_ADVANTAGE_LOWER_BOUND + 1));
    // private Double doubleVal23 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.VAR_LONG_ADVANTAGE_LOWER_BOUND - 1));
    // private double doubleVal24 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_UPPER_BOUND));
    // private double doubleVal25 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_UPPER_BOUND + 1));
    // private double doubleVal26 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_UPPER_BOUND - 1));
    // private double doubleVal27 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND));
    // private double doubleVal28 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND + 1));
    // private double doubleVal29 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND - 1));
    // private Double doubleVal30 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_UPPER_BOUND));
    // private Double doubleVal31 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_UPPER_BOUND + 1));
    // private Double doubleVal32 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_UPPER_BOUND - 1));
    // private Double doubleVal33 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND));
    // private Double doubleVal34 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND + 1));
    // private Double doubleVal35 = Double.longBitsToDouble(Long.reverse(DefaultSerializerFactory.NEGATIVE_VAR_LONG_ADVANTAGE_LOWER_BOUND - 1));


    private String strVal1 = "1234567890!@#$%^&*()_+=-\"\"mnbvcxzlkjhgfdsapoiuytrewq";
    private String strVal2 = "我的名字是Alex";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyBasicClass that = (MyBasicClass) o;
        return byteVal1 == that.byteVal1 &&
                byteVal2 == that.byteVal2 &&
                byteVal3 == that.byteVal3 &&
                byteVal4 == that.byteVal4 &&
                byteVal5 == that.byteVal5 &&
                bool1 == that.bool1 &&
                bool2 == that.bool2 &&
                charVal1 == that.charVal1 &&
                charVal2 == that.charVal2 &&
                charVal4 == that.charVal4 &&
                charVal5 == that.charVal5 &&
                shortVal1 == that.shortVal1 &&
                shortVal2 == that.shortVal2 &&
                shortVal3 == that.shortVal3 &&
                shortVal4 == that.shortVal4 &&
                shortVal5 == that.shortVal5 &&
                // shortVal12 == that.shortVal12 &&
                // shortVal13 == that.shortVal13 &&
                // shortVal14 == that.shortVal14 &&
                // shortVal15 == that.shortVal15 &&
                // shortVal16 == that.shortVal16 &&
                intVal1 == that.intVal1 &&
                intVal2 == that.intVal2 &&
                intVal3 == that.intVal3 &&
                intVal4 == that.intVal4 &&
                intVal5 == that.intVal5 &&
                // intVal12 == that.intVal12 &&
                // intVal13 == that.intVal13 &&
                // intVal14 == that.intVal14 &&
                // intVal15 == that.intVal15 &&
                // intVal16 == that.intVal16 &&
                // intVal17 == that.intVal17 &&
                // intVal18 == that.intVal18 &&
                // intVal19 == that.intVal19 &&
                longVal1 == that.longVal1 &&
                longVal2 == that.longVal2 &&
                longVal3 == that.longVal3 &&
                longVal4 == that.longVal4 &&
                longVal5 == that.longVal5 &&
                // longVal12 == that.longVal12 &&
                // longVal13 == that.longVal13 &&
                // longVal14 == that.longVal14 &&
                // longVal15 == that.longVal15 &&
                // longVal16 == that.longVal16 &&
                // longVal17 == that.longVal17 &&
                // longVal18 == that.longVal18 &&
                // longVal19 == that.longVal19 &&
                Float.compare(that.floatVal1, floatVal1) == 0 &&
                Float.compare(that.floatVal2, floatVal2) == 0 &&
                Float.compare(that.floatVal3, floatVal3) == 0 &&
                Float.compare(that.floatVal4, floatVal4) == 0 &&
                Float.compare(that.floatVal5, floatVal5) == 0 &&
                Float.compare(that.floatVal51, floatVal51) == 0 &&
                // Float.compare(that.floatVal12, floatVal12) == 0 &&
                // Float.compare(that.floatVal13, floatVal13) == 0 &&
                // Float.compare(that.floatVal14, floatVal14) == 0 &&
                // Float.compare(that.floatVal15, floatVal15) == 0 &&
                // Float.compare(that.floatVal16, floatVal16) == 0 &&
                // Float.compare(that.floatVal17, floatVal17) == 0 &&
                // Float.compare(that.floatVal24, floatVal24) == 0 &&
                // Float.compare(that.floatVal25, floatVal25) == 0 &&
                // Float.compare(that.floatVal26, floatVal26) == 0 &&
                // Float.compare(that.floatVal27, floatVal27) == 0 &&
                // Float.compare(that.floatVal28, floatVal28) == 0 &&
                // Float.compare(that.floatVal29, floatVal29) == 0 &&
                Double.compare(that.doubleVal1, doubleVal1) == 0 &&
                Double.compare(that.doubleVal2, doubleVal2) == 0 &&
                Double.compare(that.doubleVal3, doubleVal3) == 0 &&
                Double.compare(that.doubleVal4, doubleVal4) == 0 &&
                Double.compare(that.doubleVal5, doubleVal5) == 0 &&
                Double.compare(that.doubleVal51, doubleVal51) == 0 &&
                // Double.compare(that.doubleVal12, doubleVal12) == 0 &&
                // Double.compare(that.doubleVal13, doubleVal13) == 0 &&
                // Double.compare(that.doubleVal14, doubleVal14) == 0 &&
                // Double.compare(that.doubleVal15, doubleVal15) == 0 &&
                // Double.compare(that.doubleVal16, doubleVal16) == 0 &&
                // Double.compare(that.doubleVal17, doubleVal17) == 0 &&
                // Double.compare(that.doubleVal24, doubleVal24) == 0 &&
                // Double.compare(that.doubleVal25, doubleVal25) == 0 &&
                // Double.compare(that.doubleVal26, doubleVal26) == 0 &&
                // Double.compare(that.doubleVal27, doubleVal27) == 0 &&
                // Double.compare(that.doubleVal28, doubleVal28) == 0 &&
                // Double.compare(that.doubleVal29, doubleVal29) == 0 &&
                Objects.equals(byteVal6, that.byteVal6) &&
                Objects.equals(byteVal7, that.byteVal7) &&
                Objects.equals(byteVal8, that.byteVal8) &&
                Objects.equals(byteVal9, that.byteVal9) &&
                Objects.equals(byteVal10, that.byteVal10) &&
                Objects.equals(byteVal11, that.byteVal11) &&
                Objects.equals(bool3, that.bool3) &&
                Objects.equals(bool4, that.bool4) &&
                Objects.equals(bool5, that.bool5) &&
                Objects.equals(charVal6, that.charVal6) &&
                Objects.equals(charVal7, that.charVal7) &&
                Objects.equals(charVal8, that.charVal8) &&
                Objects.equals(charVal9, that.charVal9) &&
                Objects.equals(charVal10, that.charVal10) &&
                Objects.equals(shortVal6, that.shortVal6) &&
                Objects.equals(shortVal7, that.shortVal7) &&
                Objects.equals(shortVal8, that.shortVal8) &&
                Objects.equals(shortVal9, that.shortVal9) &&
                Objects.equals(shortVal10, that.shortVal10) &&
                Objects.equals(shortVal11, that.shortVal11) &&
                // Objects.equals(shortVal17, that.shortVal17) &&
                // Objects.equals(shortVal18, that.shortVal18) &&
                // Objects.equals(shortVal19, that.shortVal19) &&
                // Objects.equals(shortVal20, that.shortVal20) &&
                // Objects.equals(shortVal21, that.shortVal21) &&
                // Objects.equals(shortVal22, that.shortVal22) &&
                // Objects.equals(shortVal23, that.shortVal23) &&
                Objects.equals(intVal6, that.intVal6) &&
                Objects.equals(intVal7, that.intVal7) &&
                Objects.equals(intVal8, that.intVal8) &&
                Objects.equals(intVal9, that.intVal9) &&
                Objects.equals(intVal10, that.intVal10) &&
                Objects.equals(intVal11, that.intVal11) &&
                // Objects.equals(intVal20, that.intVal20) &&
                // Objects.equals(intVal21, that.intVal21) &&
                // Objects.equals(intVal22, that.intVal22) &&
                // Objects.equals(intVal23, that.intVal23) &&
                Objects.equals(longVal6, that.longVal6) &&
                Objects.equals(longVal7, that.longVal7) &&
                Objects.equals(longVal8, that.longVal8) &&
                Objects.equals(longVal9, that.longVal9) &&
                Objects.equals(longVal10, that.longVal10) &&
                Objects.equals(longVal11, that.longVal11) &&
                // Objects.equals(longVal20, that.longVal20) &&
                // Objects.equals(longVal21, that.longVal21) &&
                // Objects.equals(longVal22, that.longVal22) &&
                // Objects.equals(longVal23, that.longVal23) &&
                Objects.equals(floatVal6, that.floatVal6) &&
                Objects.equals(floatVal7, that.floatVal7) &&
                Objects.equals(floatVal8, that.floatVal8) &&
                Objects.equals(floatVal9, that.floatVal9) &&
                Objects.equals(floatVal10, that.floatVal10) &&
                Objects.equals(floatVal101, that.floatVal101) &&
                Objects.equals(floatVal11, that.floatVal11) &&
                // Objects.equals(floatVal18, that.floatVal18) &&
                // Objects.equals(floatVal19, that.floatVal19) &&
                // Objects.equals(floatVal20, that.floatVal20) &&
                // Objects.equals(floatVal21, that.floatVal21) &&
                // Objects.equals(floatVal22, that.floatVal22) &&
                // Objects.equals(floatVal23, that.floatVal23) &&
                // Objects.equals(floatVal30, that.floatVal30) &&
                // Objects.equals(floatVal31, that.floatVal31) &&
                // Objects.equals(floatVal32, that.floatVal32) &&
                // Objects.equals(floatVal33, that.floatVal33) &&
                // Objects.equals(floatVal34, that.floatVal34) &&
                // Objects.equals(floatVal35, that.floatVal35) &&
                Objects.equals(doubleVal6, that.doubleVal6) &&
                Objects.equals(doubleVal7, that.doubleVal7) &&
                Objects.equals(doubleVal8, that.doubleVal8) &&
                Objects.equals(doubleVal9, that.doubleVal9) &&
                Objects.equals(doubleVal10, that.doubleVal10) &&
                Objects.equals(doubleVal101, that.doubleVal101) &&
                Objects.equals(doubleVal11, that.doubleVal11) &&
                // Objects.equals(doubleVal18, that.doubleVal18) &&
                // Objects.equals(doubleVal19, that.doubleVal19) &&
                // Objects.equals(doubleVal20, that.doubleVal20) &&
                // Objects.equals(doubleVal21, that.doubleVal21) &&
                // Objects.equals(doubleVal22, that.doubleVal22) &&
                // Objects.equals(doubleVal23, that.doubleVal23) &&
                // Objects.equals(doubleVal30, that.doubleVal30) &&
                // Objects.equals(doubleVal31, that.doubleVal31) &&
                // Objects.equals(doubleVal32, that.doubleVal32) &&
                // Objects.equals(doubleVal33, that.doubleVal33) &&
                // Objects.equals(doubleVal34, that.doubleVal34) &&
                // Objects.equals(doubleVal35, that.doubleVal35) &&
                Objects.equals(strVal1, that.strVal1) &&
                Objects.equals(strVal2, that.strVal2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(byteVal1, byteVal2, byteVal3, byteVal4, byteVal5, byteVal6, byteVal7, byteVal8, byteVal9, byteVal10, byteVal11, bool1, bool2, bool3, bool4, bool5, charVal1, charVal2, charVal4, charVal5, charVal6, charVal7, charVal8, charVal9, charVal10, shortVal1, shortVal2, shortVal3, shortVal4, shortVal5, shortVal6, shortVal7, shortVal8, shortVal9, shortVal10, shortVal11, /*shortVal12, shortVal13, shortVal14, shortVal15, shortVal16, shortVal17, shortVal18, shortVal19, shortVal20, shortVal21, shortVal22, shortVal23, */intVal1, intVal2, intVal3, intVal4, intVal5, intVal6, intVal7, intVal8, intVal9, intVal10, intVal11, /*intVal12, intVal13, intVal14, intVal15, intVal16, intVal17, intVal18, intVal19, intVal20, intVal21, intVal22, intVal23,*/ longVal1, longVal2, longVal3, longVal4, longVal5, longVal6, longVal7, longVal8, longVal9, longVal10, longVal11, /*longVal12, longVal13, longVal14, longVal15, longVal16, longVal17, longVal18, longVal19, longVal20, longVal21, longVal22, longVal23, */floatVal1, floatVal2, floatVal3, floatVal4, floatVal5, floatVal51, floatVal6, floatVal7, floatVal8, floatVal9, floatVal10, floatVal101, floatVal11, /*floatVal12, floatVal13, floatVal14, floatVal15, floatVal16, floatVal17, floatVal18, floatVal19, floatVal20, floatVal21, floatVal22, floatVal23, floatVal24, floatVal25, floatVal26, floatVal27, floatVal28, floatVal29, floatVal30, floatVal31, floatVal32, floatVal33, floatVal34, floatVal35, */doubleVal1, doubleVal2, doubleVal3, doubleVal4, doubleVal5, doubleVal51, doubleVal6, doubleVal7, doubleVal8, doubleVal9, doubleVal10, doubleVal101, doubleVal11, /*doubleVal12, doubleVal13, doubleVal14, doubleVal15, doubleVal16, doubleVal17, doubleVal18, doubleVal19, doubleVal20, doubleVal21, doubleVal22, doubleVal23, doubleVal24, doubleVal25, doubleVal26, doubleVal27, doubleVal28, doubleVal29, doubleVal30, doubleVal31, doubleVal32, doubleVal33, doubleVal34, doubleVal35,*/ strVal1, strVal2);
    }
}