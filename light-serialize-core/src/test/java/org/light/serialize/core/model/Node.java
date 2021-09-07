package org.light.serialize.core.model;

/**
 * @date 2020/12/13
 */
public class Node {
    private int id;
    private Node pre;
    private Node next;
    private InnerNode innerNode;

    public Node() {
    }

    public Node(int id, Node pre, Node next) {
        this.id = id;
        this.pre = pre;
        this.next = next;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Node getPre() {
        return pre;
    }

    public void setPre(Node pre) {
        this.pre = pre;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public InnerNode getInnerNode() {
        return innerNode;
    }

    public void setInnerNode(InnerNode innerNode) {
        this.innerNode = innerNode;
    }

    public static class InnerNode {

        private Node node1;
        private Node node2;

        public Node getNode1() {
            return node1;
        }

        public void setNode1(Node node1) {
            this.node1 = node1;
        }

        public Node getNode2() {
            return node2;
        }

        public void setNode2(Node node2) {
            this.node2 = node2;
        }

    }
}
