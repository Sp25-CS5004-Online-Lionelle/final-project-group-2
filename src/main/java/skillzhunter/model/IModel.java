package skillzhunter.model;
import java.util.List;

public interface IModel {
    /**
     * Add a new job.
     * @param job JobRecord instance to add
     */
    void addJob(JobRecord job);
    /**
     * Retrieve a single job record by title.
     * @param jobTitle Job title
     * @return JobRecord if found, otherwise null
     */
    JobRecord getJobRecord(String jobTitle);
    /**
     * Retrieve all job records.
     * @return List of JobRecords
     */
    List<JobRecord> getJobRecords();
    /**
     * Remove a job by ID.
     * @param id Job ID to remove
     * @return true if removed, false otherwise
     */
    boolean removeJob(int id);
    
    /**
     * Update a job's comments and rating.
     * @param id Job ID
     * @param comments Comments to update
     * @param rating Rating to update
     */
    void updateJob(int id, String comments, int rating);
     /**
     * Searches for jobs based on the given parameters.
     * @param query The search query (e.g., job title, keywords).
     * @param numberOfResults The maximum number of results to return.
     * @param location The location to search for jobs in.
     * @param industry The industry to filter jobs by.
     * @return A list of JobRecord objects representing the search results.
     */
    List<JobRecord> searchJobs(String query, Integer numberOfResults, String location, String industry);
    
    /**
     * Gets locations to be used in the search in a list.
     * @return location list
     */
    List<String> getLocations();

    /**
     * Gets the industries to be used in the search in a list.
     * @return Map of industries based on keys and is sorted to a list.
     */
    List<String> getIndustries();
    /**
     * Saves the job records (saved jobs) in the jobList to a CSV file.
     * @param fileName Name of the CSV file to save to
     */
    void saveJobsToCsv(String fileName);

    /**
     * Exports the saved jobs to a specified format and file path.
     * @param jobs List of JobRecord objects to export
     * @param formatStr Format string (e.g., "CSV", "JSON")
     * @param filePath Path to the output file
     */
    void exportSavedJobs(List<JobRecord> jobs, String formatStr, String filePath);
}
