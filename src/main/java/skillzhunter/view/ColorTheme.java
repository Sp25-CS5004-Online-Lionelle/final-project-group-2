package skillzhunter.view;
import java.awt.Color;
public class ColorTheme {
    /** The background color for the application. */
    private final Color backgroundColor;
    /** The foreground color for the application. */
    private final Color foregroundColor;
    /** The button background color for the application. */
    private final Color buttonBackgroundColor;
    /** The button foreground color for the application. */
    private final Color buttonForegroundColor;
    /** The table background color for the application. */
    private final Color tableBackgroundColor;
    /** The table foreground color for the application. */
    private final Color tableForegroundColor;
    /** The table header background color for the application. */
    private final Color tableHeaderBackgroundColor;
    /** The table header foreground color for the application. */
    private final Color tableHeaderForegroundColor;
    /** The border color for the application. */
    private final Color borderColor;
    /**
     * Constructor for ColorTheme.
     * This constructor sets the colors based on the isDarkMode parameter.
     * If isDarkMode is true, it sets the colors for dark mode.
     * If isDarkMode is false, it sets the colors for light mode.
     * @param isDarkMode
     */
    public ColorTheme(boolean isDarkMode) {
        if (isDarkMode) {
            backgroundColor = new Color(18, 18, 18); // Deep black
            foregroundColor = new Color(0, 255, 255); // Vibrant cyan
            buttonBackgroundColor = new Color(30, 30, 30); // Dark gray
            buttonForegroundColor = new Color(255, 105, 180); // Hot pink
            tableBackgroundColor = new Color(40, 40, 40);
            tableForegroundColor = new Color(200, 200, 200);
            tableHeaderBackgroundColor = new Color(0, 50, 100);
            tableHeaderForegroundColor = Color.WHITE;
            borderColor = new Color(100, 100, 100);
        } else {
            backgroundColor = new Color(240,245,255); // Light blue
            foregroundColor = new Color(255, 20, 147); // Playful pink
            buttonBackgroundColor = new Color(220, 220, 220);
            buttonForegroundColor = new Color(50, 50, 50);
            tableBackgroundColor = new Color(245, 245, 245);
            tableForegroundColor = new Color(60, 60, 60);
            tableHeaderBackgroundColor = new Color(200, 200, 200);
            tableHeaderForegroundColor = Color.BLACK;
            borderColor = new Color(150, 150, 150); // Light gray
        }
    }

    /**
     * Getter for background color.
     * @return backgroundColor
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Getter for foreground color.
     * @return foregroundColor
     */
    public Color getForegroundColor() {
        return foregroundColor;
    }

    /**
     * Getter for button background color.
     * @return buttonBackgroundColor
     */
    public Color getButtonBackgroundColor() {
        return buttonBackgroundColor;
    }

    /**
     * Getter for button foreground color.
     * 
     * @return buttonForegroundColor
     */
    public Color getButtonForegroundColor() {
        return buttonForegroundColor;
    }

    /**
     * Getter for table background color.
     * @return tableBackgroundColor
     */
    public Color getTableBackgroundColor() {
        return tableBackgroundColor;
    }

    /**
     * Getter for table foreground color.
     * @return tableForegroundColor
     */
    public Color getTableForegroundColor() {
        return tableForegroundColor;
    }

    /**
     * Getter for table header background color.
     * @return tableHeaderBackgroundColor
     */
    public Color getTableHeaderBackgroundColor() {
        return tableHeaderBackgroundColor;
    }

    /**
     * Getter for table header foreground color.
     * @return tableHeaderForegroundColor
     */
    public Color getTableHeaderForegroundColor() {
        return tableHeaderForegroundColor;
    }

    /**
     * Getter for border color.
     * @return borderColor
     */
    public Color getBorderColor() {
        return borderColor; // Getter for border color
    }
}
