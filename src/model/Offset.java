package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Offset {
	
	private static List<IntervalModel<Double>> offset = init();
	
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
		offset = new LinkedList<IntervalModel<Double>>();
		List<Double> values = new ArrayList<Double>();
			values.add(0.10);
			values.add(0.10);
			values.add(0.10);
			values.add(0.10);
		IntervalModel<Double> initialOffset = new IntervalModel<Double>(0, values, Integer.MAX_VALUE);
		offset.add(initialOffset);
		return null;
	}
	
	
	/**
	 * The imageDataModel object has to query this method to access the offset for that row.
	 * The getoffset method finds the current values from the static linked list of IntervalModels
	 * @param Row the row of interest
	 * @return A list of double values corresponding to the row
	 */
	public static List<Double> getOffset(int row) {
		//could probably use hashmap to more efficently find offsets but for now, good enough
		//TODO: do it using hashmaps
		for (int i = 0; i<offset.size(); i++) {
			if(row >= offset.get(i).getStartRow() && row<=offset.get(i).getEndRow()) {
				return offset.get(i).getValues();
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
	public static void changeOffset(int fromRow, List<Double> newValues, boolean replaceAllValuesToEnd) {
		
	}
}
