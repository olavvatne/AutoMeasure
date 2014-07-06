package view.mainPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import automeasurer.Measurer;
/**
 * The toolbar of this application. Contain several icons buttons that will typically open a view.
 * The button events are not handled here, it is instead handled by the ActionListner (MainPanel).
 * 
 * @author Olav
 *
 */
public class MeasurerToolBar extends JToolBar {

	public MeasurerToolBar(ActionListener listener) {
		
		this.setFloatable(false);
		addButtons(listener);
	}
	
	private void addButtons(ActionListener listener) {
		JButton button = null;
		this.add(Box.createHorizontalStrut(10));
		button = makeToolBarButton("open", Measurer.OPEN, "åpne bildemappe", "åpne", listener);
		this.add(button);
		this.add(Box.createHorizontalStrut(10));
		button = makeToolBarButton("write", Measurer.SAVE, "lagre til excel", "Lagre", listener);
		button.setEnabled(false);
		this.add(button);
		this.add( Box.createHorizontalGlue() );
		button = makeToolBarButton("calibrate", Measurer.CALIBRATE, "kalibrer", "Kalibrer", listener);
		this.add(button);
		button.setEnabled(false);
		button = makeToolBarButton("analyze", Measurer.ANALYZE, "kjør analysering", "analyser", listener);
		button.setEnabled(false);
		this.add(button);
		this.add(Box.createHorizontalStrut(10));
	}
	
	public void setButtonEnabled(String cmd, boolean enabled) {
		Component[] buttons =this.getComponents();
		for (int i = 0; i<buttons.length; i++) {
			if(buttons[i] instanceof JButton ) {
				if(((JButton)buttons[i]).getActionCommand().equals(cmd)) {
					buttons[i].setEnabled(enabled);
				}
			}
		}
	}
	private JButton makeToolBarButton(String imageName,
			String actionCommand,
			String toolTipText,
			String altText,
			ActionListener listener) {
		//Look for the image.
		String imgLocation = "/"
				+ imageName
				+ ".png";
		URL imageURL = Measurer.class.getResource(imgLocation);

		//Create and initialize the button.
		JButton button = new JButton();
		button.setBackground(Color.gray);
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		button.addActionListener(listener);
		

		if (imageURL != null) {                      //image found
			button.setIcon(new ImageIcon(imageURL, altText));
		} else {                                     //no image found
			button.setText(altText);
			System.err.println("Resource not found: "
					+ imgLocation);
		}
		
		return button;
	}
}
