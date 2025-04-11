package skillzhunter.view;

import java.awt.*;

/**
 * Manages color themes for the application with predefined schemes.
 * - Light mode uses Bootstrap 5 colors with Spotify green for success buttons
 * - Dark mode uses a teal-based color scheme with yellow success buttons
 */
public class ColorTheme {
    /** The primary button normal color */
    public final Color buttonNormal;
    /** The primary button hover color */
    public final Color buttonHover;
    /** The secondary button normal color */
    public final Color secondaryButtonNormal;
    /** The secondary button hover color */
    public final Color secondaryButtonHover;
    /** The success button normal color */
    public final Color successButtonNormal;
    /** The success button hover color */
    public final Color successButtonHover;
    /** The danger button normal color */
    public final Color dangerButtonNormal;
    /** The danger button hover color */
    public final Color dangerButtonHover;
    /** The warning button normal color */
    public final Color warningButtonNormal;
    /** The warning button hover color */
    public final Color warningButtonHover;
    /** The info button normal color */
    public final Color infoButtonNormal;
    /** The info button hover color */
    public final Color infoButtonHover;
    
    /** The background color */
    public final Color background;
    /** The foreground color */
    public final Color foreground;
    /** The field background color */
    public final Color fieldBackground;
    /** The field foreground color */
    public final Color fieldForeground;
    /** The button foreground color */
    public final Color buttonForeground;
    /** The label color */
    public final Color labelForeground;
    
    // Menu bar colors
    /** The menu bar background for light theme */
    public final Color menuBarBackgroundLight;
    /** The menu bar foreground for light theme */
    public final Color menuBarForegroundLight;
    /** The menu bar background for dark theme */
    public final Color menuBarBackgroundDark;
    /** The menu bar foreground for dark theme */
    public final Color menuBarForegroundDark;
    
    // Platform-specific tab colors for Windows/Linux
    /** Selected tab background color for Windows/Linux in Light theme */
    public final Color winSelectedBgLight;
    /** Selected tab foreground color for Windows/Linux in Light theme */
    public final Color winSelectedFgLight;
    /** Unselected tab background color for Windows/Linux in Light theme */
    public final Color winUnselectedBgLight;
    /** Unselected tab foreground color for Windows/Linux in Light theme */
    public final Color winUnselectedFgLight;
    
    /** Selected tab background color for Windows/Linux in Dark theme */
    public final Color winSelectedBgDark;
    /** Selected tab foreground color for Windows/Linux in Dark theme */
    public final Color winSelectedFgDark;
    /** Unselected tab background color for Windows/Linux in Dark theme */
    public final Color winUnselectedBgDark;
    /** Unselected tab foreground color for Windows/Linux in Dark theme */
    public final Color winUnselectedFgDark;
    
    // Platform-specific tab colors for macOS
    /** Selected tab background color for macOS in Light theme */
    public final Color macSelectedBgLight;
    /** Selected tab foreground color for macOS in Light theme */
    public final Color macSelectedFgLight;
    /** Unselected tab background color for macOS in Light theme */
    public final Color macUnselectedBgLight;
    /** Unselected tab foreground color for macOS in Light theme */
    public final Color macUnselectedFgLight;
    
    /** Selected tab background color for macOS in Dark theme */
    public final Color macSelectedBgDark;
    /** Selected tab foreground color for macOS in Dark theme */
    public final Color macSelectedFgDark;
    /** Unselected tab background color for macOS in Dark theme */
    public final Color macUnselectedBgDark;
    /** Unselected tab foreground color for macOS in Dark theme */
    public final Color macUnselectedFgDark;
    
    /** Tab pane background color for Windows/Linux in Light theme */
    public final Color winTabPaneBgLight;
    /** Tab pane background color for Windows/Linux in Dark theme */
    public final Color winTabPaneBgDark;
    /** Tab pane background color for macOS in Light theme */
    public final Color macTabPaneBgLight;
    /** Tab pane background color for macOS in Dark theme */
    public final Color macTabPaneBgDark;
    
    /**
     * Constructor for ColorTheme
     */
    private ColorTheme(
            // Button colors
            Color buttonNormal, Color buttonHover,
            Color secondaryButtonNormal, Color secondaryButtonHover,
            Color successButtonNormal, Color successButtonHover,
            Color dangerButtonNormal, Color dangerButtonHover,
            Color warningButtonNormal, Color warningButtonHover,
            Color infoButtonNormal, Color infoButtonHover,
            
            // General colors
            Color background, Color foreground, 
            Color fieldBackground, Color fieldForeground,
            Color buttonForeground, Color labelForeground,
            
            // Menu bar colors
            Color menuBarBackgroundLight, Color menuBarForegroundLight,
            Color menuBarBackgroundDark, Color menuBarForegroundDark,
            
            // Tab colors for Windows/Linux
            Color winSelectedBgLight, Color winSelectedFgLight,
            Color winUnselectedBgLight, Color winUnselectedFgLight,
            Color winSelectedBgDark, Color winSelectedFgDark,
            Color winUnselectedBgDark, Color winUnselectedFgDark,
            
            // Tab colors for macOS
            Color macSelectedBgLight, Color macSelectedFgLight,
            Color macUnselectedBgLight, Color macUnselectedFgLight,
            Color macSelectedBgDark, Color macSelectedFgDark,
            Color macUnselectedBgDark, Color macUnselectedFgDark,
            
            // Tab pane background colors
            Color winTabPaneBgLight, Color winTabPaneBgDark,
            Color macTabPaneBgLight, Color macTabPaneBgDark) {
            
        // Button colors
        this.buttonNormal = buttonNormal;
        this.buttonHover = buttonHover;
        this.secondaryButtonNormal = secondaryButtonNormal;
        this.secondaryButtonHover = secondaryButtonHover;
        this.successButtonNormal = successButtonNormal;
        this.successButtonHover = successButtonHover;
        this.dangerButtonNormal = dangerButtonNormal;
        this.dangerButtonHover = dangerButtonHover;
        this.warningButtonNormal = warningButtonNormal;
        this.warningButtonHover = warningButtonHover;
        this.infoButtonNormal = infoButtonNormal;
        this.infoButtonHover = infoButtonHover;
        
        // General colors
        this.background = background;
        this.foreground = foreground;
        this.fieldBackground = fieldBackground;
        this.fieldForeground = fieldForeground;
        this.buttonForeground = buttonForeground;
        this.labelForeground = labelForeground;
        
        // Menu bar colors
        this.menuBarBackgroundLight = menuBarBackgroundLight;
        this.menuBarForegroundLight = menuBarForegroundLight;
        this.menuBarBackgroundDark = menuBarBackgroundDark;
        this.menuBarForegroundDark = menuBarForegroundDark;
        
        // Platform-specific tab colors
        this.winSelectedBgLight = winSelectedBgLight;
        this.winSelectedFgLight = winSelectedFgLight;
        this.winUnselectedBgLight = winUnselectedBgLight;
        this.winUnselectedFgLight = winUnselectedFgLight;
        this.winSelectedBgDark = winSelectedBgDark;
        this.winSelectedFgDark = winSelectedFgDark;
        this.winUnselectedBgDark = winUnselectedBgDark;
        this.winUnselectedFgDark = winUnselectedFgDark;
        this.macSelectedBgLight = macSelectedBgLight;
        this.macSelectedFgLight = macSelectedFgLight;
        this.macUnselectedBgLight = macUnselectedBgLight;
        this.macUnselectedFgLight = macUnselectedFgLight;
        this.macSelectedBgDark = macSelectedBgDark;
        this.macSelectedFgDark = macSelectedFgDark;
        this.macUnselectedBgDark = macUnselectedBgDark;
        this.macUnselectedFgDark = macUnselectedFgDark;
        
        // Tab pane background colors
        this.winTabPaneBgLight = winTabPaneBgLight;
        this.winTabPaneBgDark = winTabPaneBgDark;
        this.macTabPaneBgLight = macTabPaneBgLight;
        this.macTabPaneBgDark = macTabPaneBgDark;
    }

    /** The light color theme for the application. */
    public static final ColorTheme LIGHT = new ColorTheme(
        // Button colors - Bootstrap 5
        new Color(13, 110, 253),      // Primary button normal: Bootstrap blue (#0d6efd)
        new Color(11, 94, 215),       // Primary button hover: Darker blue (#0b5ed7)
        new Color(108, 117, 125),     // Secondary button normal: Bootstrap gray (#6c757d)
        new Color(92, 99, 106),       // Secondary button hover: Darker gray (#5c636a)
        new Color(30, 215, 96),       // Success button normal: Spotify green (#1ED760)
        new Color(25, 185, 84),       // Success button hover: Darker Spotify green
        new Color(220, 53, 69),       // Danger button normal: Bootstrap red (#dc3545)
        new Color(187, 45, 59),       // Danger button hover: Darker red (#bb2d3b)
        new Color(255, 193, 7),       // Warning button normal: Bootstrap yellow (#ffc107)
        new Color(255, 202, 44),      // Warning button hover: Brighter yellow (#ffca2c)
        new Color(13, 202, 240),      // Info button normal: Bootstrap cyan (#0dcaf0)
        new Color(49, 210, 242),      // Info button hover: Brighter cyan (#31d2f2)
        
        // General colors - ORIGINAL VALUES FROM LIGHT THEME
        new Color(245, 245, 245),     // Background: Light gray (original)
        Color.BLACK,                  // Foreground: Black text
        Color.WHITE,                  // Field background: White 
        Color.BLACK,                  // Field foreground: Black
        Color.WHITE,                  // Button foreground: White text
        Color.BLACK,                  // Label foreground: Black
        
        // Menu bar colors - ORIGINAL VALUES FROM LIGHT THEME
        Color.WHITE,                // Menu bar background - Light theme
        Color.BLACK,                // Menu bar foreground - Light theme
        new Color(43, 43, 43),      // Menu bar background - Dark theme
        Color.WHITE,                // Menu bar foreground - Dark theme
        
        // Windows/Linux tab colors - ORIGINAL VALUES FROM LIGHT THEME
        new Color(202, 220, 245),   // WIN_SELECTED_BG_LIGHT
        Color.BLACK,                // WIN_SELECTED_FG_LIGHT
        new Color(235, 235, 235),   // WIN_UNSELECTED_BG_LIGHT
        Color.BLACK,                // WIN_UNSELECTED_FG_LIGHT
        
        // Windows/Linux tab colors - ORIGINAL VALUES FROM LIGHT THEME
        new Color(197, 218, 240),   // WIN_SELECTED_BG_DARK
        Color.BLACK,                // WIN_SELECTED_FG_DARK
        new Color(43, 43, 43),      // WIN_UNSELECTED_BG_DARK
        Color.WHITE,                // WIN_UNSELECTED_FG_DARK
        
        // macOS tab colors - ORIGINAL VALUES FROM LIGHT THEME
        new Color(255, 255, 255),   // MAC_SELECTED_BG_LIGHT
        new Color(0, 120, 215),     // MAC_SELECTED_FG_LIGHT - Windows blue
        new Color(235, 235, 235),   // MAC_UNSELECTED_BG_LIGHT
        Color.DARK_GRAY,            // MAC_UNSELECTED_FG_LIGHT
        
        // macOS tab colors - ORIGINAL VALUES FROM LIGHT THEME
        new Color(201, 201, 201),   // MAC_SELECTED_BG_DARK
        Color.BLACK,                // MAC_SELECTED_FG_DARK
        new Color(33, 33, 33),      // MAC_UNSELECTED_BG_DARK
        new Color(180, 180, 180),   // MAC_UNSELECTED_FG_DARK
        
        // Tab pane background colors - ORIGINAL VALUES FROM LIGHT THEME
        new Color(240, 240, 240),   // WIN_TABPANE_BG_LIGHT
        new Color(43, 43, 43),      // WIN_TABPANE_BG_DARK
        new Color(245, 245, 245),   // MAC_TABPANE_BG_LIGHT
        new Color(33, 33, 33)       // MAC_TABPANE_BG_DARK
    );

    /** The dark color theme for the application. */
    public static final ColorTheme DARK = new ColorTheme(
        // Button colors - Teal-based with complementary colors (Option 2)
        new Color(0, 189, 195),       // Primary button normal: Teal (#00BDC3)
        new Color(0, 165, 171),       // Primary button hover: Darker teal (#00A5AB)
        new Color(126, 87, 194),      // Secondary button normal: Purple (#7E57C2)
        new Color(106, 70, 181),      // Secondary button hover: Darker purple (#6A46B5)
        new Color(255, 222, 0),       // Success button normal: Yellow (#FFDE00)
        new Color(230, 200, 0),       // Success button hover: Darker yellow
        new Color(239, 83, 80),       // Danger button normal: Red (#EF5350)
        new Color(229, 57, 53),       // Danger button hover: Darker red (#E53935)
        new Color(255, 202, 40),      // Warning button normal: Yellow (#FFCA28)
        new Color(255, 179, 0),       // Warning button hover: Darker yellow (#FFB300)
        new Color(41, 182, 246),      // Info button normal: Blue (#29B6F6)
        new Color(3, 155, 229),       // Info button hover: Darker blue (#039BE5)
        
        // General colors - ORIGINAL VALUES FROM DARK THEME
        new Color(43, 43, 43),      // Background: Dark gray
        new Color(240, 240, 240),   // Foreground: Light gray text
        new Color(60, 63, 65),      // Field background: Darker gray
        new Color(240, 240, 240),   // Field foreground: Light gray text
        Color.WHITE,                // Button foreground: White text
        Color.WHITE,                // Label foreground: White
        
        // Menu bar colors - ORIGINAL VALUES FROM DARK THEME
        Color.WHITE,                // Menu bar background - Light theme
        Color.BLACK,                // Menu bar foreground - Light theme
        new Color(43, 43, 43),      // Menu bar background - Dark theme
        Color.WHITE,                // Menu bar foreground - Dark theme
        
        // Windows/Linux tab colors - ORIGINAL VALUES FROM DARK THEME
        new Color(202, 220, 245),   // WIN_SELECTED_BG_LIGHT
        Color.BLACK,                // WIN_SELECTED_FG_LIGHT
        new Color(235, 235, 235),   // WIN_UNSELECTED_BG_LIGHT
        Color.BLACK,                // WIN_UNSELECTED_FG_LIGHT
        
        // Windows/Linux tab colors - ORIGINAL VALUES FROM DARK THEME
        new Color(197, 218, 240),   // WIN_SELECTED_BG_DARK
        Color.BLACK,                // WIN_SELECTED_FG_DARK
        new Color(43, 43, 43),      // WIN_UNSELECTED_BG_DARK
        Color.WHITE,                // WIN_UNSELECTED_FG_DARK
        
        // macOS tab colors - ORIGINAL VALUES FROM DARK THEME
        new Color(255, 255, 255),   // MAC_SELECTED_BG_LIGHT
        new Color(0, 120, 215),     // MAC_SELECTED_FG_LIGHT - Windows blue
        new Color(235, 235, 235),   // MAC_UNSELECTED_BG_LIGHT
        Color.DARK_GRAY,            // MAC_UNSELECTED_FG_LIGHT
        
        // macOS tab colors - ORIGINAL VALUES FROM DARK THEME
        new Color(201, 201, 201),   // MAC_SELECTED_BG_DARK
        Color.BLACK,                // MAC_SELECTED_FG_DARK
        new Color(33, 33, 33),      // MAC_UNSELECTED_BG_DARK
        new Color(180, 180, 180),   // MAC_UNSELECTED_FG_DARK
        
        // Tab pane background colors - ORIGINAL VALUES FROM DARK THEME
        new Color(240, 240, 240),   // WIN_TABPANE_BG_LIGHT
        new Color(43, 43, 43),      // WIN_TABPANE_BG_DARK
        new Color(245, 245, 245),   // MAC_TABPANE_BG_LIGHT
        new Color(33, 33, 33)       // MAC_TABPANE_BG_DARK
    );
}