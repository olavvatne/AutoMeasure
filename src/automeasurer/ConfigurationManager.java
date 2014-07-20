package automeasurer;

import java.util.prefs.Preferences;

import view.settingsPanel.Setting;

//TODO: DOCUMENTATION
public class ConfigurationManager {
	public static final String VALUES_THREEPHASE = "valThree";
	
	public static ConfigurationManager getManager() {
		return new ConfigurationManager();
	}
	
	private Preferences config;
	
	public ConfigurationManager() {
		config = Preferences.userRoot().userNodeForPackage(ConfigurationManager.class);
	}
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
	
	public String get(Setting key) {
		return config.get(key.name(), key.defaultString());
	}
	
	public void reset(Setting key) {
		config.remove(key.name());
	}
	
}
