package skillzhunter.model;

import java.util.List;

import skillzhunter.model.net.JobBoardApi;

public class Jobs {

        public Jobs() {
        }

        public List<JobRecord> searchJobs(String query, Integer numberOfResults,
                                        String location, String industry) {
        List<JobRecord> jobResults = JobBoardApi.getJobBoard("python", 10, "USA", "business");
        return jobResults;
        }

 public static void main(String[] args) {
        Jobs jobs = new Jobs();
        List<JobRecord> jobResults = jobs.searchJobs("python", 10, "USA", "business");
        jobResults.forEach(job -> System.out.println(job));
}
}
