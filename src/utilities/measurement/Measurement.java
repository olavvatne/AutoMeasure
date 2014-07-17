package utilities.measurement;

import java.util.Date;
import java.util.List;

import model.ImageDataModel;
import model.Status;
import analyze.ImageMarkerPoint;

public class Measurement implements ValueMeasurement {
	
	/**
	 * Converting from pixel markers and values into a measurement can be done using the
	 * measurement class. The static calculator method return the single instance of calculator.
	 * The measurement object contain method for populating the calculator with values and a method for retrieving the 
	 * measured value.
	 * 
	 * Another usage is to initialize a Measurement class, which offers more
	 * options for setting and getting the marker lines, offset etc.
	 */
	private static Measurement calculator = new Measurement();
	
	/**
	 * Simple singleton. Return the same calculator object.
	 */
	public static ValueMeasurement calculator() {
		return calculator;
	}
	
	public static final int OG_VALUE = 0;
	public static final int OW_VALUE = 1;
	
	public static final int OW_HIGH = 3;
	public static final int OW_LOW = 2;
	public static final int OG_HIGH = 0;
	public static final int OG_LOW = 1;
	
	public static final int NUMBER_OF_THREEPHASE_VALUES = 2;
	public static final int NR_OF_THREEPHASE_MARKERS =4;

	private List<Double> offset;
	private List<Double> markerValue;
	private List<ImageMarkerPoint> markers;
	private	double[] values;
	//As default set to one to not influence calculations unless specified.
	private int imageWidth = 1;
	private int windowWidth  = 1;
	private boolean isMarkersDirty;
	private boolean isValuesDirty;
	
	/**
	 * A measurement object has to be set with data. ImageWidth and windowWidth not set in this method, sine
	 * its not needed in all cases.
	 * 
	 * @param offset - Offset percentage from markers
	 * @param markerValue - Values the markers represent
	 * @param markers - The marker positions
	 * @param values - The values of oil/gas/water seperation
	 */
	public void setCalculator(List<Double> offset, List<Double> markerValue, List<ImageMarkerPoint> markers, double[] values) {
		this.offset = offset;
		this.markerValue = markerValue;
		this.markers = markers;
		this.values = values;
		
	}
	
	/**
	 * In some cases the imageWidth and windowWidth has to be set
	 * @param 
	 * @return
	 */
	public void setGuiValues(int imageWidth, int windowWidth) {
		this.imageWidth = imageWidth;
		this.windowWidth = windowWidth;
	}
	
	/**
	 * Important for getting the actual values of the value markers.
	 * Of special interest for imageDataModel
	 */
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
		if(markers != null && i< markers.size()) {
			return this.markers.get(i).getX();
		}
		return -1;
	}
	
	private double getValuesXPosition(int i) {
		if (values != null &&  i< values.length) {
			return this.values[i];
		}
		return -1;
	}
	
	public void setMarkerXPosition(int i, double x) {
		if (markers != null &&  i< markers.size()) {
			this.markers.get(i).setX(x);
		}
	}
	
	public void setValuesXPosition(int i, double x) {
		if (i< values.length) {
			this.values[i] = x;
		}
	}
	
	/**
	 * The getValue method is used to calculate value of the marker values.
	 * Since the marker values is just x-coordinates in a image, some converting
	 * has to be done before a value can be established.
	 * 
	 * @param markerLow - The lower marker of the range
	 * @param markerHigh - The higher marker of the range.
	 * @param valueType - OG or OW.
	 * @return the actual value of the OG/OW seperation
	 */
	private double getValue(int markerLow, int markerHigh,  int valueType) {
		boolean withOffset = true;
		double lineInPixel = getLinePos(markerHigh, withOffset) - getLinePos(markerLow, withOffset);
		double lineInValue = getLineValue(markerHigh) - getLineValue(markerLow);
		double lineToPos = getValueLinePos(valueType) - getLinePos(markerLow, withOffset);
		return ((lineToPos/lineInPixel)*lineInValue)+ getLineValue(markerLow);
	}

	/**
	 * Depending on the position of the marker in the array , and in the image an offset can be added or subtracted from the value.
	 * 
	 * @param i - What marker to return position of.
	 * @param withOffset If offset should be added.
	 * @return the value of the marker either with offset or not.
	 */
	public double getLinePos(int i, boolean withOffset) {
		if (i<NR_OF_THREEPHASE_MARKERS) {
			if(withOffset) {
				if (i%2== 0) {
					return (( getMarkerXPosition(i) /this.imageWidth)*this.windowWidth)
							+ getOffset(getMarkerXPosition(i), getMarkerXPosition(i+1), this.offset.get(i));
				}
				else {
					return ((getMarkerXPosition(i) /this.imageWidth)*this.windowWidth)
							- getOffset(getMarkerXPosition(i-1),getMarkerXPosition(i), this.offset.get(i));
				}
			}
			else {
				return ((getMarkerXPosition(i)/this.imageWidth)*this.windowWidth);
			}
		}
		return -1;
	}
	
	public void setLinePos(int i, double value, boolean withOffset) {
		if (i<NR_OF_THREEPHASE_MARKERS) {
			isMarkersDirty = true;
			if (withOffset) {
				if (i%2== 0) {
					double offset = getOffset(getMarkerXPosition(i), getMarkerXPosition(i+1), this.offset.get(i));
					setMarkerXPosition(i, ((value / this.windowWidth) * this.imageWidth) - offset);
				}
				else {
					double offset = getOffset(getMarkerXPosition(i-1), getMarkerXPosition(i), this.offset.get(i));
					setMarkerXPosition(i, ((value / this.windowWidth) * this.imageWidth) + offset);
				}
				
			}
			else {
				setMarkerXPosition(i, ((value/this.windowWidth)*this.imageWidth));
			}			
		}
	}
	
	public void setValueLinePos(int i, double value) {
		if(i<NUMBER_OF_THREEPHASE_VALUES) {
			isValuesDirty = true;
			setValuesXPosition(i, ((value/this.windowWidth)*this.imageWidth));
		}
	}
	
	public double getValueLinePos(int i) {
		if(i<NUMBER_OF_THREEPHASE_VALUES) {
			return ((getValuesXPosition(i)/this.imageWidth)*this.windowWidth);
		}
		return -1;
	}

	/**
	 * Marker - where the green dot is placed
	 * Actual marker - Have a value. Typically between 0-20. The percentage is a value describing the percentage
	 * of the range needed to get to the actual marker.
	 *  | . . . . . . . . . |. | Range, and actual marker. 10 percent in this case
	 *  
	 *  The getOffset calculate the actual screen pixel offset of the percentage
	 * @param x1 marker value 1
	 * @param x2 marker value 2
	 * @param percentage percentage of the range
	 * @return
	 */
	private double getOffset(double x1, double x2, double percentage) {
		return ((((x2 - x1)*percentage)/this.imageWidth)*this.windowWidth);
	}

	/**
	 * What  value the marker line represent.
	 * Typically between 0-20. Each marker line has such a value.
	 * @param i
	 * @return
	 */

	private double getLineValue(int i) {
		return markerValue.get(i);
	}
	
	
	/**
	 * Returns a new percentage for the offset
	 * 
	 * @param selected -Offset line that is selected by user
	 * @param pos - The new position of the offset
	 * @return
	 */
	public double getNewPercentage(int selected, int pos) {
		double x1;
		double x2;
		int nr = NR_OF_THREEPHASE_MARKERS;
		if(selected%2 == 0) {
			x1 = getLinePos(selected-nr, false);
			x2 = getLinePos(selected-nr +1, false);
			//legge inn sperringer om pos er mindre enn 0 eller større enn andre siden av menisk
			return Math.abs((pos-x1)/(x1-x2));
		}
		else {
			x1 = getLinePos(selected-nr, false);
			x2 = getLinePos(selected-nr-1, false);
			return Math.abs((pos-x1)/(x1-x2));
			
		}	
	}
	
	/**
	 * If any setters has been used, the Measurement is set to dirty.
	 * It has changed since initialization.
	 */
	public boolean isDirty() {
		return isValuesDirty || isMarkersDirty;
	}
	// END CALCULATING METHODS FOR MEASUREMENT VALUE
}
