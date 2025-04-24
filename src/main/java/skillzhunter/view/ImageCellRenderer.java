package skillzhunter.view;

import java.awt.Component;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A cell renderer for displaying company logos from URLs.
 * Uses a static shared cache to ensure each image is loaded only once.
 */
public class ImageCellRenderer extends DefaultTableCellRenderer {
    /** Image width. */
    private static final int IMAGE_WIDTH = 32;
    /** Image height. */
    private static final int IMAGE_HEIGHT = 32;
    /** Static cache for loaded images, shared across all renderer instances. */
    private static final Map<String, ImageIcon> SHARED_IMAGE_CACHE = new ConcurrentHashMap<>();
    /** Static loading status tracker, shared across all renderer instances. */
    private static final Map<String, Boolean> SHARED_LOADING_STATUS = new ConcurrentHashMap<>();
    /** Map to track start time for each image loading operation. */
    private static final Map<String, Long> LOAD_START_TIMES = new ConcurrentHashMap<>();
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, "", isSelected, hasFocus, row, column);
        
        // Center the image
        label.setHorizontalAlignment(JLabel.CENTER);
        
        if (value instanceof String imageUrl) {   
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // Check if we have this image in shared cache
                if (SHARED_IMAGE_CACHE.containsKey(imageUrl)) {
                    ImageIcon icon = SHARED_IMAGE_CACHE.get(imageUrl);
                    label.setIcon(icon);
                    label.setText(icon == null ? "X" : "");
                } else if (!SHARED_LOADING_STATUS.containsKey(imageUrl)) {
                    // Start loading the image - only if not already being loaded
                    synchronized (SHARED_LOADING_STATUS) {
                        // Double-check to prevent race conditions
                        if (!SHARED_LOADING_STATUS.containsKey(imageUrl)) {
                            SHARED_LOADING_STATUS.put(imageUrl, true);
                            LOAD_START_TIMES.put(imageUrl, System.currentTimeMillis());
                            
                            // Start loading asynchronously
                            loadImageAsync(imageUrl, table);
                        }
                    }
                    label.setText("...");
                } else {
                    // Image is loading by another thread
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
     * Uses a static cache to ensure each URL is loaded only once.
     * 
     * @param imageUrl The URL of the image to load
     * @param table The table to repaint after loading
     */
    private void loadImageAsync(String imageUrl, JTable table) {
        new Thread(() -> {
            try {
                long startTime = LOAD_START_TIMES.get(imageUrl);
                
                // Skip if already loaded by another thread while we were starting
                if (SHARED_IMAGE_CACHE.containsKey(imageUrl)) {
                    return;
                }
                
                // Create URL
                URL url = new URL(imageUrl);
                
                // Open connection
                java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                
                // Set browser-like headers
                connection.setRequestProperty("User-Agent", 
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");
                
                // Connect and check response
                int responseCode = connection.getResponseCode();
                
                ImageIcon icon = null;
                if (responseCode == java.net.HttpURLConnection.HTTP_OK) {
                    // Read the image
                    try (java.io.InputStream in = connection.getInputStream()) {
                        Image image = ImageIO.read(in);
                        
                        if (image != null) {
                            // Resize the image
                            Image resized = image.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(resized);
                        }
                    }
                }
                
                // Store in cache (even if null, to prevent retrying failed URLs)
                SHARED_IMAGE_CACHE.put(imageUrl, icon);
                
                // Calculate and log load time
                long endTime = System.currentTimeMillis();
                long loadTime = endTime - startTime;
                System.out.printf("Image loaded from %s in %.2f seconds (cache size: %d)%n", 
                                 imageUrl, loadTime / 1000.0, SHARED_IMAGE_CACHE.size());
                
                // Update the table on the EDT
                javax.swing.SwingUtilities.invokeLater(() -> {
                    table.repaint();
                });
            } catch (Exception e) {
                // Store null in cache to indicate failed loading
                SHARED_IMAGE_CACHE.put(imageUrl, null);
                
                // Log error and load time
                long endTime = System.currentTimeMillis();
                long loadTime = endTime - LOAD_START_TIMES.get(imageUrl);
                System.out.printf("Failed to load image from %s after %.2f seconds: %s%n", 
                                 imageUrl, loadTime / 1000.0, e.getMessage());
                
                // Update the table on the EDT
                javax.swing.SwingUtilities.invokeLater(() -> {
                    table.repaint();
                });
            } finally {
                // Mark as no longer loading
                SHARED_LOADING_STATUS.remove(imageUrl);
                LOAD_START_TIMES.remove(imageUrl);
            }
        }).start();
    }
    
    /**
     * Clear the image cache when no longer needed.
     * This affects all instances due to the static cache.
     */
    public void clearCache() {
        synchronized (SHARED_IMAGE_CACHE) {
            SHARED_IMAGE_CACHE.clear();
            SHARED_LOADING_STATUS.clear();
            LOAD_START_TIMES.clear();
            System.out.println("Image cache cleared");
        }
    }
}
