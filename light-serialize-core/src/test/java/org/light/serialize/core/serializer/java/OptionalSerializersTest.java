package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;

/**
 * OptionalSerializers test
 *
 * @author alex
 */
public class OptionalSerializersTest {

    @Test
    public void testOptionalSerializers() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        OptionalClass optionalClass = new OptionalClass();
        output.writeObject(optionalClass);
        Object actual1 = input.readObject();
        Assert.assertEquals(optionalClass, actual1);

        Assert.assertEquals(0, output.buffer().readableBytes());
    }

}

class OptionalClass {
    OptionalItem item = new OptionalItem();
    Optional o = Optional.of(item);
    Optional o1 = Optional.of(TimeUnit.DAYS);
    Optional o2 = Optional.empty();
    OptionalInt o3 = OptionalInt.of(Integer.MAX_VALUE);
    OptionalInt o4 = OptionalInt.empty();
    OptionalLong o5 = OptionalLong.of(Integer.MAX_VALUE);
    OptionalLong o6 = OptionalLong.empty();

    public OptionalClass() {
        item.o1 = o;
    }

    @Override
    public boolean equals(Object o7) {
        if (this == o7) return true;
        if (o7 == null || getClass() != o7.getClass()) return false;
        OptionalClass that = (OptionalClass) o7;
        return Objects.equals(item, that.item) &&
                Objects.equals(o, that.o) &&
                Objects.equals(o1, that.o1) &&
                Objects.equals(o2, that.o2) &&
                Objects.equals(o3, that.o3) &&
                Objects.equals(o4, that.o4) &&
                Objects.equals(o5, that.o5) &&
                Objects.equals(o6, that.o6);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, o, o1, o2, o3, o4, o5, o6);
    }
}

class OptionalItem {
    Object o1;

    @Override
    public boolean equals(Object o) {
        return true;
        // if (this == o) return true;
        // if (o == null || getClass() != o.getClass()) return false;
        // OptionalItem that = (OptionalItem) o;
        // return Objects.equals(o1, that.o1);
    }

}