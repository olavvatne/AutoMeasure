package model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import utilities.ExcelModel;
import utilities.measurement.Measurement;
import utilities.measurement.ValueMeasurement;
import view.settingsPanel.Setting;
import analyze.ImageMarkerPoint;
import automeasurer.ConfigurationManager;

/**
 * The data model for a measurement picture. Each measurement picture contain
 * the filepath to the actual image, the measured values, the picture date, and
 * status.
 * 
 * Implements ExcelModel to let ExcelWriter know that this class has the
 * necessary methods.
 * 
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
	public static List<ImageMarkerPoint> SUGGESTED_MARKERS;
	public static int objects = 0;

	private Status status;
	private DateTime date;
	private List<ImageMarkerPoint> markers;
	private double[] values;
	private String filepath;
	private int id;

	public ImageDataModel(DateTime date, String path) {
		this.date = date;
		this.status = Status.UNKNOWN;

		this.values = new double[2];
		this.filepath = path;
		this.id = objects;
		objects++;
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
		if (markers != null && i < markers.size()) {
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
		if (this.markers != null
				&& (this.status == Status.SUCCESS || this.status == Status.MANUAL_EDIT)) {
			return true;
		} else {
			return false;
		}
	}

	public double getMarkerXPosition(int i) {
		if (markers != null && i < markers.size()) {
			return this.markers.get(i).getX();
		}
		return -1;
	}

	public void setMarkerXPosition(int i, double x) {
		if (markers != null && i < markers.size()) {
			this.markers.get(i).setX(x);
		}
	}

	public double getValuesXPosition(int i) {
		if (values != null && i < values.length) {
			return this.values[i];
		}
		return -1;
	}

	public void setValuesXPosition(int i, double x) {
		if (i < values.length) {
			this.values[i] = x;
		}
	}

	public void setMarkers(List<ImageMarkerPoint> markers) {
		this.SUGGESTED_MARKERS = markers;
		this.markers = markers;
	}

	public double[] getValues() {
		return this.values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	// for tablemodel
	public Object get(int i) {
		switch (i) {
		case 0: {
			return (Object) getStatus();
		}
		case 1: {
			return (Object) getDate();
		}
		default: {
			return null;
		}
		}
	}

	public void set(int i, Object o) {
		switch (i) {
		case 0: {
			setStatus((Status) o);
			break;
		}
		case 1: {
			// no-op
			break;
		}
		default: {
			break;
		}
		}
	}

	public double[] getMeasurementValues() {
		if (isMarkersValid()) {
			ValueMeasurement valueCalculator = Measurement.calculator();
			valueCalculator.setCalculator(Offset.getOffset(id),
					MarkerValue.getMarkerValues(this.id), markers, values);
			return new double[] { valueCalculator.getThreePhaseValue(OW_VALUE),
					valueCalculator.getThreePhaseValue(OG_VALUE) };
		} else {
			return new double[] { -1, -1 };
		}
	}

	public DateTime getDate() {
		return this.date;
	}

	public void setMarkers(double[] markers2) {
		List<ImageMarkerPoint> list = new ArrayList<ImageMarkerPoint>();
		for (int i = 0; i < markers2.length; i++) {
			ImageMarkerPoint p = new ImageMarkerPoint(markers2[i], 0, 0);
			list.add(p);
		}
		SUGGESTED_MARKERS = list;
		this.markers = list;

	}

	// Excel model required method for writing.
	@Override
	public String[] getRowAsStringRow() {
		double[] values = this.getMeasurementValues();
		// TODO: Should use a hashmap for sending this kind of data.
		// This will result in some context for the data, when each value has a
		// key.
		// TODO: Either change invalid values to the most negative number, or
		// use null values instead. -1 can happen for a valid reading.
		if (values[0] == -1.0 || values[1] == -1.0) {
			return new String[] {};
		}
		return new String[] {
				this.date.toString(ConfigurationManager.getManager().get(
						Setting.DATE_REGEX)),
				this.date.toString(ConfigurationManager.getManager().get(
						Setting.TIME_REGEX)), values[0] + "", values[1] + "" };
	}
}
