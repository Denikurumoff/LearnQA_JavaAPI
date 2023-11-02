package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }
    @Step("Make a POST-request")
    public Response makePostRequest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }
    @Step("Создание пользователя с некорректным email")
    public static Response createUserWithInvalidEmail() {
        String invalidEmail = "invalidemail.com";  // Некорректный email без символа @
        DataGenerator.getRegistrationData().put("email", invalidEmail);

        return sendUserRegistrationRequest(DataGenerator.getRegistrationData());
    }

    @Step("Создание пользователя без указания поля: {missingField}")
    public static Response createUserWithoutField(String missingField) {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.remove(missingField);

        return sendUserRegistrationRequest(userData);
    }

    @Step("Создание пользователя с очень коротким именем")
    public static Response createUserWithShortName() {
        // Генерируем данные для регистрации пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.put("firstName", "a"); // Очень короткое имя (1 символ)

        return sendUserRegistrationRequest(userData);
    }

    @Step("Создание пользователя с очень длинным именем")
    public static Response createUserWithLongName() {
        // Генерируем данные для регистрации пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();
        StringBuilder longNameBuilder = new StringBuilder();

        for (int i = 0; i < 251; i++) {
            longNameBuilder.append("a");
        }

        userData.put("firstName", longNameBuilder.toString()); // Очень длинное имя (более 250 символов)

        return sendUserRegistrationRequest(userData);
    }

    private static Response sendUserRegistrationRequest(Map<String, String> userData) {
        return RestAssured.given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
    }
}