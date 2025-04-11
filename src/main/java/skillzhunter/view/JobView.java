package skillzhunter.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.TextField;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;
import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;

public abstract class JobView extends JPanel {
    /** search button. */
    protected ThemedButton searchButton;
    /** search field. */
    protected JTextArea searchField;
    /** record text area. */
    protected JTextArea recordText;
    /** jobs table. */
    protected JobsTable jobsTable = new JobsTable(getColumnNames(), new Object[0][0]);
    /** theme for the view. */
    protected ColorTheme theme;
    /** list of job records. */
    protected List<JobRecord> jobsList = new ArrayList<>();
    /** open job button. */
    protected ThemedButton openJob;
    /** save job button. */
    protected IController controller; // Declare the controller field
    /** save job button. */
    protected JPanel topButtonLayout = new JPanel();
    /** save job button. */
    protected JPanel mainPanel;
    /** save job button. */
    protected boolean savedJobs = false;

    /**
     * default constructor.
     */
    public JobView() {

    }

    /**
     * Initializes the view with a default size and layout.
     * Sets up the main panel and adds components to it.
     */
    public void initView() {
        setSize(1000, 1000);
        jobsTable = new JobsTable(getColumnNames(), getData(jobsList));
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(makeTopButtonPanel());
        mainPanel.add(makeTablePanel());
        mainPanel.add(makeBottomButtonPanel());
        add(mainPanel);
    }

    // Method to create the top panel with a search field (customize as needed)
    /**
     * Creates the top button panel with a search field.
     * This method can be overridden in subclasses to customize the panel.
     * @return The created top button panel
     */
    public JPanel makeTopButtonPanel() {
        JPanel topRow = new JPanel();
        topRow.setLayout(new BoxLayout(topRow, BoxLayout.LINE_AXIS));
        topRow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        TextField searchField = new TextField("!!This is Place Holder Override this method!!", 20);
        topRow.add(searchField);
        return topRow;
    }

    /**
     * Creates the bottom button panel with a search field.
     * This method can be overridden in subclasses to customize the panel.
     * @return The created bottom button panel
     */
    public JPanel makeBottomButtonPanel() {
        JPanel bottomRow = new JPanel();
        bottomRow.setLayout(new BoxLayout(bottomRow, BoxLayout.LINE_AXIS));
        bottomRow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        TextField searchField = new TextField("!!This is Place Holder Overide this method!!", 20);
        bottomRow.add(searchField);
        return bottomRow;
    }

    /**
     * Creates the table panel with a jobs table.
     * This method can be overridden in subclasses to customize the panel.
     * @return The created table panel
     */
    public JPanel makeTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setPreferredSize(new Dimension(900, 400));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(5, 5,  5, 5));
        jobsTable = new JobsTable(getColumnNames(), getData(jobsList));
        jobsTable.setAutoCreateRowSorter(true);
        JScrollPane tablePane = new JScrollPane(jobsTable);
        tablePanel.add(tablePane, BorderLayout.CENTER);
        return tablePanel;
    }

    /**
     * Applies the specified theme to the view and its components.
     * This method updates the background, foreground, and other properties of the components.
     * @param theme
     */
    public void applyTheme(ColorTheme theme) {
        this.theme = theme;
        
        // Apply to the panel itself
        setBackground(theme.background);
        
        // Apply to main panel
        if (mainPanel != null) {
            mainPanel.setBackground(theme.background);
            
            // Apply theme to inner panels
            for (int i = 0; i < mainPanel.getComponentCount(); i++) {
                if (mainPanel.getComponent(i) instanceof JPanel) {
                    mainPanel.getComponent(i).setBackground(theme.background);
                }
            }
        }
        
        // Apply to buttons
        if (openJob != null) {
            openJob.applyTheme(theme);
        }
        
        if (searchButton != null) {
            searchButton.applyTheme(theme);
        }

        // Apply to table
        if (jobsTable != null) {
            jobsTable.setBackground(theme.fieldBackground);
            jobsTable.setForeground(theme.fieldForeground);
            jobsTable.setBorder(BorderFactory.createLineBorder(theme.buttonNormal));
            
            if (jobsTable.getTableHeader() != null) {
                jobsTable.getTableHeader().setBackground(theme.fieldBackground);
                jobsTable.getTableHeader().setForeground(theme.foreground);
            }
            
            // Call the table's applyTheme method
            jobsTable.applyTheme(theme);
        }
        
        // Repaint to show changes
        repaint();
    }

    // Set the jobs list to update the table and UI
    /**
     * Sets the list of job records and updates the table with the new data.
     * This method also ensures that the table is configured correctly for displaying job data.
     * @param jobsList
     */
    public void setJobsList(List<JobRecord> jobsList) {
        this.jobsList = jobsList;
        
        // Update the table with job data including logos
        this.jobsTable.setData(getData(jobsList));
        
        // Make sure the table is configured correctly
        this.jobsTable.repaint();
        this.jobsTable.setRowSelectionAllowed(true);
        this.jobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Gets the list of job records.
     * 
     * @return The list of job records
     */
    public List<JobRecord> getJobsList() {
        return this.jobsList;
    }

    // Set text for job records (not used in this class but can be adapted)
    /**
     * Sets the text for the job record area.
     * 
     * @param text The text to set
     */
    public void setRecordText(String text) {
        recordText.setText(text);
    }

    /**
     * Adds a job record to the saved list via the controller.
     * 
     * @param record The job record to add
     */
    public void addJobRecord(JobRecord record) {
        controller.job2SavedList(record); // Delegate to controller
    }

    // Remove a job record via controller (delegates to controller)
    /**
     * Removes a job record from the saved list via the controller.
     * 
     * @param index The index of the job record to remove
     */
    public void removeJobRecord(int index) {
        controller.removeJobFromList(index); // Delegate to controller
    }

    // Update the job list after an operation (like add or remove)
    /**
     * updates the jobs list and refreshes the table data.
     * 
     * @param jobsList The updated list of job records
     */
    public void updateJobsList(List<JobRecord> jobsList) {
        this.jobsList = jobsList;
        this.jobsTable.setData(getData(jobsList));
    }

    // Add listeners and features (e.g., search button functionality)
    /**
     * Adds features to the view, such as action listeners for buttons.
     * 
     * @param controller The controller to delegate actions to
     */
    public void addFeatures(IController controller) {
        this.controller = controller; // Set controller
    }

    // Get the current theme - useful for subclasses
    /**
     * Gets the current theme applied to the view.
     * 
     * @return The current ColorTheme
     */
    protected ColorTheme getTheme() {
        return theme;
    }

    // Create a new themed button with the specified text
    /**
     * Creates a new themed button with the specified text.
     * 
     * @param text The text for the button
     * @return The created themed button
     */
    protected ThemedButton createThemedButton(String text) {
        return createThemedButton(text, ThemedButton.ButtonType.PRIMARY);
    }
    
    // New method to create a themed button with a specific type
    protected ThemedButton createThemedButton(String text, ThemedButton.ButtonType buttonType) {
        ThemedButton button = new ThemedButton(text, buttonType);
        if (theme != null) {
            button.applyTheme(theme);
        }
        return button;
    }
}