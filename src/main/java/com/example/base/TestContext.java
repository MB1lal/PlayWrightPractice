package com.example.base;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Per-scenario state shared between step definitions via PicoContainer
 * constructor injection.
 *
 * <p>One instance is created per Cucumber scenario, so unlike the old static
 * {@code SharedState} this is safe for parallel execution.
 */
@Getter
@Setter
public class TestContext {

    private String currentUrl;
    private String searchTerm;
    private String linkText;
    private List<List<String>> excelData;
    private List<List<String>> castAndCrew;

    /** Generic key/value store for anything step definitions need to share. */
    private final Map<String, Object> dataStore = new HashMap<>();

    public void put(String key, Object value) {
        dataStore.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        return (T) dataStore.get(key);
    }

    /** Reset all state (called after each scenario). */
    public void clear() {
        currentUrl = null;
        searchTerm = null;
        linkText = null;
        excelData = null;
        castAndCrew = null;
        dataStore.clear();
    }
}
