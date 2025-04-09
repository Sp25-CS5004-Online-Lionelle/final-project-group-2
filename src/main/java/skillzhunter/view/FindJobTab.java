package skillzhunter.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.Component;

import java.util.List;
import java.util.ArrayList;

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
    private List<JobRecord> searchResults = new ArrayList<>();

    public FindJobTab(IController controller){
        super();
        
        this.controller = controller;
        this.locations = controller.getLocations().toArray(new String[0]);
        this.industries = controller.getIndustries().toArray(new String[0]);

        super.initView();

        // Load initial set of jobs
        searchResults = controller.getApiCall("any", 10 , "any", "any");
        setJobsList(searchResults);
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
            searchResults = controller.getApiCall(searchField.getText(), (Integer) resultsCombo.getSelectedItem() , locationCombo.getSelectedItem().toString(),
                industryCombo.getSelectedItem().toString());
            setJobsList(searchResults);
        });

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
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx >= 0) {
            int modelIdx = jobsTable.convertRowIndexToModel(viewIdx);
            JobRecord selectedJob = jobsList.get(modelIdx);
            JobDetailsDialogue.showJobDetails(jobsTable, selectedJob, SavedJobsLists.getSavedJobs(), controller);
        }
    }
    
    /**
     * Override the updateJobsList method to maintain our search results
     * but still observe saved jobs changes for any UI updates needed
     */
    @Override
    public void updateJobsList(List<JobRecord> jobsList) {
        // Don't replace our search results with saved jobs
        // Instead, keep our search results but update the UI if needed
        // This ensures the Find Jobs tab keeps showing search results
        if (searchResults != null && !searchResults.isEmpty()) {
            super.setJobsList(searchResults);
        } else {
            super.updateJobsList(jobsList);
        }
    }

    public static void main(String[] args) {
        System.out.println("hello");
    }
}