package skillzhunter.model;

import java.util.List;
import java.util.Map;

/**
 * Interface for the model in the MVC architecture.
 */
public interface IModel {
    /**
     * Sets the alert listener for this model.
     * @param listener The alert listener
     */
    void setAlertListener(AlertListener listener);
    
    /**
     * Gets the industries to be used in the search.
     * @return List of industries
     */
    List<String> getIndustries();

    /**
     * Gets locations to be used in the search.
     * @return List of locations
     */
    List<String> getLocations();

    /**
     * Adds a job to the model.
     * @param job Job record to add
     */
    void addJob(JobRecord job);

    /**
     * Gets a job record by title.
     * @param jobTitle Title of the job to get
     * @return The job record with the specified title
     */
    JobRecord getJobRecord(String jobTitle);

    /**
     * Gets all job records.
     * @return List of all job records
     */
    List<JobRecord> getJobRecords();

    /**
     * Removes a job from the model.
     * @param id ID of the job to remove
     * @return true if the job was removed, false otherwise
     */
    boolean removeJob(int id);

    /**
     * Updates a job in the model.
     * @param id ID of the job to update
     * @param comments New comments for the job
     * @param rating New rating for the job
     */
    void updateJob(int id, String comments, int rating);

    /**
     * Searches for jobs based on the given parameters.
     * @param query The search query
     * @param numberOfResults The maximum number of results to return
     * @param location The location to search for jobs in
     * @param industry The industry to filter jobs by
     * @return List of job records matching the search criteria
     */
    List<JobRecord> searchJobs(String query, Integer numberOfResults, String location, String industry);

    /**
     * Saves the jobs to a CSV file.
     * @param fileName Name of the CSV file to save to
     */
    void saveJobsToCsv(String fileName);

    /**
     * Exports the saved jobs to the specified format and file path.
     * This handles all formats, including CSV (which uses exportCustomCSV internally).
     * 
     * @param jobs List of job records to export
     * @param formatStr Format string (e.g., "CSV", "JSON")
     * @param filePath Path to the output file
     */
    void exportSavedJobs(List<JobRecord> jobs, String formatStr, String filePath);
    
    /**
     * Cleans a job record by processing HTML entities.
     * @param job The job to clean
     * @return A cleaned job record
     */
    JobRecord cleanJob(JobRecord job);

    /**
     * Sends an alert message.
     * This method notifies the registered alert listener.
     * @param alertMessage The alert message to send
     */
    void sendAlert(String alertMessage);

    /**
     * Capitalizes a list of items with special case handling.
     * @param items List of strings to capitalize
     * @param specialCases Map of words with special capitalization rules
     * @return Capitalized list of strings
     */
    List<String> capitalizeItems(List<String> items, Map<String, String> specialCases);

    /**
     * Suggests a correction for a query if it might be a typo.
     * @param query
     * @param resultCount
     * @return
     */
    String suggestQueryCorrection(String query, int resultCount);
}
