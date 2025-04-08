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
    void setViewData();
    /** gets add job */
    void getAddJob(JobRecord job);
    /** gets remove job */
    void getRemoveJob(int id);
    /** gets saved jobs that need to be made into a csv */
    void getSavedJobsToCsv(String filePath);
    /** gets saved jobs */
    List<JobRecord> getSavedJobs();
    /** updates comments */
    void updateComments(int id, String comments);
    /** updates rating */
    void updateRating(int id, int rating);

}
