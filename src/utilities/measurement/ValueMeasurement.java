package utilities.measurement;

import java.util.List;

import analyze.ImageMarkerPoint;

/**
 * ValueMeasurement abstract away all potentially public methods that
 * some other classes use. When writing to an excel file only the 
 * setCalculator and getThreePhaseValue is of interest.
 * The static method of measurement return a ValueMeasurement for example,
 * and threephasepanel does calculations by initializing a new Measurement.
 * Different needs, different methods.
 * 
 * @author Olav
 *
 */
public interface ValueMeasurement {
	public double getThreePhaseValue(int i);
	public void setCalculator(List<Double> offset,
							  List<Double> markerValue,
							  List<ImageMarkerPoint> markers,
							  double[] values);
}
