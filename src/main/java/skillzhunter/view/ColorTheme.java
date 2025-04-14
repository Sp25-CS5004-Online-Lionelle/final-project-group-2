package skillzhunter.view;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages color themes for the application with predefined schemes.
 * - Light mode uses Bootstrap 5 colors with Spotify green for success buttons
 * - Dark mode uses a teal-based color scheme with yellow success buttons
 */
public class ColorTheme {
    /** The map of color names to their values for this theme. */
    private final Map<String, Color> colors;
    
    /** The light color theme for the application. */
    public static final ColorTheme LIGHT = createLightTheme();
    
    /** The dark color theme for the application. */
    public static final ColorTheme DARK = createDarkTheme();
    
    /**
     * Constructor for ColorTheme.
     * 
     * @param colors Map of color names to Color values
     */
    private ColorTheme(Map<String, Color> colors) {
        this.colors = new HashMap<>(colors);
    }
    
    /**
     * Creates the light color theme.
     * 
     * @return The light color theme
     */
    private static ColorTheme createLightTheme() {
        Map<String, Color> colors = new HashMap<>();
        
        // Button colors - Bootstrap 5
        colors.put("buttonNormal", new Color(13, 110, 253));      // Primary button normal: Bootstrap blue (#0d6efd)
        colors.put("buttonHover", new Color(11, 94, 215));       // Primary button hover: Darker blue (#0b5ed7)
        colors.put("secondaryButtonNormal", new Color(108, 117, 125));     // Secondary button normal: Bootstrap gray (#6c757d)
        colors.put("secondaryButtonHover", new Color(92, 99, 106));       // Secondary button hover: Darker gray (#5c636a)
        colors.put("successButtonNormal", new Color(30, 215, 96));       // Success button normal: Spotify green (#1ED760)
        colors.put("successButtonHover", new Color(25, 185, 84));       // Success button hover: Darker Spotify green
        colors.put("dangerButtonNormal", new Color(220, 53, 69));       // Danger button normal: Bootstrap red (#dc3545)
        colors.put("dangerButtonHover", new Color(187, 45, 59));       // Danger button hover: Darker red (#bb2d3b)
        colors.put("warningButtonNormal", new Color(255, 193, 7));       // Warning button normal: Bootstrap yellow (#ffc107)
        colors.put("warningButtonHover", new Color(255, 202, 44));      // Warning button hover: Brighter yellow (#ffca2c)
        colors.put("infoButtonNormal", new Color(13, 202, 240));      // Info button normal: Bootstrap cyan (#0dcaf0)
        colors.put("infoButtonHover", new Color(49, 210, 242));      // Info button hover: Brighter cyan (#31d2f2)
        
        // General colors - ORIGINAL VALUES FROM LIGHT THEME
        colors.put("background", new Color(245, 245, 245));     // Background: Light gray (original)
        colors.put("foreground", Color.BLACK);                  // Foreground: Black text
        colors.put("fieldBackground", Color.WHITE);                  // Field background: White 
        colors.put("fieldForeground", Color.BLACK);                  // Field foreground: Black
        colors.put("buttonForeground", Color.WHITE);                  // Button foreground: White text
        colors.put("labelForeground", Color.BLACK);                  // Label foreground: Black
        
        // Menu bar colors - ORIGINAL VALUES FROM LIGHT THEME
        colors.put("menuBarBackgroundLight", Color.WHITE);                // Menu bar background - Light theme
        colors.put("menuBarForegroundLight", Color.BLACK);                // Menu bar foreground - Light theme
        colors.put("menuBarBackgroundDark", new Color(43, 43, 43));      // Menu bar background - Dark theme
        colors.put("menuBarForegroundDark", Color.WHITE);                // Menu bar foreground - Dark theme
        
        // Windows/Linux tab colors - ORIGINAL VALUES FROM LIGHT THEME
        colors.put("winSelectedBgLight", new Color(202, 220, 245));   // WIN_SELECTED_BG_LIGHT
        colors.put("winSelectedFgLight", Color.BLACK);                // WIN_SELECTED_FG_LIGHT
        colors.put("winUnselectedBgLight", new Color(235, 235, 235));   // WIN_UNSELECTED_BG_LIGHT
        colors.put("winUnselectedFgLight", Color.BLACK);                // WIN_UNSELECTED_FG_LIGHT
        
        colors.put("winSelectedBgDark", new Color(197, 218, 240));   // WIN_SELECTED_BG_DARK
        colors.put("winSelectedFgDark", Color.BLACK);                // WIN_SELECTED_FG_DARK
        colors.put("winUnselectedBgDark", new Color(43, 43, 43));      // WIN_UNSELECTED_BG_DARK
        colors.put("winUnselectedFgDark", Color.WHITE);                // WIN_UNSELECTED_FG_DARK
        
        // macOS tab colors - ORIGINAL VALUES FROM LIGHT THEME
        colors.put("macSelectedBgLight", new Color(255, 255, 255));   // MAC_SELECTED_BG_LIGHT
        colors.put("macSelectedFgLight", new Color(0, 120, 215));     // MAC_SELECTED_FG_LIGHT - Windows blue
        colors.put("macUnselectedBgLight", new Color(235, 235, 235));   // MAC_UNSELECTED_BG_LIGHT
        colors.put("macUnselectedFgLight", Color.DARK_GRAY);            // MAC_UNSELECTED_FG_LIGHT
        
        colors.put("macSelectedBgDark", new Color(201, 201, 201));   // MAC_SELECTED_BG_DARK
        colors.put("macSelectedFgDark", Color.BLACK);                // MAC_SELECTED_FG_DARK
        colors.put("macUnselectedBgDark", new Color(33, 33, 33));      // MAC_UNSELECTED_BG_DARK
        colors.put("macUnselectedFgDark", new Color(180, 180, 180));   // MAC_UNSELECTED_FG_DARK
        
        // Tab pane background colors - ORIGINAL VALUES FROM LIGHT THEME
        colors.put("winTabPaneBgLight", new Color(240, 240, 240));   // WIN_TABPANE_BG_LIGHT
        colors.put("winTabPaneBgDark", new Color(43, 43, 43));      // WIN_TABPANE_BG_DARK
        colors.put("macTabPaneBgLight", new Color(245, 245, 245));   // MAC_TABPANE_BG_LIGHT
        colors.put("macTabPaneBgDark", new Color(33, 33, 33));       // MAC_TABPANE_BG_DARK
        
        return new ColorTheme(colors);
    }
    
    /**
     * Creates the dark color theme.
     * 
     * @return The dark color theme
     */
    private static ColorTheme createDarkTheme() {
        Map<String, Color> colors = new HashMap<>();
        
        // Button colors - Teal-based with complementary colors (Option 2)
        colors.put("buttonNormal", new Color(0, 189, 195));       // Primary button normal: Teal (#00BDC3)
        colors.put("buttonHover", new Color(0, 165, 171));       // Primary button hover: Darker teal (#00A5AB)
        colors.put("secondaryButtonNormal", new Color(126, 87, 194));      // Secondary button normal: Purple (#7E57C2)
        colors.put("secondaryButtonHover", new Color(106, 70, 181));      // Secondary button hover: Darker purple (#6A46B5)
        colors.put("successButtonNormal", new Color(255, 222, 0));       // Success button normal: Yellow (#FFDE00)
        colors.put("successButtonHover", new Color(230, 200, 0));       // Success button hover: Darker yellow
        colors.put("dangerButtonNormal", new Color(239, 83, 80));       // Danger button normal: Red (#EF5350)
        colors.put("dangerButtonHover", new Color(229, 57, 53));       // Danger button hover: Darker red (#E53935)
        colors.put("warningButtonNormal", new Color(255, 202, 40));      // Warning button normal: Yellow (#FFCA28)
        colors.put("warningButtonHover", new Color(255, 179, 0));       // Warning button hover: Darker yellow (#FFB300)
        colors.put("infoButtonNormal", new Color(41, 182, 246));      // Info button normal: Blue (#29B6F6)
        colors.put("infoButtonHover", new Color(3, 155, 229));       // Info button hover: Darker blue (#039BE5)
        
        // General colors - ORIGINAL VALUES FROM DARK THEME
        colors.put("background", new Color(43, 43, 43));      // Background: Dark gray
        colors.put("foreground", new Color(240, 240, 240));   // Foreground: Light gray text
        colors.put("fieldBackground", new Color(60, 63, 65));      // Field background: Darker gray
        colors.put("fieldForeground", new Color(240, 240, 240));   // Field foreground: Light gray text
        colors.put("buttonForeground", Color.WHITE);                // Button foreground: White text
        colors.put("labelForeground", Color.WHITE);                // Label foreground: White
        
        // Menu bar colors - ORIGINAL VALUES FROM DARK THEME
        colors.put("menuBarBackgroundLight", Color.WHITE);                // Menu bar background - Light theme
        colors.put("menuBarForegroundLight", Color.BLACK);                // Menu bar foreground - Light theme
        colors.put("menuBarBackgroundDark", new Color(43, 43, 43));      // Menu bar background - Dark theme
        colors.put("menuBarForegroundDark", Color.WHITE);                // Menu bar foreground - Dark theme
        
        // Windows/Linux tab colors - ORIGINAL VALUES FROM DARK THEME
        colors.put("winSelectedBgLight", new Color(202, 220, 245));   // WIN_SELECTED_BG_LIGHT
        colors.put("winSelectedFgLight", Color.BLACK);                // WIN_SELECTED_FG_LIGHT
        colors.put("winUnselectedBgLight", new Color(235, 235, 235));   // WIN_UNSELECTED_BG_LIGHT
        colors.put("winUnselectedFgLight", Color.BLACK);                // WIN_UNSELECTED_FG_LIGHT
        
        colors.put("winSelectedBgDark", new Color(197, 218, 240));   // WIN_SELECTED_BG_DARK
        colors.put("winSelectedFgDark", Color.BLACK);                // WIN_SELECTED_FG_DARK
        colors.put("winUnselectedBgDark", new Color(43, 43, 43));      // WIN_UNSELECTED_BG_DARK
        colors.put("winUnselectedFgDark", Color.WHITE);                // WIN_UNSELECTED_FG_DARK
        
        // macOS tab colors - ORIGINAL VALUES FROM DARK THEME
        colors.put("macSelectedBgLight", new Color(255, 255, 255));   // MAC_SELECTED_BG_LIGHT
        colors.put("macSelectedFgLight", new Color(0, 120, 215));     // MAC_SELECTED_FG_LIGHT - Windows blue
        colors.put("macUnselectedBgLight", new Color(235, 235, 235));   // MAC_UNSELECTED_BG_LIGHT
        colors.put("macUnselectedFgLight", Color.DARK_GRAY);            // MAC_UNSELECTED_FG_LIGHT
        
        colors.put("macSelectedBgDark", new Color(201, 201, 201));   // MAC_SELECTED_BG_DARK
        colors.put("macSelectedFgDark", Color.BLACK);                // MAC_SELECTED_FG_DARK
        colors.put("macUnselectedBgDark", new Color(33, 33, 33));      // MAC_UNSELECTED_BG_DARK
        colors.put("macUnselectedFgDark", new Color(180, 180, 180));   // MAC_UNSELECTED_FG_DARK
        
        // Tab pane background colors - ORIGINAL VALUES FROM DARK THEME
        colors.put("winTabPaneBgLight", new Color(240, 240, 240));   // WIN_TABPANE_BG_LIGHT
        colors.put("winTabPaneBgDark", new Color(43, 43, 43));      // WIN_TABPANE_BG_DARK
        colors.put("macTabPaneBgLight", new Color(245, 245, 245));   // MAC_TABPANE_BG_LIGHT
        colors.put("macTabPaneBgDark", new Color(33, 33, 33));       // MAC_TABPANE_BG_DARK
        
        return new ColorTheme(colors);
    }
    
    /**
     * Gets a color by name.
     * 
     * @param name The name of the color
     * @return The Color, or null if not found
     */
    public Color getColor(String name) {
        return colors.get(name);
    }
    
    // Getters for all colors - for use in replacing direct field access
    
    /** Gets the primary button normal color. */
    public Color getButtonNormal() {
        return getColor("buttonNormal");
    }
    
    /** Gets the primary button hover color. */
    public Color getButtonHover() {
        return getColor("buttonHover");
    }
    
    /** Gets the secondary button normal color. */
    public Color getSecondaryButtonNormal() {
        return getColor("secondaryButtonNormal");
    }
    
    /** Gets the secondary button hover color. */
    public Color getSecondaryButtonHover() {
        return getColor("secondaryButtonHover");
    }
    
    /** Gets the success button normal color. */
    public Color getSuccessButtonNormal() {
        return getColor("successButtonNormal");
    }
    
    /** Gets the success button hover color. */
    public Color getSuccessButtonHover() {
        return getColor("successButtonHover");
    }
    
    /** Gets the danger button normal color. */
    public Color getDangerButtonNormal() {
        return getColor("dangerButtonNormal");
    }
    
    /** Gets the danger button hover color. */
    public Color getDangerButtonHover() {
        return getColor("dangerButtonHover");
    }
    
    /** Gets the warning button normal color. */
    public Color getWarningButtonNormal() {
        return getColor("warningButtonNormal");
    }
    
    /** Gets the warning button hover color. */
    public Color getWarningButtonHover() {
        return getColor("warningButtonHover");
    }
    
    /** Gets the info button normal color. */
    public Color getInfoButtonNormal() {
        return getColor("infoButtonNormal");
    }
    
    /** Gets the info button hover color. */
    public Color getInfoButtonHover() {
        return getColor("infoButtonHover");
    }
    
    /** Gets the background color. */
    public Color getBackground() {
        return getColor("background");
    }
    
    /** Gets the foreground color. */
    public Color getForeground() {
        return getColor("foreground");
    }
    
    /** Gets the field background color. */
    public Color getFieldBackground() {
        return getColor("fieldBackground");
    }
    
    /** Gets the field foreground color. */
    public Color getFieldForeground() {
        return getColor("fieldForeground");
    }
    
    /** Gets the button foreground color. */
    public Color getButtonForeground() {
        return getColor("buttonForeground");
    }
    
    /** Gets the label color. */
    public Color getLabelForeground() {
        return getColor("labelForeground");
    }
    
    /** Gets the menu bar background for light theme. */
    public Color getMenuBarBackgroundLight() {
        return getColor("menuBarBackgroundLight");
    }
    
    /** Gets the menu bar foreground for light theme. */
    public Color getMenuBarForegroundLight() {
        return getColor("menuBarForegroundLight");
    }
    
    /** Gets the menu bar background for dark theme. */
    public Color getMenuBarBackgroundDark() {
        return getColor("menuBarBackgroundDark");
    }
    
    /** Gets the menu bar foreground for dark theme. */
    public Color getMenuBarForegroundDark() {
        return getColor("menuBarForegroundDark");
    }
    
    /** Gets the selected tab background color for Windows/Linux in Light theme. */
    public Color getWinSelectedBgLight() {
        return getColor("winSelectedBgLight");
    }
    
    /** Gets the selected tab foreground color for Windows/Linux in Light theme. */
    public Color getWinSelectedFgLight() {
        return getColor("winSelectedFgLight");
    }
    
    /** Gets the unselected tab background color for Windows/Linux in Light theme. */
    public Color getWinUnselectedBgLight() {
        return getColor("winUnselectedBgLight");
    }
    
    /** Gets the unselected tab foreground color for Windows/Linux in Light theme. */
    public Color getWinUnselectedFgLight() {
        return getColor("winUnselectedFgLight");
    }
    
    /** Gets the selected tab background color for Windows/Linux in Dark theme. */
    public Color getWinSelectedBgDark() {
        return getColor("winSelectedBgDark");
    }
    
    /** Gets the selected tab foreground color for Windows/Linux in Dark theme. */
    public Color getWinSelectedFgDark() {
        return getColor("winSelectedFgDark");
    }
    
    /** Gets the unselected tab background color for Windows/Linux in Dark theme. */
    public Color getWinUnselectedBgDark() {
        return getColor("winUnselectedBgDark");
    }
    
    /** Gets the unselected tab foreground color for Windows/Linux in Dark theme. */
    public Color getWinUnselectedFgDark() {
        return getColor("winUnselectedFgDark");
    }
    
    /** Gets the selected tab background color for macOS in Light theme. */
    public Color getMacSelectedBgLight() {
        return getColor("macSelectedBgLight");
    }
    
    /** Gets the selected tab foreground color for macOS in Light theme. */
    public Color getMacSelectedFgLight() {
        return getColor("macSelectedFgLight");
    }
    
    /** Gets the unselected tab background color for macOS in Light theme. */
    public Color getMacUnselectedBgLight() {
        return getColor("macUnselectedBgLight");
    }
    
    /** Gets the unselected tab foreground color for macOS in Light theme. */
    public Color getMacUnselectedFgLight() {
        return getColor("macUnselectedFgLight");
    }
    
    /** Gets the selected tab background color for macOS in Dark theme. */
    public Color getMacSelectedBgDark() {
        return getColor("macSelectedBgDark");
    }
    
    /** Gets the selected tab foreground color for macOS in Dark theme. */
    public Color getMacSelectedFgDark() {
        return getColor("macSelectedFgDark");
    }
    
    /** Gets the unselected tab background color for macOS in Dark theme. */
    public Color getMacUnselectedBgDark() {
        return getColor("macUnselectedBgDark");
    }
    
    /** Gets the unselected tab foreground color for macOS in Dark theme. */
    public Color getMacUnselectedFgDark() {
        return getColor("macUnselectedFgDark");
    }
    
    /** Gets the tab pane background color for Windows/Linux in Light theme. */
    public Color getWinTabPaneBgLight() {
        return getColor("winTabPaneBgLight");
    }
    
    /** Gets the tab pane background color for Windows/Linux in Dark theme. */
    public Color getWinTabPaneBgDark() {
        return getColor("winTabPaneBgDark");
    }
    
    /** Gets the tab pane background color for macOS in Light theme. */
    public Color getMacTabPaneBgLight() {
        return getColor("macTabPaneBgLight");
    }
    
    /** Gets the tab pane background color for macOS in Dark theme. */
    public Color getMacTabPaneBgDark() {
        return getColor("macTabPaneBgDark");
    }
}