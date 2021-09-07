package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

/**
 * TODO:测试继承
 * MapSerializersTest
 *
 * @author alex
 */
public class MapSerializersTest {

    @Test
    public void testMapSerializers() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        MyMap myMap = new MyMap();
        output.writeObject(myMap);

        Object actual = input.readObject();
        Assert.assertEquals(myMap, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());
    }
}

class MyMap {

    private Map<Object, Object> o1 = new HashMap<>(16);
    private Map<Object, Object> o2 = new LinkedHashMap<>(16);
    private Map<Object, Object> o3 = new Hashtable<>(16);
    private Map<Object, Object> o4 = new ConcurrentHashMap<>(16);

    private Map<Object, Object> o5 = Collections.emptyMap();
    private Map<Object, Object> o6 = Collections.emptyNavigableMap();

    private Map<Object, Object> o7 = Collections.singletonMap("name", "alex");

    private Map<Object, Object> o8 = Collections.unmodifiableMap(o1);
    private Map<Object, Object> o9 = Collections.unmodifiableSortedMap(new TreeMap<>());
    private Map<Object, Object> o10 = Collections.unmodifiableNavigableMap(new TreeMap<>());

    private Map<String, String> o11 = Collections.checkedMap(new HashMap<>(), String.class, String.class);
    private Map<String, String> o12 = Collections.checkedSortedMap(new TreeMap<>(), String.class, String.class);
    private Map<String, String> o13 = Collections.checkedNavigableMap(new TreeMap<>(), String.class, String.class);

    private Map<String, String> o14 = Collections.synchronizedMap(new HashMap<>());
    private Map<String, String> o15 = Collections.synchronizedSortedMap(new TreeMap<>());
    private Map<String, String> o16 = Collections.synchronizedNavigableMap(new TreeMap<>());

    private TreeMap<String, String> o17 = new TreeMap<>();

    MapItem item = new MapItem(o15, o7);
    private Map<Object, Object> o18 = Collections.singletonMap(item, item);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyMap myMap = (MyMap) o;
        return Objects.equals(o1, myMap.o1) &&
                Objects.equals(o2, myMap.o2) &&
                Objects.equals(o3, myMap.o3) &&
                Objects.equals(o4, myMap.o4) &&
                Objects.equals(o5, myMap.o5) &&
                Objects.equals(o6, myMap.o6) &&
                Objects.equals(o7, myMap.o7) &&
                Objects.equals(o8, myMap.o8) &&
                Objects.equals(o9, myMap.o9) &&
                Objects.equals(o10, myMap.o10) &&
                Objects.equals(o11, myMap.o11) &&
                Objects.equals(o12, myMap.o12) &&
                Objects.equals(o13, myMap.o13) &&
                Objects.equals(o14, myMap.o14) &&
                Objects.equals(o15, myMap.o15) &&
                Objects.equals(o16, myMap.o16) &&
                Objects.equals(o17, myMap.o17) &&
                Objects.equals(item, myMap.item) &&
                Objects.equals(o18, myMap.o18);
    }

    @Override
    public int hashCode() {
        return Objects.hash(o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, item, o18);
    }
}

class MapItem {
    public Object o1;
    public Object o2;

    public MapItem() {
    }

    public MapItem(Object o1, Object o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapItem mapItem = (MapItem) o;
        return Objects.equals(o1, mapItem.o1) &&
                Objects.equals(o2, mapItem.o2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(o1, o2);
    }
}
