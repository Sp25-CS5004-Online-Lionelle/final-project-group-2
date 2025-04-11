package skillzhunter.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import skillzhunter.model.JobRecord;
import skillzhunter.controller.IController;
import skillzhunter.controller.MainController;

public class JobDetailsDialogue extends BaseJobDetailsDialogue {

    /**
     * Shows job details when accessed from the Find Jobs tab
     * with "Save this Job?" option and fields for user input.
     * @param parent The parent component
     * @param job The job record from the controller
     * @param savedJobs List of saved jobs
     * @param controller The controller instance
     */
    public static void showJobDetails(Component parent, JobRecord job,
                                    List<JobRecord> savedJobs, IController controller) {
        if (job == null || controller == null) {
            ImageIcon errorIcon = IconLoader.loadIcon("images/warning.png");
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
            showFindJobDetails(parent, job, controller);
        }
    }
    
    /**
     * Shows job details when accessed from Find Jobs tab
     * with "Save this Job?" option and fields for user input.
     * @param parent The parent component
     * @param job The job record from the controller
     * @param controller The controller instance
     */
    private static void showFindJobDetails(Component parent, JobRecord job, IController controller) {
        // Create the dialog using the base class method
        JDialog dialog = createBaseDialog(parent, job, "Job Details");
        
        // Create and add the main content panel
        JPanel mainPanel = createMainContentPanel(job);
        
        // Get the content panel from the scroll pane
        JScrollPane scrollPane = (JScrollPane) mainPanel.getComponent(1);
        JPanel contentPanel = (JPanel) scrollPane.getViewport().getView();
        
        // Add rating and comments section
        JPanel ratingPanel = new JPanel(new BorderLayout(5, 5));
        ratingPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Create star rating panel - use existing rating from job 
        StarRatingPanel starRating = new StarRatingPanel(job.rating() > 0 ? job.rating() : 0);
        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel ratingLabel = new JLabel("Your Rating:");
        starPanel.add(ratingLabel);
        starPanel.add(starRating);
        ratingPanel.add(starPanel, BorderLayout.NORTH);
        
        // Create comments text area
        JTextArea commentsArea = new JTextArea(3, 20);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        
        // Set existing comments if available
        if (job.comments() != null && !job.comments().isEmpty() && !job.comments().equals("No comments provided")) {
            commentsArea.setText(job.comments());
        }
        
        JScrollPane commentScrollPane = new JScrollPane(commentsArea);
        commentScrollPane.setBorder(BorderFactory.createTitledBorder("Your Comments"));
        ratingPanel.add(commentScrollPane, BorderLayout.CENTER);
        
        contentPanel.add(ratingPanel);
        
        // Create a save question panel
        JPanel confirmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel confirmLabel = new JLabel("Save this Job?");
        confirmLabel.setFont(confirmLabel.getFont().deriveFont(Font.BOLD));
        confirmPanel.add(confirmLabel);
        contentPanel.add(confirmPanel);
        
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
            int newRating = starRating.getRating();
            String commentText = commentsArea.getText().isEmpty() ? "No comments provided" : commentsArea.getText();
            
            // First, add the job to the model through the controller
            controller.job2SavedList(job);
            
            // Then update the job with the rating and comments through the controller
            if (controller instanceof MainController cont) {
                (cont).getUpdateJob(job.id(), commentText, newRating);
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
}
