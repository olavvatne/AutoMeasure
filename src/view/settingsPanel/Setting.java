package view.settingsPanel;

import java.awt.Color;

/**
 * Enum for retrieving data from the configuration manager. 
 * Each enum has a default data attached to it, of the type
 * object. It is the callers responsibiliy to know what type
 * it should be, int boolean or String.
 * In addition to the default value, a text string is assosiated
 * with a enum. Could be used in a JLabel etc.
 * @author Olav
 *
 */
public enum Setting {
	MIN_BRI (new Integer(21), "Minimum brightness"),
	MIN_SAT (new Integer(77), "Minimum saturation"),
	MIN_HUE (new Integer(65), "Minimum hue"),
	MAX_BRI (new Integer(192), "Maximum brightness"),
	MAX_SAT (new Integer(255), "Maximum saturation"),
	MAX_HUE (new Integer(129), "Maximum hue"),
	
	DATE_COLUMN (new Integer(0), "Maximum hue"),
	TIME_COLUMN (new Integer(1), "Maximum hue"),
	
	DATE_REGEX ("dd/MM/yy", "Date regex"),
	TIME_REGEX ("HH:mm:ss", "Time regex"),
	
	SHOW_DATES (new Boolean(true), "Show dates"),
	SHOW_STATUS (new Boolean(true), "Show status");
	
	private final Object defaultValue;   // Color associated with status
    private final String plainText; // The Status in plainText
    
    Setting(Object value, String plainText) {
        this.defaultValue = value;
        this.plainText = plainText;
    }
    
    public int defaultInt() { 
    	//TODO:ERRORHANDLING
    	return (int)defaultValue; 
    }
    public String defaultString() {
    	//TODO:ERRRORHANDLING
    	return (String)defaultValue;
    }
    public double defaultDouble() {
    	//TODO:ERRRORHANDLING
    	return (double)defaultValue;
    }
    
    public boolean defaultBoolean() {
    	//TODO:ERRRORHANDLING
    	return (boolean)defaultValue;
    }
    
    public String text() { return plainText; }
}
