package org.light.serialize.core.serializer.java;

import org.light.serialize.core.constants.TagId;
import org.light.serialize.core.instantiator.ObjectInstantiator;
import org.light.serialize.core.instantiator.sun.UnSafeInstantiator;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.serializer.SerializerFactory;
import org.light.serialize.core.util.ReflectUtil;
import org.light.serialize.core.util.TypeResolver;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.Date;

/**
 * TimeSerializers
 *
 * @author alex
 */
public class TimeSerializers {

    public static final DateSerializer DATE_SERIALIZER = new DateSerializer(Date.class);

    /**
     * The serialize for {@link Date}, {@link java.sql.Date}, {@link Time}, {@link Timestamp} and any other subclass.
     */
    public static class DateSerializer extends Serializer<Date> {

        private final ObjectInstantiator instantiator;

        public DateSerializer(Class<? extends Date> type) {
            super(type);
            this.instantiator = new UnSafeInstantiator(type);
        }

        @Override
        public void write(ObjectOutput output, Date value) throws IOException {
            output.writeLong(value.getTime());
        }

        @Override
        public Date read(ObjectInput input) throws IOException {
            long time = input.readLong();

            if (type == Date.class) {
                return new Date(time);
            }

            if (type == Timestamp.class) {
                return new Timestamp(time);
            }

            if (type == java.sql.Date.class) {
                return new java.sql.Date(time);
            }

            if (type == Time.class) {
                return new Time(time);
            }

            try {
                Constructor<? extends Date> constructor = (Constructor<? extends Date>) ReflectUtil.getAccessibleConstructor(type, long.class);
                return constructor.newInstance(time);
            } catch (Throwable ex) {
                Date d = (Date) instantiator.newInstance();
                d.setTime(time);
                return d;
            }
        }
    }

    /**
     * The serializer for {@link java.time.Duration}.
     */
    public static class DurationSerializer extends Serializer<Duration> {

        public DurationSerializer() {
            super(Duration.class);
        }

        @Override
        public void write(ObjectOutput output, Duration value) throws IOException {
            output.writeZigzagVarLong(value.getSeconds());
            output.writeZigzagVarInt(value.getNano());
        }

        @Override
        public Duration read(ObjectInput input) throws IOException {
            long seconds = input.readZigzagVarLong();
            int nanos = input.readZigzagVarInt();
            return Duration.ofSeconds(seconds, nanos);
        }
    }

    /**
     * The serializer for {@link java.time.Instant}.
     */
    public static class InstantSerializer extends Serializer<Instant> {

        public InstantSerializer() {
            super(Instant.class);
        }

        @Override
        public void write(ObjectOutput output, Instant value) throws IOException {
            output.writeVarLong(value.getEpochSecond());
            output.writeVarInt(value.getNano());
        }

        @Override
        public Instant read(ObjectInput input) throws IOException {
            long seconds = input.readVarLong();
            int nanos = input.readVarInt();
            return Instant.ofEpochSecond(seconds, nanos);
        }
    }

    /**
     * The serializer for {@link java.time.LocalDate}.
     */
    public static class LocalDateSerializer extends Serializer<LocalDate> {

        public LocalDateSerializer() {
            super(LocalDate.class);
        }

        @Override
        public void write(ObjectOutput output, LocalDate value) throws IOException {
            doWrite(output, value);
        }

        @Override
        public LocalDate read(ObjectInput input) throws IOException {
            return doRead(input);
        }

        static void doWrite (ObjectOutput out, LocalDate date) {
            out.writeVarInt(date.getYear());
            out.writeByte(date.getMonthValue());
            out.writeByte(date.getDayOfMonth());
        }

        static LocalDate doRead (ObjectInput in) {
            int year = in.readVarInt();
            int month = in.readByte();
            int dayOfMonth = in.readByte();
            return LocalDate.of(year, month, dayOfMonth);
        }
    }

    /**
     * The serializer for {@link java.time.LocalDateTime}.
     */
    public static class LocalDateTimeSerializer extends Serializer<LocalDateTime> {

        public LocalDateTimeSerializer() {
            super(LocalDateTime.class);
        }

        @Override
        public void write(ObjectOutput output, LocalDateTime value) throws IOException {
            LocalDateSerializer.doWrite(output, value.toLocalDate());
            LocalTimeSerializer.doWrite(output, value.toLocalTime());
        }

        @Override
        public LocalDateTime read(ObjectInput input) throws IOException {
            LocalDate date = LocalDateSerializer.doRead(input);
            LocalTime time = LocalTimeSerializer.doRead(input);
            return LocalDateTime.of(date, time);
        }
    }

    /**
     * The serializer for {@link java.time.LocalTime}.
     */
    public static class LocalTimeSerializer extends Serializer<LocalTime> {

        public LocalTimeSerializer() {
            super(LocalTime.class);
        }

        @Override
        public void write(ObjectOutput output, LocalTime value) throws IOException {
            doWrite(output, value);
        }

        @Override
        public LocalTime read(ObjectInput input) throws IOException {
            return doRead(input);
        }

        public static void doWrite (ObjectOutput out, LocalTime time) {
            if (time.getNano() == 0) {
                if (time.getSecond() == 0) {
                    if (time.getMinute() == 0) {
                        out.writeByte(~time.getHour());
                    } else {
                        out.writeByte(time.getHour());
                        out.writeByte(~time.getMinute());
                    }
                } else {
                    out.writeByte(time.getHour());
                    out.writeByte(time.getMinute());
                    out.writeByte(~time.getSecond());
                }
            } else {
                out.writeByte(time.getHour());
                out.writeByte(time.getMinute());
                out.writeByte(time.getSecond());
                out.writeVarInt(time.getNano());
            }
        }

        public static LocalTime doRead (ObjectInput in) {
            int hour = in.readByte();
            int minute = 0;
            int second = 0;
            int nano = 0;
            if (hour < 0) {
                hour = ~hour;
            } else {
                minute = in.readByte();
                if (minute < 0) {
                    minute = ~minute;
                } else {
                    second = in.readByte();
                    if (second < 0) {
                        second = ~second;
                    } else {
                        nano = in.readVarInt();
                    }
                }
            }

            return LocalTime.of(hour, minute, second, nano);
        }
    }

    /**
     * The serializer for {@link java.time.ZoneOffset}.
     */
    public static class ZoneOffsetSerializer extends Serializer<ZoneOffset> {

        public ZoneOffsetSerializer() {
            super(ZoneOffset.class);
        }

        @Override
        public void write(ObjectOutput output, ZoneOffset value) throws IOException {
            doWrite(output, value);
        }

        @Override
        public ZoneOffset read(ObjectInput input) throws IOException {
            return doRead(input);
        }

        static void doWrite (ObjectOutput out, ZoneOffset value) {
            final int offsetSecs = value.getTotalSeconds();
            int offsetByte = offsetSecs % 900 == 0 ? offsetSecs / 900 : 127;  // compress to -72 to +72
            out.writeByte(offsetByte);
            if (offsetByte == 127) {
                out.writeZigzagVarInt(offsetSecs);
            }
        }

        static ZoneOffset doRead (ObjectInput in) {
            int offsetByte = in.readByte();
            return (offsetByte == 127 ? ZoneOffset.ofTotalSeconds(in.readZigzagVarInt()) : ZoneOffset.ofTotalSeconds(offsetByte * 900));

        }
    }

    /**
     * The serializer for {@link java.time.ZoneId}.
     */
    public static class ZoneIdSerializer extends Serializer<ZoneId> {

        public ZoneIdSerializer(Class<?> type) {
            super(type);
        }

        @Override
        public void write(ObjectOutput output, ZoneId value) throws IOException {
            doWrite(output, value);
        }

        @Override
        public ZoneId read(ObjectInput input) throws IOException {
            return doRead(input);
        }

        static void doWrite(ObjectOutput out, ZoneId obj) {
            out.writeString(obj.getId());
        }

        static ZoneId doRead(ObjectInput in) {
            return ZoneId.of(in.readString());
        }
    }

    /**
     * The serializer for {@link java.time.OffsetTime}.
     */
    public static class OffsetTimeSerializer extends Serializer<OffsetTime> {

        public OffsetTimeSerializer() {
            super(OffsetTime.class);
        }

        @Override
        public void write(ObjectOutput output, OffsetTime value) throws IOException {
            LocalTimeSerializer.doWrite(output, value.toLocalTime());
            ZoneOffsetSerializer.doWrite(output, value.getOffset());
        }

        @Override
        public OffsetTime read(ObjectInput input) throws IOException {
            LocalTime time = LocalTimeSerializer.doRead(input);
            ZoneOffset offset = ZoneOffsetSerializer.doRead(input);
            return OffsetTime.of(time, offset);
        }
    }

    /**
     * The serializer for {@link java.time.OffsetDateTime}.
     */
    public static class OffsetDateTimeSerializer extends Serializer<OffsetDateTime> {

        public OffsetDateTimeSerializer() {
            super(OffsetDateTime.class);
        }

        @Override
        public void write(ObjectOutput output, OffsetDateTime value) throws IOException {
            LocalDateSerializer.doWrite(output, value.toLocalDate());
            LocalTimeSerializer.doWrite(output, value.toLocalTime());
            ZoneOffsetSerializer.doWrite(output, value.getOffset());
        }

        @Override
        public OffsetDateTime read(ObjectInput input) throws IOException {
            LocalDate date = LocalDateSerializer.doRead(input);
            LocalTime time = LocalTimeSerializer.doRead(input);
            ZoneOffset offset = ZoneOffsetSerializer.doRead(input);
            return OffsetDateTime.of(date, time, offset);
        }
    }

    /**
     * The serializer for {@link java.time.ZonedDateTime}.
     */
    public static class ZonedDateTimeSerializer extends Serializer<ZonedDateTime> {

        public ZonedDateTimeSerializer() {
            super(ZonedDateTime.class);
        }

        @Override
        public void write(ObjectOutput output, ZonedDateTime value) throws IOException {
            LocalDateSerializer.doWrite(output, value.toLocalDate());
            LocalTimeSerializer.doWrite(output, value.toLocalTime());
            ZoneIdSerializer.doWrite(output, value.getZone());
        }

        @Override
        public ZonedDateTime read(ObjectInput input) throws IOException {
            LocalDate date = LocalDateSerializer.doRead(input);
            LocalTime time = LocalTimeSerializer.doRead(input);
            ZoneId zone = ZoneIdSerializer.doRead(input);
            return ZonedDateTime.of(date, time, zone);
        }
    }

    /**
     * The serializer for {@link java.time.Year}.
     */
    public static class YearSerializer extends Serializer<Year> {

        public YearSerializer() {
            super(Year.class);
        }

        @Override
        public void write(ObjectOutput output, Year value) throws IOException {
            output.writeVarInt(value.getValue());
        }

        @Override
        public Year read(ObjectInput input) throws IOException {
            return Year.of(input.readVarInt());
        }
    }

    /**
     * The serializer for {@link java.time.YearMonth}.
     */
    public static class YearMonthSerializer extends Serializer<YearMonth> {

        public YearMonthSerializer() {
            super(YearMonth.class);
        }

        @Override
        public void write(ObjectOutput output, YearMonth value) throws IOException {
            output.writeVarInt(value.getYear());
            output.writeByte(value.getMonthValue());
        }

        @Override
        public YearMonth read(ObjectInput input) throws IOException {
            int year = input.readVarInt();
            byte month = input.readByte();
            return YearMonth.of(year, month);
        }
    }

    /**
     * The serializer for {@link java.time.MonthDay}.
     */
    public static class MonthDaySerializer extends Serializer<MonthDay> {

        public MonthDaySerializer() {
            super(MonthDay.class);
        }

        @Override
        public void write(ObjectOutput output, MonthDay value) throws IOException {
            output.writeByte(value.getMonthValue());
            output.writeByte(value.getDayOfMonth());
        }

        @Override
        public MonthDay read(ObjectInput input) throws IOException {
            byte month = input.readByte();
            byte day = input.readByte();
            return MonthDay.of(month, day);
        }
    }

    /**
     * The serializer for {@link java.time.Period}.
     */
    public static class PeriodSerializer extends Serializer<Period> {

        public PeriodSerializer() {
            super(Period.class);
        }

        @Override
        public void write(ObjectOutput output, Period value) throws IOException {
            output.writeVarInt(value.getYears());
            output.writeVarInt(value.getMonths());
            output.writeVarInt(value.getDays());
        }

        @Override
        public Period read(ObjectInput input) throws IOException {
            int years = input.readVarInt();
            int months = input.readVarInt();
            int days = input.readVarInt();
            return Period.of(years, months, days);
        }
    }

    public static void register(SerializerFactory factory) {
        factory.register(DATE_SERIALIZER);
        factory.register(new DateSerializer(java.sql.Date.class));
        factory.register(new DateSerializer(Time.class));
        factory.register(new DateSerializer(Timestamp.class));

        factory.register(new DurationSerializer());
        factory.register(new InstantSerializer());
        factory.register(new LocalDateSerializer());
        factory.register(new LocalTimeSerializer());
        factory.register(new LocalDateTimeSerializer());
        factory.register(new ZoneOffsetSerializer());
        factory.register(new ZoneIdSerializer(ZoneId.class));
        factory.register(new ZoneIdSerializer(ReflectUtil.loadClass("java.time.ZoneRegion")));
        factory.register(new OffsetTimeSerializer());
        factory.register(new OffsetDateTimeSerializer());
        factory.register(new ZonedDateTimeSerializer());
        factory.register(new YearSerializer());
        factory.register(new YearMonthSerializer());
        factory.register(new MonthDaySerializer());
        factory.register(new PeriodSerializer());
    }

}
