package skillzhunter.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import java.util.List;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;

/**
 * This class represents the Find Job tab in the application.
 * It allows users to search for jobs and view the results in a table.
 */
public class FindJobTab extends JobView {
    private IController controller;
    private String[] locations;
    private String[] industries;
    private List<JobRecord> searchResults = new ArrayList<>();
    
    // UI components
    private JLabel industryLabel, locationLabel, resultsLabel, titleLabel;
    private ImageIcon openIcon;
    private SalaryVisualizationPanel salaryVisualizationPanel;
    private JCheckBox showVisualizationCheckbox;
    private JPanel tablePanel;

    public FindJobTab(IController controller) {
        super();
        
        this.controller = controller;
        this.locations = controller.getLocations().toArray(new String[0]);
        this.industries = controller.getIndustries().toArray(new String[0]);

        // Use IconLoader to load icons
        this.openIcon = IconLoader.loadIcon("images/open.png");
        
        super.initView();

        // Load initial set of jobs
        searchResults = controller.getApiCall("any", 10, "any", "any");
        setJobsList(searchResults);
        
        modifyTablePanel();
        setupEnterKeyAction();
    }

    //These next two methods are all about setting up the enter key to search
    private void setupEnterKeyAction() {
        Action enterAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchButton != null && searchButton.isEnabled()) {
                    searchButton.doClick();
                }
            }
        };
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "performSearch");
        getActionMap().put("performSearch", enterAction);
        disableEnterKeyTraversalIn(this);
    }

    //Helper method to disable Enter key default behavior in text components
    private void disableEnterKeyTraversalIn(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextField || comp instanceof TextField) {
                comp.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            searchButton.doClick();
                            e.consume(); // Prevent default handling
                        }
                    }
                });
            }
            // Recursively process nested containers
            if (comp instanceof Container) {
                disableEnterKeyTraversalIn((Container) comp);
            }
        }
    }

    /**
     * Modifies the table panel to include the visualization panel
     */
    private void modifyTablePanel() {
        // Find the table panel
        for (int i = 0; i < mainPanel.getComponentCount(); i++) {
            if (mainPanel.getComponent(i) instanceof JPanel && 
                ((JPanel)mainPanel.getComponent(i)).getLayout() instanceof BorderLayout) {
                tablePanel = (JPanel)mainPanel.getComponent(i);
                break;
            }
        }
        
        if (tablePanel == null) return;
        
        // Create and set up visualization
        salaryVisualizationPanel = new SalaryVisualizationPanel(searchResults);
        salaryVisualizationPanel.setPreferredSize(new Dimension(800, 200));
        
        // Set up panel containers
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.add(tablePanel.getComponent(0), BorderLayout.CENTER);
        
        JPanel visualizationContainer = new JPanel(new BorderLayout());
        visualizationContainer.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        visualizationContainer.add(salaryVisualizationPanel, BorderLayout.CENTER);
        
        JPanel combinedPanel = new JPanel(new BorderLayout());
        combinedPanel.add(tableContainer, BorderLayout.CENTER);
        combinedPanel.add(visualizationContainer, BorderLayout.SOUTH);
        
        // Replace original panel content
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
        
        // Initial update
        updateVisualizationIfNeeded(searchResults);
        
        // Apply theme if available
        if (getTheme() != null) {
            applyThemeToVisualization(getTheme());
        }
        
        // Revalidate and repaint
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    @Override
    public JPanel makeTopButtonPanel() {
        JPanel searchRow = new JPanel();
        searchRow.setLayout(new BoxLayout(searchRow, BoxLayout.LINE_AXIS));

        // Create UI components
        TextField searchField = new TextField("", 20);    
        searchButton = createThemedButton("Find Jobs \t🔎");
        Integer[] results = {5, 10, 20, 50};
        JComboBox<String> industryCombo = new JComboBox<>(industries);
        industryCombo.setEditable(true);
        JComboBox<String> locationCombo = new JComboBox<>(locations);
        locationCombo.setEditable(true);
        JComboBox<Integer> resultsCombo = new JComboBox<>(results);
        resultsCombo.setPrototypeDisplayValue(100);
        resultsCombo.setEditable(true);

        // Create labels
        industryLabel = new JLabel("Industry: ");
        locationLabel = new JLabel("Location: ");
        resultsLabel = new JLabel("# of Results: ");
        titleLabel = new JLabel("Job Title: ");

        // Add components
        searchRow.add(industryLabel);
        searchRow.add(industryCombo);
        searchRow.add(Box.createRigidArea(new Dimension(5, 0)));
        searchRow.add(locationLabel);
        searchRow.add(locationCombo);
        searchRow.add(Box.createRigidArea(new Dimension(5, 0)));
        searchRow.add(resultsLabel);
        searchRow.add(resultsCombo);
        searchRow.add(Box.createRigidArea(new Dimension(5, 0)));
        searchRow.add(titleLabel);
        searchRow.add(searchField);
        searchRow.add(searchButton);

        // Set search action
        searchButton.addActionListener(e -> {
            Object locationObj = locationCombo.getSelectedItem();
            Object industryObj = industryCombo.getSelectedItem();
            Object resultsObj = resultsCombo.getSelectedItem();
            int numberOfResults = 10;

            // If null, set to default values
            String location = (locationObj != null) ? locationObj.toString() : "any";
            String industry = (industryObj != null) ? industryObj.toString() : "any";

            // If resultsObj is null, set to default
            if (resultsObj instanceof Integer) {
                numberOfResults = (Integer) resultsObj;
            } else if (resultsObj instanceof String) {
                try {
                    numberOfResults = Integer.parseInt((String) resultsObj);
                } catch (NumberFormatException ex) {
                    numberOfResults = 10;
                }
            }
            
            // Perform search
            searchResults = controller.getApiCall(searchField.getText(), 
                    numberOfResults, location, industry);

            if (searchResults != null && !searchResults.isEmpty()) {
                setJobsList(searchResults);
                updateVisualizationIfNeeded(searchResults);
            } else {
                // Check if the number of results requested is too large
                if (numberOfResults > 50) {
                    ImageIcon warningIcon = IconLoader.loadIcon("images/warning.png");
                    JOptionPane.showMessageDialog(this,
                            "The number of results requested is too large.\nPlease try a smaller number.",
                            "Too Many Results Requested",
                            JOptionPane.WARNING_MESSAGE,
                            warningIcon);
                } else {
                    // Show custom popup if no results found for the query
                    ImageIcon errorIcon = IconLoader.loadIcon("images/warning.png");
                    JOptionPane.showMessageDialog(this,
                            "No jobs found for the given query.\nTry different keywords or filters.",
                            "No Results Found",
                            JOptionPane.INFORMATION_MESSAGE,
                            errorIcon);
                }
            }
        });

        return searchRow;
    }

    @Override
    public JPanel makeBottomButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Create and configure button
        openJob = createThemedButton("Open");
        openJob.setIcon(openIcon);
        openJob.setHorizontalTextPosition(SwingConstants.LEFT);
        openJob.setIconTextGap(5);
        openJob.addActionListener(e -> openSelectedJob());

        buttonPanel.add(openJob);
        return buttonPanel;
    }
    
    /**
     * Updates the visualization panel with job data if needed
     */
    private void updateVisualizationIfNeeded(List<JobRecord> jobs) {
        if (salaryVisualizationPanel != null && jobs != null && !jobs.isEmpty()) {
            salaryVisualizationPanel.updateJobs(jobs);
        }
    }
    
    /**
     * Applies theme to visualization components
     */
    private void applyThemeToVisualization(ColorTheme theme) {
        if (salaryVisualizationPanel != null) {
            salaryVisualizationPanel.applyTheme(theme);
        }
        
        if (showVisualizationCheckbox != null) {
            showVisualizationCheckbox.setBackground(theme.background);
            showVisualizationCheckbox.setForeground(theme.labelForeground);
        }
    }

    @Override
    public void applyTheme(ColorTheme theme) {
        // Call parent implementation for common styling
        super.applyTheme(theme);
        
        // Apply theme to visualization components
        applyThemeToVisualization(theme);
        
        // Use correct theme foreground color for labels
        java.awt.Color color = (theme == ColorTheme.DARK) ? 
                ColorTheme.DARK.labelForeground : ColorTheme.LIGHT.labelForeground;
                
        industryLabel.setForeground(color);
        locationLabel.setForeground(color);
        resultsLabel.setForeground(color);
        titleLabel.setForeground(color);
    }

    // In the openSelectedJob method
    /**
     * Opens the selected job in a new dialog
     * Displays job details in a new dialog when a job is selected from the table.
     */
    private void openSelectedJob() {
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx >= 0) {
            int modelIdx = jobsTable.convertRowIndexToModel(viewIdx);
            JobRecord selectedJob = jobsList.get(modelIdx);
            // Use controller directly instead of SavedJobsLists
            JobDetailsDialogue.showJobDetails(jobsTable, selectedJob, 
                    controller.getSavedJobs(), controller);
        }
    }

    // In updateJobsList and setJobsList methods, maintain searchResults if needed
    @Override
    /**
     * Updates the job list and visualization if needed
     */
    public void updateJobsList(List<JobRecord> jobsList) {
        if (searchResults != null && !searchResults.isEmpty()) {
            super.setJobsList(searchResults);
            updateVisualizationIfNeeded(searchResults);
        } else {
            super.updateJobsList(jobsList);
            updateVisualizationIfNeeded(jobsList);
        }
    }
    
    @Override
    public void setJobsList(List<JobRecord> jobsList) {
        super.setJobsList(jobsList);
        updateVisualizationIfNeeded(jobsList);
    }
}