package skillzhunter.view;

import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;

import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;

import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumn;
import skillzhunter.controller.FindJobController;
import skillzhunter.model.JobRecord;


public class FindJobView extends JPanel {
    private JButton searchButton;
    private JTextArea recordText;
    private JobsTable jobsTable;
    private List<JobRecord> jobsList = JobRecordGenerator.generateDummyRecords(10);

    public FindJobView() {
        setSize(600,600);
        JPanel mainPanel = new JPanel();
        BoxLayout layout = new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS);
        mainPanel.setLayout(layout);

        searchButton = new JButton("Find Jobs");
        mainPanel.add(searchButton);

        recordText = new JTextArea("click to find jobs");
        mainPanel.add(recordText);


        JPanel tablePanel = new JPanel(new FlowLayout());
        tablePanel.setSize(600,600);


        jobsTable = new JobsTable(getColumnNames(), getData(jobsList));

        //experimenting with column widths
        TableColumn column1 = jobsTable.getColumnModel().getColumn(0); // Name column
        column1.setPreferredWidth(150);
        column1.setMinWidth(150);
        column1.setMaxWidth(150);

        TableColumn column2 = jobsTable.getColumnModel().getColumn(1); // Name column
        column2.setPreferredWidth(150);
        column2.setMinWidth(150);
        column2.setMaxWidth(150);

        //attempting to make the table scrollable
        //so far has not worked
        JScrollPane tablePane = new JScrollPane();
        tablePane.setViewportView(jobsTable);
        tablePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tablePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tablePanel.add(tablePane);
        mainPanel.add(tablePanel);

        //this code is for the job details popup
        //currently doesn't actually save anything
        JButton openJob = new JButton("Open Selected Job");
        openJob.addActionListener(e -> {
            JTextArea jobTitle = new JTextArea();
            JTextArea jobCompany = new JTextArea();
            JTextArea jobIndustry = new JTextArea();
            JTextArea jobType = new JTextArea();
            JTextArea jobGeo = new JTextArea();
            JTextArea jobLevel = new JTextArea();
            JTextArea jobSalaryRange = new JTextArea();
            JTextArea jobCurrency = new JTextArea();
            JTextArea jobPubDate = new JTextArea();
            JSlider jobRating = new JSlider(0,10, 5);
            jobRating.setMajorTickSpacing(5);
            jobRating.setMinorTickSpacing(1);
            jobRating.setPaintTicks(true);
            jobRating.setPaintLabels(true);
            JButton saveJob = new JButton("Save Job");
            int viewIdx = jobsTable.getSelectedRow();
            if (viewIdx >= 0) {
                int n =  jobsTable.convertRowIndexToModel(viewIdx);
                JobRecord activeJob = jobsList.get(n);
                jobTitle.setText(activeJob.jobTitle());
                jobCompany.setText(activeJob.companyName());
                jobIndustry.setText(activeJob.jobIndustry().toString());
                jobType.setText(activeJob.jobType().toString());
                jobGeo.setText(activeJob.jobGeo());
                jobLevel.setText(activeJob.jobLevel());
                jobSalaryRange.setText(String.valueOf(activeJob.annualSalaryMin()));
                jobCurrency.setText(activeJob.salaryCurrency());
                jobPubDate.setText(activeJob.pubDate());
                Object[] obj = {"Job Title: ", jobTitle,"Company: ", jobCompany,"Industry: ", jobIndustry,"Type: ", jobType,
                "Location: ", jobGeo, "Level: ", jobLevel, "Salary: ", jobSalaryRange, "Currency: ", jobCurrency, "Published: ", jobPubDate,
                "Rate this Job: ", jobRating, "Save this Job?"};
                int result = JOptionPane.showConfirmDialog(jobsTable, obj, "Job Details: " + activeJob.id(), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        mainPanel.add(openJob);
        add(mainPanel);
    }


    public void setRecordText(String text) {
        this.recordText.setText(text);
    }


    public void addFeatures(FindJobController controller) {
        searchButton.addActionListener(e -> controller.dummyFindJobMethod());
    }

    public static void main(String[] args) {
        System.out.println("hello");
    }
    
}
