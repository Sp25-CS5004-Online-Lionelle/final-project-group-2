package skillzhunter.controller;


import java.util.List;

import skillzhunter.model.IModel;
import skillzhunter.model.JobRecord;
import skillzhunter.model.Jobs;
import skillzhunter.model.net.JobBoardApi;
import skillzhunter.view.FindJobTab;
import skillzhunter.view.IView;
import skillzhunter.view.MainView;
import skillzhunter.view.SavedJobTab;


public class MainController implements IController{

    private IModel model;
    private IView view;


    public MainController(){
        /** Model */
        model = new Jobs();
        /* View */
        view = new MainView(this);
    }


    /**
     * This method sets the view for the controller.
     * It also adds the controller as a feature to the view.
     * This will be used for testing purposes.
     * @param view The view to be set for the controller.
     */
    protected void setView(IView view) {
        this.view = view;
    }

    /**
     * This method sets the view for the controller.
     * It also adds the controller as a feature to the view.
     * This will be used for testing purposes.
     * @param view The view to be set for the controller.
     */
    protected void setModel(IModel model) {
        this.model = model;
    }

    /**
     * This method gets the view using the controller.
     * 
     */
    @Override
    public IView getView() {
        return view;
    }

    /**
     * This method gets the model using the controller.
     * 
     */
    @Override
    public IModel getModel() {
        return model;
    }

    /**
     * This method gets the locations using the controller.
     * @return List<String> of locations
     */
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

    /**
     * This method gets the industries using the controller.
     * @return List<String> of industries
     */
    @Override
    public List<String> getIndustries() {
        return model.getIndustries().stream()
                    .map(industry -> {
                        String[] words = industry.split(" ");
                        StringBuilder capitalizedIndustry = new StringBuilder();
                        for (String word : words) {
                            if (!word.isEmpty()) {
                                capitalizedIndustry.append(Character.toUpperCase(word.charAt(0)))
                                                   .append(word.substring(1).toLowerCase())
                                                   .append(" ");
                            }
                        }
                        return capitalizedIndustry.toString().trim();
                    })
                    .toList();
    }
    
    /**
     * This method queries job board api for jobs api and returns the results.
     * @param query
     * @param numberOfResults
     * @param location
     * @param industry
     * @return
     */
    @Override
    public  List<JobRecord> getApiCall(String query, Integer numberOfResults, String location, String industry) {
        return model.searchJobs(query, numberOfResults, location, industry);
    }

    /**
     * Passes the add job method to the view from the model.
     * @param jobRecord
     */
    public void getAddJob(JobRecord jobRecord) {
        model.addJob(jobRecord);
    }
    
    /**
     * Passes the remove job method to the view from the model.
     * 
     * 
     * @param id
     */
    public void getRemoveJob(int id) {
        model.removeJob(id);
    }

    //getUpdateJob to send to view
    /**
     * Passes the update job method to the view from the model.
     * @param id
     * @param comments
     * @param rating
     */
    public void getUpdateJob(int id, String comments, int rating) {
        model.updateJob(id, comments, rating);
    }


    /**
     * This will be used for testing purposes.
     */
    public static void main(String[] args){
        MainController mainController = new MainController();
    }

}
