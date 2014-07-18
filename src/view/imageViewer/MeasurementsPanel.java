package view.imageViewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
public class MeasurementsPanel extends Viewer implements ActionListener {
	ImageTableModel model;
	private PropertyChangeSupport pcs; 
	int row;
	
	//finn bedre måte med refactorering
	public MeasurementsPanel(JFrame frame, ImageDataModel data, ImageTableModel model, int row) {
		super(data);
		this.model = model;
		this.row = row;
		JToolBar toolBar = new JToolBar();
        addButtons(toolBar);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        add(toolBar, BorderLayout.PAGE_START);
        
        frame.addWindowListener(new WindowAdapter() {
        	@Override
            public void windowClosed(WindowEvent e) {
                saveChanges();
            }
        });
        this.pcs = new PropertyChangeSupport(this);
	}
	
	
	private void addButtons(JToolBar toolBar) {
		JButton button = null;
		toolBar.add( Box.createHorizontalGlue() );
		button = new JButton("Previous");
        button.setActionCommand(Measurer.PREVIOUS);
        button.setToolTipText("Previous image in table");
        button.addActionListener(this);
        toolBar.add(button);
        
        button = new JButton("Next");
        button.setActionCommand(Measurer.NEXT);
        button.setToolTipText("Next image in the table");
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
				saveChanges();
				row ++;	
				this.setModel(this.model.getDataModel(row));
				pcs.firePropertyChange(Measurer.TABLE_SELECTION, this.row-1, this.row);
			}
		}
		else if (Measurer.PREVIOUS.equals(cmd) ) {
			if(row-1 >= 0) {
				saveChanges();
				row --;
				this.setModel(this.model.getDataModel(row));
				pcs.firePropertyChange(Measurer.TABLE_SELECTION, this.row+1, this.row);
			}
		}
	}
	
	public void addChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	

	private void saveChanges() {
		this.threePhasePanel.close();
	}
}
