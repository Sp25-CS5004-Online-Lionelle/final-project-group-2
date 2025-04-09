package skillzhunter.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;

import java.util.List;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;

public class FindJobTab extends JobView {
    private IController controller;
    private String[] locations;
    private String[] industries;
    private List<JobRecord> searchResults = new ArrayList<>();
    
    // Store labels as instance variables
    private JLabel industryLabel;
    private JLabel locationLabel;
    private JLabel resultsLabel;
    private JLabel titleLabel;

    public FindJobTab(IController controller) {
        super();
        
        this.controller = controller;
        this.locations = controller.getLocations().toArray(new String[0]);
        this.industries = controller.getIndustries().toArray(new String[0]);

        super.initView();

        // Load initial set of jobs
        searchResults = controller.getApiCall("any", 10, "any", "any");
        setJobsList(searchResults);
    }

    @Override
    public JPanel makeTopButtonPanel() {
        // Make the panel & set layout
        JPanel searchRow = new JPanel();
        searchRow.setLayout(new BoxLayout(searchRow, BoxLayout.LINE_AXIS));

        // Create fields, buttons, and combos
        TextField searchField = new TextField("", 20);    
        searchButton = createThemedButton("Find Jobs");
        
        String[] comboOptions = {"any", "option1", "option2"}; // Need to make this specific to field
        Integer[] results = {5, 10, 20, 50};
        JComboBox<String> industryCombo = new JComboBox<>(industries);
        industryCombo.setEditable(true);
        JComboBox<String> locationCombo = new JComboBox<>(locations);
        locationCombo.setEditable(true);
        JComboBox<Integer> resultsCombo = new JComboBox<>(results);
        resultsCombo.setPrototypeDisplayValue(100);
        resultsCombo.setEditable(true);

        // Create standard JLabels
        industryLabel = new JLabel("Industry: ");
        locationLabel = new JLabel("Location: ");
        resultsLabel = new JLabel("# of Results: ");
        titleLabel = new JLabel("Job Title: ");

        // Add fields, buttons, labels, combos, and spaces
        searchRow.add(industryLabel);
        searchRow.add(industryCombo);
        searchRow.add(Box.createRigidArea(new Dimension(5, 0))); // These are invisible objects that create spacing
        searchRow.add(locationLabel);
        searchRow.add(locationCombo);
        searchRow.add(Box.createRigidArea(new Dimension(5, 0)));
        searchRow.add(resultsLabel);
        searchRow.add(resultsCombo);
        searchRow.add(Box.createRigidArea(new Dimension(5, 0)));
        searchRow.add(titleLabel);
        searchRow.add(searchField);
        searchRow.add(searchButton);

        // Set listeners
        searchButton.addActionListener(e -> {
            searchResults = controller.getApiCall(searchField.getText(), (Integer) resultsCombo.getSelectedItem(),
                    locationCombo.getSelectedItem().toString(), industryCombo.getSelectedItem().toString());
            setJobsList(searchResults);
        });

        return searchRow;
    }

    @Override
    public JPanel makeBottomButtonPanel() {
        // Make the panel & set layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Create button using the helper method
        openJob = createThemedButton("Open Job");

        // Add button to panel
        buttonPanel.add(openJob);

        // Set listeners
        openJob.addActionListener(e -> openSelectedJob());

        return buttonPanel;
    }

    @Override
    public void applyTheme(ColorTheme theme) {
        // Call the parent implementation for common styling
        super.applyTheme(theme);
        
        // Use direct class constants instead of the passed theme
        if (theme == ColorTheme.DARK) {
            // Dark mode - use DARK theme's labelForeground
            industryLabel.setForeground(ColorTheme.DARK.labelForeground);
            locationLabel.setForeground(ColorTheme.DARK.labelForeground);
            resultsLabel.setForeground(ColorTheme.DARK.labelForeground);
            titleLabel.setForeground(ColorTheme.DARK.labelForeground);
        } else {
            // Light mode - use LIGHT theme's labelForeground
            industryLabel.setForeground(ColorTheme.LIGHT.labelForeground);
            locationLabel.setForeground(ColorTheme.LIGHT.labelForeground);
            resultsLabel.setForeground(ColorTheme.LIGHT.labelForeground);
            titleLabel.setForeground(ColorTheme.LIGHT.labelForeground);
        }
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
}