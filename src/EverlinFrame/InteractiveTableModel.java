package EverlinFrame;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class InteractiveTableModel extends AbstractTableModel {
    public static final int ROW_INDEX = 0;
    public static final int POLYGON_INDEX = 1;
    public static final int HIDDEN_INDEX = 2;

    protected String[] columnNames;
    protected Vector<TableItemDomain> dataVector;

    public InteractiveTableModel(String[] columnNames) {
        this.columnNames = columnNames;
        dataVector = new Vector<TableItemDomain>();
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    public boolean isCellEditable(int row, int column) {
        switch (column) {
        case POLYGON_INDEX:
        	return true;
        default:
        	return false;
        }
    }
    
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case ROW_INDEX:
               return Integer.class;
            case POLYGON_INDEX:
               return String.class;
            default:
               return Object.class;
        }
    }

    public Object getValueAt(int row, int column) {
    	TableItemDomain item = (TableItemDomain)dataVector.get(row);
        switch (column) {
            case ROW_INDEX:
               return item.getRow();
            case POLYGON_INDEX:
               return item.getPolyString();
            default:
               return new Object();
        }
    }

    public void setValueAt(Object value, int row, int column) {
    	TableItemDomain item = (TableItemDomain)dataVector.get(row);
        switch (column) {
            case ROW_INDEX:
            	item.setRow((Integer)value);
               break;
            case POLYGON_INDEX:
               item.setPolyString((String)value);
               break;
            default:
               System.out.println("Invalid index " + column);
        }
        fireTableCellUpdated(row, column);
    }

    public int getRowCount() {
        return dataVector.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public boolean hasEmptyRow() {
        if (dataVector.size() == 0) return false;
        TableItemDomain item = (TableItemDomain)dataVector.get(dataVector.size() - 1);
        if (item.getPolyString().trim().equals(""))
        {
        	return true;
        }
        else
        	return false;
    }

    public void init(Vector<TableItemDomain> scheme)
    {
    	dataVector = new Vector<TableItemDomain>(scheme);
    	fireTableRowsInserted(0,dataVector.size() - 1);
    	addEmptyRow();
    }
    
    public void addEmptyRow() {
    	TableItemDomain item = new TableItemDomain();
    	item.setRow(dataVector.size());
        dataVector.add(item);
        fireTableRowsInserted(dataVector.size() - 1,dataVector.size() - 1);
    }
    
    public void addRow(String arg)
    {
    	if (dataVector.size() == 1)
    	{
    		if(dataVector.get(0).getPolyString().equals(""))
    		{
    			dataVector.remove(0);
    		}

    	}
    	TableItemDomain item = new TableItemDomain();
    	item.setRow(dataVector.size());
    	item.setPolyString(arg);    	
        dataVector.add(item);
        fireTableRowsInserted(dataVector.size() - 1 ,dataVector.size() - 1);
    }
    
}