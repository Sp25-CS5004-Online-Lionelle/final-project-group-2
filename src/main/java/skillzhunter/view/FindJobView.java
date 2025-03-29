package skillzhunter.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import skillzhunter.controler.FindJobController;
import skillzhunter.view.JobsLoader.*;



public class FindJobView extends JPanel {
    private JButton searchButton;
    private JTextArea recordText;
    private JobsTable jobsTable;

    public FindJobView() {
        searchButton = new JButton("Find Jobs");
        add(searchButton);

        recordText = new JTextArea("click to find jobs");
        add(recordText);


        jobsTable = new JobsTable(JobsLoader.getColumnNames(), JobsLoader.getData(JobRecordGenerator.generateDummyRecords(10)));
        add(new JScrollPane(jobsTable));

    }
    public void setRecordText(String text) {
        this.recordText.setText(text);
    }


    public void addFeatures(FindJobController controller) {
        searchButton.addActionListener(e -> controller.dummyFindJobMethod());
    }

    public static void main(String[] args) {
        System.out.println("hello");
    }
    
}
