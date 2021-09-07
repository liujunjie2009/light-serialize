package org.light.serialize.core.serializer.java;

import junit.framework.TestCase;
import org.junit.Assert;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.util.ReflectUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * CollectionSerializers test
 * TODO:检查Collections   ArrayList 子类序列化测试？
 * // TODO:list set  EnumSet Collection 子类？
 * TODO: AraysAsList 嵌套
 * TODO:测试继承
 * @author alex
 */
public class CollectionSerializersTest extends TestCase {

    public void testCollectionSerializers() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        MyCollectionClass myCollectionClass = new MyCollectionClass();
        output.writeObject(myCollectionClass);

        Object actual = input.readObject();
        System.out.println(actual.equals(myCollectionClass));
        Assert.assertEquals(myCollectionClass, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());
    }

}

class MyCollectionClass {

    private MyCollectionClass me = this;

    private ArrayList<Object> c1 = new ArrayList<>(16);
    private LinkedList<Object> c2 = new LinkedList<>();
    private Vector<Object> c3 = new Vector<>(16);
    private List<Object> c4 = Arrays.asList("1", 127);
    private List<Object> c5 = Collections.nCopies(100, "hello");
    private List<Object> c6 = c1.subList(0, 0);
    private HashSet<Object> c7 = new HashSet<>(16);
    private LinkedHashSet<Object> c8 = new LinkedHashSet<>(16);
    private Set<Object> c9 = Collections.newSetFromMap(new HashMap<>());
    private Set<Object> c10 = Collections.emptySet();
    private NavigableSet<Object> c11 = Collections.emptyNavigableSet();
    private List<Object> c12 = Collections.emptyList();
    private List<Object> c13 = Collections.singletonList("hello");
    private Set<Object> c14 = Collections.singleton("hello");

    private Set<Object> c15 = Collections.unmodifiableNavigableSet(c11);
    private Set<Object> c16 = Collections.unmodifiableSet(c9);
    private Set<Object> c17 = Collections.unmodifiableSortedSet(c11);
    private Collection<Object> c18 = Collections.unmodifiableCollection(c1);
    private List<Object> c19 = Collections.unmodifiableList(c4);

    private Set<Object> c20 = Collections.synchronizedNavigableSet(c11);
    private Set<Object> c21 = Collections.synchronizedSet(c11);
    private Set<Object> c22 = Collections.synchronizedSortedSet(c11);
    private Collection<Object> c23 = Collections.synchronizedCollection(c11);
    private List<Object> c24 = Collections.synchronizedList(c12);

    private Set<Object> c25 = Collections.checkedNavigableSet(c11, Object.class);
    private Set<Object> c26 = Collections.checkedSet(c11, Object.class);
    private Set<Object> c27 = Collections.checkedSortedSet(c11, Object.class);
    private Collection<Object> c28 = Collections.checkedCollection(c11, Object.class);
    // private List<Object> c29 = Collections.checkedList(c13, Object.class);
    // private Queue<Object> c30 = Collections.checkedQueue(c2, Object.class);

    // private EnumSet<TimeUnit> c31 = EnumSet.allOf(TimeUnit.class);
    // private EnumSet<MyEnum> c32 = EnumSet.allOf(MyEnum.class);
    //
    // private PriorityQueue<Object> c33 = new PriorityQueue(Comparator.naturalOrder());
    // private BitSet c34 = new BitSet();
    // private TreeSet<Object> c35 = new TreeSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyCollectionClass that = (MyCollectionClass) o;
        return /*me == that.me &&*/
                Objects.equals(c1, that.c1) &&
                Objects.equals(c2, that.c2) &&
                Objects.equals(c3, that.c3) &&
                Objects.equals(c4, that.c4) &&
                Objects.equals(c5, that.c5) &&
                Objects.equals(c6, that.c6) &&
                Objects.equals(c7, that.c7) &&
                Objects.equals(c8, that.c8) &&
                Objects.equals(c9, that.c9) &&
                Objects.equals(c10, that.c10) &&
                Objects.equals(c11, that.c11) &&
                Objects.equals(c12, that.c12) &&
                Objects.equals(c13, that.c13) &&
                Objects.equals(c14, that.c14) &&
                Objects.equals(c15, that.c15) &&
                Objects.equals(c16, that.c16) &&
                Objects.equals(c17, that.c17) &&
                // Objects.equals(c18, that.c18) &&
                Objects.equals(c19, that.c19) &&
                Objects.equals(c20, that.c20) &&
                Objects.equals(c21, that.c21) &&
                Objects.equals(c22, that.c22) &&
                // Objects.equals(c23, that.c23) &&
                Objects.equals(c24, that.c24) &&
                Objects.equals(c25, that.c25) &&
                Objects.equals(c26, that.c26) &&
                Objects.equals(c27, that.c27) /*&&
                Objects.equals(c28, that.c28)*//* &&
                Objects.equals(c29, that.c29) &&
                Objects.equals(c30, that.c30)*/ /*&&
                Objects.equals(c31, that.c31) &&
                Objects.equals(c32, that.c32) &&
                Objects.equals(c33, that.c33) &&
                Objects.equals(c34, that.c34) &&
                Objects.equals(c35, that.c35)*/;
    }

    @Override
    public int hashCode() {
        return Objects.hash( c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27, c28/*, c29, c30*//*, c31, c32, c33, c34, c35*/);
    }
}

enum MyEnum {
    a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20,
    a21, a22, a23, a24, a25, a26, a27, a28, a29, a210, a211, a212, a213, a214, a215, a216, a217, a218, a219, a220,
    a31, a32, a33, a34, a35, a36, a37, a38, a39, a310, a311, a312, a313, a314, a315, a316, a317, a318, a319, a320,
    a41, a42, a43, a44, a45, a46, a47, a48, a49, a410, a411, a412, a413, a414, a415, a416, a417, a418, a419, a420,
    ;
}

class CollectionItem {

    Object o1;
    Object o2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionItem that = (CollectionItem) o;
        return Objects.equals(o1, that.o1) &&
                Objects.equals(o2, that.o2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(o1, o2);
    }
}