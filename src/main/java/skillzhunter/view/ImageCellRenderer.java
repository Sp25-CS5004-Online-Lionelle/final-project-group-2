package skillzhunter.view;

import java.awt.Component;
import java.awt.Image;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A cell renderer for displaying company logos from URLs.
 */
public class ImageCellRenderer extends DefaultTableCellRenderer {
    /** Image width. */
    private static final int IMAGE_WIDTH = 32;
    /** Image height. */
    private static final int IMAGE_HEIGHT = 32;
    /** Cache for loaded images. */
    private final Map<String, ImageIcon> imageCache = new HashMap<>();
    /** Loading status for images. */
    private final Map<String, Boolean> loadingStatus = new HashMap<>();
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, "", isSelected, hasFocus, row, column);
        
        // Center the image
        label.setHorizontalAlignment(JLabel.CENTER);
        
        if (value instanceof String imageUrl) {   
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // Check if we have this image in cache
                if (imageCache.containsKey(imageUrl)) {
                    ImageIcon icon = imageCache.get(imageUrl);
                    label.setIcon(icon);
                    label.setText(icon == null ? "X" : "");
                } else if (!loadingStatus.containsKey(imageUrl)) {
                    // Start loading the image
                    loadingStatus.put(imageUrl, true);
                    label.setText("...");
                    
                    loadImageAsync(imageUrl, label, table);
                } else {
                    // Image is loading
                    label.setText("...");
                }
                
                // Set tooltip
                label.setToolTipText(imageUrl);
            } else {
                // No URL
                label.setIcon(null);
                label.setText("");
                label.setToolTipText(null);
            }
        } else {
            // Not a string value
            label.setIcon(null);
            label.setText("");
            label.setToolTipText(null);
        }
        
        return label;
    }
    
    /**
     * Load an image asynchronously to avoid blocking the UI.
     * @param imageUrl The URL of the image to load
     * @param label The label to update with the image
     * @param table The table to repaint after loading
     */
    private void loadImageAsync(String imageUrl, JLabel label, JTable table) {
        new Thread(() -> {
            try {
                // Create URL
                URL url = new URL(imageUrl);
                
                // Open connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                
                // Set browser-like headers
                connection.setRequestProperty("User-Agent", 
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
                    + " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");
                
                // Connect and check response
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new Exception("HTTP error: " + responseCode);
                }
                
                // Read the image
                try (InputStream in = connection.getInputStream()) {
                    Image image = ImageIO.read(in);
                    
                    if (image != null) {
                        // Resize the image
                        Image resized = image.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                        ImageIcon icon = new ImageIcon(resized);
                        
                        // Store in cache
                        imageCache.put(imageUrl, icon);
                        
                        // Update the table on the EDT
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            table.repaint();
                        });
                    } else {
                        throw new Exception("Failed to decode image");
                    }
                }
            } catch (Exception e) {
                System.err.println("Error loading image from " + imageUrl + ": " + e.getMessage());
                
                // Store null in cache to indicate failed loading
                imageCache.put(imageUrl, null);
                
                // Update the table on the EDT
                javax.swing.SwingUtilities.invokeLater(() -> {
                    table.repaint();
                });
            } finally {
                // Mark as no longer loading
                loadingStatus.remove(imageUrl);
            }
        }).start();
    }
    
    /**
     * Clear the image cache when no longer needed.
     */
    public void clearCache() {
        imageCache.clear();
        loadingStatus.clear();
    }
}
