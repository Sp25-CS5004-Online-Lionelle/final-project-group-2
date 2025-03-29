package skillzhunter.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import skillzhunter.controler.SavedJobController;



public class SavedJobView extends JPanel{
    private JButton searchButton;
    private JTextArea recordText;
    private JobsTable jobsTable;

    public SavedJobView() {
        searchButton = new JButton("Saved Jobs");
        add(searchButton);
        recordText = new JTextArea("click to find jobs");
        add(recordText);

        jobsTable = new JobsTable(JobsLoader.getColumnNames(), JobsLoader.getData(JobRecordGenerator.generateDummyRecords(10)));
        add(new JScrollPane(jobsTable));
    }


    public void addFeatures(SavedJobController controller) {
        this.searchButton.addActionListener(e -> controller.dummySavedJobMethod());
    }

    public void setRecordText(String text) {
        this.recordText.setText(text);

    }
    public static void main(String[] args) {
        System.out.println("hello");
    }

}
