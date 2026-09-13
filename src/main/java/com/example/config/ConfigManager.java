package com.example.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centralized configuration for the UI test framework.
 *
 * <p>Resolution order (highest priority first):
 * <ol>
 *   <li>JVM system properties ({@code -Dbrowser=firefox})</li>
 *   <li>Environment variables ({@code BROWSER}, {@code HEADLESS}, {@code BASE_URL})</li>
 *   <li>{@code config.properties} on the test classpath</li>
 *   <li>{@code application.properties} on the main classpath (shipped defaults)</li>
 *   <li>Hard-coded defaults below</li>
 * </ol>
 */
@Slf4j
public class ConfigManager {

    private static volatile ConfigManager instance;

    private final Properties testProperties = new Properties();
    private final Properties defaultProperties = new Properties();

    private ConfigManager() {
        loadFromClasspath("config.properties", testProperties);
        loadFromClasspath("application.properties", defaultProperties);
    }

    public static ConfigManager getInstance() {
        ConfigManager result = instance;
        if (result == null) {
            synchronized (ConfigManager.class) {
                result = instance;
                if (result == null) {
                    instance = result = new ConfigManager();
                }
            }
        }
        return result;
    }

    /** Reset the singleton (for tests only). */
    public static synchronized void reset() {
        instance = null;
    }

    private void loadFromClasspath(String resource, Properties target) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resource)) {
            if (input != null) {
                target.load(input);
                log.info("Configuration loaded from {}", resource);
            } else {
                log.debug("Configuration resource not found on classpath: {}", resource);
            }
        } catch (IOException e) {
            log.error("Error loading configuration resource: {}", resource, e);
        }
    }

    /**
     * Resolve a raw value: system property &gt; environment variable &gt;
     * config.properties &gt; application.properties &gt; default.
     */
    public String getString(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value != null && !value.isBlank()) {
            return value;
        }
        value = testProperties.getProperty(key);
        if (value != null && !value.isBlank()) {
            return value;
        }
        value = defaultProperties.getProperty(key);
        if (value != null && !value.isBlank()) {
            return value;
        }
        return defaultValue;
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public int getInt(String key, int defaultValue) {
        String value = getString(key);
        if (value != null && !value.isBlank()) {
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                log.warn("Invalid integer for key '{}': '{}', using default {}", key, value, defaultValue);
            }
        }
        return defaultValue;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key);
        if (value != null && !value.isBlank()) {
            return Boolean.parseBoolean(value.trim());
        }
        return defaultValue;
    }

    // ---- Typed accessors (canonical keys) ----

    /** Base URL under test. Supports legacy {@code baseURI}/{@code googleURL} keys. */
    public String getBaseUrl() {
        String url = System.getenv("BASE_URL");
        if (url != null && !url.isBlank()) {
            return url;
        }
        url = getString("base.url");
        if (url == null) {
            url = getString("baseURI", getString("googleURL", "https://duckduckgo.com"));
        }
        return url;
    }

    /** Base URL of the Herokuapp demo site used by the ported Cypress specs. */
    public String getHerokuUrl() {
        String url = System.getenv("HEROKU_URL");
        if (url != null && !url.isBlank()) {
            return url;
        }
        return getString("heroku.url", "https://the-internet.herokuapp.com");
    }

    /**
     * Browser to launch: {@code chromium} (default), {@code chrome},
     * {@code firefox} or {@code webkit}. Overridable via {@code -Dbrowser=...}
     * or the {@code BROWSER} environment variable.
     */
    public String getBrowser() {
        String browser = System.getProperty("browser");
        if (browser == null || browser.isBlank()) {
            browser = System.getenv("BROWSER");
        }
        if (browser == null || browser.isBlank()) {
            browser = getString("browser", "chromium");
        }
        return browser.trim().toLowerCase();
    }

    public boolean isHeadless() {
        String env = System.getenv("HEADLESS");
        if (env != null && !env.isBlank()) {
            return Boolean.parseBoolean(env.trim());
        }
        return getBoolean("headless", true);
    }

    /** Slow-motion delay (ms) applied between Playwright actions; useful for debugging. */
    public int getSlowMoMs() {
        return getInt("slow.mo.ms", getInt("slowmo", 0));
    }

    /** Default timeout (ms) for navigation, actions and assertions. */
    public int getTimeoutMs() {
        return getInt("timeout.ms", getInt("timeout", 30000));
    }

    public int getViewportWidth() {
        return getInt("viewport.width", 1920);
    }

    public int getViewportHeight() {
        return getInt("viewport.height", 1080);
    }

    public String getTestDataDir() {
        return getString("testdata.dir", "src/test/resources/data-files");
    }

    public String getScreenshotDir() {
        return getString("screenshot.dir", "target/screenshots");
    }

    /** Log the effective configuration (no secrets are held by this project). */
    public void displayConfig() {
        log.info("=== Effective Configuration ===");
        log.info("base.url        = {}", getBaseUrl());
        log.info("heroku.url      = {}", getHerokuUrl());
        log.info("browser         = {}", getBrowser());
        log.info("headless        = {}", isHeadless());
        log.info("timeout.ms      = {}", getTimeoutMs());
        log.info("viewport        = {}x{}", getViewportWidth(), getViewportHeight());
        log.info("testdata.dir    = {}", getTestDataDir());
        log.info("screenshot.dir  = {}", getScreenshotDir());
    }
}
