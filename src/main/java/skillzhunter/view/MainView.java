package skillzhunter.view;

import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


import skillzhunter.controller.IController;


public class MainView extends JFrame implements IView {

//    /** Main pane for the entire program */
//    private Container mainPane = this.getContentPane();
    private JPanel mainPane = new JPanel();
    private JPanel findJobPane = new JPanel();
    //private JobsTable findJobsTable = new JobsTable();
    private JPanel savedJobPane = new JPanel();
    /** Pane to host findJobs and searchJobs*/
    private JTabbedPane tabbedPane;



    public MainView(FindJobView findJobTab, SavedJobView saveJobTab) {
        super("Jobz Hunter App");
        //this.setSize(500, 500);
        this.setLocation(200, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SavedJobsLists.addObserver(saveJobTab);

        //Assigns the sub-views
        findJobPane.add(findJobTab);
        savedJobPane.add(saveJobTab);

        //Creates the sub-view tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Find Jobs", findJobPane);
        tabbedPane.addTab("Saved Jobs", savedJobPane);

        // Set hand cursor for tabs
        tabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
            tabbedPane.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            tabbedPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        });

        //Adds tabs to main view
        mainPane.add(tabbedPane);
        add(mainPane);

        // Adding menu bar
        JMenuBar menuBar = addMenuBar();
        setJMenuBar(menuBar);

        pack();
    }

    /**
     * Returns menu logic for the main view
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

    /**
     * Adds features to the view.
     */
    @Override
    public void addFeatures(IController controller) {
    //   this.findJobTab.addFeatures(controller);
    //   this.searchJobTabbed.addFeatures(controller);
    } 


    /**
     * Runs the view.
     * This method is used to start the view and display it to the user.
     * It should be called after the view has been set up and configured.
     */
    @Override
    public void run(){
        setVisible(true);
    }
    public static void main(String[] args) {

        
        // This will generate the view with no actions
        FindJobView findjobview = new FindJobView();
        SavedJobView savedjobview = new SavedJobView();
        new MainView(findjobview, savedjobview);

    }
}
