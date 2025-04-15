package skillzhunter.controller;

import java.util.List;

import skillzhunter.model.IModel;
import skillzhunter.model.JobRecord;
import skillzhunter.view.IView;
import skillzhunter.view.SavedJobsTab;

public interface IController {
    /**
     * Get a reference to the view.
     * @return the view
     */
    IView getView();

    /**
     * Get a reference to the model.
     * @return the model
     */
    IModel getModel();

    /**
     * Get all locations from the API.
     * @return List of locations
     */
    List<String> getLocations();

    /**
     * Get all industries from the API.
     * @return List of industries
     */
    List<String> getIndustries();

    /**
     * Make an API call to get jobs.
     * @param query The search query
     * @param numberOfResults The number of results to return
     * @param location The location to filter by
     * @param industry The industry to filter by
     * @return List of JobRecord objects
     */
    List<JobRecord> getApiCall(String query, Integer numberOfResults, String location, String industry);

    /**
     * Get the list of saved jobs.
     * @return List of JobRecord objects
     */
    List<JobRecord> getSavedJobs();

    /**
     * Add a job to the saved jobs list.
     * @param jobRecord The JobRecord object to add
     */
    void job2SavedList(JobRecord jobRecord);
    
    /**
     * Used when the another part of
     * the program needs to alert the user.
     * @param alert the alert message to be sent
     **/
    void sendAlert(String alert);

    /**
     * Check if a job is already in the saved jobs list.
     *
     * @param jobRecord The job record to check
     * @return true if the job is already saved, false otherwise
     */
    boolean isJobAlreadySaved(JobRecord jobRecord);

    /**
     * Remove a job from the saved jobs list.
     * @param id The ID of the job to remove
     */
    void removeJobFromList(int id);

    /**
     * Writes the saved jobs to a CSV file.
     * @param filePath The path to the CSV file
     */
    void path2CSV(String filePath);

    /**
     * Exports job records to a file in the specified format.
     * @param jobs The list of JobRecord objects to export
     * @param formatStr The format string (e.g., "CSV", "JSON", "XML")
     * @param filePath The path to save the file to
     */
    void export2FileType(List<JobRecord> jobs, String formatStr, String filePath);

    /**
     * Gets the saved jobs tab.
     * @return The saved jobs tab
     */
    SavedJobsTab getSavedJobsTab();
    
    /**
     * Try to add a job to the saved jobs list, checking for duplicates.
     * 
     * @param jobRecord The JobRecord object to add
     * @return true if the job was added successfully, false if it was already in the list
     */
    boolean tryAddJobToSavedList(JobRecord jobRecord);
    
    /**
     * Updates a job with new comments and rating.
     * 
     * @param id The ID of the job to update
     * @param comments The comments to set
     * @param rating The rating to set
     * @return The updated JobRecord
     */
    JobRecord getUpdateJob(int id, String comments, int rating);
    
    /**
     * Sets the saved jobs.
     * 
     * @param savedJobs List of JobRecord objects to set
     * @return List of JobRecord objects
     */
    List<JobRecord> setSavedJobs(List<JobRecord> savedJobs);

    /**
     * Clean job record and sanitize html for view.
     * @param args
     */
    public JobRecord cleanJobRecord(JobRecord job);
}
