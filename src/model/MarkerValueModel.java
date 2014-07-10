package model;

import java.util.ArrayList;
import java.util.List;

public class MarkerValueModel {
	private static MarkerValueModel defaultValues;
	private final List<Double> values;
	
	/**
	 * Constructor specially for threephase
	 * 
	 * owh = Oil/water high -- the bigger value
	 * owl = Oil/water low -- the smaller value
	 * ogh = Oil/gas high
	 * ogl = Oil/gas low
	 */
	public MarkerValueModel(double ogh, double ogl, double owl, double owh) {
		this.values = new ArrayList<Double>();
		this.values.add(owh);
		this.values.add(owl);
		this.values.add(ogh);
		this.values.add(ogl);
	}
	
	/**
	 * General constructor
	 * @param values list with double values. What each marker represent
	 */
	public MarkerValueModel(List<Double> values) {
		this.values = values;
	}
	
	public double getvalue(int i) {
		return this.values.get(i);
	}
	
	/**
	 * Default static method for retrieving a object reference to the default marker values.
	 * Typically all images has the same markerValueModel, and 
	 * @return
	 */
	public static MarkerValueModel getDefaultMarkerValues() {
		if(defaultValues == null) {
			defaultValues = new MarkerValueModel(18, 1, 18, 1);
		}
		return defaultValues;
	}
	
	/**
	 * If a image changes its valueModel, 
	 * @return
	 */
	public static MarkerValueModel createNewMarkerValues(List<Double> values) {
		return null;
	}
}
