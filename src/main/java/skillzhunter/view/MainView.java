package skillzhunter.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import skillzhunter.controller.IController;

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
        setupExitKeyAction();
    }

    //These next two methods are all about setting up the enter key to search
    private void setupExitKeyAction() {
        Action exitAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show confirmation dialog, just like in the menu
                int result = JOptionPane.showConfirmDialog(
                    tabbedPane,
                    "Are you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION
                );
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        };

        JComponent rootPane = getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "exit");
        ActionMap actionMap = rootPane.getActionMap();
        actionMap.put("exit", exitAction);
    }

    //Helper method to disable Enter key default behavior in text components
    private void disableEnterKeyTraversalIn(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextField || comp instanceof TextField) {
                comp.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_Q) {
                            System.exit(0);
                            e.consume(); // Prevent default handling
                        }
                    }
                });
            }
            // Recursively process nested containers
            if (comp instanceof Container) {
                disableEnterKeyTraversalIn((Container) comp);
            }
        }
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

    public void notifyUser(String message) {
        ImageIcon warningIcon = IconLoader.loadIcon("images/warning.png");
        JOptionPane.showMessageDialog(this,
            message,
            "User Alert",
            JOptionPane.WARNING_MESSAGE,
            warningIcon);
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