package skillzhunter.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import skillzhunter.model.formatters.DataFormatter;
import skillzhunter.model.formatters.Formats;
import skillzhunter.model.net.JobBoardApi;

public class Jobs implements IModel {
    /** map for storing industries and their slugs*/
    private static final Map<String, String> industriesMap = JobBoardApi.loadCsvData(
        Paths.get("data", "industries.csv").toString(), "industry", "slug");
    /** map for storing locations and their slugs*/
    private static final Map<String, String> locationsMap = JobBoardApi.loadCsvData(
        Paths.get("data", "locations.csv").toString(), "location", "slug");

    /** Job List */
    private final List<JobRecord> jobList;

    /** used to track number of times the joblist is accessed*/
    private int runs = 0;

    /** Jobs Api */
    private final JobBoardApi api = new JobBoardApi();

    public Jobs() {
        this.jobList = new ArrayList<>();
    }

    /**
     * Gets the industries to be used in the search in a list.
     * @return Map of industries based on keys and is sorted to a list.
     */
    public List<String> getIndustries() {
        return industriesMap.keySet().stream().sorted().toList();
    }

    /**
     * Gets locations to be used in the search in a list.
     */
    public List<String> getLocations() {
        return locationsMap.keySet().stream().sorted().toList();
    }

    // CRUD functionality
    /**
     * Add a new job.
     * @param job JobRecord instance to add
     */
    public void addJob(JobRecord job) {
        JobBean jobBean = new JobBean();
        // Set fields in the same order as JobRecord
        jobBean.setId(job.id());
        jobBean.setUrl(job.url());
        jobBean.setJobSlug(job.jobSlug());
        jobBean.setJobTitle(job.jobTitle() != null && !job.jobTitle().isBlank() ? job.jobTitle() : "");
        jobBean.setCompanyName(job.companyName() != null && !job.companyName().isBlank() ? job.companyName() : "");
        jobBean.setCompanyLogo(job.companyLogo());
        jobBean.setJobIndustry(job.jobIndustry() != null && job.jobIndustry().stream().noneMatch(String::isBlank) ? job.jobIndustry() : new ArrayList<>());
        jobBean.setJobType(job.jobType() != null && job.jobType().stream().noneMatch(String::isBlank) ? job.jobType() : new ArrayList<>());
        jobBean.setJobGeo(job.jobGeo() != null && !job.jobGeo().isBlank() ? job.jobGeo() : "");
        jobBean.setJobLevel(job.jobLevel() != null && !job.jobLevel().isBlank() ? job.jobLevel() : "");
        jobBean.setJobExcerpt(job.jobExcerpt());
        jobBean.setJobDescription(job.jobDescription());
        jobBean.setPubDate(job.pubDate() != null && !job.pubDate().isBlank() ? job.pubDate() : "");
        jobBean.setAnnualSalaryMin(job.annualSalaryMin());
        jobBean.setAnnualSalaryMax(job.annualSalaryMax());
        jobBean.setSalaryCurrency(job.salaryCurrency() != null && !job.salaryCurrency().isBlank() ? job.salaryCurrency() : "");
        jobBean.setRating(job.rating());
        jobBean.setComments(job.comments() != null && !job.comments().isBlank() ? job.comments() : "No comments provided");
        
        jobList.add(jobBean.toRecord());
    }

    /**
     * Retrieve a single job record by title.
     * @param jobTitle Job title
     * @return JobRecord if found, otherwise null
     */
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
        List<JobRecord> results = api.getJobBoard(query, numberOfResults, location, industry);
        return results;
    }

    /**
     * Creates a version of the job list with empty URLs and HTML content for export.
     * 
     * @param jobs The original job records
     * @return A new list with URLs and HTML content removed
     */
    private List<JobRecord> createExportableJobs(List<JobRecord> jobs) {
        return jobs.stream()
            .map(job -> new JobRecord(
                job.id(),
                "", // Empty URL
                job.jobSlug(),
                job.jobTitle(),
                job.companyName(),
                "", // Empty company logo URL
                job.jobIndustry(),
                job.jobType(),
                job.jobGeo(),
                job.jobLevel(),
                "", // Empty jobExcerpt
                "", // Empty jobDescription
                job.pubDate(),
                job.annualSalaryMin(),
                job.annualSalaryMax(),
                job.salaryCurrency(),
                job.rating(),
                job.comments()
            ))
            .collect(Collectors.toList());
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
            System.out.println("Job before export: " + job.jobTitle() + 
                               ", Rating: " + job.rating() + 
                               ", Comments: " + job.comments());
        }
        
        try (OutputStream out = new FileOutputStream(fileName)) {
            // Create exportable jobs that have cleaned data and empty URLs
            List<JobRecord> exportableJobs = createExportableJobs(jobList);
            
            // Debug information after transformation
            System.out.println("Transformed jobs for export:");
            for (JobRecord job : exportableJobs) {
                System.out.println("Transformed job: " + job.jobTitle() + 
                                   ", Rating: " + job.rating() + 
                                   ", Comments: " + job.comments());
            }
            
            // Write to file
            DataFormatter.write(exportableJobs, Formats.CSV, out);
            System.out.println("CSV export completed successfully");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
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
        
        // Export the jobs to the specified file with URLs cleared
        try (OutputStream out = new FileOutputStream(filePath)) {
            // Create exportable jobs
            List<JobRecord> exportableJobs = createExportableJobs(jobs);
            
            // Write to file
            DataFormatter.write(exportableJobs, format, out);
            System.out.println("Export completed successfully");
        } catch (IOException e) {
            System.err.println("Failed to export jobs: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to export jobs: " + e.getMessage(), e);
        }
    }

    /**
     * Main method for testing purposes.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Jobs jobs = new Jobs();
        // Test adding, updating, removing jobs
        JobRecord job = new JobRecord(1, "url", "slug", "Python Developer", "Company A", "logo", Arrays.asList("Tech"), Arrays.asList("Full-time"), "NYC", "Senior", "Job excerpt", "Job description", "2025-03-30", 60000, 80000, "USD", 4, "Great job");
        System.out.println("Adding job" + job);
        jobs.addJob(job);

        // Search job by title
        JobRecord searchedJob = jobs.getJobRecord("Python Developer");
        System.out.println("Searched job: " + searchedJob);
        System.out.println(searchedJob);

        // Remove job
        System.out.println("Removing job with ID 1");
        jobs.removeJob(1);

        jobs.getIndustries().forEach(System.out::println);
        jobs.getLocations().forEach(System.out::println);
    }
}