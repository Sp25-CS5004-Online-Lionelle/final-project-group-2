package skillzhunter.controller;

import skillzhunter.view.SavedJobView;

public class SavedJobController {
    private SavedJobView view;
    public SavedJobView getView() {
        return view;
    }
    public SavedJobController(SavedJobView savedJobTabbed) {
        this.view = savedJobTabbed;
    }
    public void setFeatures() {
        this.view.addFeatures(this);
    }

    public void dummySavedJobMethod(){
        this.view.setRecordText("Dummy Saved Job Method Called");

    }

}
