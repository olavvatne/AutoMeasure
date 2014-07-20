package automeasurer;

import java.util.prefs.Preferences;

import view.settingsPanel.Setting;

/**
 * The configuration manager persist settings about the application
 * between sessions. All settings has a default value found in 
 * the enum in case the Preferences cant be found. 
 * @author Olav
 *
 */
public class ConfigurationManager {
	public static final String VALUES_THREEPHASE = "valThree";
	
	//TODO: Make single object, and register listeners.
	public static ConfigurationManager getManager() {
		return new ConfigurationManager();
	}
	
	private Preferences config;
	
	public ConfigurationManager() {
		config = Preferences.userRoot().userNodeForPackage(ConfigurationManager.class);
	}
	
	/**
	 * Persist a String value for the enum name.
	 * @param key enum for that setting
	 * @param value String value to persist
	 */
	public void put(Setting key, double value) {
		config.putDouble(key.name(), value);
	}
	
	public void put(Setting key, int value) {
		config.putInt(key.name(), value);
	}
	
	public void put(Setting key, String value) {
		config.put(key.name(), value);
	}
	
	public void put(Setting key, boolean value) {
		config.putBoolean(key.name(), value);
	}
	
	public double getDouble(Setting key) {
		return config.getDouble(key.name(), key.defaultDouble());
	}
	
	public boolean getBoolean(Setting key) {
		return config.getBoolean(key.name(), key.defaultBoolean());
	}
	
	public int getInt(Setting key) {
		return config.getInt(key.name(), key.defaultInt());
	}
	
	/**
	 * Getter for a setting enum. Tries to retrieve the stored
	 * value. If no is found a default value is returned instead.
	 * @param key
	 * @return
	 */
	public String get(Setting key) {
		return config.get(key.name(), key.defaultString());
	}
	
	/**
	 * If a persisted value should be removed the reset 
	 * method can be called.
	 * @param key A Setting enum.
	 */
	public void reset(Setting key) {
		config.remove(key.name());
	}
	
}
