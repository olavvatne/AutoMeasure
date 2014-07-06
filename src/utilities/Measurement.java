package utilities;

import java.util.Date;
import java.util.List;

import model.ImageDataModel;
import model.Status;
import analyze.ImageMarkerPoint;

public class Measurement {
	
	/**
	 * Converting from pixel markers and values into a measurement can be done using the
	 * measurement class. The static calculator method return the single instance of calculator.
	 * The measurement object contain method for populating the calculator with values and a method for retrieving the 
	 * measured value.
	 */
	private static Measurement calculator = new Measurement();
	
	public static Measurement calculator() {
		return calculator;
	}
	
	public static final int OG_VALUE = 0;
	public static final int OW_VALUE = 1;
	
	public static final int OW_HIGH = 3;
	public static final int OW_LOW = 2;
	public static final int OG_HIGH = 0;
	public static final int OG_LOW = 1;
	
	public static final int NUMBER_OF_THREEPHASE_VALUES = 2;
	
	private double[] offset;
	private double[] markerValue;
	private List<ImageMarkerPoint> markers;
	private	double[] values;
	
	public void setCalculator(double[] offset, double[] markerValue, List<ImageMarkerPoint> markers, double[] values) {
		this.offset = offset;
		this.markerValue = markerValue;
		this.markers = markers;
		this.values = values;
		
	}
	
	public double getThreePhaseValue(int i) {
		if (i<NUMBER_OF_THREEPHASE_VALUES) {
			if(i == OG_VALUE) {
				return getValue(OG_LOW, OG_HIGH, OG_VALUE);
			}
			else if( i == OW_VALUE) {
				return getValue(OW_LOW, OW_HIGH, OW_VALUE);
			}
		}
		return -1;
	}
	
	private double getMarkerXPosition(int i) {
		if(i< markers.size()) {
			return this.markers.get(i).getX();
		}
		return -1;
	}
	
	private double getValuesXPosition(int i) {
		if (i< values.length) {
			return this.values[i];
		}
		return -1;
	}
	
	//START MEASUREMENT METHODS
	private double getValue(int markerLow, int markerHigh,  int valueType) {
		double lineInPixel = getLinePosWithOffset(markerHigh) - getLinePosWithOffset(markerLow);
		double lineInValue = getLineValue(markerHigh) - getLineValue(markerLow);
		double lineToPos = getValueLinePos(valueType) - getLinePosWithOffset(markerLow);
		return ((lineToPos/lineInPixel)*lineInValue)+ getLineValue(markerLow);
	}

	/**
	 * 
	 * @param i Marker nr (i) in the array, or image from left to right
	 * @return pixel position of the line with the offset 
	 */
	private double getLinePosWithOffset(int i) {
		if (i%2== 0) {
			return getMarkerXPosition(i)
					+ getOffset(getMarkerXPosition(i), getMarkerXPosition(i+1), ImageDataModel.offset[i]);
		}
		else {
			return getMarkerXPosition(i)
					- getOffset(getMarkerXPosition(i-1),getMarkerXPosition(i), ImageDataModel.offset[i]);
		}
	}

	private double getValueLinePos(int i) {
		return getValuesXPosition(i);
	}

	/**
	 * Offset is an array with values between 0-1
	 * @param x1 - pos in range
	 * @param x2 - pos in range
	 * @param percentage of this range
	 * @return a range value the offset should have
	 */
	private static double getOffset(double x1, double x2, double percentage) {
		return ((x2 - x1)*percentage);
	}

	/**
	 * What  value the marker line represent.
	 * Typically between 0-20. Each marker line has such a value.
	 * @param i
	 * @return
	 */

	private double getLineValue(int i) {
		return markerValue[i];
	}
	// END CALCULATING METHODS FOR MEASUREMENT VALUE
}
