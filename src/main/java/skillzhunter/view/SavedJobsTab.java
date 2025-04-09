package skillzhunter.view;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.FlowLayout;
import java.io.File;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JFrame;
import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;

public class SavedJobsTab extends JobView {
    
    // Store buttons as fields to apply theme later
    private ThemedButton loadButton;
    private ThemedButton saveButton;
    
    public SavedJobsTab(IController controller, List<JobRecord> savedJobs) {
        super();
        // set inherited field from jobview
        this.controller = controller;
        super.initView();
        updateJobsList(savedJobs);
    }

    @Override
    public JPanel makeTopButtonPanel() {
        JPanel topRow = new JPanel();
        
        // Create the load button and store it as field
        loadButton = new ThemedButton("Load Job List from File");
        topRow.add(loadButton);

        loadButton.addActionListener(e -> {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(topRow);
            FileDialog fd = new FileDialog(parentFrame, "Pick file to load", FileDialog.LOAD);
            fd.setVisible(true);

            String path = null;
            if (fd.getFile() != null) {
                File file = new File(fd.getDirectory(), fd.getFile());
                path = file.getPath();
            }

            if (path != null) {
                // You can wire this up later to actually read the file into the model
                // For now, let's simulate grabbing the saved jobs from model
                List<JobRecord> savedJobs = controller.getSavedJobs();
            } else {
                System.out.println("No file selected.");
            }
        });

        return topRow;
    }

    @Override
    public JPanel makeBottomButtonPanel() {
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Create save button and store it as field
        saveButton = new ThemedButton("Save Jobs");
        bottomRow.add(saveButton);

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
        
        return bottomRow;
    }
    
    @Override
    public void applyTheme(ColorTheme theme) {
        // Call parent implementation for common styling
        super.applyTheme(theme);
        
        // Explicitly apply theme to our buttons
        if (loadButton != null) {
            loadButton.applyTheme(theme);
        }
        
        if (saveButton != null) {
            saveButton.applyTheme(theme);
        }
        
        // Make sure changes are visible
        this.revalidate();
        this.repaint();
    }
}