import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.DataGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.HashMap;
import java.util.Map;

@Epic("User Data Deletion")
@Feature("Delete User Data")
public class UserDeleteTest extends BaseTestCase {

    private ApiCoreRequests apiCoreRequests;

    @BeforeAll
    public void setup() {
        apiCoreRequests = new ApiCoreRequests();
    }

    @BeforeEach
    public void setupTest() {
        // Add any common setup steps for tests if needed
    }

    @Test
    @Description("Попытка удаления пользователя без прав на удаление")
    @DisplayName("Attempt to Delete User Without Permission")
    @Story("Negative Cases")
    public void testDeleteUserWithoutPermission() {
        // Подготавливает данные для аутентификации (email и пароль)
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        // Выполняет POST-запрос для аутентификации и получения токенов
        Response responseGetAuth = apiCoreRequests.authenticateUser(authData);

        // Попытка удаления пользователя с ID = 2
        int targetUserId = 2; // ID пользователя, которого попытаемся удалить
        Response responseDeleteUser = apiCoreRequests.deleteUser(responseGetAuth, targetUserId);

        // Проверяем, что запрос вернул статус 403 Forbidden
        Assertions.assertResponseCodeEquals(responseDeleteUser, 403);
    }

    @Test
    @Description("Успешное удаление пользователя и проверка отсутствия данных пользователя")
    @DisplayName("Successful User Deletion and Verification of User Data Absence")
    @Story("Positive Cases")
    public void testSuccessfulUserDeletion() {
        // Создаем нового пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = RestAssured.given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
        String userId = responseCreateAuth.getString("id");

        // Подготавливает данные для аутентификации (email и пароль)
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        // Выполняет POST-запрос для аутентификации и получения токенов
        Response responseGetAuth = apiCoreRequests.authenticateUser(authData);

        // Удаляем пользователя
        Response responseDeleteUser = apiCoreRequests.deleteUser(responseGetAuth, Integer.parseInt(userId));

        // Попытка получить данные удаленного пользователя по его ID
        Response responseGetDeletedUser = apiCoreRequests.getUserData(responseGetAuth, Integer.parseInt(userId));

        // Проверяем, что запрос на получение вернул статус 404 Not Found
        Assertions.assertResponseCodeEquals(responseGetDeletedUser, 404);
    }

    @Test
    @Description("Попытка удаления пользователя другим пользователем")
    @DisplayName("Attempt to Delete User by Another User")
    @Story("Negative Cases")
    public void testDeleteUserByAnotherUser() {
        // Подготавливает данные для аутентификации (email и пароль)
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "anotheruser@example.com"); // Замените на email другого пользователя
        authData.put("password", "password"); // Замените на пароль другого пользователя

        // Выполняет POST-запрос для аутентификации и получения токенов
        Response responseGetAuth = apiCoreRequests.authenticateUser(authData);

        // Попытка удаления пользователя с ID = 2
        int targetUserId = 2; // ID пользователя, которого попытаемся удалить
        Response responseDeleteUser = apiCoreRequests.deleteUser(responseGetAuth, targetUserId);

        // Проверяем, что запрос вернул статус 403 Forbidden
        Assertions.assertResponseCodeEquals(responseDeleteUser, 403);
    }
}