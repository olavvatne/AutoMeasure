package view.settingsPanel;

import javax.swing.JLabel;
import javax.swing.JSpinner;

import automeasurer.ConfigurationManager;
import utilities.ExcelWriter;

/**
 * A jpanel containing the settings related to conducting measurements.
 * The textfields - and - should contain the values the physical markers represent.
 * FIX description
 */
public class MeasurementSettings extends BaseSettings {
	private JSpinner minMarkerValue;
	private JSpinner maxMarkerValue;
	
	public MeasurementSettings() {
		super();
		initComponents();
	}
	
	/**
	 * Initialize components and put them into the jpanel.
	 */
	private void initComponents() {
		JLabel label = new JLabel("lower marking");
		this.add(label);
		minMarkerValue = new JSpinner();
		minMarkerValue.setValue(config.getInt(Setting.LOWER_MARKER));
		this.add(minMarkerValue);
		
		label = new JLabel("upper marking");
		this.add(label);
		maxMarkerValue = new JSpinner();
		maxMarkerValue.setValue(config.getInt(Setting.UPPER_MARKER));
		this.add(maxMarkerValue);
	}

	
	/**
	 * This class extends BaseSettings and should implement the abstract
	 * saveChanges(). In this case it takes all the controls model values,
	 * and save them into the configuration manager.
	 */
	@Override
	protected void saveChanges() {
		this.config.put(Setting.LOWER_MARKER, (Integer)minMarkerValue.getValue());
		this.config.put(Setting.UPPER_MARKER, (Integer)(Integer)maxMarkerValue.getValue());

	}
}
