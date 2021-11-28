package org.light.serialize.core.model;

import java.io.Serializable;
import java.util.*;

/**
 * Simple
 */
public class Simple extends SimpleParent implements Serializable {
//    private boolean booVal1 = false;
//    private boolean booVal2 = true;
//
//    private byte byteVal1 = 0;
//    private byte byteVal2 = -1;
//    private byte byteVal3 = 1;
//    private byte byteVal4 = Byte.MIN_VALUE;
//    private byte byteVal5 = Byte.MAX_VALUE;
//
//    private short shortVal1 = 0;
//    private short shortVal2 = -1;
//    private short shortVal3 = 1;
//    private short shortVal4 = Short.MIN_VALUE;
//    private short shortVal5 = Short.MAX_VALUE;
//
//    private int intVal1 = 0;
//    private int intVal2 = -1;
//    private int intVal3 = 1;
//    private int intVal4 = Integer.MIN_VALUE;
//    private int intVal5 = Integer.MAX_VALUE;
//
//    private long longVal1 = 0;
//    private long longVal2 = -1;
//    private long longVal3 = 1;
//    private long longVal4 = Long.MIN_VALUE;
//    private long longVal5 = Long.MAX_VALUE;
//
//    private float floatVal1 = -1;
//    private float floatVal2 = 0;
//    private float floatVal3 = 1;
//    private float floatVal4 = Float.MAX_VALUE;
//    private float floatVal5 = Float.MIN_VALUE;
//    private float floatVal6 = Float.NaN;
//
//    private double doubleVal1 = -1;
//    private double doubleVal2 = 0;
//    private double doubleVal3 = 1;
//    private double doubleVal4 = Double.MAX_VALUE;
//    private double doubleVal5 = Double.NaN;
//    private double doubleVal6 = Double.MIN_VALUE;
//
//    private String strVal = "hello son";

    private List<Object> list = new ArrayList<>();
//    private Set<Object> set = new HashSet<>();
//    private Map<Object, Object> map = new HashMap<>();
//    {
//        map.put("name", "lsl");
//    }

    public void outStr() {
//        System.out.println("this:" + this.strVal);
//        System.out.println("parent:" + super.strVal);
    }
//
//    @Override
//    public String toString() {
//        return "Simple{" +
//                "booVal1=" + booVal1 +
//                ", booVal2=" + booVal2 +
//                ", byteVal1=" + byteVal1 +
//                ", byteVal2=" + byteVal2 +
//                ", byteVal3=" + byteVal3 +
//                ", byteVal4=" + byteVal4 +
//                ", byteVal5=" + byteVal5 +
//                ", shortVal1=" + shortVal1 +
//                ", shortVal2=" + shortVal2 +
//                ", shortVal3=" + shortVal3 +
//                ", shortVal4=" + shortVal4 +
//                ", shortVal5=" + shortVal5 +
//                ", intVal1=" + intVal1 +
//                ", intVal2=" + intVal2 +
//                ", intVal3=" + intVal3 +
//                ", intVal4=" + intVal4 +
//                ", intVal5=" + intVal5 +
//                ", longVal1=" + longVal1 +
//                ", longVal2=" + longVal2 +
//                ", longVal3=" + longVal3 +
//                ", longVal4=" + longVal4 +
//                ", longVal5=" + longVal5 +
//                ", floatVal1=" + floatVal1 +
//                ", floatVal2=" + floatVal2 +
//                ", floatVal3=" + floatVal3 +
//                ", floatVal4=" + floatVal4 +
//                ", floatVal5=" + floatVal5 +
//                ", floatVal6=" + floatVal6 +
//                ", doubleVal1=" + doubleVal1 +
//                ", doubleVal2=" + doubleVal2 +
//                ", doubleVal3=" + doubleVal3 +
//                ", doubleVal4=" + doubleVal4 +
//                ", doubleVal5=" + doubleVal5 +
//                ", doubleVal6=" + doubleVal6 +
//                ", strVal='" + strVal + '\'' +
//                ", list=" + list +
//                ", set=" + set +
//                ", map=" + map +
//                '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Simple simple = (Simple) o;
//        return booVal1 == simple.booVal1 && booVal2 == simple.booVal2 && byteVal1 == simple.byteVal1 && byteVal2 == simple.byteVal2 && byteVal3 == simple.byteVal3 && byteVal4 == simple.byteVal4 && byteVal5 == simple.byteVal5 && shortVal1 == simple.shortVal1 && shortVal2 == simple.shortVal2 && shortVal3 == simple.shortVal3 && shortVal4 == simple.shortVal4 && shortVal5 == simple.shortVal5 && intVal1 == simple.intVal1 && intVal2 == simple.intVal2 && intVal3 == simple.intVal3 && intVal4 == simple.intVal4 && intVal5 == simple.intVal5 && longVal1 == simple.longVal1 && longVal2 == simple.longVal2 && longVal3 == simple.longVal3 && longVal4 == simple.longVal4 && longVal5 == simple.longVal5 && Float.compare(simple.floatVal1, floatVal1) == 0 && Float.compare(simple.floatVal2, floatVal2) == 0 && Float.compare(simple.floatVal3, floatVal3) == 0 && Float.compare(simple.floatVal4, floatVal4) == 0 && Float.compare(simple.floatVal5, floatVal5) == 0 && Float.compare(simple.floatVal6, floatVal6) == 0 && Double.compare(simple.doubleVal1, doubleVal1) == 0 && Double.compare(simple.doubleVal2, doubleVal2) == 0 && Double.compare(simple.doubleVal3, doubleVal3) == 0 && Double.compare(simple.doubleVal4, doubleVal4) == 0 && Double.compare(simple.doubleVal5, doubleVal5) == 0 && Double.compare(simple.doubleVal6, doubleVal6) == 0 && Objects.equals(strVal, simple.strVal) && Objects.equals(list, simple.list) && Objects.equals(set, simple.set) && Objects.equals(map, simple.map);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(booVal1, booVal2, byteVal1, byteVal2, byteVal3, byteVal4, byteVal5, shortVal1, shortVal2, shortVal3, shortVal4, shortVal5, intVal1, intVal2, intVal3, intVal4, intVal5, longVal1, longVal2, longVal3, longVal4, longVal5, floatVal1, floatVal2, floatVal3, floatVal4, floatVal5, floatVal6, doubleVal1, doubleVal2, doubleVal3, doubleVal4, doubleVal5, doubleVal6, strVal, list, set, map);
//    }
}


class SimpleParent {
//    protected String strVal = "hello parent";
//
//    public String getStrVal() {
//        return strVal;
//    }
//
//    public void setStrVal(String strVal) {
//        this.strVal = strVal;
//    }
//
//    @Override
//    public String toString() {
//        return "SimpleParent{" +
//                "strVal='" + strVal + '\'' +
//                '}';
//    }
}

