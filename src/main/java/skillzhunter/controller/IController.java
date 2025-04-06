package skillzhunter.controller;


import java.util.List;

import skillzhunter.model.IModel;
import skillzhunter.model.JobRecord;
import skillzhunter.view.IView;

public interface IController {
    /** gets View */
    IView getView();
    
    /** gets Model */
    IModel getModel();
    /** queries jobicy api */
    List<JobRecord> getApiCall(String query, Integer numberOfResults, String location, String industry);
    /** gets locations */
    List<String> getLocations();
    /** gets industries */
    List<String> getIndustries();

}
