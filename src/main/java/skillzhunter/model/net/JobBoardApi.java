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

public class JobBoardApi {
    /** client for making request call.*/
    private static final OkHttpClient CLIENT = new OkHttpClient();

    /** object mapper for parsing json response.*/
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /** map for storing industries and their slugs.*/
    private static final Map<String, String> INDUSTRY_MAP = JobBoardApi.loadCsvData(
    Paths.get("data", "industries.csv").toString(), "industry", "slug");

    /** map for storing locations and their slugs.*/
    private static final Map<String, String> LOCATION_MAP = JobBoardApi.loadCsvData(
    Paths.get("data", "locations.csv").toString(), "location", "slug");

    /** Message to send as an error, defaults to empty.*/
    private String errorMessage = null;

    /** empty contructor.*/
    public JobBoardApi() {
        // empty constructor
    }
    

    /**
     * loads csv data into a map.
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
     * @return List<JobRecord> list of jobs given criteria
     */
    public JobBoardApiResult getJobBoard(String query) {
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
     * @return List<JobRecord> list of jobs given criteria
     */
    public JobBoardApiResult getJobBoard(String query, Integer numberOfResults) {
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
     * @return List<JobRecord> list of jobs given criteria
     */
    public JobBoardApiResult getJobBoard(String query, Integer numberOfResults, String location) {
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
     * The `location.csv` and `industry.csv` files are loaded into memory as maps (`locationsMap` and `industriesMap`),
     * ensuring that only valid locations and industries are used for lookups.
     *
     * @param query Search query for the job board.
     * @param numberOfResults Number of results to return.
     * @param location Location to filter jobs (e.g., city or region).
     * @param industry Industry to filter jobs (e.g., IT, healthcare).
     * @return List of job records matching the criteria.
     */
    public JobBoardApiResult getJobBoard(String query, Integer numberOfResults, String location, String industry) {
        // Reset error message at the start of each call
        errorMessage = null;
        
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
    
        List<JobRecord> jobs = this.searchApi(url);
        return new JobBoardApiResult(jobs, errorMessage);
    }

    /**
     * Replaces common HTML special characters in a string with more comprehensive handling.
     *
     * @param text Text with potential HTML special characters
     * @return Text with replaced HTML special characters
     */
    public static String replaceHtmlEntities(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Create a more comprehensive map of HTML entities and their replacements
        java.util.Map<String, String> htmlEntities = new java.util.HashMap<>();
        htmlEntities.put("&amp;", "&");
        htmlEntities.put("&lt;", "<");
        htmlEntities.put("&gt;", ">");
        htmlEntities.put("&quot;", "\"");
        htmlEntities.put("&#39;", "'");
        htmlEntities.put("&apos;", "'");
        htmlEntities.put("&nbsp;", " ");
        htmlEntities.put("&ndash;", "–");
        htmlEntities.put("&mdash;", "—");
        htmlEntities.put("&lsquo;", "'");
        htmlEntities.put("&rsquo;", "'");
        htmlEntities.put("&ldquo;", "\"");
        htmlEntities.put("&rdquo;", "\"");
        htmlEntities.put("&bull;", "•");
        htmlEntities.put("&copy;", "©");
        htmlEntities.put("&reg;", "®");
        htmlEntities.put("&trade;", "™");
        htmlEntities.put("&euro;", "€");
        htmlEntities.put("&pound;", "£");
        htmlEntities.put("&yen;", "¥");
        htmlEntities.put("&cent;", "¢");

        // First handle nested entities (like &amp;amp;) by applying multiple passes
        String result = text;
        String prevResult;
        do {
            prevResult = result;
            for (java.util.Map.Entry<String, String> entry : htmlEntities.entrySet()) {
                result = result.replace(entry.getKey(), entry.getValue());
            }
        } while (!result.equals(prevResult));

        // Handle numeric entities (like &#123;)
        java.util.regex.Pattern numericPattern = java.util.regex.Pattern.compile("&#(\\d+);");
        java.util.regex.Matcher numericMatcher = numericPattern.matcher(result);
        StringBuilder numericBuilder = new StringBuilder();
        while (numericMatcher.find()) {
            try {
                String numStr = numericMatcher.group(1);
                int code = Integer.parseInt(numStr);
                numericMatcher.appendReplacement(numericBuilder, String.valueOf((char) code));
            } catch (NumberFormatException e) {
                numericMatcher.appendReplacement(numericBuilder, numericMatcher.group(0));
            } catch (IllegalArgumentException e) {
                // In case of invalid replacement or any other issues
                numericMatcher.appendReplacement(numericBuilder, numericMatcher.group(0));
            }
        }
        numericMatcher.appendTail(numericBuilder);
        result = numericBuilder.toString();

        // Handle hexadecimal entities (like &#x1F600;)
        java.util.regex.Pattern hexPattern = java.util.regex.Pattern.compile("&#[xX]([0-9a-fA-F]+);");
        java.util.regex.Matcher hexMatcher = hexPattern.matcher(result);
        StringBuilder hexBuilder = new StringBuilder();
        while (hexMatcher.find()) {
            try {
                String hex = hexMatcher.group(1);
                int code = Integer.parseInt(hex, 16);
                String replacement = String.valueOf(Character.toChars(code));
                // Need to quote the replacement to avoid special character issues
                hexMatcher.appendReplacement(hexBuilder, java.util.regex.Matcher.quoteReplacement(replacement));
            } catch (Exception e) {
                hexMatcher.appendReplacement(hexBuilder, hexMatcher.group(0));
            }
        }
        hexMatcher.appendTail(hexBuilder);
        result = hexBuilder.toString();

        // Log a message if we detect any remaining HTML entities
        if (result.matches(".*&[a-zA-Z0-9#]+;.*")) {
            System.out.println("Warning: Possible unhandled HTML entity in: " + result);
        }

        return result;
    }

    /**
     * makes a request to the api and returns the response.
     * @param url url to make the request to
     * @return list of job records
     */
    protected List<JobRecord> searchApi(String url) {
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
            return jobResponse.jobs();

        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
    /**
     * main method for testing.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        JobBoardApi api = new JobBoardApi();
        JobBoardApiResult apiResults = api.getJobBoard("python", 5, "austria", "devops & sysadmin");
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
