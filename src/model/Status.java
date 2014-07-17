package model;

import java.awt.Color;

/**
 * Enum for status of a data model.
 * Image analysis gives unreliable results and therefore a enum is needed to
 * categorize the analysis result.
 * @author Olav
 *
 */
public enum Status  {
	SUCCESS (Color.green, "Success"),
	FAILURE (Color.red, "Failure"),
	UNKNOWN (Color.gray, "Unknown"),
	PARTIALSUCCESS (Color.yellow, "Partial success"),
	WORKED_ON (Color.blue, "Worked on"),
	MANUAL_EDIT (Color.cyan, "Manually edited");
	
	private final Color color;   // Color associated with status
    private final String plainText; // The Status in plainText
    
    Status(Color color, String plainText) {
        this.color = color;
        this.plainText = plainText;
    }
    
    public Color color() { return color; }
    public String text() { return plainText; }
}
