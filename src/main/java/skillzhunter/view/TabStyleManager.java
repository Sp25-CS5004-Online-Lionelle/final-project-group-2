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
    private JTabbedPane tabbedPane;
    private JLabel[] tabLabels;
    private ColorTheme theme;
    
    /**
     * Creates a new TabStyleManager for the given tabbed pane.
     * 
     * @param tabbedPane The JTabbedPane to style
     * @param tabNames Array of tab names
     */
    public TabStyleManager(JTabbedPane tabbedPane, String[] tabNames) {
        this.tabbedPane = tabbedPane;
        this.tabLabels = new JLabel[tabNames.length];
        
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
     * Updates tab styling based on the current theme.
     * 
     * @param theme The ColorTheme to apply
     */
    public void applyTheme(ColorTheme theme) {
        this.theme = theme;
        
        // Set tabbed pane background
        tabbedPane.setBackground(theme.background);
        tabbedPane.setForeground(theme.foreground);
        
        // Update tab styles
        updateTabStyles();
    }
    
    /**
     * Updates the appearance of all tabs based on selection state and theme.
     */
    private void updateTabStyles() {
        if (theme == null) return;
        
        int selectedIndex = tabbedPane.getSelectedIndex();
        
        // Update each tab
        for (int i = 0; i < tabLabels.length; i++) {
            if (i == selectedIndex) {
                // Selected tab
                tabLabels[i].setBackground(theme.selectedTabBackground);
                tabLabels[i].setForeground(theme.selectedTabForeground);
                tabLabels[i].setFont(tabLabels[i].getFont().deriveFont(Font.BOLD));
            } else {
                // Unselected tab
                tabLabels[i].setBackground(theme.unselectedTabBackground);
                tabLabels[i].setForeground(theme.unselectedTabForeground);
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