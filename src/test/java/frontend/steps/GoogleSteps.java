package frontend.steps;

import frontend.pages.GooglePages;
import frontend.pages.IMDBPages;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import utils.SharedState;

import java.util.List;

public class GoogleSteps extends BaseSteps{

    GooglePages googlePages = new GooglePages(page);
    IMDBPages iMDBPages = new IMDBPages(page);
    List<List<String>> excelData;
    String linkText;

    @Given("User navigates to Google")
    public void openingABrowser() {
        googlePages.pageHasLogo();
    }

    @Then("The page is loaded")
    public void assertingThatPageIsLoaded() {
//        googlePages.pageHasLogo();
    }

    @And("User searches for value in {string} of provided sheet")
    public void searchThePageForValue(String sheetIndex) {
        excelData = SharedState.EXCEL_DATA;
        int index1 = Integer.parseInt(String.valueOf(sheetIndex.charAt(1))) - 1;
        int index2 = excelReader.getColumnIndex(sheetIndex);
        String searchString = excelData.get(index1).get(index2);
        googlePages.enterDataIntoSearchBox(searchString);
        googlePages.clickOnSearchButton();
    }

    @And("User finds the link present in {string} of provided sheet")
    public void findTheLinkOnPage(String sheetIndex) {
        int index1 = Integer.parseInt(String.valueOf(sheetIndex.charAt(1))) - 1;
        int index2 = excelReader.getColumnIndex(sheetIndex);
        linkText = excelData.get(index1).get(index2);
    }

    @And("User right clicks on the link")
    public void rightClickOnTheLink() {
        googlePages.saveTheLinkedURL(linkText);
    }

    @And("User opens the link")
    public void clickOpen() {
        googlePages.clicksOnPartialLinkText(linkText);
        iMDBPages.pageHasLogo();
    }




}
