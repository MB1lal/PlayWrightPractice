package frontend.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.FindBy;

public class MkHomePage extends PageObject {


    @FindBy(id = "onetrust-accept-btn-handler")
    WebElementFacade acceptAllCookies;
    @FindBy(id = "collapse")
    WebElementFacade closeCountryPopup;
    @FindBy(css = ".close-button:nth-child(1)")
    WebElementFacade closeHelloPopup;

    @FindBy(className = "icon")
    WebElementFacade tapOnSearchbar;

    @FindBy(id = "search-box")
    WebElementFacade searchBar;

    public void clickAcceptAllCookies() {
        acceptAllCookies.click();
    }

    public boolean cookiesPopUpIsDisplayed() {
        return acceptAllCookies.isDisplayed();
    }

    public void clickCloseCountryPopup() {closeCountryPopup.click();}

    public boolean helloCountryPopUpIsDisplayed() {
        return closeHelloPopup.isDisplayed();
    }
    public void clickCloseHelloPopup() {
        closeHelloPopup.click();
    }

    public void tapOnSearchBarClick() {tapOnSearchbar.click();}

    public void searchForItems(String searchText) {
        String keys = Keys.chord(Keys.ENTER);
        searchBar.sendKeys(searchText + keys);
    }
}
