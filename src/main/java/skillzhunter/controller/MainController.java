package skillzhunter.controller;

import skillzhunter.model.Jobs;
import skillzhunter.view.FindJobView;
import skillzhunter.view.IView;
import skillzhunter.view.MainView;
import skillzhunter.view.SavedJobView;

public class MainController implements IController {
    private IView view;
    private Jobs model;
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
        this.findJobController.setFeatures();
        this.savedJobController = new SavedJobController(savedJobTabbed);
        this.savedJobController.setFeatures();
        this.view = new MainView(this.findJobController.getView(), this.savedJobController.getView());

    }

    @Override
    public void setView(IView view) {
        this.view = view;
        view.addFeatures(this);
    }

    @Override
    public void setModel(Jobs model) {
        this.model = model;
    }

    @Override
    public IView getView() {
        return view;
    }

    @Override
    public Jobs getModel() {
        return model;
    }

    public static void main(String[] args){
        MainController mainController = new MainController();
    }


}
