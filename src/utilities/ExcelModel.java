package utilities;

import java.util.Date;

/**
 * An interface that data models has to implement to be 
 * writable to an excel file.
 * 
 * @author Olav
 *
 */
public interface ExcelModel {
	public String[] getRowAsStringRow();
	public Date getDate();
}
