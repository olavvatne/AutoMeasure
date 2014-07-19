package view.mainPanel.menuBar;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import view.mainPanel.MainPanel;
import view.settingsPanel.settingsPanel;

public class MenuBar extends JMenuBar {
	JMenu menu, submenu;
	JMenuItem menuItem;
	MainPanel panel; 
	JFrame frame;
	
	public MenuBar(MainPanel panel, JFrame frame) {
		super();
		this.panel = panel;
		this.frame = frame;
		
		menu = new JMenu("Options");
		menu.setMnemonic(KeyEvent.VK_O);
		this.add(menu);
		
		menuItem = new JMenuItem("Settings",
                KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		KeyEvent.VK_S, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
		"Settings to tweak the analyzer, and gui");
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				openSettingsPanel();
			}
		});
	}
	
	public void openSettingsPanel() {
		settingsPanel settings = new settingsPanel(this.panel);
    	JDialog dialog = new JDialog(this.frame, "Settings", true);  
		dialog.setResizable(false);  
		dialog.getContentPane().add(settings);   
		dialog.pack();  
		Dimension Size = Toolkit.getDefaultToolkit().getScreenSize();  
		dialog.setLocation(frame.getLocation()); 
		dialog.setVisible(true); 
	}
}
