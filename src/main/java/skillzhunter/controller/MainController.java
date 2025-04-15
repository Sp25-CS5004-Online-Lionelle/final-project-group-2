package skillzhunter.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skillzhunter.model.IModel;
import skillzhunter.model.IModel.AlertListener;
import skillzhunter.model.JobRecord;
import skillzhunter.model.Jobs;
import skillzhunter.model.formatters.DataFormatter;
import skillzhunter.view.IView;
import skillzhunter.view.MainView;
import skillzhunter.view.SavedJobsTab;

public class MainController implements IController, AlertListener {

    /** Model. */
    private IModel model;
    /** View. */
    private IView view;
    /** Saved jobs tab. */
    private SavedJobsTab savedJobsTab;
    /** List of alert observers. */
    private final List<AlertObserver> alertObservers = new ArrayList<>();

    /**
     * Constructor for MainController.
     * Initializes the model and view.
     */
    public MainController() {
        // Model
        model = new Jobs();
        
        // Register this controller as the model's alert listener
        model.setAlertListener(this);
        
        // Saved jobs tab initialized with actual saved jobs list
        savedJobsTab = new SavedJobsTab(this, model.getJobRecords());

        // View - create after model and controller are properly linked
        view = new MainView(this);
    }

    /**
     * Registers an alert observer with this controller.
     * @param observer The alert observer to register
     */
    @Override
    public void registerAlertObserver(AlertObserver observer) {
        if (observer != null && !alertObservers.contains(observer)) {
            alertObservers.add(observer);
        }
    }
    
    /**
     * Unregisters an alert observer from this controller.
     * @param observer The alert observer to unregister
     */
    @Override
    public void unregisterAlertObserver(AlertObserver observer) {
        alertObservers.remove(observer);
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
    public void setView(IView view) {
        this.view = view;
    }

    /**
     * Returns the model.
     * @return IModel
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
    public void setModel(IModel model) {
        this.model = model;
        // Register this controller as the model's alert listener
        model.setAlertListener(this);
        savedJobsTab.updateJobsList(model.getJobRecords());
    }
    
    /**
     * Gets the locations from the API and capitalizes them appropriately.
     * @return List<String> of locations
     */
    @Override
    public List<String> getLocations() {
        return capitalizeItems(model.getLocations(), Collections.emptyMap());
    }

    /**
     * Gets the industries from the API and capitalizes them appropriately.
     * @return List<String> of industries
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
     * Gets the API call for jobicy api search.
     * 
     * @param query The search query
     * @param numberOfResults The number of results to return (Nullable)
     * @param location The location to filter by (Nullable)
     * @param industry The industry to filter by (Nullable)
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
    @Override
    public List<JobRecord> setSavedJobs(List<JobRecord> savedJobs) {
        for (JobRecord job : savedJobs) {
            model.addJob(job);
        }
        List<JobRecord> savedJobsList = model.getJobRecords();
        savedJobsTab.updateJobsList(savedJobsList);
        return savedJobsList;
    }

    /**
     * Checks if a job is already in the saved jobs list.
     * 
     * @param jobRecord The job record to check
     * @return true if the job is already saved, false otherwise
     */
    @Override
    public boolean isJobAlreadySaved(JobRecord jobRecord) {
        List<JobRecord> savedJobs = getSavedJobs();
        
        // Check if the job is already in the list
        return savedJobs.stream()
                .anyMatch(job -> job.id() == jobRecord.id());
    }

    /**
     * Adds a job to the saved jobs list.
     * 
     * @param jobRecord The JobRecord object to add
     */
    @Override
    public void jobToSavedList(JobRecord jobRecord) {
        model.addJob(jobRecord);
    }
    
    /**
     * Try to add a job to the saved jobs list, checking for duplicates.
     * 
     * @param jobRecord The JobRecord object to add
     * @return true if the job was added successfully, false if it was already in the list
     */
    @Override
    public boolean tryAddJobToSavedList(JobRecord jobRecord) {
        // Check if the job is already in the list
        if (isJobAlreadySaved(jobRecord)) {
            return false;
        }
        
        // Add the job to the list
        model.addJob(jobRecord);
        return true;
    }

    /**
     * Removes a job from the saved jobs list.
     * 
     * @param index The index of the job to remove
     */
    @Override
    public void removeJobFromList(int index) {
        model.removeJob(index);
    }

    /**
     * Saves the job records to a CSV file using a custom CSV writer
     * that properly handles collections and HTML content.
     * 
     * @param filePath The file path to save the CSV file
     */
    @Override
    public void pathToCSV(String filePath) {
        // Get all job records
        List<JobRecord> jobs = model.getJobRecords();

        // Make sure the parent directory exists
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            if (!dirCreated) {
                System.err.println("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }

        // Get from DataFormatter the custom CSV writer
        System.out.println("Saving to: " + file.getAbsolutePath());
        DataFormatter.exportCustomCSV(jobs, filePath);
    }
    
    /**
     * Exports the saved jobs to a specified format and file path.
     * @param jobs The list of JobRecord objects to export
     * @param formatStr The format string (e.g., "csv", "json")
     * @param filePath The file path to save the exported file
     */
    @Override
    public void exportToFileType(List<JobRecord> jobs, String formatStr, String filePath) {
        // Make sure the parent directory exists
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            if (!dirCreated) {
                System.err.println("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }

        System.out.println("Exporting " + jobs.size() + " jobs to " + formatStr + ": " + file.getAbsolutePath());
        
        if ("CSV".equalsIgnoreCase(formatStr)) {
            // Use the static method in DataFormatter
            DataFormatter.exportCustomCSV(jobs, filePath);
        } else {
            // Use the model's export for other formats
            model.exportSavedJobs(jobs, formatStr, filePath);
        }
        
        // Verify the file was created
        File savedFile = new File(filePath);
        System.out.println("File exists after export: " + savedFile.exists() + ", size: " + savedFile.length());
    }

    /**
     * Gets the saved jobs tab.
     * @return The saved jobs tab
     */
    @Override
    public SavedJobsTab getSavedJobsTab() {
        return savedJobsTab;
    }
    
    /**
     * Updates a job with new comments and rating.
     * 
     * @param id The ID of the job to update
     * @param comments The comments to set
     * @param rating The rating to set
     * @return The updated JobRecord
     */
    @Override
    public JobRecord getUpdateJob(int id, String comments, int rating) {
        // Call model to update the job
        model.updateJob(id, comments, rating);
        
        // Update the view with the latest data
        savedJobsTab.updateJobsList(model.getJobRecords());
        
        // Return the updated job record so the view can use it
        for (JobRecord job : model.getJobRecords()) {
            if (job.id() == id) {
                return job;
            }
        }
        
        // Job not found
        return null;
    }

    /**
     * Handles alert messages from the model and forwards them to observers.
     * This is the implementation of the AlertListener interface.
     * 
     * @param alertMessage The alert message
     */
    @Override
    public void onAlert(String alertMessage) {
        sendAlert(alertMessage);
    }

    /**
     * Sends an alert to all registered alert observers.
     * 
     * @param alert The alert message
     */
    @Override
    public void sendAlert(String alert) {
        System.out.println("Controller alerting: " + alert);
        
        // Make a copy of the list in case an observer removes itself during notification
        List<AlertObserver> observers = new ArrayList<>(alertObservers);
        
        if (!observers.isEmpty()) {
            // Notify all registered observers
            for (AlertObserver observer : observers) {
                observer.onAlert(alert);
            }
        } else {
            // Fallback if no observers are registered
            System.err.println("No alert observers registered: " + alert);
        }
    }

    /**
     * Clean job record and sanitize html for view.
     * @param job Job record to clean
     * @return Cleaned job record
     */
    @Override
    public JobRecord cleanJobRecord(JobRecord job){
        JobRecord cleanJobRecord = DataFormatter.processJobHtml(job);
        return cleanJobRecord;
    }
    
    /**
     * Main method for testing purposes.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        MainController mainController = new MainController();
        IView mainView = mainController.getView();
        mainView.run();
    }
}
