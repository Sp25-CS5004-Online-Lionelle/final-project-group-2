package skillzhunter.model.net;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import skillzhunter.model.JobRecord;
import skillzhunter.model.ResponseRecord;

public class JobBoardApi {
    /** client for making request call*/
    private static final OkHttpClient client = new OkHttpClient();
    /** object mapper for parsing json response*/
    private static final ObjectMapper objectMapper = new ObjectMapper();
    /** map for storing industries and their slugs*/
    private static final Map<String, String> industriesMap = loadCsvData("data\\industries.csv", "industry", "slug");
    /** map for storing locations and their slugs*/
    private static final Map<String, String> locationsMap = loadCsvData("data\\locations.csv", "location", "slug");
    /** empty contructor*/
    public JobBoardApi() {}
    
    
    /**
     * retries pretty name for industry.
     * @return List<String> pretty name for industry
     */
    public static List<String> getIndustries() {
        return industriesMap.keySet().stream().toList();
    }

    /**
     * retries pretty name for location.
     * @return List<String> pretty name for location
     */
    public static List<String> getLocations() {
        return locationsMap.keySet().stream().toList();
    }

    /** loads csv data into a map
     * @param filePath path to the csv file
     * @param key column name for the key
     * @param value column name for the value
     * @return map of key-value pairs from the csv file
     */
    public static  Map<String, String>  loadCsvData(String filePath, String key, String value) {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        Map<String, String> myMap = new HashMap<>();

        try {
            MappingIterator<Map<String, String>> it = csvMapper.readerFor(Map.class)
                    .with(schema)
                    .readValues(new File(filePath));

            while (it.hasNext()) {
                Map<String, String> row = it.next();
                String industry = row.get(key).toLowerCase().trim();
                String slug = row.get(value).trim();
                myMap.put(industry, slug);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return myMap;
    }

    /** gets the job board with default values
     * @param query search query
     * @return list of job records
     */
    public static List<JobRecord> getJobBoard(String query){
        List<JobRecord> jobs  = getJobBoard(query, 5);
        return jobs;
    }
    /** gets the job board with default values
     * @param query search query
     * @param numberOfResults number of results to return
     * @return list of job records
     */
    public static List<JobRecord> getJobBoard(String query, Integer numberOfResults){
        List<JobRecord> jobs = getJobBoard(query, 5, "anywhere", "any");
        return jobs;
    }

    /**
     * Retrieves job records based on the provided query parameters.
     * If any parameter is invalid or not provided, default values are applied:
     * - `query`: Defaults to "all" if null, empty, or generic terms like "any" or "all" are passed.
     * - `numberOfResults`: Defaults to 5 if null or less than 1.
     * - `location`: Must match an entry in `location.csv`. Defaults to "anywhere" if not found.
     * - `industry`: Must match an entry in `industry.csv`. Defaults to "any" if not found.
     *
     * The `location.csv` and `industry.csv` files are loaded into memory as maps (`locationsMap` and `industriesMap`),
     * ensuring that only valid locations and industries are used for lookups.
     *
     * @param query Search query for the job board.
     * @param numberOfResults Number of results to return.
     * @param location Location to filter jobs (e.g., city or region).
     * @param industry Industry to filter jobs (e.g., IT, healthcare).
     * @return List of job records matching the criteria.
     */
    public static List<JobRecord> getJobBoard(String query, Integer numberOfResults, String location, String industry){
        // getting slug for request, if it breaks we default
        location = locationsMap.get(location.toLowerCase().trim());
        industry = industriesMap.get(industry.toLowerCase().trim());
        Boolean location_passed = location != null && !location.isEmpty() && !location.equalsIgnoreCase("anywhere");
        Boolean industry_passed = industry != null && !industry.isEmpty() && !industry.equalsIgnoreCase("all")&& !industry.equalsIgnoreCase("any");

        //defualting and slugggin query
        if (query == null || query.isEmpty() || query.equalsIgnoreCase("any") || query.equalsIgnoreCase("all") || query.equalsIgnoreCase("all jobs") || query.equalsIgnoreCase("all job")) {
            System.out.println("No query passed, using default values.");
            query = "all";
        }
        if (numberOfResults == null || numberOfResults < 1) {
            numberOfResults = 5;
        }
        query = query.replaceAll(" ", "+").toLowerCase();

        // building url
        String url;
        if (location_passed && industry_passed) {
            url = String.format("https://jobicy.com/api/v2/remote-jobs?count=%s&geo=%s&industry=%s&tag=%s", numberOfResults, location, industry, query);
        } else if (location_passed) {
            url = String.format("https://jobicy.com/api/v2/remote-jobs?count=%s&geo=%s&tag=%s", numberOfResults, location, query);
        } else if (industry_passed) {
            url = String.format("https://jobicy.com/api/v2/remote-jobs?count=%s&industry=%s&tag=%s", numberOfResults, industry, query);
        }  else {
            url = String.format("https://jobicy.com/api/v2/remote-jobs?count=%s&tag=%s", numberOfResults, query);
        }
        System.err.println("URL: " + url);
    
        List<JobRecord> jobs = searchApi(url);
        return jobs;
    }
    
    /** makes a request to the api and returns the response
     * @param url url to make the request to
     * @return list of job records
     */
    private static List<JobRecord> searchApi(String url) {
        Request request = new Request.Builder()
            .url(url)
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

        JobBoardApi api = new JobBoardApi();
        List<JobRecord> results = api.getJobBoard("python", 5, "all", "all");
        System.out.println("Job Records: " + results.size());
        for (JobRecord job : results) {
            System.out.println("Job Title: " + job.jobTitle());
            System.out.println("Company: " + job.companyName());
            System.out.println("Location: " + job.jobGeo());
            System.out.println("-----------------------------");
        }
        
        
    }

}
