package EverlinFrame;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;


 @SuppressWarnings("serial")
public class InteractiveTable extends JPanel {
     public static final String[] columnNames = {
         "Номер", "Наименование",""
     };

     private JTable table;
     private JScrollPane scroller;
     private InteractiveTableModel tableModel;

     public InteractiveTable() {
         initComponent();
     }

     public void initComponent() {
         tableModel = new InteractiveTableModel(columnNames);
         tableModel.addTableModelListener(new InteractiveTable.InteractiveTableModelListener());
         
         table = new JTable();
         table.setModel(tableModel);
         table.setSurrendersFocusOnKeystroke(true);
         if (!tableModel.hasEmptyRow()) {
             tableModel.addEmptyRow();
         }
         scroller = new javax.swing.JScrollPane(table);
         table.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 300));
         TableColumn hidden = table.getColumnModel().getColumn(InteractiveTableModel.HIDDEN_INDEX);
         hidden.setMinWidth(2);
         hidden.setPreferredWidth(2);
         hidden.setMaxWidth(2);
         hidden.setCellRenderer(new InteractiveRenderer(InteractiveTableModel.HIDDEN_INDEX));
         table.getColumnModel().getColumn(0).setPreferredWidth(45);
 		 table.getColumnModel().getColumn(0).setMinWidth(45);
 		 table.getColumnModel().getColumn(0).setMaxWidth(45);
 		 table.getColumnModel().getColumn(1).setPreferredWidth(80);
 		 table.getColumnModel().getColumn(1).setMinWidth(50);
 		 table.getColumnModel().getColumn(2).setPreferredWidth(2);
 		 table.getColumnModel().getColumn(2).setMinWidth(2);
 		 table.getColumnModel().getColumn(2).setMaxWidth(2);
 	 	 table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
 		 table.setFillsViewportHeight(true);
         setLayout(new BorderLayout());
         add(scroller, BorderLayout.CENTER);
     }

     public JTable getTable()
     {
    	 return table;
     }
     
     public InteractiveTableModel getModel()
     {
    	 return tableModel;
     }
     
     
     public void highlightLastRow(int row) {
         int lastrow = tableModel.getRowCount();
         if (row == lastrow - 1) {
             table.setRowSelectionInterval(lastrow - 1, lastrow - 1);
         } else {
             table.setRowSelectionInterval(row + 1, row + 1);
         }

         table.setColumnSelectionInterval(1, 1);
     }

     class InteractiveRenderer extends DefaultTableCellRenderer {
         protected int interactiveColumn;

         public InteractiveRenderer(int interactiveColumn) {
             this.interactiveColumn = interactiveColumn;
         }

         public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row,int column)
         {
             Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
             if (column == interactiveColumn && hasFocus) {
                 if ((InteractiveTable.this.tableModel.getRowCount() - 1) == row &&
                    !InteractiveTable.this.tableModel.hasEmptyRow())
                 {
                     InteractiveTable.this.tableModel.addEmptyRow();
                 }

                 highlightLastRow(row);
             }

             return c;
         }
     }
     public class InteractiveTableModelListener implements TableModelListener {
         public void tableChanged(TableModelEvent evt) {
             if (evt.getType() == TableModelEvent.UPDATE) {
                 int column = evt.getColumn();
                 int row = evt.getFirstRow();
                 System.out.println("row: " + row + " column: " + column);
                 table.setColumnSelectionInterval(column + 1, column + 1);
                 table.setRowSelectionInterval(row, row);
             }
         }
     }
 }