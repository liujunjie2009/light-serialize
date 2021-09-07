package org.light.serialize.core.constants;

/**
 * Marks for serialization with a byte.
 *
 * @author alex
 */
public final class TagId {

    public static final int NULL                                   = -128;

    public static final int BOOL                                   = -127;
    public static final int BOOL_WRAPPER                           = -126;
    public static final int BOOL_TRUE                              = -125;
    public static final int BOOL_FALSE                             = -124;

    public static final int BYTE                                   = -123;
    public static final int BYTE_WRAPPER                           = -122;
    public static final int BYTE_0                                 = -121;
    public static final int BYTE_1                                 = -120;
    public static final int BYTE_N_1                               = -119;

    public static final int CHAR                                   = -118;
    public static final int CHAR_WRAPPER                           = -117;
    public static final int CHAR_ASCII                             = -116;

    public static final int SHORT                                  = -115;
    public static final int SHORT_WRAPPER                          = -114;
    public static final int SHORT_BYTE                             = -113;
    public static final int SHORT_REVERSE_VAR                      = -112;
    public static final int SHORT_COMPLEMENT_REVERSE_VAR           = -111;
    public static final int SHORT_0                                = -110;
    public static final int SHORT_1                                = -109;
    public static final int SHORT_N_1                              = -108;

    public static final int INT                                    = -107;
    public static final int INT_WRAPPER                            = -106;
    public static final int INT_REVERSE_VAR                        = -105;
    public static final int INT_COMPLEMENT_REVERSE_VAR             = -104;
    public static final int INT_N_16                               = -103;
    public static final int INT_47                                 = -40;
    public static final int INT_N_2048                             = -39;
    public static final int INT_2047                               = -24;
    public static final int INT_N_262144                           = -23;
    public static final int INT_262143                             = -16;
    public static final int INT_N_33554432                         = -15;
    public static final int INT_33554433                           = -12;

    public static final int LONG                                   = -11;
    public static final int LONG_WRAPPER                           = -10;
    public static final int LONG_REVERSE_VAR                       = -9;
    public static final int LONG_COMPLEMENT_REVERSE_VAR            = -8;
    public static final int LONG_N_4                               = -7;
    public static final int LONG_11                                = 8;
    public static final int LONG_N_1024                            = 9;
    public static final int LONG_1023                              = 16;
    public static final int LONG_N_131072                          = 17;
    public static final int LONG_131071                            = 20;
    public static final int LONG_N_16777216                        = 21;
    public static final int LONG_16777215                          = 22;
    public static final int LONG_INT                               = 23;
    public static final int LONG_5_BYTES                           = 24;
    public static final int LONG_6_BYTES                           = 25;
    public static final int LONG_7_BYTES                           = 26;


    public static final int FLOAT                                  = 27;
    public static final int FLOAT_WRAPPER                          = 28;
    public static final int FLOAT_0                                = 29;
    public static final int FLOAT_1                                = 30;
    public static final int FLOAT_N_1                              = 31;
    public static final int FLOAT_BYTE                             = 32;
    public static final int FLOAT_SHORT                            = 33;
    public static final int FLOAT_MILLI_VAR                        = 34;
    public static final int FLOAT_MILLI_COMPLEMENT_VAR             = 35;


    public static final int DOUBLE                                 = 36;
    public static final int DOUBLE_WRAPPER                         = 37;
    public static final int DOUBLE_0                               = 38;
    public static final int DOUBLE_1                               = 39;
    public static final int DOUBLE_N_1                             = 40;
    public static final int DOUBLE_BYTE                            = 41;
    public static final int DOUBLE_SHORT                           = 42;
    public static final int DOUBLE_MILLI_VAR                       = 43;
    public static final int DOUBLE_MILLI_COMPLEMENT_VAR            = 44;

    public static final int STRING                                 = 45;
    public static final int STRING_EMPTY                           = 46;

    public static final int OBJECT_REFERENCE                       = 47;
    public static final int OBJECT_REFERENCE_0                     = 48;
    public static final int OBJECT_REFERENCE_1                     = 49;
    public static final int OBJECT_REFERENCE_2                     = 50;
    public static final int OBJECT_REFERENCE_3                     = 51;
    public static final int OBJECT_REFERENCE_4                     = 52;
    public static final int OBJECT_REFERENCE_5                     = 53;
    public static final int OBJECT_REFERENCE_6                     = 54;
    public static final int OBJECT_REFERENCE_7                     = 55;
    public static final int OBJECT_REFERENCE_LAST_0                = 56;
    public static final int OBJECT_REFERENCE_LAST_1                = 57;
    public static final int OBJECT_REFERENCE_LAST_2                = 58;
    public static final int OBJECT_REFERENCE_LAST_3                = 59;
    public static final int OBJECT_REFERENCE_LAST_4                = 60;
    public static final int OBJECT_REFERENCE_LAST_5                = 61;
    public static final int OBJECT_REFERENCE_LAST_6                = 62;
    public static final int OBJECT_REFERENCE_LAST_7                = 63;

    public static final int TYPE_REFERENCE                         = 64;
    public static final int TYPE_REFERENCE_0                       = 65;
    public static final int TYPE_REFERENCE_1                       = 66;
    public static final int TYPE_REFERENCE_2                       = 67;
    public static final int TYPE_REFERENCE_3                       = 68;
    public static final int TYPE_REFERENCE_4                       = 69;
    public static final int TYPE_REFERENCE_5                       = 70;
    public static final int TYPE_REFERENCE_6                       = 71;
    public static final int TYPE_REFERENCE_7                       = 72;
    public static final int TYPE_REFERENCE_LAST_0                  = 73;
    public static final int TYPE_REFERENCE_LAST_1                  = 74;
    public static final int TYPE_REFERENCE_LAST_2                  = 75;
    public static final int TYPE_REFERENCE_LAST_3                  = 76;
    public static final int TYPE_REFERENCE_LAST_4                  = 77;
    public static final int TYPE_REFERENCE_LAST_5                  = 78;
    public static final int TYPE_REFERENCE_LAST_6                  = 79;
    public static final int TYPE_REFERENCE_LAST_7                  = 80;

    public static final int TYPE_REGISTERED                        = 81;
    public static final int TYPE_NAME                              = 82;

    public static final int VOID                                   = 83;
    public static final int VOID_WRAPPER                           = 84;
    public static final int CLASS                                  = 85;
    public static final int OBJECT                                 = 86;

    public static final int BOOL_ARRAY                             = 87;
    public static final int BOOL_WRAPPER_ARRAY                     = 88;
    public static final int BYTE_ARRAY                             = 89;
    public static final int BYTE_WRAPPER_ARRAY                     = 90;
    public static final int CHAR_ARRAY                             = 91;
    public static final int CHAR_WRAPPER_ARRAY                     = 92;
    public static final int SHORT_ARRAY                            = 93;
    public static final int SHORT_WRAPPER_ARRAY                    = 94;
    public static final int INT_ARRAY                              = 95;
    public static final int INT_WRAPPER_ARRAY                      = 96;
    public static final int LONG_ARRAY                             = 97;
    public static final int LONG_WRAPPER_ARRAY                     = 98;
    public static final int FLOAT_ARRAY                            = 99;
    public static final int FLOAT_WRAPPER_ARRAY                    = 100;
    public static final int DOUBLE_ARRAY                           = 101;
    public static final int DOUBLE_WRAPPER_ARRAY                   = 102;
    public static final int STRING_ARRAY                           = 103;
    public static final int CLASS_ARRAY                            = 104;
    public static final int OBJECT_ARRAY                           = 105;

    public static final int HASH_SET                               = 106;
    public static final int LINKED_HASH_SET                        = 107;
    public static final int TREE_SET                               = 108;
    public static final int ARRAY_LIST                             = 109;
    public static final int LINKED_LIST                            = 110;
    public static final int VECTOR                                 = 111;
    public static final int HASH_MAP                               = 112;
    public static final int HASH_TABLE                             = 113;
    public static final int LINKED_HASH_MAP                        = 114;
    public static final int CONCURRENT_HASH_MAP                    = 115;
    public static final int TREE_MAP                               = 116;

    public static final int OPTIONAL                               = 117;
    public static final int OPTIONAL_DOUBLE                        = 118;
    public static final int OPTIONAL_INT                           = 119;
    public static final int OPTIONAL_LONG                          = 120;

    public static final int DATE                                   = 121;
    public static final int GREGORIAN_CALENDAR                     = 122;
    public static final int BIG_DECIMAL                            = 123;
    public static final int BIG_INTEGER                            = 124;

    public static final int PROXY                                  = 125;
    public static final int LAMBDA                                 = 126;




}
