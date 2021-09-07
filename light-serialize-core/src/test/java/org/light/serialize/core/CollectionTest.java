package org.light.serialize.core;

import org.junit.Test;
import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @date 2021/01/16
 */
public class CollectionTest {

    @Test
    public void testList() throws IOException {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        byte[] data = SerializeUtil.serializeToBytes(list);
        List<Integer> deserializedMe = (List<Integer>)SerializeUtil.deserialize(data);
        System.out.println(deserializedMe.toString());

    }
    @Test
    public void testSet() throws IOException {
        Set<Integer> set = new HashSet<>();
        set.add(11);
        set.add(22);
        set.add(33);
        byte[] data = SerializeUtil.serializeToBytes(set);
        Set<Integer>  deserializedMe = (Set<Integer> )SerializeUtil.deserialize(data);
         System.out.println(deserializedMe.toString());

    }

}
