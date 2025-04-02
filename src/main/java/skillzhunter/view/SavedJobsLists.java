//A static utility class to help out SavedJobView allowing FindJobView change the saved job list
package skillzhunter.view;

import java.util.ArrayList;
import java.util.List;
import skillzhunter.model.JobRecord;

public class SavedJobsLists {
  private static List<JobRecord> savedJobs = new ArrayList<>();

  //observer pattern to notify views when the list changes
  private static List<JobView> observers = new ArrayList<>();

  public static void addObserver(JobView view) {
    if (!observers.contains(view)) {
      observers.add(view);
    }
  }

  public static void removeObserver(JobView view) {
    observers.remove(view);
  }

  public static void addSavedJob(JobRecord job) {
    if (!savedJobs.contains(job)) {
      savedJobs.add(job);
      notifyObservers();
    }
  }

  public static void removeSavedJob(JobRecord job) {
    if (savedJobs.contains(job)) {
      savedJobs.remove(job);
      notifyObservers();
    }
  }

  public static List<JobRecord> getSavedJobs() {
    return new ArrayList<>(savedJobs); // Return a copy to prevent direct modification
  }

  private static void notifyObservers() {
    for (JobView observer : observers) {
      if (observer instanceof SavedJobView) {
        observer.setJobsList(getSavedJobs());
      }
    }
  }
}