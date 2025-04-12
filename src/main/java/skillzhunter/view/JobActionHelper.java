package skillzhunter.view;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import skillzhunter.controller.IController;
import skillzhunter.controller.MainController;
import skillzhunter.model.JobRecord;

/**
 * Helper class for common job-related actions across different views.
 * Consolidates duplicate functionality for saving, editing and deleting jobs.
 */
public final class JobActionHelper {
    
    // Icons for operations
    private static final ImageIcon EDIT_ICON = IconLoader.loadIcon("images/edit.png", 24, 24);
    private static final ImageIcon DELETE_ICON = IconLoader.loadIcon("images/delete.png", 24, 24);
    private static final ImageIcon WARNING_ICON = IconLoader.loadIcon("images/warning.png");
    private static final ImageIcon SUCCESS_ICON = IconLoader.loadIcon("images/success.png");
    
    /**
     * Private constructor to prevent instantiation.
     */
    private JobActionHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Saves a job to the saved jobs list and updates its rating and comments.
     * 
     * @param job The job to save
     * @param comments User comments for the job
     * @param rating User rating for the job
     * @param controller The controller to interact with
     * @param parent The parent component (for displaying messages)
     */
    public static void saveJob(JobRecord job, String comments, int rating, 
                               IController controller, Component parent) {
        // Add the job to saved jobs list
        controller.job2SavedList(job);
        
        // Update rating and comments if provided
        if (controller instanceof MainController cont) {
            cont.getUpdateJob(job.id(), 
                comments != null && !comments.isEmpty() ? comments : "No comments provided", 
                rating);
        }
        
        // Show confirmation if parent is provided
        if (parent != null) {
            JOptionPane.showMessageDialog(parent,
                "Job saved successfully!",
                "Job Saved",
                JOptionPane.INFORMATION_MESSAGE,
                SUCCESS_ICON);
        }
        
        // Switch to Saved Jobs tab
        switchToSavedJobsTab(parent, controller);
    }
    
    /**
     * Opens a dialog to edit a job's rating and comments.
     * 
     * @param job The job to edit
     * @param controller The controller to interact with
     * @param parent The parent component
     * @return The updated job record
     */
    public static JobRecord editJob(JobRecord job, IController controller, Component parent) {
        // Find the parent frame for centering dialogs
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);
        JobRecord updatedJob = null;
        
        // Create a panel for editing
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new java.awt.BorderLayout(10, 10));
        
        // Create star rating panel
        StarRatingPanel starRating = new StarRatingPanel(job.rating() > 0 ? job.rating() : 0);
        editPanel.add(starRating, java.awt.BorderLayout.NORTH);
        
        // Create comments text area
        JTextArea commentsArea = new JTextArea(5, 20);
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
            EDIT_ICON
        );
        
        // Process the result
        if (result == JOptionPane.OK_OPTION) {
            // Use the controller to update the job
            if (controller instanceof MainController mainController) {
                // Get the values from the UI
                final int finalRating = starRating.getRating();
                final String finalComments = commentsArea.getText();
                
                // Update through the controller
                updatedJob = mainController.getUpdateJob(job.id(), finalComments, finalRating);
                
                if (updatedJob != null) {
                    // Show confirmation
                    JOptionPane.showMessageDialog(
                        parentFrame,
                        "Job updated successfully!",
                        "Update Complete",
                        JOptionPane.INFORMATION_MESSAGE,
                        SUCCESS_ICON
                    );
                    
                    // Update any SavedJobsTab in the view hierarchy
                    updateSavedJobsTabView(parent, controller);
                }
            }
        }
        
        return updatedJob;
    }
    
    /**
     * Deletes a job from the saved jobs list.
     * 
     * @param job The job to delete
     * @param controller The controller to interact with
     * @param parent The parent component
     * @return true if the job was deleted, false otherwise
     */
    public static boolean deleteJob(JobRecord job, IController controller, Component parent) {
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
            DELETE_ICON,
            options,
            options[1]  // Default is Cancel
        );
        
        if (result == JOptionPane.YES_OPTION) {
            // Use the controller to remove the job
            controller.removeJobFromList(job.id());
            
            // Update any SavedJobsTab in the view hierarchy
            updateSavedJobsTabView(parent, controller);
            
            // Show confirmation
            JOptionPane.showMessageDialog(
                parentFrame,
                "Job deleted successfully!",
                "Delete Complete",
                JOptionPane.INFORMATION_MESSAGE,
                SUCCESS_ICON
            );
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Shows a message when no row is selected.
     * 
     * @param message The message to display
     * @param parent The parent component
     */
    public static void showNoSelectionMessage(String message, Component parent) {
        // Find the parent frame for centering dialogs
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);
        
        // Show warning message
        JOptionPane.showMessageDialog(
            parentFrame,
            message,
            "No Selection",
            JOptionPane.WARNING_MESSAGE,
            WARNING_ICON
        );
    }
    
    /**
     * Updates the job list in any SavedJobsTab found in the component hierarchy.
     * 
     * @param component The component to start searching from
     * @param controller The controller to get the job list from
     */
    public static void updateSavedJobsTabView(Component component, IController controller) {
        if (component instanceof JTable) {
            Component comp = component;
            while (comp != null && !(comp instanceof SavedJobsTab)) {
                comp = comp.getParent();
            }
            
            if (comp instanceof SavedJobsTab) {
                // Update the jobs list in the view to reflect changes
                ((SavedJobsTab) comp).updateJobsList(controller.getSavedJobs());
            }
        } else if (component instanceof SavedJobsTab) {
            ((SavedJobsTab) component).updateJobsList(controller.getSavedJobs());
        } else if (component instanceof JDialog) {
            // If it's a dialog, try the parent component
            Component parent = ((JDialog) component).getParent();
            updateSavedJobsTabView(parent, controller);
        }
    }
    
    /**
     * Switches to the Saved Jobs tab in the main window.
     * 
     * @param component The component to start searching from
     * @param controller The controller to get the job list from
     */
    public static void switchToSavedJobsTab(Component component, IController controller) {
        // Find the main window containing the tabs
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(component);
        if (frame != null) {
            // Look for the tabbed pane in the component hierarchy
            javax.swing.JTabbedPane tabbedPane = findTabbedPane(frame.getContentPane());
            if (tabbedPane != null) {
                // Find and select the "Saved Jobs" tab
                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    String title = tabbedPane.getTitleAt(i);
                    Component comp = tabbedPane.getTabComponentAt(i);
                    
                    if ("Saved Jobs".equals(title) 
                        || (comp instanceof JLabel && "Saved Jobs".equals(((JLabel) comp).getText()))) {
                        
                        // Select the tab
                        tabbedPane.setSelectedIndex(i);
                        
                        // Update the tab content if it's a SavedJobsTab
                        Component tabComponent = tabbedPane.getComponentAt(i);
                        if (tabComponent instanceof SavedJobsTab) {
                            ((SavedJobsTab) tabComponent).updateJobsList(controller.getSavedJobs());
                        }
                        
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Recursively searches for a JTabbedPane in the component hierarchy.
     * 
     * @param component The component to search in
     * @return The found JTabbedPane, or null if not found
     */
    private static javax.swing.JTabbedPane findTabbedPane(Component component) {
        if (component instanceof javax.swing.JTabbedPane) {
            return (javax.swing.JTabbedPane) component;
        } else if (component instanceof java.awt.Container) {
            java.awt.Container container = (java.awt.Container) component;
            for (int i = 0; i < container.getComponentCount(); i++) {
                javax.swing.JTabbedPane found = findTabbedPane(container.getComponent(i));
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}