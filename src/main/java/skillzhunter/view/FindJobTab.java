package skillzhunter.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.Image;
import java.awt.BorderLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import java.util.List;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;

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

    // Open image for open job button
    private ImageIcon openIcon;
    
    // Salary visualization panel
    private SalaryVisualizationPanel salaryVisualizationPanel;
    private JCheckBox showVisualizationCheckbox;
    private JPanel tablePanel; // Store reference to the table panel

    public FindJobTab(IController controller) {
        super();
        
        this.controller = controller;
        this.locations = controller.getLocations().toArray(new String[0]);
        this.industries = controller.getIndustries().toArray(new String[0]);
        this.openIcon = loadIcon("images/open.png");
        
        super.initView();

        // Load initial set of jobs
        searchResults = controller.getApiCall("any", 10, "any", "any");
        setJobsList(searchResults);
        
        // Modify the table panel to include the visualization
        modifyTablePanel();
    }

    /**
     * Modifies the table panel to include the visualization panel below it
     */
    private void modifyTablePanel() {
        // Find the table panel
        for (int i = 0; i < mainPanel.getComponentCount(); i++) {
            if (mainPanel.getComponent(i) instanceof JPanel) {
                JPanel panel = (JPanel) mainPanel.getComponent(i);
                if (panel.getLayout() instanceof BorderLayout) {
                    // This is likely the table panel
                    tablePanel = panel;
                    break;
                }
            }
        }
        
        if (tablePanel != null) {
            // Get the existing scroll pane containing the table
            JPanel tableContainer = new JPanel(new BorderLayout());
            
            // Move the table's scroll pane to the new container
            tableContainer.add(tablePanel.getComponent(0), BorderLayout.CENTER);
            
            // Create visualization panel with a smaller preferred size
            salaryVisualizationPanel = new SalaryVisualizationPanel(searchResults);
            salaryVisualizationPanel.setPreferredSize(new Dimension(800, 200));
            JPanel visualizationContainer = new JPanel(new BorderLayout());
            visualizationContainer.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            visualizationContainer.add(salaryVisualizationPanel, BorderLayout.CENTER);
            
            // Add the visualization panel to the bottom of the container
            JPanel combinedPanel = new JPanel(new BorderLayout());
            combinedPanel.add(tableContainer, BorderLayout.CENTER);
            combinedPanel.add(visualizationContainer, BorderLayout.SOUTH);
            
            // Clear the original table panel and add the combined panel
            tablePanel.removeAll();
            tablePanel.add(combinedPanel, BorderLayout.CENTER);
            
            // Add checkbox to the top panel
            JPanel topPanel = (JPanel) mainPanel.getComponent(0);
            showVisualizationCheckbox = new JCheckBox("Show Salary Graph", true);
            showVisualizationCheckbox.addActionListener(e -> {
                visualizationContainer.setVisible(showVisualizationCheckbox.isSelected());
                tablePanel.revalidate();
                tablePanel.repaint();
            });
            
            topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            topPanel.add(showVisualizationCheckbox);
            
            // Update the visualization panel with job data
            if (searchResults != null && !searchResults.isEmpty()) {
                salaryVisualizationPanel.updateJobs(searchResults);
            }
            
            // Apply theme if available
            if (getTheme() != null) {
                salaryVisualizationPanel.applyTheme(getTheme());
                showVisualizationCheckbox.setBackground(getTheme().background);
                showVisualizationCheckbox.setForeground(getTheme().labelForeground);
            }
            
            // Revalidate and repaint
            tablePanel.revalidate();
            tablePanel.repaint();
        }
    }

    /**
     * Loads an icon from the resources folder.
     * 
     * @param path The path to the icon
     * @return The loaded icon, or null if it couldn't be loaded
     */
    private ImageIcon loadIcon(String path) {
        try {
            URL url = getClass().getClassLoader().getResource(path);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                // Scale the image to an appropriate size for the header
                Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + path);
        }
        
        return null;
    }

    @Override
    public JPanel makeTopButtonPanel() {
        // Make the panel & set layout
        JPanel searchRow = new JPanel();
        searchRow.setLayout(new BoxLayout(searchRow, BoxLayout.LINE_AXIS));

        // Create fields, buttons, and combos
        TextField searchField = new TextField("", 20);    
        searchButton = createThemedButton("Find Jobs \t🔎");
        
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
            
            // Update the visualization panel with the new search results
            if (salaryVisualizationPanel != null) {
                salaryVisualizationPanel.updateJobs(searchResults);
            }
        });

        return searchRow;
    }

    @Override
    public JPanel makeBottomButtonPanel() {
        // Make the panel & set layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Create button using the helper method
        openJob = createThemedButton("Open Job");
        openJob.setIcon(openIcon);
        openJob.setHorizontalTextPosition(SwingConstants.LEFT);
        openJob.setIconTextGap(5); // optional: tweak spacing between text and icon

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
        
        // Apply theme to the salary visualization panel
        if (salaryVisualizationPanel != null) {
            salaryVisualizationPanel.applyTheme(theme);
        }
        
        // Apply theme to the checkbox
        if (showVisualizationCheckbox != null) {
            showVisualizationCheckbox.setBackground(theme.background);
            showVisualizationCheckbox.setForeground(theme.labelForeground);
        }
        
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
            
            // Update visualization with search results
            if (salaryVisualizationPanel != null) {
                salaryVisualizationPanel.updateJobs(searchResults);
            }
        } else {
            super.updateJobsList(jobsList);
            
            // Update visualization with jobsList
            if (salaryVisualizationPanel != null) {
                salaryVisualizationPanel.updateJobs(jobsList);
            }
        }
    }
    
    @Override
    public void setJobsList(List<JobRecord> jobsList) {
        super.setJobsList(jobsList);
        
        // Update the visualization panel
        if (salaryVisualizationPanel != null) {
            salaryVisualizationPanel.updateJobs(jobsList);
        }
    }
}