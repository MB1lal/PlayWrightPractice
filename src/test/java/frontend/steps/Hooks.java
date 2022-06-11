package frontend.steps;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.SharedState;

public class Hooks extends BaseSteps{

    @Before
    public void bootstrap(Scenario scenario) {
        if(scenario.getSourceTagNames().contains("@excelData")) {
            utils.ExcelReader excelReader = utils.ExcelReader.getInstance();
            try
            {
                SharedState.EXCEL_DATA = excelReader.readExcel("input");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        playwright =  Playwright.create();
        switch(System.getenv("browser").toLowerCase()) {
            case "chrome" -> {
                browserType = playwright.chromium();
                browser = browserType.launch(new BrowserType.LaunchOptions()
                        .setChannel("chrome")
                );
            }
            case "firefox" -> {
                browserType = playwright.firefox();
                browser = browserType.launch(new BrowserType.LaunchOptions().setChannel("firefox"));
            }
            case "webkit" -> {
                browserType = playwright.webkit();
                browser = browserType.launch(new BrowserType.LaunchOptions().setChannel("webkit"));
            }
        }
        page = browser.newPage();
    }

    @After
    public void tearDown() {
        playwright.close();
    }


}
