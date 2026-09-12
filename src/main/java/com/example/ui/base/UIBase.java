package com.example.ui.base;

import com.example.base.TestContext;
import com.example.config.ConfigManager;
import com.example.utils.AssertionHelper;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for UI tests with Playwright browser management.
 * Handles initialization, configuration, and cleanup of browser instances.
 */
@Slf4j
public class UIBase {
    
    protected Playwright playwright;
    protected Browser browser;
    protected Page page;
    protected TestContext testContext;
    protected ConfigManager configManager;
    
    @Getter
    private String browserName;
    
    /**
     * Initialize browser and page
     */
    public void initializeBrowser(TestContext testContext) {
        this.testContext = testContext;
        this.configManager = ConfigManager.getInstance();
        
        try {
            log.info("Initializing Playwright browser");
            playwright = Playwright.create();
            
            browserName = configManager.getBrowser();
            log.info("Browser type selected: {}", browserName);
            
            browser = launchBrowser(browserName);
            page = browser.newPage();
            
            log.info("Browser and page initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize browser", e);
            throw new RuntimeException("Browser initialization failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Launch browser based on browser name
     */
    private Browser launchBrowser(String browserName) {
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(configManager.isHeadless())
                .setChannel(browserName);
        
        switch (browserName.toLowerCase()) {
            case "firefox":
                log.info("Launching Firefox browser");
                return playwright.firefox().launch(launchOptions);
            case "webkit":
                log.info("Launching WebKit browser");
                return playwright.webkit().launch(launchOptions);
            case "chrome":
            default:
                log.info("Launching Chrome browser");
                return playwright.chromium().launch(launchOptions);
        }
    }
    
    /**
     * Navigate to URL
     */
    public void navigateTo(String url) {
        try {
            log.info("Navigating to URL: {}", url);
            page.navigate(url);
            testContext.setCurrentUrl(url);
            log.debug("Navigation successful");
        } catch (Exception e) {
            log.error("Navigation failed to URL: {}", url, e);
            throw new RuntimeException("Navigation failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Wait for page load with default timeout
     */
    public void waitForPageLoad() {
        try {
            log.debug("Waiting for page load");
            page.waitForLoadState();
            log.debug("Page loaded successfully");
        } catch (Exception e) {
            log.warn("Page load wait timed out", e);
        }
    }
    
    /**
     * Get current page title
     */
    public String getPageTitle() {
        String title = page.title();
        log.debug("Current page title: {}", title);
        return title;
    }
    
    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        String url = page.url();
        log.debug("Current URL: {}", url);
        return url;
    }
    
    /**
     * Take screenshot
     */
    public void takeScreenshot(String fileName) {
        try {
            String path = "screenshots/" + fileName + ".png";
            page.screenshot(new Page.ScreenshotOptions().setPath(new java.nio.file.Paths.get(path)));
            log.info("Screenshot saved: {}", path);
        } catch (Exception e) {
            log.error("Failed to take screenshot", e);
        }
    }
    
    /**
     * Close browser and cleanup
     */
    public void closeBrowser() {
        try {
            if (page != null) {
                page.close();
                log.debug("Page closed");
            }
            if (browser != null) {
                browser.close();
                log.debug("Browser closed");
            }
            if (playwright != null) {
                playwright.close();
                log.debug("Playwright closed");
            }
            log.info("Browser cleanup completed successfully");
        } catch (Exception e) {
            log.error("Error during browser cleanup", e);
        }
    }
    
    /**
     * Execute JavaScript
     */
    public Object executeScript(String script, Object... args) {
        try {
            log.debug("Executing JavaScript: {}", script);
            return page.evaluate(script, args);
        } catch (Exception e) {
            log.error("JavaScript execution failed", e);
            throw new RuntimeException("Script execution failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Wait for specific time in milliseconds
     */
    public void wait(long milliseconds) {
        try {
            log.debug("Waiting for {} ms", milliseconds);
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            log.warn("Wait interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}
