package skillzhunter.view;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;

public class SavedJobsTab extends JobView {
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
        JButton loadButton = new JButton("Load Job List from File");
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
        JPanel bottomRow = new JPanel();
        JButton saveButton = new JButton("Save Jobs");
        bottomRow.add(saveButton);

        saveButton.addActionListener(e -> {
            // Save to data/SavedJobs.csv in a fixed location (overwrite each time)
            String filePath = "data/SavedJobs.csv";
            List<JobRecord> savedJobs = controller.getSavedJobs();
            
            if (savedJobs.isEmpty()) {
                System.out.println("No jobs to save.");
            } else {
                controller.getSavedJobsToCsv(filePath);
                System.out.println("Saved to " + filePath);
            }
        });
        

        return bottomRow;
    }

    public static void main(String[] args) {
        System.out.println("hello");
    }
}
