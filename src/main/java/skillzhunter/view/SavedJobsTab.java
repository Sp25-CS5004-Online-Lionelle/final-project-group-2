package skillzhunter.view;

import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;

public class SavedJobsTab extends JobView {
    private JButton saveButton;
    private JButton loadButton;
    
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
        loadButton = new JButton("Load Job List from File");
        // Set hand cursor for the load button
        loadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topRow.add(loadButton);
        
        // Initial styling will happen during applyTheme

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
        JPanel bottomRow = new JPanel();
        saveButton = new JButton("Save Jobs");
        // Set hand cursor for the save button
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Initial styling will happen during applyTheme
        
        bottomRow.add(saveButton);

        saveButton.addActionListener(e -> {
            // Set hand cursor for confirmation dialog buttons
            UIManager.put("Button.cursor", Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
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
                // Ask for confirmation before saving - use parentFrame instead of bottomRow
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
 
                    // Use parentFrame instead of bottomRow for centering
                    JOptionPane.showMessageDialog(parentFrame, 
                        "Jobs successfully saved to " + filePath, 
                        "Save Complete", 
                        JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Saved to " + filePath);
                }
            }
            
            // Reset cursor property
            UIManager.put("Button.cursor", Cursor.getDefaultCursor());
        });
        
        return bottomRow;
    }
    
    @Override
    public void applyTheme(ColorTheme theme) {
        // Call parent implementation first for common styling
        super.applyTheme(theme);
        
        // Now specifically style our buttons
        if (saveButton != null) {
            setButtonProperties(saveButton);
        }
        
        if (loadButton != null) {
            setButtonProperties(loadButton);
        }
    }

    public static void main(String[] args) {
        System.out.println("hello");
    }
}