package skillzhunter.controller;


import skillzhunter.view.FindJobView;


import java.util.List;

import skillzhunter.model.JobRecord;
import skillzhunter.model.Jobs;
public class FindJobController {
    private FindJobView view;
    private Jobs model;
    public FindJobController(FindJobView findJobTabbed) {
        this.view = findJobTabbed;
        this.view.addFeatures(this); //give this controller to the view
    }

    /**
     * This method sets the view for the controller.
     * @return
     */
    public FindJobView getView() {
        return view;
    }

    /**
     * Sets text for the view when find job is called.
     */
    public void dummyFindJobMethod(){
        this.view.setRecordText("Dummy Find Job Method Called");
    }

    //if query - search by query
    /**
     * Searches for jobs based on the query.
     * @param query
     */
    public void searchByQuery(String query) {
        this.view.setRecordText("Search by query: " + query);
        List<JobRecord> results = model.searchByQuery(query);
        //Tor/Judson will create this - commenting out until they do
        //this.view.showSearchResults(results);
    }

    /**
     * Searches for jobs based on the location.
     * @param location
     */
    public void searchByLocation(String location) {
        this.view.setRecordText("Search by location: " + location);
        List<JobRecord> results = model.searchByLocation(location);
        //Tor/Judson will create this - commenting out until they do
        //this.view.showSearchResults(results);
    }
    
    /**
     * Searches for jobs based on the industry.
     * @param industry
     */
    public void searchByIndustry(String industry) {
        this.view.setRecordText("Search by industry: " + industry);
        List<JobRecord> results = model.searchByIndustry(industry);
        //Tor/Judson will create this - commenting out until they do
        //this.view.showSearchResults(results);

    }

}
