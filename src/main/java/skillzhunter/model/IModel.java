package skillzhunter.model;
import java.util.List;

public interface IModel {
    void addJob(JobRecord job);
    JobRecord getJobRecord(String jobTitle);
    List<JobRecord> getJobRecords();
    boolean removeJob(int id);
    void updateJob(int id, String comments, int rating);
    List<JobRecord> searchByQuery(String query);
    List<JobRecord> searchByLocation(String location);
    List<JobRecord> searchByIndustry(String industry);
    List<JobRecord> searchJobs(String query, Integer numberOfResults, String location, String industry);
    List<String> getLocations();
    List<String> getIndustries();
    void saveJobsToCsv(String fileName);
    
    // List<JobRecord> getSavedJobs(String savedJob); //what are the optional parameters for getSavedJobs(string, string, string)
    //void downloadJobs(String jobDownloaded);
}