package utilities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import view.settingsPanel.Setting;
import automeasurer.ConfigurationManager;
import automeasurer.Measurer;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
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
	//settings maybe
	private int dateColumn = ConfigurationManager.getManager().getInt(Setting.DATE_COLUMN);
	private int timeColumn = ConfigurationManager.getManager().getInt(Setting.TIME_COLUMN);
	private String dateRegex = ConfigurationManager.getManager().get(Setting.DATE_REGEX);
	private String timeRegex = ConfigurationManager.getManager().get(Setting.TIME_REGEX);
	private DateTimeFormatter formatter = DateTimeFormat.forPattern(dateRegex + " " + timeRegex);
	//If file contains dates, matching between image and excel date can be performed
	boolean fileContainDates = false;
	private PropertyChangeSupport pcs;
	
	/**
	 * A new file has to be created, and inserted when initializing an ExcelWriter.
	 * 
	 * TODO: NO date placement yet
	 * TODO: throw expections, and not handle here. Should be handled by view. Show text or something.
	 * @param excelFileWithDates 
	 */
	public ExcelWriter(File file, boolean newFile) {
		pcs = new PropertyChangeSupport(this);
		if(newFile) {
			try {
				workbook =Workbook.createWorkbook(file);
				sheet = workbook.createSheet("data", FIRST_EXCEL_SHEET);
				
				//sheet = workbook.getSheet(FIRST_EXCEL_SHEET);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		else {
			fileContainDates = true;
			File copyFile = findSavePath(file, newFile);
			try {
				Workbook originalWorkbook =Workbook.getWorkbook(file);
				workbook = Workbook.createWorkbook(copyFile, originalWorkbook);
				sheet = workbook.getSheet(FIRST_EXCEL_SHEET);
				System.out.println(sheet.getCell(0, 0).getContents());
			} catch (BiffException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Write the List of data to an excel file. Data written is dependent on the 
	 * implementation of the ExcelModel in each data model.
	 * @param List of objects implementing ExcelModel interface
	 * 
	 */
	public void writeExcelFile(List<ExcelModel> data) {
		if(this.fileContainDates) {
			writeToFileWithDates(data);
		}
		else {
			writeToFileWithNoDates(data);
		}
		closeAndWriteExcel();
	}
	
	/**
	 * Opening a already existing excel file with dates, will require matching
	 * of dates. The excelfile will often contain a huge amount of dates, and there is more often than not
	 * few image measurements. The excel file therefore has to be read sequentially to map excel date to measurements.
	 * 
	 * NOTE: THe method assumes that the excelModel list is sorted in chronological order. 
	 * 
	 * First Excel date and first measurement:
		 * Measurement is equal to excel date:
		 	- Record measurement at that row
		 * Measurement happened before excel date:
		 *  - No recource. All later dates will have same outcome.
		 * Measurement happened after excel date:
		 * 	- Go to next excel date
	 * 
	 * Second Excel date and a measurement:
	 *  Measurement is equal to excel date:
	 *    - Record measurement at that row
	 * 	Measurement happened before excel date:
	 *   - If measurement happened after previous excel date, record inbetween
	 *  Measurement happened after excel date:
	 *  - Go to next excel date.
	 * @param data List of objects implementing ExcelModel interface.
	 */
	private void writeToFileWithDates(List<ExcelModel> data) {
		int excelIndex = 0;
		int nrNotRecorded = 0;
		DateTime prevExcelDate = null;
		int excelLength = sheet.getRows();
		
		for(ExcelModel measurement: data) {
			DateTime measurementDate = measurement.getDate();
			boolean recorded = false;

			pcs.firePropertyChange(Measurer.SETMAX, null, new Integer(excelLength));
			for(int i= excelIndex; i<excelLength; i++ ) {
				DateTime excelDate = getDate(i);
				
				if(i % 50 == 0) {
					pcs.firePropertyChange(Measurer.PROGRESS_UPDATE, null, new Integer(i));
				}
				
				//System.out.println("-----------------------");
				//System.out.println("Measurement " + measurementDate.toString(dateRegex + " " + timeRegex));
				if (excelDate != null) {
					//System.out.println("Excel " + excelDate.toString(dateRegex + " " + timeRegex));
				
					if(measurementDate.isEqual(excelDate)) {
						System.out.println("EQUAL");
						
						//Method for date added, but also for values only, to avoid magic numbers
						setValueCells(measurement.getRowAsStringRow(), i);
						excelIndex = i;
						recorded = true;
						break;
					}
					else {
						if (prevExcelDate != null) {
							//Does nothing to gaps in excel without dates where measurement falls between exceldate on either side of gap.
							//there has happened a gap, and no no equal date found. Place value somewhere within.
							if(measurementDate.isAfter(prevExcelDate) && measurementDate.isBefore(excelDate)) {
								setValueCells(measurement.getRowAsStringRow(), i);
								excelIndex = i;
								recorded = true;
								break;									
							}
						}
					}
				}
				prevExcelDate = excelDate;
			}
			if(!recorded) {
				nrNotRecorded += 1;
			}
		}
		if (nrNotRecorded>0) {
			pcs.firePropertyChange(Measurer.NOT_RECORDED, null, new Integer(nrNotRecorded));
		}
		pcs.firePropertyChange(Measurer.FINISHED, null, null);
	}
	
	private void setValueCells(String[] values, int row) {
		for(int column = 2; column<values.length; column++) {
			this.setCell(column, row,  values[column]);
		}
	}
	
	private DateTime getDate(int row) {
		String date = sheet.getWritableCell(dateColumn, row).getContents().trim();
		String time = sheet.getWritableCell(timeColumn, row).getContents().trim();
		String datetime = date + " " + time;
		
		try {
			DateTime result = formatter.parseDateTime(datetime);
			return result.minuteOfDay().roundFloorCopy();
			//return new SimpleDateFormat(this.dateRegex).parse(datetime);
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
	}
	/**
	 * If Excelwriter has created a new xls file there is no dates present in the row A and B.
	 * Matching image dates and excel dates is not necessary. Date and value is written
	 * sequentially below each other in the excel file.
	 * @param data List of objects implementing ExcelModel interface
	 */
	private void writeToFileWithNoDates(List<ExcelModel> data) {
		pcs.firePropertyChange(Measurer.SETMAX, null, new Integer(data.size()));
		for(int row = 0; row<data.size(); row++) {
			String[] values = data.get(row).getRowAsStringRow();
			//TODO: CONFIG WHAT ROWS TO WRITE TO
			for(int column = 0; column<values.length; column++) {
				this.setCell(column, row,  values[column]);
			}
			
			if(row % 30 == 0) {
				pcs.firePropertyChange(Measurer.PROGRESS_UPDATE, null, new Integer(row));
			}
		}
		
		pcs.firePropertyChange(Measurer.FINISHED, null, null);
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
	
	private static File findSavePath(File excelFile, boolean newFile) {
		String copyString = "- copy";
		String postfix = ".xls";
		
		if(newFile) {
			copyString = "";
			postfix = "";
		}
		int initialFileLength = excelFile.getName().length();
		int copyInt = 0;
		File copyFile;
		do {
			copyInt ++;
			copyFile = new File(excelFile.getParentFile().getAbsolutePath() + "\\"+
					excelFile.getName().substring(0, initialFileLength) + copyString+ copyInt+ ".xls");
		} while(copyFile.exists());

		return copyFile;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
}
