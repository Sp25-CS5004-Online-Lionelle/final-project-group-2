package skillzhunter.controller;


import skillzhunter.view.FindJobView;

public class FindJobController {
    private FindJobView view;

    public FindJobController(FindJobView findJobTabbed) {
        this.view = findJobTabbed;
        this.view.addFeatures(this); //give this controller to the view
    }

    public FindJobView getView() {
        return view;
    }


    public void dummyFindJobMethod(){
        this.view.setRecordText("Dummy Find Job Method Called");
    }

}
