import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

public class CookieTest {

    @Test
    public void testCookieValue() {

        RestAssured.baseURI = "https://playground.learnqa.ru";
        Response response = RestAssured.get("/api/homework_cookie");
        assertEquals(200, response.getStatusCode());

        String cookieValue = response.getCookie("your_cookie_name");
        assertEquals("ExpectedCookieValue", cookieValue);
    }
}