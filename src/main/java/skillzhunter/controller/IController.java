package skillzhunter.controller;

import java.util.List;

import skillzhunter.model.IModel;
import skillzhunter.model.JobRecord;
import skillzhunter.view.IView;

/**
 * Interface for the controller in the MVC architecture.
 */
public interface IController {
    /**
     * Sets the model for this controller.
     * @param model The model to set
     */
    void setModel(IModel model);
    
    /**
     * Sets the view for this controller.
     * @param view The view to set
     */
    void setView(IView view);
    
    /**
     * Registers an alert observer with this controller.
     * @param observer The alert observer to register
     */
    void registerAlertObserver(AlertObserver observer);
    
    /**
     * Unregisters an alert observer from this controller.
     * @param observer The alert observer to unregister
     */
    void unregisterAlertObserver(AlertObserver observer);
    
    /**
     * Gets the locations from the model.
     * @return List of locations
     */
    List<String> getLocations();
    
    /**
     * Gets the industries from the model.
     * @return List of industries
     */
    List<String> getIndustries();
    
    /**
     * Gets the model.
     * @return The model
     */
    IModel getModel();
    
    /**
     * Gets the view.
     * @return The view
     */
    IView getView();
    
    /**
     * Gets the API call for job search.
     * @param query The search query
     * @param numberOfResults The number of results to return
     * @param location The location to filter by
     * @param industry The industry to filter by
     * @return List of job records
     */
    List<JobRecord> getApiCall(String query, Integer numberOfResults, String location, String industry);
    
    /**
     * Gets the saved jobs from the model.
     * @return List of saved job records
     */
    List<JobRecord> getSavedJobs();
    
    /**
     * Sets the saved jobs in the model.
     * @param savedJobs List of job records to set
     * @return List of saved job records
     */
    List<JobRecord> setSavedJobs(List<JobRecord> savedJobs);
    
    /**
     * Checks if a job is already in the saved jobs list.
     * @param jobRecord The job record to check
     * @return true if the job is already saved, false otherwise
     */
    boolean isJobAlreadySaved(JobRecord jobRecord);
    
    /**
     * Adds a job to the saved jobs list.
     * @param jobRecord The job record to add
     */
    void jobToSavedList(JobRecord jobRecord);

    
    /**
     * Removes a job from the saved jobs list.
     * @param index The index of the job to remove
     */
    void removeJobFromList(int index);
    
    /**
     * Saves the job records to a CSV file.
     * @param filePath The file path to save the CSV file
     */
    void pathToCSV(String filePath);
    
    /**
     * Exports the saved jobs to a specified format and file path.
     * @param jobs The list of job records to export
     * @param formatStr The format string (e.g., "csv", "json")
     * @param filePath The file path to save the exported file
     */
    void exportToFileType(List<JobRecord> jobs, String formatStr, String filePath);
    
    /**
     * Updates a job with new comments and rating.
     * @param id The ID of the job to update
     * @param comments The comments to set
     * @param rating The rating to set
     * @return The updated job record
     */
    JobRecord getUpdateJob(int id, String comments, int rating);
    
    /**
     * Sends an alert message.
     * This notifies all registered alert observers.
     * @param alert The alert message to send
     */
    void sendAlert(String alert);
    
    /**
     * Cleans a job record and sanitizes HTML for view.
     * @param job The job record to clean
     * @return The cleaned job record
     */
    JobRecord cleanJobRecord(JobRecord job);

    /**
     * Suggests query corrections based on common terms.
     * @return A query suggestion
     */
    String suggestQueryCorrection(String query, int resultCount);
}