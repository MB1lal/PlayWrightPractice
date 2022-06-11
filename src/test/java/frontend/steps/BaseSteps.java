package frontend.steps;


import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import core.PropertiesReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseSteps {
    public utils.ExcelReader excelReader = utils.ExcelReader.getInstance();
    public static final Logger logger = LogManager.getLogger(backend.steps.BaseSteps.class);

    public static Playwright playwright;
    public static Browser browser;
    public static Page page;
    public static BrowserType browserType;

}
