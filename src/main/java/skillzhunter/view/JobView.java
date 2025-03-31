package skillzhunter.view;

import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;

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
import skillzhunter.controller.IJobController;
import skillzhunter.model.JobRecord;
import javax.swing.*;
import java.awt.*;

public class JobView extends JPanel {
    protected JButton searchButton;
    protected JTextArea recordText;
    protected JobsTable jobsTable;
    protected List<JobRecord> jobsList = JobRecordGenerator.generateDummyRecords(10);

    public JobView() {
        setSize(1000, 1000);
        JPanel mainPanel = new JPanel();
        BoxLayout layout = new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS);
        mainPanel.setLayout(layout);

        searchButton = new JButton("Find Jobs");
        mainPanel.add(searchButton);

        recordText = new JTextArea("click to find jobs");
        mainPanel.add(recordText);

        JPanel tablePanel = new JPanel(new FlowLayout());
        tablePanel.setSize(600, 600);

        jobsTable = new JobsTable(getColumnNames(), getData(jobsList));

        JScrollPane tablePane = new JScrollPane(jobsTable);
        tablePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tablePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tablePanel.add(tablePane);
        mainPanel.add(tablePanel);

        // Create a horizontal panel for Open Job and Exit buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton openJob = new JButton("Open Selected Job");
        openJob.addActionListener(e -> {
            int viewIdx = jobsTable.getSelectedRow();
            if (viewIdx >= 0) {
                int n = jobsTable.convertRowIndexToModel(viewIdx);
                JobRecord activeJob = jobsList.get(n);
                
                JTextArea jobTitle = new JTextArea(activeJob.jobTitle());
                JTextArea jobCompany = new JTextArea(activeJob.companyName());
                JTextArea jobIndustry = new JTextArea(activeJob.jobIndustry().toString());
                JTextArea jobType = new JTextArea(activeJob.jobType().toString());
                JTextArea jobGeo = new JTextArea(activeJob.jobGeo());
                JTextArea jobLevel = new JTextArea(activeJob.jobLevel());
                JTextArea jobSalaryRange = new JTextArea(String.valueOf(activeJob.annualSalaryMin()));
                JTextArea jobCurrency = new JTextArea(activeJob.salaryCurrency());
                JTextArea jobPubDate = new JTextArea(activeJob.pubDate());
                
                JSlider jobRating = new JSlider(0, 10, 5);
                jobRating.setMajorTickSpacing(5);
                jobRating.setMinorTickSpacing(1);
                jobRating.setPaintTicks(true);
                jobRating.setPaintLabels(true);
                
                Object[] obj = {"Job Title: ", jobTitle, "Company: ", jobCompany, "Industry: ", jobIndustry,
                                "Type: ", jobType, "Location: ", jobGeo, "Level: ", jobLevel, "Salary: ", jobSalaryRange,
                                "Currency: ", jobCurrency, "Published: ", jobPubDate, "Rate this Job: ", jobRating, "Save this Job?"};
                
                JOptionPane.showConfirmDialog(jobsTable, obj, "Job Details: " + activeJob.id(), JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton exit = new JButton("Exit");
        exit.addActionListener(exitEvent -> {
            int result = JOptionPane.showConfirmDialog(mainPanel, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Add buttons to button panel
        buttonPanel.add(openJob);
        buttonPanel.add(exit);
        mainPanel.add(buttonPanel);

        // Set cursor for all buttons
        JButton[] buttons = {searchButton, openJob, exit};
        for (JButton button : buttons) {
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        add(mainPanel);
    }

    public void setRecordText(String text) {
        this.recordText.setText(text);
    }


    public void addFeatures(IJobController controller) {
        searchButton.addActionListener(e -> controller.setViewData());
    }

    public static void main(String[] args) {
        System.out.println("hello");
    }
}


