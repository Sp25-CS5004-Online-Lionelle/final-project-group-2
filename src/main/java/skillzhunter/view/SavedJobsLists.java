package skillzhunter.view;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Static utility class for managing saved jobs across the application.
 * Works as a temporary bridge between views.
 */
public class SavedJobsLists {
    
    private static final List<JobView> observers = new ArrayList<>();
    private static IController controller;
    private static List<JobRecord> savedJobs = new ArrayList<>();

    /**
     * Get the current list of saved jobs
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
        System.out.println("SAVEDJOBSLIST.JAVA - add saved job:" + job);
        
        // Ensure we have a controller
        if (controller == null) {
            System.err.println("Controller not set in SavedJobsLists");
            return;
        }
        
        // First make a direct add to the model
        controller.getAddJob(job);
        
        // Then explicitly update with values we want
        if (controller instanceof skillzhunter.controller.MainController) {
            String comments = job.comments();
            int rating = job.rating();
            
            if (comments == null || comments.isEmpty()) {
                comments = "No comments provided";
            }
            
            if (rating < 0 || rating > 5) {
                rating = 0;
            }
            
            System.out.println("Updating job with comments: " + comments + " and rating: " + rating);
            ((skillzhunter.controller.MainController) controller).getUpdateJob(job.id(), comments, rating);
        }
        
        // Get the updated job list from the model
        List<JobRecord> updatedList = controller.getSavedJobs();
        
        // Update all observers
        System.out.println("SAVEDJOBSLIST.JAVA - setting view jobsList to:" + updatedList);
        for (JobView observer : observers) {
            observer.updateJobsList(updatedList);
        }
    }

    /**
     * Remove a job from the saved jobs list
     * @param job The job record to remove
     */
    public static void removeSavedJob(JobRecord job) {
        System.out.println("SAVEDJOBSLIST.JAVA - remove saved job:" + job);
        
        // Ensure we have a controller
        if (controller == null) {
            System.err.println("Controller not set in SavedJobsLists");
            return;
        }
        
        // Remove the job from the model
        controller.getRemoveJob(job.id());
        
        // Get the updated job list to ensure we have the most recent data
        List<JobRecord> updatedList = controller.getSavedJobs();
        
        // Update all observers
        System.out.println("SAVEDJOBSLIST.JAVA - setting view jobsList to:" + updatedList);
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