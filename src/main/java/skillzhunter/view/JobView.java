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

        // Table Panel with Scrollable Jobs Table
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout()); // Use BorderLayout for better handling of the table
        tablePanel.setPreferredSize(new Dimension(800, 400)); // Set a reasonable preferred size for the table

        jobsTable = new JobsTable(getColumnNames(), getData(jobsList));

        JScrollPane tablePane = new JScrollPane(jobsTable);
        tablePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tablePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tablePanel.add(tablePane, BorderLayout.CENTER); // Add tablePane to the center of the panel
        mainPanel.add(tablePanel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton openJob = new JButton("Open Job");
        openJob.addActionListener(e -> openSelectedJob());

        JButton exit = new JButton("Exit");
        exit.addActionListener(exitEvent -> {
            int result = JOptionPane.showConfirmDialog(mainPanel, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Dark Mode Toggle Button
        darkModeToggle = new JToggleButton("🌙");
        darkModeToggle.setSelected(false);
        darkModeToggle.addActionListener(e -> {
            boolean isDarkMode = darkModeToggle.isSelected();
            theme = new ColorTheme(isDarkMode); 
            applyTheme(); 
            darkModeToggle.setText(isDarkMode ? "☀️" : "🌙");
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
        darkModeToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jobsTable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        add(mainPanel);
        applyTheme(); // Apply initial theme
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
        jobsTable.setBorder(BorderFactory.createLineBorder(theme.getBorderColor()));
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

            JTextArea comments = new JTextArea(5, 20);
            comments.setLineWrap(true);
            comments.setWrapStyleWord(true);
            comments.setBorder(BorderFactory.createTitledBorder("Your Comments"));

            Object[] obj = {"Job Title: ", jobTitle, "Company: ", jobCompany, "Industry: ", jobIndustry,
                            "Type: ", jobType, "Location: ", jobGeo, "Level: ", jobLevel, "Salary: ", jobSalaryRange,
                            "Currency: ", jobCurrency, "Published: ", jobPubDate, "Rate this Job: ", jobRating, 
                            "Comments:", comments, "Save this Job?"};

            JOptionPane.showConfirmDialog(jobsTable, obj, "Job Details: " + activeJob.id(), JOptionPane.INFORMATION_MESSAGE);
        }
    }



    public void setRecordText(String text) {
        recordText.setText(text);
    }

    public void addFeatures(IJobController controller) {
        searchButton.addActionListener(e -> controller.setViewData());
    }

    public static void main(String[] args) {
        System.out.println("hello");
    }
}
