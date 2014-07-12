package analyze;

import java.util.List;

/**
 * The fault detector's job is to ensure that image that is incorrectly measured by 
 * the analyzer is not accepted. There are two methods check the validity of points.
 * Different attributes like y variance, number of points is taken into account when testing points.
 * @author Olav
 *
 */
public class FaultDetector {
	
	private String errorMessage;
	//TODO: NOT USED
	private boolean correct;
	
	
	public String getErrorMessage() {
		if (errorMessage == null) {
			return "No errors";
		}
		else {
			return errorMessage;
		}
	}
	
	/**
	 * isMarkerCorrect return true if the markers found is most likely correct and
	 * false if their most likely false. 
	 * Tests for  the found markers are as follows:
	 * 	-Marker list is not null
	 * 	-There are exactly 4 markers found (TODO: Develop a blob filter that removes small blobs.)
	 *  -The vertical variance is not too large
	 * 	
	 * @param markers Markers found by the analyzer
	 * @return Whether the markers are correct or something about them are fishy.
	 */
	public boolean isMarkersCorrect(List<ImageMarkerPoint> markers) {
		errorMessage = null;
		correct = true;
		
		if ( markers == null) {
			errorMessage = "No markers found";
			return !correct;
		}
		
		//TODO: Config for how many markers there should be! Settings etc. TEMP set to 4 markers
		if(markers.size() != 4) {
			errorMessage = markers.size() + " markers were found. Should be 4 exactly";
			return !correct;
		}
		
		int verticalVariance = calcVerticalVariance(markers);
		//TODO: Parameter in settings for variance allowed
		if (verticalVariance > 200) {
			errorMessage = "The points are not align along the y axis. The variance is " + verticalVariance;
			return !correct;
		}
		
		return correct;
	}
	
	
	public boolean isValuesCorrect(List<Double> values) {
		errorMessage = null;
		correct = true;
		
		if ( values == null) {
			errorMessage = "No markers found";
			return !correct;
		}
		
		//TODO: Config for how many markers there should be! Settings etc. TEMP set to 2 values
		if(values.size() != 2) {
			errorMessage = values.size() + " values were found. Should be 4 exactly";
			return !correct;
		}
		
		return correct;
	}
	
	private int calcVerticalVariance(List<ImageMarkerPoint> points) {
		double lowestY = Integer.MAX_VALUE;
		double highestY = 0;
		for(ImageMarkerPoint point: points) {
			if(point.getY()> highestY) {
				highestY = point.getY();
			}
			if(point.getY() < lowestY) {
				lowestY = point.getY();
			}
		}
		
		return Math.abs((int)(highestY - lowestY));
	}
}
