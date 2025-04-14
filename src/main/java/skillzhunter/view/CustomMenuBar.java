package skillzhunter.view;

import java.awt.FlowLayout;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

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
    
    /**
     * Creates a new CustomMenuBar with standard menu options.
     */
    public CustomMenuBar() {
        // Use LEFT alignment instead of RIGHT
        setLayout(new FlowLayout(FlowLayout.LEFT));
        
        // Set padding but leave bottom for the line
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5 + lineThickness, 10));
        
        // Create settings button
        settingsButton = createSettingsButton();
        
        // Create popup menu
        settingsMenu = new JPopupMenu();
        
        // Build menu structure
        buildMenuStructure();
        
        // Add button to panel
        add(settingsButton);
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
        
        // Update line color based on theme
        if (theme == ColorTheme.DARK) {
            // In dark mode, use the teal color from the dark theme
            lineColor = theme.getButtonNormal();
        } else {
            // In light mode, use the blue color from the light theme
            lineColor = theme.getButtonNormal();
        }
        
        // Update settings icon based on theme using IconLoader
        if (theme == ColorTheme.DARK) {
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
        UIManager.put("PopupMenu.background", theme == ColorTheme.DARK
            ? theme.getMenuBarBackgroundDark() : theme.getMenuBarBackgroundLight());
        UIManager.put("PopupMenu.foreground", theme == ColorTheme.DARK
            ? theme.getMenuBarForegroundDark() : theme.getMenuBarForegroundLight());
        UIManager.put("MenuItem.background", theme == ColorTheme.DARK
            ? theme.getMenuBarBackgroundDark() : theme.getMenuBarBackgroundLight());
        UIManager.put("MenuItem.foreground", theme == ColorTheme.DARK
            ? theme.getMenuBarForegroundDark() : theme.getMenuBarForegroundLight());
        UIManager.put("Menu.background", theme == ColorTheme.DARK 
            ? theme.getMenuBarBackgroundDark() : theme.getMenuBarBackgroundLight());
        UIManager.put("Menu.foreground", theme == ColorTheme.DARK
            ? theme.getMenuBarForegroundDark() : theme.getMenuBarForegroundLight());
        
        // Repaint to show the updated line color
        repaint();
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
