package view.settingsPanel;

import javax.swing.JLabel;

public class GuiSettings extends BaseSettings {
	
	public GuiSettings() {
		super();
		initComponents();
	}
	
	
	private void initComponents() {
		JLabel label = new JLabel("GUI");
		this.add(label);
	}


	@Override
	protected void saveChanges() {
		// TODO Auto-generated method stub
		
	}
}
