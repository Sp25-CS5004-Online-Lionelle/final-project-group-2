package skillzhunter.view;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.net.URL;

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
    
    private JButton settingsButton;
    private JPopupMenu settingsMenu;
    private JMenuItem exitItem;
    private JMenuItem csvDownloadItem;
    private JMenuItem jsonDownloadItem;
    private JMenuItem xmlDownloadItem;
    private JMenuItem lightModeItem;
    private JMenuItem darkModeItem;
    private Color lineColor = new Color(0, 183, 195); // Teal color matching the button normal from DARK theme
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
     */
    private JButton createSettingsButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Set default icon
        try {
            URL imgURL = getClass().getClassLoader().getResource("images/SettingsIcon.png");
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            e.printStackTrace();
            button.setText("⚙");
            button.setFont(new java.awt.Font(button.getFont().getName(), java.awt.Font.PLAIN, 20));
        }
        
        // Show menu when clicked
        button.addActionListener(e -> settingsMenu.show(button, 0, button.getHeight()));
        
        return button;
    }
    
    /**
     * Override paintComponent to draw the teal line at the bottom
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
        // Download submenu
        JMenu downloadMenu = new JMenu("Download Data");
        csvDownloadItem = new JMenuItem("csv");
        jsonDownloadItem = new JMenuItem("json");
        xmlDownloadItem = new JMenuItem("xml");
        downloadMenu.add(xmlDownloadItem);
        downloadMenu.add(jsonDownloadItem);
        downloadMenu.add(csvDownloadItem);
        
        // View mode items
        JMenu viewMenu = new JMenu("View Mode");
        
        try {
            URL lightModeImgURL = getClass().getClassLoader().getResource("images/LightModeIcon.png");
            if (lightModeImgURL != null) {
                ImageIcon lightModeIcon = new ImageIcon(lightModeImgURL);
                Image scaledLightModeImage = lightModeIcon.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
                lightModeItem = new JMenuItem(new ImageIcon(scaledLightModeImage));
                lightModeItem.setText("Light Mode");
            } else {
                lightModeItem = new JMenuItem("Light Mode");
            }

            URL darkModeImgURL = getClass().getClassLoader().getResource("images/DarkModeIcon.png");
            if (darkModeImgURL != null) {
                ImageIcon darkModeIcon = new ImageIcon(darkModeImgURL);
                Image scaledDarkModeImage = darkModeIcon.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
                darkModeItem = new JMenuItem(new ImageIcon(scaledDarkModeImage));
                darkModeItem.setText("Dark Mode");
            } else {
                darkModeItem = new JMenuItem("Dark Mode");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lightModeItem = new JMenuItem("Light Mode");
            darkModeItem = new JMenuItem("Dark Mode");
        }
        
        viewMenu.add(lightModeItem);
        viewMenu.add(darkModeItem);
        
        // Exit menu item
        exitItem = new JMenuItem("Exit");
        
        // Add items to menu
        settingsMenu.add(downloadMenu);
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
        setBackground(theme.background);
        
        // Update line color based on theme
        if (theme == ColorTheme.DARK) {
            // In dark mode, use the teal color from the dark theme
            lineColor = theme.buttonNormal;
        } else {
            // In light mode, use the blue color from the light theme
            lineColor = theme.buttonNormal;
        }
        
        // Update settings icon based on theme
        if (theme == ColorTheme.DARK) {
            try {
                URL imgURL = getClass().getClassLoader().getResource("images/settingswhite.png");
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                    settingsButton.setIcon(new ImageIcon(scaledImage));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                URL imgURL = getClass().getClassLoader().getResource("images/SettingsIcon.png");
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                    settingsButton.setIcon(new ImageIcon(scaledImage));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Theme popup menus - this is system-wide and will affect all popups
        UIManager.put("PopupMenu.background", theme == ColorTheme.DARK ? 
            theme.menuBarBackgroundDark : theme.menuBarBackgroundLight);
        UIManager.put("PopupMenu.foreground", theme == ColorTheme.DARK ? 
            theme.menuBarForegroundDark : theme.menuBarForegroundLight);
        UIManager.put("MenuItem.background", theme == ColorTheme.DARK ? 
            theme.menuBarBackgroundDark : theme.menuBarBackgroundLight);
        UIManager.put("MenuItem.foreground", theme == ColorTheme.DARK ? 
            theme.menuBarForegroundDark : theme.menuBarForegroundLight);
        UIManager.put("Menu.background", theme == ColorTheme.DARK ? 
            theme.menuBarBackgroundDark : theme.menuBarBackgroundLight);
        UIManager.put("Menu.foreground", theme == ColorTheme.DARK ? 
            theme.menuBarForegroundDark : theme.menuBarForegroundLight);
        
        // Repaint to show the updated line color
        repaint();
    }
    
    /**
     * Sets the thickness of the bottom line
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
     * @return The exit menu item
     */
    public JMenuItem getExitItem() {
        return exitItem;
    }
    
    /**
     * @return The CSV download menu item
     */
    public JMenuItem getCsvDownloadItem() {
        return csvDownloadItem;
    }
    
    /**
     * @return The JSON download menu item
     */
    public JMenuItem getJsonDownloadItem() {
        return jsonDownloadItem;
    }
    
    /**
     * @return The XML download menu item
     */
    public JMenuItem getXmlDownloadItem() {
        return xmlDownloadItem;
    }
    
    /**
     * @return The light mode menu item
     */
    public JMenuItem getLightModeItem() {
        return lightModeItem;
    }
    
    /**
     * @return The dark mode menu item
     */
    public JMenuItem getDarkModeItem() {
        return darkModeItem;
    }
}