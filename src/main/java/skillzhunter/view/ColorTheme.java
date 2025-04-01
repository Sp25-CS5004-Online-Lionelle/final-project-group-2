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
        new Color(200, 200, 200),
        new Color(180, 180, 255),
        new Color(240, 240, 240),
        Color.BLACK,
        Color.WHITE,
        Color.BLACK,
        Color.BLACK
    );

    /** The dark color theme for the application. */
    public static final ColorTheme DARK = new ColorTheme(
        new Color(58, 141, 255),
        new Color(0, 95, 204),
        new Color(43, 43, 43),
        new Color(224, 224, 224),
        new Color(60, 63, 65),
        new Color(224, 224, 224),
        Color.WHITE
    );
}
