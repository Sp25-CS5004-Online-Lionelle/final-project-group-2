package skillzhunter.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;

/**
 * This class represents the Find Job tab in the application.
 * It allows users to search for jobs and view the results in a table.
 */
public class FindJobTab extends JobView {
    /** Controller. */
    private final IController controller;
    /** location list. */
    private final String[] locations;
    /** industry list. */
    private final String[] industries;
    /** search results list. */
    private List<JobRecord> searchResults = new ArrayList<>();
    
    // UI components
    /** Industry label. */
    private JLabel industryLabel;
    /** Location label. */
    private JLabel locationLabel;
    /** Search button label. */
    private JLabel resultsLabel;
    /** title label. */
    private JLabel titleLabel;
    /** Search button. */
    private final ImageIcon openIcon;
    /** save icon. */
    private final ImageIcon saveIcon;
    /** info icon. */
    private final ImageIcon infoIcon;
    /** warning icon. */
    private final ImageIcon warningIcon;
    /** Salary range viz. */
    private SalaryVisualizationPanel salaryVisualizationPanel;
    /** show vis button. */
    private JCheckBox showVisualizationCheckbox;
    /** table panel. */
    private JPanel tablePanel;
    /** theme button.*/
    private ThemedButton saveJob; // Now using ThemedButton instead of javax.swing.JButton

    /** 
     * Constructor for FindJobTab.
     * Initializes the controller and sets up the UI components.
     * @param controller The controller to interact with the model
     */
    public FindJobTab(IController controller) {
        super();
        
        this.controller = controller;
        this.locations = controller.getLocations().toArray(new String[0]);
        this.industries = controller.getIndustries().toArray(new String[0]);

        // Use IconLoader to load icons
        this.openIcon = IconLoader.loadIcon("images/open.png");
        this.saveIcon = IconLoader.loadIcon("images/saveIcon.png");
        this.infoIcon = IconLoader.loadIcon("images/info.png");
        this.warningIcon = IconLoader.loadIcon("images/warning.png");
        
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
     * Modifies the table panel to include the visualization panel.
     */
    private void modifyTablePanel() {
         // Find the table panel
         for (int i = 0; i < mainPanel.getComponentCount(); i++) {
            if (mainPanel.getComponent(i) instanceof JPanel
                && ((JPanel) mainPanel.getComponent(i)).getLayout() instanceof BorderLayout) {
                tablePanel = (JPanel) mainPanel.getComponent(i);
                break;
            }
        }
        
        if (tablePanel == null) {
            return;
        }        
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
        searchButton = createThemedButton("Find Jobs \t🔎", ThemedButton.ButtonType.PRIMARY);
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
            if (resultsObj instanceof Integer num) {
                numberOfResults = num;
            } else if (resultsObj instanceof String res) {
                try {
                    numberOfResults = Integer.parseInt(res);
                } catch (NumberFormatException ex) {
                    //add non-numeric dialogue
                    ImageIcon warningIcon = IconLoader.loadIcon("images/warning.png");
                    JOptionPane.showMessageDialog(this,
                        "Couldn't parse the number of results requested."
                            + "\nPlease ensure you enter a numeric value."
                            + "\nReturned 10 results.",
                        "Null or Non-Numeric Value",
                        JOptionPane.WARNING_MESSAGE,
                        warningIcon);
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // Create and configure Open button with PRIMARY type
        openJob = createThemedButton("Open", ThemedButton.ButtonType.PRIMARY);
        openJob.setIcon(openIcon);
        openJob.setHorizontalTextPosition(SwingConstants.LEFT);
        openJob.setIconTextGap(5);
        openJob.addActionListener(e -> openSelectedJob());

        // Create and configure Save button with SUCCESS type
        saveJob = createThemedButton("Save", ThemedButton.ButtonType.SUCCESS);
        saveJob.setIcon(saveIcon);
        saveJob.setHorizontalTextPosition(SwingConstants.LEFT);
        saveJob.setIconTextGap(5);
        saveJob.addActionListener(e -> saveSelectedJob());

        buttonPanel.add(openJob);
        buttonPanel.add(saveJob);
        return buttonPanel;
    }
    
    /**
     * Updates the visualization panel with job data if needed.
     * @param jobs The list of jobs to update the visualization with
     */
    private void updateVisualizationIfNeeded(List<JobRecord> jobs) {
        if (salaryVisualizationPanel != null && jobs != null && !jobs.isEmpty()) {
            salaryVisualizationPanel.updateJobs(jobs);
        }
    }
    
    /**
     * Applies theme to visualization components.
     * @param theme The color theme to apply
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
        
        // Apply theme to saveJob button (which might not be handled by parent)
        if (saveJob != null) {
            saveJob.applyTheme(theme);
        }
        
        // Use correct theme foreground color for labels
        java.awt.Color color = (theme == ColorTheme.DARK)
                ? ColorTheme.DARK.labelForeground : ColorTheme.LIGHT.labelForeground;
                
        industryLabel.setForeground(color);
        locationLabel.setForeground(color);
        resultsLabel.setForeground(color);
        titleLabel.setForeground(color);
    }

    /**
     * Opens the selected job in a new dialog
     * Displays job details in a new dialog when a job is selected from the table.
     * Uses JobDetailsDialogue directly for proper rating functionality.
     */
    private void openSelectedJob() {
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx >= 0) {
            int modelIdx = jobsTable.convertRowIndexToModel(viewIdx);
            JobRecord selectedJob = jobsList.get(modelIdx);
            // Use controller directly with JobDetailsDialogue
            JobDetailsDialogue.showJobDetails(jobsTable, selectedJob, 
                    controller.getSavedJobs(), controller);
        } else {
            JobActionHelper.showNoSelectionMessage("Please select a job to open", this);
        }
    }

    /**
     * Saves the selected job directly without opening the dialog
     * Adds the job to saved jobs and switches to the Saved Jobs tab.
     * Uses JobActionHelper for consistent save behavior.
     */
    private void saveSelectedJob() {
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx < 0) {
            // No job selected
            JobActionHelper.showNoSelectionMessage("Please select a job to save", this);
            return;
        }
        
        int modelIdx = jobsTable.convertRowIndexToModel(viewIdx);
        JobRecord selectedJob = jobsList.get(modelIdx);
        
        // Check if job is already saved using controller's method
        if (controller.isJobAlreadySaved(selectedJob)) {
            JOptionPane.showMessageDialog(this,
                    "This job is already saved.",
                    "Job Already Saved",
                    JOptionPane.INFORMATION_MESSAGE,
                    infoIcon);
            return;
        }
        
        // Add the job to saved jobs
        controller.job2SavedList(selectedJob);
        
        // Set default rating and comments
        controller.getUpdateJob(selectedJob.id(), "No comments provided", 0);
        
        // Show success message
        JOptionPane.showMessageDialog(this,
                "Job saved successfully!",
                "Job Saved",
                JOptionPane.INFORMATION_MESSAGE,
                IconLoader.loadIcon("images/success.png"));
        
        // Switch to Saved Jobs tab using JobActionHelper
        JobActionHelper.switchToSavedJobsTab(this, controller);
    }
    
    /**
     * Updates the job list and visualization if needed.
     * @param jobsList The list of jobs to update
     */
    @Override
    public void updateJobsList(List<JobRecord> jobsList) {
        if (searchResults != null && !searchResults.isEmpty()) {
            super.setJobsList(searchResults);
            updateVisualizationIfNeeded(searchResults);
        } else {
            super.updateJobsList(jobsList);
            updateVisualizationIfNeeded(jobsList);
        }
    }
    
    /**
     * Sets the job list and updates the visualization if needed.
     * @param jobsList The list of jobs to set
     */
    @Override
    public void setJobsList(List<JobRecord> jobsList) {
        super.setJobsList(jobsList);
        updateVisualizationIfNeeded(jobsList);
    }
}
