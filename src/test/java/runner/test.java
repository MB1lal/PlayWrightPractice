package runner;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import core.EnvSerenity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class test {

    public static void main(String[] args) {

        Playwright playwright = Playwright.create();
        APIRequestContext request;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        request = playwright.request()
                .newContext(new APIRequest.NewContextOptions()
                        .setBaseURL("https://petstore.swagger.io/v2/store/")
                        .setExtraHTTPHeaders(headers)
                );
        Map<String, String> body = new HashMap<>();
        body.put("id","400");
        body.put("petId","731368479");
        body.put("quantity", "4");
        body.put("status","placed");
        body.put("complete", "true");
        APIResponse addNewPet = request.post("order",
                RequestOptions.create().setData(body));
        System.out.println(addNewPet.text());
        assertTrue(addNewPet.ok());
        assertEquals(200, addNewPet.status());

        playwright.close();


    }

}

