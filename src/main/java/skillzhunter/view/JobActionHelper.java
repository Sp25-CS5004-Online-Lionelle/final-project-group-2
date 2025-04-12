package skillzhunter.view;

import java.awt.Component;
import javax.swing.*;

import skillzhunter.controller.IController;
import skillzhunter.controller.MainController;
import skillzhunter.model.JobRecord;

/**
 * Helper class for common job-related actions across different views.
 */
public final class JobActionHelper {

    private static final ImageIcon EDIT_ICON = IconLoader.loadIcon("images/edit.png", 24, 24);
    private static final ImageIcon DELETE_ICON = IconLoader.loadIcon("images/delete.png", 24, 24);
    private static final ImageIcon WARNING_ICON = IconLoader.loadIcon("images/warning.png");
    private static final ImageIcon SUCCESS_ICON = IconLoader.loadIcon("images/success.png");

    private JobActionHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void saveJob(JobRecord job, String comments, int rating, 
                               IController controller, Component parent) {
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
    }

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

        if (result == JOptionPane.OK_OPTION && controller instanceof MainController mainController) {
            JobRecord updatedJob = mainController.getUpdateJob(
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
