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
        @Override
        public List<JobRecord> searchJobs(String query, Integer numberOfResults,
                                        String location, String industry) {
        List<JobRecord> jobResults = JobBoardApi.getJobBoard("python", 10, "USA", "business");
        return jobResults;
        }

        /**
         * Retrieves a list of saved jobs based on the given search string.
         * @param savedJob The search string to filter saved jobs.
         * @return A list of JobRecord objects representing the saved jobs.
         * @throws IllegalArgumentException if the search string is null or empty.
         */
        @Override
        public List<JobRecord> getSavedJobs(String savedJob){
                //TODO: implement this method
                return null;
        };

        /**
         * Retrieves a JobRecord based on the given search string.
         * @param searchString The search string to filter the job record.
         * @return A JobRecord object representing the job record.
         * @throws IllegalArgumentException if the search string is null or empty.
         */
        @Override
        public JobRecord getJobRecord(String searchString){
                //TODO: implement this method
                return null;
        };

        /**
         * Adds a job record to the saved jobs list.
         * @param jobRecord The JobRecord object to be added.
         * @throws IllegalArgumentException if the job record is null.
         */
        @Override
        public void addJobs(JobRecord jobRecord){
                //TODO: implement this method
        };

        /**
         * Removes a job from the saved jobs list.
         * @param jobRemoved The job to be removed.
         * @throws IllegalArgumentException if the job to be removed is null or empty.
         */
        @Override
        public void removeJobs(String jobRemoved){
                //TODO: implement this method
        };

        /**
         * Downloads a job based on the given job downloaded string.
         * @param jobDownloaded The job to be downloaded.
         * @throws IllegalArgumentException if the job to be downloaded is null or empty.
         */
        @Override
        public void downloadJobs(String jobDownloaded){
                //TODO: implement this method
        }

        /**
         * Main method for testing purposes.
         * @param args Command line arguments (not used).
         */
        public static void main(String[] args) {
                // Jobs jobs = new Jobs();
                // List<JobRecord> jobResults = jobs.searchJobs("python", 10, "USA", "business");
                // jobResults.forEach(job -> System.out.println(job));
        }
}
