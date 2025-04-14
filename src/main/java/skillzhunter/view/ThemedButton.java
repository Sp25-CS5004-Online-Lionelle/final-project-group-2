package skillzhunter.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

/**
 * A custom button class that automatically applies theme styling and hover effects.
 * This centralizes button styling logic across the application.
 * Supports multiple button types based on Bootstrap styling (primary, secondary, success, etc.)
 * The ThemedButton maintains consistent styling based on the application's color theme
 * and automatically handles visual feedback for user interactions like hovering.
 * This helps ensure a uniform look and feel throughout the application.
 */
public class ThemedButton extends JButton {
    /** The color theme currently applied to this button. */
    private ColorTheme theme;
    
    /** Tracks whether the mouse is currently hovering over this button. */
    private boolean isHovering = false;
    
    /** The button's style type, determining its color scheme. */
    private ButtonType buttonType = ButtonType.PRIMARY; // Default to primary
    
    /**
     * Button types based on Bootstrap styling conventions.
     * Each type corresponds to a different visual style and semantic meaning
     */
    public enum ButtonType {
        /** PRIMARY - Main actions, the most important action on a page. */
        PRIMARY,
        /** SECONDARY - Alternative or less important actions. */
        SECONDARY,
        /** SUCCESS - Positive actions like saving or confirming. */
        SUCCESS,
        /** DANGER - Destructive or potentially harmful actions. */
        DANGER,
        /** WARNING - Actions that require caution. */
        WARNING,
        /** INFO - Informational actions. */
        INFO
    }
    
    /**
     * Creates a new themed button with the given text.
     * Uses PRIMARY button type by default.
     * 
     * @param text The button text to display
     */
    public ThemedButton(String text) {
        this(text, ButtonType.PRIMARY);
    }
    
    /**
     * Creates a new themed button with the given text and button type.
     * 
     * @param text The button text to display
     * @param buttonType The button type that determines the visual style
     */
    public ThemedButton(String text, ButtonType buttonType) {
        super(text);
        this.buttonType = buttonType;
        initializeButton();
    }
    
    /**
     * Initializes button appearance and behavior.
     * Sets default appearance properties, cursor type, and adds event listeners
     * for hover effects. If no theme has been explicitly set, applies the default
     * light theme.
     */
    private void initializeButton() {
        // Set default appearance
        setFocusPainted(false);
        setOpaque(true);
        setBorderPainted(false);
        
        // Set hand cursor
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        addMouseListener(new MouseAdapter() {
            /**
             * Updates button state when mouse enters the button area.
             * 
             * @param e the MouseEvent containing event information
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovering = true;
                updateButtonColors();
            }
            
            /**
             * Updates button state when mouse exits the button area.
             * 
             * @param e the MouseEvent containing event information
             */
            @Override
            public void mouseExited(MouseEvent e) {
                isHovering = false;
                updateButtonColors();
            }
        });
        
        // Apply default light theme if no theme set
        if (theme == null) {
            applyTheme(ColorTheme.LIGHT);
        }
    }
    
    /**
     * Applies the specified color theme to the button.
     * Updates the button's appearance to match the new theme.
     * 
     * @param theme The ColorTheme to apply to this button
     */
    public void applyTheme(ColorTheme theme) {
        this.theme = theme;
        updateButtonColors();
    }
    
    /**
     * Sets the button type (PRIMARY, SECONDARY, etc.) and updates the appearance.
     * 
     * @param buttonType The new button type to apply
     */
    public void setButtonType(ButtonType buttonType) {
        this.buttonType = buttonType;
        updateButtonColors();
    }
    
    /**
     * Gets the current button type.
     * 
     * @return The current ButtonType used by this button
     */
    public ButtonType getButtonType() {
        return buttonType;
    }
    
    /**
     * Updates button colors based on current theme, button type, and hover state.
     * This method handles the visual presentation logic for different combinations
     * of button types, themes, and interaction states.
     * <p>
     * Different button types use different color pairs from the theme, and the
     * hover state determines which variant of the color is used. Text color is
     * also adjusted based on the background color to ensure readability.
     */
    private void updateButtonColors() {
        if (theme == null) {
            return;
        }
        Color normalColor;
        Color hoverColor;
        
        // Select the appropriate colors based on button type
        switch (buttonType) {
            case PRIMARY:
                normalColor = theme.buttonNormal;
                hoverColor = theme.buttonHover;
                break;
            case SECONDARY:
                normalColor = theme.secondaryButtonNormal;
                hoverColor = theme.secondaryButtonHover;
                break;
            case SUCCESS:
                normalColor = theme.successButtonNormal;
                hoverColor = theme.successButtonHover;
                break;
            case DANGER:
                normalColor = theme.dangerButtonNormal;
                hoverColor = theme.dangerButtonHover;
                break;
            case WARNING:
                normalColor = theme.warningButtonNormal;
                hoverColor = theme.warningButtonHover;
                break;
            case INFO:
                normalColor = theme.infoButtonNormal;
                hoverColor = theme.infoButtonHover;
                break;
            default:
                normalColor = theme.buttonNormal;
                hoverColor = theme.buttonHover;
        }
        
        // Apply the appropriate color based on hover state
        setBackground(isHovering ? hoverColor : normalColor);
        
        // Set appropriate text color - check if we need white or black text
        // For warning buttons which are typically yellow, use black text
        if (buttonType == ButtonType.WARNING) {
            setForeground(Color.BLACK);
        } else if (buttonType == ButtonType.INFO && theme == ColorTheme.LIGHT) {
            // For info buttons in light mode (typically cyan), use black text
            setForeground(Color.BLACK);
        } else {
            // For all other buttons, use white text
            setForeground(theme.buttonForeground);
        }
    }
    
    /**
     * Gets the current theme applied to this button.
     * 
     * @return The current ColorTheme
     */
    public ColorTheme getTheme() {
        return theme;
    }
}
