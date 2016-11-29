package view.excelSaveDialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import utilities.ExcelModel;
import utilities.ExcelWriter;
import filter.ExcelFilter;

public class SaveToExcelFilePanel extends JPanel implements ActionListener {
	private JButton excelButton;
	private JLabel excelLoadText;
	private final JFileChooser fc = new JFileChooser();
	private List<ExcelModel> model;
	private File saveFile = null;
	private final static String HELP_TEXT = "Either open exisiting timestamp excel file, or save as new file. The former will match measurement date with timestamps in file."; 
	private ArrayList<PropertyChangeListener> excelListener = new ArrayList<PropertyChangeListener>();
	
	public SaveToExcelFilePanel(List<ExcelModel> model) {
		Dimension d = new Dimension(300, 200);
		Dimension d2 = new Dimension(300, 100);
		JTextArea help = new JTextArea(HELP_TEXT);
		help.setWrapStyleWord(true);
		help.setLineWrap(true);
		help.setEditable(false);
		help.setPreferredSize(d2);
		this.add(help);
		excelLoadText = new JLabel("");
		this.add(excelLoadText);
		excelButton = new JButton("select/create file");
		excelButton.addActionListener(this);
		this.add(excelButton);
		this.model = model;
		this.setMinimumSize(d);
		this.setPreferredSize(d);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		File file = null;
		
		fc.setFileFilter(new ExcelFilter());

		fc.setDialogTitle("Selct existing or save new excel file.");
		int returnVal = fc.showDialog(this, "Save");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			
			/*returnVal = JOptionPane.showConfirmDialog(this,
                    "Pressing yes will save the measurements");
			if (returnVal == JOptionPane.NO_OPTION)
	            return;*/
			this.saveFile = file;
			this.excelLoadText.setText(this.saveFile.getName());
			
		}
	}
	
	public void writeFile() {
		if(this.saveFile != null ) {
			boolean newFile = !this.saveFile.exists();
			ExcelWriter writer = new ExcelWriter(this.saveFile, !this.saveFile.exists());
			for(PropertyChangeListener listener:excelListener) {
				writer.addPropertyChangeListener(listener);				
			}
			writer.writeExcelFile(this.model);
		}	
	}
	
	public void destroyFile() {
		if(saveFile != null ) {
			saveFile.delete();
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		excelListener.add(listener);
	}
}
