package view.imageViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import analyze.Analyzer;
import analyze.AnalyzerThreadsMonitor;
import analyze.ImageMarkerFinder;
import analyze.ImageMarkerPoint;
import analyze.ImageValueFinder;
import automeasurer.Measurer;
import model.ImageDataModel;
import model.Status;

public class CalibratePanel extends Viewer implements ActionListener {

	public CalibratePanel(ImageDataModel data) {
		super(data);
		
		JToolBar toolBar = new JToolBar();
        addButtons(toolBar);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        add(toolBar, BorderLayout.PAGE_START);
	}

	private void addButtons(JToolBar toolBar) {
		JButton button = null;
		toolBar.add(Box.createHorizontalStrut(10));
		button = new JButton("Find markers");
        button.setActionCommand(Measurer.ANALYZE_IMAGE);
        button.setToolTipText("Analyze image to test the calibration of the analyzer");
        button.addActionListener(this);
        toolBar.add(button);
        
        toolBar.add( Box.createHorizontalGlue() );
        
        button = new JButton("Finish");
        button.setActionCommand("FINISH");
        button.setToolTipText("Save calibration");
        button.addActionListener(this);
        toolBar.add(button);
        
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		 
        
        if (Measurer.ANALYZE_IMAGE.equals(cmd))
        {
        	List<ImageMarkerPoint> markers =ImageMarkerFinder.run(data.getFilePath());

			if(markers != null) {
				System.out.println(markers.size());
				data.setMarkers(markers);
				threePhasePanel.setData(data);
			}
			else {
				//image.setStatus(Status.FAILURE);
				//Må komme en feilmelding eller noe , eller en måte å endre på innstillingene!
			}	
        }
        else if ("FINISH".equals(cmd)) {
        	//Only save calibration if user clics the button. Not if he exits.
        	saveAndCloseCalibration();
        }
	}
	
	private void saveAndCloseCalibration() {
		//TODO: Save other calibration when time comes
		threePhasePanel.close();
		((JFrame)SwingUtilities.getRoot(this)).setVisible(false);
    	((JFrame)SwingUtilities.getRoot(this)).dispose();
	}
}
