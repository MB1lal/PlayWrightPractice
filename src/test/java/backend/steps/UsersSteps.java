package backend.steps;

import backend.connectors.UserConnector;
import backend.models.users.UserModel;
import com.microsoft.playwright.APIResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.SharedState;
import static org.junit.Assert.assertEquals;

public class UsersSteps extends BaseSteps{

    private UserConnector userConnector = new UserConnector();

    @Given("I create a user")
    public void i_create_a_user() {
        UserModel user = createUserPayLoad();
        userConnector.createNewUser(user.toJson());
    }

    @When("User is successfully created")
    public void user_is_successfully_created() {
        verifyUserExists();
    }

    @Then("I login using same user")
    public void i_login_using_same_user() {
        loginUser();
    }

    @Then("I logout using same user")
    public void i_logout_using_same_user() {
        logoutUser();
    }

    @Then("I delete the user")
    public void i_delete_user() {
        SharedState.USER_RESPONSE = userConnector.deleteUser(SharedState.UserData.USERNAME);
    }

    @And("User is successfully deleted")
    public void user_is_successfully_deleted() {
        APIResponse response = SharedState.USER_RESPONSE;
        assertEquals(response.status(),200);
    }

}
