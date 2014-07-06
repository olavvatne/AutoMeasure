package view.excelSaveDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import utilities.ExcelModel;
import utilities.ExcelWriter;
import filter.ExcelFilter;
import filter.ImageFilter;

public class SaveToExcelFilePanel extends JPanel implements ActionListener {
	private JButton excelButton;
	private final JFileChooser fc = new JFileChooser();
	private List<ExcelModel> model;
	
	public SaveToExcelFilePanel(List<ExcelModel> model) {
		this.add(new JLabel("test"));
		excelButton = new JButton("SAVE");
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
			/**file = new File( fc.getSelectedFile( ) + ".xls" );
			if(file == null)
		        return;
			if(file.exists())
		    {
				returnVal = JOptionPane.showConfirmDialog(this,
		                           "Replace existing file?");
		        // may need to check for cancel option as well
		        if (returnVal == JOptionPane.NO_OPTION)
		            return;
		    }*/
			file = fc.getSelectedFile();
			ExcelWriter writer = new ExcelWriter(file);
			writer.writeExcelFile(this.model);
		}
	}

}
