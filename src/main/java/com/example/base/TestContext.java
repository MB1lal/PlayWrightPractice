package com.example.base;

import com.microsoft.playwright.APIResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Thread-safe context for sharing test data between steps.
 * Replaces static SharedState to support parallel execution.
 */
@Getter
@Setter
public class TestContext {
    
    // API/Backend Context
    private APIResponse petResponse;
    private long petId;
    private String petStatus;
    private int petStoreId;
    private APIResponse petStoreResponse;
    private int petOrderId;
    
    // User Context
    private APIResponse userResponse;
    private final UserData userData = new UserData();
    
    // Frontend Context
    private List<List<String>> excelData;
    private List<List<String>> castAndCrew;
    private String currentUrl;
    
    // Generic data storage for flexibility
    private final Map<String, Object> dataStore = new HashMap<>();
    
    /**
     * Store any generic data with a key
     */
    public void put(String key, Object value) {
        dataStore.put(key, value);
    }
    
    /**
     * Retrieve any generic data by key
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        return (T) dataStore.get(key);
    }
    
    /**
     * Clear all context data
     */
    public void clear() {
        petResponse = null;
        petId = 0;
        petStatus = null;
        petStoreId = 0;
        petStoreResponse = null;
        petOrderId = 0;
        userResponse = null;
        userData.clear();
        excelData = null;
        castAndCrew = null;
        currentUrl = null;
        dataStore.clear();
    }
    
    /**
     * User data holder
     */
    @Getter
    @Setter
    public static class UserData {
        private String username;
        private String password;
        private String email;
        private String phone;
        private String firstName;
        private String lastName;
        private Integer status;
        private Integer userId;
        
        public void clear() {
            username = null;
            password = null;
            email = null;
            phone = null;
            firstName = null;
            lastName = null;
            status = null;
            userId = null;
        }
    }
}
