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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    /** Map for UI components. */
    private Map<String, Component> components = new HashMap<>();  
    /** Map for icons. */
    private Map<String, ImageIcon> icons = new HashMap<>();
    
    /** Salary range visualization panel. */
    private SalaryVisualizationPanel salaryVisualizationPanel;
    /** Show visualization checkbox. */
    private JCheckBox showVisualizationCheckbox;
    /** Table panel. */
    private JPanel tablePanel;

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

        // Load icons into map with simple loop
        String[] iconNames = {"open", "saveIcon", "warning", "lightbulb", "success"};
        for (String name : iconNames) {
            icons.put(name, IconLoader.loadIcon("images/" + name + ".png"));
        }
        
        super.initView();
        searchResults = controller.getApiCall("any", 10, "any", "any");
        setJobsList(searchResults);
        modifyTablePanel();
        setupEnterKeyAction();
    }

    /**
     * Sets up the enter key action to trigger search.
     * This allows users to press Enter to search instead of clicking the button.
     */
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

    /**
     * Helper method to disable Enter key default behavior in text components.
     * This prevents the Enter key from being handled by the default focus traversal
     * mechanism and instead triggers the search action.
     * 
     * @param container The container to process
     */
    private void disableEnterKeyTraversalIn(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextField || comp instanceof TextField) {
                comp.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            searchButton.doClick();
                            e.consume();
                        }
                    }
                });
            }
            if (comp instanceof Container) {
                disableEnterKeyTraversalIn((Container) comp);
            }
        }
    }

    /**
     * Modifies the table panel to include the visualization panel.
     * This adds a salary visualization below the jobs table.
     */
    private void modifyTablePanel() {
        for (int i = 0; i < mainPanel.getComponentCount(); i++) {
            if (mainPanel.getComponent(i) instanceof JPanel
                && ((JPanel) mainPanel.getComponent(i)).getLayout() instanceof BorderLayout) {
                tablePanel = (JPanel) mainPanel.getComponent(i);
                break;
            }
        }
        
        if (tablePanel == null) return;
            
        salaryVisualizationPanel = new SalaryVisualizationPanel(searchResults);
        salaryVisualizationPanel.setPreferredSize(new Dimension(800, 200));
        
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.add(tablePanel.getComponent(0), BorderLayout.CENTER);
        
        JPanel visualizationContainer = new JPanel(new BorderLayout());
        visualizationContainer.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        visualizationContainer.add(salaryVisualizationPanel, BorderLayout.CENTER);
        
        JPanel combinedPanel = new JPanel(new BorderLayout());
        combinedPanel.add(tableContainer, BorderLayout.CENTER);
        combinedPanel.add(visualizationContainer, BorderLayout.SOUTH);
        
        tablePanel.removeAll();
        tablePanel.add(combinedPanel, BorderLayout.CENTER);
        
        JPanel topPanel = (JPanel) mainPanel.getComponent(0);
        showVisualizationCheckbox = new JCheckBox("Show Salary Graph", true);
        showVisualizationCheckbox.addActionListener(e -> {
            visualizationContainer.setVisible(showVisualizationCheckbox.isSelected());
            tablePanel.revalidate();
            tablePanel.repaint();
        });
        
        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        topPanel.add(showVisualizationCheckbox);
        
        updateVisualizationIfNeeded(searchResults);
        
        if (getTheme() != null) {
            applyThemeToVisualization(getTheme());
        }
        
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    /**
     * Creates the top button panel that contains search controls.
     * Includes industry, location, and results filters, as well as
     * a search field and search button.
     *
     * @return The panel containing search controls
     */
    @Override
    public JPanel makeTopButtonPanel() {
        JPanel searchRow = new JPanel();
        searchRow.setLayout(new BoxLayout(searchRow, BoxLayout.LINE_AXIS));

        // Create components with simple arrays
        String[] labels = {"Industry: ", "Location: ", "# of Results: ", "Job Title: "};
        String[] keys = {"industryLabel", "locationLabel", "resultsLabel", "titleLabel"};
        
        // Create search field and button
        TextField searchField = new TextField("", 20);
        components.put("searchField", searchField);
        searchButton = createThemedButton("Find Jobs \t🔎", ThemedButton.ButtonType.PRIMARY);
        
        // Create combo boxes
        JComboBox<String> industryCombo = new JComboBox<>(industries);
        industryCombo.setEditable(true);
        components.put("industryCombo", industryCombo);
        
        JComboBox<String> locationCombo = new JComboBox<>(locations);
        locationCombo.setEditable(true);
        components.put("locationCombo", locationCombo);
        
        Integer[] results = {5, 10, 20, 50};
        JComboBox<Integer> resultsCombo = new JComboBox<>(results);
        resultsCombo.setPrototypeDisplayValue(Integer.valueOf(100));
        resultsCombo.setEditable(true);
        components.put("resultsCombo", resultsCombo);
        
        // Create and store labels
        for (int i = 0; i < labels.length; i++) {
            components.put(keys[i], new JLabel(labels[i]));
        }

        // Add components in order with spacers
        Component[][] layout = {
            {components.get("industryLabel"), industryCombo},
            {null, Box.createRigidArea(new Dimension(5, 0))},
            {components.get("locationLabel"), locationCombo},
            {null, Box.createRigidArea(new Dimension(5, 0))},
            {components.get("resultsLabel"), resultsCombo},
            {null, Box.createRigidArea(new Dimension(5, 0))},
            {components.get("titleLabel"), searchField, searchButton}
        };
        
        for (Component[] row : layout) {
            for (Component comp : row) {
                if (comp != null) searchRow.add(comp);
            }
        }

        // Set search action
        searchButton.addActionListener(e -> {
            String query = searchField.getText();
            String location = getSelectedItem(locationCombo);
            String industry = getSelectedItem(industryCombo);
            int numberOfResults = getNumberOfResults(resultsCombo);
            
            checkForSuggestions(query, numberOfResults, location, industry);
        });

        return searchRow;
    }
    
    /**
     * Gets the selected item from a combo box as a string.
     * 
     * @param combo The combo box to get the selected item from
     * @return The selected item as a string, or "any" if nothing is selected
     */
    private String getSelectedItem(JComboBox<?> combo) {
        Object obj = combo.getSelectedItem();
        return (obj != null) ? obj.toString() : "any";
    }
    
    /**
     * Gets the number of results from the results combo box.
     * Handles both integer and string inputs, with appropriate error handling.
     * 
     * @param resultsCombo The combo box containing the number of results
     * @return The number of results to return
     */
    private int getNumberOfResults(JComboBox<?> resultsCombo) {
        Object resultsObj = resultsCombo.getSelectedItem();
        int numberOfResults = 10; // Default
        
        if (resultsObj instanceof Integer num) {
            numberOfResults = num;
        } else if (resultsObj instanceof String res) {
            try {
                numberOfResults = Integer.parseInt(res);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Couldn't parse the number of results requested.\n" +
                    "Please ensure you enter a numeric value.\n" +
                    "Returned 10 results.",
                    "Null or Non-Numeric Value",
                    JOptionPane.WARNING_MESSAGE,
                    icons.get("warning"));
                numberOfResults = 10;
            }
        }
        return numberOfResults;
    }
    
    /**
     * Checks if a query might have a typo and suggests corrections.
     * If a suggestion is accepted, performs search with the suggestion.
     * Otherwise, performs search with the original query.
     * 
     * @param query The original search query
     * @param numberOfResults The number of results to return
     * @param location The location to filter by
     * @param industry The industry to filter by
     */
    private void checkForSuggestions(String query, int numberOfResults, String location, String industry) {
        if (query != null && !query.isEmpty() && 
            !query.equalsIgnoreCase("any") && !query.equalsIgnoreCase("all")) {
            
            String suggestion = controller.suggestQueryCorrection(query, 0);
            
            if (suggestion != null && !suggestion.equalsIgnoreCase(query)) {
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Did you mean \"" + suggestion + "\"?",
                    "Search Suggestion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    icons.get("lightbulb"));
                
                if (choice == JOptionPane.YES_OPTION) {
                    ((TextField)components.get("searchField")).setText(suggestion);
                    performSearch(suggestion, numberOfResults, location, industry);
                    return;
                }
            }
        }
        performSearch(query, numberOfResults, location, industry);
    }
    
    /**
     * Performs a search with the specified parameters.
     * 
     * @param query The search query
     * @param numberOfResults The number of results to return
     * @param location The location filter
     * @param industry The industry filter
     */
    private void performSearch(String query, int numberOfResults, String location, String industry) {
        searchResults = controller.getApiCall(query, numberOfResults, location, industry);
        handleSearchResults(searchResults, numberOfResults, query);
    }
    
    /**
     * Handles search results and shows appropriate messages.
     * 
     * @param results The search results
     * @param numberOfResults The number of results that was requested
     * @param query The original search query
     */
    private void handleSearchResults(List<JobRecord> results, int numberOfResults, String query) {
        if (results != null && !results.isEmpty()) {
            setJobsList(results);
            updateVisualizationIfNeeded(results);
        } else {
            if (numberOfResults > 50) {
                JOptionPane.showMessageDialog(this,
                    "The number of results requested is too large.\nPlease try a smaller number.",
                    "Too Many Results Requested",
                    JOptionPane.WARNING_MESSAGE,
                    icons.get("warning"));
            } else {
                JOptionPane.showMessageDialog(this,
                    "No jobs found for: " + query,
                    "No Results Found",
                    JOptionPane.INFORMATION_MESSAGE,
                    icons.get("warning"));
            }
        }
    }

    /**
     * Creates the bottom button panel that contains action buttons.
     * Includes Open and Save buttons for job management.
     *
     * @return The panel containing action buttons
     */
    @Override
    public JPanel makeBottomButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // Create buttons with array
        String[][] buttonProps = {
            {"Open", "PRIMARY", "open", "open"}, 
            {"Save", "SUCCESS", "saveIcon", "save"}
        };
        
        for (String[] props : buttonProps) {
            ThemedButton button = createThemedButton(props[0], 
                ThemedButton.ButtonType.valueOf(props[1]));
            button.setIcon(icons.get(props[2]));
            button.setHorizontalTextPosition(SwingConstants.LEFT);
            button.setIconTextGap(5);
            
            if ("open".equals(props[3])) {
                button.addActionListener(e -> openSelectedJob());
                openJob = button;
            } else {
                button.addActionListener(e -> saveSelectedJob());
                components.put("saveJob", button);
            }
            
            buttonPanel.add(button);
        }

        return buttonPanel;
    }
    
    /**
     * Updates the visualization panel with job data if needed.
     * 
     * @param jobs The list of jobs to update the visualization with
     */
    private void updateVisualizationIfNeeded(List<JobRecord> jobs) {
        if (salaryVisualizationPanel != null && jobs != null && !jobs.isEmpty()) {
            salaryVisualizationPanel.updateJobs(jobs);
        }
    }
    
    /**
     * Applies theme to visualization components.
     * 
     * @param theme The color theme to apply
     */
    private void applyThemeToVisualization(ColorTheme theme) {
        if (salaryVisualizationPanel != null) {
            salaryVisualizationPanel.applyTheme(theme);
        }
        
        if (showVisualizationCheckbox != null) {
            showVisualizationCheckbox.setBackground(theme.getBackground());
            showVisualizationCheckbox.setForeground(theme.getLabelForeground());
        }
    }

    /**
     * Applies the specified color theme to all components in this panel.
     * 
     * @param theme The color theme to apply
     */
    @Override
    public void applyTheme(ColorTheme theme) {
        super.applyTheme(theme);
        applyThemeToVisualization(theme);
        
        if (components.get("saveJob") != null) {
            ((ThemedButton)components.get("saveJob")).applyTheme(theme);
        }
        
        // Apply theme to all labels with a single loop
        java.awt.Color color = theme.getLabelForeground();
        for (String key : new String[]{"industryLabel", "locationLabel", "resultsLabel", "titleLabel"}) {
            if (components.get(key) instanceof JLabel) {
                ((JLabel)components.get(key)).setForeground(color);
            }
        }
    }

    /**
     * Opens the selected job in a new dialog.
     * Displays job details in a new dialog when a job is selected from the table.
     * Uses JobDetailsDialogue directly for proper rating functionality.
     */
    private void openSelectedJob() {
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx >= 0) {
            int modelIdx = jobsTable.convertRowIndexToModel(viewIdx);
            JobRecord selectedJob = jobsList.get(modelIdx);
            JobDetailsDialogue.showJobDetails(jobsTable, selectedJob, 
                controller.getSavedJobs(), controller);
        } else {
            JobActionHelper.showNoSelectionMessage("Please select a job to open", this);
        }
    }

    /**
     * Saves the selected job directly without opening the dialog.
     * Adds the job to saved jobs and switches to the Saved Jobs tab.
     * Uses JobActionHelper for consistent save behavior.
     */
    private void saveSelectedJob() {
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx < 0) {
            JobActionHelper.showNoSelectionMessage("Please select a job to save", this);
            return;
        }
        
        int modelIdx = jobsTable.convertRowIndexToModel(viewIdx);
        JobRecord selectedJob = jobsList.get(modelIdx);
        
        if (controller.isJobAlreadySaved(selectedJob)) {
            JOptionPane.showMessageDialog(this,
                "This job is already saved.",
                "Job Already Saved",
                JOptionPane.WARNING_MESSAGE,
                icons.get("warning"));
            return;
        }
        
        controller.jobToSavedList(selectedJob);
        controller.getUpdateJob(selectedJob.id(), "No comments provided", 0);
        
        JOptionPane.showMessageDialog(this,
            "Job saved successfully!",
            "Job Saved",
            JOptionPane.INFORMATION_MESSAGE,
            icons.get("success"));
        
        JobActionHelper.switchToSavedJobsTab(this, controller);
    }
    
    /**
     * Updates the job list and visualization if needed.
     * This method ensures we don't override search results with saved jobs.
     * 
     * @param jobsList The list of jobs to update
     */
    @Override
    public void updateJobsList(List<JobRecord> jobsList) {
        if (searchResults == null || searchResults.isEmpty()) {
            super.updateJobsList(jobsList);
            updateVisualizationIfNeeded(jobsList);
        } else {
            super.updateJobsList(searchResults);
            updateVisualizationIfNeeded(searchResults);
        }
    }
    
    /**
     * Sets the job list and updates the visualization if needed.
     * 
     * @param jobsList The list of jobs to set
     */
    @Override
    public void setJobsList(List<JobRecord> jobsList) {
        super.setJobsList(jobsList);
        updateVisualizationIfNeeded(jobsList);
    }
}