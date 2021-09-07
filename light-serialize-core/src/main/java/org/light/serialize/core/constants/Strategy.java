package org.light.serialize.core.constants;

/**
 * Serializer strategy.
 *
 * @author alex
 */
public enum Strategy {

    /**
     * Match each field exactly.
     */
    EXACT,

    /**
     * Match by field order.
     */
    ORDER,

    /**
     * Match by field name.
     */
    NAME,
    ;

    public static Strategy getDefault() {
        return NAME;
    }

}
