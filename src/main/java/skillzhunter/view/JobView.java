package skillzhunter.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicRadioButtonMenuItemUI;

import skillzhunter.controller.IJobController;
import skillzhunter.model.JobRecord;
import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;

import skillzhunter.controller.FindJobController;
import skillzhunter.controller.IController;


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


    //This is an ugly way to let inheriting classes know if they are SavedJobView or FindJobView
    //We should probably figure something else out
    protected boolean savedJobs = false;

    public JobView() {
 
    }

    public void initView(){
        setSize(1000, 1000);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        // Adding in Layouts
        mainPanel.add(makeTopButtonPanel());
        mainPanel.add(makeTablePanel());
        mainPanel.add(makeBottomButtonPanel());
        add(mainPanel);

    }

    public JPanel makeTopButtonPanel() {
        JPanel searchRow = new JPanel();
        searchRow.setLayout(new BoxLayout(searchRow, BoxLayout.LINE_AXIS));
        TextField searchField = new TextField("!!This is Place Holder Overide this method!!", 20);    
        searchRow.add(searchField);
        return searchRow;

    }

    public JPanel makeBottomButtonPanel() {
        JPanel searchRow = new JPanel();
        searchRow.setLayout(new BoxLayout(searchRow, BoxLayout.LINE_AXIS));
        TextField searchField = new TextField("!!This is Place Holder Overide this method!!", 20);    
        searchRow.add(searchField);
        return searchRow;
    }
    

    public JPanel makeTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout()); // Use BorderLayout for better handling of the table
        tablePanel.setPreferredSize(new Dimension(800, 400)); // Set a reasonable preferred size for the table

        jobsTable = new JobsTable(getColumnNames(), getData(jobsList));

        JScrollPane tablePane = new JScrollPane(jobsTable);
        tablePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tablePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tablePanel.add(tablePane, BorderLayout.CENTER); // Add tablePane to the center of the panel
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
    //    this.jobsTable.fireTableStructureChanged();
       this.jobsTable.repaint();

    }

    public List<JobRecord> getJobsList() {
        return this.jobsList;
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
