package view.settingsPanel;

import javax.swing.JLabel;

public class WriterSettings extends BaseSettings {
	
	public WriterSettings() {
		super();
		initComponents();
	}
	
	
	private void initComponents() {
		JLabel label = new JLabel("WRITER");
		this.add(label);
	}


	@Override
	protected void saveChanges() {
		// TODO Auto-generated method stub
		
	}
}
