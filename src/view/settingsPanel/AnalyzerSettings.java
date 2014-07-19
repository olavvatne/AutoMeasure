package view.settingsPanel;

import javax.swing.JLabel;

public class AnalyzerSettings extends BaseSettings {
	
	public AnalyzerSettings() {
		super();
		initComponents();
	}
	
	
	private void initComponents() {
		JLabel label = new JLabel("ANALYZER");
		this.add(label);
	}


	@Override
	protected void saveChanges() {
		// TODO Auto-generated method stub
		
	}
}
