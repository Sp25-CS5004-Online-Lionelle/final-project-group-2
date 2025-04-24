package skillzhunter.view;

import java.awt.Image;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * Utility class for loading icons consistently across the application.
 * Provides centralized icon loading with customizable sizing and error handling.
 * 
 * Standard icon sizes across the application:
 * - Regular button/menu icons: 24x24
 * - Company logos: 64x64
 * - Table header arrows: 12x12
 * - Star rating icons: 20x20
 * - Light/Dark mode menu icons: 12x12
 */
public final class IconLoader {
    
    /** Cache for storing loaded logo images to avoid reloading */
    private static final Map<String, ImageIcon> LOGO_CACHE = new ConcurrentHashMap<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private IconLoader() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Loads an icon from the resources folder with default size (24x24).
     * This is the standard size for most button and menu icons.
     *
     * @param path The path to the icon within the resources folder
     * @return The loaded icon, or null if it couldn't be loaded
     */
    public static ImageIcon loadIcon(String path) {
        return loadIcon(path, 24, 24);
    }
    
    /**
     * Loads an icon from the resources folder with custom dimensions.
     *
     * @param path The path to the icon within the resources folder
     * @param width The desired width of the icon
     * @param height The desired height of the icon
     * @return The loaded icon, or null if it couldn't be loaded
     */
    public static ImageIcon loadIcon(String path, int width, int height) {
        try {
            URL url = IconLoader.class.getClassLoader().getResource(path);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + path + " - " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Creates a text-based icon as a fallback if image loading fails.
     * 
     * @param text The text to display (usually a Unicode symbol)
     * @param size The font size to use
     * @return An icon with the specified text
     */
    public static ImageIcon createTextIcon(String text, int size) {
        javax.swing.JLabel label = new javax.swing.JLabel(text);
        label.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, size));
        label.setSize(size + 4, size + 4);
        
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
            label.getWidth(), label.getHeight(), java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2 = image.createGraphics();
        label.paint(g2);
        g2.dispose();
        
        return new ImageIcon(image);
    }
    
    /**
     * Attempts to load a company logo from a URL with caching.
     * If loading fails, returns a default icon.
     * 
     * @param logoUrl The URL of the company logo
     * @param width The desired width of the logo
     * @param height The desired height of the logo
     * @param defaultIconPath Path to the default icon to use if loading fails
     * @return The loaded logo as an ImageIcon, or a default icon if loading fails
     */
    public static ImageIcon loadCompanyLogo(String logoUrl, int width, int height, String defaultIconPath) {
        if (logoUrl == null || logoUrl.isEmpty()) {
            return loadIcon(defaultIconPath);
        }
        
        // Generate a cache key that includes dimensions
        String cacheKey = logoUrl + "_" + width + "x" + height;
        
        // Check cache first
        ImageIcon cachedIcon = LOGO_CACHE.get(cacheKey);
        if (cachedIcon != null) {
            return cachedIcon;
        }
        
        try {
            // Create URL
            URL url = new URL(logoUrl);
            
            // Open connection
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            // Set browser-like headers to avoid being blocked
            connection.setRequestProperty("User-Agent", 
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit"
                + "/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");
            
            // Connect and check response
            int responseCode = connection.getResponseCode();
            if (responseCode != java.net.HttpURLConnection.HTTP_OK) {
                System.err.println("HTTP error when loading logo: " + responseCode);
                return loadIcon(defaultIconPath);
            }
            
            // Read the image
            try (java.io.InputStream in = connection.getInputStream()) {
                Image image = javax.imageio.ImageIO.read(in);
                
                if (image != null) {
                    // Resize the image
                    Image resized = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(resized);
                    
                    // Cache the icon
                    LOGO_CACHE.put(cacheKey, icon);
                    
                    return icon;
                } else {
                    System.err.println("Failed to decode logo image");
                    return loadIcon(defaultIconPath);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading company logo from " + logoUrl + ": " + e.getMessage());
            return loadIcon(defaultIconPath);
        }
    }
    
    /**
     * Clears the logo cache to free memory when no longer needed.
     * This can be called during application shutdown or when
     * switching between major views.
     */
    public static void clearLogoCache() {
        LOGO_CACHE.clear();
    }
}
