import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

public class UserEditTest extends BaseTestCase {
    @Test
    public void testEditJustCreatedTest() {
        // Generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = RestAssured.given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
        String userId = responseCreateAuth.getString("id");

        // LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnga.ru/api/user/login")
                .andReturn();

        // EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        // GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        Assertions.assertJsonByName(responseUserData, "fristName", newName);
    }

    @Test
    @Description("Попытка изменения данных пользователя без аутентификации")
    @DisplayName("Attempt to Edit User Data Without Authentication")
    public void testEditUserDataWithoutAuth() {
        // Генерируем данные для изменения пользователя
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "New Name");

        // Отправляем PUT-запрос без аутентификации
        int targetUserId = 2; // Замените на ID пользователя, чьи данные вы хотите изменить
        Response responseEditUser = apiCoreRequests.editUserDataWithoutAuth(targetUserId, editData);

        // Проверяем, что запрос вернул статус 401 Unauthorized
        Assertions.assertResponseCodeEquals(responseEditUser, 401);
    }

    @Test
    @Description("Попытка изменения данных другого пользователя")
    @DisplayName("Attempt to Edit Another User's Data")
    public void testEditAnotherUser() {
        // Подготавливает данные для аутентификации (email и пароль)
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "anotheruser@example.com"); // Замените на email другого пользователя
        authData.put("password", "password"); // Замените на пароль другого пользователя

        // Выполняет POST-запрос для аутентификации и получения токенов
        Response responseGetAuth = apiCoreRequests.authenticateUser(authData);

        // Генерируем данные для изменения пользователя
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "New Name");

        // Отправляем PUT-запрос для изменения данных другого пользователя
        int targetUserId = 2; // Замените на ID пользователя, чьи данные вы хотите изменить
        Response responseEditUser = apiCoreRequests.editUserData(responseGetAuth, targetUserId, editData);

        // Проверяем, что запрос вернул статус 403 Forbidden
        Assertions.assertResponseCodeEquals(responseEditUser, 403);
    }

    @Test
    @Description("Попытка изменения email без символа @")
    @DisplayName("Attempt to Edit Email Without @ Symbol")
    public void testEditEmailWithoutAtSymbol() {
        // Подготавливает данные для аутентификации (email и пароль)
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        // Выполняет POST-запрос для аутентификации и получения токенов
        Response responseGetAuth = apiCoreRequests.authenticateUser(authData);

        // Генерируем данные для изменения email
        Map<String, String> editData = new HashMap<>();
        editData.put("email", "invalid_email"); // Новый email без символа @

        // Отправляем PUT-запрос для изменения email
        int targetUserId = 2; // Замените на ID пользователя, чьи данные вы хотите изменить
        Response responseEditUser = apiCoreRequests.editUserData(responseGetAuth, targetUserId, editData);

        // Проверяем, что запрос вернул статус 400 Bad Request
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
    }

    @Test
    @Description("Попытка изменения firstName на очень короткое значение")
    @DisplayName("Attempt to Edit FirstName with Very Short Value")
    public void testEditFirstNameWithShortValue() {
        // Подготавливает данные для аутентификации (email и пароль)
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        // Выполняет POST-запрос для аутентификации и получения токенов
        Response responseGetAuth = apiCoreRequests.authenticateUser(authData);

        // Генерируем данные для изменения firstName
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "a"); // Очень короткое имя (1 символ)

        // Отправляем PUT-запрос для изменения firstName
        int targetUserId = 2; // Замените на ID пользователя, чьи данные вы хотите изменить
        Response responseEditUser = apiCoreRequests.editUserData(responseGetAuth, targetUserId, editData);

        // Проверяем, что запрос вернул статус 400 Bad Request
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
    }
}
}