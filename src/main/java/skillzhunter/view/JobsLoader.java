package skillzhunter.view;

import java.util.Collection;
import java.util.List;
import skillzhunter.model.JobRecord;

//* Static utility class to convert JobRecord collections into Object arrays for JTables */
public final class JobsLoader {

  /** Column Names. */
  private static final String[] COLUMN_NAMES = {"Logo", "Job Title", "Company",
      "Level", "Salary Range", "Currency"};

  /** private constructor to prevent instantiation. */
  private JobsLoader() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }
  /**
   * Gets the column names.
   * @return String[] of column names
   */
  public static String[] getColumnNames() {
    return COLUMN_NAMES;
  }

  //made this for converting to lists, likely uneccesary and can be removed later
  /**
   * Converts a collection of jobs into a list.
   * @param jobs Collection of job records
   * @return List of JobRecord objects
   */
  public static List<JobRecord> getJobList(Collection<JobRecord> jobs) {
    List<JobRecord> jobList = jobs.stream().toList();
    return jobList;
  }

  //Converts a collection of jobs into a type that JTable can understand
  /**
   * Converts a collection of job records into a 2D Object array for JTable.
   * This method formats the data for display in a table.
   * @param jobs Collection of job records
   * @return 2D Object array containing job data
   */
  public static Object[][] getData(Collection<JobRecord> jobs) {
    List<JobRecord> jobList = jobs.stream().toList();
    Object[][] data = new Object[jobList.size()][6]; // 6 columns including logo

    for (int i = 0; i < jobList.size(); i++) {
      JobRecord record = jobList.get(i);

      // Format the salary range
      String salaryRange = (record.annualSalaryMin() == 0 || record.annualSalaryMax() == 0
                            || record.annualSalaryMin() == 0 && record.annualSalaryMax() == 0) 
            ? "N/A" 
            : String.format("%,d - %,d", record.annualSalaryMin(), record.annualSalaryMax());

      // Format currency
      String salaryCurrency = (record.salaryCurrency() == null || record.salaryCurrency().isEmpty()) 
                ? "N/A" 
                : record.salaryCurrency();    
      
      // Add data for each column including logo URL
      data[i][0] = record.companyLogo(); // Logo URL in first column
      data[i][1] = record.jobTitle();
      data[i][2] = record.companyName();
      data[i][3] = record.jobLevel();
      data[i][4] = salaryRange;
      data[i][5] = salaryCurrency;
    }

    return data;
  }
  
  /**
   * Gets the logo URLs from a collection of job records.
   * This method can be useful for testing or debugging.
   * 
   * @param jobs Collection of job records
   * @return Array of logo URLs
   */
  public static String[] getLogoUrls(Collection<JobRecord> jobs) {
    List<JobRecord> jobList = jobs.stream().toList();
    String[] logoUrls = new String[jobList.size()];
    
    for (int i = 0; i < jobList.size(); i++) {
      JobRecord record = jobList.get(i);
      logoUrls[i] = record.companyLogo();
    }
    
    return logoUrls;
  }
}
