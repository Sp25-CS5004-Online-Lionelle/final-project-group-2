package skillzhunter.view;

import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import skillzhunter.controller.IController;
import skillzhunter.model.JobBean;
import skillzhunter.model.JobRecord;

public class SavedJobsTab extends JobView {
    
    // Store buttons as fields to apply theme later
    /** open button. */
    private ThemedButton openButton;
    /** save button. */
    private ThemedButton saveButton;
    /** export button. */
    private ThemedButton exportButton;
    /** edit button. */
    private ThemedButton editButton;
    /** delete button. */
    private ThemedButton deleteButton;

    // Icons for buttons and dialogs
    /** open icon. */
    private final ImageIcon openIcon;
    /** save icon. */
    private final ImageIcon saveIcon;
    /** export icon. */
    private final ImageIcon exportIcon;
    /** warning icon. */
    private final ImageIcon warningIcon;
    /** success icon. */
    private final ImageIcon successIcon;
    /** edit icon. */
    private final ImageIcon editIcon;
    /** delete icon. */
    private final ImageIcon deleteIcon;

    /**
     * Constructor for SavedJobsTab.
     * Initializes the view with the controller and saved jobs.
     * 
     * @param controller The controller to interact with the model
     * @param savedJobs The list of saved jobs to display
     */
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
        this.editIcon = IconLoader.loadIcon("images/edit.png");
        this.deleteIcon = IconLoader.loadIcon("images/delete.png");
        
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
        openButton = createThemedButton("Open", ThemedButton.ButtonType.PRIMARY);
        openButton.setIcon(openIcon);
        openButton.setHorizontalTextPosition(SwingConstants.LEFT);
        openButton.setIconTextGap(5);
        
        // Edit button - using INFO type
        editButton = createThemedButton("Edit", ThemedButton.ButtonType.INFO);
        editButton.setIcon(editIcon);
        editButton.setHorizontalTextPosition(SwingConstants.LEFT);
        editButton.setIconTextGap(5);
        
        // Delete button - using DANGER type for destructive actions
        deleteButton = createThemedButton("Delete", ThemedButton.ButtonType.DANGER);
        deleteButton.setIcon(deleteIcon);
        deleteButton.setHorizontalTextPosition(SwingConstants.LEFT);
        deleteButton.setIconTextGap(5);
        
        // Save button - using SUCCESS type
        saveButton = createThemedButton("Save", ThemedButton.ButtonType.SUCCESS);
        saveButton.setIcon(saveIcon);
        saveButton.setHorizontalTextPosition(SwingConstants.LEFT);
        saveButton.setIconTextGap(5);
        
        // Export button - using SECONDARY type
        exportButton = createThemedButton("Export", ThemedButton.ButtonType.SECONDARY);
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

        // Set listeners - using helper methods for common actions
        openButton.addActionListener(e -> openSelectedJob());
        editButton.addActionListener(e -> editSelectedJob());
        deleteButton.addActionListener(e -> deleteSelectedJob());
        saveButton.addActionListener(e -> handleSaveAction());
        exportButton.addActionListener(e -> handleExportAction(formatDropdown));

        return bottomRow;
    }

    /**
     * Handles the save button action.
     * Shows confirmation dialog and saves jobs to CSV file.
     */
    private void handleSaveAction() {
        // Get the list of saved jobs
        List<JobRecord> savedJobs = controller.getSavedJobs();

        // Find the parent frame for centering dialogs
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        if (savedJobs.isEmpty()) {
            showNoJobsMessage(parentFrame, "save");
            return;
        }

        // Show confirmation dialog
        if (!showSaveConfirmDialog(parentFrame, savedJobs.size())) {
            return;
        }

        // Create a File object pointing to the data directory in the application's root
        String filePath = createDataDirectoryAndGetFilePath("SavedJobs.csv");
        
        // Create a cleaned version of the jobs for export
        List<JobRecord> cleanedJobs = cleanJobRecordsForExport(savedJobs);
        
        // Save the jobs to file
        saveJobsToFile(parentFrame, cleanedJobs, "CSV", filePath);
    }

    /**
     * Handles the export button action.
     * Shows confirmation dialog and exports jobs to selected format.
     * 
     * @param formatDropdown The dropdown with the selected export format
     */
    private void handleExportAction(JComboBox<String> formatDropdown) {
        // Get the selected format from the dropdown
        String selectedFormat = (String) formatDropdown.getSelectedItem();
        if (selectedFormat == null) return;

        // Get the list of saved jobs
        List<JobRecord> savedJobs = controller.getSavedJobs();

        // Find the parent frame for centering dialogs
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        if (savedJobs.isEmpty()) {
            showNoJobsMessage(parentFrame, "export");
            return;
        }

        // Show confirmation dialog
        if (!showExportConfirmDialog(parentFrame, selectedFormat)) {
            return;
        }

        // Get the export file path from user
        String filePath = getExportFilePath(parentFrame, selectedFormat);
        if (filePath == null) return;
        
        // Create clean version of jobs for export
        List<JobRecord> cleanedJobs = cleanJobRecordsForExport(savedJobs);
        
        // Export the jobs to the selected format
        exportJobsToFile(parentFrame, cleanedJobs, selectedFormat, filePath);
    }

    /**
     * Shows a message when there are no jobs to save/export.
     * 
     * @param parentFrame The parent frame for the dialog
     * @param action The action being performed ("save" or "export")
     */
    private void showNoJobsMessage(JFrame parentFrame, String action) {
        JOptionPane.showMessageDialog(parentFrame,
            "No jobs to " + action + ".",
            action.substring(0, 1).toUpperCase() + action.substring(1) + " Jobs",
            JOptionPane.INFORMATION_MESSAGE,
            warningIcon);
    }

    /**
     * Shows a confirmation dialog for saving jobs.
     * 
     * @param parentFrame The parent frame for the dialog
     * @param jobCount The number of jobs to save
     * @return true if the user confirms, false otherwise
     */
    private boolean showSaveConfirmDialog(JFrame parentFrame, int jobCount) {
        JOptionPane optionPane = new JOptionPane(
            "Are you sure you want to save " + jobCount + " job(s) to file?",
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_OPTION);
        
        optionPane.setIcon(saveIcon);
        
        JDialog dialog = optionPane.createDialog(parentFrame, "Confirm Save");
        dialog.setVisible(true);
        
        Object value = optionPane.getValue();
        return (value instanceof Integer) && (Integer) value == JOptionPane.YES_OPTION;
    }

    /**
     * Shows a confirmation dialog for exporting jobs.
     * 
     * @param parentFrame The parent frame for the dialog
     * @param format The export format
     * @return true if the user confirms, false otherwise
     */
    private boolean showExportConfirmDialog(JFrame parentFrame, String format) {
        JOptionPane optionPane = new JOptionPane(
            "Are you sure you want to export to " + format + "?",
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_OPTION);
        
        optionPane.setIcon(exportIcon);
        
        JDialog dialog = optionPane.createDialog(parentFrame, "Confirm Export");
        dialog.setVisible(true);
        
        Object value = optionPane.getValue();
        return (value instanceof Integer) && (Integer) value == JOptionPane.YES_OPTION;
    }

    /**
     * Creates the data directory if it doesn't exist and returns the absolute file path.
     * 
     * @param fileName The name of the file to create
     * @return The absolute path to the file
     */
    private String createDataDirectoryAndGetFilePath(String fileName) {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            boolean created = dataDir.mkdirs();
            System.out.println("Created data directory: " + created);
        }
        
        String filePath = new File(dataDir, fileName).getAbsolutePath();
        System.out.println("File path: " + filePath);
        
        return filePath;
    }

    /**
     * Gets a file path for exporting using a file dialog.
     * 
     * @param parentFrame The parent frame for the dialog
     * @param format The export format
     * @return The selected file path, or null if canceled
     */
    private String getExportFilePath(JFrame parentFrame, String format) {
        FileDialog fd = new FileDialog(parentFrame, "Save File", FileDialog.SAVE);
        fd.setFile("SavedJobs." + format.toLowerCase());
        fd.setVisible(true);

        if (fd.getFile() != null) {
            return fd.getDirectory() + fd.getFile();
        }
        
        return null;
    }

    /**
     * Saves jobs to a CSV file and shows appropriate messages.
     * 
     * @param parentFrame The parent frame for dialogs
     * @param jobs The jobs to save
     * @param format The format to save as
     * @param filePath The path to save to
     */
    private void saveJobsToFile(JFrame parentFrame, List<JobRecord> jobs, String format, String filePath) {
        try {
            // Pass the cleaned jobs to the controller
            controller.export2FileType(jobs, format, filePath);
            
            // Verify that the file was created
            File savedFile = new File(filePath);
            System.out.println("File exists after save: " + savedFile.exists() + 
                ", size: " + savedFile.length());
            
            // Extract just the filename from the path
            String fileName = savedFile.getName();
            
            // Use save icon for success message - showing only the filename
            JOptionPane.showMessageDialog(parentFrame,
                "Jobs successfully saved to " + fileName,
                "Save Complete",
                JOptionPane.INFORMATION_MESSAGE,
                successIcon);
        } catch (Exception ex) {
            // Show error message if save fails
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame,
                "Error saving jobs: " + ex.getMessage(),
                "Save Failed",
                JOptionPane.ERROR_MESSAGE,
                warningIcon);
        }
    }

    /**
     * Exports jobs to a file and shows appropriate messages.
     * 
     * @param parentFrame The parent frame for dialogs
     * @param jobs The jobs to export
     * @param format The format to export as
     * @param filePath The path to export to
     */
    private void exportJobsToFile(JFrame parentFrame, List<JobRecord> jobs, String format, String filePath) {
        try {
            // Pass the cleaned jobs to the controller
            controller.export2FileType(jobs, format, filePath);
            
            // Show success message with export icon
            JOptionPane.showMessageDialog(parentFrame,
                "Jobs successfully exported in " + format + " format to:\n" + filePath,
                "Export Complete",
                JOptionPane.INFORMATION_MESSAGE,
                exportIcon);
        } catch (Exception ex) {
            // Show error message if export fails
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame,
                "Error exporting jobs: " + ex.getMessage(),
                "Export Failed",
                JOptionPane.ERROR_MESSAGE,
                warningIcon);
        }
    }

    /**
     * Creates a cleaned copy of job records for export.
     * Removes HTML content and simplifies descriptions.
     * 
     * @param jobs The original job records
     * @return A new list of cleaned job records
     */
    private List<JobRecord> cleanJobRecordsForExport(List<JobRecord> jobs) {
        List<JobRecord> cleanedRecords = new ArrayList<>();
        
        for (JobRecord job : jobs) {
            // Create a new JobBean with cleaned data
            JobBean cleanedJob = new JobBean();
            
            // Copy basic fields directly
            cleanedJob.setId(job.id());
            cleanedJob.setUrl(job.url());
            cleanedJob.setJobSlug(job.jobSlug());
            cleanedJob.setJobTitle(job.jobTitle());
            cleanedJob.setCompanyName(job.companyName());
            cleanedJob.setCompanyLogo(job.companyLogo());
            cleanedJob.setJobGeo(job.jobGeo());
            cleanedJob.setJobLevel(job.jobLevel());
            cleanedJob.setPubDate(job.pubDate());
            cleanedJob.setAnnualSalaryMin(job.annualSalaryMin());
            cleanedJob.setAnnualSalaryMax(job.annualSalaryMax());
            cleanedJob.setSalaryCurrency(job.salaryCurrency());
            cleanedJob.setRating(job.rating());
            cleanedJob.setComments(job.comments());
            
            // Handle collection fields
            if (job.jobIndustry() != null) {
                cleanedJob.setJobIndustry(new ArrayList<>(job.jobIndustry()));
            } else {
                cleanedJob.setJobIndustry(new ArrayList<>());
            }
            
            if (job.jobType() != null) {
                cleanedJob.setJobType(new ArrayList<>(job.jobType()));
            } else {
                cleanedJob.setJobType(new ArrayList<>());
            }
            
            // Clean excerpt - extract first sentence without HTML
            String excerpt = "";
            if (job.jobExcerpt() != null) {
                excerpt = extractFirstSentence(stripHTML(job.jobExcerpt()));
            }
            cleanedJob.setJobExcerpt(excerpt);
            
            // Simplify job description
            String description = "Position at " + job.companyName() + " - " + job.jobTitle();
            cleanedJob.setJobDescription(description);
            
            // Add to the list of cleaned records
            cleanedRecords.add(cleanedJob.toRecord());
        }
        
        return cleanedRecords;
    }
    
    /**
     * Strips HTML tags from a string.
     * 
     * @param html The string possibly containing HTML tags
     * @return The string with HTML tags removed
     */
    private String stripHTML(String html) {
        if (html == null) return "";
        return html.replaceAll("<[^>]*>", "").replaceAll("\\s+", " ").trim();
    }
    
    /**
     * Extracts the first sentence from a text string.
     * 
     * @param text The text to extract from
     * @return The first sentence or a truncated version if no end marker found
     */
    private String extractFirstSentence(String text) {
        if (text == null || text.isEmpty()) return "";
        
        // Find the end of the first sentence
        int endPos = -1;
        for (String endMark : new String[]{".", "!", "?"}) {
            int pos = text.indexOf(endMark);
            if (pos >= 0 && (endPos == -1 || pos < endPos)) {
                endPos = pos + 1;
            }
        }
        
        // If a sentence end was found, return just that sentence
        if (endPos > 0) {
            return text.substring(0, endPos);
        }
        
        // Otherwise truncate to a reasonable length
        if (text.length() > 100) {
            return text.substring(0, 97) + "...";
        }
        
        return text;
    }

    /**
     * Opens the selected job for viewing.
     * Uses SavedJobDetailsDialogue directly for consistent behavior.
     */
    private void openSelectedJob() {
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx >= 0) {
            int modelIdx = jobsTable.convertRowIndexToModel(viewIdx);
            JobRecord selectedJob = jobsList.get(modelIdx);
            SavedJobDetailsDialogue.show(jobsTable, selectedJob, controller);
        } else {
            JobActionHelper.showNoSelectionMessage("Please select a job to open", this);
        }
    }
    
    /**
     * Edit the selected job using JobActionHelper.
     */
    private void editSelectedJob() {
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx >= 0) {
            int modelIdx = jobsTable.convertRowIndexToModel(viewIdx);
            JobRecord selectedJob = jobsList.get(modelIdx);
            
            // Use JobActionHelper to edit the job
            JobActionHelper.editJob(selectedJob, controller, this);
        } else {
            JobActionHelper.showNoSelectionMessage("Please select a job to edit", this);
        }
    }
    
    /**
     * Delete the selected job using JobActionHelper.
     */
    private void deleteSelectedJob() {
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx >= 0) {
            int modelIdx = jobsTable.convertRowIndexToModel(viewIdx);
            JobRecord selectedJob = jobsList.get(modelIdx);
            
            // Use JobActionHelper to delete the job
            if (JobActionHelper.deleteJob(selectedJob, controller, this)) {
                // Job was deleted successfully
                updateJobsList(controller.getSavedJobs());
            }
        } else {
            JobActionHelper.showNoSelectionMessage("Please select a job to delete", this);
        }
    }

    @Override
    public void applyTheme(ColorTheme theme) {
        // Call parent implementation for common styling
        super.applyTheme(theme);
        
        // Create a list of all buttons and apply theme to each
        List<ThemedButton> buttons = Arrays.asList(
            openButton, 
            saveButton, 
            exportButton, 
            editButton, 
            deleteButton
        );
        
        // Apply theme to all buttons
        for (ThemedButton button : buttons) {
            button.applyTheme(theme);
        }

        // Make sure changes are visible
        this.revalidate();
        this.repaint();
    }
}