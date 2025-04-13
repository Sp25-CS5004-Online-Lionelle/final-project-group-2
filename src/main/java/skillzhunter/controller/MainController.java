package skillzhunter.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skillzhunter.model.IModel;
import skillzhunter.model.JobRecord;
import skillzhunter.model.Jobs;
import skillzhunter.model.formatters.DataFormatter;
import skillzhunter.model.formatters.Formats;
import skillzhunter.view.IView;
import skillzhunter.view.MainView;
import skillzhunter.view.SavedJobsTab;

public class MainController implements IController {

    /** Model */
    private IModel model;
    /** View */
    private IView view;
    /** Saved jobs tab */
    private SavedJobsTab savedJobsTab;

    /**
     * Constructor for MainController.
     * Initializes the model and view.
     */
    public MainController() {
        // Model
        model = new Jobs();
        model.setController(this);

        // Saved jobs tab initialized with actual saved jobs list
        savedJobsTab = new SavedJobsTab(this, model.getJobRecords());

        // View
        view = new MainView(this);  // You might want to make sure MainView takes savedJobsTab
    }

    /**
     * Returns the view.
     * 
     * @return The view
     */
    @Override
    public IView getView() {
        return view;
    }

    /**
     * Sets the view and updates the saved jobs tab.
     * 
     * @param view The view to set
     */
    protected void setView(IView view) {
        this.view = view;
    }

    /**
     * Returns the model.
     */
    @Override
    public IModel getModel() {
        return model;
    }

    /**
     * Sets the model and updates the saved jobs tab.
     * 
     * @param model The model to set
     */
    protected void setModel(IModel model) {
        this.model = model;
        savedJobsTab.updateJobsList(model.getJobRecords());
    }
    

    /**
     * Gets the locations from the API and capitalizes them appropriately.
     */
    @Override
    public List<String> getLocations() {
        return capitalizeItems(model.getLocations(), Collections.emptyMap());
    }

    /**
     * Gets the industries from the API and capitalizes them appropriately.
     */
    @Override
    public List<String> getIndustries() {
        Map<String, String> specialCases = new HashMap<>();
        specialCases.put("hr", "HR");
        return capitalizeItems(model.getIndustries(), specialCases);
    }

    /**
     * Capitalizes the items in the list.
     * Handles multi-word strings and keeps in mind any special cases for capitalization.
     * @param items List of strings to capitalize
     * @param specialCases Map of special case words and their capitalization
     * @return List of capitalized strings
     */
    private List<String> capitalizeItems(List<String> items, Map<String, String> specialCases) {
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
     * Capitalizes a single word.
     * @param word The word to capitalize
     * @return The capitalized word
     */
    private String capitalizeWord(String word) {
        if (word.isEmpty()) {
            return word;
        }
        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }

    /**
     * Gets the API call for job search.
     * 
     * @param query The search query
     * @param numberOfResults The number of results to return
     * @param location The location to filter by
     * @param industry The industry to filter by
     * @return List of JobRecord objects
     */
    @Override
    public List<JobRecord> getApiCall(String query, Integer numberOfResults, String location, String industry) {
        return model.searchJobs(query, numberOfResults, location, industry);
    }

    /**
     * Gets the saved jobs.
     * 
     * @return List of JobRecord objects
     */
    @Override
    public List<JobRecord> getSavedJobs() {
        return model.getJobRecords();
    }

    /**
     * Sets the saved jobs.
     * 
     * @param savedJobs List of JobRecord objects to set
     * @return List of JobRecord objects
     */
    public List<JobRecord> setSavedJobs(List<JobRecord> savedJobs) {
        for (JobRecord job : savedJobs) {
            model.addJob(job);
        }
        List<JobRecord> savedJobsList = model.getJobRecords();
        savedJobsTab.updateJobsList(savedJobsList);
        return savedJobsList;
    }

    /**
     * Adds a job to the saved jobs list.
     * 
     * @param jobRecord The JobRecord object to add
     */
    @Override
    public void getAddJob(JobRecord jobRecord) {
        model.addJob(jobRecord);
    }

    /**
     * Removes a job from the saved jobs list.
     * 
     * @param index The index of the job to remove
     */
    @Override
    public void getRemoveJob(int index) {
        model.removeJob(index);
    }


    /**
     * Saves the job records to a CSV file.
     */
    @Override
    public void getSavedJobsToCsv(String filePath) {
        model.saveJobsToCsv(filePath);
    }

    /**
     * Exports the saved jobs to a specified format and file path.
     */
    @Override
    public void getExportSavedJobs(List<JobRecord> jobs, String formatStr, String filePath) {
        model.exportSavedJobs(jobs, formatStr, filePath);
    }

    /**
     * Gets the saved jobs tab.
     * 
     * @return The saved jobs tab
     */
    public SavedJobsTab getSavedJobsTab() {
        return savedJobsTab;
    }
    

    /**
     * Updates a job with new comments and rating
     * 
     * @param id The ID of the job to update
     * @param comments The comments to set
     * @param rating The rating to set
     * @return The updated JobRecord
     */
    public JobRecord getUpdateJob(int id, String comments, int rating) {
        model.updateJob(id, comments, rating); //IDEA: if model.updateJob returns a record then you can delete the loop at the end
        savedJobsTab.updateJobsList(model.getJobRecords());
        
        // Return the updated job record so the view can use it
        for (JobRecord job : model.getJobRecords()) {
            if (job.id() == id) {
                return job;
            }
        }
        return null;
    }

    public void sendAlert(String alert) {
        if (this.view != null) {
            this.view.notifyUser(alert);
        }
    }


    public static void main(String[] args) {
        MainController mainController = new MainController();
    }
}