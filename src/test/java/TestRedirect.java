import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;


public class TestRedirect {

    @Test
    public void testGetRedirectUrl() {
        Response response = RestAssured.get("https://playground.learnqa.ru/api/long_redirect");
        String redirectUrl = response.getHeader("Location");
        System.out.println("Redirect URL: " + redirectUrl);
    }
}