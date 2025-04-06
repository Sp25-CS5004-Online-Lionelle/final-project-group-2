package skillzhunter.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.TextField;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import skillzhunter.controller.IController;
import skillzhunter.controller.IJobController;
import skillzhunter.model.JobRecord;
import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;


public abstract class JobView extends JPanel implements IJobView {
    protected JButton searchButton;
    protected JTextArea searchField;
    protected JTextArea recordText;
    protected JobsTable jobsTable = new JobsTable(getColumnNames(), new Object[0][0]);
    private ColorTheme theme;
    protected List<JobRecord> jobsList = new ArrayList<>();
    protected JButton darkModeToggle;
    protected JButton openJob;
    protected JButton exit;
    protected JPanel topButtonLayout = new JPanel();
    protected IController controller;
    protected JPanel mainPanel;
    protected boolean savedJobs = false;

    public JobView() {
    }

    public void initView(){

        //make the frame & panels and set layout
        setSize(1000, 1000);
        jobsTable = new JobsTable(getColumnNames(), getData(jobsList));
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        // Adding in panels
        mainPanel.add(makeTopButtonPanel());
        mainPanel.add(makeTablePanel());
        mainPanel.add(makeBottomButtonPanel());
        add(mainPanel);

    }

    public JPanel makeTopButtonPanel() {

        //make the panel & set layout
        JPanel topRow = new JPanel();
        topRow.setLayout(new BoxLayout(topRow, BoxLayout.LINE_AXIS)); //this doesn't seem to get passed to children well?
        topRow.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        //create fields, buttons, and combos
        TextField searchField = new TextField("!!This is Place Holder Overide this method!!", 20);

        //add fields, buttons, labels, combos, and spaces
        topRow.add(searchField);

        //return the panel
        return topRow;

    }

    public JPanel makeBottomButtonPanel() {

        //make the panel & set layout
        JPanel bottomRow = new JPanel();
        bottomRow.setLayout(new BoxLayout(bottomRow, BoxLayout.LINE_AXIS));
        bottomRow.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        //create fields, buttons, and combos
        TextField searchField = new TextField("!!This is Place Holder Overide this method!!", 20);

        //add fields, buttons, labels, combos, and spaces
        bottomRow.add(searchField);

        //return the panel
        return bottomRow;
    }
    

    public JPanel makeTablePanel() {

        //make the panel & set layout
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout()); // Use BorderLayout for better handling of the table
        tablePanel.setPreferredSize(new Dimension(900, 400)); // Set a reasonable preferred size for the table
        tablePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        //create tables
        jobsTable = new JobsTable(getColumnNames(), getData(jobsList));
        jobsTable.setAutoCreateRowSorter(true); //not working yet
        JScrollPane tablePane = new JScrollPane(jobsTable);

        //add tables
        tablePanel.add(tablePane, BorderLayout.CENTER); // Add tablePane to the center of the panel

        //return the panel
        return tablePanel;
    }


    public void setButtonProperties(JButton button) {
        Color normalColor = theme.buttonNormal;
        Color hoverColor = theme.buttonHover;
    
        // First, set the normal background color for the button
        button.setBackground(normalColor);
        button.setForeground(theme.buttonForeground);
        
        // Apply hover effect with the correct hover color
        applyHoverEffect(button, hoverColor, normalColor);
    
        // Other properties
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
    }


    public void applyTheme(ColorTheme theme) {
        setBackground(theme.background);
        System.out.println("Applying theme: " + theme);
        // recordText.setBackground(theme.background);
        // recordText.setForeground(theme.foreground);
        // Set button colors
        // searchButton.setBackground(theme.buttonNormal);
        // searchButton.setForeground(theme.buttonForeground);
        openJob.setBackground(theme.buttonNormal);
        openJob.setForeground(theme.buttonForeground);
        jobsTable.setBackground(theme.fieldBackground);
        jobsTable.setForeground(theme.fieldForeground);
        jobsTable.setBorder(BorderFactory.createLineBorder(theme.buttonNormal));
        jobsTable.getTableHeader().setBackground(theme.fieldBackground);
        jobsTable.getTableHeader().setForeground(theme.foreground);
        repaint();
    }

    private void applyHoverEffect(JButton button, Color hoverColor, Color normalColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(theme.buttonHover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(theme.buttonNormal); // Dynamically retrieve the current normal color
            }
        });
    }

    public void setJobsList(List<JobRecord> jobsList) {
       this.jobsList = jobsList;
       this.jobsTable.setData(getData(jobsList));
       Object[][] data = getData(jobsList);
       DefaultTableModel tableData = new DefaultTableModel(data, getColumnNames());
       this.jobsTable.setModel(tableData);
       this.jobsTable.repaint();
       // Enable selection
        this.jobsTable.setRowSelectionAllowed(true);
        this.jobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       
    }

    public List<JobRecord> getJobsList() {
        return this.jobsList;
    }

    public void setRecordText(String text) {
        recordText.setText(text);
    }

    public void addJobRecord(JobRecord record) {
        this.jobsList.add(record);
        this.jobsTable.setData(getData(jobsList));
    }

    public void removeJobRecord(JobRecord record) {
        this.jobsList.remove(record);
        this.jobsTable.setData(getData(jobsList));
    }

    public static void main(String[] args) {
        System.out.println("hello");
    }
    public void addFeatures(IJobController controller) {
        searchButton.addActionListener(e -> controller.setViewData());
    }
}
