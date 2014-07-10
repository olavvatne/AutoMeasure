package model;

import java.util.List;

public class IntervalModel<T> {
	
	private int startRow;
	private int endRow;
	private final List<T> values;
	
	/**
	 * The intervalModel is a model that represent a range where values list is the only valid values between, startRow and endRow.
	 * The List cannot be changed after the object initialization, only the startRow and endRow can change, which means that
	 * the you can only change the span where the list of values is valid.
	 * 
	 * @param startRow The row where the values is valid
	 * @param values The actual objects containing the valid values
	 * @param endRow The row where the values stop being valid.
	 */
	public IntervalModel(int startRow, List<T> values, int endRow) {
		this.startRow = startRow;
		this.endRow = endRow;
		this.values = values;
	}

	public int getStartRow() {
		return startRow;
	}
	
	public void changeInterval(int start, int end) {
		setStartRow(start);
		setEndRow(end);
	}
	
	private void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	private void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public List<T> getValues() {
		return values;
	}
}
