package backend.connectors;

import backend.steps.BaseSteps;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import core.Environment;
import core.PropertiesReader;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PetStoreConnector {

    Playwright playwright = BaseSteps.playwright;
    APIRequestContext request;

    private APIRequestContext baseRequest() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        request = playwright.request()
                .newContext(new APIRequest.NewContextOptions()
                        .setBaseURL(Environment.basePetStoreURI)
                        .setExtraHTTPHeaders(headers)
                );
        return request;
    }

    public void placingAnOrder(String body) {
        APIResponse addNewPet = baseRequest().post("order",
                RequestOptions.create().setData(body));
        assertTrue(addNewPet.ok());
        assertEquals(200, addNewPet.status() );
    }

    public APIResponse fetchOrder(int orderId) {
        APIResponse order = baseRequest().get("order/" + orderId);
        assertEquals(order.status(), 200);
        return order;
    }

    public void fetchInvalidOrder(int orderId) {
        APIResponse order = baseRequest().get("order/" + orderId);
        assertEquals(order.status(),404);
    }

    public void deleteOrderById(int orderId) {
         APIResponse order = baseRequest().delete("order/" + orderId);
         assertEquals(200,order.status());
    }

}
