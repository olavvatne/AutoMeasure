package automeasurer;

import java.util.prefs.Preferences;

import view.settingsPanel.Setting;

//TODO: DOCUMENTATION
public class ConfigurationManager {
	public static final String DATE_COLUMN = "datecolumn";
	public static final String TIME_COLUMN = "time";
	public static final String VALUES_THREEPHASE = "valThree";
	public static final String DATE_REGEX = "dateRegex";
	public static final String TIME_REGEX = "timeRegex";
	public static final String MIN_HUE = "Min hue";
	public static final String MIN_BRI = "Min bri";
	public static final String MIN_SAT = "Min sat";
	
	public static final String MAX_HUE = "Max hue";
	public static final String MAX_BRI = "Max bri";
	public static final String MAX_SAT = "Max sat";
	
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
