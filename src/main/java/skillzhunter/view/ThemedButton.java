package skillzhunter.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 * A custom button class that automatically applies theme styling and hover effects.
 * This centralizes button styling logic across the application.
 */
public class ThemedButton extends JButton {
    private ColorTheme theme;
    private boolean isHovering = false;
    
    /**
     * Creates a new themed button with the given text.
     * 
     * @param text The button text
     */
    public ThemedButton(String text) {
        super(text);
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
     * Updates button colors based on current theme and hover state.
     */
    private void updateButtonColors() {
        if (theme == null) return;
        
        if (isHovering) {
            setBackground(theme.buttonHover);
        } else {
            setBackground(theme.buttonNormal);
        }
        
        setForeground(theme.buttonForeground);
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