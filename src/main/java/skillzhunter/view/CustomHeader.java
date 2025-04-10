package skillzhunter.view;

import java.awt.Component;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableCellRenderer;

/**
 * Custom header renderer that adds sort icons to table headers
 * without changing the sorting behavior.
 */
public class CustomHeader implements TableCellRenderer {
    private final TableCellRenderer defaultRenderer;
    private ImageIcon upArrowLight;
    private ImageIcon downArrowLight;
    private ImageIcon upArrowDark;
    private ImageIcon downArrowDark;
    private boolean isDarkMode = false;
    
    /**
     * Creates a new sortable header renderer with the specified renderer.
     * 
     * @param defaultRenderer The default renderer to use
     */
    public CustomHeader(TableCellRenderer defaultRenderer) {
        // Check for null defaultRenderer and provide a fallback
        if (defaultRenderer == null) {
            throw new IllegalArgumentException("Default renderer cannot be null");
        }
        
        this.defaultRenderer = defaultRenderer;
        
        // Load arrow images for both light and dark modes
        this.upArrowLight = loadIcon("images/arrow-up.png");
        this.downArrowLight = loadIcon("images/arrow-down.png");
        
        // Create white versions for dark mode
        this.upArrowDark = createColoredArrow(upArrowLight, Color.WHITE);
        this.downArrowDark = createColoredArrow(downArrowLight, Color.WHITE);
    }
    
    /**
     * Sets the current theme mode.
     * 
     * @param isDarkMode True if dark mode is active
     */
    public void setDarkMode(boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
    }
    
    /**
     * Creates a colored version of an arrow icon.
     * 
     * @param originalIcon The original icon
     * @param color The color to use
     * @return A new icon with the specified color
     */
    private ImageIcon createColoredArrow(ImageIcon originalIcon, Color color) {
        if (originalIcon == null) return null;
        
        int width = originalIcon.getIconWidth();
        int height = originalIcon.getIconHeight();
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Draw the original icon
        originalIcon.paintIcon(null, g2d, 0, 0);
        
        // Apply color filter
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgba = image.getRGB(x, y);
                int alpha = (rgba >> 24) & 0xff;
                if (alpha > 0) {
                    // Keep the alpha but use the new color
                    image.setRGB(x, y, (alpha << 24) | (color.getRed() << 16) | 
                                      (color.getGreen() << 8) | color.getBlue());
                }
            }
        }
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    /**
     * Loads an icon from the resources folder.
     * 
     * @param path The path to the icon
     * @return The loaded icon, or null if it couldn't be loaded
     */
    private ImageIcon loadIcon(String path) {
        try {
            URL url = getClass().getClassLoader().getResource(path);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                // Scale the image to an appropriate size for the header
                Image img = icon.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + path);
        }
        
        return null;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                 boolean isSelected, boolean hasFocus,
                                                 int row, int column) {
        // Defensive check - if defaultRenderer is somehow null (should never happen)
        if (defaultRenderer == null) {
            // Create a simple fallback renderer
            JLabel fallback = new JLabel(value != null ? value.toString() : "");
            fallback.setOpaque(true);
            fallback.setBackground(Color.WHITE);
            fallback.setForeground(Color.BLACK);
            return fallback;
        }
        
        // Get the default component (usually a JLabel)
        Component comp = defaultRenderer.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);
        
        if (comp instanceof JLabel) {
            JLabel label = (JLabel) comp;
            
            // Special case for Logo column - don't show any sort icon
            if (column == 0 && "Logo".equals(value)) {
                label.setIcon(null);
                label.setHorizontalAlignment(JLabel.CENTER); // Center the Logo text
                return label;
            }
            
            // Choose icons based on the current theme
            ImageIcon downArrow = isDarkMode ? downArrowDark : downArrowLight;
            ImageIcon upArrow = isDarkMode ? upArrowDark : upArrowLight;
            
            // Start with default down arrow for all columns
            ImageIcon icon = downArrow;
            
            // Check if there's active sorting
            RowSorter<?> sorter = table.getRowSorter();
            if (sorter != null && !sorter.getSortKeys().isEmpty()) {
                // Get the model column index for this view column
                int modelColumn = table.convertColumnIndexToModel(column);
                
                // Check if this is the currently sorted column
                if (!sorter.getSortKeys().isEmpty() && 
                    sorter.getSortKeys().get(0).getColumn() == modelColumn) {
                    // Show the appropriate arrow based on sort direction
                    SortOrder sortOrder = sorter.getSortKeys().get(0).getSortOrder();
                    icon = (sortOrder == SortOrder.ASCENDING) ? upArrow : downArrow;
                } else {
                    // Not the sorted column, always show default down arrow
                    icon = downArrow;
                }
            }
            
            // Set the icon on the label
            if (icon != null) {
                label.setIcon(icon);
                label.setHorizontalTextPosition(JLabel.LEFT);
            }
        }
        
        return comp;
    }
}