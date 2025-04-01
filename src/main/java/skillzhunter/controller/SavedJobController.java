package skillzhunter.controller;

import skillzhunter.view.SavedJobView;

public class SavedJobController implements IJobController{
    private SavedJobView view;

    public SavedJobController(SavedJobView savedJobTabbed) {
        this.view = savedJobTabbed;
        this.view.addFeatures(this);
    }

    public SavedJobView getView() {
        return view;
    }

    public void setFeatures() {
        this.view.addFeatures(this);
    }

    @Override
    public void setViewData(){
        this.view.setRecordText("Dummy Saved Job Method Called");

    }

}
