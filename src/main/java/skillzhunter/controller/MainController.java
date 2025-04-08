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

    //FIXME?
    @Override
    public void getAddJob(JobRecord jobRecord) {
        model.addJob(jobRecord);
    }

    @Override
    public void getRemoveJob(int index) {
        model.removeJob(index);
    }

    //FIXME?
    public void getUpdateJob(int id, String comments, int rating) {
        model.updateJob(id, comments, rating);
        savedJobsTab.updateJobsList(model.getJobRecords());
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

    //updates rating in the record
    /**
     * Updates the rating of a job record.
     * @param id
     * @param rating
     */
    public void updateRating(int id, int rating) {
        model.updateJob(id, null, rating);
    }

    //updates comments in the record
    /**
     * Updates the comments of a job record.
     * @param id
     * @param comments
     */
    public void updateComments(int id, String comments) {
        model.updateJob(id, comments, 0);
    }

    public static void main(String[] args) {
        MainController mainController = new MainController();
    }
}
