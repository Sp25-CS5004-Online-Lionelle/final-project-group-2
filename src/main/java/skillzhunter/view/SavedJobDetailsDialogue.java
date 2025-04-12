package skillzhunter.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
    
    // Standard button dimensions
    private static final int BUTTON_WIDTH = 100;
    
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
        if (!validateInputs(parent, job, controller)) {
            return;
        }
        
        // Create and configure the dialog
        JDialog dialog = createBaseDialog(parent, job, "Job Details");
        
        // Create and add content panel
        JPanel mainPanel = createMainContentPanel(job);
        dialog.add(mainPanel, BorderLayout.CENTER);
        
        // Create and add button panel
        JPanel buttonPanel = createButtonPanel(dialog, job, controller, parent);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set dialog properties and display
        configureDialogProperties(dialog, parent);
    }
    
    /**
     * Validates that required inputs are provided.
     * 
     * @param parent The parent component
     * @param job The job to display
     * @param controller The controller
     * @return true if inputs are valid, false otherwise
     */
    private static boolean validateInputs(Component parent, JobRecord job, IController controller) {
        if (job == null || controller == null) {
            JOptionPane.showMessageDialog(parent,
                "Job or controller not provided.",
                "Error",
                JOptionPane.INFORMATION_MESSAGE,
                WARNING_ICON);
            return false;
        }
        return true;
    }
    
    /**
     * Creates the button panel with Edit, Delete, and Close buttons.
     * 
     * @param dialog The parent dialog
     * @param job The job record
     * @param controller The controller
     * @param parent The parent component
     * @return The created button panel
     */
    private static JPanel createButtonPanel(JDialog dialog, JobRecord job, IController controller, Component parent) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Create themed buttons
        ThemedButton editButton = createButton("Edit", ThemedButton.ButtonType.INFO, EDIT_ICON);
        ThemedButton deleteButton = createButton("Delete", ThemedButton.ButtonType.DANGER, DELETE_ICON);
        ThemedButton closeButton = createButton("Close", ThemedButton.ButtonType.SECONDARY, CLOSE_ICON);
        
        // Set consistent button sizes
        int buttonHeight = calculateButtonHeight(editButton, deleteButton, closeButton);
        setButtonSizes(editButton, deleteButton, closeButton, buttonHeight);
        
        // Add buttons to panel
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);
        
        // Set button actions
        configureButtonActions(dialog, editButton, deleteButton, closeButton, job, controller, parent);
        
        return buttonPanel;
    }
    
    /**
     * Creates a themed button with specified text, type, and icon.
     * 
     * @param text The button text
     * @param buttonType The button type
     * @param icon The button icon
     * @return The created button
     */
    private static ThemedButton createButton(String text, ThemedButton.ButtonType buttonType, ImageIcon icon) {
        ThemedButton button = new ThemedButton(text, buttonType);
        button.setIcon(icon);
        button.setHorizontalTextPosition(SwingConstants.LEFT);
        button.setIconTextGap(5);
        return button;
    }
    
    /**
     * Calculates the maximum height required for the buttons.
     * 
     * @param buttons The buttons to calculate height for
     * @return The maximum height
     */
    private static int calculateButtonHeight(ThemedButton... buttons) {
        int maxHeight = 0;
        for (ThemedButton button : buttons) {
            maxHeight = Math.max(maxHeight, button.getPreferredSize().height);
        }
        return maxHeight;
    }
    
    /**
     * Sets consistent size for all buttons.
     * 
     * @param buttons The buttons to size
     * @param height The height to set
     */
    private static void setButtonSizes(ThemedButton editButton, ThemedButton deleteButton, 
                                      ThemedButton closeButton, int height) {
        editButton.setPreferredSize(new Dimension(BUTTON_WIDTH, height));
        deleteButton.setPreferredSize(new Dimension(BUTTON_WIDTH, height));
        closeButton.setPreferredSize(new Dimension(BUTTON_WIDTH, height));
    }
    
    /**
     * Configures the action listeners for the buttons.
     * 
     * @param dialog The parent dialog
     * @param editButton The edit button
     * @param deleteButton The delete button
     * @param closeButton The close button
     * @param job The job record
     * @param controller The controller
     * @param parent The parent component
     */
    private static void configureButtonActions(JDialog dialog, ThemedButton editButton, 
                                              ThemedButton deleteButton, ThemedButton closeButton,
                                              JobRecord job, IController controller, Component parent) {
        // Edit button action
        editButton.addActionListener(e -> {
            dialog.dispose(); // Close dialog first
            JobActionHelper.editJob(job, controller, parent); // Use helper method to edit
        });
        
        // Delete button action
        deleteButton.addActionListener(e -> {
            if (JobActionHelper.deleteJob(job, controller, parent)) {
                dialog.dispose(); // Only close the dialog if delete was confirmed
            }
        });
        
        // Close button action
        closeButton.addActionListener(e -> dialog.dispose());
    }
    
    /**
     * Configures and displays the dialog.
     * 
     * @param dialog The dialog to configure
     * @param parent The parent component
     */
    private static void configureDialogProperties(JDialog dialog, Component parent) {
        dialog.setSize(450, 450);
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}