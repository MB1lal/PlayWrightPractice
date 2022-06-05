package backend.steps;


import com.microsoft.playwright.Playwright;
import io.cucumber.java.After;
import io.cucumber.java.Before;


public class Hooks extends BaseSteps {


    void createPlaywright() {
        playwright = Playwright.create();
    }

    void closePlaywright() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    @Before
    public void setup() {
        createPlaywright();
    }

    @After
    public void tearDown() {
        closePlaywright();
    }
}
