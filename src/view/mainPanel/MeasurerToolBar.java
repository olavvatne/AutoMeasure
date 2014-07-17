package view.mainPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.HashMap;

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
	private static final int HOVER_IMG = 1;
	private static final int CLICK_IMG = 2;
	private static final int NORMAL_IMG = 0;
	
	HashMap<JButton, String[]> map = new HashMap<JButton, String[]>();
	
	public MeasurerToolBar(ActionListener listener) {
		
		this.setFloatable(false);
		addButtons(listener);
	}
	
	private void addButtons(ActionListener listener) {
		JButton button = null;
		this.add(Box.createHorizontalStrut(10));
		button = makeToolBarButton("/open.png", Measurer.OPEN, "åpne bildemappe", "åpne", listener);
		map.put(button, new String[]{"/open.png", "/openhover.png", "/openClick.png"});
		this.add(button);
		this.add(Box.createHorizontalStrut(10));
		
		button = makeToolBarButton("/save.png", Measurer.SAVE, "lagre til excel", "Lagre", listener);
		map.put(button, new String[]{"/save.png", "/saveHover.png", "/saveClick.png"});
		button.setEnabled(false);
		this.add(button);
		this.addSeparator();
		this.add( Box.createHorizontalGlue() );
		this.addSeparator();
		button = makeToolBarButton("/calibrate.png", Measurer.CALIBRATE, "kalibrer", "Kalibrer", listener);
		map.put(button, new String[]{"/calibrate.png", "/calibratehover.png", "/calibrateClick.png"});
		this.add(button);
		button.setEnabled(false);
		button = makeToolBarButton("/analyze.png", Measurer.ANALYZE, "kjør analysering", "analyser", listener);
		map.put(button, new String[]{"/analyze.png", "/analyzehover.png", "/analyzeClick.png"});
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
		
		

		//Create and initialize the button.
		JButton button = new JButton();
		button.setBackground(Color.gray);
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		button.addActionListener(listener);
		button.setFocusPainted(false);
		setIconForButton(button, imageName, altText);	
		button.addMouseListener(new MouseListener() {
			public void mouseEntered(MouseEvent e) {
		        JButton button = (JButton)e.getSource();
		        setIconForButton(button, map.get(button)[HOVER_IMG], "test");
		    }

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				 JButton button = (JButton)e.getSource();
			     setIconForButton(button, map.get(button)[NORMAL_IMG], "test");
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				JButton button = (JButton)e.getSource();
		        setIconForButton(button, map.get(button)[CLICK_IMG], "test");
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				JButton button = (JButton)e.getSource();
		        setIconForButton(button, map.get(button)[NORMAL_IMG], "test");
				
			}
		});
		
		return button;
	}
	
	public void setIconForButton(JButton button, String imgLocation, String altText) {
		URL imageURL = Measurer.class.getResource(imgLocation);
		
		if (imageURL != null) {                      //image found
			button.setIcon(new ImageIcon(imageURL, altText));
		} else {                                     //no image found
			button.setText(altText);
			System.err.println("Resource not found: "
					+ imgLocation);
		}
	}
	
	


}
