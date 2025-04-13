package skillzhunter.view;

import java.awt.Component;
import java.awt.Window;
import javax.swing.*;

import skillzhunter.controller.IController;
import skillzhunter.controller.MainController;
import skillzhunter.model.JobRecord;

/**
 * Helper class for common job-related actions across different views.
 */
public final class JobActionHelper {

    // Common icons used across job actions
    private static final ImageIcon EDIT_ICON = IconLoader.loadIcon("images/edit.png", 24, 24);
    private static final ImageIcon DELETE_ICON = IconLoader.loadIcon("images/delete.png", 24, 24);
    private static final ImageIcon WARNING_ICON = IconLoader.loadIcon("images/warning.png");
    private static final ImageIcon SUCCESS_ICON = IconLoader.loadIcon("images/success.png");
    private static final ImageIcon INFO_ICON = IconLoader.loadIcon("images/info.png");

    private JobActionHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Saves a job with comments and rating.
     * 
     * @param job The job to save
     * @param comments The comments to save
     * @param rating The rating to save
     * @param controller The application controller
     * @param parent The parent component
     * @return true if the save was successful
     */
    public static boolean saveJob(JobRecord job, String comments, int rating, 
                               IController controller, Component parent) {
        if (job == null || controller == null) {
            showErrorMessage(parent, "Job or controller not provided.");
            return false;
        }
        
        // Check if job is already saved
        if (controller.isJobAlreadySaved(job)) {
            showInfoMessage(parent, "This job is already saved.", "Job Already Saved");
            return false;
        }

        controller.job2SavedList(job);

        if (controller instanceof MainController cont) {
            cont.getUpdateJob(job.id(), 
                comments != null && !comments.isEmpty() ? comments : "No comments provided", 
                rating);
        }

        if (parent != null) {
            JOptionPane.showMessageDialog(parent,
                "Job saved successfully!",
                "Job Saved",
                JOptionPane.INFORMATION_MESSAGE,
                SUCCESS_ICON);
        }

        switchToSavedJobsTab(parent, controller);
        return true;
    }

    /**
     * Shows dialog to edit a job's rating and comments.
     * 
     * @param job The job to edit
     * @param controller The application controller
     * @param parent The parent component
     * @return The updated job record, or null if edit was cancelled
     */
    public static JobRecord editJob(JobRecord job, IController controller, Component parent) {
        if (job == null || controller == null) {
            showErrorMessage(parent, "Job or controller not provided.");
            return null;
        }
        
        Window parentWindow = getParentWindow(parent);
        JPanel editPanel = new JPanel(new java.awt.BorderLayout(10, 10));

        StarRatingPanel starRating = new StarRatingPanel(Math.max(job.rating(), 0));
        editPanel.add(starRating, java.awt.BorderLayout.NORTH);

        JTextArea commentsArea = new JTextArea(5, 20);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);

        if (job.comments() != null && !job.comments().isEmpty() && !job.comments().equals("No comments provided")) {
            commentsArea.setText(job.comments());
        }

        JScrollPane commentScrollPane = new JScrollPane(commentsArea);
        commentScrollPane.setBorder(BorderFactory.createTitledBorder("Your Comments"));
        editPanel.add(commentScrollPane, java.awt.BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
            parentWindow,
            editPanel,
            "Edit Job: " + job.jobTitle(),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            EDIT_ICON
        );

        if (result == JOptionPane.OK_OPTION && controller instanceof MainController mainController) {
            JobRecord updatedJob = mainController.getUpdateJob(
                job.id(),
                commentsArea.getText(),
                starRating.getRating()
            );

            if (updatedJob != null) {
                JOptionPane.showMessageDialog(
                    parentWindow,
                    "Job updated successfully!",
                    "Update Complete",
                    JOptionPane.INFORMATION_MESSAGE,
                    SUCCESS_ICON
                );
                updateSavedJobsTabView(parent, controller);
            }

            return updatedJob;
        }

        return null;
    }

    /**
     * Shows confirmation dialog to delete a job.
     * 
     * @param job The job to delete
     * @param controller The application controller
     * @param parent The parent component
     * @return true if the job was deleted
     */
    public static boolean deleteJob(JobRecord job, IController controller, Component parent) {
        if (job == null || controller == null) {
            showErrorMessage(parent, "Job or controller not provided.");
            return false;
        }
        
        Window parentWindow = getParentWindow(parent);

        Object[] options = {"Delete", "Cancel"};
        int result = JOptionPane.showOptionDialog(
            parentWindow,
            "Are you sure you want to delete the job: " + job.jobTitle() + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE,
            DELETE_ICON,
            options,
            options[1]
        );

        if (result == JOptionPane.YES_OPTION) {
            controller.removeJobFromList(job.id());
            updateSavedJobsTabView(parent, controller);

            JOptionPane.showMessageDialog(
                parentWindow,
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
     * Shows a message when no job is selected.
     * 
     * @param message The message to display
     * @param parent The parent component
     */
    public static void showNoSelectionMessage(String message, Component parent) {
        Window parentWindow = getParentWindow(parent);

        JOptionPane.showMessageDialog(
            parentWindow,
            message,
            "No Selection",
            JOptionPane.WARNING_MESSAGE,
            WARNING_ICON
        );
    }
    
    /**
     * Shows an error message.
     * 
     * @param parent The parent component
     * @param message The error message
     */
    public static void showErrorMessage(Component parent, String message) {
        Window parentWindow = getParentWindow(parent);
        
        JOptionPane.showMessageDialog(
            parentWindow,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE,
            WARNING_ICON
        );
    }
    
    /**
     * Shows an information message.
     * 
     * @param parent The parent component
     * @param message The information message
     * @param title The dialog title
     */
    public static void showInfoMessage(Component parent, String message, String title) {
        Window parentWindow = getParentWindow(parent);
        
        JOptionPane.showMessageDialog(
            parentWindow,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE,
            INFO_ICON
        );
    }

    /**
     * Updates the Saved Jobs tab if visible.
     * 
     * @param component The context component
     * @param controller The application controller
     */
    public static void updateSavedJobsTabView(Component component, IController controller) {
        if (component instanceof JTable) {
            Component comp = component;
            while (comp != null && !(comp instanceof SavedJobsTab)) {
                comp = comp.getParent();
            }
            if (comp instanceof SavedJobsTab tab) {
                tab.updateJobsList(controller.getSavedJobs());
            }
        } else if (component instanceof SavedJobsTab tab) {
            tab.updateJobsList(controller.getSavedJobs());
        } else if (component instanceof JDialog dialog) {
            updateSavedJobsTabView(dialog.getParent(), controller);
        }
    }

    /**
     * Switches to the Saved Jobs tab and refreshes it.
     * 
     * @param component The context component
     * @param controller The application controller
     */
    public static void switchToSavedJobsTab(Component component, IController controller) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(component);
        if (frame != null) {
            JTabbedPane tabbedPane = findTabbedPane(frame.getContentPane());
            if (tabbedPane != null) {
                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    String title = tabbedPane.getTitleAt(i);
                    Component comp = tabbedPane.getTabComponentAt(i);
                    
                    if ("Saved Jobs".equals(title) 
                        || (comp instanceof JLabel label && "Saved Jobs".equals(label.getText()))) {
                        
                        tabbedPane.setSelectedIndex(i);
                        
                        Component tabComponent = tabbedPane.getComponentAt(i);
                        if (tabComponent instanceof SavedJobsTab tab) {
                            tab.updateJobsList(controller.getSavedJobs());
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Gets the parent window for a component.
     * 
     * @param component The component
     * @return The parent window, or null if not found
     */
    private static Window getParentWindow(Component component) {
        if (component == null) {
            return null;
        }
        
        if (component instanceof Window) {
            return (Window) component;
        }
        
        return SwingUtilities.getWindowAncestor(component);
    }

    /**
     * Recursively searches for a JTabbedPane in the component hierarchy.
     * 
     * @param component The component to search in
     * @return The found JTabbedPane, or null if not found
     */
    private static JTabbedPane findTabbedPane(Component component) {
        if (component instanceof JTabbedPane tabbedPane) {
            return tabbedPane;
        } else if (component instanceof java.awt.Container container) {
            for (int i = 0; i < container.getComponentCount(); i++) {
                JTabbedPane found = findTabbedPane(container.getComponent(i));
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}