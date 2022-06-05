package backend.steps;

import backend.connectors.PetConnector;
import backend.connectors.PetStoreConnector;
import backend.connectors.UserConnector;
import backend.models.pet.PetModel;
import backend.models.store.PetStoreModel;
import backend.models.users.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.microsoft.playwright.Playwright;
import core.EnvSerenity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import utils.SharedState;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;


public abstract class BaseSteps {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    private final PetConnector petConnector = new PetConnector();
    private final PetStoreConnector petStoreConnector = new PetStoreConnector();
    private final UserConnector userConnector = new UserConnector();

    public static Playwright playwright;

    public static final Logger logger = LogManager.getLogger(BaseSteps.class);

    public EasyRandom random = new EasyRandom(
            new EasyRandomParameters()
                    .seed(new Random().nextLong())
            //sensible string length
                    .stringLengthRange(5,50)
    );

    public PetModel createNewPetPayload() {
        return random.nextObject(PetModel.class);
    }
    public PetModel createPetPayloadUsingFile() throws IOException {
        logger.info("Creating a pet payload using sample json file");
        return getStaticBody(
            PetModel.class, EnvSerenity.petFileBodiesRoot + "new-pet.json");
    }

    public PetStoreModel createPetStorePayload() {
        PetStoreModel petStoreModel = new PetStoreModel();
        Faker faker = new Faker();

        petStoreModel.setId(faker.random().nextInt(0,1000));
        petStoreModel.setPetId(faker.hashCode());
        petStoreModel.setQuantity(4);


        return petStoreModel;
    }

    public static <T> T getStaticBody(Class<T> tClass, String path) throws IOException {
        return objectMapper.readValue(new File(path), tClass);
    }

    public void addANewPet(PetModel petModel) {
        SharedState.PET_STATUS = petModel.getStatus();
        SharedState.PET_ID = petModel.getId();
        petConnector.addNewPet(petModel.toJson());
    }

    public void getPetById(long petId) {
        SharedState.PET_RESPONSE = petConnector.getPetById((int) petId);
    }

    public void getPetStatus(List<String> status) {
        SharedState.PET_RESPONSE = petConnector.getPetStatus(status);
    }

    public void deletePetWithId(long petId) {
        petConnector.deletePetWithId((int) petId);
    }

    public void updatePetDetails(String attribute, String attributeValue) {
        petConnector.updatePetDetails(attribute, attributeValue);
    }

    public void placePetStoreOrder(PetStoreModel petStoreModel) {
        SharedState.PET_ORDER_ID = petStoreModel.getId();
        petStoreConnector.placingAnOrder(petStoreModel.toJson());
    }

    public void fetchPetStoreOrderDetails(int orderId) {
        SharedState.PET_STORE_RESPONSE = petStoreConnector.fetchOrder(orderId);
    }

    public void fetchDeletedOrder(int orderId) {
        petStoreConnector.fetchInvalidOrder(orderId);
    }

    public void deleteOrderById(int orderId) {
        petStoreConnector.deleteOrderById(orderId);
    }

    public UserModel createUserPayLoad() {
        UserModel userModel = new UserModel();
        Faker faker = new Faker();
        userModel.setId(faker.hashCode());
        userModel.setUsername(faker.name().username());
        userModel.setFirstName(faker.name().firstName());
        userModel.setLastName(faker.name().lastName());
        userModel.setEmail(faker.internet().emailAddress());
        userModel.setPassword(faker.internet().password());
        userModel.setPhone(faker.phoneNumber().cellPhone());
        userModel.setUserStatus(faker.random().nextInt(3));

        SharedState.UserData.USERNAME = userModel.getUsername();
        SharedState.UserData.PASSWORD = userModel.getPassword();
        SharedState.UserData.EMAIL = userModel.getEmail();
        SharedState.UserData.PHONE = userModel.getPhone();
        SharedState.UserData.FIRST_NAME = userModel.getFirstName();
        SharedState.UserData.LAST_NAME = userModel.getLastName();
        SharedState.UserData.STATUS = userModel.getUserStatus();
        SharedState.UserData.USER_ID = userModel.getId();

        return userModel;
    }

    public void verifyUserExists() {
        SharedState.USER_RESPONSE = userConnector.getUser(SharedState.UserData.USERNAME);
    }

    public void loginUser() {
        SharedState.USER_RESPONSE = userConnector.loginExistingUser(SharedState.UserData.USERNAME, SharedState.UserData.PASSWORD);
    }

    public void logoutUser() {
        userConnector.logoutUser();
    }

}
