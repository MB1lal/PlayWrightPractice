package backend.steps;

import backend.models.store.PetStoreModel;
import com.microsoft.playwright.APIResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.SharedState;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
public class PetStoreSteps extends BaseSteps {


    @Given("I place an order on pet store with id = {int}")
    public void placingOrderOnPetStore(int orderId) {
        PetStoreModel petStoreModel = createPetStorePayload();
        petStoreModel.setId(orderId);
        SharedState.PET_STORE_ID = orderId;
        placePetStoreOrder(petStoreModel);
    }

    @When("I fetch the order using id = {int}")
    public void petStoreOrderStatusIsCalled(int orderId) {
        fetchPetStoreOrderDetails(orderId);
    }

    @Then("The order is successfully placed")
    public void assertingOrderIsSuccessfullyPlaced() throws IOException {
        APIResponse response = SharedState.PET_STORE_RESPONSE;

        PetStoreModel petStoreModel = objectMapper.readValue(response.text(),PetStoreModel.class);

        assertEquals(petStoreModel.getId(), SharedState.PET_STORE_ID);
    }

    @When("I delete the order by id = {int}")
    public void deleteByOrderId(int orderId) {
        deleteOrderById(orderId);
    }

    @And("The order with id = {int} shouldn't exist")
    public void assertOrderDoesNotExist(int orderId) {
        fetchDeletedOrder(orderId);
    }


}
