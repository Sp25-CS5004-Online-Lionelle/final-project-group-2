package skillzhunter.view;

import java.awt.Component;
import java.awt.Image;
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
    private final ImageIcon upArrow;
    private final ImageIcon downArrow;
    
    /**
     * Creates a new sortable header renderer with the specified renderer.
     * 
     * @param defaultRenderer The default renderer to use
     */
    public CustomHeader(TableCellRenderer defaultRenderer) {
        this.defaultRenderer = defaultRenderer;
        
        // Load arrow images
        this.upArrow = loadIcon("images/arrow-up.png");
        this.downArrow = loadIcon("images/arrow-down.png");
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
        // Get the default component (usually a JLabel)
        Component comp = defaultRenderer.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);
        
        if (comp instanceof JLabel) {
            JLabel label = (JLabel) comp;
            
            // By default, show the down arrow to indicate the column is sortable
            ImageIcon icon = downArrow;
            
            // Check if this column is the current sort column
            RowSorter<?> sorter = table.getRowSorter();
            if (sorter != null && !sorter.getSortKeys().isEmpty()) {
                // Get the actual model column index since view columns can be reordered
                int modelColumn = table.convertColumnIndexToModel(column);
                
                // Find if this column is being sorted
                for (RowSorter.SortKey sortKey : sorter.getSortKeys()) {
                    if (sortKey.getColumn() == modelColumn) {
                        // This column is being sorted, show the appropriate arrow
                        icon = sortKey.getSortOrder() == SortOrder.ASCENDING ? upArrow : downArrow;
                        break;
                    }
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