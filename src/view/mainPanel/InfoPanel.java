package view.mainPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.ImageDataModel;
import utilities.SelectionListener;

/**
 * Info panel that can be used to display summaried information about a particular
 * data model.
 * @author Olav
 *
 */
public class InfoPanel extends JPanel implements SelectionListener {
	
	public InfoPanel() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(10, 10, 10 ,10));
		JLabel label = new JLabel("No selection");
		this.add(label, BorderLayout.CENTER);
	}
	
	public void setSelection(ImageDataModel data) {
		this.removeAll();
		this.repaint();
		String filePath = data.getFilePath();
		double[] values = data.getMeasurementValues();
		String valuesString = "";
		if (values != null && values.length >0) {
			for(int i = 0; i<values.length; i ++) {
				if (values[i] > -1) {
					valuesString += " " + values[i];
				}
			}
		}
		if(valuesString.equals("")) {
				valuesString = "No values have been set";
		}
		
		JLabel label = new JLabel(filePath);
		this.add(label, BorderLayout.NORTH);
		label = new JLabel(valuesString);
		this.add(label, BorderLayout.CENTER);
		this.revalidate();
	}
}
