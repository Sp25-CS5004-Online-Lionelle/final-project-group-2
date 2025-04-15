package skillzhunter.view;

import java.awt.FlowLayout;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.SwingConstants;

/**
 * Custom menu bar implementation that provides consistent styling across platforms.
 * Used as an alternative to JMenuBar for better styling control, especially on macOS.
 */
public class CustomMenuBar extends JPanel {
    /** Setting button. */
    private final JButton settingsButton;
    /** Popup menu for settings. */
    private final JPopupMenu settingsMenu;
    /** exit menu item. */
    private JMenuItem exitItem;
    /** Light mode menu item. */
    private JMenuItem lightModeItem;
    /** Dark mode menue item. */
    private JMenuItem darkModeItem;
    /** Color for the bottom line. */
    private Color lineColor = new Color(0, 183, 195); // Teal color matching the button normal from DARK theme
    /** Thickness of the bottom line. */
    private int lineThickness = 2; // Thickness of the bottom line in pixels
    /** Title label showing "Skillz Hunter" */
    private JLabel titleLabel;
    /** Tagline label showing the slogan */
    private JLabel taglineLabel;
    /** Width of the settings button area */
    private static final int SETTINGS_WIDTH = 40;
    
    /**
     * Creates a new CustomMenuBar with standard menu options.
     */
    public CustomMenuBar() {
        // Use BorderLayout for component positioning
        setLayout(new BorderLayout());
        
        // Set padding but leave bottom for the line
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5 + lineThickness, 10));
        
        // Create settings button
        settingsButton = createSettingsButton();
        
        // Create popup menu
        settingsMenu = new JPopupMenu();
        
        // Build menu structure
        buildMenuStructure();
        
        // Create a panel for the button to control its position on the left
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(settingsButton);
        
        // Create a matching empty panel for the right to balance the layout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        // Add invisible component with same size as settings button for balance
        rightPanel.add(Box.createRigidArea(new Dimension(SETTINGS_WIDTH, 24)));
        
        // Add the panels to the WEST and EAST positions
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        
        // Create title panel for app name and tagline
        JPanel titlePanel = new JPanel(new BorderLayout(0, 2));
        titlePanel.setOpaque(false);
        
        // Create title label
        titleLabel = new JLabel("Skillz Hunter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        
        // Create tagline label with smaller font
        taglineLabel = new JLabel("Find your next career opportunity", SwingConstants.CENTER);
        taglineLabel.setFont(new Font("Dialog", Font.ITALIC, 12));
        
        // Add labels to title panel
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(taglineLabel, BorderLayout.SOUTH);
        
        // Add title panel to the center of the menu bar
        add(titlePanel, BorderLayout.CENTER);
    }
    
    /**
     * Creates the settings button with the default icon.
     * @return The created settings button
     */
    private JButton createSettingsButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Set fixed size for the button to ensure consistent spacing
        button.setPreferredSize(new Dimension(SETTINGS_WIDTH, 24));
        
        // Set default icon using IconLoader
        ImageIcon icon = IconLoader.loadIcon("images/SettingsIcon.png", 24, 24);
        if (icon != null) {
            button.setIcon(icon);
        } else {
            button.setText("⚙");
            button.setFont(new java.awt.Font(button.getFont().getName(), java.awt.Font.PLAIN, 20));
        }
        
        // Show menu when clicked
        button.addActionListener(e -> settingsMenu.show(button, 0, button.getHeight()));
        
        return button;
    }
    
    /**
     * Override paintComponent to draw the teal line at the bottom.
     * @param g The Graphics object to paint on
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw the teal line at the bottom
        g.setColor(lineColor);
        g.fillRect(0, getHeight() - lineThickness, getWidth(), lineThickness);
    }
    
    /**
     * Builds the menu structure with all necessary items.
     */
    private void buildMenuStructure() {
        // View mode items
        JMenu viewMenu = new JMenu("View Mode");
        
        // Use IconLoader to load light mode and dark mode icons
        ImageIcon lightModeIcon = IconLoader.loadIcon("images/LightModeIcon.png", 12, 12);
        lightModeItem = new JMenuItem("Light Mode");
        if (lightModeIcon != null) {
            lightModeItem.setIcon(lightModeIcon);
        }
        
        ImageIcon darkModeIcon = IconLoader.loadIcon("images/DarkModeIcon.png", 12, 12);
        darkModeItem = new JMenuItem("Dark Mode");
        if (darkModeIcon != null) {
            darkModeItem.setIcon(darkModeIcon);
        }
        
        viewMenu.add(lightModeItem);
        viewMenu.add(darkModeItem);
        
        // Exit menu item
        exitItem = new JMenuItem("Exit");
        
        // Add items to menu
        // settingsMenu.add(downloadMenu);
        settingsMenu.add(viewMenu);
        settingsMenu.addSeparator();
        settingsMenu.add(exitItem);
    }
    
    /**
     * Applies the specified theme to the menu components.
     * 
     * @param theme The color theme to apply
     */
    public void applyTheme(ColorTheme theme) {
        // Apply theme to menu panel
        setBackground(theme.getBackground());
        
        // Update title and tagline colors based on theme
        titleLabel.setForeground(theme.getTitleTextColor());
        taglineLabel.setForeground(theme.getTaglineTextColor());
        
        // Update line color based on theme - using the button normal color
        lineColor = theme.getButtonNormal();
        
        // Update settings icon based on theme using IconLoader
        // Check background brightness to determine icon color
        boolean isDarkTheme = isDarkColor(theme.getBackground());
        
        if (isDarkTheme) {
            ImageIcon whiteSettingsIcon = IconLoader.loadIcon("images/settingswhite.png", 24, 24);
            if (whiteSettingsIcon != null) {
                settingsButton.setIcon(whiteSettingsIcon);
            }
        } else {
            ImageIcon normalSettingsIcon = IconLoader.loadIcon("images/SettingsIcon.png", 24, 24);
            if (normalSettingsIcon != null) {
                settingsButton.setIcon(normalSettingsIcon);
            }
        }
        
        // Theme popup menus - this is system-wide and will affect all popups
        Color menuBgColor = isDarkTheme ? theme.getMenuBarBackgroundDark() : theme.getMenuBarBackgroundLight();
        Color menuFgColor = isDarkTheme ? theme.getMenuBarForegroundDark() : theme.getMenuBarForegroundLight();
        
        UIManager.put("PopupMenu.background", menuBgColor);
        UIManager.put("PopupMenu.foreground", menuFgColor);
        UIManager.put("MenuItem.background", menuBgColor);
        UIManager.put("MenuItem.foreground", menuFgColor);
        UIManager.put("Menu.background", menuBgColor);
        UIManager.put("Menu.foreground", menuFgColor);
        
        // Repaint to show the updated line color
        repaint();
    }
    
    /**
     * Utility method to determine if a color is "dark" for theming decisions.
     * Uses color brightness to make the determination.
     *
     * @param color The color to check
     * @return true if the color is dark, false otherwise
     */
    private boolean isDarkColor(Color color) {
        // Calculate brightness using common formula
        double brightness = (0.299 * color.getRed() + 
                            0.587 * color.getGreen() + 
                            0.114 * color.getBlue()) / 255;
                            
        // If brightness is less than 0.5, consider it a dark color
        return brightness < 0.5;
    }
    
    /**
     * Sets the thickness of the bottom line.
     * 
     * @param thickness Line thickness in pixels
     */
    public void setLineThickness(int thickness) {
        this.lineThickness = thickness;
        // Update the bottom border padding to match the new thickness
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5 + thickness, 10));
        repaint();
    }
    
    // Getters for menu items so listeners can be attached
    
    /**
     * retrieves exit item.
     * @return The exit menu item
     */
    public JMenuItem getExitItem() {
        return exitItem;
    }
    

    
    /**
     * retrieves light mode item.
     * @return The light mode menu item
     */
    public JMenuItem getLightModeItem() {
        return lightModeItem;
    }
    
    /**
     * retrieves dark mode item.
     * @return The dark mode menu item
     */
    public JMenuItem getDarkModeItem() {
        return darkModeItem;
    }
}
