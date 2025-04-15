package skillzhunter.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import skillzhunter.controller.AlertObserver;
import skillzhunter.controller.IController;

public class MainView extends JFrame implements IView, AlertObserver {
    /** Main Pane. */
    private final JPanel mainPane = new JPanel();
    /** Find Job Tab. */
    private JobView findJobTab;
    /** Saved Job Tab. */
    private JobView savedJobTab;
    /** Tabbed Pane. */
    private JTabbedPane tabbedPane;
    /** Tab Style Manager. */
    private TabStyleManager tabStyleManager;
    /** Controller. */
    private IController controller;
    /** Custom Menu Bar. */
    private CustomMenuBar customMenuBar;
    /** Theme. */
    private ColorTheme theme;

    /**
     * Creates main view with initial setup but without controller connection.
     */
    public MainView() {
        super("Skillz Hunter App");
        this.setLocation(200, 200);
        
        // Change from EXIT_ON_CLOSE to DO_NOTHING_ON_CLOSE
        // We'll handle closing manually with our window listener
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Add window listener to handle close button (X) click
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitHelper();
            }
        });

        // Set default theme (light mode) early
        theme = ColorTheme.LIGHT;

        // Initialize minimal UI components that don't need controller
        mainPane.setLayout(new BorderLayout());
        add(mainPane, BorderLayout.CENTER);
        
        // Create custom menu bar (doesn't need controller yet)
        createCustomMenu();
    }

    /**
     * Sets the controller for this view and completes initialization.
     * This must be called before using the view.
     * 
     * @param controller The controller to set
     */
    @Override
    public void setController(IController controller) {
        this.controller = controller;
        
        // Register as alert observer
        controller.registerAlertObserver(this);
        
        try {
            // Now we can initialize controller-dependent components
            savedJobTab = controller.getSavedJobsTab();
            findJobTab = new FindJobTab(controller);
            
            // Building tabbed pane
            tabbedPane = buildTabbedPane(findJobTab, savedJobTab);
            
            // Adds tabs to main view
            mainPane.add(tabbedPane);
            
            // Create tab style manager
            String[] tabNames = {"Find Jobs", "Saved Jobs"};
            tabStyleManager = new TabStyleManager(tabbedPane, tabNames);
            
            // Map menu events now that we have controller
            mapMenuEvents();
            
            // Apply the theme after everything is set up
            applyTheme(theme);
            
            // Apply theme once more after showing the frame to ensure it takes effect
            SwingUtilities.invokeLater(() -> {
                applyTheme(theme);
            });
            
            pack();
            setupExitKeyAction();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error initializing UI: " + ex.getMessage(),
                "Initialization Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Implementation of AlertObserver interface.
     * Receives alerts from the controller and displays them to the user.
     * 
     * @param message The alert message to display
     */
    @Override
    public void onAlert(String message) {
        notifyUser(message);
    }

    /**
     * Sets up the exit key (Q).
     */
    private void setupExitKeyAction() {
        Action exitAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show confirmation dialog, just like in the menu
                exitHelper();
            }
        };

        JComponent rootPane = getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // Keep just Q key without modifiers
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "exit");
        
        ActionMap actionMap = rootPane.getActionMap();
        actionMap.put("exit", exitAction);
    }
    
    /**
     * Builds the tabbed pane for the main view.
     * @param findJobTab The tab for finding jobs
     * @param savedJobTab The tab for saved jobs
     * @return The created tabbed pane
     */
    private JTabbedPane buildTabbedPane(JobView findJobTab, JobView savedJobTab) {
        // Create the tabbed pane
        JTabbedPane pane = new JTabbedPane();

        // Add tabs with components
        pane.addTab("Find Jobs", findJobTab);
        pane.addTab("Saved Jobs", savedJobTab);
        
        // Add change listener to update only the Saved Jobs tab when selected
        pane.addChangeListener(e -> {
            // Get the selected tab
            int selectedIndex = pane.getSelectedIndex();
            if (selectedIndex >= 0) {
                String tabName = pane.getTitleAt(selectedIndex);
                Component selectedComponent = pane.getComponentAt(selectedIndex);
                
                // Only update the Saved Jobs tab with the latest saved jobs
                if ("Saved Jobs".equals(tabName) && selectedComponent instanceof JobView) {
                    JobView jobView = (JobView) selectedComponent;
                    jobView.updateJobsList(controller.getSavedJobs());
                }
                // Don't update Find Jobs tab when switching to it
            }
        });
        
        return pane;
    }

    /**
     * Creates and adds the custom menu bar to the frame.
     */
    private void createCustomMenu() {
        // Create our custom menu bar
        customMenuBar = new CustomMenuBar();
        
        // Add the custom menu to the top of the frame
        getContentPane().add(customMenuBar, BorderLayout.NORTH);
    }

    /**
     * Maps action listeners to menu items.
     * This needs to be called after controller is set.
     */
    private void mapMenuEvents() {
        if (controller == null || customMenuBar == null) {
            return;
        }
        
        // Exit Functionality
        customMenuBar.getExitItem().addActionListener(exitEvent -> {
            exitHelper();
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
    }
    
    /**
     * Applies the specified color theme to all components in the view hierarchy.
     * @param theme The color theme to apply
     */
    private void applyTheme(ColorTheme theme) {
        // Apply theme to main frame components
        getContentPane().setBackground(theme.getBackground());
        mainPane.setBackground(theme.getBackground());
        
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

    /**
     * Notifies the user with a message dialog.
     * 
     * @param message The message to display
     */
    @Override
    public void notifyUser(String message) {
        ImageIcon warningIcon = IconLoader.loadIcon("images/warning.png");
        JOptionPane.showMessageDialog(this,
            message,
            "User Alert",
            JOptionPane.WARNING_MESSAGE,
            warningIcon);
    }

    /**
     * Sets the visibility of the main view.
     */
    @Override
    public void run() {
        if (controller == null) {
            System.err.println("Error: Controller must be set before running the view");
            return;
        }
        
        setVisible(true);
        
        // Apply theme once more after becoming visible
        SwingUtilities.invokeLater(() -> {
            applyTheme(theme);
        });
    }

    /**
     * Exits the application with a confirmation dialog.
     */
    public void exitHelper() {
        if (controller == null) {
            System.exit(0);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(
                    tabbedPane,
                    "Your jobs will be auto-saved to SavedJobs.csv.\nAre you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    IconLoader.loadIcon("images/warning.png")
                );
                if (result == JOptionPane.YES_OPTION) {
                    // Unregister as an alert observer before exiting
                    controller.unregisterAlertObserver(this);
                    System.exit(0);
                }
    }
}
