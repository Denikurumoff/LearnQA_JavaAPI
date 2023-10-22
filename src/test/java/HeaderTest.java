import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeaderTest {

    @Test
    public void HeaderTestValue() {
        RestAssured.baseURI = "https://playground.learnqa.ru/api/homework_header";
        Response response = RestAssured.get();
        assertEquals(200, response.getStatusCode());
        String expectedHeaderName = "X-Header-Name";
        String expectedHeaderValue = "Expected-Value";
        String actualHeaderValue = response.getHeader(expectedHeaderName);
        assertEquals(expectedHeaderValue, actualHeaderValue);
        System.out.println("Response Headers:");
        response.getHeaders().forEach(header -> System.out.println(header.getName() + ": " + header.getValue()));
    }
}