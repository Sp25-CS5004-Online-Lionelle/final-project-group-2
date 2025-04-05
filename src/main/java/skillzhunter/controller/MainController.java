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
     * this methos queries job board api for jobs api and returns the results.
     * @param query
     * @param numberOfResults
     * @param location
     * @param industry
     * @return
     */
    @Override
    public  List<JobRecord> getApiCall(String query, Integer numberOfResults, String location, String industry) {
        return JobBoardApi.getJobBoard(query, numberOfResults, location, industry);
    }

    //getApiCall to send to view


    /**
     * This will be used for testing purposes.
     */
    public static void main(String[] args){
        MainController mainController = new MainController();
    }

}
