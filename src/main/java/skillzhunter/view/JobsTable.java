package skillzhunter.view;

import java.awt.Dimension;
import java.util.Comparator;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 * A table for displaying job listings with basic sorting functionality.
 */
public class JobsTable extends JTable {
  /** 
   * Table model for the job listings.
   */
  private DefaultTableModel tableModel;
  /** 
   * Row sorter for sorting the table rows.
   */
  private TableRowSorter<DefaultTableModel> sorter;
  /** 
   * Column names for the table.
   */
  private String[] columnNames = {"Logo", "Job Title", "Company", "Level", "Salary Range", "Currency" };
  /** 
   * Custom header renderer for sortable columns.
   */
  private CustomHeader customHeaderRenderer;
  /**
   * Current color theme for the table.
   */
  private ColorTheme currentTheme = ColorTheme.LIGHT; // Default theme
  /**
   * Image cell renderer for displaying logos.
   */
  private ImageCellRenderer imageCellRenderer = new ImageCellRenderer();

  /**
   * Default constructor that initializes the table with no data.
   */
  public JobsTable() {
    this(new String[] {}, new Object[0][0]);
    setPreferredScrollableViewportSize(new Dimension(1000, 500));
  }
  /**
   * Constructor that initializes the table with specified column names and data.
   * This constructor is used to create the table with data from the controller.
   * @param columnNames
   * @param data
   */
  public JobsTable(String[] columnNames, Object[][] data) {
    // Create a non-editable table model
    tableModel = new DefaultTableModel(data, columnNames) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false; // Make all cells non-editable
      }
    };
    
    setModel(tableModel);
    
    // Create and set the row sorter directly for type safety
    sorter = new TableRowSorter<>(tableModel);
    setRowSorter(sorter);
    
    // Configure the sorter
    configureColumnSorters();
    
    // Set selection behavior
    setRowSelectionAllowed(true);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    // Disable column reordering
    getTableHeader().setReorderingAllowed(false);
    
    // Set up custom header renderer
    setupHeaderRenderer();
    
    // Set up custom cell renderer for image column
    setupColumnRenderers();
    
    // Increase row height to give more space for images
    setRowHeight(44);
  }
  
  /**
   * Sets up custom renderers for specific columns.
   */
  private void setupColumnRenderers() {
    // Set the logo column renderer (column 0)
    if (getColumnCount() > 0) {
      TableColumn logoColumn = getColumnModel().getColumn(0);
      logoColumn.setCellRenderer(imageCellRenderer);
      
      // Set a fixed width for the logo column - wider to show "Logo" text fully
      logoColumn.setPreferredWidth(60);
      logoColumn.setMaxWidth(60);
      logoColumn.setMinWidth(60);
    }
  }
  
  /**
   * Sets the current theme for the table and header.
   * 
   * @param theme The color theme to apply
   */
  public void applyTheme(ColorTheme theme) {
    // Store the current theme
    this.currentTheme = theme;
    
    // Update the header renderer with the theme
    if (customHeaderRenderer != null) {
      customHeaderRenderer.setDarkMode(theme == ColorTheme.DARK);
      
      // Force refresh of the header
      if (getTableHeader() != null) {
        getTableHeader().repaint();
      }
    }
  }
  
  /**
   * Sets up the custom header renderer for visual indication of sortable columns.
   */
  private void setupHeaderRenderer() {
    JTableHeader header = getTableHeader();
    if (header != null) {
      // Safely get the default renderer - IMPORTANT: handle potential null
      TableCellRenderer defaultRenderer = header.getDefaultRenderer();
      if (defaultRenderer != null) {
        // Apply custom renderer without changing sort behavior
        customHeaderRenderer = new CustomHeader(defaultRenderer);
        
        // Initialize with the current theme
        customHeaderRenderer.setDarkMode(currentTheme == ColorTheme.DARK);
        
        header.setDefaultRenderer(customHeaderRenderer);
        
        // Add hand cursor to indicate clickable headers
        header.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
      } else {
        // Log the error
        System.err.println("Warning: Default table header renderer is null");
      }
    }
  }
  /**
   * sets the data for the table model and reconfigures the sorter.
   * @param data
   */
  public void setData(Object[][] data) {
    tableModel.setDataVector(data, getColumnNames());
    
    // Re-create the sorter for the updated model
    sorter = new TableRowSorter<>(tableModel);
    setRowSorter(sorter);
    
    // Re-configure the sorter
    configureColumnSorters();
    
    // Re-apply the custom header renderer
    setupHeaderRenderer();
    
    // Re-apply column renderers for logos
    setupColumnRenderers();
    
    // Re-apply the current theme to ensure consistent styling
    applyTheme(currentTheme);
    
    // Make sure we maintain the increased row height
    setRowHeight(44);
  }

  /**
   * Configure any special column comparators for the sorter.
   */
  private void configureColumnSorters() {
    if (sorter != null) {
      // Add special comparator for the Salary Range column (index 4)
      sorter.setComparator(4, new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
          if (s1.equals("N/A") && s2.equals("N/A")) {
            return 0;
          } else if (s1.equals("N/A")) {
            return -1;
          } else if (s2.equals("N/A")) {
            return 1;
          }
          
          // Extract the minimum salary value from the ranges
          int min1 = extractMinSalary(s1);
          int min2 = extractMinSalary(s2);
          
          return Integer.compare(min1, min2);
        }
      });
      
      // Disable sorting for the logo column
      sorter.setSortable(0, false);
      
      // Enable single-column sorting mode
      sorter.setSortsOnUpdates(true);
    }
  }
  
  /**
   * Helper method to extract minimum salary from range string.
   * @param salaryRange The salary range string (e.g., "120,000 - 150,000")
   * @return The minimum salary as an integer, or 0 if parsing fails
   */
  private int extractMinSalary(String salaryRange) {
    try {
      // Handle format like "120,000 - 150,000"
      String minPart = salaryRange.split("-")[0].trim();
      // Remove commas and non-numeric characters
      minPart = minPart.replaceAll("[^0-9]", "");
      return Integer.parseInt(minPart);
    } catch (Exception e) {
      return 0; // If parsing fails, default to 0
    }
  }

  /**
   * Sets the column names for the table model and reconfigures the header renderer.
   * @param columnNames The new column names to set
   */
  public void setColumnNames(String[] columnNames) {
    tableModel.setColumnIdentifiers(columnNames);
    setupHeaderRenderer(); // Reapply the custom header
    setupColumnRenderers(); // Set up the image renderer for logo column
    
    // Re-apply theme to ensure consistent styling
    applyTheme(currentTheme);
  }
  /**
   * Adds a new row to the table model with the specified data.
   */
  public void addRow(Object[] rowData) {
    tableModel.addRow(rowData);
  }
  /**
   * Removes a row from the table model at the specified index.
   * @param row The index of the row to remove
   */
  public void removeRow(int row) {
    int modelRow = convertRowIndexToModel(row);
    tableModel.removeRow(modelRow);
  }
  /**
   * Updates a cell in the table model at the specified row and column with the given value.
   * @param row The row index of the cell to update
   */
  public void updateCell(int row, int column, Object value) {
    int modelRow = convertRowIndexToModel(row);
    tableModel.setValueAt(value, modelRow, column);
  }
  /**
   * Returns the value at the specified row and column in the table model.
   * @return The value at the specified row and column
   */
  @Override
  public int getRowCount() {
    return tableModel.getRowCount();
  }

  @Override
  public int getColumnCount() {
    return tableModel.getColumnCount();
  }
  /**
   * Returns the column names of the table model.
   * @return The column names of the table model
   */
  public String[] getColumnNames() {
    return this.columnNames;
  }
  
  /**
   * Clean up resources when the table is no longer needed.
   */
  public void cleanup() {
    if (imageCellRenderer != null) {
      imageCellRenderer.clearCache();
    }
  }
}
