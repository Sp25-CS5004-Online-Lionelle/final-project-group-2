package skillzhunter.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import skillzhunter.model.JobRecord;

//* Static utility class to convert JobRecord collections into Object arrays for JTables */
public final class JobsLoader {


  private static String[] columnNames = {"Job ID", "Job Title", "Company",
      "Industry", "Type", "Geo", "Level", "Salary Range", "Currency", "Published Date" };
  private final Collection<JobRecord> jobs = new ArrayList<>();

  //* private constructor to prevent instantiation */
  private JobsLoader() {
  }

  public static String[] getColumnNames() {
    return columnNames;
  }

  public static Object[][] getData(Collection<JobRecord> jobs) {
    List<JobRecord> jobList = jobs.stream().toList();
    Object[][] data = new Object[jobList.size()][10];

    for (int i = 0; i < jobList.size(); i++) {
      JobRecord record = jobList.get(i);

      // Format the industry list as a comma-separated string
      String industry = String.join(", ", record.jobIndustry());

      // Format the job type list as a comma-separated string
      String jobType = String.join(", ", record.jobType());

      // Format the salary range
      String salaryRange = record.annualSalaryMin() + " - " + record.annualSalaryMax();

      // Add data for each column
      data[i][0] = record.id();
      data[i][1] = record.jobTitle();
      data[i][2] = record.companyName();
      data[i][3] = industry;
      data[i][4] = jobType;
      data[i][5] = record.jobGeo();
      data[i][6] = record.jobLevel();
      data[i][7] = salaryRange;
      data[i][8] = record.salaryCurrency();
      data[i][9] = record.pubDate();
    }

    return data;
  }
}