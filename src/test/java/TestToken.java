import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestToken {
    private final String apiUrl;

    public TestToken(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String createJob() throws IOException {
        // Отправляем HTTP-запрос для создания задачи
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            // Задача создана успешно, получаем JSON-ответ
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();
        } else {
            throw new IOException("Failed to create a job. HTTP Response Code: " + responseCode);
        }
    }

    public String checkJobStatus(String token) throws IOException {
        // Отправляем HTTP-запрос для проверки статуса задачи по токену
        URL url = new URL(apiUrl + "?token=" + token);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            // Получаем JSON-ответ о статусе задачи
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();
        } else {
            throw new IOException("Failed to check job status. HTTP Response Code: " + responseCode);
        }
    }

    public static void main(String[] args) {
        String apiUrl = "https://playground.learnqa.ru/ajax/api/longtime_job";
        TestToken tester = new TestToken(apiUrl);

        try {
            // Создаем задачу
            String createJobResponse = tester.createJob();
            System.out.println("Create Job Response: " + createJobResponse);



            String token = "your_token_value";

            // Проверяем статус задачи по токену
            String jobStatusResponse = tester.checkJobStatus(token);
            System.out.println("Job Status Response: " + jobStatusResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}