package skillzhunter.model;

import java.util.List;

import skillzhunter.model.net.JobBoardApi;

public class Jobs implements IModel{

        public Jobs() {
        }

        /**
         * Searches for jobs based on the given parameters.
         * @param query The search query (e.g., job title, keywords).
         * @param numberOfResults The maximum number of results to return.
         * @param location The location to search for jobs in.
         * @param industry The industry to filter jobs by.
         * @return A list of JobRecord objects representing the search results.
         */
        public List<JobRecord> searchJobs(String query, Integer numberOfResults,
                                        String location, String industry) {
        List<JobRecord> jobResults = JobBoardApi.getJobBoard("python", 10, "USA", "business");
        return jobResults;
        }

        public static void main(String[] args) {
                // Jobs jobs = new Jobs();
                // List<JobRecord> jobResults = jobs.searchJobs("python", 10, "USA", "business");
                // jobResults.forEach(job -> System.out.println(job));
        }
}
