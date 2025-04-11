package skillzhunter.view;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.FlowLayout;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JFrame;
import skillzhunter.controller.IController;
import skillzhunter.controller.MainController;
import skillzhunter.model.JobRecord;

public class SavedJobsTab extends JobView {
    
    // Store buttons as fields to apply theme later
    private ThemedButton openButton;
    private ThemedButton saveButton;
    private ThemedButton exportButton;
    private ThemedButton editButton;   // New edit button
    private ThemedButton deleteButton; // New delete button

    // Icons for buttons and dialogs
    private ImageIcon openIcon;
    private ImageIcon saveIcon;
    private ImageIcon exportIcon;
    private ImageIcon warningIcon;
    private ImageIcon successIcon;
    private ImageIcon editIcon;      // New edit icon 
    private ImageIcon deleteIcon;    // New delete icon
    
    public SavedJobsTab(IController controller, List<JobRecord> savedJobs) {
        super();
        // set inherited field from jobview
        this.controller = controller;
        
        // Use IconLoader to load icons
        this.openIcon = IconLoader.loadIcon("images/open.png");
        this.saveIcon = IconLoader.loadIcon("images/saveIcon.png");
        this.exportIcon = IconLoader.loadIcon("images/exportIcon.png");
        this.warningIcon = IconLoader.loadIcon("images/warning.png");
        this.successIcon = IconLoader.loadIcon("images/success.png");
        this.editIcon = IconLoader.loadIcon("images/edit.png");      // Load edit icon
        this.deleteIcon = IconLoader.loadIcon("images/delete.png");  // Load delete icon
        
        super.initView();
        updateJobsList(savedJobs);
    }
    
    @Override
    public JPanel makeTopButtonPanel() {
        //Blank top row override
        JPanel topRow = new JPanel();
        return topRow;
    }

    @Override
    public JPanel makeBottomButtonPanel() {
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Create buttons and store them as fields with consistent sizing
        openButton = createThemedButton("Open");
        openButton.setIcon(openIcon);
        openButton.setHorizontalTextPosition(SwingConstants.LEFT);
        openButton.setIconTextGap(5);
        
        // New Edit button
        editButton = createThemedButton("Edit");
        editButton.setIcon(editIcon);
        editButton.setHorizontalTextPosition(SwingConstants.LEFT);
        editButton.setIconTextGap(5);
        
        // New Delete button
        deleteButton = createThemedButton("Delete");
        deleteButton.setIcon(deleteIcon);
        deleteButton.setHorizontalTextPosition(SwingConstants.LEFT);
        deleteButton.setIconTextGap(5);
        
        saveButton = createThemedButton("Save");  // Use createThemedButton for consistency
        saveButton.setIcon(saveIcon);
        saveButton.setHorizontalTextPosition(SwingConstants.LEFT);
        saveButton.setIconTextGap(5);
        
        exportButton = createThemedButton("Export");  // Use createThemedButton for consistency
        exportButton.setIcon(exportIcon);
        exportButton.setHorizontalTextPosition(SwingConstants.LEFT);
        exportButton.setIconTextGap(5);

        // Create a dropdown for export formats
        String[] formats = {"CSV", "JSON", "XML"};
        JComboBox<String> formatDropdown = new JComboBox<>(formats);

        // Set preferred size for all buttons to ensure consistency
        int buttonWidth = 100;  // Fixed width for all buttons
        int buttonHeight = Math.max(
            openButton.getPreferredSize().height,
            Math.max(
                editButton.getPreferredSize().height,
                Math.max(
                    deleteButton.getPreferredSize().height,
                    Math.max(
                        saveButton.getPreferredSize().height,
                        exportButton.getPreferredSize().height
                    )
                )
            )
        );
        
        openButton.setPreferredSize(new java.awt.Dimension(buttonWidth, buttonHeight));
        editButton.setPreferredSize(new java.awt.Dimension(buttonWidth, buttonHeight));
        deleteButton.setPreferredSize(new java.awt.Dimension(buttonWidth, buttonHeight));
        saveButton.setPreferredSize(new java.awt.Dimension(buttonWidth, buttonHeight));
        exportButton.setPreferredSize(new java.awt.Dimension(buttonWidth, buttonHeight));

        // Add buttons to panel
        bottomRow.add(openButton);
        bottomRow.add(editButton);
        bottomRow.add(deleteButton);
        bottomRow.add(saveButton);
        bottomRow.add(exportButton);
        bottomRow.add(formatDropdown);

        // Set listeners - using controller methods directly
        openButton.addActionListener(e -> openSelectedJob());
        
        // Edit button listener using controller
        editButton.addActionListener(e -> editSelectedJob());
        
        // Delete button listener using controller
        deleteButton.addActionListener(e -> deleteSelectedJob());
        
        saveButton.addActionListener(e -> {
            // Get the list of saved jobs
            List<JobRecord> savedJobs = controller.getSavedJobs();

            // Find the parent frame for centering dialogs
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

            if (savedJobs.isEmpty()) {
                // Use warning icon for empty list notification
                JOptionPane.showMessageDialog(parentFrame,
                    "No jobs to save.",
                    "Save Jobs",
                    JOptionPane.INFORMATION_MESSAGE,
                    warningIcon);
            } else {
                // Create a custom confirm dialog using JOptionPane
                JOptionPane optionPane = new JOptionPane(
                    "Are you sure you want to save " + savedJobs.size() + " job(s) to file?",
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.YES_NO_OPTION);
                
                // Set a custom icon for the dialog
                optionPane.setIcon(saveIcon);
                
                // Create and display the dialog
                JDialog dialog = optionPane.createDialog(parentFrame, "Confirm Save");
                dialog.setVisible(true);
                
                // Get the result
                Object value = optionPane.getValue();
                int result = (value instanceof Integer) ? (Integer) value : JOptionPane.CLOSED_OPTION;

                if (result == JOptionPane.YES_OPTION) {
                    // Save to data/SavedJobs.csv in a fixed location (overwrite each time)
                    String filePath = "data/SavedJobs.csv";
                    controller.path2CSV(filePath);
                    
                    // Use save icon for success message
                    JOptionPane.showMessageDialog(parentFrame,
                        "Jobs successfully saved to " + filePath,
                        "Save Complete",
                        JOptionPane.INFORMATION_MESSAGE,
                        successIcon);
                }
            }
        });

        exportButton.addActionListener(e -> {
            // Get the selected format from the dropdown
            String selectedFormat = (String) formatDropdown.getSelectedItem();

            // Get the list of saved jobs
            List<JobRecord> savedJobs = controller.getSavedJobs();

            // Find the parent frame for centering dialogs
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

            if (savedJobs.isEmpty()) {
                // Use warning icon for empty list notification
                JOptionPane.showMessageDialog(parentFrame,
                    "No jobs to export.",
                    "Export Jobs",
                    JOptionPane.INFORMATION_MESSAGE,
                    warningIcon);
                return;
            }

            if (selectedFormat != null) {
                // Create a custom confirm dialog using JOptionPane for export
                JOptionPane optionPane = new JOptionPane(
                    "Are you sure you want to export to " + selectedFormat + "?",
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.YES_NO_OPTION);
                
                // Set export icon for the dialog
                optionPane.setIcon(exportIcon);
                
                // Create and display the dialog
                JDialog dialog = optionPane.createDialog(parentFrame, "Confirm Export");
                dialog.setVisible(true);
                
                // Get the result
                Object value = optionPane.getValue();
                int result = (value instanceof Integer) ? (Integer) value : JOptionPane.CLOSED_OPTION;

                if (result == JOptionPane.YES_OPTION) {
                    // Export to a file
                    FileDialog fd = new FileDialog(parentFrame, "Save File", FileDialog.SAVE);
                    fd.setFile("SavedJobs." + selectedFormat.toLowerCase());
                    fd.setVisible(true);

                    // Get the selected file path
                    if (fd.getFile() != null) {
                        String filePath = fd.getDirectory() + fd.getFile();
                        controller.export2FileType(savedJobs, selectedFormat, filePath);

                        // Show success message with export icon
                        JOptionPane.showMessageDialog(parentFrame,
                            "Jobs successfully exported in " + selectedFormat + " format to:\n" + filePath,
                            "Export Complete",
                            JOptionPane.INFORMATION_MESSAGE,
                            exportIcon);
                    }
                }
            }
        });

        return bottomRow;
    }

    /**
     * Opens the selected job for viewing
     */
    private void openSelectedJob() {
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx >= 0) {
            int modelIdx = jobsTable.convertRowIndexToModel(viewIdx);
            JobRecord selectedJob = jobsList.get(modelIdx);
            SavedJobDetailsDialogue.show(jobsTable, selectedJob, controller);
        } else {
            showNoSelectionMessage("Please select a job to open");
        }
    }
    
    /**
     * Edit the selected job using the controller
     */
    private void editSelectedJob() {
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx >= 0) {
            int modelIdx = jobsTable.convertRowIndexToModel(viewIdx);
            JobRecord selectedJob = jobsList.get(modelIdx);
            
            // Open the job details dialog in edit mode
            showEditDialog(selectedJob);
        } else {
            showNoSelectionMessage("Please select a job to edit");
        }
    }
    
    /**
     * Shows a dialog to edit job rating and comments
     * Uses the controller's getUpdateJob method
     * 
     * @param job The job to edit
     */
    private void showEditDialog(JobRecord job) {
        // Find the parent frame for centering dialogs
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        
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
        
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(comments);
        scrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Your Comments"));
        editPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
        
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
    }
    
    /**
     * Delete the selected job using the controller
     */
    private void deleteSelectedJob() {
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx >= 0) {
            int modelIdx = jobsTable.convertRowIndexToModel(viewIdx);
            JobRecord selectedJob = jobsList.get(modelIdx);
            
            // Find the parent frame for centering dialogs
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            
            // Create a confirm dialog
            Object[] options = {"Delete", "Cancel"};
            int result = JOptionPane.showOptionDialog(
                parentFrame,
                "Are you sure you want to delete the job: " + selectedJob.jobTitle() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                deleteIcon,
                options,
                options[1]  // Default is Cancel
            );
            
            if (result == JOptionPane.YES_OPTION) {
                // Use the controller to remove the job
                controller.removeJobFromList(selectedJob.id());
                
                // Update the jobs list in the view to reflect changes
                updateJobsList(controller.getSavedJobs());
                
                // Show confirmation
                JOptionPane.showMessageDialog(
                    parentFrame,
                    "Job deleted successfully!",
                    "Delete Complete",
                    JOptionPane.INFORMATION_MESSAGE,
                    successIcon
                );
            }
        } else {
            showNoSelectionMessage("Please select a job to delete");
        }
    }
    
    /**
     * Shows a message when no row is selected
     * 
     * @param message The message to display
     */
    private void showNoSelectionMessage(String message) {
        // Find the parent frame for centering dialogs
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        
        // Show warning message
        JOptionPane.showMessageDialog(
            parentFrame,
            message,
            "No Selection",
            JOptionPane.WARNING_MESSAGE,
            warningIcon
        );
    }

    @Override
    public void applyTheme(ColorTheme theme) {
        // Call parent implementation for common styling
        super.applyTheme(theme);
        
        // Explicitly apply theme to our buttons
        if (openButton != null) {
            openButton.applyTheme(theme);
        }

        if (saveButton != null) {
            saveButton.applyTheme(theme);
        }

        if (exportButton != null) {
            exportButton.applyTheme(theme);
        }
        
        // Apply theme to new buttons
        if (editButton != null) {
            editButton.applyTheme(theme);
        }
        
        if (deleteButton != null) {
            deleteButton.applyTheme(theme);
        }

        // Make sure changes are visible
        this.revalidate();
        this.repaint();
    }
}