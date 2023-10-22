package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import lib.BaseTestCase;
import lib.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthTest extends BaseTestCase {
    String cookie;
    String header;
    int userIdOnAuth;

    @BeforeEach
    public void loginUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = responseGetAuth.jsonPath().getInt("user_id");
    }

    @Test
    public void testAuthUser() {
        JsonPath responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", this.header)
                .cookie("auth_sid", this.cookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();

        int userIdOnCheck = responseCheckAuth.getInt("user_id");
        assertTrue(userIdOnCheck > 0, "Unexpected user id: " + userIdOnCheck);
        assertEquals(userIdOnAuth, userIdOnCheck, "User id from auth request is not equal to user_id from check request");
    }
}