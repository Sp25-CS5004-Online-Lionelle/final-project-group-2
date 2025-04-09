package skillzhunter.controller;

import java.util.List;

import skillzhunter.model.IModel;
import skillzhunter.model.JobRecord;
import skillzhunter.model.Jobs;
import skillzhunter.view.IView;
import skillzhunter.view.MainView;
import skillzhunter.view.SavedJobsTab;

public class MainController implements IController {

    private IModel model;
    private IView view;
    private SavedJobsTab savedJobsTab;

    public MainController() {
        // Model
        model = new Jobs();

        // Saved jobs tab initialized with actual saved jobs list
        savedJobsTab = new SavedJobsTab(this, model.getJobRecords());

        // View
        view = new MainView(this);  // You might want to make sure MainView takes savedJobsTab
    }

    protected void setView(IView view) {
        this.view = view;
    }

    protected void setModel(IModel model) {
        this.model = model;
        savedJobsTab.updateJobsList(model.getJobRecords());
    }

    @Override
    public IView getView() {
        return view;
    }

    @Override
    public IModel getModel() {
        return model;
    }

    @Override
    public List<String> getLocations() {
        return model.getLocations().stream()
                .map(location -> {
                    String[] words = location.split(" ");
                    StringBuilder capitalizedLocation = new StringBuilder();
                    for (String word : words) {
                        if (!word.isEmpty()) {
                            capitalizedLocation.append(Character.toUpperCase(word.charAt(0)))
                                    .append(word.substring(1).toLowerCase())
                                    .append(" ");
                        }
                    }
                    return capitalizedLocation.toString().trim();
                })
                .toList();
    }

    @Override
    public List<String> getIndustries() {
        return model.getIndustries().stream()
                .map(industry -> {
                    String[] words = industry.split(" ");
                    StringBuilder capitalizedIndustry = new StringBuilder();
                    for (String word : words) {
                        if (!word.isEmpty()) {
                            if (word.equalsIgnoreCase("hr")) {
                                capitalizedIndustry.append("HR").append(" ");
                            } else {
                                capitalizedIndustry.append(Character.toUpperCase(word.charAt(0)))
                                        .append(word.substring(1).toLowerCase())
                                        .append(" ");
                            }
                        }
                    }
                    return capitalizedIndustry.toString().trim();
                })
                .toList();
    }

    @Override
    public List<JobRecord> getApiCall(String query, Integer numberOfResults, String location, String industry) {
        return model.searchJobs(query, numberOfResults, location, industry);
    }

    @Override
    public List<JobRecord> getSavedJobs() {
        return model.getJobRecords();
    }

    public List<JobRecord> setSavedJobs(List<JobRecord> savedJobs) {
        for (JobRecord job : savedJobs) {
            model.addJob(job);
        }
        List<JobRecord> savedJobsList = model.getJobRecords();
        savedJobsTab.updateJobsList(savedJobsList);
        return savedJobsList;
    }

    @Override
    public void getAddJob(JobRecord jobRecord) {
        model.addJob(jobRecord);
    }

    @Override
    public void getRemoveJob(int index) {
        model.removeJob(index);
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
        model.updateJob(id, comments, rating);
        savedJobsTab.updateJobsList(model.getJobRecords());
        
        // Return the updated job record so the view can use it
        for (JobRecord job : model.getJobRecords()) {
            if (job.id() == id) {
                return job;
            }
        }
        return null;
    }

    @Override
    public void getSavedJobsToCsv(String filePath) {
        model.saveJobsToCsv(filePath);
    }

    public SavedJobsTab getSavedJobsTab() {
        return savedJobsTab;
    }

    @Override
    public void setViewData() {
        // Optional, depending on your logic
    }

    /**
     * Updates the rating of a job record.
     * Now preserves any existing comments.
     * 
     * @param id The job ID
     * @param rating The new rating
     * @return The updated job record
     */
    public JobRecord updateRating(int id, int rating) {
        // Get the current job record to preserve existing comments
        String existingComments = null;
        for (JobRecord job : model.getJobRecords()) {
            if (job.id() == id) {
                existingComments = job.comments();
                break;
            }
        }
        
        // Update with existing comments and new rating
        model.updateJob(id, existingComments, rating);
        
        // Return the updated job
        for (JobRecord job : model.getJobRecords()) {
            if (job.id() == id) {
                return job;
            }
        }
        return null;
    }

    /**
     * Updates the comments of a job record.
     * Now preserves any existing rating.
     * 
     * @param id The job ID
     * @param comments The new comments
     * @return The updated job record
     */
    public JobRecord updateComments(int id, String comments) {
        // Get the current job record to preserve existing rating
        int existingRating = 0;
        for (JobRecord job : model.getJobRecords()) {
            if (job.id() == id) {
                existingRating = job.rating();
                break;
            }
        }
        
        // Update with new comments and existing rating
        model.updateJob(id, comments, existingRating);
        
        // Return the updated job
        for (JobRecord job : model.getJobRecords()) {
            if (job.id() == id) {
                return job;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        MainController mainController = new MainController();
    }
}