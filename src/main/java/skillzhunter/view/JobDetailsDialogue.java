package skillzhunter.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import skillzhunter.model.JobRecord;
import skillzhunter.controller.IController;
import skillzhunter.controller.MainController;

/**
 * Dialog for displaying job details with the option to save the job.
 * This is used specifically when opening a job from the Find Jobs tab.
 */
public class JobDetailsDialogue extends BaseJobDetailsDialogue {

    // Icons for buttons and dialogs
    /** info icon. */
    private static final ImageIcon INFO_ICON = IconLoader.loadIcon("images/info.png");
    /** warning icon. */
    private static final ImageIcon WARNING_ICON = IconLoader.loadIcon("images/warning.png");
    /** success icon. */
    private static final ImageIcon SUCCESS_ICON = IconLoader.loadIcon("images/success.png");

    /**
     * Shows job details when accessed from the Find Jobs tab
     * with "Save this Job?" option and fields for user input.
     * 
     * @param parent The parent component
     * @param job The job record from the controller
     * @param savedJobs List of saved jobs
     * @param controller The controller instance
     */
    public static void showJobDetails(Component parent, JobRecord job,
                                    List<JobRecord> savedJobs, IController controller) {
        if (job == null || controller == null) {
            JOptionPane.showMessageDialog(parent,
                    "Job or controller not provided.",
                    "Error",
                    JOptionPane.INFORMATION_MESSAGE,
                    WARNING_ICON);
            return;
        }
        
        // Check if this job is already saved
        boolean jobPresent = controller.isJobAlreadySaved(job);
        
        // If job is already saved, use the specialized SavedJobDetailsDialog
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
     * 
     * @param parent The parent component
     * @param job The job record from the controller
     * @param controller The controller instance
     */
    private static void showFindJobDetails(Component parent, JobRecord job, IController controller) {
        // Create the dialog using the base class method
        JDialog dialog = createBaseDialog(parent, job, "Job Details");
        
        // Create and add content
        JPanel mainPanel = createContentPanel(job);
        dialog.add(mainPanel, BorderLayout.CENTER);
        
        // Create button panel with Yes/No options
        JPanel buttonPanel = createButtonPanel(dialog, parent, job, controller);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set dialog properties
        dialog.setSize(450, 580);
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
    
    /**
     * Creates the main content panel with job details and rating/comments fields.
     * 
     * @param job The job to display
     * @return The created content panel
     */
    private static JPanel createContentPanel(JobRecord job) {
        // Create and add the main content panel
        JPanel mainPanel = createMainContentPanel(job);
        
        // Get the content panel from the scroll pane
        JScrollPane scrollPane = (JScrollPane) mainPanel.getComponent(1);
        JPanel contentPanel = (JPanel) scrollPane.getViewport().getView();
        
        // Add rating and comments section
        JPanel ratingPanel = createRatingPanel(job);
        contentPanel.add(ratingPanel);
        
        // Create a save question panel
        JPanel confirmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel confirmLabel = new JLabel("Save this Job?");
        confirmLabel.setFont(confirmLabel.getFont().deriveFont(Font.BOLD));
        confirmPanel.add(confirmLabel);
        contentPanel.add(confirmPanel);
        
        return mainPanel;
    }
    
    /**
     * Creates a panel for the star rating and comments input.
     * 
     * @param job The job to use for initial values
     * @return The created rating panel
     */
    private static JPanel createRatingPanel(JobRecord job) {
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
        
        // Store the components in the client properties for later access
        ratingPanel.putClientProperty("starRating", starRating);
        ratingPanel.putClientProperty("commentsArea", commentsArea);
        
        return ratingPanel;
    }
    
    /**
     * Creates the button panel with Yes/No buttons.
     * 
     * @param dialog The parent dialog
     * @param parentComponent The original parent component
     * @param job The job to save
     * @param controller The controller
     * @return The created button panel
     */
    private static JPanel createButtonPanel(JDialog dialog, Component parentComponent, 
                                           JobRecord job, IController controller) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        
        // Set consistent size for buttons
        int buttonWidth = 100;
        int buttonHeight = Math.max(yesButton.getPreferredSize().height, noButton.getPreferredSize().height);
        
        yesButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        noButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        
        // Set up button actions
        yesButton.addActionListener(e -> handleSaveAction(dialog, parentComponent, job, controller));
        noButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        
        return buttonPanel;
    }
    
    /**
     * Handles the save action when the user clicks "Yes".
     * 
     * @param dialog The parent dialog
     * @param parentComponent The original parent component
     * @param job The job to save
     * @param controller The controller
     */
    private static void handleSaveAction(JDialog dialog, Component parentComponent, 
                                        JobRecord job, IController controller) {
        // Check if the job is already saved
        if (controller.isJobAlreadySaved(job)) {
            // Show message that the job is already saved
            JOptionPane.showMessageDialog(
                dialog,
                "This job is already saved.",
                "Job Already Saved",
                JOptionPane.INFORMATION_MESSAGE,
                INFO_ICON
            );
            dialog.dispose();
            return;
        }
        
        // Get the rating panel from the dialog
        JPanel mainPanel = (JPanel) dialog.getContentPane().getComponent(0);
        JScrollPane scrollPane = (JScrollPane) mainPanel.getComponent(1);
        JPanel contentPanel = (JPanel) scrollPane.getViewport().getView();
        JPanel ratingPanel = (JPanel) contentPanel.getComponent(contentPanel.getComponentCount() - 2);
        
        // Get the components from the client properties
        StarRatingPanel starRating = (StarRatingPanel) ratingPanel.getClientProperty("starRating");
        JTextArea commentsArea = (JTextArea) ratingPanel.getClientProperty("commentsArea");
        
        // Get the rating and comments from UI
        int newRating = starRating.getRating();
        String commentText = commentsArea.getText().isEmpty() ? "No comments provided" : commentsArea.getText();
        
        // First, add the job to the model through the controller
        controller.job2SavedList(job);
        
        // Then update the job with the rating and comments through the controller
        if (controller instanceof MainController cont) {
            cont.getUpdateJob(job.id(), commentText, newRating);
        }
        
        // Show success message
        JOptionPane.showMessageDialog(
            dialog,
            "Job saved successfully!",
            "Job Saved",
            JOptionPane.INFORMATION_MESSAGE,
            SUCCESS_ICON
        );
        
        // Switch to "Saved Jobs" tab after adding a job
        JobActionHelper.switchToSavedJobsTab(parentComponent, controller);
        
        dialog.dispose();
    }
}
