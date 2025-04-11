package skillzhunter.model.net;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.checkerframework.common.returnsreceiver.qual.This;

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
    /** client for making request call*/
    private static final OkHttpClient client = new OkHttpClient();
    /** object mapper for parsing json response*/
    private static final ObjectMapper objectMapper = new ObjectMapper();
    /** map for storing industries and their slugs*/
    private static final Map<String, String> industriesMap = JobBoardApi.loadCsvData(
    Paths.get("data", "industries.csv").toString(), "industry", "slug");
    /** map for storing locations and their slugs*/
    private static final Map<String, String> locationsMap = JobBoardApi.loadCsvData(
    Paths.get("data", "locations.csv").toString(), "location", "slug");

    /** empty contructor*/
    public JobBoardApi() {}
    

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

    // Optional overloads — these go here:
    /**
     * Retrieves job records based on the provided query parameters.
     * If any parameter is invalid or not provided, default values are applied:
     * - `query`: Defaults to "all" if null, empty, or generic terms like "any" or "all" are passed.
     * - `numberOfResults`: Defaults to 5 if null or less than 1.
     * - `location`: Must match an entry in `location.csv`. Defaults to "anywhere" if not found.
     * - `industry`: Must match an entry in `industry.csv`. Defaults to "any" if not found.
     * @param query
     * @return
     */
    public List<JobRecord> getJobBoard(String query) {
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
     * @return
     */
    public List<JobRecord> getJobBoard(String query, Integer numberOfResults) {
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
     * @return
     */
    public List<JobRecord> getJobBoard(String query, Integer numberOfResults, String location) {
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
    public List<JobRecord> getJobBoard(String query, Integer numberOfResults, String location, String industry){
        // getting slug for request, if it breaks we default
        if(location != null) {
            location = locationsMap.get(location.toLowerCase().trim());
        }
        if(industry != null) {
            industry = industriesMap.get(industry.toLowerCase().trim());
        }
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
    
        List<JobRecord> jobs = this.searchApi(url);
        return processJobRecords(jobs);
    }
    
    /**
     * Process job records to replace HTML special characters with improved debugging
     * 
     * @param jobs List of job records from API
     * @return List of job records with decoded HTML special characters
     */
    private List<JobRecord> processJobRecords(List<JobRecord> jobs) {
        System.out.println("Processing " + jobs.size() + " job records to replace HTML entities");
        
        List<JobRecord> processedJobs = jobs.stream().map(job -> {
            // Create a new JobBean and copy all values
            JobBean bean = new JobBean();
            bean.setId(job.id());
            bean.setUrl(job.url());
            bean.setJobSlug(job.jobSlug());
            
            // Process and log the job title
            String originalTitle = job.jobTitle();
            String processedTitle = replaceHtmlEntities(originalTitle);
            if (!originalTitle.equals(processedTitle)) {
                System.out.println("Replaced HTML entities in job title: '" + originalTitle + "' -> '" + processedTitle + "'");
            }
            bean.setJobTitle(processedTitle);
            
            // Process and log company name
            String originalCompanyName = job.companyName();
            String processedCompanyName = replaceHtmlEntities(originalCompanyName);
            if (!originalCompanyName.equals(processedCompanyName)) {
                System.out.println("Replaced HTML entities in company name: '" + originalCompanyName + "' -> '" + processedCompanyName + "'");
            }
            bean.setCompanyName(processedCompanyName);
            
            bean.setCompanyLogo(job.companyLogo());
            
            // Replace HTML special characters in industry list with improved debugging
            if (job.jobIndustry() != null) {
                List<String> cleanedIndustries = job.jobIndustry().stream()
                    .map(industry -> {
                        String processed = replaceHtmlEntities(industry);
                        if (!industry.equals(processed)) {
                            System.out.println("Replaced HTML entities in industry: '" + industry + "' -> '" + processed + "'");
                        }
                        return processed;
                    })
                    .collect(Collectors.toList());
                bean.setJobIndustry(cleanedIndustries);
            } else {
                bean.setJobIndustry(job.jobIndustry());
            }
            
            // Replace HTML special characters in job type list with improved debugging
            if (job.jobType() != null) {
                List<String> cleanedTypes = job.jobType().stream()
                    .map(type -> {
                        String processed = replaceHtmlEntities(type);
                        if (!type.equals(processed)) {
                            System.out.println("Replaced HTML entities in job type: '" + type + "' -> '" + processed + "'");
                        }
                        return processed;
                    })
                    .collect(Collectors.toList());
                bean.setJobType(cleanedTypes);
            } else {
                bean.setJobType(job.jobType());
            }
            
            // Process and log job geo
            String originalGeo = job.jobGeo();
            String processedGeo = replaceHtmlEntities(originalGeo);
            if (originalGeo != null && !originalGeo.equals(processedGeo)) {
                System.out.println("Replaced HTML entities in job geo: '" + originalGeo + "' -> '" + processedGeo + "'");
            }
            bean.setJobGeo(processedGeo);
            
            // Process and log job level
            String originalLevel = job.jobLevel();
            String processedLevel = replaceHtmlEntities(originalLevel);
            if (originalLevel != null && !originalLevel.equals(processedLevel)) {
                System.out.println("Replaced HTML entities in job level: '" + originalLevel + "' -> '" + processedLevel + "'");
            }
            bean.setJobLevel(processedLevel);
            
            // Process and log job excerpt
            String originalExcerpt = job.jobExcerpt();
            String processedExcerpt = replaceHtmlEntities(originalExcerpt);
            if (originalExcerpt != null && !originalExcerpt.equals(processedExcerpt)) {
                System.out.println("Replaced HTML entities in job excerpt");
            }
            bean.setJobExcerpt(processedExcerpt);
            
            // Process job description - this can be long so don't log the whole thing
            String originalDescription = job.jobDescription();
            String processedDescription = replaceHtmlEntities(originalDescription);
            if (originalDescription != null && !originalDescription.equals(processedDescription)) {
                System.out.println("Replaced HTML entities in job description for job: " + processedTitle);
            }
            bean.setJobDescription(processedDescription);
            
            bean.setPubDate(job.pubDate());
            bean.setAnnualSalaryMin(job.annualSalaryMin());
            bean.setAnnualSalaryMax(job.annualSalaryMax());
            bean.setSalaryCurrency(job.salaryCurrency());
            bean.setRating(job.rating());
            bean.setComments(job.comments());
            
            return bean.toRecord();
        }).collect(Collectors.toList());
        
        System.out.println("Processed " + processedJobs.size() + " job records");
        return processedJobs;
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
    
    /** makes a request to the api and returns the response
     * @param url url to make the request to
     * @return list of job records
     */
    protected List<JobRecord> searchApi(String url) {
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
            return Collections.emptyList();
        }
    }

    public static void main(String[] args) {
        JobBoardApi api = new JobBoardApi();
        List<JobRecord> results = api.getJobBoard("python", 5, "austria", "devops & sysadmin");
        System.out.println("Job Records: " + results.size());
        for (JobRecord job : results) {
            System.out.println("Job Title: " + job.jobTitle());
            System.out.println("Company: " + job.companyName());
            System.out.println("Location: " + job.jobGeo());
            System.out.println("-----------------------------");
        }
    }
}