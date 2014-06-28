

package view;
 
 
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import model.Status;

import java.awt.Color;
import java.awt.Component;
 
public class ColorRenderer extends JLabel
                           implements TableCellRenderer {
    Border unselectedBorder = null;
    Border selectedBorder = null;
    boolean isBordered = true;
 
    public ColorRenderer(boolean isBordered) {
        this.isBordered = isBordered;
        setOpaque(true); //MUST do this for background to show up.
    }
 
    public Component getTableCellRendererComponent(
                            JTable table, Object status,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
    	Color newColor = null;
    	if(Status.FAILURE == (Status) status) {
    		newColor = Color.red;
    	}
    	else if (Status.SUCCESS == (Status) status) {
    		newColor = Color.green;
    	}
    	else if (Status.WORKED_ON == (Status) status) {
    		newColor = Color.blue;
    	}
    	else {
    		newColor = Color.gray;
    	}
        setBackground(newColor);
        if (isBordered) {
            if (isSelected) {
                if (selectedBorder == null) {
                    selectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
                                              table.getSelectionBackground());
                }
                setBorder(selectedBorder);
            } else {
                if (unselectedBorder == null) {
                    unselectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
                                              table.getBackground());
                }
                setBorder(unselectedBorder);
            }
        }
         
        setToolTipText("Status");
        return this;
    }
}