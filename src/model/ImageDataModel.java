package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utilities.ExcelModel;
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
	
	public static double[] offset = new double[]{0.10, 0.10, 0.10, 0.1};
	private Status status;
	private Date date;
	private List<ImageMarkerPoint> markers;
	private	double[] values;
	private String filepath;
	
	
	public ImageDataModel(Date date, String path) {
		this.date = date;
		this.status = Status.UNKNOWN;
		
		this.values = new double[2];
		this.filepath = path;
	}

	public String getFilePath() {
		return this.filepath;
	}
	
	public Status getStatus() {
		return status;
	}
	public double markerArea(int i) {
		return this.markers.get(i).getArea();
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
		if(i< markers.size()) {
			return this.markers.get(i).getX();
		}
		return -1;
	}
	
	public void setMarkerXPosition(int i, double x) {
		if (i< markers.size()) {
			this.markers.get(i).setX(x);
		}
	}
	
	public double getValuesXPosition(int i) {
		if (i< values.length) {
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
		return values;
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
		case 2: {
			return "Melding utifra status";
		}
		default: {
			return null;
		}
		}
	}
	
	public void set(int i, Object o) {
		switch(i) {
		case 0: {
			setStatus((Status) o);;
			break;
		}
		case 1: {
			//no-op
			break;
		}
		case 2: {
			//feilmelding -forel�ping no op
			break;
		}
		default: {
			break;
		}
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
	//Excel model required method for writing.
	@Override
	public String[] getRowAsStringRow() {
		return new String[]{this.date.toString()+ "", this.values[0] +"", this.values[1] + ""};
	}
}