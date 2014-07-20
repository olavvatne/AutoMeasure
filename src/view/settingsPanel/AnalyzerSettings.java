package view.settingsPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import automeasurer.ConfigurationManager;

/**
 * Settings panel for options regarding the image analyzers
 * For example setting the keying colors, through JSliders
 * slider for hue brightness and saturation.
 * @author Olav
 *
 */
public class AnalyzerSettings extends BaseSettings implements ChangeListener, ActionListener {
	private JSlider[] colorPicker1 = new JSlider[3];
	private JSlider[] colorPicker2 = new JSlider[3];
	private Setting[] colorConfig1 = new Setting[]{Setting.MIN_HUE,
											Setting.MIN_SAT,
											Setting.MIN_BRI};
	
	private Setting[] colorConfig2 = new Setting[]{
			Setting.MAX_HUE,
			Setting.MAX_SAT,
			Setting.MAX_BRI};
	
	private JPanel minColor;
	private JPanel maxColor;
	
	public AnalyzerSettings() {
		super();
		initComponents();
	}
	
	/**
	 * Initialize and put all the components into the Jpanel.
	 * Currently 6 sliders for hue brightness and saturation for 
	 * both min and max range. Used when finding the markers
	 * in the image.
	 */
	private void initComponents() {
		JLabel label = new JLabel("Min");
		this.add(label);
		
		for (int i = 0; i<colorPicker1.length; i++) {
			label = new JLabel(colorConfig1[i].text());
			this.add(label);
			colorPicker1[i] = new JSlider(0, 255, config.getInt(colorConfig1[i]));
			colorPicker1[i].setPaintLabels(true);
			colorPicker1[i].setMajorTickSpacing(255);
			colorPicker1[i].addChangeListener(this);
			this.add(colorPicker1[i]);
		}
		
		minColor = new JPanel();
		minColor.setSize(40,40);
		setColor(minColor, colorPicker1);
		this.add(minColor);
		
		label = new JLabel("Max");
		this.add(label);
		
		for (int i = 0; i<colorPicker2.length; i++) {
			label = new JLabel(colorConfig2[i].text());
			this.add(label);
			colorPicker2[i] = new JSlider(0, 255, config.getInt(colorConfig2[i]));
			colorPicker2[i].setPaintLabels(true);
			colorPicker2[i].setMajorTickSpacing(255);
			colorPicker2[i].addChangeListener(this);
			this.add(colorPicker2[i]);
		}
		
		maxColor = new JPanel();
		maxColor.setSize(40,40);
		setColor(maxColor, colorPicker2);
		this.add(maxColor);
		
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(this);
		this.add(resetButton);
	}
	/**
	 * A label is put into the panel to show what color the
	 * combination of sliders create. The setColor will
	 * set a new color to this label, by retrieving the values
	 * from the sliders and dividing them by 255, to aquire a
	 * range from 0 -1
	 * @param label The label to colorize
	 * @param hsb - The JSliders that represent the hsb color.
	 */
	private void setColor(JPanel label, JSlider[] hsb) {
		label.setBackground(
				Color.getHSBColor(
						(float)hsb[0].getValue()/255,
						(float)hsb[1].getValue()/255,
						(float)hsb[2].getValue()/255)
			);
	}
	
	/**
	 * Savechanges impl of how Jpanel should save its content.
	 */
	@Override
	protected void saveChanges() {
		saveColorPicker(colorPicker1, colorConfig1);
		saveColorPicker(colorPicker2, colorConfig2);
		
	}
	
	/**
	 * Each colorpicker contain three values (3 JSliders).
	 * These values has to be stored in the configuration manager
	 * by calling the put method for each JSlider.
	 * @param colorPicker The hsb JSlider
	 * @param hsb the hsb enums.
	 */
	private void saveColorPicker(JSlider[] colorPicker, Setting[] hsb) {
		for(int i = 0; i<colorPicker.length; i++) {
			config.put(hsb[i], colorPicker[i].getValue());
		}
	}

	/**
	 * Whenever a JSlider is moved this method is called, since
	 * this class is added as a changeListener.
	 * Will call setColor to update the label color that represent
	 * the hsb color.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		setColor(minColor, colorPicker1);
		setColor(maxColor, colorPicker2);
	}
	
	/**
	 * Since its hard to remember the default values of 6
	 * JSliders a reset button can be used to remove the stored
	 * data about hsb color data in the configuration manager.
	 * @param hsb
	 * @param picker
	 */
	private void resetColorPicker(Setting[] hsb, JSlider[] picker) {
		for(int i = 0; i<hsb.length; i++) {
			config.reset(hsb[i]);
			picker[i].setValue(config.getInt(hsb[i]));
		}
	}
	
	/**
	 * If reset button is clicked, the resetColorPicker method
	 * is called for both color-pickers.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		resetColorPicker(colorConfig1, colorPicker1);
		resetColorPicker(colorConfig2, colorPicker2);

	}
}
