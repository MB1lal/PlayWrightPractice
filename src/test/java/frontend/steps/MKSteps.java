package frontend.steps;

import frontend.pages.GooglePages;
import frontend.pages.MKPDPage;
import frontend.pages.MKPLPage;
import frontend.pages.MkHomePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

import static org.assertj.core.api.Assertions.assertThat;

public class MKSteps extends BaseSteps {

    GooglePages googlePages;
    MkHomePage mkHomePage;
    MKPLPage mkPLPage;

    MKPDPage mkPDPage;

    @And("User searches for {string} on google")
    public void searchTermOnGoogle(String searchText) {
        googlePages.enterDataIntoSearchBox(searchText);
        googlePages.clickOnSearchButton();
    }

    @And("User clicks on on partial link text {string}")
    public void clickOnMKOfficialWebsite(String partialLinkText) {
        googlePages.clicksOnPartialLinkText(partialLinkText);
    }

    @And("User closes all popups")
    public void closeALLPopups() {
        if(mkHomePage.cookiesPopUpIsDisplayed()) {
            mkHomePage.clickAcceptAllCookies();
        }
        if(mkHomePage.helloCountryPopUpIsDisplayed()) {
            mkHomePage.clickCloseHelloPopup();
        }
    }

    @And("User taps on SearchBar")
    public void tapOnSearchbar(){
        mkHomePage.tapOnSearchBarClick();
    }
    @And("User searches for {string}")
    public void searchForItems(String searchText){
        mkHomePage.searchForItems(searchText);
    }

    @And("User opens {string} from search results")
    public void openItemFromPLP(String item){
        mkPLPage.selectItemUsingPartialLinkText(item);
    }

    @Then ("The price of bag is {string}")
    public void checkPriceOfItem(String price){
        assertThat(mkPDPage.getItemPrice())
                .isEqualTo(price);
    }
}
