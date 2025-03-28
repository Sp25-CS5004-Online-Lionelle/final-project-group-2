package skillzhunter.view;

import java.awt.Container;

import javax.swing.JFrame;
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
        
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        tabbedPane.add("Panel 1", panel1);
        tabbedPane.add("Panel 2", panel2);

        mainPane.add(tabbedPane);
        // pack();
        setVisible(true);
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
