package view.settingsPanel;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import automeasurer.ConfigurationManager;
import utilities.ExcelWriter;

/**
 * A jpanel containing gui controls for chaning settings regarding
 * the excel writer. 
 * 
 * -Where to read date from
 * -Where to read time from
 * -What date regex to use when writing to file
 * -What time regex to use when wrinting to file
 * @author Olav
 *
 */
public class WriterSettings extends BaseSettings {
	private JSpinner dateCol;
	private JSpinner timeCol;
	private JTextField dateRegex;
	private JTextField timeRegex;
	
	public WriterSettings() {
		super();
		initComponents();
	}
	
	/**
	 * Initialize components and put them into the jpanel.
	 */
	private void initComponents() {
		JLabel label = new JLabel("Date column");
		this.add(label);
		dateCol = new JSpinner();
		dateCol.setValue(config.getInt(Setting.DATE_COLUMN));
		this.add(dateCol);
		
		label = new JLabel("Time column");
		this.add(label);
		timeCol = new JSpinner();
		timeCol.setValue(config.getInt(Setting.TIME_COLUMN));
		this.add(timeCol);
		
		label = new JLabel("Date regex");
		this.add(label);
		dateRegex = new JTextField(config.get(Setting.DATE_REGEX));
		this.add(dateRegex);
		
		label = new JLabel("Time regex");
		this.add(label);
		timeRegex = new JTextField(config.get(Setting.TIME_REGEX));
		this.add(timeRegex);
	}

	
	/**
	 * This class extends BaseSettings and should implement the abstract
	 * saveChanges(). In this case it takes all the controls model values,
	 * and save them into the configuration manager.
	 */
	@Override
	protected void saveChanges() {
		System.out.println((Integer)dateCol.getValue());
		this.config.put(Setting.DATE_COLUMN, (Integer)dateCol.getValue());
		this.config.put(Setting.TIME_COLUMN, (Integer)timeCol.getValue());
		this.config.put(Setting.DATE_REGEX, dateRegex.getText());
		this.config.put(Setting.TIME_REGEX, timeRegex.getText());
	}
}
