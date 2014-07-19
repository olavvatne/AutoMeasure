package view.settingsPanel;

import javax.swing.JPanel;

import automeasurer.ConfigurationManager;

public abstract class BaseSettings extends JPanel {
	protected ConfigurationManager config = new ConfigurationManager();
	
	//TODO: utilize dirt flag, instead of saving everything
	private boolean dirty = true;
	
	public void save() {
		if (dirty) {
			saveChanges();
		}
		
	}
	
	protected abstract void saveChanges();
}
