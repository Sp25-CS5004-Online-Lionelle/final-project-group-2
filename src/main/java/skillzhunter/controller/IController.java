package skillzhunter.controller;


import java.util.List;

import skillzhunter.model.IModel;
import skillzhunter.model.JobRecord;
import skillzhunter.view.IView;

public interface IController {
    /**
     * Returns the view.
     * 
     * @return The view
     */
    IView getView();

    /**
     * Returns the model.
     * @return IModel
     */
    IModel getModel();

    /**
     * Gets the API call for jobicy api search.
     * 
     * @param query The search query
     * @param numberOfResults The number of results to return (Nullable)
     * @param location The location to filter by (Nullable)
     * @param industry The industry to filter by (Nullable)
     * @return List of JobRecord objects
     */
    List<JobRecord> getApiCall(String query, Integer numberOfResults, String location, String industry);
    
    /**
     * Gets the locations from the API and capitalizes them appropriately.
     * @return List<String> of locations
     */
    List<String> getLocations();
    
    /**
     * Gets the industries from the API and capitalizes them appropriately.
     * @return List<String> of industries
     */
    List<String> getIndustries();
    
    /**
     * Adds a job to the saved jobs list.
     * 
     * @param job The JobRecord object to add
     */
    void job2SavedList(JobRecord job);
    
    /**
     * Removes a job from the saved jobs list.
     * 
     * @param id The index of the job to remove
     */
    void removeJobFromList(int id);
    
    /**
     * Saves the job records to a CSV file.
     * @param filePath The file path to save the CSV file
     */
    void path2CSV(String filePath);
    
    /**
     * Exports the saved jobs to a specified format and file path.
     * @param jobs The list of JobRecord objects to export
     * @param formatStr The format string (e.g., "csv", "json")
     * @param filePath The file path to save the exported file
     */
    void export2FileType(List<JobRecord> jobs, String formatStr, String filePath);
    
    /**
     * Gets the saved jobs tab.
     * @return The saved jobs tab
     */
    List<JobRecord> getSavedJobs();
    
}
