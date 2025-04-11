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
    /** gets add job model*/
    void job2SavedList(JobRecord job);
    /** gets remove job */
    void getRemoveJob(int id);
    /** gets saved jobs that need to be made into a csv */
    void getSavedJobsToCsv(String filePath);
    /** exports saved jobs into selected format */
    void getExportSavedJobs(List<JobRecord> jobs, String formatStr, String filePath);
    /** gets saved jobs */
    List<JobRecord> getSavedJobs();
    
}
