package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import lib.ApiCoreRequests;

public class UserGetTest extends BaseTestCase {
    @Test
    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.Learnqa.ru/api/user/2")
                .andReturn();

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test
    public void testGetUserDetailsAuthAsSameUser() {
        // Prepare authentication data
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        // Perform authentication and get CSRF token and auth_sid
        Response responseGetAuth = RestAssured.given()
                .body(authData)
                .post("https://playground.Learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        // Make an authenticated GET request to retrieve user details
        Response responseUserData = RestAssured.given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnga.ru/api/user/2")
                .andReturn();
        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields (responseUserData, expectedFields);


    }

    @Test
    @Description("Получение данных другого пользователя после аутентификации")
    @DisplayName("Get Another User Data After Authentication")
    public void testGetAnotherUserDataAfterAuth() {
        // Подготавливает данные для аутентификации (email и пароль)
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        // Выполняет POST-запрос для аутентификации и получения токенов
        Response responseGetAuth = RestAssured.given()
                .body(authData)
                .post("https://playground.Learnqa.ru/api/user/login")
                .andReturn();

        String authToken = this.getHeader(responseGetAuth, "x-csrf-token");

        // Выполняет GET-запрос для получения данных другого пользователя
        int targetUserId = 2; // Замените на ID другого пользователя
        Response responseUserData = apiCoreRequests.getUserDataForAnotherUser(authToken, targetUserId);

        // Проверяет, что в ответе присутствует только поле "username"
        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }
}
}