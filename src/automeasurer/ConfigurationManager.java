package automeasurer;

import java.util.prefs.Preferences;

import view.settingsPanel.Setting;

//TODO: DOCUMENTATION
public class ConfigurationManager {
	public static final String VALUES_THREEPHASE = "valThree";
	
	public static ConfigurationManager getManager() {
		return new ConfigurationManager();
	}
	
	private Preferences config = null;
	
	public void put(Setting key, double value) {
		if(config == null) {
			loadPreference();
		}
		config.putDouble(key.name(), value);
	}
	
	public void put(Setting key, int value) {
		if(config == null) {
			loadPreference();
		}
		config.putInt(key.name(), value);
	}
	
	public void put(Setting key, String value) {
		if(config == null) {
			loadPreference();
		}
		config.put(key.name(), value);
	}
	
	public double getDouble(Setting key) {
		if(config == null) {
			loadPreference();
		}
		return config.getDouble(key.name(), key.defaultDouble());
	}
	
	public int getInt(Setting key) {
		if(config == null) {
			loadPreference();
		}
		return config.getInt(key.name(), key.defaultInt());
	}
	
	public String get(Setting key) {
		if(config == null) {
			loadPreference();
		}
		return config.get(key.name(), key.defaultString());
	}
	
	public void reset(Setting key) {
		if(config==null) {
			loadPreference();
		}
		
	}
	
	private void loadPreference() {
		config = Preferences.userRoot().userNodeForPackage(ConfigurationManager.class);
	}
}
