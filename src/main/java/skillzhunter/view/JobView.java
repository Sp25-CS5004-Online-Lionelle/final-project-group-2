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
    protected ThemedButton searchButton;
    protected JTextArea searchField;
    protected JTextArea recordText;
    protected JobsTable jobsTable = new JobsTable(getColumnNames(), new Object[0][0]);
    protected ColorTheme theme;
    protected List<JobRecord> jobsList = new ArrayList<>();
    protected ThemedButton openJob;
    protected IController controller; // Declare the controller field
    protected JPanel topButtonLayout = new JPanel();
    protected JPanel mainPanel;
    protected boolean savedJobs = false;

    public JobView() {
    }

    // Initialize the view with components
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
    public JPanel makeTopButtonPanel() {
        JPanel topRow = new JPanel();
        topRow.setLayout(new BoxLayout(topRow, BoxLayout.LINE_AXIS));
        topRow.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        TextField searchField = new TextField("!!This is Place Holder Overide this method!!", 20);
        topRow.add(searchField);
        return topRow;
    }

    // Method to create the bottom panel with buttons (customize as needed)
    public JPanel makeBottomButtonPanel() {
        JPanel bottomRow = new JPanel();
        bottomRow.setLayout(new BoxLayout(bottomRow, BoxLayout.LINE_AXIS));
        bottomRow.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        TextField searchField = new TextField("!!This is Place Holder Overide this method!!", 20);
        bottomRow.add(searchField);
        return bottomRow;
    }

    // Method to create the table panel to show the list of jobs
    public JPanel makeTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setPreferredSize(new Dimension(900, 400));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        jobsTable = new JobsTable(getColumnNames(), getData(jobsList));
        jobsTable.setAutoCreateRowSorter(true);
        JScrollPane tablePane = new JScrollPane(jobsTable);
        tablePanel.add(tablePane, BorderLayout.CENTER);
        return tablePanel;
    }

    // Apply theme to the components
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
    public void setJobsList(List<JobRecord> jobsList) {
        this.jobsList = jobsList;
        
        // Update the table with job data including logos
        this.jobsTable.setData(getData(jobsList));
        
        // Make sure the table is configured correctly
        this.jobsTable.repaint();
        this.jobsTable.setRowSelectionAllowed(true);
        this.jobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // Get the current jobs list
    public List<JobRecord> getJobsList() {
        return this.jobsList;
    }

    // Set text for job records (not used in this class but can be adapted)
    public void setRecordText(String text) {
        recordText.setText(text);
    }

    // Add a new job record via controller (delegates to controller)
    public void addJobRecord(JobRecord record) {
        controller.job2SavedList(record); // Delegate to controller
    }

    // Remove a job record via controller (delegates to controller)
    public void removeJobRecord(int index) {
        controller.removeJobFromList(index); // Delegate to controller
    }

    // Update the job list after an operation (like add or remove)
    public void updateJobsList(List<JobRecord> jobsList) {
        this.jobsList = jobsList;
        this.jobsTable.setData(getData(jobsList));
    }

    // Add listeners and features (e.g., search button functionality)
    public void addFeatures(IController controller) {
        this.controller = controller; // Set controller
        // if (searchButton != null) {
        //     searchButton.addActionListener(e -> controller.setViewData());  // Delegate logic to controller for search
        // }
        // You can add other action listeners for other buttons (like add, remove job)
    }

    // Get the current theme - useful for subclasses
    protected ColorTheme getTheme() {
        return theme;
    }

    // Create a new themed button with the specified text
    protected ThemedButton createThemedButton(String text) {
        ThemedButton button = new ThemedButton(text);
        if (theme != null) {
            button.applyTheme(theme);
        }
        return button;
    }
}