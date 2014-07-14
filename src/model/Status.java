package model;

/**
 * Enum for status of a data model.
 * Image analysis gives unreliable results and the status
 * categories the analysis quality, and status.
 * @author Olav
 *
 */
public enum Status  {
	SUCCESS, FAILURE, UNKNOWN, PARTIALSUCCESS, WORKED_ON, MANUAL_EDIT
}
