package io.github.mb1lal.playwright.ui.pages.herokuapp;

import io.github.mb1lal.playwright.base.TestContext;
import io.github.mb1lal.playwright.ui.pages.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

/**
 * Add/Remove Elements page ({@code /add_remove_elements/}).
 */
@Slf4j
public class AddRemoveElementsPage extends BasePage {

    private final Locator addButton;
    private final Locator deleteButtons;

    public AddRemoveElementsPage(Page page, TestContext testContext) {
        super(page, testContext);
        this.addButton = page.locator("button[onclick='addElement()']");
        this.deleteButtons = page.locator(".added-manually");
    }

    public void addElements(int times) {
        for (int i = 0; i < times; i++) {
            click(addButton, "Add Element button");
        }
    }

    public int deleteCount() {
        return getElementCount(deleteButtons, "Delete buttons");
    }

    public void assertDeleteVisible(boolean expected) {
        boolean visible = deleteCount() > 0
                && deleteButtons.first().isVisible();
        if (expected) {
            assertIsVisible(deleteButtons.first(), "Delete button");
        } else {
            io.github.mb1lal.playwright.utils.AssertionHelper.assertFalse(visible, "Delete button should be gone");
        }
    }

    public void deleteFirst() {
        click(deleteButtons.first(), "first Delete button");
    }
}
