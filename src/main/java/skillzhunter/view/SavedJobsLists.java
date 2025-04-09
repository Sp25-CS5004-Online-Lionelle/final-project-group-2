package skillzhunter.view;

import skillzhunter.controller.IController;
import skillzhunter.controller.MainController;
import skillzhunter.model.JobRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Static utility class for managing saved jobs across the application.
 * Works as a temporary bridge between views.
 */
public class SavedJobsLists {
    
    // List of observers that will be notified of changes
    private static final List<JobView> observers = new ArrayList<>();
    private static IController controller;

    /**
     * Get the current list of saved jobs from the controller
     * @return The list of saved jobs
     */
    public static List<JobRecord> getSavedJobs() {
        if (controller != null) {
            return controller.getSavedJobs();
        }
        return new ArrayList<>();
    }

    /**
     * Add a job to the saved jobs list 
     * @param job The job record to add
     */
    public static void addSavedJob(JobRecord job) {
        // Ensure we have a controller
        if (controller == null) {
            System.err.println("Controller not set in SavedJobsLists");
            return;
        }
        
        // Add to model through controller
        controller.getAddJob(job);
        
        // Update comments and rating
        if (controller instanceof MainController) {
            String comments = job.comments();
            int rating = job.rating();
            
            if (comments == null || comments.isEmpty()) {
                comments = "No comments provided";
            }
            ((MainController) controller).getUpdateJob(job.id(), comments, rating);
        }
        
        // Get updated list and notify observers
        List<JobRecord> updatedList = getSavedJobs();
        for (JobView observer : observers) {
            observer.updateJobsList(updatedList);
        }
    }

    /**
     * Remove a job from the saved jobs list
     * @param job The job record to remove
     */
    public static void removeSavedJob(JobRecord job) {
        
        // Ensure we have a controller
        if (controller == null) {
            System.err.println("Controller not set in SavedJobsLists");
            return;
        }
        
        // Remove from model through controller
        controller.getRemoveJob(job.id());
        
        // Get updated list and notify observers
        List<JobRecord> updatedList = getSavedJobs();
        for (JobView observer : observers) {
            observer.updateJobsList(updatedList);
        }
    }

    /**
     * Add an observer to be notified of changes to the saved jobs list
     * @param observer The observer to add
     */
    public static void addObserver(JobView observer) {
        observers.add(observer);
    }

    /**
     * Set the controller for this utility class
     * @param newController The controller to set
     */
    public static void setController(IController newController) {
        controller = newController;
    }
}