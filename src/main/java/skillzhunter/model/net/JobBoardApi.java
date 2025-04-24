package skillzhunter.model.net;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import skillzhunter.model.JobBean;
import skillzhunter.model.JobRecord;
import skillzhunter.model.ResponseRecord;

/**
 * Utility class for making API calls to the Job Board.
 * This class provides static methods for fetching job listings from the API.
 */
public final class JobBoardApi {
    /** client for making request call.*/
    private static final OkHttpClient CLIENT = new OkHttpClient();

    /** object mapper for parsing json response.*/
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /** map for storing industries and their slugs.*/
    private static final Map<String, String> INDUSTRY_MAP = loadCsvData(
    Paths.get("data", "industries.csv").toString(), "industry", "slug");

    /** map for storing locations and their slugs.*/
    private static final Map<String, String> LOCATION_MAP = loadCsvData(
    Paths.get("data", "locations.csv").toString(), "location", "slug");

    /** 
     * Private constructor to prevent instantiation of utility class.
     */
    private JobBoardApi() {
        // Prevent instantiation
    }
    
    /**
     * Loads csv data into a map.
     * Decodes HTML entities in the key and value before storing them.
     * @param filePath path to the csv file
     * @param key column name for the key
     * @param value column name for the value
     * @return map of key-value pairs from the csv file
     */
    public static Map<String, String> loadCsvData(String filePath, String key, String value) {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        Map<String, String> myMap = new HashMap<>();

        try {
            MappingIterator<Map<String, String>> it = csvMapper.readerFor(Map.class)
                    .with(schema)
                    .readValues(new File(filePath));

            while (it.hasNext()) {
                Map<String, String> row = it.next();
                
                // Decode HTML entities in the key value before storing
                String rawKey = row.get(key);
                String decodedKey = (rawKey != null) ? 
                    skillzhunter.model.formatters.DataFormatter.replaceHtmlEntities(rawKey.toLowerCase().trim()) :
                    null;
                    
                // Decode HTML entities in the value before storing    
                String rawValue = row.get(value);
                String decodedValue = (rawValue != null) ?
                    skillzhunter.model.formatters.DataFormatter.replaceHtmlEntities(rawValue.trim()) :
                    null;
                    
                if (decodedKey != null && decodedValue != null) {
                    myMap.put(decodedKey, decodedValue);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + filePath, e);
            // e.printStackTrace();
        }
        return myMap;
    }

    // Optional overloads — these go here:
    /**
     * Retrieves job records based on the provided query parameters.
     * If any parameter is invalid or not provided, default values are applied:
     * - `query`: Defaults to "all" if null, empty, or generic terms like "any" or "all" are passed.
     * - `numberOfResults`: Defaults to 5 if null or less than 1.
     * - `location`: Must match an entry in `location.csv`. Defaults to "anywhere" if not found.
     * - `industry`: Must match an entry in `industry.csv`. Defaults to "any" if not found.
     * @param query
     * @return JobBoardApiResult containing list of jobs given criteria and any error messages
     */
    public static JobBoardApiResult getJobBoard(String query) {
        return getJobBoard(query, null, null, null);
    }

    /**
     * Retrieves job records based on the provided query parameters.
     * If any parameter is invalid or not provided, default values are applied:
     * - `query`: Defaults to "all" if null, empty, or generic terms like "any" or "all" are passed.
     * - `numberOfResults`: Defaults to 5 if null or less than 1.
     * - `location`: Must match an entry in `location.csv`. Defaults to "anywhere" if not found.
     * - `industry`: Must match an entry in `industry.csv`. Defaults to "any" if not found.
     * @param query
     * @param numberOfResults
     * @return JobBoardApiResult containing list of jobs given criteria and any error messages
     */
    public static JobBoardApiResult getJobBoard(String query, Integer numberOfResults) {
        return getJobBoard(query, numberOfResults, null, null);
    }

    /**
     * Retrieves job records based on the provided query parameters.
     * If any parameter is invalid or not provided, default values are applied:
     * - `query`: Defaults to "all" if null, empty, or generic terms like "any" or "all" are passed.
     * - `numberOfResults`: Defaults to 5 if null or less than 1.
     * - `location`: Must match an entry in `location.csv`. Defaults to "anywhere" if not found.
     * - `industry`: Must match an entry in `industry.csv`. Defaults to "any" if not found.
     * @param query
     * @param numberOfResults
     * @param location
     * @return JobBoardApiResult containing list of jobs given criteria and any error messages
     */
    public static JobBoardApiResult getJobBoard(String query, Integer numberOfResults, String location) {
        return getJobBoard(query, numberOfResults, location, null);
    }
    
    /**
     * Retrieves job records based on the provided query parameters.
     * If any parameter is invalid or not provided, default values are applied:
     * - `query`: Defaults to "all" if null, empty, or generic terms like "any" or "all" are passed.
     * - `numberOfResults`: Defaults to 5 if null or less than 1.
     * - `location`: Must match an entry in `location.csv`. Defaults to "anywhere" if not found.
     * - `industry`: Must match an entry in `industry.csv`. Defaults to "any" if not found.
     *
     * The `location.csv` and `industry.csv` files are loaded into memory as maps (`LOCATION_MAP` and `INDUSTRY_MAP`),
     * ensuring that only valid locations and industries are used for lookups.
     *
     * @param query Search query for the job board.
     * @param numberOfResults Number of results to return.
     * @param location Location to filter jobs (e.g., city or region).
     * @param industry Industry to filter jobs (e.g., IT, healthcare).
     * @return JobBoardApiResult containing list of jobs and any error message.
     */
    public static JobBoardApiResult getJobBoard(String query, Integer numberOfResults, String location, String industry) {
        // Initialize error message as null
        String errorMessage = null;
        
        // getting slug for request, if it breaks we default
        if (location != null) {
            location = LOCATION_MAP.get(location.toLowerCase().trim());
        }
        if (industry != null) {
            industry = INDUSTRY_MAP.get(industry.toLowerCase().trim());
        }
        Boolean locationPassed = location != null && !location.isEmpty()
                                 && !location.equalsIgnoreCase("anywhere");

        Boolean industryPassed = industry != null && !industry.isEmpty()
                                && !industry.equalsIgnoreCase("all")
                                && !industry.equalsIgnoreCase("any");

        // Check if query is generic
        boolean isQueryGeneric = query == null || query.isEmpty()
                              || query.equalsIgnoreCase("any")
                              || query.equalsIgnoreCase("all")
                              || query.equalsIgnoreCase("all jobs")
                              || query.equalsIgnoreCase("all job");
                              
        // Check if location is generic
        boolean isLocationGeneric = location == null || location.isEmpty()
                                 || location.equalsIgnoreCase("any")
                                 || location.equalsIgnoreCase("all")
                                 || location.equalsIgnoreCase("anywhere");
                                 
        // Check if industry is generic
        boolean isIndustryGeneric = industry == null || industry.isEmpty()
                                 || industry.equalsIgnoreCase("all")
                                 || industry.equalsIgnoreCase("any");
        
        // Only set error message if ALL parameters are generic
        if (isQueryGeneric && isLocationGeneric && isIndustryGeneric) {
            System.out.println("All generic search parameters, using default values.");
            errorMessage = "All search parameters were generic, showing " + 
                          (numberOfResults != null ? numberOfResults : 5) + " available jobs.";
        }
        
        // Set default query if needed, regardless of error message
        if (isQueryGeneric) {
            query = "all";
        }

        if (numberOfResults == null || numberOfResults < 1) {
            numberOfResults = 5;
        }
        if (query != null) {
            query = query.replaceAll(" ", "+").toLowerCase();
        }

        // building url
        String url;
        if (locationPassed && industryPassed) {
            url = String.format("https://jobicy.com/api/v2/remote-jobs?count=%s&geo=%s&industry=%s&tag=%s",
                                numberOfResults, location, industry, query);
        } else if (locationPassed) {
            url = String.format("https://jobicy.com/api/v2/remote-jobs?count=%s&geo=%s&tag=%s",
                                numberOfResults, location, query);
        } else if (industryPassed) {
            url = String.format("https://jobicy.com/api/v2/remote-jobs?count=%s&industry=%s&tag=%s",
                                    numberOfResults, industry, query);
        }  else {
            url = String.format("https://jobicy.com/api/v2/remote-jobs?count=%s&tag=%s", numberOfResults, query);
        }
        System.err.println("URL: " + url);
    
        List<JobRecord> jobs = searchApi(url);
        return new JobBoardApiResult(jobs, errorMessage);
    }

    /**
     * Makes a request to the API and returns the response.
     * @param url URL to make the request to
     * @return List of job records
     */
    public static List<JobRecord> searchApi(String url) {
        Request request = new Request.Builder()
            .url(url)
            .build();

        try (Response response = CLIENT.newCall(request).execute()) {
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
            ResponseRecord jobResponse = OBJECT_MAPPER.readValue(jsonResponse, ResponseRecord.class);
            // Clean HTML entities in each job record right after receiving them
            return jobResponse.jobs().stream()
                .map(job -> skillzhunter.model.formatters.DataFormatter.processJobHtml(job))
                .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Main method for testing.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        JobBoardApiResult apiResults = getJobBoard("python", 5, "austria", "devops & sysadmin");
        List<JobRecord> results = apiResults.getJobs();
        System.out.println("Job Records: " + results.size());
        for (JobRecord job : results) {
            System.out.println("Job Title: " + job.jobTitle());
            System.out.println("Company: " + job.companyName());
            System.out.println("Location: " + job.jobGeo());
            System.out.println("-----------------------------");
        }
    }
}
