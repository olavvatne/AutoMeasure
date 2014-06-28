package view;

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
        button.setToolTipText("Analyser bilde for å finne markører");
        button.addActionListener(this);
        toolBar.add(button);
        
        toolBar.add( Box.createHorizontalGlue() );
        
        button = new JButton("Fullfør");
        button.setActionCommand("FINISH");
        button.setToolTipText("Lagre innstillinger");
        button.addActionListener(this);
        toolBar.add(button);
        
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		 
        
        if (Measurer.ANALYZE_IMAGE.equals(cmd))
        {
        	System.out.println("LOL");
        	List<ImageMarkerPoint> markers =ImageMarkerFinder.run(data.getFilePath());
			if(markers != null) {
				 
				data.setMarkers(markers);
				threePhasePanel.setData(data);
			}
			else {
				//image.setStatus(Status.FAILURE);
				//Må komme en feilmelding eller noe , eller en måte å endre på innstillingene!
			}	
        }
        else if ("FINISH".equals(cmd)) {
        	((JFrame)SwingUtilities.getRoot(this)).setVisible(false);
        	((JFrame)SwingUtilities.getRoot(this)).dispose();
        }
	}

}
