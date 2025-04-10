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
    private final boolean isMacOS;

    // Platform-specific colors for Windows/Linux
    private final Color WIN_SELECTED_BG_LIGHT = new Color(202,220,245);
    private final Color WIN_SELECTED_FG_LIGHT = Color.BLACK;
    private final Color WIN_UNSELECTED_BG_LIGHT = new Color(235, 235, 235);
    private final Color WIN_UNSELECTED_FG_LIGHT = Color.BLACK;

    private final Color WIN_SELECTED_BG_DARK = new Color(197,218,240);
    private final Color WIN_SELECTED_FG_DARK = Color.BLACK;
    private final Color WIN_UNSELECTED_BG_DARK = new Color(43, 43, 43);
    private final Color WIN_UNSELECTED_FG_DARK = new Color(187, 187, 187);

    // Platform-specific colors for macOS - revised based on screenshot
    private final Color MAC_SELECTED_BG_LIGHT = new Color(255, 255, 255);
    private final Color MAC_SELECTED_FG_LIGHT = new Color(0, 120, 215);
    private final Color MAC_UNSELECTED_BG_LIGHT = new Color(235, 235, 235);
    private final Color MAC_UNSELECTED_FG_LIGHT = Color.DARK_GRAY;

    // Updated Mac dark mode colors to match screenshot
    private final Color MAC_SELECTED_BG_DARK = new Color(201,201,201); // Match the background
    private final Color MAC_SELECTED_FG_DARK = Color.BLACK; // White text for selected tab
    private final Color MAC_UNSELECTED_BG_DARK = new Color(33, 33, 33);
    private final Color MAC_UNSELECTED_FG_DARK = new Color(180, 180, 180); // Lighter grey for unselected

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
            tabPaneBackground = (theme == ColorTheme.DARK) ?
                new Color(33, 33, 33) : new Color(245, 245, 245);
        } else {
            tabPaneBackground = (theme == ColorTheme.DARK) ?
                new Color(43, 43, 43) : new Color(240, 240, 240);
        }

        // Apply background color to tabbedPane
        tabbedPane.setBackground(tabPaneBackground);
        tabbedPane.setForeground(theme == ColorTheme.DARK ? Color.WHITE : Color.BLACK);

        // Update tab styles
        updateTabStyles();
    }

    /**
     * Gets the appropriate tab colors based on platform and theme
     *
     * @param isSelected Whether this is for a selected tab
     * @return Array containing [background color, foreground color]
     */
    private Color[] getTabColors(boolean isSelected) {
        Color bg, fg;

        if (isMacOS) {
            // macOS coloring
            if (theme == ColorTheme.DARK) {
                if (isSelected) {
                    bg = MAC_SELECTED_BG_DARK;
                    fg = MAC_SELECTED_FG_DARK;
                } else {
                    bg = MAC_UNSELECTED_BG_DARK;
                    fg = MAC_UNSELECTED_FG_DARK;
                }
            } else {
                if (isSelected) {
                    bg = MAC_SELECTED_BG_LIGHT;
                    fg = MAC_SELECTED_FG_LIGHT;
                } else {
                    bg = MAC_UNSELECTED_BG_LIGHT;
                    fg = MAC_UNSELECTED_FG_LIGHT;
                }
            }
        } else {
            // Windows/Linux coloring - also set selected tab text to white in dark mode
            if (theme == ColorTheme.DARK) {
                if (isSelected) {
                    bg = WIN_SELECTED_BG_DARK;
                    fg = WIN_SELECTED_FG_DARK;
                } else {
                    bg = WIN_UNSELECTED_BG_DARK;
                    fg = WIN_UNSELECTED_FG_DARK;
                }
            } else {
                if (isSelected) {
                    bg = WIN_SELECTED_BG_LIGHT;
                    fg = WIN_SELECTED_FG_LIGHT;
                } else {
                    bg = WIN_UNSELECTED_BG_LIGHT;
                    fg = WIN_UNSELECTED_FG_LIGHT;
                }
            }
        }

        return new Color[] { bg, fg };
    }

    /**
     * Updates the appearance of all tabs based on selection state and theme.
     */
    private void updateTabStyles() {
        if (theme == null) return;

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
