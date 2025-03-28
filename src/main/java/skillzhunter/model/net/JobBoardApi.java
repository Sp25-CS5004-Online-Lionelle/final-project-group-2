package skillzhunter.model.net;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import skillzhunter.model.JobRecord;
import skillzhunter.model.ResponseRecord;

public class JobBoardApi {

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JobBoardApi() {}

    public static List<JobRecord> getJobBoard(String query, Integer numberOfResults, String location, String industry) {

        String url = String.format("https://jobicy.com/api/v2/remote-jobs?count=%s&industry=%s&tag=%s", numberOfResults, industry, query);

        Request request = new Request.Builder()
            .url(url)
            .addHeader("User-Agent", "job_finder")
            .addHeader("User-IP", "127.0.0.1") // Replace with actual user IP if available
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Error: HTTP " + response.code());
                return Collections.emptyList();
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                System.err.println("Error: Response body is null");
                return Collections.emptyList();
            }

            String jsonResponse = responseBody.string();
            ResponseRecord jobResponse = objectMapper.readValue(jsonResponse, ResponseRecord.class);
            return jobResponse.jobs();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static void main(String[] args) {
        List<JobRecord> jobs = JobBoardApi.getJobBoard("Data Science", 10, "USA", "business");
        jobs.forEach(job -> System.out.println("Job Title: " + job.jobTitle()));
    }

}
