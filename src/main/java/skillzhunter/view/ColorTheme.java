package skillzhunter.view;

import java.awt.*;

public class ColorTheme {
    /** The default color theme for the application. */
    public final Color buttonNormal;
    /** The hover color for buttons. */
    public final Color buttonHover;
    /** The background color for the application. */
    public final Color background;
    /** The foreground color for text. */
    public final Color foreground;
    /** The background color for input fields. */
    public final Color fieldBackground;
    /** The foreground color for input fields. */
    public final Color fieldForeground;
    /** The foreground color for buttons. */
    public final Color buttonForeground;

    /** The constructor for the color theme for the application. */
    private ColorTheme(Color buttonNormal, Color buttonHover, Color background,
                       Color foreground, Color fieldBackground, Color fieldForeground,
                       Color buttonForeground) {
        this.buttonNormal = buttonNormal;
        this.buttonHover = buttonHover;
        this.background = background;
        this.foreground = foreground;
        this.fieldBackground = fieldBackground;
        this.fieldForeground = fieldForeground;
        this.buttonForeground = buttonForeground;
    }

    /** The light color theme for the application. */
    public static final ColorTheme LIGHT = new ColorTheme(
    new Color(0, 0, 0),       // Black button normal color
    new Color(105, 105, 105), // Darker grey hover color for better contrast
    new Color(240, 240, 240), // Background color for light mode
    Color.BLACK,              // Foreground color for text
    Color.WHITE,              // Field background for input fields
    Color.BLACK,              // Field foreground for input fields
    Color.WHITE               // Button foreground color (text) for light mode
);

/** The dark color theme for the application. */
public static final ColorTheme DARK = new ColorTheme(
    new Color(58, 141, 255),  // Original blue button normal color
    new Color(50, 120, 230),   // Darker blue hover color for better contrast
    new Color(43, 43, 43),    // Background color for dark mode
    new Color(224, 224, 224), // Foreground color for text
    new Color(60, 63, 65),    // Field background for input fields
    new Color(224, 224, 224), // Field foreground for input fields
    Color.WHITE               // Button foreground color (text) for dark mode
);

}
