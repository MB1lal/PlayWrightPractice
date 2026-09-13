package com.example.playwright;

import com.example.base.TestContext;
import com.example.config.ConfigManager;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.Cookie;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Owns the Playwright browser lifecycle for a single Cucumber scenario.
 *
 * <p>All state is held in {@link ThreadLocal}s so scenarios can run in
 * parallel. A single instance is shared per scenario through PicoContainer
 * constructor injection (see {@code com.example.steps.Hooks}).
 */
@Slf4j
public class BrowserManager {

    private final TestContext testContext;
    private final ConfigManager config;

    private final ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private final ThreadLocal<Browser> browser = new ThreadLocal<>();
    private final ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();
    private final ThreadLocal<Page> page = new ThreadLocal<>();

    public BrowserManager(TestContext testContext) {
        this.testContext = testContext;
        this.config = ConfigManager.getInstance();
    }

    /** Launch the configured browser and open a fresh page. Idempotent per thread. */
    public void start() {
        start(null, null);
    }

    /**
     * Launch with HTTP basic-auth credentials pre-installed in the browser context
     * (e.g. for {@code /basic_auth}). Call <em>instead of</em> {@link #start()}.
     */
    public void startWithHttpCredentials(String username, String password) {
        start(username, password);
    }

    private void start(String httpUsername, String httpPassword) {
        if (page.get() != null) {
            return;
        }
        String browserName = config.getBrowser();
        log.info("Starting Playwright browser '{}' (headless={})", browserName, config.isHeadless());

        Playwright pw = Playwright.create();
        playwright.set(pw);

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(config.isHeadless())
                .setSlowMo(config.getSlowMoMs());

        Browser launched;
        switch (browserName) {
            case "chrome" -> {
                log.info("Launching branded Google Chrome");
                launched = pw.chromium().launch(launchOptions.setChannel("chrome"));
            }
            case "firefox" -> {
                log.info("Launching Firefox");
                launched = pw.firefox().launch(launchOptions);
            }
            case "webkit" -> {
                log.info("Launching WebKit");
                launched = pw.webkit().launch(launchOptions);
            }
            case "chromium" -> {
                log.info("Launching Chromium");
                launched = pw.chromium().launch(launchOptions);
            }
            default -> throw new IllegalArgumentException(
                    "Unsupported browser '" + browserName + "'. Use chromium, chrome, firefox or webkit.");
        }
        browser.set(launched);

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(config.getViewportWidth(), config.getViewportHeight())
                .setIgnoreHTTPSErrors(true);
        if (httpUsername != null) {
            log.info("Installing HTTP basic-auth credentials for user '{}'", httpUsername);
            contextOptions.setHttpCredentials(httpUsername, httpPassword);
        }
        BrowserContext context = launched.newContext(contextOptions);
        browserContext.set(context);

        Page newPage = context.newPage();
        newPage.setDefaultTimeout(config.getTimeoutMs());
        page.set(newPage);

        log.info("Browser started successfully");
    }

    /** The active page. Fails fast if {@link #start()} has not run yet. */
    public Page getPage() {
        Page current = page.get();
        if (current == null) {
            throw new IllegalStateException("Browser has not been started. Did the @BeforeEach setup run?");
        }
        return current;
    }

    /** Navigate to a URL and remember it in the scenario context. */
    public void navigate(String url) {
        log.info("Navigating to {}", url);
        getPage().navigate(url);
        getPage().waitForLoadState();
        testContext.setCurrentUrl(url);
    }

    public String title() {
        return getPage().title();
    }

    public String url() {
        return getPage().url();
    }

    /** Add a cookie to the current browser context (domain-scoped, e.g. opt-out flags). */
    public void addCookie(String name, String value, String domain) {
        log.info("Setting cookie {}={} for domain {}", name, value, domain);
        getPage().context().addCookies(List.of(
                new Cookie(name, value).setDomain(domain).setPath("/")));
    }

    /** Screenshot bytes (e.g. for attaching to reports on failure). */
    public byte[] screenshot() {
        return getPage().screenshot();
    }

    /** Save a screenshot under the configured screenshot directory. */
    public Path screenshot(String fileName) {
        Path path = Paths.get(config.getScreenshotDir(), fileName + ".png");
        try {
            path.toFile().getParentFile().mkdirs();
            getPage().screenshot(new Page.ScreenshotOptions().setPath(path));
            log.info("Screenshot saved to {}", path);
        } catch (Exception e) {
            log.error("Failed to save screenshot to {}", path, e);
        }
        return path;
    }

    /** Close page, context, browser and Playwright for the current thread. */
    public void close() {
        closeQuietly(page, "page");
        closeQuietly(browserContext, "browser context");
        closeQuietly(browser, "browser");
        closeQuietly(playwright, "playwright");
        page.remove();
        browserContext.remove();
        browser.remove();
        playwright.remove();
        log.info("Browser closed");
    }

    private static void closeQuietly(ThreadLocal<? extends AutoCloseable> holder, String name) {
        AutoCloseable closeable = holder.get();
        if (closeable != null) {
            try {
                closeable.close();
                log.debug("Closed {}", name);
            } catch (Exception e) {
                log.warn("Error closing {}", name, e);
            }
        }
    }
}
