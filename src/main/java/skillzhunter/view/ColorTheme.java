package skillzhunter.view;
import java.awt.Color;
public class ColorTheme {
    private final Color backgroundColor;
    private final Color foregroundColor;
    private final Color buttonBackgroundColor;
    private final Color buttonForegroundColor;
    private final Color tableBackgroundColor;
    private final Color tableForegroundColor;
    private final Color tableHeaderBackgroundColor;
    private final Color tableHeaderForegroundColor;
    private final Color borderColor;

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

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getButtonBackgroundColor() {
        return buttonBackgroundColor;
    }

    public Color getButtonForegroundColor() {
        return buttonForegroundColor;
    }
    
    public Color getTableBackgroundColor() {
        return tableBackgroundColor;
    }

    public Color getTableForegroundColor() {
        return tableForegroundColor;
    }

    public Color getTableHeaderBackgroundColor() {
        return tableHeaderBackgroundColor;
    }

    public Color getTableHeaderForegroundColor() {
        return tableHeaderForegroundColor;
    }

    public Color getBorderColor() {
        return borderColor; // Getter for border color
    }
}
