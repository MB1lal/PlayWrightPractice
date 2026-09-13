package com.example.ui.pages;

import com.example.base.TestContext;
import com.example.utils.AssertionHelper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.options.MouseButton;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Base class for all page objects.
 * Provides common element interactions and assertions.
 */
@Slf4j
public abstract class BasePage {
    
    protected Page page;
    protected TestContext testContext;
    
    protected BasePage(Page page, TestContext testContext) {
        this.page = page;
        this.testContext = testContext;
    }
    
    /**
     * Click on element with error handling
     */
    protected void click(Locator locator, String elementName) {
        try {
            log.debug("Clicking on element: {}", elementName);
            locator.waitFor();
            locator.click();
            log.debug("Element clicked successfully: {}", elementName);
        } catch (Exception e) {
            log.error("Failed to click on element: {}", elementName, e);
            throw new RuntimeException("Click failed on: " + elementName, e);
        }
    }
    
    /**
     * Type text into element with error handling
     */
    protected void type(Locator locator, String text, String elementName) {
        try {
            log.debug("Typing into element: {}", elementName);
            locator.waitFor();
            locator.clear();
            locator.type(text);
            log.debug("Text typed successfully into: {}", elementName);
        } catch (Exception e) {
            log.error("Failed to type into element: {}", elementName, e);
            throw new RuntimeException("Type failed on: " + elementName, e);
        }
    }
    
    /**
     * Fill element with text (Playwright's fill method)
     */
    protected void fill(Locator locator, String text, String elementName) {
        try {
            log.debug("Filling element: {}", elementName);
            locator.waitFor();
            locator.fill(text);
            log.debug("Element filled successfully: {}", elementName);
        } catch (Exception e) {
            log.error("Failed to fill element: {}", elementName, e);
            throw new RuntimeException("Fill failed on: " + elementName, e);
        }
    }
    
    /**
     * Get text from element with error handling
     */
    protected String getText(Locator locator, String elementName) {
        try {
            log.debug("Getting text from element: {}", elementName);
            locator.waitFor();
            String text = locator.textContent();
            log.debug("Text retrieved from {}: {}", elementName, text);
            return text;
        } catch (Exception e) {
            log.error("Failed to get text from element: {}", elementName, e);
            throw new RuntimeException("Get text failed on: " + elementName, e);
        }
    }
    
    /**
     * Get attribute value with error handling
     */
    protected String getAttribute(Locator locator, String attributeName, String elementName) {
        try {
            log.debug("Getting attribute '{}' from element: {}", attributeName, elementName);
            locator.waitFor();
            String value = locator.getAttribute(attributeName);
            log.debug("Attribute value: {}", value);
            return value;
        } catch (Exception e) {
            log.error("Failed to get attribute from element: {}", elementName, e);
            throw new RuntimeException("Get attribute failed on: " + elementName, e);
        }
    }
    
    /**
     * Check if element is visible with error handling
     */
    protected boolean isVisible(Locator locator, String elementName) {
        try {
            log.debug("Checking visibility of element: {}", elementName);
            boolean visible = locator.isVisible();
            log.debug("Element '{}' visibility: {}", elementName, visible);
            return visible;
        } catch (Exception e) {
            log.warn("Failed to check visibility of element: {}", elementName, e);
            return false;
        }
    }
    
    /**
     * Assert element is visible
     */
    protected void assertIsVisible(Locator locator, String elementName) {
        try {
            locator.waitFor();
            AssertionHelper.assertTrue(locator.isVisible(), 
                    "Element '" + elementName + "' should be visible");
            log.debug("Visibility assertion passed for: {}", elementName);
        } catch (Exception e) {
            log.error("Visibility assertion failed for: {}", elementName, e);
            throw new RuntimeException("Visibility assertion failed: " + elementName, e);
        }
    }
    
    /**
     * Assert element is not visible
     */
    protected void assertIsNotVisible(Locator locator, String elementName) {
        try {
            AssertionHelper.assertFalse(locator.isVisible(), 
                    "Element '" + elementName + "' should not be visible");
            log.debug("Non-visibility assertion passed for: {}", elementName);
        } catch (Exception e) {
            log.error("Non-visibility assertion failed for: {}", elementName, e);
            throw new RuntimeException("Non-visibility assertion failed: " + elementName, e);
        }
    }
    
    /**
     * Assert element contains text
     */
    protected void assertContainsText(Locator locator, String expectedText, String elementName) {
        try {
            locator.waitFor();
            String actualText = locator.textContent();
            AssertionHelper.assertContains(actualText, expectedText, elementName);
            log.debug("Text assertion passed for: {}", elementName);
        } catch (Exception e) {
            log.error("Text assertion failed for: {}", elementName, e);
            throw new RuntimeException("Text assertion failed: " + elementName, e);
        }
    }
    
    /**
     * Get count of elements
     */
    protected int getElementCount(Locator locator, String elementName) {
        try {
            log.debug("Getting count of elements: {}", elementName);
            int count = locator.count();
            log.debug("Element count for {}: {}", elementName, count);
            return count;
        } catch (Exception e) {
            log.error("Failed to get element count: {}", elementName, e);
            throw new RuntimeException("Get count failed: " + elementName, e);
        }
    }
    
    /**
     * Wait for element to be enabled
     */
    protected void waitForEnabled(Locator locator, String elementName) {
        try {
            log.debug("Waiting for element to be enabled: {}", elementName);
            locator.waitFor();
            log.debug("Element is enabled: {}", elementName);
        } catch (Exception e) {
            log.error("Failed waiting for element to be enabled: {}", elementName, e);
            throw new RuntimeException("Wait for enabled failed: " + elementName, e);
        }
    }
    
    /**
     * Locate a frame for chained element access.
     * Example: {@code frame("#my-frame").locator("button").click();}
     */
    protected FrameLocator frame(String frameSelector) {
        try {
            log.debug("Locating frame: {}", frameSelector);
            return page.frameLocator(frameSelector);
        } catch (Exception e) {
            log.error("Failed to locate frame: {}", frameSelector, e);
            throw new RuntimeException("Frame lookup failed: " + frameSelector, e);
        }
    }
    
    /**
     * Scroll element into view
     */
    protected void scrollIntoView(Locator locator, String elementName) {
        try {
            log.debug("Scrolling element into view: {}", elementName);
            locator.scrollIntoViewIfNeeded();
            log.debug("Element scrolled into view: {}", elementName);
        } catch (Exception e) {
            log.warn("Failed to scroll element into view: {}", elementName, e);
        }
    }
    
    /**
     * Check if element exists
     */
    protected boolean elementExists(Locator locator, String elementName) {
        try {
            log.debug("Checking if element exists: {}", elementName);
            boolean exists = locator.count() > 0;
            log.debug("Element exists: {} - {}", elementName, exists);
            return exists;
        } catch (Exception e) {
            log.debug("Element does not exist: {}", elementName);
            return false;
        }
    }
    
    /**
     * Double click on element
     */
    protected void doubleClick(Locator locator, String elementName) {
        try {
            log.debug("Double-clicking on element: {}", elementName);
            locator.waitFor();
            locator.dblclick();
            log.debug("Element double-clicked successfully: {}", elementName);
        } catch (Exception e) {
            log.error("Failed to double-click on element: {}", elementName, e);
            throw new RuntimeException("Double-click failed: " + elementName, e);
        }
    }
    
    /**
     * Right click on element
     */
    protected void rightClick(Locator locator, String elementName) {
        try {
            log.debug("Right-clicking on element: {}", elementName);
            locator.waitFor();
            locator.click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));
            log.debug("Element right-clicked successfully: {}", elementName);
        } catch (Exception e) {
            log.error("Failed to right-click on element: {}", elementName, e);
            throw new RuntimeException("Right-click failed: " + elementName, e);
        }
    }
}
