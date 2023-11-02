import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.DisplayName;
import lib.ApiCoreRequests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);


        Response responseCreateAuth = RestAssured.given()
                .body(userData)
                .post("https://playground.Learnga.ru/api/user/")
                .andReturn();

        // Assuming that you have defined the expectedStatusCode as an int.
        int expectedStatusCode = 400;

        Assertions.assertResponseCodeEquals(responseCreateAuth, expectedStatusCode);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    public void testCreateUserSuccessfully() {
        String email = DataGenerator.getRandomEmail();
        Map<String, String> userData = DataGenerator.getRegistrationData();


        Response responseCreateAuth = RestAssured.given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }



    private ApiCoreRequests apiCoreRequests;

    @Test
    @DisplayName("Create User with Invalid Email")
    public void testCreateUserWithInvalidEmail() {
        apiCoreRequests.createUserWithInvalidEmail();
    }

    @Test
    @DisplayName("Create User Without Field")
    public void testCreateUserWithoutField() {
        apiCoreRequests.createUserWithoutField("email");
    }

    @Test
    @DisplayName("Create User with Short Name")
    public void testCreateUserWithShortName() {
        apiCoreRequests.createUserWithShortName();
    }

    @Test
    @DisplayName("Create User with Long Name")
    public void testCreateUserWithLongName() {
        apiCoreRequests.createUserWithLongName();
    }
}