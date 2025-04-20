package skillzhunter.view;

import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;

public class SavedJobsTab extends JobView {
    
    /** Map of buttons for easy reference and theme application. */
    private Map<String, ThemedButton> buttons = new HashMap<>();
    
    /** Map of icons used in this tab. */
    private Map<String, ImageIcon> icons = new HashMap<>();

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
        
        // Load icons with a loop
        String[] iconNames = {"open", "saveIcon", "exportIcon", "warning", "success", "edit", "delete"};
        String[] iconKeys = {"OPEN", "SAVE", "EXPORT", "WARNING", "SUCCESS", "EDIT", "DELETE"};
        
        for (int i = 0; i < iconNames.length; i++) {
            icons.put(iconKeys[i], IconLoader.loadIcon("images/" + iconNames[i] + ".png"));
        }
        
        super.initView();
        updateJobsList(savedJobs);
    }
    
    /**
     * Creates the top button panel.
     * This is intentionally left blank for this tab.
     *
     * @return An empty panel for the top row
     */
    @Override
    public JPanel makeTopButtonPanel() {
        //Blank top row override
        JPanel topRow = new JPanel();
        return topRow;
    }

    /**
     * Creates the bottom button panel that contains action buttons.
     * Includes Open, Edit, Delete, Save and Export buttons with their respective actions.
     *
     * @return The panel containing action buttons
     */
    @Override
    public JPanel makeBottomButtonPanel() {
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Button configuration data: name, type, icon, action method
        Object[][] buttonConfig = {
            {"Open", ThemedButton.ButtonType.PRIMARY, "OPEN", "openSelectedJob"},
            {"Edit", ThemedButton.ButtonType.INFO, "EDIT", "editSelectedJob"},
            {"Delete", ThemedButton.ButtonType.DANGER, "DELETE", "deleteSelectedJob"},
            {"Save", ThemedButton.ButtonType.SUCCESS, "SAVE", "handleSaveAction"},
            {"Export", ThemedButton.ButtonType.SECONDARY, "EXPORT", "handleExportAction"}
        };
        
        // Create buttons with a loop
        for (Object[] config : buttonConfig) {
            String name = (String) config[0];
            ThemedButton.ButtonType type = (ThemedButton.ButtonType) config[1];
            String iconKey = (String) config[2];
            
            ThemedButton button = createThemedButton(name, type);
            button.setIcon(icons.get(iconKey));
            button.setHorizontalTextPosition(SwingConstants.LEFT);
            button.setIconTextGap(5);
            
            // Store button in map
            buttons.put(name.toUpperCase(), button);
        }

        // Create a dropdown for export formats
        String[] formats = {"CSV", "JSON", "XML"};
        JComboBox<String> formatDropdown = new JComboBox<>(formats);

        // Get max button height for consistent sizing
        int buttonWidth = 100;  // Fixed width for all buttons
        int buttonHeight = 0;
        for (ThemedButton button : buttons.values()) {
            buttonHeight = Math.max(buttonHeight, button.getPreferredSize().height);
        }
        
        // Set consistent size for all buttons
        for (ThemedButton button : buttons.values()) {
            button.setPreferredSize(new java.awt.Dimension(buttonWidth, buttonHeight));
        }

        // Add buttons to panel in order
        for (String buttonName : new String[]{"OPEN", "EDIT", "DELETE", "SAVE", "EXPORT"}) {
            bottomRow.add(buttons.get(buttonName));
        }
        bottomRow.add(formatDropdown);

        // Set listeners with a more modular approach
        buttons.get("OPEN").addActionListener(e -> openSelectedJob());
        buttons.get("EDIT").addActionListener(e -> editSelectedJob());
        buttons.get("DELETE").addActionListener(e -> deleteSelectedJob());
        buttons.get("SAVE").addActionListener(e -> handleSaveAction());
        buttons.get("EXPORT").addActionListener(e -> handleExportAction(formatDropdown));

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
        // Use just the filename "SavedJobs.csv" which is passed into method with data directory
        String filePath = createDataDirectoryAndGetFilePath("SavedJobs.csv");
        
        // Clean jobs before saving
        List<JobRecord> cleanedJobs = cleanJobRecords(savedJobs);
        
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
        if (selectedFormat == null) { 
            return; 
        }

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
        if (filePath == null) { 
            return; 
        }
        
        // Clean jobs before exporting
        List<JobRecord> cleanedJobs = cleanJobRecords(savedJobs);
        
        // Export the jobs to the selected format
        exportJobsToFile(parentFrame, cleanedJobs, selectedFormat, filePath);
    }
    
    /**
     * Cleans a list of job records using the controller.
     * 
     * @param jobs The jobs to clean
     * @return A list of cleaned job records
     */
    private List<JobRecord> cleanJobRecords(List<JobRecord> jobs) {
        List<JobRecord> cleanedJobs = new ArrayList<>();
        // Create a cleaned version of the jobs by using controller cleanJobRecord
        for(JobRecord job : jobs){
            controller.cleanJobRecord(job);
            cleanedJobs.add(job);
        }
        return cleanedJobs;
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
            icons.get("WARNING"));
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
        
        optionPane.setIcon(icons.get("SAVE"));
        
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
        
        optionPane.setIcon(icons.get("EXPORT"));
        
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
        // FIXED: Extract just the file name without any directory paths
        String justFileName = new File(fileName).getName();
        
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            boolean created = dataDir.mkdirs();
            System.out.println("Created data directory: " + created);
        }
        
        // FIXED: Combine data directory with just the file name
        String filePath = new File(dataDir, justFileName).getAbsolutePath();
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
            controller.exportToFileType(jobs, format, filePath);
            
            // Verify that the file was created
            File savedFile = new File(filePath);
            System.out.println("File exists after save: " + savedFile.exists()
            + ", size: " + savedFile.length());
            
            // Extract just the filename from the path
            String fileName = savedFile.getName();
            
            // Use save icon for success message - showing only the filename
            JOptionPane.showMessageDialog(parentFrame,
                "Jobs successfully saved to " + fileName,
                "Save Complete",
                JOptionPane.INFORMATION_MESSAGE,
                icons.get("SUCCESS"));
        } catch (Exception ex) {
            // Show error message if save fails
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame,
                "Error saving jobs: " + ex.getMessage(),
                "Save Failed",
                JOptionPane.ERROR_MESSAGE,
                icons.get("WARNING"));
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
            controller.exportToFileType(jobs, format, filePath);
            
            // Show success message with export icon
            JOptionPane.showMessageDialog(parentFrame,
                "Jobs successfully exported in " + format + " format to:\n" + filePath,
                "Export Complete",
                JOptionPane.INFORMATION_MESSAGE,
                icons.get("EXPORT"));
        } catch (Exception ex) {
            // Show error message if export fails
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame,
                "Error exporting jobs: " + ex.getMessage(),
                "Export Failed",
                JOptionPane.ERROR_MESSAGE,
                icons.get("WARNING"));
        }
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

    /**
     * Applies the given color theme to all components in this tab.
     * 
     * @param theme The color theme to apply
     */
    @Override
    public void applyTheme(ColorTheme theme) {
        // Call parent implementation for common styling
        super.applyTheme(theme);
        
        // Apply theme to all buttons with a simple loop
        for (ThemedButton button : buttons.values()) {
            button.applyTheme(theme);
        }

        // Make sure changes are visible
        this.revalidate();
        this.repaint();
    }
}