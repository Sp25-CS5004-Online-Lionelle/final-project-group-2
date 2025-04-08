//A static utility class to help out SavedJobView allowing FindJobView change the saved job list
package skillzhunter.view;

import java.util.ArrayList;
import java.util.List;
import skillzhunter.model.JobRecord;
import skillzhunter.controller.IController;

public class SavedJobsLists {
  private static List<JobRecord> savedJobs = new ArrayList<>();
  private static IController controller;

  //observer pattern to notify views when the list changes
  private static List<JobView> observers = new ArrayList<>();

  public static void setController(IController newController) {
    SavedJobsLists.controller = newController;
  }

  public static void addObserver(JobView view) {
    if (!observers.contains(view)) {
      observers.add(view);
    }
  }

  public static void removeObserver(JobView view) {
    observers.remove(view);
  }

  public static void addSavedJob(JobRecord job) {
    System.out.println("SAVEDJOBSLIST.JAVA - add saved job:" + job);
    if (!savedJobs.contains(job)) {
      controller.getAddJob(job);
      savedJobs = controller.getSavedJobs();
      notifyObservers();
    }
  }

  public static void removeSavedJob(JobRecord job) {
    if (savedJobs.contains(job)) {
      //savedJobs.remove(job);
      controller.getRemoveJob(job.id());
      savedJobs = controller.getSavedJobs();
      System.out.println("\tRemoved job new list list");
      notifyObservers();
    }
  }

  public static List<JobRecord> getSavedJobs() {
    return new ArrayList<>(savedJobs); // Return a copy to prevent direct modification
  }

  private static void notifyObservers() {

    for (JobView observer : observers) {
      if (observer instanceof SavedJobsTab) {
        System.out.println("SAVEDJOBSLIST.JAVA - setting view jobsList to:" + getSavedJobs());
        observer.setJobsList(getSavedJobs());
      }
    }
  }
}