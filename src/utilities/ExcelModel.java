package utilities;

/**
 * An interface that data models has to implement to be 
 * writable to an excel file.
 * 
 * @author Olav
 *
 */
public interface ExcelModel {
	public String[] getRowAsStringRow();
}
