package skillzhunter.model.net;

import java.util.List;
import skillzhunter.model.JobRecord;

/**
 * Class that represents the result of a job board API query,
 * including both the job records and any error/warning messages.
 */
public class JobBoardApiResult {
  private final List<JobRecord> jobs;
  private final String errorMessage;

  public JobBoardApiResult(List<JobRecord> jobs, String errorMessage) {
    this.jobs = jobs;
    this.errorMessage = errorMessage;
  }

  public List<JobRecord> getJobs() {
    return jobs;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public boolean hasError() {
    return errorMessage != null && !errorMessage.isEmpty();
  }
}