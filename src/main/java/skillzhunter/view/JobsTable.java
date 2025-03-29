package skillzhunter.view;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import skillzhunter.model.JobRecord;

public class JobsTable extends JTable {

  private DefaultTableModel tableModel;
  private final Collection<JobRecord> jobs = new ArrayList<>();
  private String[] columnNames = {"id", "url", "jobSlug", "jobTitle", "companyName",
      "companyLogo", "jobIndustry", "jobType", "jobGeo", "jobLevel",
      "jobExcerpt", "jobDescription", "pubDate", "annualSalaryMin",
      "annualSalaryMax", "salaryCurrency"};

  public JobsTable() {
    this(new String[] {}, new Object[][] {});
    setPreferredScrollableViewportSize(new Dimension(500, 200));
  }

  public JobsTable(String[] columnNames, Object[][] data) {
    tableModel = new DefaultTableModel(data, columnNames);
    setModel(tableModel);
  }

    public void setData (Object[][]data){
      tableModel.setDataVector(data, getColumnNames());
      tableModel.fireTableDataChanged();
    }

    public void setColumnNames (String[]columnNames){
      tableModel.setColumnIdentifiers(columnNames);
      tableModel.fireTableStructureChanged();
    }

    public void addRow (Object[]rowData){
      tableModel.addRow(rowData);
    }

    public void removeRow ( int row){
      tableModel.removeRow(row);
    }

    public void updateCell ( int row, int column, Object value){
      tableModel.setValueAt(value, row, column);
    }

    public Object getValueAt ( int row, int column){
      return tableModel.getValueAt(row, column);
    }

    public int getRowCount () {
      return tableModel.getRowCount();
    }

    public int getColumnCount() {
      return tableModel.getColumnCount();
    }

    public String[] getColumnNames() {
      return this.columnNames;
    }

  }
