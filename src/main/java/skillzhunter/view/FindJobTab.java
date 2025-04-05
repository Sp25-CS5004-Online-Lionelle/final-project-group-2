package skillzhunter.view;


import static skillzhunter.view.JobsLoader.getData;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import kotlin.Suppress;
import skillzhunter.model.JobRecord;
import skillzhunter.controller.IController;
import static skillzhunter.view.JobsLoader.getColumnNames;

public class FindJobTab extends JobView {
    private IController controller;

    public FindJobTab(IController controller){
        super();
        super.initView();
        this.controller = controller;
        setJobsList(JobRecordGenerator.generateDummyRecords(10));
        
    }


    @Override
    public JPanel makeTopButtonPanel() {
        JPanel searchRow = new JPanel();
        searchRow.setLayout(new BoxLayout(searchRow, BoxLayout.LINE_AXIS));
        TextField searchField = new TextField("", 20);    
        searchButton = new JButton("Find Jobs");
        searchRow.add(searchField);
        searchRow.add(searchButton);
        searchButton.addActionListener(e -> {
            List<JobRecord> jobs = controller.getApiCall(searchField.getText(), 10, "any","any");
            setJobsList(jobs);
            //setJobsList(JobRecordGenerator.generateDummyRecords(10));

            // this.jobsTable = new JobsTable(getColumnNames(), getData(jobsList));

        }
            );
        return searchRow;

    }

    @Override
    public JPanel makeBottomButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        openJob = new JButton("Open Job!!");
        openJob.addActionListener(e -> openSelectedJob());
        buttonPanel.add(openJob);
        return buttonPanel;
    }


    @Override
    public void applyTheme(ColorTheme theme) {
        super.applyTheme(theme);
        System.out.println("Applying theme: " + theme);
        setBackground(theme.background);
        openJob.setBackground(theme.buttonNormal);
        openJob.setForeground(theme.buttonForeground);
        // searchButton.setBackground(theme.buttonNormal);
        // searchButton.setForeground(theme.buttonForeground);
        jobsTable.setBackground(theme.fieldBackground);
        jobsTable.setForeground(theme.fieldForeground);
        jobsTable.setBorder(BorderFactory.createLineBorder(theme.buttonNormal));
        jobsTable.getTableHeader().setBackground(theme.fieldBackground);
        jobsTable.getTableHeader().setForeground(theme.foreground);
        repaint();
    }

    private void openSelectedJob() {
        boolean jobPresent = false;
        String dialogMsg = "Save this Job?";
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx >= 0) {
            int n = jobsTable.convertRowIndexToModel(viewIdx);
            JobRecord activeJob = jobsList.get(n);

            //See if we already have this one
            if (SavedJobsLists.getSavedJobs().contains(activeJob)) {
                jobPresent = true;
                dialogMsg = "Remove this Job?";
            }
    
            JTextArea jobTitle = new JTextArea(activeJob.jobTitle());
            JTextArea jobCompany = new JTextArea(activeJob.companyName());
            
            // Join the industry array elements into a string without brackets
            String jobIndustryText = String.join(", ", activeJob.jobIndustry());
            JTextArea jobIndustry = new JTextArea(jobIndustryText);
    
            // Join the type array elements into a string without brackets
            String jobTypeText = String.join(", ", activeJob.jobType());
            JTextArea jobType = new JTextArea(jobTypeText);
    
            JTextArea jobGeo = new JTextArea(activeJob.jobGeo());
            JTextArea jobLevel = new JTextArea(activeJob.jobLevel());
            JTextArea jobSalaryRange = new JTextArea(String.valueOf(activeJob.annualSalaryMin()));
            JTextArea jobCurrency = new JTextArea(activeJob.salaryCurrency());
            JTextArea jobPubDate = new JTextArea(activeJob.pubDate());
    
            JSlider jobRating = new JSlider(0, 5, 2);
            jobRating.setMajorTickSpacing(5);
            jobRating.setMinorTickSpacing(1);
            jobRating.setPaintTicks(true);
            jobRating.setPaintLabels(true);
    
            JTextArea comments = new JTextArea(5, 20);
            comments.setLineWrap(true);
            comments.setWrapStyleWord(true);
            comments.setBorder(BorderFactory.createTitledBorder("Your Comments"));

            //TODO: make the text & function update if the record is already in the saved list (ADD v REMOVE)
    
            Object[] obj = {"Job Title: ", jobTitle, "Company: ", jobCompany, "Industry: ", jobIndustry,
                            "Type: ", jobType, "Location: ", jobGeo, "Level: ", jobLevel, "Salary: ", jobSalaryRange,
                            "Currency: ", jobCurrency, "Published: ", jobPubDate, "Rate this Job: ", jobRating, 
                            "Comments:", comments, dialogMsg};
    
            int result = JOptionPane.showConfirmDialog(jobsTable, obj, "Job Details: ", JOptionPane.INFORMATION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                if (jobPresent) {
                    SavedJobsLists.removeSavedJob(activeJob);
                } else {
                    SavedJobsLists.addSavedJob(activeJob);
                }
            }
        }
    }


    public static void main(String[] args) {
        System.out.println("hello");
    }

}