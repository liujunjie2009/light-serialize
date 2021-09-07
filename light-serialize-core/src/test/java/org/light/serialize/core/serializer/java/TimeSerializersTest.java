package org.light.serialize.core.serializer.java;

import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.io.WriteContext;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.Date;
import java.util.Objects;

/**
 * TimeSerializers test
 *
 * @author alex
 */
public class TimeSerializersTest {

    @Test
    public void test() throws IOException {
        ObjectOutput output = new ObjectOutput();
        ObjectInput input = new ObjectInput(output.buffer());

        TimeClass timeClass = new TimeClass();
        output.writeObject(timeClass);
        Object actual = input.readObject();
        Assert.assertEquals(timeClass, actual);
        Assert.assertEquals(0, output.buffer().readableBytes());
    }

}

class TimeClass {
    Object o1 = new Date();
    Object o2 = new java.sql.Date(System.currentTimeMillis());
    Object o3 = new Time(System.currentTimeMillis());
    Object o4 = new Timestamp(System.currentTimeMillis());
    Object o5 = Duration.ofMillis(System.currentTimeMillis());
    Object o6 = Instant.now();
    Object o7 = LocalDate.now() ;
    Object o8 = LocalTime.now() ;
    Object o9 = LocalDateTime.now() ;
    Object o10 = ZoneOffset.of("+8");
    Object o11 = ZoneId.systemDefault() ;
    Object o12 = OffsetTime.now() ;
    Object o13 = OffsetDateTime.now() ;
    Object o14 = ZonedDateTime.now();
    Object o15 = Year.now();
    Object o16 = YearMonth.now();
    Object o17 = MonthDay.now();
    Object o18 = Period.of(2000, 11, 11);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeClass timeClass = (TimeClass) o;
        return Objects.equals(o1, timeClass.o1) &&
                Objects.equals(o2, timeClass.o2) &&
                Objects.equals(o3, timeClass.o3) &&
                Objects.equals(o4, timeClass.o4) &&
                Objects.equals(o5, timeClass.o5) &&
                Objects.equals(o6, timeClass.o6) &&
                Objects.equals(o7, timeClass.o7) &&
                Objects.equals(o8, timeClass.o8) &&
                Objects.equals(o9, timeClass.o9) &&
                Objects.equals(o10, timeClass.o10) &&
                Objects.equals(o11, timeClass.o11) &&
                Objects.equals(o12, timeClass.o12) &&
                Objects.equals(o13, timeClass.o13) &&
                Objects.equals(o14, timeClass.o14) &&
                Objects.equals(o15, timeClass.o15) &&
                Objects.equals(o16, timeClass.o16) &&
                Objects.equals(o17, timeClass.o17) &&
                Objects.equals(o18, timeClass.o18);
    }

    @Override
    public int hashCode() {
        return Objects.hash(o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18);
    }
}
