package view.excelSaveDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utilities.ExcelModel;
import utilities.ExcelWriter;
import filter.ExcelFilter;

public class SaveToExcelFilePanel extends JPanel implements ActionListener {
	private JButton excelButton;
	private final JFileChooser fc = new JFileChooser();
	private List<ExcelModel> model;
	private File saveFile = null;
	
	private PropertyChangeListener excelListener;
	
	public SaveToExcelFilePanel(List<ExcelModel> model) {
		this.add(new JLabel("test"));
		excelButton = new JButton("select/create file");
		excelButton.addActionListener(this);
		this.add(excelButton);
		this.model = model;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		File file = null;
		
		fc.setFileFilter(new ExcelFilter());

		
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			
			/*returnVal = JOptionPane.showConfirmDialog(this,
                    "Pressing yes will save the measurements");
			if (returnVal == JOptionPane.NO_OPTION)
	            return;*/
			this.saveFile = file;
			
		}
	}
	
	public void writeFile() {
		if(this.saveFile != null ) {
			boolean newFile = !this.saveFile.exists();
			ExcelWriter writer = new ExcelWriter(this.saveFile, !this.saveFile.exists());
			if(excelListener != null) {
				writer.addPropertyChangeListener(excelListener);				
			}
			writer.writeExcelFile(this.model);
		}	
	}
	
	public void destroyFile() {
		if(saveFile != null ) {
			saveFile.delete();
		}
	}
	
	public void setPropertyChangeListener(PropertyChangeListener listener) {
		excelListener = listener;
	}
}
