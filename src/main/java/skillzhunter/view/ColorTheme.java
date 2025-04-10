package skillzhunter.view;

import java.awt.*;

/**
 * Manages color themes for the application with predefined schemes.
 */
public class ColorTheme {
    /** The button normal color */
    public final Color buttonNormal;
    /** The button hover color */
    public final Color buttonHover;
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
    // Platform-specific tab colors replace these generic ones
    /** The label color */
    public final Color labelForeground;

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
    private ColorTheme(Color buttonNormal, Color buttonHover, Color background,
                       Color foreground, Color fieldBackground, Color fieldForeground,
                       Color buttonForeground, Color labelForeground,
                       Color winSelectedBgLight, Color winSelectedFgLight,
                       Color winUnselectedBgLight, Color winUnselectedFgLight,
                       Color winSelectedBgDark, Color winSelectedFgDark,
                       Color winUnselectedBgDark, Color winUnselectedFgDark,
                       Color macSelectedBgLight, Color macSelectedFgLight,
                       Color macUnselectedBgLight, Color macUnselectedFgLight,
                       Color macSelectedBgDark, Color macSelectedFgDark,
                       Color macUnselectedBgDark, Color macUnselectedFgDark,
                       Color winTabPaneBgLight, Color winTabPaneBgDark,
                       Color macTabPaneBgLight, Color macTabPaneBgDark) {
        this.buttonNormal = buttonNormal;
        this.buttonHover = buttonHover;
        this.background = background;
        this.foreground = foreground;
        this.fieldBackground = fieldBackground;
        this.fieldForeground = fieldForeground;
        this.buttonForeground = buttonForeground;
        this.labelForeground = labelForeground;
        
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
        new Color(0, 120, 215),     // Button normal: Windows blue
        new Color(0, 102, 184),     // Button hover: Darker blue
        new Color(245, 245, 245),   // Background: Light gray
        Color.BLACK,                // Foreground: Black text
        Color.WHITE,                // Field background: White
        Color.BLACK,                // Field foreground: Black text
        Color.WHITE,                // Button foreground: White text
        Color.BLACK,                // Label foreground: Black
        
        // Windows/Linux tab colors - Light theme
        new Color(202, 220, 245),   // WIN_SELECTED_BG_LIGHT
        Color.BLACK,                // WIN_SELECTED_FG_LIGHT
        new Color(235, 235, 235),   // WIN_UNSELECTED_BG_LIGHT
        Color.BLACK,                // WIN_UNSELECTED_FG_LIGHT
        
        // Windows/Linux tab colors - Dark theme
        new Color(197, 218, 240),   // WIN_SELECTED_BG_DARK
        Color.BLACK,                // WIN_SELECTED_FG_DARK
        new Color(43, 43, 43),      // WIN_UNSELECTED_BG_DARK
        Color.WHITE,                // WIN_UNSELECTED_FG_DARK
        
        // macOS tab colors - Light theme
        new Color(255, 255, 255),   // MAC_SELECTED_BG_LIGHT
        new Color(0, 120, 215),     // MAC_SELECTED_FG_LIGHT
        new Color(235, 235, 235),   // MAC_UNSELECTED_BG_LIGHT
        Color.DARK_GRAY,            // MAC_UNSELECTED_FG_LIGHT
        
        // macOS tab colors - Dark theme
        new Color(201, 201, 201),   // MAC_SELECTED_BG_DARK
        Color.BLACK,                // MAC_SELECTED_FG_DARK
        new Color(33, 33, 33),      // MAC_UNSELECTED_BG_DARK
        new Color(180, 180, 180),   // MAC_UNSELECTED_FG_DARK
        
        // Tab pane background colors
        new Color(240, 240, 240),   // WIN_TABPANE_BG_LIGHT
        new Color(43, 43, 43),      // WIN_TABPANE_BG_DARK
        new Color(245, 245, 245),   // MAC_TABPANE_BG_LIGHT
        new Color(33, 33, 33)       // MAC_TABPANE_BG_DARK
    );

    /** The dark color theme for the application. */
    public static final ColorTheme DARK = new ColorTheme(
        new Color(0, 183, 195),     // Button normal: Teal blue
        new Color(0, 158, 170),     // Button hover: Darker teal
        new Color(43, 43, 43),      // Background: Dark gray
        new Color(240, 240, 240),   // Foreground: Light gray text
        new Color(60, 63, 65),      // Field background: Darker gray
        new Color(240, 240, 240),   // Field foreground: Light gray text
        Color.WHITE,                // Button foreground: White text
        Color.WHITE,                // Label foreground: White
        
        // Windows/Linux tab colors - Light theme
        new Color(202, 220, 245),   // WIN_SELECTED_BG_LIGHT
        Color.BLACK,                // WIN_SELECTED_FG_LIGHT
        new Color(235, 235, 235),   // WIN_UNSELECTED_BG_LIGHT
        Color.BLACK,                // WIN_UNSELECTED_FG_LIGHT
        
        // Windows/Linux tab colors - Dark theme
        new Color(197, 218, 240),   // WIN_SELECTED_BG_DARK
        Color.BLACK,                // WIN_SELECTED_FG_DARK
        new Color(43, 43, 43),      // WIN_UNSELECTED_BG_DARK
        Color.WHITE,                // WIN_UNSELECTED_FG_DARK
        
        // macOS tab colors - Light theme
        new Color(255, 255, 255),   // MAC_SELECTED_BG_LIGHT
        new Color(0, 120, 215),     // MAC_SELECTED_FG_LIGHT
        new Color(235, 235, 235),   // MAC_UNSELECTED_BG_LIGHT
        Color.DARK_GRAY,            // MAC_UNSELECTED_FG_LIGHT
        
        // macOS tab colors - Dark theme
        new Color(201, 201, 201),   // MAC_SELECTED_BG_DARK
        Color.BLACK,                // MAC_SELECTED_FG_DARK
        new Color(33, 33, 33),      // MAC_UNSELECTED_BG_DARK
        new Color(180, 180, 180),   // MAC_UNSELECTED_FG_DARK
        
        // Tab pane background colors
        new Color(240, 240, 240),   // WIN_TABPANE_BG_LIGHT
        new Color(43, 43, 43),      // WIN_TABPANE_BG_DARK
        new Color(245, 245, 245),   // MAC_TABPANE_BG_LIGHT
        new Color(33, 33, 33)       // MAC_TABPANE_BG_DARK
    );
}