package skillzhunter.view;

import java.awt.Component;
import javax.swing.*;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;

/**
 * Helper class for common job-related actions across different views.
 * This utility class provides static methods for saving, editing, and deleting jobs,
 * as well as navigation functionality between different tabs in the application.
 * It serves as a centralized location for job-related UI interactions.
 */
public final class JobActionHelper {

    /** Icon for edit operations, loaded from resources. */
    private static final ImageIcon EDIT_ICON = IconLoader.loadIcon("images/edit.png", 24, 24);
    
    /** Icon for delete operations, loaded from resources. */
    private static final ImageIcon DELETE_ICON = IconLoader.loadIcon("images/delete.png", 24, 24);
    
    /** Warning icon for alert dialogs, loaded from resources. */
    private static final ImageIcon WARNING_ICON = IconLoader.loadIcon("images/warning.png");
    
    /** Success icon for confirmation dialogs, loaded from resources. */
    private static final ImageIcon SUCCESS_ICON = IconLoader.loadIcon("images/success.png");

    /** Information icon for informational dialogs, loaded from resources. */
    private static final ImageIcon INFO_ICON = IconLoader.loadIcon("images/info.png");

    /**
     * Private constructor to prevent instantiation of this utility class.
     * 
     * @throws UnsupportedOperationException if an attempt is made to instantiate this class
     */
    private JobActionHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Saves a job to the saved jobs list with the provided comments and rating.
     * Displays a success message and switches to the Saved Jobs tab.
     * 
     * @param job the JobRecord to be saved
     * @param comments user comments for the job, can be null or empty
     * @param rating user rating for the job
     * @param controller the controller to handle the save operation
     * @param parent the parent component for dialog display
     */
    public static void saveJob(JobRecord job, String comments, int rating, 
                               IController controller, Component parent) {
        controller.job2SavedList(job);
        controller.getUpdateJob(job.id(), 
            comments != null && !comments.isEmpty() ? comments : "No comments provided", 
            rating);

        if (parent != null) {
            JOptionPane.showMessageDialog(parent,
                "Job saved successfully!",
                "Job Saved",
                JOptionPane.INFORMATION_MESSAGE,
                SUCCESS_ICON);
        }

        switchToSavedJobsTab(parent, controller);
    }

    /**
     * Opens a dialog to edit an existing job with comments and rating.
     * If confirmed, updates the job in the system and shows a success message.
     * 
     * @param job the JobRecord to be edited
     * @param controller the controller to handle the update operation
     * @param parent the parent component for dialog display
     * @return the updated JobRecord if edited successfully, or null if canceled
     */
    public static JobRecord editJob(JobRecord job, IController controller, Component parent) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);
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
            parentFrame,
            editPanel,
            "Edit Job: " + job.jobTitle(),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            EDIT_ICON
        );

        if (result == JOptionPane.OK_OPTION) {
            JobRecord updatedJob = controller.getUpdateJob(
                job.id(),
                commentsArea.getText(),
                starRating.getRating()
            );

            if (updatedJob != null) {
                JOptionPane.showMessageDialog(
                    parentFrame,
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
     * Displays a confirmation dialog to delete a job and processes the deletion
     * if confirmed by the user.
     * 
     * @param job the JobRecord to be deleted
     * @param controller the controller to handle the deletion operation
     * @param parent the parent component for dialog display
     * @return true if the job was deleted, false if the operation was canceled
     */
    public static boolean deleteJob(JobRecord job, IController controller, Component parent) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);

        Object[] options = {"Delete", "Cancel"};
        int result = JOptionPane.showOptionDialog(
            parentFrame,
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
     * Shows a warning message when no job is selected but an action requires a selection.
     * 
     * @param message the warning message to display
     * @param parent the parent component for dialog display
     */
    public static void showNoSelectionMessage(String message, Component parent) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);

        JOptionPane.showMessageDialog(
            parentFrame,
            message,
            "No Selection",
            JOptionPane.WARNING_MESSAGE,
            WARNING_ICON
        );
    }

    /**
     * Updates the Saved Jobs tab view with the current list of saved jobs from the controller.
     * Handles different component types and traverses component hierarchy to find and update
     * the SavedJobsTab component.
     * 
     * @param component the component from which to start traversing the component hierarchy
     * @param controller the controller to retrieve the updated list of saved jobs
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
     * Switches the application view to the Saved Jobs tab and updates its content.
     * Locates the tabbed pane in the component hierarchy and selects the tab
     * with the title "Saved Jobs".
     * 
     * @param component the component from which to start traversing the component hierarchy
     * @param controller the controller to retrieve the updated list of saved jobs
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
     * Recursively searches for a JTabbedPane in the component hierarchy.
     * 
     * @param component the component from which to start the search
     * @return the first JTabbedPane found in the hierarchy, or null if none is found
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
