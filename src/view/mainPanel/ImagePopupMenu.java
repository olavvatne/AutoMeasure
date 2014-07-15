package view.mainPanel;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import model.ImageTableModel;

/**
 * ImagePopupMenu extends JPopupMenu. Specialized popupmenu containing
 * two options. Open containing folder or open image. When initing object, actionListeners are added
 * to all menu items.
 * 
 * 	- Open containing folder - Open a finder/explorer window with the the content of the folder the image belongs to
 *  - Open image - Open the image currently selected in an external program, like window image viewer.
 *  
 * @author Olav
 *
 */
public class ImagePopupMenu extends JPopupMenu {
	private ImageTableModel model;
	
	public ImagePopupMenu(ImageTableModel tableModel) {
		this.model = tableModel;
		JMenuItem openFolder = new JMenuItem("Open containing folder");
		openFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				  	Component c = (Component)e.getSource();
			        JPopupMenu popup = (JPopupMenu)c.getParent();
			        JTable table = (JTable)popup.getInvoker();
					int row = table.getSelectedRow();
					displayFile(model.getDataModel(row).getFilePath(), true);
				}
				
			
		});
		
		JMenuItem inspectItem = new JMenuItem("Open image");
		inspectItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					Component c = (Component)e.getSource();
			        JPopupMenu popup = (JPopupMenu)c.getParent();
			        JTable table = (JTable)popup.getInvoker();
					int row = table.getSelectedRow();
					displayFile(model.getDataModel(row).getFilePath(), false);
				}
				
			
		});
		this.add(openFolder);
		this.add(inspectItem);
	}
	
	public void displayFile(String path, boolean parent) {
		File dir = new File(path);
		if(parent) {
			dir = dir.getParentFile();
		}
		try {
			Desktop.getDesktop().open(dir);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
