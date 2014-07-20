package view.settingsPanel;

import javax.swing.JPanel;

import automeasurer.ConfigurationManager;

/**
 * A common parent for all settings panels. Contain a
 * reference to the configuration manager, and 
 * a dirty flag that can be used to avoid setting values that
 * has not changed.
 * @author Olav
 *
 */
public abstract class BaseSettings extends JPanel {
	protected ConfigurationManager config = new ConfigurationManager();
	
	//TODO: utilize dirt flag, instead of saving everything
	private boolean dirty = true;
	
	/**
	 * When the user wants to save his/her new settings this
	 * method must be called, if the panels are dirty, the
	 * abstract saveChanges method is called, which has to be impl.
	 * by subclasses
	 */
	public void save() {
		if (dirty) {
			saveChanges();
		}
		
	}
	
	/**
	 * Abstract methods all sub setting panels has to implement.
	 * How the panel wants to save its options.
	 */
	protected abstract void saveChanges();
}
