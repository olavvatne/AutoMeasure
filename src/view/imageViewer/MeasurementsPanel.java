package view.imageViewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

/**
 * Panel to view the image and its measurements, and update them if necessary.
 * 
 * @author Olav
 *
 */
public class MeasurementsPanel extends Viewer implements ActionListener, KeyListener {
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
        this.addKeyListener(this);
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
		iterateModel(cmd);
		System.out.println("AREA: " + this.data.markerArea(3));
	}
	
	private void iterateModel(String cmd) {
		if (Measurer.NEXT.equals(cmd)) {
			if(this.model.getRowCount() > row+1) {
				this.threePhasePanel.close();
				row ++;	
				this.setModel(this.model.getDataModel(row));
			}
		}
		else if (Measurer.PREVIOUS.equals(cmd) ) {
			if(row-1 >= 0) {
				this.threePhasePanel.close();
				row --;
				this.setModel(this.model.getDataModel(row));
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			iterateModel(Measurer.NEXT);
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			iterateModel(Measurer.PREVIOUS);
		}
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
