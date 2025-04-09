package skillzhunter.view;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import skillzhunter.model.JobRecord;

/**
 * A table for displaying job listings with basic sorting functionality.
 */
public class JobsTable extends JTable {

  private DefaultTableModel tableModel;
  private TableRowSorter<DefaultTableModel> sorter;
  private String[] columnNames = { "Job Title", "Company", "Level", "Salary Range", "Currency" };

  public JobsTable() {
    this(new String[] {}, new Object[0][0]);
    setPreferredScrollableViewportSize(new Dimension(1000, 500));
  }

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
  }
  
  /**
   * Sets up the custom header renderer for visual indication of sortable columns
   */
  private void setupHeaderRenderer() {
    JTableHeader header = getTableHeader();
    if (header != null) {
      // Apply custom renderer without changing sort behavior
      header.setDefaultRenderer(new CustomHeader(header.getDefaultRenderer()));
      
      // Add hand cursor to indicate clickable headers
      header.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }
  }

  public void setData(Object[][] data) {
    tableModel.setDataVector(data, getColumnNames());
    
    // Re-create the sorter for the updated model
    sorter = new TableRowSorter<>(tableModel);
    setRowSorter(sorter);
    
    // Re-configure the sorter
    configureColumnSorters();
    
    // Re-apply the custom header renderer
    setupHeaderRenderer();
  }

  /**
   * Configure any special column comparators for the sorter
   */
  private void configureColumnSorters() {
    if (sorter != null) {
      // Add special comparator for the Salary Range column (index 3)
      sorter.setComparator(3, new Comparator<String>() {
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
    }
  }
  
  /**
   * Helper method to extract minimum salary from range string
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

  public void setColumnNames(String[] columnNames) {
    tableModel.setColumnIdentifiers(columnNames);
    setupHeaderRenderer(); // Reapply the custom header
  }

  public void addRow(Object[] rowData) {
    tableModel.addRow(rowData);
  }

  public void removeRow(int row) {
    int modelRow = convertRowIndexToModel(row);
    tableModel.removeRow(modelRow);
  }

  public void updateCell(int row, int column, Object value) {
    int modelRow = convertRowIndexToModel(row);
    tableModel.setValueAt(value, modelRow, column);
  }

  @Override
  public int getRowCount() {
    return tableModel.getRowCount();
  }

  @Override
  public int getColumnCount() {
    return tableModel.getColumnCount();
  }

  public String[] getColumnNames() {
    return this.columnNames;
  }
}