package skillzhunter.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;

public class FindJobTab extends JobView {
    private IController controller;
    private String[] locations;
    private String[] industries;

    public FindJobTab(IController controller){
        super();
        
        this.controller = controller;
        this.locations = controller.getLocations().toArray(new String[0]);
        this.industries = controller.getIndustries().toArray(new String[0]);

        super.initView();

        setJobsList(controller.getApiCall("any", 10 , "any", "any"));
    }


    @Override
    public JPanel makeTopButtonPanel() {

        //make the panel & set layout
        JPanel searchRow = new JPanel();
        searchRow.setLayout(new BoxLayout(searchRow, BoxLayout.LINE_AXIS));

        //create fields, buttons, and combos
        TextField searchField = new TextField("", 20);    
        searchButton = new JButton("Find Jobs");
        String[] comboOptions = {"any", "option1", "option2"};//need to make this specific to field
        Integer[] results = {5,10,20,50};
        JComboBox<String> industryCombo = new JComboBox<>(industries);
        industryCombo.setEditable(true);
        JComboBox<String> locationCombo = new JComboBox<>(locations);
        locationCombo.setEditable(true);
        JComboBox<Integer> resultsCombo = new JComboBox<>(results);
        resultsCombo.setPrototypeDisplayValue(100);
        resultsCombo.setEditable(true);

        //add fields, buttons, labels, combos, and spaces
        searchRow.add(new JLabel("Industry: "));
        searchRow.add(industryCombo);
        searchRow.add(Box.createRigidArea(new Dimension(5, 0))); // these are invisible objects that create spacing
        searchRow.add(new JLabel("Location: "));
        searchRow.add(locationCombo);
        searchRow.add(Box.createRigidArea(new Dimension(5, 0)));
        searchRow.add(new JLabel("# of Results : "));
        searchRow.add(resultsCombo);
        searchRow.add(Box.createRigidArea(new Dimension(5, 0)));
        searchRow.add(new JLabel("Job Title : "));
        searchRow.add(searchField);
        searchRow.add(searchButton);


        //set listeners
        searchButton.addActionListener(e -> {
            List<JobRecord> jobs = controller.getApiCall(searchField.getText(), (Integer) resultsCombo.getSelectedItem() , locationCombo.getSelectedItem().toString(),
                industryCombo.getSelectedItem().toString());
            setJobsList(jobs);
        }
            );

        //return the panel
        return searchRow;

    }

    @Override
    public JPanel makeBottomButtonPanel() {

        //make the panel & set layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        //make buttons
        openJob = new JButton("Open Job");

        //add fields, buttons, labels, combos, and spaces
        buttonPanel.add(openJob);

        //set listeners
        openJob.addActionListener(e -> openSelectedJob());

        //return the panel
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