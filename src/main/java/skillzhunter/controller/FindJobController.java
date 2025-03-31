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

    public FindJobView getView() {
        return view;
    }

    //if query - search by query
    public void searchByQuery(String query) {
        this.view.setRecordText("Search by query: " + query);
        List<JobRecord> results = model.searchByQuery(query);
        //Tor/Judson will create this - commenting out until they do
        //this.view.showSearchResults(results);
    }

    public void searchByLocation(String location) {
        this.view.setRecordText("Search by location: " + location);
        List<JobRecord> results = model.searchByLocation(location);
        //Tor/Judson will create this - commenting out until they do
        //this.view.showSearchResults(results);
    }
    
    public void searchByIndustry(String industry) {
        this.view.setRecordText("Search by industry: " + industry);
        List<JobRecord> results = model.searchByIndustry(industry);
        //Tor/Judson will create this - commenting out until they do
        //this.view.showSearchResults(results);
    }

}
