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

import skillzhunter.controller.IController;

public class MainView extends JFrame implements IView {
    /** Main Pane. */
    private final JPanel mainPane = new JPanel();
    /** Find Job Tab. */
    private JobView findJobTab;
    /** Saved Job Tab. */
    private JobView savedJobTab; // Removed final modifier
    /** Tabbed Pane. */
    private JTabbedPane tabbedPane;
    /** Tab Style Manager. */
    private TabStyleManager tabStyleManager;
    /** Controller. */
    private final IController controller;
    /** Custom Menu Bar. */
    private CustomMenuBar customMenuBar;
    /** Theme. */
    private ColorTheme theme;

    /**
     * Creates main view and pane for model.
     * 
     * @param controller The controller instance
     */
    public MainView(IController controller) {
        super("Jobz Hunter App");
        this.controller = controller;
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

        try {
            // First get the saved jobs tab from the controller (already created)
            savedJobTab = controller.getSavedJobsTab();
            
            // Create find jobs tab
            findJobTab = new FindJobTab(controller);
            
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
        
        // Add change listener to update the selected tab
        pane.addChangeListener(e -> {
            // Get the selected tab
            int selectedIndex = pane.getSelectedIndex();
            if (selectedIndex >= 0) {
                Component selectedComponent = pane.getComponentAt(selectedIndex);
                
                // If it's a JobView, update its job list
                if (selectedComponent instanceof JobView) {
                    JobView jobView = (JobView) selectedComponent;
                    jobView.updateJobsList(controller.getSavedJobs());
                }
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
        
        // Map menu events
        mapMenuEvents();
    }

    /**
     * Maps action listeners to menu items.
     */
    private void mapMenuEvents() {
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
        int result = JOptionPane.showConfirmDialog(
                    tabbedPane,
                    "Your jobs will be auto-saved to SavedJobs.csv.\nAre you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    IconLoader.loadIcon("images/warning.png")
                );
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
    }
}
