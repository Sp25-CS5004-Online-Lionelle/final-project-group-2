package skillzhunter.view;

import javax.swing.*;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.util.List;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.GridLayout;

import skillzhunter.model.JobRecord;
import skillzhunter.controller.IController;
import skillzhunter.controller.MainController;

public class JobDetailsDialogue {

    // The size for company logos
    private static final int LOGO_WIDTH = 64;
    private static final int LOGO_HEIGHT = 64;

    /**
     * Loads an icon from the resources folder.
     * Now delegates to IconLoader utility class.
     *
     * @param path The path to the icon
     * @return The loaded icon, or null if it couldn't be loaded
     */
    private static ImageIcon loadIcon(String path) {
        return IconLoader.loadIcon(path, 24, 24);
    }

    /**
     * Attempts to load a company logo from a URL.
     * If loading fails, returns the default icon.
     * Now delegates to IconLoader utility class.
     * 
     * @param logoUrl The URL of the company logo
     * @return The loaded logo as an ImageIcon, or a default icon if loading fails
     */
    private static ImageIcon loadCompanyLogo(String logoUrl) {
        return IconLoader.loadCompanyLogo(logoUrl, LOGO_WIDTH, LOGO_HEIGHT, "images/idea.png");
    }

    public static void showJobDetails(Component parent, JobRecord job, List<JobRecord> savedJobs, IController controller) {
        if (job == null || controller == null) {
            ImageIcon errorIcon = loadIcon("images/warning.png");
                    JOptionPane.showMessageDialog(parent,
                            "Job or controller not provided.",
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE,
                            errorIcon);
            return;
        }
        
        boolean jobPresent = savedJobs != null && savedJobs.contains(job);
        
        // Check if this is being accessed from Saved Jobs tab
        if (jobPresent) {
            // Use the specialized SavedJobDetailsDialog for saved jobs
            SavedJobDetailsDialogue.show(parent, job, controller);
        } else {
            // Keep the original implementation for Find Jobs tab
            showFindJobDetails(parent, job, savedJobs, controller);
        }
    }
    
    /**
     * Shows job details when accessed from Find Jobs tab
     * with "Save this Job?" option and fields for user input
     */
    private static void showFindJobDetails(Component parent, JobRecord job, List<JobRecord> savedJobs, IController controller) {
        // Create the dialog - properly get the parent window
        Window parentWindow = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog;
        
        if (parentWindow instanceof java.awt.Frame) {
            dialog = new JDialog((java.awt.Frame) parentWindow, "Job Details", true);
        } else if (parentWindow instanceof java.awt.Dialog) {
            dialog = new JDialog((java.awt.Dialog) parentWindow, "Job Details", true);
        } else {
            dialog = new JDialog(new JFrame(), "Job Details", true);
        }
        
        dialog.setLayout(new BorderLayout(0, 10));
        
        // Create main panel for content
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        
        // Add logo at top, centered
        ImageIcon companyLogo = loadCompanyLogo(job.companyLogo());
        JLabel logoLabel = new JLabel(companyLogo);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(logoLabel, BorderLayout.CENTER);
        
        // Add header panel to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Add company name
        JLabel companyNameLabel = new JLabel(job.companyName(), SwingConstants.CENTER);
        companyNameLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        companyNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(companyNameLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Add job title
        JLabel titleLabel = new JLabel("<html><h2>" + job.jobTitle() + "</h2></html>", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Create a simple panel for job details with 2 columns
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        
        // Add job details
        if (job.jobGeo() != null && !job.jobGeo().isEmpty()) {
            addDetailRow(detailsPanel, "Location:", job.jobGeo());
        }
        
        if (job.jobIndustry() != null && !job.jobIndustry().isEmpty()) {
            String industries = String.join(", ", job.jobIndustry());
            addDetailRow(detailsPanel, "Industry:", industries);
        }
        
        if (job.jobType() != null && !job.jobType().isEmpty()) {
            String types = String.join(", ", job.jobType().stream()
                .map(type -> {
                    String[] parts = type.split("-");
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
                    }
                    return String.join("-", parts);
                }).toList());
            addDetailRow(detailsPanel, "Type:", types);
        }
        
        if (job.jobLevel() != null && !job.jobLevel().isEmpty()) {
            addDetailRow(detailsPanel, "Level:", job.jobLevel());
        }
        
        if ((job.annualSalaryMin() != 0 || job.annualSalaryMax() != 0)) {
            String salaryText = String.format("%s - %s",
                job.annualSalaryMin() == 0 ? "N/A" : String.format("%,d", job.annualSalaryMin()),
                job.annualSalaryMax() == 0 ? "N/A" : String.format("%,d", job.annualSalaryMax())
            );
            
            if (job.salaryCurrency() != null && !job.salaryCurrency().isEmpty()) {
                salaryText += " " + job.salaryCurrency();
            }
            
            addDetailRow(detailsPanel, "Salary:", salaryText);
        }
        
        if (job.pubDate() != null && !job.pubDate().isEmpty()) {
            addDetailRow(detailsPanel, "Published:", job.pubDate());
        }
        
        // Center the details panel
        JPanel detailsWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        detailsWrapper.add(detailsPanel);
        contentPanel.add(detailsWrapper);
        
        // Add rating and comments section
        JPanel ratingPanel = new JPanel(new BorderLayout(5, 5));
        ratingPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Create star rating panel
        StarRatingPanel starRating = new StarRatingPanel(job.rating() > 0 ? job.rating() : 0);
        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel ratingLabel = new JLabel("Your Rating:");
        starPanel.add(ratingLabel);
        starPanel.add(starRating);
        ratingPanel.add(starPanel, BorderLayout.NORTH);
        
        // Create comments text area
        JTextArea comments = new JTextArea(3, 20);
        comments.setLineWrap(true);
        comments.setWrapStyleWord(true);
        
        // Set existing comments if available
        if (job.comments() != null && !job.comments().isEmpty() && !job.comments().equals("No comments provided")) {
            comments.setText(job.comments());
        }
        
        JScrollPane commentScrollPane = new JScrollPane(comments);
        commentScrollPane.setBorder(BorderFactory.createTitledBorder("Your Comments"));
        ratingPanel.add(commentScrollPane, BorderLayout.CENTER);
        
        contentPanel.add(ratingPanel);
        
        // Create a save question panel
        JPanel confirmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel confirmLabel = new JLabel("Save this Job?");
        confirmLabel.setFont(confirmLabel.getFont().deriveFont(Font.BOLD));
        confirmPanel.add(confirmLabel);
        contentPanel.add(confirmPanel);
        
        // Add content panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add the main panel to the dialog
        dialog.add(mainPanel, BorderLayout.CENTER);
        
        // Create button panel with Yes/No options
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        
        // Set consistent size for buttons
        int buttonWidth = 100;
        int buttonHeight = Math.max(yesButton.getPreferredSize().height, noButton.getPreferredSize().height);
        
        yesButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        noButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Handle the button clicks
        yesButton.addActionListener(e -> {
            // Get the rating and comments from UI
            int rating = starRating.getRating();
            String commentText = comments.getText().isEmpty() ? "No comments provided" : comments.getText();
            
            // First, add the job to the model through the controller
            controller.getAddJob(job);
            
            // Then update the job with the rating and comments
            if (controller instanceof MainController) {
                ((MainController) controller).getUpdateJob(job.id(), commentText, rating);
            }
            
            // Switch to "Saved Jobs" tab after adding a job
            switchToSavedJobsTab(parent, controller);
            
            dialog.dispose();
        });
        
        noButton.addActionListener(e -> dialog.dispose());
        
        // Set dialog properties
        dialog.setSize(450, 580);
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    /**
     * Switches to the Saved Jobs tab
     * @param component The component used to find the main window
     */
    private static void switchToSavedJobsTab(Component component, IController controller) {
        // Find the main window containing the tabs
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(component);
        if (frame != null) {
            // Look for the tabbed pane in the component hierarchy
            JTabbedPane tabbedPane = findTabbedPane(frame.getContentPane());
            if (tabbedPane != null) {
                // Find and select the "Saved Jobs" tab
                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    String title = tabbedPane.getTitleAt(i);
                    Component comp = tabbedPane.getTabComponentAt(i);
                    
                    if ("Saved Jobs".equals(title) || 
                        (comp instanceof JLabel && "Saved Jobs".equals(((JLabel)comp).getText()))) {
                        
                        // Select the tab
                        tabbedPane.setSelectedIndex(i);
                        
                        // Update the tab content if it's a SavedJobsTab
                        Component tabComponent = tabbedPane.getComponentAt(i);
                        if (tabComponent instanceof SavedJobsTab) {
                            SavedJobsTab savedJobsTab = (SavedJobsTab) tabComponent;
                            savedJobsTab.updateJobsList(controller.getSavedJobs());
                        }
                        
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Recursively searches for a JTabbedPane in the component hierarchy
     * @param component The component to search within
     * @return The found JTabbedPane or null if none is found
     */
    private static JTabbedPane findTabbedPane(Component component) {
        if (component instanceof JTabbedPane) {
            return (JTabbedPane) component;
        } else if (component instanceof Container) {
            Container container = (Container) component;
            for (int i = 0; i < container.getComponentCount(); i++) {
                JTabbedPane found = findTabbedPane(container.getComponent(i));
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
    /**
     * Helper method to add detail rows to the details panel
     */
    private static void addDetailRow(JPanel panel, String label, String value) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(labelComp.getFont().deriveFont(Font.BOLD));
        panel.add(labelComp);
        
        JLabel valueComp = new JLabel(value);
        panel.add(valueComp);
    }
}