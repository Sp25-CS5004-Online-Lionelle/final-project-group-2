package skillzhunter.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skillzhunter.model.AlertListener;
import skillzhunter.model.IModel;
import skillzhunter.model.JobRecord;
import skillzhunter.view.IView;

public class MainController implements IController, AlertListener {

    /** Model. */
    private IModel model;
    /** View. */
    private IView view;
    /** List of alert observers. */
    private final List<AlertObserver> alertObservers = new ArrayList<>();

    /**
     * Constructor for MainController.
     * Creates a controller without model or view.
     */
    public MainController() {
        // Empty constructor, model and view are set later through setter methods
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
     * Sets the model for this controller.
     * Also registers this controller as the model's alert listener.
     * 
     * @param model The model to set
     */
    @Override
    public void setModel(IModel model) {
        this.model = model;
        
        // Register this controller as the model's alert listener
        if (model != null) {
            model.setAlertListener(this);
        }
    }
    
    /**
     * Sets the view for this controller.
     * Also gives the view a reference to this controller.
     * 
     * @param view The view to set
     */
    @Override
    public void setView(IView view) {
        this.view = view;
        
        // Give view a reference to this controller
        if (view != null) {
            view.setController(this);
        }
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
     * Returns the model.
     * @return IModel
     */
    @Override
    public IModel getModel() {
        return model;
    }
    
    /**
     * Gets the locations from the model.
     * @return List<String> of locations
     */
    @Override
    public List<String> getLocations() {
        if (model == null) {
            return Collections.emptyList();
        }
        return model.getLocations();
    }

    /**
     * Gets the industries from the model.
     * @return List<String> of industries
     */
    @Override
    public List<String> getIndustries() {
        if (model == null) {
            return Collections.emptyList();
        }
        return model.getIndustries();
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
        if (model == null) {
            return Collections.emptyList();
        }
        return model.searchJobs(query, numberOfResults, location, industry);
    }

    /**
     * Gets the saved jobs.
     * 
     * @return List of JobRecord objects
     */
    @Override
    public List<JobRecord> getSavedJobs() {
        if (model == null) {
            return Collections.emptyList();
        }
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
        if (model == null || savedJobs == null) {
            return Collections.emptyList();
        }
        
        for (JobRecord job : savedJobs) {
            model.addJob(job);
        }
        
        return model.getJobRecords();
    }

    /**
     * Checks if a job is already in the saved jobs list.
     * 
     * @param jobRecord The job record to check
     * @return true if the job is already saved, false otherwise
     */
    @Override
    public boolean isJobAlreadySaved(JobRecord jobRecord) {
        if (model == null || jobRecord == null) {
            return false;
        }
        
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
        if (model == null || jobRecord == null) {
            return;
        }
                
        // Check if the job is already in the list
        if (isJobAlreadySaved(jobRecord)) {
            return;
        }
        model.addJob(jobRecord);
    }

    /**
     * Removes a job from the saved jobs list.
     * 
     * @param index The index of the job to remove
     */
    @Override
    public void removeJobFromList(int index) {
        if (model == null) {
            return;
        }
        
        model.removeJob(index);
    }

    /**
     * Saves the job records to a CSV file.
     * 
     * @param filePath The file path to save the CSV file
     */
    @Override
    public void pathToCSV(String filePath) {
        if (model == null || filePath == null || filePath.isEmpty()) {
            return;
        }
        
        // Use the model's method to save to CSV
        model.saveJobsToCsv(filePath);
    }
    
    /**
     * Exports the saved jobs to a specified format and file path.
     * This uses the model's exportSavedJobs method which handles all formats.
     * 
     * @param jobs The list of JobRecord objects to export
     * @param formatStr The format string (e.g., "csv", "json")
     * @param filePath The file path to save the exported file
     */
    @Override
    public void exportToFileType(List<JobRecord> jobs, String formatStr, String filePath) {
        if (model == null || jobs == null || formatStr == null || filePath == null) {
            return;
        }
        
        // Make sure the parent directory exists
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                System.err.println("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }

        // Use the model's method to export jobs in the specified format
        model.exportSavedJobs(jobs, formatStr, filePath);
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
        if (model == null) {
            return null;
        }
        
        // Call model to update the job
        model.updateJob(id, comments, rating);
        
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
     * @param message The alert message
     */
    @Override
    public void onAlert(String message) {
        // Forward the alert to all registered observers
        sendAlert(message);
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
     * Uses the model's cleanJob method to process the job record.
     * 
     * @param job Job record to clean
     * @return Cleaned job record
     */
    @Override
    public JobRecord cleanJobRecord(JobRecord job){
        if (model == null || job == null) {
            return null;
        }
        
        return model.cleanJob(job);
    }

    /**
     * Suggests a query correction based on the given query and result count.
     * This method uses the model's suggestQueryCorrection method.
     * @return A query suggestion.
     */
    @Override
    public String suggestQueryCorrection(String query, int resultCount) {
        if (model == null) {
            return null;
        }
        return model.suggestQueryCorrection(query, resultCount);
    }
}
