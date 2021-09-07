package org.light.serialize.core;

/**
 * RuntimeEnv
 *
 * @author alex
 */
public class RuntimeEnv {

    private static final String javaVersion = System.getProperty("java.version");
    private static boolean java8 = javaVersion.contains("1.8.");
    private static boolean java9Plus = javaVersion.compareTo("1.9") > 0;

    public static String getJavaVersion() {
        return javaVersion;
    }

    public static boolean isJava8() {
        return java8;
    }

    public static boolean isJava9Plus() {
        return java9Plus;
    }
}
