package frontend.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.FindBy;
import utils.StringUtils;

public class MKPDPage extends PageObject {

    @FindBy (xpath = "*//span[@class='ada-link productAmount']")
    WebElementFacade priceOfItem;

    public String getItemPrice() {
       return  StringUtils.unescapeHTML(priceOfItem.getAttribute("outerText"))
               .replace("\u200C","");
       }
}
