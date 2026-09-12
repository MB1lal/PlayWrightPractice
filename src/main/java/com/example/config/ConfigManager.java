package com.example.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Centralized configuration manager for all test settings.
 * Loads from config.properties with fallback defaults.
 */
@Slf4j
public class ConfigManager {
    
    private static ConfigManager instance;
    private final Properties properties;
    
    // Configuration keys with defaults
    private static final String BASE_URI = "baseURI";
    private static final String BASE_URI_DEFAULT = "https://petstore.swagger.io/v2/";
    
    private static final String GOOGLE_URL = "googleURL";
    private static final String GOOGLE_URL_DEFAULT = "https://www.google.com";
    
    private static final String PET_FILE_ROOT = "petFileBodiesRoot";
    private static final String PET_FILE_ROOT_DEFAULT = "src/test/resources/data-files/backend/";
    
    private static final String BROWSER = "browser";
    private static final String BROWSER_DEFAULT = "chrome";
    
    private static final String HEADLESS = "headless";
    private static final boolean HEADLESS_DEFAULT = false;
    
    private static final String TIMEOUT_MS = "timeoutMs";
    private static final int TIMEOUT_MS_DEFAULT = 30000;
    
    private ConfigManager() {
        this.properties = new Properties();
        loadProperties();
    }
    
    /**
     * Get singleton instance of ConfigManager
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    /**
     * Load properties from config.properties file
     */
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            
            if (input != null) {
                properties.load(input);
                log.info("Configuration loaded successfully from config.properties");
            } else {
                log.warn("config.properties not found, using default values");
            }
        } catch (IOException e) {
            log.error("Error loading configuration file", e);
        }
    }
    
    /**
     * Get configuration value as String with default fallback
     */
    public String getString(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        log.debug("Config[{}] = {}", key, value);
        return value;
    }
    
    /**
     * Get configuration value as String
     */
    public String getString(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get configuration value as Integer with default fallback
     */
    public int getInteger(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value != null && !value.isEmpty()) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                log.warn("Invalid integer value for key {}: {}, using default: {}", key, value, defaultValue);
            }
        }
        return defaultValue;
    }
    
    /**
     * Get configuration value as Boolean with default fallback
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value != null && !value.isEmpty()) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }
    
    // Convenience methods for common configs
    
    public String getBaseUri() {
        return getString(BASE_URI, BASE_URI_DEFAULT);
    }
    
    public String getGoogleUrl() {
        return getString(GOOGLE_URL, GOOGLE_URL_DEFAULT);
    }
    
    public String getPetFileRoot() {
        return getString(PET_FILE_ROOT, PET_FILE_ROOT_DEFAULT);
    }
    
    public String getBrowser() {
        String browser = System.getenv("browser");
        if (browser != null && !browser.isEmpty()) {
            return browser.toLowerCase();
        }
        return getString(BROWSER, BROWSER_DEFAULT).toLowerCase();
    }
    
    public boolean isHeadless() {
        return getBoolean(HEADLESS, HEADLESS_DEFAULT);
    }
    
    public int getTimeoutMs() {
        return getInteger(TIMEOUT_MS, TIMEOUT_MS_DEFAULT);
    }
    
    /**
     * Reset singleton for testing purposes
     */
    public static synchronized void reset() {
        instance = null;
    }
    
    /**
     * Display all loaded configuration (mask sensitive values)
     */
    public void displayConfig() {
        log.info("=== Loaded Configuration ===");
        log.info("Base URI: {}", getBaseUri());
        log.info("Google URL: {}", getGoogleUrl());
        log.info("Browser: {}", getBrowser());
        log.info("Headless: {}", isHeadless());
        log.info("Timeout (ms): {}", getTimeoutMs());
    }
}
