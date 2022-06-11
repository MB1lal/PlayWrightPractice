package frontend.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IMDBPages {

    private final Page page;
    private final Locator pageLogo;
    private final Locator allCastAndCrew;
    private final Locator castTable;
    private final Locator castTableRow;
   public IMDBPages(Page page) {
       this.page = page;
       this.pageLogo = page.locator("home_img");
       this.allCastAndCrew = page.locator("input[class='ipc-metadata-list-item__label ipc-metadata-list-item__label--link']");
       this.castTable = page.locator("//*[@id='fullcredits_content']/table[3]/tbody/tr");
       this.castTableRow = page.locator("//*[@id='fullcredits_content']/table[3]/tbody/tr");
   }

    public void pageHasLogo() {
       pageLogo.isVisible();
    }

    public void scrollToText(String elementText) {
       Locator targetElement = page.locator("text=" + elementText);
       targetElement.scrollIntoViewIfNeeded();
    }

    public void clickScrolledElement(String elementText) {
        Locator targetElement = page.locator("text=" + elementText);
        targetElement.click();
    }

    public List<List<String>> getCastTableData() {
        List<List<String>> castTableData = new ArrayList<>();
        List<String> tableData = castTableRow.allInnerTexts();
        List<String> rows = new ArrayList<>();

        for (String tableDatum : tableData) {
            if (!tableDatum.equalsIgnoreCase("")) {
                rows.add(tableDatum);
            }
        }
       for (int i=0; i<rows.size(); i++) {
           Scanner s = new Scanner(rows.get(i)).useDelimiter("[\\t\\n]");
           castTableData.add(new ArrayList<>());
           for(int j=0;j<4;j++) {
               String rowText = s.next();
               if(!rowText.equals("...")) {
                   castTableData.get(i).add(rowText);
               }
           }
       }
        return castTableData;
    }
}
