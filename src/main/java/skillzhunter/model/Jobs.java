package skillzhunter.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skillzhunter.model.formatters.DataFormatter;
import skillzhunter.model.formatters.Formats;
import skillzhunter.model.net.JobBoardApi;
import skillzhunter.model.net.JobBoardApiResult;

public class Jobs implements IModel {
    /** Map for storing industries and their slugs. */
    private static final Map<String, String> INDUSTRY_MAP = JobBoardApi.loadCsvData(
        Paths.get("data", "industries.csv").toString(), "industry", "slug");

    /** Map for storing locations and their slugs. */
    private static final Map<String, String> LOCATION_MAP = JobBoardApi.loadCsvData(
        Paths.get("data", "locations.csv").toString(), "location", "slug");
    
    /** Alert listener for this model. */
    private AlertListener alertListener;

    /** Job List. */
    private final List<JobRecord> jobList;

    /** Used to track number of times the joblist is accessed.*/
    private int runs = 0;

    /** Object representing the Jobs Api.*/
    private final JobBoardApi api;

    /** Standard path for saved jobs file. */
    private static final String DEFAULT_SAVED_JOBS_PATH = "data/SavedJobs.csv";

    /** Flag to indicate if running in test mode. */
    private boolean isTestMode = false;
    
    /** Flag to indicate if this is an initial search on startup. */
    private boolean isInitialSearch = true;

    /** Flag to indicate if suggestion can be provided. */
    private final QuerySuggestionService suggestionService = new QuerySuggestionService();

    /**
     * Constructor for Jobs class.
     * Initializes the job list and API.
     */
    public Jobs() {
        this.jobList = new ArrayList<>();
        this.api = createJobBoardApi();

        // Check if running in test environment
        isTestMode = isRunningInTestEnvironment();

        try {
            // Only load from CSV in non-test environment or if file exists
            File savedJobsFile = new File(DEFAULT_SAVED_JOBS_PATH);
            if (!isTestMode && savedJobsFile.exists()) {
                loadJobsFromCsv(DEFAULT_SAVED_JOBS_PATH);
                
                // Add a shutdown hook to save the jobs to CSV on shutdown
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    System.out.println("Application is shutting down. Saving jobs to " + DEFAULT_SAVED_JOBS_PATH);
                    saveJobsToCsv(DEFAULT_SAVED_JOBS_PATH);
                    System.out.println("Jobs saved to " + DEFAULT_SAVED_JOBS_PATH + " on shutdown.");
                }));
            }
        } catch (Exception e) {
            System.err.println("Note: Could not load jobs from CSV: " + e.getMessage());
            // No need to rethrow - we can start with an empty list
        }
    }
    
    /**
     * Sets the alert listener for this model.
     * @param listener The alert listener
     */
    @Override
    public void setAlertListener(AlertListener listener) {
        this.alertListener = listener;
    }

    /**
     * Helper method to detect if we're running in a test environment.
     * This helps prevent the shutdown hook from interfering with tests.
     */
    private boolean isRunningInTestEnvironment() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            String className = element.getClassName();
            if (className.contains("org.junit") || 
                className.contains("Test") ||
                className.contains("test")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a JobBoardApi instance.
     * This method exists to allow test classes to override it and provide a mock API.
     * 
     * @return A JobBoardApi instance
     */
    protected JobBoardApi createJobBoardApi() {
        return new JobBoardApi();
    }

    /**
     * Gets the industries to be used in the search in a list, with proper capitalization.
     * This method uses HR as a special case.
     * @return List of industries properly capitalized and sorted.
     */
    @Override
    public List<String> getIndustries() {
        Map<String, String> specialCases = new HashMap<>();
        specialCases.put("hr", "HR");
        
        List<String> industries = INDUSTRY_MAP.keySet().stream().sorted().toList();
        return capitalizeItems(industries, specialCases);
    }

    /**
     * Gets locations to be used in the search in a list, with proper capitalization.
     * This method uses USA as a special case.
     * @return List of locations properly capitalized and sorted.
     */
    @Override
    public List<String> getLocations() {
        Map<String, String> specialCases = new HashMap<>();
        specialCases.put("usa", "USA");
        List<String> locations = LOCATION_MAP.keySet().stream().sorted().toList();
        return capitalizeItems(locations, specialCases);
    }
    /**
     * Add a new job.
     * @param job JobRecord instance to add
     */
    @Override
    public void addJob(JobRecord job) {
        // Sanitize HTML before saving
        JobRecord cleanJob = DataFormatter.processJobHtml(job);

        JobBean jobBean = new JobBean();
        // Set fields in the same order as JobRecord
        jobBean.setId(cleanJob.id());
        jobBean.setUrl(cleanJob.url());
        jobBean.setJobSlug(cleanJob.jobSlug());
        jobBean.setJobTitle(cleanJob.jobTitle() != null && !cleanJob.jobTitle().isBlank() ? cleanJob.jobTitle() : "");
        jobBean.setCompanyName(cleanJob.companyName() != null && !cleanJob.companyName().isBlank() ? cleanJob.companyName() : "");
        jobBean.setCompanyLogo(cleanJob.companyLogo());

        jobBean.setJobIndustry(cleanJob.jobIndustry() != null
                            && cleanJob.jobIndustry().stream().noneMatch(String::isBlank)
                            ? cleanJob.jobIndustry() : new ArrayList<>());
        jobBean.setJobType(cleanJob.jobType() != null
                        && cleanJob.jobType().stream().noneMatch(String::isBlank)
                        ? cleanJob.jobType() : new ArrayList<>());

        jobBean.setJobGeo(cleanJob.jobGeo() != null && !cleanJob.jobGeo().isBlank() ? cleanJob.jobGeo() : "");
        jobBean.setJobLevel(cleanJob.jobLevel() != null && !cleanJob.jobLevel().isBlank() ? cleanJob.jobLevel() : "");
        jobBean.setJobExcerpt(cleanJob.jobExcerpt());
        jobBean.setJobDescription(cleanJob.jobDescription());
        jobBean.setPubDate(cleanJob.pubDate() != null && !cleanJob.pubDate().isBlank() ? cleanJob.pubDate() : "");
        jobBean.setAnnualSalaryMin(cleanJob.annualSalaryMin());
        jobBean.setAnnualSalaryMax(cleanJob.annualSalaryMax());
        jobBean.setSalaryCurrency(cleanJob.salaryCurrency() != null
                                && !cleanJob.salaryCurrency().isBlank()
                                ? cleanJob.salaryCurrency() : "");
        jobBean.setRating(cleanJob.rating());
        jobBean.setComments(cleanJob.comments() != null && !cleanJob.comments().isBlank()
                            ? cleanJob.comments() : "No comments provided");

        jobList.add(jobBean.toRecord());
    }


    /**
     * Retrieve a single job record by title.
     * @param jobTitle Job title
     * @return JobRecord if found, otherwise null
     */
    @Override
    public JobRecord getJobRecord(String jobTitle) {
        for (JobRecord job : jobList) {
            if (job.jobTitle().equals(jobTitle)) {
                return job;
            }
        }
        return null;
    }

    /**
     * Retrieve all job records.
     * @return List of JobRecords
     */
    @Override
    public List<JobRecord> getJobRecords() {
        if (jobList.isEmpty() && runs > 0) {
            System.out.println("Job List is empty. Ensure jobs are added before retrieving.");
            runs++;
        }

        return new ArrayList<>(jobList);
    }

    /**
     * Remove a job by ID.
     * @param id Job ID to remove
     * @return true if removed, false otherwise
     */
    @Override
    public boolean removeJob(int id) {
        return jobList.removeIf(job -> job.id() == id);
    }

    /**
     * Update a job's comments and rating.
     * @param id Job ID
     * @param comments Comments to update
     * @param rating Rating to update
     */
    @Override
    public void updateJob(int id, String comments, int rating) {
        // Debug output to verify values
        System.out.println("Updating job " + id + " with rating: " + rating + " and comments: " + comments);
        
        for (JobRecord job : jobList) {
            if (job.id() == id) {
                // Create JobBean and set all fields in the same order as JobRecord
                JobBean jobBean = new JobBean();
                jobBean.setId(job.id());
                jobBean.setUrl(job.url());
                jobBean.setJobSlug(job.jobSlug());
                jobBean.setJobTitle(job.jobTitle());
                jobBean.setCompanyName(job.companyName());
                jobBean.setCompanyLogo(job.companyLogo());
                jobBean.setJobIndustry(job.jobIndustry());
                jobBean.setJobType(job.jobType());
                jobBean.setJobGeo(job.jobGeo());
                jobBean.setJobLevel(job.jobLevel());
                jobBean.setJobExcerpt(job.jobExcerpt());
                jobBean.setJobDescription(job.jobDescription());
                jobBean.setPubDate(job.pubDate());
                jobBean.setAnnualSalaryMin(job.annualSalaryMin());
                jobBean.setAnnualSalaryMax(job.annualSalaryMax());
                jobBean.setSalaryCurrency(job.salaryCurrency());
                
                // Update comments and rating
                jobBean.setRating(rating);
                jobBean.setComments(comments);
                
                // Debug output to verify bean values before conversion
                System.out.println("Job bean rating set to: " + jobBean.getRating());
                System.out.println("Job bean comments set to: " + jobBean.getComments());
                
                // Convert back to JobRecord
                JobRecord updatedJob = jobBean.toRecord();
                
                // Debug output to verify record values after conversion
                System.out.println("Updated job record rating: " + updatedJob.rating());
                System.out.println("Updated job record comments: " + updatedJob.comments());
                
                // Update the job in the list
                jobList.remove(job);
                jobList.add(updatedJob);
                break;
            }
        }
    }
    
    /**
     * Searches for jobs based on the given parameters.
     * @param query The search query (e.g., job title, keywords).
     * @param numberOfResults The maximum number of results to return.
     * @param location The location to search for jobs in.
     * @param industry The industry to filter jobs by.
     * @return A list of JobRecord objects representing the search results.
     */
    @Override
    public List<JobRecord> searchJobs(String query, Integer numberOfResults, String location, String industry) {
        // Check if this is a generic default search
        boolean isGenericSearch = "any".equals(query) && 
                                 ("any".equals(location) || location == null) && 
                                 ("any".equals(industry) || industry == null);
        
        JobBoardApiResult result = api.getJobBoard(query, numberOfResults, location, industry);

        // If there's an error message and this is not an initial generic search, send alert
        if (result.hasError() && !(isInitialSearch && isGenericSearch)) {
            sendAlert(result.getErrorMessage());
        }
        
        // After first search, set initial search flag to false
        isInitialSearch = false;

        // Process HTML entities in each job record before returning
        List<JobRecord> processedJobs = new ArrayList<>();
        for (JobRecord job : result.getJobs()) {
            processedJobs.add(DataFormatter.processJobHtml(job));
        }

        // Add query to successful queries if results found
        if (!processedJobs.isEmpty() && query != null && !query.isEmpty() && 
            !query.equalsIgnoreCase("any") && !query.equalsIgnoreCase("all")) {
            suggestionService.addSuccessfulQuery(query);
        }
    
        return processedJobs;
    }

    /**
     * Saves the job records (saved jobs) in the jobList to a CSV file, ensuring data is sanitized before saving.
     * @param filePath Name of the CSV file to save to
     */
    @Override
    public void saveJobsToCsv(String filePath) {
        // Debug information before export
        System.out.println("Saving " + jobList.size() + " jobs to CSV: " + filePath);
        
        // Ensure the parent directory exists
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            System.out.println("Created directory structure: " + created);
        }
        
        // Log jobs before sanitization for debugging
        for (JobRecord job : jobList) {
            // Print industry data to diagnose HTML entity issues
            String industryStr = "null";
            if (job.jobIndustry() != null && !job.jobIndustry().isEmpty()) {
                industryStr = String.join(", ", job.jobIndustry());
            }
            
            System.out.println("Job before export: " + job.jobTitle() 
                            + ", Industry: " + industryStr
                            + ", Rating: " + job.rating()
                            + ", Comments: " + (job.comments() != null ? job.comments() : "null"));
        }

        // Let DataFormatter handle the export with proper sanitization
        // This avoids duplicate sanitizing logic and ensures consistent cleaning
        DataFormatter.exportCustomCSV(new ArrayList<>(jobList), filePath);
        
        System.out.println("Jobs saved to CSV: " + filePath);
    }

    /**
     * Exports the saved jobs to a specified format and file path.
     * Handles all formats, including CSV (which uses exportCustomCSV internally).
     * 
     * @param jobs List of JobRecord objects to export
     * @param formatStr Format string (e.g., "CSV", "JSON")
     * @param filePath Path to the output file
     */
    @Override
    public void exportSavedJobs(List<JobRecord> jobs, String formatStr, String filePath) {
        if (jobs == null || formatStr == null || filePath == null) {
            throw new IllegalArgumentException("Jobs, format, and file path must not be null");
        }
        
        // Ensure the parent directory exists
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            System.out.println("Created directory structure: " + created);
        }
        
        System.out.println("Exporting " + jobs.size() + " jobs to " + formatStr + ": " + filePath);
        
        // Handle CSV format internally using the custom CSV exporter
        if ("CSV".equalsIgnoreCase(formatStr)) {
            DataFormatter.exportCustomCSV(jobs, filePath);
            return;
        }
        
        // For other formats, use the standard export mechanism
        try {
            // Check if the format is valid
            Formats format = Formats.containsValues(formatStr);
            if (format == null) {
                throw new IllegalArgumentException("Unsupported format: " + formatStr);
            }
            
            // Export the jobs to the specified file
            try (OutputStream out = new FileOutputStream(filePath)) {
                // DataFormatter now handles escaping correctly for each format
                DataFormatter.write(jobs, format, out);
                System.out.println("Export completed successfully to " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to export jobs: " + e.getMessage());
            throw new RuntimeException("Failed to export jobs: " + e.getMessage(), e);
        }
    }
    
    /**
     * Cleans a job record by processing HTML entities.
     * 
     * @param job The job to clean
     * @return A cleaned job record
     */
    @Override
    public JobRecord cleanJob(JobRecord job) {
        return DataFormatter.processJobHtml(job);
    }

    /**
     * Sends an alert message to the registered alert listener.
     * 
     * @param alertMessage The alert message to send
     */
    @Override
    public void sendAlert(String alertMessage) {
        System.out.println("Model alert: " + alertMessage);
        
        // If an alert listener is registered, notify it
        if (alertListener != null) {
            alertListener.onAlert(alertMessage);
        } else {
            // Fallback if no listener is registered
            System.err.println("No alert listener registered with model: " + alertMessage);
        }
    }

    /**
     * Loads job records from a CSV file and adds them to the job list.
     * @param fileName Name of the CSV file to load from
     */
    private void loadJobsFromCsv(String fileName) {
        File csvFile = new File(fileName);
        if (!csvFile.exists()) {
            System.err.println("CSV file does not exist: " + fileName);
            return;
        }
        
        try (InputStream in = new FileInputStream(fileName)) {
            List<JobRecord> loadedJobs = DataFormatter.read(in, Formats.CSV);
            this.jobList.clear();  // Clear existing list before loading
            this.jobList.addAll(loadedJobs);  // Add loaded jobs
            System.out.println("Loaded " + loadedJobs.size() + " jobs from " + fileName);
        } catch (IOException e) {
            System.err.println("Error loading jobs from CSV file: " + e.getMessage());
        }
    }

    @Override
    /**
     * Capitalizes the first letter of each word in a list of items.
     * @param items List of items to capitalize
     * @param specialCases Map of special cases where the key is the lowercase word and the value is the capitalized version
     * @return List of capitalized items
     */
    public List<String> capitalizeItems(List<String> items, Map<String, String> specialCases) {
        return items.stream()
                .map(item -> {
                    String[] words = item.split(" ");
                    StringBuilder result = new StringBuilder();
                    
                    for (String word : words) {
                        if (!word.isEmpty()) {
                            String lowerWord = word.toLowerCase();
                            String capitalizedWord = specialCases.getOrDefault(lowerWord, capitalizeWord(word));
                            result.append(capitalizedWord).append(" ");
                        }
                    }
                    
                    return result.toString().trim();
                })
                .toList();
    }

    /**
     * Helper method to capitalize a single word.
     * @param word Word to capitalize
     * @return Capitalized word
     */
    private String capitalizeWord(String word) {
        if (word.isEmpty()) {
            return word;
        }
        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }

    /**
     * Suggests a correction for a query if it might be a typo.
     * @param query
     * @param resultCount
     * @return
     */
    @Override
    public String suggestQueryCorrection(String query, int resultCount) {
        return suggestionService.suggestCorrection(query, resultCount);
    }
}
