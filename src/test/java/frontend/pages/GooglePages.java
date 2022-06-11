package frontend.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import core.PropertiesReader;


public class GooglePages {
    private final Page page;
    private final Locator pageLogo;
    private final Locator searchBox;
    private final Locator searchButton;

    public GooglePages(Page page) {
        this.page = page;
        this.pageLogo =  page.locator("lnXdpd");
        this.searchBox = page.locator("input[class='gLFyf gsfi']");
        this.searchButton = page.locator("btnK");
    }
    String url;
    PropertiesReader propertiesReader = new PropertiesReader();

    public void pageHasLogo() {
        page.navigate(propertiesReader.readPropertyFile("googleURL"));
        pageLogo.isVisible();
    }

    public void enterDataIntoSearchBox(String searchText) {
        searchBox.type(searchText);
    }

    public void clickOnSearchButton() {
//        searchButton.click();
        searchBox.type("\n");
    }

    public void rightClickOnTheLink(String linkText) {
//        Actions actions = new Actions(driver);
        Locator partialLinkText = page.locator("text=" + linkText);
        url = partialLinkText.getAttribute("baseUri");
//        actions.contextClick(partialLinkText).perform();
    }

    public void saveTheLinkedURL(String linkText) {
        Locator partialLinkText = page.locator("text=" + linkText);
        url = partialLinkText.getAttribute("baseURI");
    }

    //Deprecated
//    public void openInANewTab() {
//        Actions actions = new Actions(driver);
//        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
//        ArrayList<String> tab = new ArrayList<>(driver.getWindowHandles());
//        driver.switchTo().window(tab.get(1));
//    }

    public void openInANewTab() {
//        WebDriver newTab = driver.switchTo().newWindow(WindowType.TAB);
//        newTab.get(url);
    }

    public void clicksOnPartialLinkText(String partialLinkText) {
        Locator partialLinkTextElement = page.locator("text=" + partialLinkText);
        partialLinkTextElement.click();
    }

    public void clicksOnLinkText(String linkText) {
//        WebElement linkTextElement = driver.findElement(By.partialLinkText(linkText));
//        linkTextElement.click();
    }

}
