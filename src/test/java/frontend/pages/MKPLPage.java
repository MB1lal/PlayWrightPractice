package frontend.pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MKPLPage extends PageObject {

    private final WebDriver driver = super.getDriver();


    public void selectItemUsingPartialLinkText(String partialLinkText) {
        WebElement partialLinkTextElement = driver.findElement(By.partialLinkText(partialLinkText));
        partialLinkTextElement.click();
    }
}
