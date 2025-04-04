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
    final private JobView findJobTab;
    //private JobsTable findJobsTable = new JobsTable();
    final private JobView savedJobTab;
    /** Pane to host findJobs and searchJobs*/
    private JTabbedPane tabbedPane;
    final private IController controller;



    public MainView(IController controller) {
        super("Jobz Hunter App");
        this.controller = controller;
        //this.setSize(500, 500);
        this.setLocation(200, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // building tabs
        findJobTab = new FindJobView(controller);
        savedJobTab = new SavedJobView(controller);
        
        // building tabbed pane
        tabbedPane  = buildTabbedPane(findJobTab, savedJobTab);

        //Adds tabs to main view
        mainPane.add(tabbedPane);
        add(mainPane);

        // Adding menu bar
        JMenuBar menuBar = addMenuBar();
        setJMenuBar(menuBar);

        pack();
    }

    /**
     * Builds the tabbed pane for the main view.
     * @param findJobTab The tab for finding jobs.
     * @param savedJobTab The tab for saved jobs.
     * @return The tabbed pane containing the two tabs.
     */
    private JTabbedPane buildTabbedPane(JobView findJobTab, JobView savedJobTab) {
        // Create the tabbed pane
        tabbedPane = new JTabbedPane();

        findJobTab = new FindJobView(controller);
        savedJobTab = new SavedJobView(controller);

        SavedJobsLists.addObserver(savedJobTab);

        tabbedPane.add("Find Jobs",findJobTab);
        tabbedPane.add("Saved Jobs",savedJobTab);


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
        return tabbedPane;
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
