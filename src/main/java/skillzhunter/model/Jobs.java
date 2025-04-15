package skillzhunter.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import skillzhunter.controller.IController;
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
    
    /** Used for identifying the connected controller. */
    private IController controller;

    /** Job List. */
    private final List<JobRecord> jobList;

    /** Used to track number of times the joblist is accessed.*/
    private int runs = 0;

    /** Object representing the Jobs Api.*/
    private final JobBoardApi api;

    /**
     * Constructor for Jobs class.
     * Initializes the job list and API.
     */
    public Jobs() {
        this.jobList = new ArrayList<>();
        this.api = createJobBoardApi();

        try {
            // Load jobs from CSV when the application starts
            // We wrap this in try-catch to avoid issues during testing
            loadJobsFromCsv("SavedJobs.csv");

            // Add a shutdown hook to save the jobs to CSV on shutdown
            // Only add this in non-test environment
            if (!isRunningInTestEnvironment()) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    saveJobsToCsv("SavedJobs.csv");
                    System.out.println("Jobs saved to SavedJobs.csv on shutdown.");
                }));
            }
        } catch (Exception e) {
            System.err.println("Note: Could not load jobs from CSV. This is normal during testing.");
            // No need to rethrow - this allows tests to run without the CSV file
        }
    }

    /**
     * Helper method to detect if we're running in a test environment.
     * This helps prevent the shutdown hook from interfering with tests.
     */
    private boolean isRunningInTestEnvironment() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().contains("org.junit")) {
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
     * Gets the industries to be used in the search in a list.
     * @return Map of industries based on keys and is sorted to a list.
     */
    @Override
    public List<String> getIndustries() {
        return INDUSTRY_MAP.keySet().stream().sorted().toList();
    }

    /**
     * Gets locations to be used in the search in a list.
     * @return location list
     */
    @Override
    public List<String> getLocations() {
        return LOCATION_MAP.keySet().stream().sorted().toList();
    }

    // CRUD functionality

    /**
     * Used to identify the controller attached to the model.
     * @param controller the attached controller
     */
    @Override
    public void setController(IController controller) {
      this.controller = controller;
    }

    /**
     * Add a new job.
     * @param job JobRecord instance to add
     */
    @Override
    public void addJob(JobRecord job) {
        JobBean jobBean = new JobBean();
        // Set fields in the same order as JobRecord
        jobBean.setId(job.id());
        jobBean.setUrl(job.url());
        jobBean.setJobSlug(job.jobSlug());
        jobBean.setJobTitle(job.jobTitle() != null && !job.jobTitle().isBlank() ? job.jobTitle() : "");
        jobBean.setCompanyName(job.companyName() != null && !job.companyName().isBlank() ? job.companyName() : "");
        jobBean.setCompanyLogo(job.companyLogo());

        jobBean.setJobIndustry(job.jobIndustry() != null
                            && job.jobIndustry().stream().noneMatch(String::isBlank)
                            ? job.jobIndustry() : new ArrayList<>());
        jobBean.setJobType(job.jobType() != null
                        && job.jobType().stream().noneMatch(String::isBlank)
                        ? job.jobType() : new ArrayList<>());

        jobBean.setJobGeo(job.jobGeo()
                        != null && !job.jobGeo().isBlank() ? job.jobGeo() : "");
        jobBean.setJobLevel(job.jobLevel() != null && !job.jobLevel().isBlank() ? job.jobLevel() : "");
        jobBean.setJobExcerpt(job.jobExcerpt());
        jobBean.setJobDescription(job.jobDescription());
        jobBean.setPubDate(job.pubDate() != null && !job.pubDate().isBlank() ? job.pubDate() : "");
        jobBean.setAnnualSalaryMin(job.annualSalaryMin());
        jobBean.setAnnualSalaryMax(job.annualSalaryMax());
        jobBean.setSalaryCurrency(job.salaryCurrency() != null
                                && !job.salaryCurrency().isBlank()
                                ? job.salaryCurrency() : "");
        jobBean.setRating(job.rating());
        jobBean.setComments(job.comments() != null
                            && !job.comments().isBlank()
                            ? job.comments() : "No comments provided");
        
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
        JobBoardApiResult result = api.getJobBoard(query, numberOfResults, location, industry);

        // If there's an error message, send alert
        if (result.hasError()) {
            sendAlert(result.getErrorMessage());
        }

        // Process HTML entities in each job record before returning
        List<JobRecord> processedJobs = new ArrayList<>();
        for (JobRecord job : result.getJobs()) {
            processedJobs.add(DataFormatter.processJobHtml(job));
        }

        return processedJobs;
    }

    /**
     * Saves the job records (saved jobs) in the jobList to a CSV file.
     * @param fileName Name of the CSV file to save to
     */
    @Override
    public void saveJobsToCsv(String fileName) {
        // Debug information before export
        System.out.println("Saving " + jobList.size() + " jobs to CSV: " + fileName);
        for (JobRecord job : jobList) {
            System.out.println("Job before export: " + job.jobTitle()
                            + ", Rating: " + job.rating()
                            + ", Comments: " + job.comments());
        }
        
        try (OutputStream out = new FileOutputStream(fileName)) {
            // Write to file - DataFormatter now handles escaping correctly
            DataFormatter.write(jobList, Formats.CSV, out);
            System.out.println("CSV export completed successfully");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            // e.printStackTrace();
            throw new RuntimeException("Error writing to CSV file", e);
        }
    }

    /**
     * Exports the saved jobs to a specified format and file path.
     * @param jobs List of JobRecord objects to export
     * @param formatStr Format string (e.g., "CSV", "JSON")
     * @param filePath Path to the output file
     */
    @Override
    public void exportSavedJobs(List<JobRecord> jobs, String formatStr, String filePath) {
        // Check if the format is valid
        Formats format = Formats.containsValues(formatStr);
        if (format == null) {
            throw new IllegalArgumentException("Unsupported format: " + formatStr);
        }

        // Debug information before export
        System.out.println("Exporting " + jobs.size() + " jobs to " + formatStr + ": " + filePath);
        
        // Export the jobs to the specified file
        try (OutputStream out = new FileOutputStream(filePath)) {
            // DataFormatter now handles escaping correctly for each format
            DataFormatter.write(jobs, format, out);
            System.out.println("Export completed successfully");
        } catch (IOException e) {
            System.err.println("Failed to export jobs: " + e.getMessage());
            // e.printStackTrace();
            throw new RuntimeException("Failed to export jobs: " + e.getMessage(), e);
        }
    }

    /**
     * Used to send alerts to other parts of the program.
     * @param alert
     */
    @Override
    public void sendAlert(String alert) {
        controller.sendAlert(alert);
    }

    /**
     * Loads job records from a CSV file and adds them to the job list.
     * @param fileName Name of the CSV file to load from
     */
    private void loadJobsFromCsv(String fileName) {
    try (InputStream in = new FileInputStream(fileName)) {
        List<JobRecord> loadedJobs = DataFormatter.read(in, Formats.CSV);
        this.jobList.clear();  // Clear existing list before loading
        this.jobList.addAll(loadedJobs);  // Add loaded jobs
        System.out.println("Loaded " + loadedJobs.size() + " jobs from CSV.");
    } catch (IOException e) {
        System.err.println("Error loading jobs from CSV file: " + e.getMessage());
    }
}

    /**
     * Main method for testing purposes.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Jobs jobs = new Jobs();
        // // Test adding, updating, removing jobs
        // JobRecord job = new JobRecord(1, "https://example.com/job", "slug",
        // "Python Developer & Data Scientist", "Company A", "logo",
        // Arrays.asList("Tech"), Arrays.asList("Full-time"), "NYC", "Senior",
        // "Job excerpt", "Job description", "2025-03-30", 60000,
        // 80000, "USD", 4, "Great job");
        // System.out.println("Adding job" + job);
        // jobs.addJob(job);

        // // Search job by title
        // JobRecord searchedJob = jobs.getJobRecord("Python Developer");
        // System.out.println("Searched job: " + searchedJob);
        // System.out.println(searchedJob);

        // // Export test
        // jobs.exportSavedJobs(jobs.getJobRecords(), "CSV", "test_jobs.csv");
        // jobs.exportSavedJobs(jobs.getJobRecords(), "JSON", "test_jobs.json");
        // jobs.exportSavedJobs(jobs.getJobRecords(), "XML", "test_jobs.xml");

        // // Remove job
        // System.out.println("Removing job with ID 1");
        // jobs.removeJob(1);

        // jobs.getIndustries().forEach(System.out::println);
        // jobs.getLocations().forEach(System.out::println);
    }
}

