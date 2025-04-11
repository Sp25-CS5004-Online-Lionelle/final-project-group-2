package skillzhunter.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A custom JPanel that displays a star rating system.
 * Users can click on stars to set a rating from 0 to 5.
 */
public class StarRatingPanel extends JPanel {
    /** star rating. */
    private final JLabel[] stars;
    /** default rating. */
    private int rating = 0;
    /** rate change listener. */
    private RatingChangeListener listener;

    // Interface for rating change callbacks
    /**
     * Listener interface for rating changes.
     */
    public interface RatingChangeListener {
        /**
         * Called when the rating changes.
         * @param newRating The new rating value (0-5)
         */
        void onRatingChanged(int newRating);
    }

    /**
     * Creates a new star rating panel with the specified initial rating.
     * 
     * @param initialRating The initial rating to display (0-5)
     */
    public StarRatingPanel(int initialRating) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Rating"));
        
        // Normalize initial rating to be between 0 and 5
        this.rating = Math.min(5, Math.max(0, initialRating));
        
        stars = new JLabel[5];
        
        // Create the star labels
        for (int i = 0; i < 5; i++) {
            stars[i] = new JLabel(getEmptyStar());
            stars[i].setPreferredSize(new Dimension(24, 24));
            final int starIndex = i;
            
            // Add mouse listener to handle clicks
            stars[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Set rating to clicked star index + 1 (or 0 if clicking already filled star)
                    if (rating == starIndex + 1) {
                        setRating(0); // Toggle off if clicking the same star
                    } else {
                        setRating(starIndex + 1);
                    }
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    // Preview rating when hovering
                    updateStarsDisplay(starIndex + 1);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    // Restore actual rating when mouse exits
                    updateStarsDisplay(rating);
                }
            });
            
            add(stars[i]);
        }
        
        // Set the initial rating display
        updateStarsDisplay(rating);
        
        // Add a label to show the numeric rating
        JLabel ratingLabel = new JLabel("(" + rating + ")");
        add(ratingLabel);
        
        // Update the numeric label when rating changes
        setRatingChangeListener(newRating -> ratingLabel.setText("(" + newRating + ")"));
    }
    
    /**
     * Sets the rating and updates the star display.
     * 
     * @param newRating The new rating value (0-5)
     */
    public void setRating(int newRating) {
        // Normalize rating to be between 0 and 5
        this.rating = Math.min(5, Math.max(0, newRating));
        updateStarsDisplay(rating);
        
        // Notify the listener if one exists
        if (listener != null) {
            listener.onRatingChanged(rating);
        }
    }
    
    /**
     * Gets the current rating value.
     * 
     * @return The current rating (0-5)
     */
    public int getRating() {
        return rating;
    }
    
    /**
     * Sets a listener to be notified when the rating changes.
     * 
     * @param listener The listener to notify
     */
    public void setRatingChangeListener(RatingChangeListener listener) {
        this.listener = listener;
    }
    
    /**
     * Updates the display of stars based on the given rating.
     * 
     * @param displayRating The rating to display (0-5)
     */
    private void updateStarsDisplay(int displayRating) {
        for (int i = 0; i < stars.length; i++) {
            if (i < displayRating) {
                stars[i].setIcon(getFilledStar());
            } else {
                stars[i].setIcon(getEmptyStar());
            }
        }
    }
    
    /**
     * Gets the icon for an empty (unfilled) star.
     * Using IconLoader utility class.
     * 
     * @return The empty star icon
     */
    private ImageIcon getEmptyStar() {
        // Try to load the star-empty.png image using IconLoader
        ImageIcon icon = IconLoader.loadIcon("images/star-empty.png", 20, 20);
        
        // If loading failed, use a text fallback
        if (icon == null) {
            icon = IconLoader.createTextIcon("☆", 20);
        }
        
        return icon;
    }
    
    /**
     * Gets the icon for a filled star.
     * Using IconLoader utility class.
     * 
     * @return The filled star icon
     */
    private ImageIcon getFilledStar() {
        // Try to load the star-filled.png image using IconLoader
        ImageIcon icon = IconLoader.loadIcon("images/star-filled.png", 20, 20);
        
        // If loading failed, use a text fallback with orange color
        if (icon == null) {
            icon = IconLoader.createTextIcon("★", 20);
        }
        
        return icon;
    }
}
