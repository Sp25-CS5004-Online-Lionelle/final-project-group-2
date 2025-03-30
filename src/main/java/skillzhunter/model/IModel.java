package skillzhunter.model;
import skillzhunter.model.JobRecord;
import java.util.List;

public interface IModel {
    //TODO: clarify differences between these two
    List<JobRecord> searchJobs(String query, Integer numberOfResults,
    String location, String industry); //filter search for jobs?
    //Gets entire list of saved jobs
    List<JobRecord> getSavedJobs(String savedJob); //what are the optional parameters for getSavedJobs(string, string, string) ??

    JobRecord getJobRecord(String searchString);
    void addJobs(JobRecord jobRecord);
    void removeJobs(String jobRemoved);
    void downloadJobs(String jobDownloaded);
}

//TODO
//explain why and when were using in the app for the methods
//add javadocs to everything
//have imodel translate to Jobs.java
//make mock model - return dummy values similar to tests for domainmodelmodel
//Have SkillzHunterApp implementing correctly without any args -- database can be put in later
//Test the model currently with controler?

//TA Notes
//finalize and agree on this model interface (add javadocs to make purpose and usage of methods clear)
//      create a mock model to help develop the controller and view, then use setModel() to give controller the mock model
//      meanwhile have a branch to work on the real implementation of the model
//clarify where .run() will be - inside of IController or IView?
//add more methods to IController and/or IView to specify how the controller and view should interact
//      person writing view should know what methods it can use from the controller and what methods the controller expects from it
//      person writing controller should know what methods from the view it can use and what methods the view is expecting from it

/*      EXAMPLE:
    when someone clicks the delete button in the view it has to tell the controller through some method (even if that method is inside of a callback)
    IController{
        void deleteRecord(string);
    }

    MyView{
        IController controller;

        //later in the code...
        deleteCallback = event -> this.controller.deleteRecord(string)
    }
*/