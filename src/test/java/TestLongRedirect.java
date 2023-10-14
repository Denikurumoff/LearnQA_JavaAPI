import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class TestLongRedirect {
    @Test
    public void testFollowRedirects() {
        String initialUrl = "https://playground.learnqa.ru/api/long_redirect";
        int redirectCount = 0;

        while (true) {
            Response response = RestAssured.get(initialUrl);
            if (response.getStatusCode() == 200) {
                System.out.println("Final URL: " + initialUrl);
                break;
            }
            if (response.getStatusCode() != 302) {
                System.out.println("Unexpected response code: " + response.getStatusCode());
                break;
            }
            initialUrl = response.getHeader("Location");
            System.out.println("Redirect URL #" + redirectCount + ": " + initialUrl);
            redirectCount++;
            if (redirectCount > 10) {
                System.out.println("Exceeded maximum redirect limit");
                break;
            }
        }

        System.out.println("Total redirects: " + redirectCount);
    }
}

