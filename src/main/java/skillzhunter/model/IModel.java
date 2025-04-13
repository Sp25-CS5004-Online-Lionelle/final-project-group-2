package skillzhunter.model;
import java.util.List;
import skillzhunter.controller.IController;

public interface IModel {
    void setController(IController controller);
    void addJob(JobRecord job);
    JobRecord getJobRecord(String jobTitle);
    List<JobRecord> getJobRecords();
    boolean removeJob(int id);
    void updateJob(int id, String comments, int rating);
    List<JobRecord> searchJobs(String query, Integer numberOfResults, String location, String industry);
    List<String> getLocations();
    List<String> getIndustries();
    void saveJobsToCsv(String fileName);
    void exportSavedJobs(List<JobRecord> jobs, String formatStr, String filePath);
    void sendAlert(String alert);
}