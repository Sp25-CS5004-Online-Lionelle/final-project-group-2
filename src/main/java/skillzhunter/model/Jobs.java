package skillzhunter.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import skillzhunter.model.net.JobBoardApi;

public class Jobs implements IModel {

    private final List<JobRecord> jobList;

    public Jobs() {
        this.jobList = new ArrayList<>();
    }

    // CRUD functionality
    /**
     * Add a new job.
     * @param job JobRecord instance to add
     */
    public void addJob(JobRecord job) {
        jobList.add(job);
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

    public List<JobRecord> searchByQuery(String query) {
        List<JobRecord> result = new ArrayList<>();
        for (JobRecord job : jobList) {
            if (job.jobTitle().toLowerCase().contains(query.toLowerCase())) {
                result.add(job);
            }
        }
        return result;
    }

    public List<JobRecord> searchByLocation(String location) {
        List<JobRecord> result = new ArrayList<>();
        for (JobRecord job : jobList) {
            if (job.jobGeo().toLowerCase().contains(location.toLowerCase())) {
                result.add(job);
            }
        }
        return result;
    }

    public List<JobRecord> searchByIndustry(String industry) {
        List<JobRecord> result = new ArrayList<>();
        for (JobRecord job : jobList) {
            if (job.jobIndustry().contains(industry)) {
                result.add(job);
            }
        }
        return result;
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
        return JobBoardApi.getJobBoard(query, numberOfResults, location, industry);
    }

    /**
     * Main method for testing purposes.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Jobs jobs = new Jobs();
        // Test adding, updating, removing jobs
        JobRecord job = new JobRecord(1, "url", "slug", "Python Developer", "Company A", "logo", Arrays.asList("Tech"), Arrays.asList("Full-time"), "NYC", "Senior", "Job excerpt", "Job description", "2025-03-30", 60000, 80000, "USD", 4, "Great job");
        System.out.println("Adding job: " + job);
        jobs.addJob(job);

        // Search job by title
        JobRecord searchedJob = jobs.getJobRecord("Python Developer");
        System.out.println("Searched job: " + searchedJob);
        System.out.println(searchedJob);

        // Search job by query
        List<JobRecord> searchResults = jobs.searchByQuery("Python");
        System.out.println("Search results for 'Python': " + searchResults);
        for (JobRecord j : searchResults) {
            System.out.println(j);
        }
        // Search job by location
        List<JobRecord> locationResults = jobs.searchByLocation("NYC");
        System.out.println("Search results for 'NYC': " + locationResults);
        for (JobRecord j : locationResults) {
            System.out.println(j);
        }
        // Search job by industry
        List<JobRecord> industryResults = jobs.searchByIndustry("Tech");
        System.out.println("Search results for 'Tech': " + industryResults);
        for (JobRecord j : industryResults) {
            System.out.println(j);
        }
        // Remove job
        System.out.println("Removing job with ID 1");
        jobs.removeJob(1);
    }
}