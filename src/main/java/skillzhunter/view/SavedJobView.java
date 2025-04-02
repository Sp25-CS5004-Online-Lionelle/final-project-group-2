package skillzhunter.view;

import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;

import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;

import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumn;
import skillzhunter.controller.SavedJobController;
import skillzhunter.model.JobRecord;


public class SavedJobView extends JobView{

    public SavedJobView(){

        super();
        setJobsList(SavedJobsLists.getSavedJobs());
        this.searchButton.setText("Saved Jobs");
        this.savedJobs = true;

        SavedJobsLists.addObserver(this);
    }




    public static void main(String[] args) {
        System.out.println("hello");
    }

}
