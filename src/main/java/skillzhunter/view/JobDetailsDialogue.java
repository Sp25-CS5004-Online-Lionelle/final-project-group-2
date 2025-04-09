package skillzhunter.view;

import javax.swing.*;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

import skillzhunter.model.JobRecord;
import skillzhunter.controller.IController;
import skillzhunter.controller.MainController;

public class JobDetailsDialogue {

    public static void showJobDetails(Component parent, JobRecord job, List<JobRecord> savedJobs, IController controller) {
        if (job == null || controller == null) {
            JOptionPane.showMessageDialog(parent, "Job or controller not provided.", "Error", JOptionPane.ERROR_MESSAGE);
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

        Object[] obj = {
            "Job Title: ", jobTitle, "Company: ", jobCompany, "Industry: ", jobIndustry,
            "Type: ", jobType, "Location: ", jobGeo, "Level: ", jobLevel, "Salary: ", jobSalaryRange,
            "Currency: ", jobCurrency, "Published: ", jobPubDate, starRating,
            new JScrollPane(comments), dialogMsg
        };

        // Show the dialog and get user's action
        int result = JOptionPane.showConfirmDialog(parent, obj, "Job Details: ", JOptionPane.YES_NO_OPTION);

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
                
                // Find the tabbed pane and switch to Saved Jobs
                switchToSavedJobsTab(parent);
            }
        }
    }

    /**
     * Helper method to find the main JTabbedPane and switch to the Saved Jobs tab
     */
    private static void switchToSavedJobsTab(Component component) {
        // Get the window ancestor (JFrame)
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(component);
        if (frame == null) return;
        
        // Find the JTabbedPane directly from the frame's content pane
        JTabbedPane tabbedPane = null;
        for (Component c : frame.getContentPane().getComponents()) {
            if (c instanceof JPanel) {
                for (Component innerComp : ((JPanel) c).getComponents()) {
                    if (innerComp instanceof JTabbedPane) {
                        tabbedPane = (JTabbedPane) innerComp;
                        break;
                    }
                }
            }
        }
        
        if (tabbedPane != null) {
            // Find the index of the "Saved Jobs" tab
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                if (tabbedPane.getTitleAt(i).equals("Saved Jobs")) {
                    tabbedPane.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    // 👇 This helper method must be inside the class, and static
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