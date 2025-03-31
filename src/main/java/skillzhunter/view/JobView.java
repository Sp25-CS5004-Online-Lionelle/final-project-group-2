package skillzhunter.view;

import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;
import java.util.List;
import javax.swing.table.TableColumn;
import skillzhunter.controller.FindJobController;
import skillzhunter.controller.IJobController;
import skillzhunter.model.JobRecord;
import javax.swing.*;
import java.awt.*;

public class JobView extends JPanel {
    protected JButton searchButton;
    protected JTextArea recordText;
    protected JobsTable jobsTable;
    private ColorTheme theme;
    protected List<JobRecord> jobsList = JobRecordGenerator.generateDummyRecords(10);
    /** Checkbox for darkmode. */
    private JToggleButton darkModeToggle;

    public JobView() {
        setSize(1000, 1000);
        theme = new ColorTheme(false); // Start with light mode
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    
        searchButton = new JButton("Find Jobs");
        mainPanel.add(searchButton);
    
        recordText = new JTextArea("Click to find jobs");
        mainPanel.add(recordText);
    
        JPanel tablePanel = new JPanel(new FlowLayout());
        tablePanel.setSize(600, 600);
    
        jobsTable = new JobsTable(getColumnNames(), getData(jobsList));
    
        JScrollPane tablePane = new JScrollPane(jobsTable);
        tablePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tablePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tablePanel.add(tablePane);
        mainPanel.add(tablePanel);
    
        // Create a horizontal panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));  // Center alignment
    
        JButton openJob = new JButton("Open Job");
        openJob.addActionListener(e -> openSelectedJob());
    
        JButton exit = new JButton("Exit");
        exit.addActionListener(exitEvent -> {
            int result = JOptionPane.showConfirmDialog(mainPanel, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    
        // Dark Mode Toggle Button with Moon and Sun Icons
        //weird artifact currently next to sun and cant figure out why - TOR please figure out
        darkModeToggle = new JToggleButton("🌙");  // Default to moon (dark mode)
        darkModeToggle.setSelected(false); // Start with light mode
        darkModeToggle.addActionListener(e -> {
            boolean isDarkMode = darkModeToggle.isSelected();
            theme = new ColorTheme(isDarkMode); // Update theme instance
            applyTheme(); // Apply new theme
            darkModeToggle.setText(isDarkMode ? "☀️" : "🌙"); // Change text
        });
    
        // Add buttons to button panel
        buttonPanel.add(openJob);
        buttonPanel.add(darkModeToggle);
        buttonPanel.add(exit);
        mainPanel.add(buttonPanel);
    
        // Set cursor for all buttons
        JButton[] buttons = {searchButton, openJob, exit};
        for (JButton button : buttons) {
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        // Set cursor for the dark mode toggle button
        darkModeToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Set cursor for the jobs table
        jobsTable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
        add(mainPanel);
        applyTheme(); // Apply initial theme
    }

    public void setRecordText(String text) {
        recordText.setText(text);
    }

    public void addFeatures(IJobController controller) {
        searchButton.addActionListener(e -> controller.setViewData());
    }

    private void applyTheme() {
        setBackground(theme.getBackgroundColor());
    
        recordText.setBackground(theme.getBackgroundColor());
        recordText.setForeground(theme.getForegroundColor());
    
        searchButton.setBackground(theme.getButtonBackgroundColor());
        searchButton.setForeground(theme.getButtonForegroundColor());
    
        darkModeToggle.setBackground(theme.getButtonBackgroundColor());
        darkModeToggle.setForeground(theme.getButtonForegroundColor());
    
        jobsTable.setBackground(theme.getTableBackgroundColor());
        jobsTable.setForeground(theme.getTableForegroundColor());
        jobsTable.setBorder(BorderFactory.createLineBorder(theme.getBorderColor())); // Add border
        jobsTable.getTableHeader().setBackground(theme.getTableHeaderBackgroundColor());
        jobsTable.getTableHeader().setForeground(theme.getTableHeaderForegroundColor());
    
        repaint();
    }

    private void openSelectedJob() {
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx >= 0) {
            int n = jobsTable.convertRowIndexToModel(viewIdx);
            JobRecord activeJob = jobsList.get(n);
            
            JTextArea jobTitle = new JTextArea(activeJob.jobTitle());
            JTextArea jobCompany = new JTextArea(activeJob.companyName());
            JTextArea jobIndustry = new JTextArea(activeJob.jobIndustry().toString());
            JTextArea jobType = new JTextArea(activeJob.jobType().toString());
            JTextArea jobGeo = new JTextArea(activeJob.jobGeo());
            JTextArea jobLevel = new JTextArea(activeJob.jobLevel());
            JTextArea jobSalaryRange = new JTextArea(String.valueOf(activeJob.annualSalaryMin()));
            JTextArea jobCurrency = new JTextArea(activeJob.salaryCurrency());
            JTextArea jobPubDate = new JTextArea(activeJob.pubDate());
            
            JSlider jobRating = new JSlider(0, 10, 5);
            jobRating.setMajorTickSpacing(5);
            jobRating.setMinorTickSpacing(1);
            jobRating.setPaintTicks(true);
            jobRating.setPaintLabels(true);
            
            //add text area for comments for user to add their own comments
            JTextArea comments = new JTextArea(5, 20);
            comments.setLineWrap(true);
            comments.setWrapStyleWord(true);
            comments.setBorder(BorderFactory.createTitledBorder("Your Comments"));


            Object[] obj = {"Job Title: ", jobTitle, "Company: ", jobCompany, "Industry: ", jobIndustry,
                            "Type: ", jobType, "Location: ", jobGeo, "Level: ", jobLevel, "Salary: ", jobSalaryRange,
                            "Currency: ", jobCurrency, "Published: ", jobPubDate, "Rate this Job: ", jobRating, "Comments:", comments, "Save this Job?"};
            
            JOptionPane.showConfirmDialog(jobsTable, obj, "Job Details: " + activeJob.id(), JOptionPane.INFORMATION_MESSAGE);
        }
    };

    public static void main(String[] args) {
        System.out.println("hello");
    }
}
