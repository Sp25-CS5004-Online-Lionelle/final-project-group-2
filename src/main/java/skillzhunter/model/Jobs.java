package skillzhunter.model;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        jobBean.setId(job.id());
        jobBean.setJobTitle(job.jobTitle() != null && !job.jobTitle().isBlank() ? job.jobTitle() : "");
        jobBean.setCompanyName(job.companyName() != null && !job.companyName().isBlank() ? job.companyName() : "");
        jobBean.setJobIndustry(job.jobIndustry() != null && job.jobIndustry().stream().noneMatch(String::isBlank) ? job.jobIndustry() : new ArrayList<>());
        jobBean.setJobType(job.jobType() != null && job.jobType().stream().noneMatch(String::isBlank) ? job.jobType() : new ArrayList<>());
        jobBean.setJobGeo(job.jobGeo() != null && !job.jobGeo().isBlank() ? job.jobGeo() : "");
        jobBean.setJobLevel(job.jobLevel() != null && !job.jobLevel().isBlank() ? job.jobLevel() : "");
        jobBean.setAnnualSalaryMin(job.annualSalaryMin());
        jobBean.setAnnualSalaryMax(job.annualSalaryMax());
        jobBean.setSalaryCurrency(job.salaryCurrency() != null && !job.salaryCurrency().isBlank() ? job.salaryCurrency() : "");
        jobBean.setPubDate(job.pubDate() != null && !job.pubDate().isBlank() ? job.pubDate() : "");
        jobBean.setComments(job.comments() != null && !job.comments().isBlank() ? job.comments() : "No comments provided");
        jobBean.setRating(job.rating());
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
        if (jobList.isEmpty()) {
            System.out.println("Job List is empty. Ensure jobs are added before retrieving.");
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

    //update comments and rating
    /**
     * Update a job's comments and rating.
     * @param id Job ID
     * @param comments Comments to update
     * @param rating Rating to update
     */
    @Override
    public void updateJob(int id, String comments, int rating) {
        for (JobRecord job : jobList) {
            if (job.id() == id) {
                //make job into bean
                JobBean jobBean = new JobBean();
                jobBean.setId(job.id());
                jobBean.setJobTitle(job.jobTitle());
                jobBean.setCompanyName(job.companyName());
                jobBean.setJobIndustry(job.jobIndustry());
                jobBean.setJobType(job.jobType());
                jobBean.setJobGeo(job.jobGeo());
                jobBean.setJobLevel(job.jobLevel());
                jobBean.setAnnualSalaryMin(job.annualSalaryMin());
                jobBean.setAnnualSalaryMax(job.annualSalaryMax());
                jobBean.setSalaryCurrency(job.salaryCurrency());
                jobBean.setPubDate(job.pubDate());

                //setcomments and rating in bean
                jobBean.setComments(comments);
                jobBean.setRating(rating);
                //then make into jobrecord
                JobRecord updatedJob = jobBean.toRecord();
                //remove old job
                jobList.remove(job);
                //add updated job
                jobList.add(updatedJob);
                //update job in list
                for (int i = 0; i < jobList.size(); i++) {
                    if (jobList.get(i).id() == id) {
                        jobList.set(i, updatedJob);
                        break;
                    }
                }
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

    //add getSavedJobs and downloadJobs methods here
    /**
     * Saves the job records to a CSV file.
     * @param jobRecords List of JobRecord objects to save
     * @param fileName Name of the CSV file
     */
    @Override
    /**
     * Saves the job records (saved jobs) in the jobList to a CSV file.
     * @param fileName Name of the CSV file to save to
     */
    public void saveJobsToCsv(String fileName) {
        try (OutputStream out = new FileOutputStream(fileName)) {
            // Use the jobList to get the saved jobs
            DataFormatter.write(jobList, Formats.CSV, out);
        } catch (IOException e) {
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

        // Export the jobs to the specified file
        try (OutputStream out = new FileOutputStream(filePath)) {
            DataFormatter.write(jobs, format, out);
        } catch (IOException e) {
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