package skillzhunter.view;

import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumn;
import skillzhunter.controler.FindJobController;




public class FindJobView extends JPanel {
    private JButton searchButton;
    private JTextArea recordText;
    private JobsTable jobsTable;


    public FindJobView() {
        setSize(600,600);
        searchButton = new JButton("Find Jobs");
        add(searchButton);

        recordText = new JTextArea("click to find jobs");
        add(recordText);

//        JPanel tablePanel = new JPanel(new FlowLayout());
//        tablePanel.setSize(600,600);
//
//        jobsTable = new JobsTable(getColumnNames(), getData(JobRecordGenerator.generateDummyRecords(10)));
//        TableColumn column1 = jobsTable.getColumnModel().getColumn(0); // Name column
//        column1.setPreferredWidth(150);
//        column1.setMinWidth(150);
//        column1.setMaxWidth(150);
//
//        TableColumn column2 = jobsTable.getColumnModel().getColumn(1); // Name column
//        column2.setPreferredWidth(150);
//        column2.setMinWidth(150);
//        column2.setMaxWidth(150);
//
//
//        JScrollPane tablePane = new JScrollPane();
//        tablePane.setViewportView(jobsTable);
//        tablePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//        tablePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//        tablePanel.add(tablePane);
//
//
//        add(tablePane);

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
