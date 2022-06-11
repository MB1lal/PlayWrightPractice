package backend.connectors;

import backend.steps.BaseSteps;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import core.Environment;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UserConnector {

    Playwright playwright = BaseSteps.playwright;

    private APIRequestContext baseRequest() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return playwright.request()
                .newContext(new APIRequest.NewContextOptions()
                        .setBaseURL(Environment.userURI)
                        .setExtraHTTPHeaders(headers)
                );
    }

    public APIResponse getUser(String username) {
        APIResponse user = baseRequest().get(username);
        assertEquals(200,user.status());
        return user;
    }

    public APIResponse loginExistingUser(String username, String password) {
        FormData formData = FormData.create();
        formData.set("username",username);
        formData.set("password", password);

        APIResponse loginResponse = baseRequest().get("login",
                RequestOptions.create().setForm(formData));
        assertEquals(200,loginResponse.status());
        return loginResponse;
    }

    public void logoutUser() {
        APIResponse response = baseRequest().get("logout");
        assertEquals(200,response.status());
    }

    public void createNewUser(String user) {
        APIResponse response = baseRequest().post("",RequestOptions.create().setData(user));
        assertEquals(200,response.status());
    }

    public APIResponse deleteUser(String username) {
        return baseRequest()
                .delete(username);
    }
}
