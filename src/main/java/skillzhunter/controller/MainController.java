package skillzhunter.controller;

import skillzhunter.model.IModel;
import skillzhunter.model.Jobs;
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

    @Override
    public IView getView() {
        return view;
    }

    @Override
    public IModel getModel() {
        return model;
    }

    public static void main(String[] args){
        MainController mainController = new MainController();
    }
}
