package com.hrms.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads test settings from src/test/resources/config.properties.
 * All values are loaded once at class-initialisation time.
 */
public class ConfigReader {

    private static final Properties props = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("config.properties not found in src/test/resources/", e);
        }
    }

    public static String get(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing key in config.properties: " + key);
        }
        return value.trim();
    }

    public static String getBaseUrl()      { return get("base.url"); }
    public static String getUsername()     { return get("username"); }
    public static String getPassword()     { return get("password"); }
    public static String getBrowser()      { return get("browser"); }
    public static int    getExplicitWait() { return Integer.parseInt(get("explicit.wait")); }
}
