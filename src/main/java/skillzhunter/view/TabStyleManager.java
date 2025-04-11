package skillzhunter.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class dedicated to managing tab styling.
 * Handles the creation and styling of tabs based on the current theme.
 */
public class TabStyleManager {
    /** pane for tabs. */
    private final JTabbedPane tabbedPane;
    /** labels for tabs. */
    private final JLabel[] tabLabels;
    /** theme for tabs. */
    private ColorTheme theme;
    /** macOS flag. */
    private final boolean isMacOS;
    
    /**
     * Creates a new TabStyleManager for the given tabbed pane.
     * 
     * @param tabbedPane The JTabbedPane to style
     * @param tabNames Array of tab names
     */
    public TabStyleManager(JTabbedPane tabbedPane, String[] tabNames) {
        this.tabbedPane = tabbedPane;
        this.tabLabels = new JLabel[tabNames.length];
        
        // Detect if running on macOS
        this.isMacOS = isMacOSPlatform();
        
        // Create and set up tab labels
        for (int i = 0; i < tabNames.length; i++) {
            tabLabels[i] = new JLabel(tabNames[i]);
            tabLabels[i].setOpaque(true);
            tabLabels[i].setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
            tabbedPane.setTabComponentAt(i, tabLabels[i]);
        }
        
        // Set hand cursor for tabs
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tabbedPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                tabbedPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        // Add change listener to update tab appearance when selection changes
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateTabStyles();
            }
        });
        
        // Make the initial selected tab bold
        SwingUtilities.invokeLater(this::updateTabStyles);
    }
    
    /**
     * Checks if the application is running on macOS platform.
     * 
     * @return true if running on macOS, false otherwise
     */
    private boolean isMacOSPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("mac");
    }
    
    /**
     * Updates tab styling based on the current theme.
     * 
     * @param theme The ColorTheme to apply
     */
    public void applyTheme(ColorTheme theme) {
        this.theme = theme;
        
        // Set tabbed pane background color based on platform and theme
        Color tabPaneBackground;
        
        if (isMacOS) {
            tabPaneBackground = (theme == ColorTheme.DARK)
                                ? theme.macTabPaneBgDark : theme.macTabPaneBgLight;
        } else {
            tabPaneBackground = (theme == ColorTheme.DARK)
                                ? theme.winTabPaneBgDark 
                                : theme.winTabPaneBgLight;
        }
        
        // Apply background color to tabbedPane
        tabbedPane.setBackground(tabPaneBackground);
        tabbedPane.setForeground(theme == ColorTheme.DARK ? Color.WHITE : Color.BLACK);
        
        // Update tab styles
        updateTabStyles();
    }
    
    /**
     * Gets the appropriate tab colors based on platform and theme.
     * 
     * @param isSelected Whether this is for a selected tab
     * @return Array containing [background color, foreground color]
     */
    private Color[] getTabColors(boolean isSelected) {
        Color bg; 
        Color fg;
        
        if (isMacOS) {
            // macOS coloring
            if (theme == ColorTheme.DARK) {
                if (isSelected) {
                    bg = theme.macSelectedBgDark;
                    fg = theme.macSelectedFgDark;
                } else {
                    bg = theme.macUnselectedBgDark;
                    fg = theme.macUnselectedFgDark;
                }
            } else {
                if (isSelected) {
                    bg = theme.macSelectedBgLight;
                    fg = theme.macSelectedFgLight; 
                } else {
                    bg = theme.macUnselectedBgLight;
                    fg = theme.macUnselectedFgLight;
                }
            }
        } else {
            // Windows/Linux coloring
            if (theme == ColorTheme.DARK) {
                if (isSelected) {
                    bg = theme.winSelectedBgDark;
                    fg = theme.winSelectedFgDark;
                } else {
                    bg = theme.winUnselectedBgDark;
                    fg = theme.winUnselectedFgDark;
                }
            } else {
                if (isSelected) {
                    bg = theme.winSelectedBgLight;
                    fg = theme.winSelectedFgLight;
                } else {
                    bg = theme.winUnselectedBgLight;
                    fg = theme.winUnselectedFgLight;
                }
            }
        }
        
        return new Color[] {bg, fg};
    }
    
    /**
     * Updates the appearance of all tabs based on selection state and theme.
     */
    private void updateTabStyles() {
        if (theme == null) {
            return;
        }
        
        int selectedIndex = tabbedPane.getSelectedIndex();
        
        // Update each tab
        for (int i = 0; i < tabLabels.length; i++) {
            boolean isSelected = (i == selectedIndex);
            Color[] colors = getTabColors(isSelected);
            
            tabLabels[i].setBackground(colors[0]);
            tabLabels[i].setForeground(colors[1]);
            
            // Bold font for selected tab
            if (isSelected) {
                tabLabels[i].setFont(tabLabels[i].getFont().deriveFont(Font.BOLD));
            } else {
                tabLabels[i].setFont(tabLabels[i].getFont().deriveFont(Font.PLAIN));
            }
        }
    }
    
    /**
     * Gets the currently selected tab index.
     * 
     * @return The index of the selected tab
     */
    public int getSelectedTabIndex() {
        return tabbedPane.getSelectedIndex();
    }
    
    /**
     * Sets the selected tab.
     * 
     * @param index The index of the tab to select
     */
    public void setSelectedTab(int index) {
        if (index >= 0 && index < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(index);
        }
    }
}
