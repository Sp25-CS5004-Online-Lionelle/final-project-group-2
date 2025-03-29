package skillzhunter.view;

import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;

import java.awt.BorderLayout;
import java.awt.Container;

import java.awt.Dimension;
import java.awt.ScrollPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import javax.swing.table.TableColumn;
import skillzhunter.controler.IController;


public class MainView extends JFrame implements IView {

//    /** Main pane for the entire program */
//    private Container mainPane = this.getContentPane();
    private JPanel mainPane = new JPanel();
    private JPanel findJobPane = new JPanel(new BorderLayout());
    private JobsTable findJobsTable = new JobsTable();
    private JPanel savedJobPane = new JPanel();
    /** Pane to host findJobs and searchJobs*/
    private JTabbedPane tabbedPane;



    public MainView(FindJobView findJobTab, SavedJobView saveJobTab) {
        super("Jobz Hunter App");
        //this.setSize(500, 500);
        this.setLocation(200, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        findJobPane.add(findJobTab,BorderLayout.NORTH);
        findJobsTable = new JobsTable(getColumnNames(), getData(JobRecordGenerator.generateDummyRecords(10)));
        TableColumn column1 = findJobsTable.getColumnModel().getColumn(0); // Name column
        column1.setPreferredWidth(150);
        column1.setMinWidth(150);
        column1.setMaxWidth(150);
        JScrollPane findJobsTableScroll = new JScrollPane(findJobsTable);
        findJobPane.add(findJobsTableScroll, BorderLayout.CENTER);

        savedJobPane.add(saveJobTab);


        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Find Jobs", findJobPane);
        tabbedPane.addTab("Saved Jobs", savedJobPane);
        mainPane.add(tabbedPane);
        add(mainPane);

        // Adding menu bar
        JMenuBar menuBar = addMenuBar();
        setJMenuBar(menuBar);

        pack();
        setVisible(true);
    }

    /**
     * Returns menue logic for the main view
     */
    private JMenuBar addMenuBar() {
        // Download Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menuDownload = new JMenu("Download Data");
        JMenuItem csvDownload = new JMenuItem("csv");
        JMenuItem jsonDownload = new JMenuItem("json");
        JMenuItem xmlDownload = new JMenuItem("xml");

        menuDownload.add(xmlDownload);
        menuDownload.add(jsonDownload);
        menuDownload.add(csvDownload);
        menuBar.add(menuDownload);

        return menuBar;
    }

    @Override
    public void addFeatures(IController controller) {
    //   this.findJobTab.addFeatures(controller);
    //   this.searchJobTabbed.addFeatures(controller);
    } 

    public static void main(String[] args) {

        
        // This will generate the view with no actions
        //new MainView();

        }
}
