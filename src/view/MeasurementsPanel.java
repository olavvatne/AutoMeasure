package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import analyze.ImageMarkerFinder;
import analyze.ImageMarkerPoint;
import automeasurer.Measurer;
import model.ImageDataModel;
import model.ImageTableModel;

public class MeasurementsPanel extends Viewer implements ActionListener {
	ImageTableModel model;
	int row;
	//finn bedre måte med refactorering
	public MeasurementsPanel(ImageDataModel data, ImageTableModel model, int row) {
		super(data);
		this.model = model;
		this.row = row;
		JToolBar toolBar = new JToolBar();
        addButtons(toolBar);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        add(toolBar, BorderLayout.PAGE_START);
	}
	
	
	private void addButtons(JToolBar toolBar) {
		JButton button = null;
		toolBar.add( Box.createHorizontalGlue() );
		button = new JButton("tilbake");
        button.setActionCommand(Measurer.PREVIOUS);
        button.setToolTipText("Neste Bilde");
        button.addActionListener(this);
        toolBar.add(button);
        
        button = new JButton("fremover");
        button.setActionCommand(Measurer.NEXT);
        button.setToolTipText("forrige bilde");
        button.addActionListener(this);
        toolBar.add(button);
        
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (Measurer.NEXT.equals(cmd)) {
			row ++;	
			this.setModel(this.model.getDataModel(row));
		}
		else if (Measurer.PREVIOUS.equals(cmd) ) {
			row --;
			this.setModel(this.model.getDataModel(row));
		}
		System.out.println("AREA: " + this.data.markerArea(3));
	}
}
