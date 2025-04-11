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
        String dialogMsg = jobPresent ? "Remove this Job?" : "Save this Job?";

        // Load company logo (or default icon if no logo available)
        ImageIcon companyLogo = loadCompanyLogo(job.companyLogo());
        JLabel logoLabel = new JLabel(companyLogo);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setPreferredSize(new Dimension(LOGO_WIDTH + 10, LOGO_HEIGHT + 10));
        
        // Create company header panel with logo and name - using vertical BoxLayout to maintain centering
        JPanel companyPanel = new JPanel();
        companyPanel.setLayout(new BoxLayout(companyPanel, BoxLayout.Y_AXIS));
        
        // Create a panel just for the logo to control its alignment
        JPanel logoPanel = new JPanel();
        logoPanel.add(logoLabel);
        
        // Company name with larger font
        JLabel companyNameLabel = new JLabel(job.companyName());
        companyNameLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        companyNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components to company panel with proper alignment
        companyPanel.add(logoPanel);
        companyPanel.add(Box.createVerticalStrut(5)); // Add spacing
        companyPanel.add(companyNameLabel);

        // Use helper method to make non-editable JTextAreas
        JTextArea jobTitle = readOnlyArea(job.jobTitle());
        jobTitle.setFont(new Font("Dialog", Font.BOLD, 14));
        
        JTextArea jobIndustry = readOnlyArea(
            job.jobIndustry() != null ? 
                String.join(", ", job.jobIndustry().stream()
                    .map(industry -> industry.replace("&amp;", ",").replace("&", ",").trim())
                    .toList()) 
                : "N/A"
        );
        JTextArea jobType = readOnlyArea(
            job.jobType() != null ?
                String.join(", ", job.jobType().stream()
                    .map(type -> {
                        String[] parts = type.split("-");
                        for (int i = 0; i < parts.length; i++) {
                            parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
                        }
                        return String.join("-", parts);
                    }).toList())
                : "N/A"
        );
        JTextArea jobGeo = readOnlyArea(job.jobGeo());
        JTextArea jobLevel = readOnlyArea(job.jobLevel());
        JTextArea jobSalaryRange = readOnlyArea(
            job.annualSalaryMin() == 0 && job.annualSalaryMax() == 0 
                ? "N/A" 
                : String.format("%s - %s",
                    job.annualSalaryMin() == 0 ? "N/A" : String.format("%,d", job.annualSalaryMin()),
                    job.annualSalaryMax() == 0 ? "N/A" : String.format("%,d", job.annualSalaryMax())
                )
        );
        JTextArea jobCurrency = readOnlyArea(
            job.salaryCurrency() == null || job.salaryCurrency().isEmpty() ? "N/A" : job.salaryCurrency()
        );
        JTextArea jobPubDate = readOnlyArea(job.pubDate());

        // Create star rating panel instead of slider
        StarRatingPanel starRating = new StarRatingPanel(job.rating() > 0 ? job.rating() : 0);

        // Comments Box - Initialize with existing comments if available
        JTextArea comments = new JTextArea(5, 20);
        comments.setLineWrap(true);
        comments.setWrapStyleWord(true);
        comments.setBorder(BorderFactory.createTitledBorder("Your Comments"));
        
        // Only set comments if they exist and aren't the default
        if (job.comments() != null && !job.comments().isEmpty() && !job.comments().equals("No comments provided")) {
            comments.setText(job.comments());
        }

        // Create a panel for the confirmation message using just text (no icon)
        JPanel confirmPanel = new JPanel();
        confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.X_AXIS));
        
        JLabel confirmLabel = new JLabel(dialogMsg);
        confirmLabel.setFont(confirmLabel.getFont().deriveFont(Font.BOLD));
        confirmPanel.add(confirmLabel);

        Object[] obj = {
            companyPanel,
            "Job Title: ", jobTitle, "Industry: ", jobIndustry,
            "Type: ", jobType, "Location: ", jobGeo, "Level: ", jobLevel, "Salary: ", jobSalaryRange,
            "Currency: ", jobCurrency, "Published: ", jobPubDate, starRating,
            new JScrollPane(comments), confirmPanel
        };

        // Create a JOptionPane without a custom icon for the dialog itself
        JOptionPane optionPane = new JOptionPane(
            obj,
            JOptionPane.PLAIN_MESSAGE,  // Changed to PLAIN_MESSAGE to remove default icon
            JOptionPane.YES_NO_OPTION
        );
        
        // Create and display the dialog
        JDialog dialog = optionPane.createDialog(parent, "Job Details: ");
        dialog.setResizable(true);
        dialog.setLocationRelativeTo(parent); // Explicitly center the dialog on the parent
        dialog.setVisible(true);
        
        // Get the result (return value is an Integer or null if closed)
        Object value = optionPane.getValue();
        int result = (value instanceof Integer) ? (Integer) value : JOptionPane.CLOSED_OPTION;

        // Get the final values
        final int finalRating = starRating.getRating();
        final String finalComments = comments.getText();
        
        // Create a new job record with the updated values
        JobRecord updatedJob = new JobRecord(
            job.id(), job.url(), job.jobSlug(), job.jobTitle(), job.companyName(),
            job.companyLogo(), job.jobIndustry(), job.jobType(), job.jobGeo(),
            job.jobLevel(), job.jobExcerpt(), job.jobDescription(), job.pubDate(),
            job.annualSalaryMin(), job.annualSalaryMax(), job.salaryCurrency(),
            finalRating, finalComments
        );

        // Handle the user's choice
        if (result == JOptionPane.YES_OPTION) {
            if (jobPresent) {
                SavedJobsLists.removeSavedJob(updatedJob);
            } else {
                SavedJobsLists.addSavedJob(updatedJob);
                
                // Switch to "Saved Jobs" tab after adding a job
                switchToSavedJobsTab(parent);
            }
        }
    }

    /**
     * Switches to the Saved Jobs tab
     * @param component The component used to find the main window
     */
    private static void switchToSavedJobsTab(Component component) {
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
                    
                    // Check both the tab title and any custom tab components
                    if ("Saved Jobs".equals(title) || 
                        (comp instanceof JLabel && "Saved Jobs".equals(((JLabel)comp).getText()))) {
                        tabbedPane.setSelectedIndex(i);
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

    // Helper method to create read-only text areas
    private static JTextArea readOnlyArea(String text) {
        JTextArea area = new JTextArea(text != null ? text : "N/A");
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setBorder(null);
        return area;
    }
}