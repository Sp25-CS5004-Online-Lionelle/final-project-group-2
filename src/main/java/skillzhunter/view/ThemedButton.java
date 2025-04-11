package skillzhunter.view;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 * A custom button class that automatically applies theme styling and hover effects.
 * This centralizes button styling logic across the application.
 * Supports multiple button types based on Bootstrap styling (primary, secondary, success, etc.)
 */
public class ThemedButton extends JButton {
    /** Button Theme. */
    private ColorTheme theme;
    /** Hover state. */
    private boolean isHovering = false;
    private ButtonType buttonType = ButtonType.PRIMARY; // Default to primary
    
    /**
     * Button types based on Bootstrap styling
     */
    public enum ButtonType {
        PRIMARY,
        SECONDARY,
        SUCCESS,
        DANGER,
        WARNING,
        INFO
    }
    
    /**
     * Creates a new themed button with the given text.
     * Uses PRIMARY button type by default.
     * 
     * @param text The button text
     */
    public ThemedButton(String text) {
        this(text, ButtonType.PRIMARY);
    }
    
    /**
     * Creates a new themed button with the given text and button type.
     * 
     * @param text The button text
     * @param buttonType The button type (PRIMARY, SECONDARY, etc.)
     */
    public ThemedButton(String text, ButtonType buttonType) {
        super(text);
        this.buttonType = buttonType;
        initializeButton();
    }
    
    /**
     * Initializes button appearance and behavior.
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
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovering = true;
                updateButtonColors();
            }
            
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
     * 
     * @param theme The ColorTheme to apply
     */
    public void applyTheme(ColorTheme theme) {
        this.theme = theme;
        updateButtonColors();
    }
    
    /**
     * Sets the button type (PRIMARY, SECONDARY, etc.)
     * 
     * @param buttonType The new button type
     */
    public void setButtonType(ButtonType buttonType) {
        this.buttonType = buttonType;
        updateButtonColors();
    }
    
    /**
     * Gets the current button type.
     * 
     * @return The current ButtonType
     */
    public ButtonType getButtonType() {
        return buttonType;
    }
    
    /**
     * Updates button colors based on current theme, button type, and hover state.
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
     * Gets the current theme.
     * 
     * @return The current ColorTheme
     */
    public ColorTheme getTheme() {
        return theme;
    }


}
