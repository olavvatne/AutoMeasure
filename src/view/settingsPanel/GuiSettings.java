package view.settingsPanel;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GuiSettings extends BaseSettings implements ChangeListener {
	JCheckBox showDates;
	public GuiSettings() {
		super();
		initComponents();
	}
	
	
	private void initComponents() {
		JLabel label = new JLabel("Show dates in panel");
		this.add(label);
		showDates = new JCheckBox();
		showDates.addChangeListener(this);
		showDates.setSelected(config.getBoolean(Setting.SHOW_DATES));
		this.add(showDates);
	}


	@Override
	protected void saveChanges() {
		config.put(Setting.SHOW_DATES, showDates.isSelected());
		
	}


	@Override
	public void stateChanged(ChangeEvent e) {
		
	}
}
