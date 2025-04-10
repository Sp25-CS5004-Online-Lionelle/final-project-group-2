package skillzhunter.view;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
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

    //Open image for open job button
    private ImageIcon openIcon;
    
    public SavedJobsTab(IController controller, List<JobRecord> savedJobs) {
        super();
        // set inherited field from jobview
        this.controller = controller;
        super.initView();
        this.openIcon = loadIcon("images/open.png");
        updateJobsList(savedJobs);
    }

    /**
     * Loads an icon from the resources folder.
     *
     * @param path The path to the icon
     * @return The loaded icon, or null if it couldn't be loaded
     */
    private ImageIcon loadIcon(String path) {
        try {
            URL url = getClass().getClassLoader().getResource(path);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                // Scale the image to an appropriate size for the header
                Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + path);
        }

        return null;
    }
    @Override
    public JPanel makeTopButtonPanel() {
        JPanel topRow = new JPanel();
        
//        // Create the load button and store it as field
//        loadButton = new ThemedButton("Load Job List from File");
//        topRow.add(loadButton);
//
//        loadButton.addActionListener(e -> {
//            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(topRow);
//            FileDialog fd = new FileDialog(parentFrame, "Pick file to load", FileDialog.LOAD);
//            fd.setVisible(true);
//
//            String path = null;
//            if (fd.getFile() != null) {
//                File file = new File(fd.getDirectory(), fd.getFile());
//                path = file.getPath();
//            }
//
//            if (path != null) {
//                // You can wire this up later to actually read the file into the model
//                // For now, let's simulate grabbing the saved jobs from model
//                List<JobRecord> savedJobs = controller.getSavedJobs();
//            } else {
//                System.out.println("No file selected.");
//            }
//        });

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
        exportButton = new ThemedButton("Export Saved List");

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
                JOptionPane.showMessageDialog(parentFrame, 
                    "No jobs to save.", 
                    "Save Jobs", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Ask for confirmation before saving - use parentFrame for centering
                int result = JOptionPane.showConfirmDialog(
                    parentFrame,
                    "Are you sure you want to save " + savedJobs.size() + " job(s) to file?",
                    "Confirm Save",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (result == JOptionPane.YES_OPTION) {
                    // Save to data/SavedJobs.csv in a fixed location (overwrite each time)
                    String filePath = "data/SavedJobs.csv";
                    controller.getSavedJobsToCsv(filePath);
                    JOptionPane.showMessageDialog(parentFrame, 
                        "Jobs successfully saved to " + filePath, 
                        "Save Complete", 
                        JOptionPane.INFORMATION_MESSAGE);
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
                JOptionPane.showMessageDialog(parentFrame,
                "No jobs to export.", 
                "Export Jobs",
                JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            if (selectedFormat != null) {
                // Ask for confirmation before exporting - use parentFrame for centering
                int result = JOptionPane.showConfirmDialog(
                    parentFrame,
                    "Are you sure you want to export to " + selectedFormat + "?",
                    "Confirm Export",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (result == JOptionPane.YES_OPTION) {
                    // Export to a file
                    FileDialog fd = new FileDialog(parentFrame, "Save File", FileDialog.SAVE);
                    fd.setFile("SavedJobs." + selectedFormat.toLowerCase());
                    fd.setVisible(true);

                    // Get the selected file path
                    if (fd.getFile() != null) {
                        String filePath = fd.getDirectory() + fd.getFile();
                        controller.exportSavedJobs(savedJobs, selectedFormat, filePath);
        
                        // Show success message
                        JOptionPane.showMessageDialog(parentFrame,
                            "Jobs successfully exported in " + selectedFormat + " format to:\n" + filePath,
                            "Export Complete",
                            JOptionPane.INFORMATION_MESSAGE);
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