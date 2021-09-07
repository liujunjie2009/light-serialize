package org.light.serialize.core.constants;

import org.light.serialize.core.serializer.java.CalendarSerializer;
import org.light.serialize.core.serializer.java.ClassSerializer;
import org.light.serialize.core.serializer.java.LambdaSerializer;
import org.light.serialize.core.serializer.java.ProxySerializer;

import java.util.GregorianCalendar;

/**
 * Constants
 *
 * @author alex
 */
public class Constants {

    private Constants() {
    }

    public static final int NULL_INDEX = -1;

    public static final Byte BYTE_0_VAL                            = new Byte((byte) 0);
    public static final Byte BYTE_1_VAL                            = new Byte((byte) 1);
    public static final Byte BYTE_N_1_VAL                          = new Byte((byte) -1);


    public static final Short SHORT_0_VAL                          = new Short((short) 0);
    public static final Short SHORT_1_VAL                          = new Short((short) 1);
    public static final Short SHORT_N_1_VAL                        = new Short((short) -1);


    public static final Integer INT_N_16_VAL                       = new Integer(-16);
    public static final Integer INT_N_15_VAL                       = new Integer(-15);
    public static final Integer INT_N_14_VAL                       = new Integer(-14);
    public static final Integer INT_N_13_VAL                       = new Integer(-13);
    public static final Integer INT_N_12_VAL                       = new Integer(-12);
    public static final Integer INT_N_11_VAL                       = new Integer(-11);
    public static final Integer INT_N_10_VAL                       = new Integer(-10);
    public static final Integer INT_N_9_VAL                        = new Integer(-9);
    public static final Integer INT_N_8_VAL                        = new Integer(-8);
    public static final Integer INT_N_7_VAL                        = new Integer(-7);
    public static final Integer INT_N_6_VAL                        = new Integer(-6);
    public static final Integer INT_N_5_VAL                        = new Integer(-5);
    public static final Integer INT_N_4_VAL                        = new Integer(-4);
    public static final Integer INT_N_3_VAL                        = new Integer(-3);
    public static final Integer INT_N_2_VAL                        = new Integer(-2);
    public static final Integer INT_N_1_VAL                        = new Integer(-1);
    public static final Integer INT_0_VAL                          = new Integer(0);
    public static final Integer INT_1_VAL                          = new Integer(1);
    public static final Integer INT_2_VAL                          = new Integer(2);
    public static final Integer INT_3_VAL                          = new Integer(3);
    public static final Integer INT_4_VAL                          = new Integer(4);
    public static final Integer INT_5_VAL                          = new Integer(5);
    public static final Integer INT_6_VAL                          = new Integer(6);
    public static final Integer INT_7_VAL                          = new Integer(7);
    public static final Integer INT_8_VAL                          = new Integer(8);
    public static final Integer INT_9_VAL                          = new Integer(9);
    public static final Integer INT_10_VAL                         = new Integer(10);
    public static final Integer INT_11_VAL                         = new Integer(11);
    public static final Integer INT_12_VAL                         = new Integer(12);
    public static final Integer INT_13_VAL                         = new Integer(13);
    public static final Integer INT_14_VAL                         = new Integer(14);
    public static final Integer INT_15_VAL                         = new Integer(15);
    public static final Integer INT_16_VAL                         = new Integer(16);
    public static final Integer INT_17_VAL                         = new Integer(17);
    public static final Integer INT_18_VAL                         = new Integer(18);
    public static final Integer INT_19_VAL                         = new Integer(19);
    public static final Integer INT_20_VAL                         = new Integer(20);
    public static final Integer INT_21_VAL                         = new Integer(21);
    public static final Integer INT_22_VAL                         = new Integer(22);
    public static final Integer INT_23_VAL                         = new Integer(23);
    public static final Integer INT_24_VAL                         = new Integer(24);
    public static final Integer INT_25_VAL                         = new Integer(25);
    public static final Integer INT_26_VAL                         = new Integer(26);
    public static final Integer INT_27_VAL                         = new Integer(27);
    public static final Integer INT_28_VAL                         = new Integer(28);
    public static final Integer INT_29_VAL                         = new Integer(29);
    public static final Integer INT_30_VAL                         = new Integer(30);
    public static final Integer INT_31_VAL                         = new Integer(31);
    public static final Integer INT_32_VAL                         = new Integer(32);
    public static final Integer INT_33_VAL                         = new Integer(33);
    public static final Integer INT_34_VAL                         = new Integer(34);
    public static final Integer INT_35_VAL                         = new Integer(35);
    public static final Integer INT_36_VAL                         = new Integer(36);
    public static final Integer INT_37_VAL                         = new Integer(37);
    public static final Integer INT_38_VAL                         = new Integer(38);
    public static final Integer INT_39_VAL                         = new Integer(39);
    public static final Integer INT_40_VAL                         = new Integer(40);
    public static final Integer INT_41_VAL                         = new Integer(41);
    public static final Integer INT_42_VAL                         = new Integer(42);
    public static final Integer INT_43_VAL                         = new Integer(43);
    public static final Integer INT_44_VAL                         = new Integer(44);
    public static final Integer INT_45_VAL                         = new Integer(45);
    public static final Integer INT_46_VAL                         = new Integer(46);
    public static final Integer INT_47_VAL                         = new Integer(47);


    public static final Long LONG_N_4_VAL                          = new Long(-4);
    public static final Long LONG_N_3_VAL                          = new Long(-3);
    public static final Long LONG_N_2_VAL                          = new Long(-2);
    public static final Long LONG_N_1_VAL                          = new Long(-1);
    public static final Long LONG_0_VAL                            = new Long(0);
    public static final Long LONG_1_VAL                            = new Long(1);
    public static final Long LONG_2_VAL                            = new Long(2);
    public static final Long LONG_3_VAL                            = new Long(3);
    public static final Long LONG_4_VAL                            = new Long(4);
    public static final Long LONG_5_VAL                            = new Long(5);
    public static final Long LONG_6_VAL                            = new Long(6);
    public static final Long LONG_7_VAL                            = new Long(7);
    public static final Long LONG_8_VAL                            = new Long(8);
    public static final Long LONG_9_VAL                            = new Long(9);
    public static final Long LONG_10_VAL                           = new Long(10);
    public static final Long LONG_11_VAL                           = new Long(11);


    public static final Float FLOAT_0_VAL                          = new Float(0);
    public static final Float FLOAT_1_VAL                          = new Float(1);
    public static final Float FLOAT_N_1_VAL                        = new Float(-1);


    public static final Double DOUBLE_0_VAL                        = new Double(0);
    public static final Double DOUBLE_1_VAL                        = new Double(1);
    public static final Double DOUBLE_N_1_VAL                      = new Double(-1);

    public static final String EMPTY_STRING_VAL                    = "";


    public static final CalendarSerializer GREGORIAN_CALENDAR_SERIALIZER = new CalendarSerializer(GregorianCalendar.class);
    public static final ClassSerializer CLASS_SERIALIZER = new ClassSerializer();
    public static final LambdaSerializer LAMBDA_SERIALIZER = new LambdaSerializer();
    public static final ProxySerializer PROXY_SERIALIZER = new ProxySerializer();

}
