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
    void addJob(JobBean job);
    JobRecord removeJob(String jobRemoved);
    void downloadJobs(String jobDownloaded);
}