package skillzhunter.view;

import javax.swing.*;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import java.util.List;

import skillzhunter.model.JobRecord;
import skillzhunter.controller.IController;
import skillzhunter.controller.MainController;

public class JobDetailsDialogue {

    /**
     * Loads an icon from the resources folder.
     * Simplified version similar to SavedJobsTab implementation.
     *
     * @param path The path to the icon
     * @return The loaded icon, or null if it couldn't be loaded
     */
    private static ImageIcon loadIcon(String path) {
        try {
            URL url = JobDetailsDialogue.class.getClassLoader().getResource(path);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + path);
        }
        return null;
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

        // Use helper method to make non-editable JTextAreas
        JTextArea jobTitle = readOnlyArea(job.jobTitle());
        JTextArea jobCompany = readOnlyArea(job.companyName());
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

        // Create a panel with icon for the confirmation message
        JPanel confirmPanel = new JPanel();
        confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.X_AXIS));
        
        // Load appropriate icon based on whether job is being saved or removed
        ImageIcon actionIcon = jobPresent ? 
            loadIcon("images/idea.png") : loadIcon("images/saveIcon.png");
        
        if (actionIcon != null) {
            JLabel iconLabel = new JLabel(actionIcon);
            confirmPanel.add(iconLabel);
            confirmPanel.add(Box.createHorizontalStrut(10)); // Add some spacing
        }
        
        JLabel confirmLabel = new JLabel(dialogMsg);
        confirmLabel.setFont(confirmLabel.getFont().deriveFont(Font.BOLD));
        confirmPanel.add(confirmLabel);

        Object[] obj = {
            "Job Title: ", jobTitle, "Company: ", jobCompany, "Industry: ", jobIndustry,
            "Type: ", jobType, "Location: ", jobGeo, "Level: ", jobLevel, "Salary: ", jobSalaryRange,
            "Currency: ", jobCurrency, "Published: ", jobPubDate, starRating,
            new JScrollPane(comments), confirmPanel  // Use the custom panel with icon instead of just text
        };

        // Create a JOptionPane with custom icon for the dialog itself
        JOptionPane optionPane = new JOptionPane(
            obj,
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_OPTION
        );
        
        // Set a custom icon for the entire dialog
        ImageIcon dialogIcon = loadIcon("images/idea.png");
        if (dialogIcon != null) {
            optionPane.setIcon(dialogIcon);
        }
        
        // Create and display the dialog
        JDialog dialog = optionPane.createDialog(parent, "Job Details: ");
        dialog.setResizable(true);
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