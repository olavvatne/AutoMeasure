package view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.ImageDataModel;
import model.ImageTableModel;
import analyze.Analyzer;
import analyze.AnalyzerThreadsMonitor;
import analyze.ImageMarkerFinder;
import analyze.ImageMarkerPoint;
import automeasurer.Measurer;

public class MainPanel extends JPanel implements ActionListener, PropertyChangeListener {
	
	private ImageTableModel model;
	private ProgressPanel progress;
	private MeasurerToolBar toolBar;
	AnalyzerThreadsMonitor mon;
	private boolean analyzingInProgress = false;
	/**
	 * Main panel containing toolbar, progressbar and table of all pictures loaded
	 */
	public MainPanel() {
		
		toolBar = new MeasurerToolBar(this);
		toolBar.setBackground(Color.gray);
		ImageTableModel model = new ImageTableModel();
		this.model = model;
		this.setLayout(new BorderLayout());
		this.add(toolBar, BorderLayout.PAGE_START);
		
		InfoPanel infoPanel = new InfoPanel();
		this.add(infoPanel, BorderLayout.EAST);
		//infoPanel.setBackground(Color.black);
		
		ImageTablePanel imageTablePanel = new ImageTablePanel(model);
		this.add(imageTablePanel, BorderLayout.CENTER);
		//imageTablePanel.setBackground(Color.blue);
		progress = new ProgressPanel();
		this.add(progress, BorderLayout.SOUTH);
		this.model.addPropertyChangeListener(this);
		
		
    	this.mon= new AnalyzerThreadsMonitor(this.model, this.progress);
    	//temp enable excel button
    	this.toolBar.setButtonEnabled(Measurer.SAVE, true);
		
	}

	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
 
        if (Measurer.OPEN.equals(cmd)) { 
        	OpenFoldersPanel dialog = new OpenFoldersPanel(this);
        	
        	dialog.addPropertyChangeListener(this.model);
        	dialog.addPropertyChangeListener(this.progress);
        	
        	int option = JOptionPane.showConfirmDialog(this ,dialog, "Folder dialog", JOptionPane.OK_CANCEL_OPTION);
            if (option==JOptionPane.OK_OPTION) {
            	
            	//finn ut thread best practices
            	dialog.getFiles();
            	toolBar.setButtonEnabled(Measurer.CALIBRATE, true);
            }
            else {
            	System.out.println("cancel");
            }
        } 
        else if (Measurer.SAVE.equals(cmd)) 
        { 
        	SaveToExcelFilePanel dialog = new SaveToExcelFilePanel();
        	int option = JOptionPane.showConfirmDialog(this ,dialog, "Excel dialog", JOptionPane.OK_CANCEL_OPTION);
            
        }
        else if (Measurer.ANALYZE.equals(cmd))
        {
        	if(!this.analyzingInProgress) {
        		//start evaluate pictures
        		int nrOfThreads = Runtime.getRuntime().availableProcessors();
        		this.mon.initThreads(nrOfThreads);
            	this.analyzingInProgress = true;
        	}
        	else {
        		
        		//stop evaluate pictures
        		this.mon.pauseThreads();
        		this.analyzingInProgress = false;
        	}
        	
        	
        }
        else if (Measurer.CALIBRATE.equals(cmd)) {
        	ImageDataModel data = this.model.getDataModel(0);
        	
			Viewer panel = new CalibratePanel(data);
			JFrame frame = new JFrame();
			Dimension sz = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setMinimumSize(new Dimension((int)(sz.getWidth()/2), (int)(sz.getHeight()/2)));
			frame.setPreferredSize(sz);// må vekk for at minimering skal faktisk minimere, maximize forårsaker layout krøll med initflytting
			frame.pack();
			frame.add(panel);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			System.out.println(data.getFilePath());
			
			
			this.toolBar.setButtonEnabled(Measurer.ANALYZE, true);
        }
		
	}




	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
	}
}
