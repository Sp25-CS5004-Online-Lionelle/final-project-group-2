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
import skillzhunter.model.JobRecord;

public class SavedJobsTab extends JobView {
    
    // Store buttons as fields to apply theme later
    private ThemedButton openButton;
    private ThemedButton saveButton;
    private ThemedButton exportButton;

    //Icons for buttons and dialogs
    private ImageIcon openIcon;
    private ImageIcon saveIcon;
    private ImageIcon exportIcon;
    private ImageIcon warningIcon;
    private ImageIcon successIcon;
    
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

        // Create save button and store it as field
        openButton = createThemedButton("Open Job");
        openButton.setIcon(openIcon);
        openButton.setHorizontalTextPosition(SwingConstants.LEFT);
        openButton.setIconTextGap(5); // optional: tweak spacing between text and icon
        saveButton = new ThemedButton("Save Jobs");
        saveButton.setIcon(saveIcon);
        saveButton.setHorizontalTextPosition(SwingConstants.LEFT);
        saveButton.setIconTextGap(5);
        exportButton = new ThemedButton("Export Saved List");
        exportButton.setIcon(exportIcon);
        exportButton.setHorizontalTextPosition(SwingConstants.LEFT);
        exportButton.setIconTextGap(5);

        // Create a dropdown for export formats
        String[] formats = {"CSV", "JSON", "XML"};
        JComboBox<String> formatDropdown = new JComboBox<>(formats);

        // Add buttons to panel
        bottomRow.add(openButton);
        bottomRow.add(saveButton);
        bottomRow.add(exportButton);
        bottomRow.add(formatDropdown);

        // Set listeners
        openButton.addActionListener(e -> openSelectedJob());
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
                    controller.getSavedJobsToCsv(filePath);
                    
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
                        controller.getExportSavedJobs(savedJobs, selectedFormat, filePath);

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

    private void openSelectedJob() {
        int viewIdx = jobsTable.getSelectedRow();
        if (viewIdx >= 0) {
            int modelIdx = jobsTable.convertRowIndexToModel(viewIdx);
            JobRecord selectedJob = jobsList.get(modelIdx);
            JobDetailsDialogue.showJobDetails(jobsTable, selectedJob, SavedJobsLists.getSavedJobs(), controller);
        }
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

        // Make sure changes are visible
        this.revalidate();
        this.repaint();
    }
}