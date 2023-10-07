import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {

    @Test
    public void testGetAPi() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }


    @Test
    public void testHelloWorldHello() {
        System.out.println("Hello from Deni");
    }
}