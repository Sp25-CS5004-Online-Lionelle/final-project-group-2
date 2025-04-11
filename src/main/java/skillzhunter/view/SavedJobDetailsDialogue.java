package skillzhunter.view;

import javax.swing.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.Font;
import java.awt.GridLayout;

import skillzhunter.model.JobRecord;
import skillzhunter.controller.IController;
import skillzhunter.controller.MainController;

/**
 * Dialog for displaying saved job details with Edit/Delete/Close options.
 * This is used specifically when opening a job from the Saved Jobs tab.
 */
public class SavedJobDetailsDialogue {
    
    // The size for company logos
    private static final int LOGO_WIDTH = 64;
    private static final int LOGO_HEIGHT = 64;
    
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
        
        // Create the dialog - properly get the parent window
        Window parentWindow = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog;
        
        if (parentWindow instanceof java.awt.Frame) {
            dialog = new JDialog((java.awt.Frame) parentWindow, "Job Details", true);
        } else if (parentWindow instanceof java.awt.Dialog) {
            dialog = new JDialog((java.awt.Dialog) parentWindow, "Job Details", true);
        } else {
            dialog = new JDialog(new JFrame(), "Job Details", true);
        }
        
        dialog.setLayout(new BorderLayout(0, 10));
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Create simple header panel
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        
        // Add logo at top, centered
        ImageIcon companyLogo = IconLoader.loadCompanyLogo(job.companyLogo(), LOGO_WIDTH, LOGO_HEIGHT, "images/idea.png");
        JLabel logoLabel = new JLabel(companyLogo);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(logoLabel, BorderLayout.CENTER);
        
        // Add header panel to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Add company name
        JLabel companyNameLabel = new JLabel(job.companyName(), SwingConstants.CENTER);
        companyNameLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        companyNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(companyNameLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Add job title
        JLabel titleLabel = new JLabel("<html><h2>" + job.jobTitle() + "</h2></html>", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Create a simple panel for job details with 2 columns
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        
        // Add job details
        if (job.jobGeo() != null && !job.jobGeo().isEmpty()) {
            addDetailRow(detailsPanel, "Location:", job.jobGeo());
        }
        
        if (job.jobIndustry() != null && !job.jobIndustry().isEmpty()) {
            String industries = String.join(", ", job.jobIndustry());
            addDetailRow(detailsPanel, "Industry:", industries);
        }
        
        if (job.jobType() != null && !job.jobType().isEmpty()) {
            String types = String.join(", ", job.jobType().stream()
                .map(type -> {
                    String[] parts = type.split("-");
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
                    }
                    return String.join("-", parts);
                }).toList());
            addDetailRow(detailsPanel, "Type:", types);
        }
        
        if (job.jobLevel() != null && !job.jobLevel().isEmpty()) {
            addDetailRow(detailsPanel, "Level:", job.jobLevel());
        }
        
        if ((job.annualSalaryMin() != 0 || job.annualSalaryMax() != 0)) {
            String salaryText = String.format("%s - %s",
                job.annualSalaryMin() == 0 ? "N/A" : String.format("%,d", job.annualSalaryMin()),
                job.annualSalaryMax() == 0 ? "N/A" : String.format("%,d", job.annualSalaryMax())
            );
            
            if (job.salaryCurrency() != null && !job.salaryCurrency().isEmpty()) {
                salaryText += " " + job.salaryCurrency();
            }
            
            addDetailRow(detailsPanel, "Salary:", salaryText);
        }
        
        if (job.pubDate() != null && !job.pubDate().isEmpty()) {
            addDetailRow(detailsPanel, "Published:", job.pubDate());
        }
        
        // Center the details panel
        JPanel detailsWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        detailsWrapper.add(detailsPanel);
        contentPanel.add(detailsWrapper);
        
        // Add content panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add the main panel to the dialog
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
            
            // Create star rating panel
            StarRatingPanel starRating = new StarRatingPanel(job.rating() > 0 ? job.rating() : 0);
            editPanel.add(starRating, java.awt.BorderLayout.NORTH);
            
            // Create comments text area
            javax.swing.JTextArea comments = new javax.swing.JTextArea(5, 20);
            comments.setLineWrap(true);
            comments.setWrapStyleWord(true);
            
            // Set existing comments if available
            if (job.comments() != null && !job.comments().isEmpty() && !job.comments().equals("No comments provided")) {
                comments.setText(job.comments());
            }
            
            javax.swing.JScrollPane commentScrollPane = new javax.swing.JScrollPane(comments);
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
                    final String finalComments = comments.getText();
                    
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
    
    /**
     * Helper method to add detail rows to the details panel
     */
    private static void addDetailRow(JPanel panel, String label, String value) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(labelComp.getFont().deriveFont(Font.BOLD));
        panel.add(labelComp);
        
        JLabel valueComp = new JLabel(value);
        panel.add(valueComp);
    }
}