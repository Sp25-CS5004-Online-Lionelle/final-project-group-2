package skillzhunter.view;


import static skillzhunter.view.JobsLoader.getData;

import java.awt.TextField;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import kotlin.Suppress;
import skillzhunter.model.JobRecord;
import skillzhunter.controller.FindJobController;
import skillzhunter.controller.IController;
import static skillzhunter.view.JobsLoader.getColumnNames;

public class FindJobView extends JobView {
    private static IController controller;

    public FindJobView(IController controller){
        super();
        setJobsList(JobRecordGenerator.generateDummyRecords(10));
    }

    @Override
    public JPanel makeTopButtonPanel() {
        JPanel searchRow = new JPanel();
        searchRow.setLayout(new BoxLayout(searchRow, BoxLayout.LINE_AXIS));
        TextField searchField = new TextField("", 20);    
        searchButton = new JButton("Find Jobs");
        searchRow.add(searchField);
        searchRow.add(searchButton);
        searchButton.addActionListener(e -> {
            List<JobRecord> jobs = FindJobController.getApiCall(searchField.getText(), 10, "any","any");
            jobsTable = new JobsTable(getColumnNames(), getData(jobsList));

            // jobsTable.setActionMap();
            System.out.println(jobs);
        }
            );
        // searchField.addActionListener(e -> controller.setViewData());
        return searchRow;

    }
    @Override
    public void applyTheme(ColorTheme theme) {
        super.applyTheme(theme);
        System.out.println("Applying theme: " + theme);
        setBackground(theme.background);
        openJob.setBackground(theme.buttonNormal);
        openJob.setForeground(theme.buttonForeground);
        jobsTable.setBackground(theme.fieldBackground);
        jobsTable.setForeground(theme.fieldForeground);
        jobsTable.setBorder(BorderFactory.createLineBorder(theme.buttonNormal));
        jobsTable.getTableHeader().setBackground(theme.fieldBackground);
        jobsTable.getTableHeader().setForeground(theme.foreground);
        repaint();
    }


    public static void main(String[] args) {
        System.out.println("hello");
    }

}