package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;
import java.util.ArrayList;



import java.util.List;

import javax.swing.table.AbstractTableModel;

import utilities.ExcelModel;
import analyze.ImageMarkerPoint;
import automeasurer.Measurer;


public class ImageTableModel extends AbstractTableModel implements PropertyChangeListener {
	private String[] columnNames = {"status", "Date", "Feilmelding"};
	protected ArrayList<ImageDataModel> data;
	protected PropertyChangeSupport pcs;
	
	public ImageTableModel() {
		super();
		this.data = new ArrayList<ImageDataModel>();
		pcs = new PropertyChangeSupport(this);
	}

	public String getColumnName(int column) {
		if(column < this.columnNames.length)
			return this.columnNames[column];
		else
			return "No name";
	}
	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public int getRowCount() {
		if(data != null)
			return data.size();
		else 
			return -1;
	}


	public void appendRow(ImageDataModel row) {
		this.data.add(row);
		this.fireTableRowsInserted(this.data.size()-1, this.data.size()-1);
	}

	//kan ikke stå lol her 
	public void deleteRow() {
		System.out.println(0);
		Integer lol = 0;
		this.data.remove(lol);
		this.fireTableRowsDeleted(lol, lol);
		this.pcs.firePropertyChange("DELETE", null, lol );
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex).get(columnIndex);
	}
	
	public void setMeasurementsAt(int rowIndex, double[] values) {
		this.data.get(rowIndex).setValues(values);
	}
	
	public void setMarkersAt(int rowIndex, List<ImageMarkerPoint> markers) {
		this.data.get(rowIndex).setMarkers(markers);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	
	public void setValueAt(Object value, int row, int col)
	
	{
	
	  this.data.get(row).set(col, value);

	  fireTableCellUpdated(row, col);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {

		if(e.getPropertyName().equals("ADD")) {
			this.data.add((ImageDataModel) e.getNewValue());
			this.fireTableRowsInserted(this.data.size()-1, this.data.size()-1);
		}
		else if(e.getPropertyName().equals("STATUS_CHANGE")) {
			//something something
		}
		else if ( e.getPropertyName().equals(Measurer.FINISHED)) {
		
			setData((ArrayList<ImageDataModel>)e.getNewValue());
		}

	}
	
	public void setData(ArrayList<ImageDataModel> images) {
		
		int size = this.data.size();
		this.data = images;
		this.fireTableRowsInserted(size-1, this.data.size()-1);
	}
	
	public Class getColumnClass(int c) {
		switch(c) {
		case 0: {
			return Enum.class;
		}
		case 1: {
			return Date.class;
		}
		default: {
			return String.class;
		}
		}
	}
	
	public ImageDataModel getDataModel(int i) {
		if(i<this.data.size()) {
			return this.data.get(i);
		}
		else {
			return null;
		}
		
	}
	
	/**
	 * TODO: Check if there are better ways to do this, even though we know that list will not contain any other objects than objecs with ExcelModel interface.
	 * @return List of excelModel compatible objects.
	 */
	public List<ExcelModel> getDataModel() {
		return (List<ExcelModel>)(Object)this.data;
	}
	
	
}