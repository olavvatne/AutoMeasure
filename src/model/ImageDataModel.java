package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utilities.ExcelModel;
import utilities.Measurement;
import analyze.ImageMarkerPoint;

/**
 * The data model for a measurement picture. 
 * Each measurement picture contain the filepath
 * to the actual image, the measured values, the
 * picture date, and status.
 * 
 * Implements ExcelModel to let ExcelWriter know that
 * this class has the necessary methods.
 * @author Olav
 *
 */
public class ImageDataModel implements ExcelModel {
	public static final int OG_VALUE = 0;
	public static final int OW_VALUE = 1;
	
	public static final int OW_HIGH = 3;
	public static final int OW_LOW = 2;
	public static final int OG_HIGH = 0;
	public static final int OG_LOW = 1;
	
	public static int objects = 0;
	
	private Status status;
	private Date date;
	private List<ImageMarkerPoint> markers;
	private	double[] values;
	private String filepath;
	private int id; 
	
	public ImageDataModel(Date date, String path) {
		this.date = date;
		this.status = Status.UNKNOWN;
		
		this.values = new double[2];
		this.filepath = path;
		this.id = objects;
		objects ++;
	}

	public String getFilePath() {
		return this.filepath;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public int getId() {
		return this.id;
	}
	
	public double markerArea(int i) {
		if(markers != null && i< markers.size()) {
			return this.markers.get(i).getArea();
		}
		return -1;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	public List<ImageMarkerPoint> getMarkers() {
		return markers;
	}
	public boolean isMarkersValid() {
		if(this.markers != null) {
			return true;
		}
		else {
			return false;
		}
	}
	public double getMarkerXPosition(int i) {
		if(markers != null && i< markers.size()) {
			return this.markers.get(i).getX();
		}
		return -1;
	}
	
	public void setMarkerXPosition(int i, double x) {
		if (markers != null &&  i< markers.size()) {
			this.markers.get(i).setX(x);
		}
	}
	
	public double getValuesXPosition(int i) {
		if (markers != null &&  i< values.length) {
			return this.values[i];
		}
		return -1;
	}
	
	public void setValuesXPosition(int i, double x) {
		if (i< values.length) {
			this.values[i] = x;
		}
	}
	
	
	public void setMarkers(List<ImageMarkerPoint> markers) {
		this.markers = markers;
	}


	public double[] getValues() {
		return this.values;
	}


	public void setValues(double[] values) {
		this.values = values;
	}
	
	//for tablemodel
	public Object get(int i) {
		switch(i) {
		case 0: {
			return (Object)getStatus();
		}
		case 1: {
			return (Object)getDate();
		}
		default: {
			return null;
		}
		}
	}
	
	public void set(int i, Object o) {
		switch(i) {
		case 0: {
			setStatus((Status) o);
			break;
		}
		case 1: {
			//no-op
			break;
		}
		default: {
			break;
		}
		}
	}
	
	public double[] getMeasurementValues() {
		if (status == Status.SUCCESS && isMarkersValid()) {
			Measurement valueCalculator = Measurement.calculator();
			valueCalculator.setCalculator(Offset.getOffset(id), MarkerValue.getMarkerValues(this.id), markers, values);
			return new double[]{valueCalculator.getThreePhaseValue(OW_VALUE), valueCalculator.getThreePhaseValue(OG_VALUE)};
		}
		else {
			return new double[]{-1, -1};
		}
	}
	
	
	public Date getDate() {
		return date;
	}

	public void setMarkers(double[] markers2) {
		List<ImageMarkerPoint> list = new ArrayList<ImageMarkerPoint>();
		for( int i=0; i< markers2.length; i++) {
			ImageMarkerPoint p = new ImageMarkerPoint(markers2[i], 0, 0);
			list.add(p);
		}
		this.markers = list;
		
	}
	
	//Used for excel stuff. Maybe make it more general. F.ex the interface for excelModel interface has getter for date etc
	//values, and the excelwriter decide how its written using some rules.	
	private String getFormattedDate() {
		SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yy");
		return dtf.format(this.date);
	}
	private String getFormattedTime() {
		SimpleDateFormat dtf = new SimpleDateFormat("hh:mm:ss");
		return dtf.format(this.date);
	}
	//Excel model required method for writing.
	@Override
	public String[] getRowAsStringRow() {
		double[] values = this.getMeasurementValues();
		return new String[]{getFormattedDate(), getFormattedTime(), values[0] +"", values[1] + ""};
	}
}
