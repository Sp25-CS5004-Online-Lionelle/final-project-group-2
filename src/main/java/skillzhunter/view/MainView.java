package skillzhunter.view;

import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;

public class MainView extends JFrame implements IView {

    private JPanel mainPane = new JPanel();
    final private JobView findJobTab;
    final private JobView savedJobTab;
    private JTabbedPane tabbedPane;
    private TabStyleManager tabStyleManager;
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

        // Set default theme (light mode) early
        theme = ColorTheme.LIGHT;

        // Building tabs
        findJobTab = new FindJobTab(controller);
        savedJobTab = new SavedJobsTab(controller, controller.getSavedJobs());

        // Building tabbed pane
        tabbedPane = buildTabbedPane(findJobTab, savedJobTab);

        // Adds tabs to main view
        mainPane.add(tabbedPane);
        add(mainPane);

        // Create tab style manager
        String[] tabNames = {"Find Jobs", "Saved Jobs"};
        tabStyleManager = new TabStyleManager(tabbedPane, tabNames);

        // Adding menu bar
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        // Apply the theme after everything is set up
        applyTheme(theme);
        
        // Apply theme once more after showing the frame to ensure it takes effect
        SwingUtilities.invokeLater(() -> {
            applyTheme(theme);
        });

        pack();
    }

    /**
     * Builds the tabbed pane for the main view.
     */
    private JTabbedPane buildTabbedPane(JobView findJobTab, JobView savedJobTab) {
        // Create the tabbed pane
        tabbedPane = new JTabbedPane();
    
        // Use the existing tabs instead of creating new ones
        SavedJobsLists.addObserver(savedJobTab);
        SavedJobsLists.addObserver(findJobTab);

        // Set controller
        SavedJobsLists.setController(controller);
        
        // Add tabs with components
        tabbedPane.add(findJobTab);
        tabbedPane.add(savedJobTab);
        
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
        try {
            java.net.URL lightModeImgURL = getClass().getClassLoader().getResource("images/LightModeIcon.png");
            if (lightModeImgURL != null) {
            javax.swing.ImageIcon lightModeIcon = new javax.swing.ImageIcon(lightModeImgURL);
            java.awt.Image scaledLightModeImage = lightModeIcon.getImage().getScaledInstance(12, 12, java.awt.Image.SCALE_SMOOTH);
            lightMode = new JMenuItem(new javax.swing.ImageIcon(scaledLightModeImage));
            } else {
            System.err.println("Couldn't find file: images/LightModeIcon.png");
            lightMode = new JMenuItem("Light Mode");
            }

            java.net.URL darkModeImgURL = getClass().getClassLoader().getResource("images/DarkModeIcon.png");
            if (darkModeImgURL != null) {
            javax.swing.ImageIcon darkModeIcon = new javax.swing.ImageIcon(darkModeImgURL);
            java.awt.Image scaledDarkModeImage = darkModeIcon.getImage().getScaledInstance(12, 12, java.awt.Image.SCALE_SMOOTH);
            darkMode = new JMenuItem(new javax.swing.ImageIcon(scaledDarkModeImage));
            } else {
            System.err.println("Couldn't find file: images/DarkModeIcon.png");
            darkMode = new JMenuItem("Dark Mode");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lightMode = new JMenuItem("Light Mode");
            darkMode = new JMenuItem("Dark Mode");
        }
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
            
            // Apply theme once more after UI update to ensure styling is applied
            SwingUtilities.invokeLater(() -> {
                applyTheme(theme);
            });
        });

        lightMode.addActionListener(e -> {
            theme = ColorTheme.LIGHT;
            applyTheme(theme);
            SwingUtilities.updateComponentTreeUI(this);
            
            // Apply theme once more after UI update to ensure styling is applied
            SwingUtilities.invokeLater(() -> {
                applyTheme(theme);
            });
        });
    }
    
    /**
     * Applies the specified color theme to all components in the view hierarchy.
     */
    private void applyTheme(ColorTheme theme) {
        // Apply theme to main frame components
        getContentPane().setBackground(theme.background);
        mainPane.setBackground(theme.background);
        
        // Apply to menu bar
        if (getJMenuBar() != null) {
            getJMenuBar().setBackground(theme.background);
            getJMenuBar().setForeground(theme.foreground);
        }
        
        // Apply theme to tabs using the TabStyleManager
        if (tabStyleManager != null) {
            tabStyleManager.applyTheme(theme);
        }
        
        // Apply theme to tab contents
        if (findJobTab != null) {
            findJobTab.applyTheme(theme);
        }
        
        if (savedJobTab != null) {
            savedJobTab.applyTheme(theme);
        }
        
        // Repaint to show changes
        repaint();
    }

    @Override
    public void addFeatures(IController controller) {
        // Not needed
    } 

    @Override
    public void run(){
        setVisible(true);
        
        // Apply theme once more after becoming visible
        SwingUtilities.invokeLater(() -> {
            applyTheme(theme);
        });
    }
}