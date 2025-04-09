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
    /** The selected tab background color */
    public final Color selectedTabBackground;
    /** The selected tab foreground color */
    public final Color selectedTabForeground;
    /** The unselected tab background color */
    public final Color unselectedTabBackground;
    /** The unselected tab foreground color */
    public final Color unselectedTabForeground;

    /**
     * Constructor for ColorTheme
     */
    private ColorTheme(Color buttonNormal, Color buttonHover, Color background,
                       Color foreground, Color fieldBackground, Color fieldForeground,
                       Color buttonForeground, Color selectedTabBackground, 
                       Color selectedTabForeground, Color unselectedTabBackground, 
                       Color unselectedTabForeground) {
        this.buttonNormal = buttonNormal;
        this.buttonHover = buttonHover;
        this.background = background;
        this.foreground = foreground;
        this.fieldBackground = fieldBackground;
        this.fieldForeground = fieldForeground;
        this.buttonForeground = buttonForeground;
        this.selectedTabBackground = selectedTabBackground;
        this.selectedTabForeground = selectedTabForeground;
        this.unselectedTabBackground = unselectedTabBackground;
        this.unselectedTabForeground = unselectedTabForeground;
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
        Color.WHITE,                // Selected tab background: White
        Color.BLACK,                // Selected tab foreground: Black
        new Color(235, 235, 235),   // Unselected tab background: Slightly darker gray
        Color.BLACK                 // Unselected tab foreground: Black
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
        new Color(203, 203, 203),      // Selected tab background: Gray to match your screenshot
        new Color(0, 183, 195),     // Selected tab foreground: Teal (same as button normal)
        new Color(43, 43, 43),      // Unselected tab background: Same as main bg
        new Color(240, 240, 240)    // Unselected tab foreground: Light gray text
    );
}