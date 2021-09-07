package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.util.TypeResolver;

import java.io.IOException;
import java.util.Locale;

/**
 * LocaleSerializer
 *
 * @author alex
 */
public class LocaleSerializer extends Serializer<Locale> {

    /**
     * Missing constants in {@link java.util.Locale} for common locale.
     */
    public static final Locale SPANISH = new Locale("es", "", "");
    public static final Locale SPAIN = new Locale("es", "ES", "");

    public LocaleSerializer() {
        super(Locale.class);
    }

    @Override
    public void write(ObjectOutput output, Locale value) throws IOException {
        output.writeString(value.getLanguage());
        output.writeString(value.getCountry());
        output.writeString(value.getVariant());
        output.writeString(value.getScript());
    }

    @Override
    public Locale read(ObjectInput input) throws IOException {
        String language = input.readString();
        String country = input.readString();
        String variant = input.readString();
        String script = input.readString();

        return create(language, country, variant, script);
    }

    protected Locale create(String language, String country, String variant, String script) {
        Locale defaultLocale = Locale.getDefault();

        if (isSameLocale(defaultLocale, language, country, variant)) {
            return defaultLocale;
        }

        /**
         * Fast-paths for constants declared in java.util.Locale :
         * 1. "US" locale (typical forced default in many applications)
         */
        if (defaultLocale != Locale.US && isSameLocale(Locale.US, language, country, variant)) {
            return Locale.US;
        }

        // 2. Language-only constant locales
        if (isSameLocale(Locale.ENGLISH, language, country, variant)) return Locale.ENGLISH;
        if (isSameLocale(Locale.GERMAN, language, country, variant)) return Locale.GERMAN;
        if (isSameLocale(SPANISH, language, country, variant)) return SPANISH;
        if (isSameLocale(Locale.FRENCH, language, country, variant)) return Locale.FRENCH;
        if (isSameLocale(Locale.ITALIAN, language, country, variant)) return Locale.ITALIAN;
        if (isSameLocale(Locale.JAPANESE, language, country, variant)) return Locale.JAPANESE;
        if (isSameLocale(Locale.KOREAN, language, country, variant)) return Locale.KOREAN;
        if (isSameLocale(Locale.SIMPLIFIED_CHINESE, language, country, variant)) return Locale.SIMPLIFIED_CHINESE;
        if (isSameLocale(Locale.CHINESE, language, country, variant)) return Locale.CHINESE;
        if (isSameLocale(Locale.TRADITIONAL_CHINESE, language, country, variant)) return Locale.TRADITIONAL_CHINESE;
        // 2. Language with Country constant locales
        if (isSameLocale(Locale.UK, language, country, variant)) return Locale.UK;
        if (isSameLocale(Locale.GERMANY, language, country, variant)) return Locale.GERMANY;
        if (isSameLocale(SPAIN, language, country, variant)) return SPAIN;
        if (isSameLocale(Locale.FRANCE, language, country, variant)) return Locale.FRANCE;
        if (isSameLocale(Locale.ITALY, language, country, variant)) return Locale.ITALY;
        if (isSameLocale(Locale.JAPAN, language, country, variant)) return Locale.JAPAN;
        if (isSameLocale(Locale.KOREA, language, country, variant)) return Locale.KOREA;
        // if (isSameLocale(Locale.CHINA, language, country, variant)) // CHINA==SIMPLIFIED_CHINESE, see Locale.java
        // return Locale.CHINA;
        // if (isSameLocale(Locale.PRC, language, country, variant)) // PRC==SIMPLIFIED_CHINESE, see Locale.java
        // return Locale.PRC;
        // if (isSameLocale(Locale.TAIWAN, language, country, variant)) // TAIWAN==SIMPLIFIED_CHINESE, see Locale.java
        // return Locale.TAIWAN;
        if (isSameLocale(Locale.CANADA, language, country, variant)) return Locale.CANADA;
        if (isSameLocale(Locale.CANADA_FRENCH, language, country, variant)) return Locale.CANADA_FRENCH;

        return new Locale(language, country, variant);
    }

    protected static boolean isSameLocale(Locale locale, String language, String country, String variant) {
        return (locale.getLanguage().equals(language) && locale.getCountry().equals(country)
                && locale.getVariant().equals(variant));
    }
}
