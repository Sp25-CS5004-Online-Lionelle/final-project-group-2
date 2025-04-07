package skillzhunter.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import skillzhunter.model.JobRecord;

//* Static utility class to convert JobRecord collections into Object arrays for JTables */
public final class JobsLoader {


  private static String[] columnNames = { "Job Title", "Company",
      "Level", "Salary Range", "Currency" };
  private final Collection<JobRecord> jobs = new ArrayList<>();

  //* private constructor to prevent instantiation */
  private JobsLoader() {
  }

  //Returns column headers as they are set at the top of the class
  public static String[] getColumnNames() {
    return columnNames;
  }

  //made this for converting to lists, likely uneccesary and can be removed later
  public static List<JobRecord> getJobList(Collection<JobRecord> jobs) {
    List<JobRecord> jobList = jobs.stream().toList();
    return jobList;
  }

  //Converts a collection of jobs into a type that JTable can understand
  public static Object[][] getData(Collection<JobRecord> jobs) {
    List<JobRecord> jobList = jobs.stream().toList();
    Object[][] data = new Object[jobList.size()][12];

    for (int i = 0; i < jobList.size(); i++) {
      JobRecord record = jobList.get(i);

      // Format the industry list as a comma-separated string
      String industry = String.join(", ", record.jobIndustry());

      // Format the job type list as a comma-separated string
      String jobType = String.join(", ", record.jobType());

      // Format the salary range
      String salaryRange = (record.annualSalaryMin() == 0 || record.annualSalaryMax() == 0 || 
            record.annualSalaryMin() == 0 && record.annualSalaryMax() == 0) 
            ? "N/A" 
            : String.format("%,d - %,d", record.annualSalaryMin(), record.annualSalaryMax());

      // Format currency
      String salaryCurrency = (record.salaryCurrency() == null || record.salaryCurrency().isEmpty()) 
                ? "N/A" 
                : record.salaryCurrency();    
      // Add data for each column
      //data[i][0] = record.id();
      data[i][0] = record.jobTitle();
      data[i][1] = record.companyName();
      //data[i][3] = industry;
      //data[i][4] = jobType;
      //data[i][5] = record.jobGeo();
      data[i][2] = record.jobLevel();
      data[i][3] = salaryRange;
      data[i][4] = salaryCurrency;
      //data[i][9] = record.pubDate();
      //data[i][10] = record.rating();
      //data[i][11] = record.comments();
    }

    return data;
  }
}