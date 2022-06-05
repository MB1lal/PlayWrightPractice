package backend.connectors;

import backend.steps.BaseSteps;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import core.EnvSerenity;
import utils.SharedState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class PetConnector {

    Playwright playwright = BaseSteps.playwright;

    private APIRequestContext baseRequest() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return playwright.request()
                .newContext(new APIRequest.NewContextOptions()
                        .setBaseURL(EnvSerenity.basePetURI)
                        .setExtraHTTPHeaders(headers)
                );
    }

    public void addNewPet(String body) {
        APIResponse addNewPet = baseRequest().post("",
                RequestOptions.create().setData(body));
        assertEquals(addNewPet.status(), 200);
    }

    public APIResponse getPetById(int id) {
        return baseRequest()
                .get(String.valueOf(id));
    }

    public APIResponse getPetStatus(List<String> status) {
        RequestOptions options = RequestOptions.create();
       status.forEach( items -> options.setQueryParam("status", items));
        return baseRequest()
                .get("findByStatus", options);
    }

    public void deletePetWithId(int petId) {
        APIResponse deleteResponse = baseRequest().delete(String.valueOf(petId));
       assertEquals(deleteResponse.status(),200);
    }

    public void updatePetDetails(String attribute, String attributeValue) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        APIRequestContext request = playwright.request().newContext(new APIRequest.NewContextOptions()
                        .setBaseURL(EnvSerenity.basePetURI)
                .setExtraHTTPHeaders(headers));
        FormData formData = FormData.create();
        formData.set(attribute,attributeValue);

        APIResponse updateResponse = request.post(String.valueOf(SharedState.PET_ID),
                RequestOptions.create().setForm(formData));
        assertEquals(200,updateResponse.status());
    }
}
