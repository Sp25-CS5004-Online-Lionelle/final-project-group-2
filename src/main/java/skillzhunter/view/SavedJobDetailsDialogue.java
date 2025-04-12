package skillzhunter.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;

/**
 * Dialog for displaying saved job details with Edit/Delete/Close options.
 * This is used specifically when opening a job from the Saved Jobs tab.
 */
public class SavedJobDetailsDialogue extends BaseJobDetailsDialogue {
    
    // Icons for buttons and dialogs
    /** edit icon. */
    private static final ImageIcon EDIT_ICON;
    /** delete icon. */
    private static final ImageIcon DELETE_ICON;
    /** close icon. */
    private static final ImageIcon CLOSE_ICON;
    /** warning icon. */
    private static final ImageIcon WARNING_ICON;
    
    // Initialize icons
    static {
        EDIT_ICON = IconLoader.loadIcon("images/edit.png", 24, 24);
        DELETE_ICON = IconLoader.loadIcon("images/delete.png", 24, 24);
        CLOSE_ICON = IconLoader.loadIcon("images/close.png", 24, 24);
        WARNING_ICON = IconLoader.loadIcon("images/warning.png", 24, 24);
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
                WARNING_ICON);
            return;
        }
        
        // Create the dialog using the base class method
        JDialog dialog = createBaseDialog(parent, job, "Job Details");
        
        // Create and add the main content panel
        JPanel mainPanel = createMainContentPanel(job);
        dialog.add(mainPanel, BorderLayout.CENTER);
        
        // Create button panel with appropriate options for saved jobs
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Create themed buttons with different button types
        ThemedButton editButton = new ThemedButton("Edit", ThemedButton.ButtonType.INFO);
        editButton.setIcon(EDIT_ICON);
        editButton.setHorizontalTextPosition(SwingConstants.LEFT);
        editButton.setIconTextGap(5);
        
        ThemedButton deleteButton = new ThemedButton("Delete", ThemedButton.ButtonType.DANGER);
        deleteButton.setIcon(DELETE_ICON);
        deleteButton.setHorizontalTextPosition(SwingConstants.LEFT);
        deleteButton.setIconTextGap(5);
        
        ThemedButton closeButton = new ThemedButton("Close", ThemedButton.ButtonType.SECONDARY);
        closeButton.setIcon(CLOSE_ICON);
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
        
        // Set button actions using helper methods
        editButton.addActionListener(e -> {
            dialog.dispose(); // Close dialog first
            JobActionHelper.editJob(job, controller, parent); // Use helper method to edit
        });
        
        deleteButton.addActionListener(e -> {
            if (JobActionHelper.deleteJob(job, controller, parent)) {
                dialog.dispose(); // Only close the dialog if delete was confirmed
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