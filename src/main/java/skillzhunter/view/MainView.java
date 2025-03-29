package skillzhunter.view;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import skillzhunter.controler.IController;


public class MainView extends JFrame implements IView {

     /** Main pane for the entire program */
    private Container mainPane; 
    /** Pane for finding a job*/
    private FindJobView findJobTabbed;
    /** Pane for finding a job*/
    private SavedJobView saveJobTabbed;
    /** Pane to host findJobs and searchJobs*/
    private JTabbedPane tabbedPane;





    public MainView(FindJobView findJobTabbed, SavedJobView saveJobTabbed) {
        super("Skillz Hunter App");
        this.setSize(500, 500);
        this.setLocation(200, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.findJobTabbed = findJobTabbed;
        this.saveJobTabbed = saveJobTabbed;

        mainPane = this.getContentPane();
        tabbedPane = new JTabbedPane();
        
      
        tabbedPane.add("Saved Jobs", this.findJobTabbed);
        tabbedPane.add("Find Jobs", this.saveJobTabbed);
        mainPane.add(tabbedPane);
        

        // Adding menue bar
        JMenuBar menuBar = addMenuBar();
        setJMenuBar(menuBar);
        
        // pack();
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
    //   this.findJobTabbed.addFeatures(controller);
    //   this.searchJobTabbed.addFeatures(controller);
    } 

    public static void main(String[] args) {

        
        // This will generate the view with no actions
        //new MainView();

        }
}
