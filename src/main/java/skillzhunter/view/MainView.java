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
    private JPanel findJobTabbed;
    /** Pane for finding a job*/
    private JPanel searchJobTabbed;
    /** Pane to host findJobs and searchJobs*/
    private JTabbedPane tabbedPane;




    public MainView() {
        super("Domain Name Info");
        this.setSize(500, 500);
        this.setLocation(200, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPane = this.getContentPane();
        tabbedPane = new JTabbedPane();
        
        // Adding Panels
        JPanel panel1 = new SavedJobView();
        JPanel panel2 = new FindJobView();
        tabbedPane.add("Saved Jobs", panel1);
        tabbedPane.add("Find Jobs", panel2);
        mainPane.add(tabbedPane);
        

        // Adding menue bar
        JMenuBar menuBar = addMenuBar();
        setJMenuBar(menuBar);
        
        // pack();
        setVisible(true);
    }

    /**
     * Returns menue logic for the the main view
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
        throw new UnsupportedOperationException("Unimplemented method 'addFeatures'");
    } 

    public static void main(String[] args) {
        // This will generate the view with no actions
        new MainView();

        }
}
