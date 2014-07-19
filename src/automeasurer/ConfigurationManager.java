package automeasurer;

import java.util.prefs.Preferences;


public class ConfigurationManager {
	private Preferences config = null;
	
	public void put(String key, double value) {
		if(config == null) {
			loadPreference();
		}
		config.putDouble(key, value);
	}
	
	public void get(String key) {
		if(config == null) {
			loadPreference();
		}
		config.getDouble(key, 0);
	}
	
	private void loadPreference() {
		config = Preferences.userRoot().userNodeForPackage(ConfigurationManager.class);
	}
}
