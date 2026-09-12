package com.example.base;

import com.example.config.ConfigManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base test fixture for all test classes.
 * Provides test lifecycle management with proper TestContext injection.
 * Supports both API and UI tests.
 */
@Slf4j
public abstract class BaseTest {
    
    protected TestContext testContext;
    protected ConfigManager configManager;
    
    /**
     * Setup before each test
     */
    @BeforeEach
    public void setUp() {
        log.info("========== Starting Test: {} ==========", getTestName());
        
        // Initialize test context
        testContext = new TestContext();
        configManager = ConfigManager.getInstance();
        
        // Display configuration for debugging
        configManager.displayConfig();
        
        // Call child class setup if needed
        beforeTest();
        
        log.info("Test setup completed");
    }
    
    /**
     * Cleanup after each test
     */
    @AfterEach
    public void tearDown() {
        try {
            log.info("Starting test cleanup");
            
            // Call child class cleanup if needed
            afterTest();
            
            // Clear context
            if (testContext != null) {
                testContext.clear();
                log.debug("Test context cleared");
            }
        } catch (Exception e) {
            log.error("Error during test cleanup", e);
        } finally {
            log.info("========== Test Completed: {} ==========\n", getTestName());
        }
    }
    
    /**
     * Override in child classes for custom setup
     */
    protected void beforeTest() {
        // Optional - override in subclasses
    }
    
    /**
     * Override in child classes for custom cleanup
     */
    protected void afterTest() {
        // Optional - override in subclasses
    }
    
    /**
     * Get test name from current thread
     */
    protected String getTestName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
    
    /**
     * Log test step
     */
    protected void logStep(String stepDescription) {
        log.info("STEP: {}", stepDescription);
    }
    
    /**
     * Log test result
     */
    protected void logResult(String result, boolean passed) {
        if (passed) {
            log.info("✓ RESULT: {} - PASSED", result);
        } else {
            log.error("✗ RESULT: {} - FAILED", result);
        }
    }
}
