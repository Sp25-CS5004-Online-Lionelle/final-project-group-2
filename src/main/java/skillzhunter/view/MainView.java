package skillzhunter.view;

import static skillzhunter.view.JobsLoader.getColumnNames;
import static skillzhunter.view.JobsLoader.getData;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JFrame;
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
    private CustomMenuBar customMenuBar;
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
        add(mainPane, BorderLayout.CENTER);

        // Create tab style manager
        String[] tabNames = {"Find Jobs", "Saved Jobs"};
        tabStyleManager = new TabStyleManager(tabbedPane, tabNames);

        // Create custom menu
        createCustomMenu();
        
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
    // Update buildTabbedPane method to remove SavedJobsLists usage
    private JTabbedPane buildTabbedPane(JobView findJobTab, JobView savedJobTab) {
        // Create the tabbed pane
        tabbedPane = new JTabbedPane();

        // Add tabs with components
        tabbedPane.addTab("Find Jobs", findJobTab);
        tabbedPane.addTab("Saved Jobs", savedJobTab);
        tabbedPane.addChangeListener(e -> {
            // Get the selected tab
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex >= 0) {
                Component selectedComponent = tabbedPane.getComponentAt(selectedIndex);
                
                // If it's a JobView, update its job list
                if (selectedComponent instanceof JobView) {
                    JobView jobView = (JobView) selectedComponent;
                    jobView.updateJobsList(controller.getSavedJobs());
                }
            }
        });
        return tabbedPane;
    }

    /**
     * Creates and adds the custom menu bar to the frame
     */
    private void createCustomMenu() {
        // Create our custom menu bar
        customMenuBar = new CustomMenuBar();
        
        // Add the custom menu to the top of the frame
        getContentPane().add(customMenuBar, BorderLayout.NORTH);
        
        // Map menu events
        mapMenuEvents();
    }

    /**
     * Maps action listeners to menu items
     */
    private void mapMenuEvents() {
        // Exit Functionality
        customMenuBar.getExitItem().addActionListener(exitEvent -> {
            int result = JOptionPane.showConfirmDialog(tabbedPane, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        // Theme switching
        customMenuBar.getDarkModeItem().addActionListener(e -> {
            theme = ColorTheme.DARK;
            applyTheme(theme);
            SwingUtilities.updateComponentTreeUI(this);
            
            // Apply theme once more after UI update to ensure styling is applied
            SwingUtilities.invokeLater(() -> {
                applyTheme(theme);
            });
        });

        customMenuBar.getLightModeItem().addActionListener(e -> {
            theme = ColorTheme.LIGHT;
            applyTheme(theme);
            SwingUtilities.updateComponentTreeUI(this);
            
            // Apply theme once more after UI update to ensure styling is applied
            SwingUtilities.invokeLater(() -> {
                applyTheme(theme);
            });
        });
        
        // Download options could be added here if needed
    }
    
    /**
     * Applies the specified color theme to all components in the view hierarchy.
     */
    private void applyTheme(ColorTheme theme) {
        // Apply theme to main frame components
        getContentPane().setBackground(theme.background);
        mainPane.setBackground(theme.background);
        
        // Apply theme to custom menu
        if (customMenuBar != null) {
            customMenuBar.applyTheme(theme);
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
        
        // Force UI update and repaint
        repaint();
    }

    @Override
    public void addFeatures(IController controller) {
        // Not needed
    } 

    @Override
    public void run() {
        setVisible(true);
        
        // Apply theme once more after becoming visible
        SwingUtilities.invokeLater(() -> {
            applyTheme(theme);
        });
    }
}