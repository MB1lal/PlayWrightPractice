package com.example.support;

import com.example.base.TestContext;
import com.example.config.ConfigManager;
import com.example.playwright.BrowserManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.RegisterExtension;

/**
 * Base class for all UI tests.
 *
 * <ul>
 *   <li>Starts a fresh, isolated browser before each test.</li>
 *   <li>Captures a screenshot under {@code target/screenshots} when a test fails.</li>
 *   <li>Closes everything afterwards, even on failure.</li>
 * </ul>
 *
 * <p>JUnit creates a new test-class instance per test method, and
 * {@link BrowserManager} keeps its state in {@link ThreadLocal}s, so tests are
 * safe to run in parallel (see {@code junit-platform.properties}).
 */
@Slf4j
public abstract class BaseUiTest {

    protected ConfigManager config = ConfigManager.getInstance();
    protected TestContext context;
    protected BrowserManager browser;

    /**
     * Screenshots must be taken in {@link AfterTestExecutionCallback} (which runs
     * <em>before</em> {@code @AfterEach}), otherwise the browser is already closed.
     */
    @RegisterExtension
    AfterTestExecutionCallback screenshotOnFailure = extensionContext -> {
        if (browser == null || extensionContext.getExecutionException().isEmpty()) {
            return;
        }
        try {
            log.error("Test failed at url={} title='{}'",
                    safe(browser::url), safe(browser::title));
        } catch (Exception ignored) {
            // diagnostics must never hide the original failure
        }
        String testName = extensionContext.getRequiredTestClass().getSimpleName()
                + "-" + extensionContext.getRequiredTestMethod().getName()
                + "-" + System.currentTimeMillis();
        browser.screenshot(testName);
    };

    @BeforeEach
    void setUp(TestInfo testInfo) {
        log.info("========== Starting test: {} ==========",
                testInfo.getTestMethod().map(m -> m.getName()).orElse("unknown"));
        config.displayConfig();
        context = new TestContext();
        browser = new BrowserManager(context);
        browser.start();
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        try {
            if (browser != null) {
                browser.close();
            }
            if (context != null) {
                context.clear();
            }
        } finally {
            log.info("========== Finished test: {} ==========",
                    testInfo.getTestMethod().map(m -> m.getName()).orElse("unknown"));
        }
    }

    private static String safe(java.util.function.Supplier<String> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return "<unavailable>";
        }
    }
}
