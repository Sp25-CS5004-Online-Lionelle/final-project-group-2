package skillzhunter.controler;


import skillzhunter.view.FindJobView;

public class FindJobController {
    private FindJobView view;
    public FindJobView getView() {
        return view;
    }
    public FindJobController(FindJobView findJobTabbed) {
        this.view = findJobTabbed;
    }
    public void setFeatures() {
        this.view.addFeatures(this);
    }

    public void dummyFindJobMethod(){
        this.view.setRecordText("Dummy Find Job Method Called");
    }

}
