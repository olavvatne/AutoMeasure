package utilities;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelWriter {
	public static int DATE_COLUMN = 0;
	public static int TIME_COLUMN = 1;
	public static int OG_COLUMN = 2;
	public static int WO_COLUMN =3;
	
	private static int FIRST_EXCEL_SHEET = 0;
	
	WritableSheet sheet = null;
	WritableWorkbook workbook = null;
	
	/**
	 * A new file has to be created, and inserted when initializing an ExcelWriter.
	 * 
	 * NOTE: NO date placement yet
	 * @param excelFileWithDates 
	 */
	public ExcelWriter(File file) {
		try {
			workbook =Workbook.createWorkbook(file);
			sheet = workbook.getSheet(FIRST_EXCEL_SHEET);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	/**
	 * Write the List of data to an excel file. Data written is dependent on the 
	 * implementation of the ExcelModel in each data model.
	 * @param List of ojects implementing ExcelModel interface
	 * 
	 */
	public void writeExcelFile(List<ExcelModel> data) {
		
		for(int row = 0; row<data.size(); row++) {
			String[] values = data.get(row).getRowAsStringRow();
			for(int column = 0; column<values.length; column++) {
				this.setCell(row, column, values[column]);
			}
		}
		
	}
	
	/**
	 * Method should be called after finishing writing to the excel sheet.
	 * Workbook is also closed, like regular IO operations
	 */
	public void closeAndWriteExcel() {
		try {
			workbook.write();
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a value into a cell (row, column) in the excel sheet.
	 * @param column in the sheet
	 * @param row in the sheet
	 * @param value to put into the cell
	 * @return a boolean telling if the operation where successful
	 */
	private boolean setCell(int column, int row, String value) {
		Label cell = new Label(column, row, value); 

		try {
			sheet.addCell(cell);
		} catch (RowsExceededException e) {
			e.printStackTrace();
			return false;
		} catch (WriteException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
}
