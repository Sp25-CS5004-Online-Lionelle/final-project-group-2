package skillzhunter.view;

import skillzhunter.controller.IController;

public class SavedJobView extends JobView {
    private IController controller;

    public SavedJobView(IController controller) {

        super();
        setJobsList(SavedJobsLists.getSavedJobs());
        // this.searchButton.setText("Saved Jobs");
        this.savedJobs = true;

        SavedJobsLists.addObserver(this);
    }




    public static void main(String[] args) {
        System.out.println("hello");
    }

}
