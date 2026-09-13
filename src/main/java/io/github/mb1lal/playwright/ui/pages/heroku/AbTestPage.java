package io.github.mb1lal.playwright.ui.pages.herokuapp;

import io.github.mb1lal.playwright.base.TestContext;
import io.github.mb1lal.playwright.ui.pages.BasePage;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A/B Test page ({@code /abtest}): Optimizely bucketing with an opt-out cookie
 * and an opt-out URL that raises an alert.
 */
@Slf4j
public class AbTestPage extends BasePage {

    public static final String OPT_OUT_MESSAGE =
            "You have successfully opted out of Optimizely for this domain.";

    public AbTestPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    public void open(String baseUrl) {
        page.navigate(baseUrl + "/abtest");
        page.waitForLoadState();
    }

    public String heading() {
        String text = getText(page.locator("h3"), "A/B test heading").trim();
        log.info("A/B heading: '{}'", text);
        return text;
    }

    /** Open the opt-out URL and return the alert text it raises. */
    public String openOptOutUrl(String baseUrl) {
        AtomicReference<String> alertText = new AtomicReference<>();
        page.onceDialog(dialog -> {
            alertText.set(dialog.message());
            dialog.dismiss();
        });
        page.navigate(baseUrl + "/abtest?optimizely_opt_out=true");
        page.waitForLoadState();
        return alertText.get();
    }
}
