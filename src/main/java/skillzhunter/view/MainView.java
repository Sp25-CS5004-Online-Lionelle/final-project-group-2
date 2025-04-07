package skillzhunter.view;

import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;

import java.awt.BorderLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;

import javax.swing.UIManager;
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
    private JMenuItem exit;
    private JMenuItem csvDownload;
    private JMenuItem jsonDownload;
    private JMenuItem xmlDownload;
    private JMenuItem lightMode;
    private JMenuItem darkMode;
    private ColorTheme theme;





    public MainView(IController controller) {
        super("Jobz Hunter App");
        this.controller = controller;
        this.setLocation(200, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // building tabs
        findJobTab = new FindJobTab(controller);
        savedJobTab = new SavedJobTab(controller);

        // building tabbed pane
        tabbedPane  = buildTabbedPane(findJobTab, savedJobTab);

        //Adds tabs to main view
        mainPane.add(tabbedPane);
        add(mainPane);

        // Adding menu bar
        JMenuBar menuBar = createMenuBar();
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
    
        // Use the existing tabs instead of creating new ones
        SavedJobsLists.addObserver(savedJobTab);
        SavedJobsLists.addObserver(findJobTab);
    
        tabbedPane.add("Find Jobs", findJobTab);
        tabbedPane.add("Saved Jobs", savedJobTab);
    
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
    private JMenuBar createMenuBar() {
        // settings
        JMenuBar menuBar = new JMenuBar();
        JMenu settings = new JMenu();
        try {
            java.net.URL imgURL = getClass().getClassLoader().getResource("images/SettingsIcon.png");
            if (imgURL != null) {
                javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imgURL);
                java.awt.Image scaledImage = icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
                settings.setIcon(new javax.swing.ImageIcon(scaledImage));
                
                // 👇 This is the key change
                settings.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Top, Left, Bottom, Right
                
                settings.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Set hand cursor
            } else {
                System.err.println("Couldn't find file: images/SettingsIcon.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        exit = new JMenuItem("Exit");
    
        // download
        JMenu menuDownload = new JMenu("Download Data");
        csvDownload = new JMenuItem("csv");
        jsonDownload = new JMenuItem("json");
        xmlDownload = new JMenuItem("xml");
        menuDownload.add(xmlDownload);
        menuDownload.add(jsonDownload);
        menuDownload.add(csvDownload);
    
        // view mode
        JMenu viewMenu = new JMenu("View Mode");
        lightMode = new JMenuItem("☀️");
        darkMode = new JMenuItem("🌙");
        viewMenu.add(lightMode);
        viewMenu.add(darkMode);
    
        // add menu items to settings
        settings.add(menuDownload);
        settings.add(viewMenu);
        settings.add(exit);
        menuBar.add(settings);
    
        mapMenuEvents();
        return menuBar;
    }    

    private void mapMenuEvents(){
        // Exit Functionality
        exit.addActionListener(exitEvent -> {
            int result = JOptionPane.showConfirmDialog(tabbedPane, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        darkMode.addActionListener(e -> {
            theme = ColorTheme.DARK;
            applyTheme(theme);
            SwingUtilities.updateComponentTreeUI(this);
        });

        lightMode.addActionListener(e -> {
            theme = ColorTheme.LIGHT;
            applyTheme(theme);
            SwingUtilities.updateComponentTreeUI(this);
        });


    }
    private void applyTheme(ColorTheme theme) {
        getContentPane().setBackground(theme.background);
        
        if (getJMenuBar() != null) {
            getJMenuBar().setBackground(theme.background);
            getJMenuBar().setForeground(theme.foreground);
        }
        
        if (findJobTab != null) {
            findJobTab.applyTheme(theme);
        }
        
        if (savedJobTab != null) {
            savedJobTab.applyTheme(theme);
        }
        
        tabbedPane.setBackground(theme.background);
        tabbedPane.setForeground(theme.foreground);
        
        repaint();
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
//        for(UIManager.LookAndFeelInfo lnFinfo: UIManager.getInstalledLookAndFeels()) {
//            System.out.println(lnFinfo.getClassName());
//        }
    }
}
