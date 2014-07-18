package view.mainPanel.menuBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuBar extends JMenuBar {
	JMenu menu, submenu;
	JMenuItem menuItem;
	
	public MenuBar() {
		super();
		
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
				System.out.println("OPTIONS");
				
			}
		});
	}
}
