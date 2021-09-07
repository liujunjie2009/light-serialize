package org.light.serialize.core.serializer.java;

import org.light.serialize.core.constants.TagId;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * The serializer for {@link java.util.Calendar}.
 *
 * @author alex
 */
public class CalendarSerializer extends Serializer<Calendar> {

    public CalendarSerializer(Class<? extends Calendar> type) {
        super(type);
    }

    @Override
    public void write(ObjectOutput output, Calendar value) throws IOException {
        output.writeString(value.getTimeZone().getID());
        output.writeVarLong(value.getTimeInMillis());
        output.writeBool(value.isLenient());
        output.writeVarInt(value.getFirstDayOfWeek());
        output.writeVarInt(value.getMinimalDaysInFirstWeek());

        if (GregorianCalendar.class.isAssignableFrom(type)) {
            output.writeZigzagVarLong(((GregorianCalendar) value).getGregorianChange().getTime());
        }
    }

    @Override
    public Calendar read(ObjectInput input) throws IOException {
        boolean isGregorian = GregorianCalendar.class.isAssignableFrom(type);
        Calendar calendar = isGregorian? new GregorianCalendar() : Calendar.getInstance();

        /*
         * check if we actually need to set the timezone, as
         * TimeZone.getTimeZone is synchronized.
         */
        String timeZoneId = input.readString();
        if (!calendar.getTimeZone().getID().equals(timeZoneId)) {
            calendar.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        }

        calendar.setTimeInMillis(input.readVarLong());
        calendar.setLenient(input.readBool());
        calendar.setFirstDayOfWeek(input.readVarInt());
        calendar.setMinimalDaysInFirstWeek(input.readVarInt());

        if (isGregorian) {
            ((GregorianCalendar) calendar).setGregorianChange(new Date(input.readZigzagVarLong()));
        }

        return calendar;
    }
}
