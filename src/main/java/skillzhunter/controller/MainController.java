package skillzhunter.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import skillzhunter.model.IModel;
import skillzhunter.model.JobRecord;
import skillzhunter.model.Jobs;
import skillzhunter.model.formatters.DataFormatter;
import skillzhunter.model.formatters.Formats;
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

// if location do this by location if industry do same thing by industry
// noun is location or industry and do these things but with those names?
//  could create enum then have case switch OR might be able to use method reference class::methodName OR something else
// or everything in the map after lambda could be helper
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


    //IDEA: maybe maybe saveJobsToCsv can be generalized with the exportSavedJobs
    @Override
    public void getSavedJobsToCsv(String filePath) {
        model.saveJobsToCsv(filePath);
    }

    
    @Override
    public void exportSavedJobs(List<JobRecord> jobs, String formatStr, String filePath) {
        //IDEA: move code in here to IModel and what if we made CSV the default format..?
        Formats format = Formats.containsValues(formatStr); // Check if the format is valid
        
        // Handle unsupported formats
        if (format == null) {
            throw new IllegalArgumentException("Unsupported format: " + formatStr);
        }

        // Export the jobs to the specified file
        try (OutputStream out = new FileOutputStream(filePath)) {
            DataFormatter.write(jobs, format, out);
        } catch (IOException e) {
            throw new RuntimeException("Failed to export jobs: " + e.getMessage(), e);
        }
    }


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

    public static void main(String[] args) {
        MainController mainController = new MainController();
    }
}