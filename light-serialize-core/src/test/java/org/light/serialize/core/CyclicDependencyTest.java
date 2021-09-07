package org.light.serialize.core;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.light.serialize.core.model.Node;
import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;

/**
 * @date 2020/12/19
 */
public class CyclicDependencyTest {

    @Test
    public void test() throws IOException {

        Node node = new Node(1, null, null);
        node.setNext(node);
        node.setPre(node);

        Node.InnerNode innerNode = new Node.InnerNode();
        innerNode.setNode1(node);
        innerNode.setNode2(node);

        node.setInnerNode(innerNode);

        byte[] data = SerializeUtil.serializeToBytes(node);

        Node deserializedMe = (Node)SerializeUtil.deserialize(data);
        System.out.println(JSON.toJSONString(deserializedMe));
    }
}
