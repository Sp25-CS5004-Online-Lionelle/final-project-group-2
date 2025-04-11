package skillzhunter.view;

import javax.swing.*;
import java.awt.*;

import skillzhunter.model.JobRecord;
import skillzhunter.controller.IController;
import skillzhunter.controller.MainController;

/**
 * Dialog for displaying saved job details with Edit/Delete/Close options.
 * This is used specifically when opening a job from the Saved Jobs tab.
 */
public class SavedJobDetailsDialogue extends BaseJobDetailsDialogue {
    
    // Icons for buttons and dialogs
    private static ImageIcon editIcon;
    private static ImageIcon deleteIcon;
    private static ImageIcon closeIcon;
    private static ImageIcon warningIcon;
    private static ImageIcon successIcon;
    
    // Initialize icons
    static {
        editIcon = IconLoader.loadIcon("images/edit.png", 24, 24);
        deleteIcon = IconLoader.loadIcon("images/delete.png", 24, 24);
        closeIcon = IconLoader.loadIcon("images/close.png", 24, 24);
        warningIcon = IconLoader.loadIcon("images/warning.png", 24, 24);
        successIcon = IconLoader.loadIcon("images/success.png", 24, 24);
    }
    
    /**
     * Shows a job details dialog with Edit, Delete, and Close buttons.
     * This is used when viewing jobs from the Saved Jobs tab.
     * 
     * @param parent The parent component
     * @param job The job to display
     * @param controller The controller
     */
    public static void show(Component parent, JobRecord job, IController controller) {
        if (job == null || controller == null) {
            JOptionPane.showMessageDialog(parent,
                "Job or controller not provided.",
                "Error",
                JOptionPane.INFORMATION_MESSAGE,
                warningIcon);
            return;
        }
        
        // Create the dialog using the base class method
        JDialog dialog = createBaseDialog(parent, job, "Job Details");
        
        // Create and add the main content panel
        JPanel mainPanel = createMainContentPanel(job);
        dialog.add(mainPanel, BorderLayout.CENTER);
        
        // Create button panel with appropriate options for saved jobs
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Create themed buttons
        ThemedButton editButton = new ThemedButton("Edit");
        editButton.setIcon(editIcon);
        editButton.setHorizontalTextPosition(SwingConstants.LEFT);
        editButton.setIconTextGap(5);
        
        ThemedButton deleteButton = new ThemedButton("Delete");
        deleteButton.setIcon(deleteIcon);
        deleteButton.setHorizontalTextPosition(SwingConstants.LEFT);
        deleteButton.setIconTextGap(5);
        
        ThemedButton closeButton = new ThemedButton("Close");
        closeButton.setIcon(closeIcon);
        closeButton.setHorizontalTextPosition(SwingConstants.LEFT);
        closeButton.setIconTextGap(5);
        
        // Set consistent size for all buttons
        int buttonWidth = 100;
        int buttonHeight = Math.max(
            editButton.getPreferredSize().height,
            Math.max(
                deleteButton.getPreferredSize().height,
                closeButton.getPreferredSize().height
            )
        );
        
        editButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        deleteButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        closeButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        
        // Add buttons to panel
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);
        
        // Add button panel to dialog
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set button actions
        editButton.addActionListener(e -> {
            dialog.dispose();
            
            // Use the same edit dialog as SavedJobsTab
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);
            
            // Create a panel for editing
            JPanel editPanel = new JPanel();
            editPanel.setLayout(new java.awt.BorderLayout(10, 10));
            
            // Create star rating panel with rating from job object
            StarRatingPanel starRating = new StarRatingPanel(job.rating() > 0 ? job.rating() : 0);
            editPanel.add(starRating, java.awt.BorderLayout.NORTH);
            
            // Create comments text area
            javax.swing.JTextArea commentsArea = new javax.swing.JTextArea(5, 20);
            commentsArea.setLineWrap(true);
            commentsArea.setWrapStyleWord(true);
            
            // Set existing comments if available
            if (job.comments() != null && !job.comments().isEmpty() && !job.comments().equals("No comments provided")) {
                commentsArea.setText(job.comments());
            }
            
            javax.swing.JScrollPane commentScrollPane = new javax.swing.JScrollPane(commentsArea);
            commentScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Your Comments"));
            editPanel.add(commentScrollPane, java.awt.BorderLayout.CENTER);
            
            // Show the edit dialog
            int result = JOptionPane.showConfirmDialog(
                parentFrame,
                editPanel,
                "Edit Job: " + job.jobTitle(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                editIcon
            );
            
            // Process the result
            if (result == JOptionPane.OK_OPTION) {
                // Use the controller to update the job
                if (controller instanceof MainController) {
                    MainController mainController = (MainController) controller;
                    
                    // Get the values from the UI
                    final int finalRating = starRating.getRating();
                    final String finalComments = commentsArea.getText();
                    
                    // Update through the controller
                    JobRecord updatedJob = mainController.getUpdateJob(job.id(), finalComments, finalRating);
                    
                    if (updatedJob != null) {
                        // Show confirmation
                        JOptionPane.showMessageDialog(
                            parentFrame,
                            "Job updated successfully!",
                            "Update Complete",
                            JOptionPane.INFORMATION_MESSAGE,
                            successIcon
                        );
                        
                        // Update SavedJobsTab if needed
                        if (parent instanceof JTable) {
                            Component comp = parent;
                            while (comp != null && !(comp instanceof SavedJobsTab)) {
                                comp = comp.getParent();
                            }
                            
                            if (comp instanceof SavedJobsTab) {
                                // Update the jobs list in the view to reflect changes
                                ((SavedJobsTab) comp).updateJobsList(controller.getSavedJobs());
                            }
                        }
                    }
                }
            }
        });
        
        deleteButton.addActionListener(e -> {
            // Find the parent frame for centering dialogs
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);
            
            // Create a confirm dialog
            Object[] options = {"Delete", "Cancel"};
            int result = JOptionPane.showOptionDialog(
                parentFrame,
                "Are you sure you want to delete the job: " + job.jobTitle() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                deleteIcon,
                options,
                options[1]  // Default is Cancel
            );
            
            if (result == JOptionPane.YES_OPTION) {
                // Use the controller to remove the job
                controller.getRemoveJob(job.id());
                
                // Get the SavedJobsTab to update its list
                if (parent instanceof JTable) {
                    Component comp = parent;
                    while (comp != null && !(comp instanceof SavedJobsTab)) {
                        comp = comp.getParent();
                    }
                    
                    if (comp instanceof SavedJobsTab) {
                        // Update the jobs list in the view to reflect changes
                        ((SavedJobsTab) comp).updateJobsList(controller.getSavedJobs());
                    }
                }
                
                // Show confirmation
                JOptionPane.showMessageDialog(
                    parentFrame,
                    "Job deleted successfully!",
                    "Delete Complete",
                    JOptionPane.INFORMATION_MESSAGE,
                    successIcon
                );
                dialog.dispose();
            }
        });
        
        closeButton.addActionListener(e -> dialog.dispose());
        
        // Set dialog properties
        dialog.setSize(450, 450);
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}