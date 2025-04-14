package skillzhunter.model.net;

import java.util.List;
import skillzhunter.model.JobRecord;

/**
 * Class that represents the result of a job board API query,
 * including both the job records and any error/warning messages.
 * This class encapsulates the full response from a job board API,
 * providing access to both successfully retrieved jobs and any
 * error information that occurred during the API request.
 */
public class JobBoardApiResult {
  /** List of job records retrieved from the API. */
  private final List<JobRecord> jobs;
  
  /** Error message if the API request failed, or null if successful. */
  private final String errorMessage;

  /**
   * Constructs a new JobBoardApiResult with the given job records and error message.
   * 
   * @param jobs list of job records retrieved from the API, may be empty but not null
   * @param errorMessage the error message if the API request failed, or null if successful
   */
  public JobBoardApiResult(List<JobRecord> jobs, String errorMessage) {
    this.jobs = jobs;
    this.errorMessage = errorMessage;
  }

  /**
   * Gets the list of job records retrieved from the API.
   * 
   * @return the list of job records, may be empty but not null
   */
  public List<JobRecord> getJobs() {
    return jobs;
  }

  /**
   * Gets the error message from the API request.
   * 
   * @return the error message if an error occurred, or null if the request was successful
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * Checks if this result contains an error message.
   * 
   * @return true if this result contains a non-empty error message, false otherwise
   */
  public boolean hasError() {
    return errorMessage != null && !errorMessage.isEmpty();
  }
}
