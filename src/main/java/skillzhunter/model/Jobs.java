package skillzhunter.model;

import java.util.ArrayList;
import java.util.List;

import skillzhunter.model.net.JobBoardApi;

public class Jobs implements IModel{

        private final List<JobBean> jobList;

        public Jobs() {
                this.jobList = new ArrayList<>();
        }

        //crud functionality
        /**
         * Add a new job.
         * @param job JobBean instance to add
         */
        public void addJob(JobBean job) {
                jobList.add(job);
        }

        /**
         * Retrieve a single job record by ID.
         * @param id Job ID
         * @return JobRecord if found, otherwise null
         */
        public JobRecord getJobRecord(String jobTitle) {
                for (JobBean job : jobList) {
                        if (job.getJobTitle().equals(jobTitle)) {
                                return job.toRecord();
                        }
                }
                return null;
        }

        /**
         * Retrieve all job records.
         * @return List of JobRecords
         */
        public List<JobRecord> getJobRecords() {
                List<JobRecord> records = new ArrayList<>();
                for (JobBean job : jobList) {
                records.add(job.toRecord());
                }
                return records;
        }

        /**
         * Update a job record if it exists.
         * @param updatedJob JobBean with updated details
         * @return Updated JobRecord if successful, otherwise null
         */
        public JobRecord updateJobRecord(JobBean updatedJob) {
                if (updatedJob == null) {
                System.err.println("Error: Job is null.");
                return null;
                }

                int id = updatedJob.getId();
                for (int i = 0; i < jobList.size(); i++) {
                if (jobList.get(i).getId() == id) {
                        JobBean oldJob = jobList.get(i);

                        // Check if any changes are actually made
                        if (oldJob.equals(updatedJob)) {
                        System.out.println("No changes detected for Job ID: " + id);
                        return oldJob.toRecord();
                        }

                        // Update the job
                        jobList.set(i, updatedJob);
                        return updatedJob.toRecord();
                }
                }

                System.out.println("Job not found for ID: " + id);
                return null;
        }

        /**
         * Remove a job by ID.
         * @param id Job ID to remove
         * @return true if removed, false otherwise
         */
        public JobRecord removeJob(String jobTitle) {
                for (JobBean job : jobList) {
                        if (job.getJobTitle().equals(jobTitle)) {
                                jobList.remove(job);
                                return job.toRecord();
                        }
                }
                return null; // Return null if no job with the given title is found
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
         * Downloads a job based on the given job downloaded string.
         * @param jobDownloaded The job to be downloaded.
         * @throws IllegalArgumentException if the job to be downloaded is null or empty.
         */
        @Override
        public void downloadJobs(String jobDownloaded){
                //TODO: implement this method
        }

        /**
         * Searches for jobs based on a query string.
         * @param query
         * @return
         */
        public List<JobRecord> searchByQuery(String query) {
        // For simplicity, use default values for location and industry
        return JobBoardApi.getJobBoard(query, 20, "", "");
        }

        /**
         * Searches for jobs based on a location string.
         * @param location
         * @return
         */
        public List<JobRecord> searchByLocation(String location) {
        return JobBoardApi.getJobBoard("", 20, location, "");
        }

        /**
         * Searches for jobs based on an industry string.
         * @param industry
         * @return
         */
        public List<JobRecord> searchByIndustry(String industry) {
        return JobBoardApi.getJobBoard("", 20, "", industry);
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
