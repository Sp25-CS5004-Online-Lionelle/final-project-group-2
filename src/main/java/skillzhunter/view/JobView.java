package skillzhunter.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.TextField;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;
import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;

public abstract class JobView extends JPanel implements IJobView {
    protected JButton searchButton;
    protected JTextArea searchField;
    protected JTextArea recordText;
    protected JobsTable jobsTable = new JobsTable(getColumnNames(), new Object[0][0]);
    private ColorTheme theme;
    protected List<JobRecord> jobsList = new ArrayList<>();
    protected JButton darkModeToggle;
    protected JButton openJob;
    protected IController controller; // Declare the controller field
    protected JButton exit;
    protected JPanel topButtonLayout = new JPanel();
    protected JPanel mainPanel;
    protected boolean savedJobs = false;

    public JobView() {}

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

    // Method to set button properties such as color and hover effects
    public void setButtonProperties(JButton button) {
        Color normalColor = theme.buttonNormal;
        Color hoverColor = theme.buttonHover;
        button.setBackground(normalColor);
        button.setForeground(theme.buttonForeground);
        applyHoverEffect(button, hoverColor, normalColor);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
    }

    // Apply theme to the components
    public void applyTheme(ColorTheme theme) {
        setBackground(theme.background);
        openJob.setBackground(theme.buttonNormal);
        openJob.setForeground(theme.buttonForeground);
        jobsTable.setBackground(theme.fieldBackground);
        jobsTable.setForeground(theme.fieldForeground);
        jobsTable.setBorder(BorderFactory.createLineBorder(theme.buttonNormal));
        jobsTable.getTableHeader().setBackground(theme.fieldBackground);
        jobsTable.getTableHeader().setForeground(theme.foreground);
        repaint();
    }

    // Hover effect for buttons (change background color on hover)
    private void applyHoverEffect(JButton button, Color hoverColor, Color normalColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(theme.buttonHover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(theme.buttonNormal);
            }
        });
    }

    // Set the jobs list to update the table and UI
    public void setJobsList(List<JobRecord> jobsList) {
        //TESTING: jobsList is entering here with null comments

        System.out.println("Setting jobs list with: " + jobsList);
        this.jobsList = jobsList;
        this.jobsTable.setData(getData(jobsList));
        DefaultTableModel tableData = new DefaultTableModel(getData(jobsList), getColumnNames());
        this.jobsTable.setModel(tableData);
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
        controller.getAddJob(record); // Delegate to controller
        controller.setViewData(); // Update the view after adding a job
    }

    // Remove a job record via controller (delegates to controller)
    public void removeJobRecord(int index) {
        controller.getRemoveJob(index); // Delegate to controller
    }

    // Update the job list after an operation (like add or remove)
    public void updateJobsList(List<JobRecord> jobsList) {
        this.jobsList = jobsList;
        this.jobsTable.setData(getData(jobsList));
    }

    // Add listeners and features (e.g., search button functionality)
    public void addFeatures(IController controller) {
        this.controller = controller; // Set controller
        searchButton.addActionListener(e -> controller.setViewData());  // Delegate logic to controller for search
        // You can add other action listeners for other buttons (like add, remove job)
    }

    // Main method (not needed unless you want to test the view)
    public static void main(String[] args) {
        System.out.println("hello");
    }
}
