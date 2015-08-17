package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MarkerValue {
private static List<IntervalModel<Double>> markerValues = init();
	
//TODO: MARKERVALUE AND OFFSET VERY SIMILAR. REFACTOR??
	/**
	 * Init for the static global variable.
	 * It should retrieve default values from data store and put them into the IntervalModel
	 * and set the start and end row for the IntervalModel to 0 and maxInt.
	 * 
	 * TODO: Create data store for default values
	 * 
	 * @return A linked list of IntervalModels
	 */
	public static List<IntervalModel<Double>> init() {
		//get some default values from store or something
		List<IntervalModel<Double>> intervalList = new LinkedList<IntervalModel<Double>>();
		List<Double> values = new ArrayList<Double>();
			values.add(18.0);
			values.add(1.0);
			values.add(1.0);
			values.add(18.0);
		IntervalModel<Double> initialMarkerValue = new IntervalModel<Double>(0, values, Integer.MAX_VALUE);
		intervalList.add(initialMarkerValue);
		System.out.println("tester");
		return intervalList;
	}
	
	
	/**
	 * The imageDataModel object has to query this method to access the offset for that row.
	 * The getMarkerValues method finds the current values from the static linked list of IntervalModels
	 * @param Row the row of interest
	 * @return A list of double values corresponding to the row
	 */
	public static List<Double> getMarkerValues(int row) {
		//could probably use hashmap to more efficently find offsets but for now, good enough
		//TODO: do it using hashmaps
		System.out.println(markerValues);
		System.out.println("I MARKER");
		for (int i = 0; i<markerValues.size(); i++) {
			if(row >= markerValues.get(i).getStartRow() && row <= markerValues.get(i).getEndRow()) {
				return markerValues.get(i).getValues();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param fromRow From where the new values should be current and valid
	 * @param newValues The new double values.
	 * @param replaceAllValuesToEnd If the method should replace all intervalModels to the end of the linked list.
	 */
	public static void changeMarkerValues(int fromRow, List<Double> newValues, boolean replaceAllValuesToEnd) {
		int nr = 0;
		for (int i = 0; i<markerValues.size(); i++) {
			if(fromRow >= markerValues.get(i).getStartRow() && fromRow <= markerValues.get(i).getEndRow()) {
				nr = i;
				break;
			}
		}
		//TODO:Endrow behavior
		//If fromRow is 0 the inital row has to be replaced.
		if(fromRow == markerValues.get(nr).getStartRow()) {
			IntervalModel<Double> newInterval = new IntervalModel<Double>(fromRow, newValues, markerValues.get(nr).getEndRow());
			((LinkedList<IntervalModel<Double>>)markerValues).set(nr, newInterval);
		}
		else {
			IntervalModel<Double> newInterval = new IntervalModel<Double>(fromRow, newValues, markerValues.get(nr).getEndRow());
			markerValues.get(nr).setEndRow(fromRow-1);
			((LinkedList<IntervalModel<Double>>)markerValues).addLast(newInterval);
		}
		//if fromRow is bigger than one it can be partitioned
		
	}
}
