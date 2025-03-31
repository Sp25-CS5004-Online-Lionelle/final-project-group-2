package skillzhunter.controller;

import java.util.List;

import skillzhunter.model.IModel;
import skillzhunter.model.JobRecord;
import skillzhunter.model.Jobs;
import skillzhunter.model.net.JobBoardApi;
import skillzhunter.view.FindJobView;
import skillzhunter.view.IView;
import skillzhunter.view.MainView;
import skillzhunter.view.SavedJobView;

public class MainController implements IController {
    private IView view;
    private IModel model;
    private FindJobController findJobController;
    private SavedJobController savedJobController;
    private SavedJobView savedJobTabbed;
    private FindJobView searchJobTabbed;

    public MainController(){
        /** Model */
        this.model = new Jobs();
        
        /** Views */
        this.searchJobTabbed = new FindJobView();
        this.savedJobTabbed = new SavedJobView();

        /** Controllers */
        this.findJobController = new FindJobController(searchJobTabbed);

        this.savedJobController = new SavedJobController(savedJobTabbed);

        //theres an inheritance reason for this .... may move it back
        this.savedJobController.setFeatures(); //aka give the savedJobTabbed View the relevant controller obj
        
        this.view = new MainView(this.findJobController.getView(), this.savedJobController.getView());

    }

    /**
     * This method sets the view for the controller.
     * It also adds the controller as a feature to the view.
     * This will be used for testing purposes.
     * @param view The view to be set for the controller.
     */
    protected void setView(IView view) {
        this.view = view;
        view.addFeatures(this);
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

    //getApiCall to send to view
    /**
     * This method gets the API call using the controller.
     * 
     */
    public List<JobRecord> getApiCall(String query, Integer numberOfResults, String location, String industry) {
        return JobBoardApi.getJobBoard(query, numberOfResults, location, industry);
    }

    /**
     * This will be used for testing purposes.
     */
    public static void main(String[] args){
        MainController mainController = new MainController();
    }
}
