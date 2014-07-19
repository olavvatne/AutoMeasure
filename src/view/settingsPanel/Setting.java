package view.settingsPanel;

import java.awt.Color;

/**
 * Enum for retrieving data from the configuration manager. 
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
	TIME_REGEX ("hh:mm:ss", "Time regex");
	
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
    
    public String text() { return plainText; }
}
