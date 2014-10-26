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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import analyze.ImageMarkerFinder;
import analyze.ImageMarkerPoint;
import automeasurer.Measurer;
import model.ImageDataModel;
import model.ImageTableModel;
import model.Status;

/**
 * Panel to view the image and its measurements, and update them if necessary.
 * 
 * @author Olav
 *
 */
public class MeasurementsPanel extends Viewer implements ActionListener {
	ImageTableModel model;
	private PropertyChangeSupport pcs; 
	JCheckBox iterateSuccessful;
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
		toolBar.add( Box.createHorizontalGlue() );
		this.iterateSuccessful = new JCheckBox("Iterate only successful images");
		this.iterateSuccessful.setSelected(false);
        toolBar.add(this.iterateSuccessful);
        
		JButton button = null;
		button = new JButton("Previous");
        button.setActionCommand(Measurer.PREVIOUS);
        button.setToolTipText("Previous image in table");
        button.addActionListener(this);
        toolBar.add(button);
        /*Set shortcut that work*/
        button = new JButton("Next");
        button.setActionCommand(Measurer.NEXT);
        button.setToolTipText("Next image in the table");
        button.addActionListener(this);
        toolBar.add(button);
        /*Set shortcut that works!*/
        
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
				this.setModel(getNextDataModel(row, this.iterateSuccessful.isSelected()));
				pcs.firePropertyChange(Measurer.TABLE_SELECTION, this.row-1, this.row);
			}
		}
		else if (Measurer.PREVIOUS.equals(cmd) ) {
			if(row-1 >= 0) {
				saveChanges();
				this.setModel(getPreviousDataModel(row, this.iterateSuccessful.isSelected()));
				pcs.firePropertyChange(Measurer.TABLE_SELECTION, this.row+1, this.row);
			}
		}
	}
	
	/**
	 * A helper method that depending on the boolean returns the appropriate next dataModel. If
	 * onlySuccessful is checked the next successfully read imageDataModel is returned, else the next image in the
	 * arrayList is returned
	 * @param i
	 * @param onlySuccessful
	 * @return
	 */
	public ImageDataModel getNextDataModel(int i, boolean onlySuccessful) {
		if(onlySuccessful) {
			for(int j = i+1; j<this.model.getRowCount(); j++) {
				//TODO: Inefficent if very many failed images inbetween
				if(this.model.getDataModel(j).getStatus() == Status.SUCCESS) {
					this.row = j;
					return this.model.getDataModel(j);
				}
			}
			//If no next success is found, same image is returned.
			return this.model.getDataModel(i);
		}
		else {
			//If the onlySuccessful is false, the next imageDataModel is returned.
			this.row = i+1;
			return this.model.getDataModel(this.row);			
		}
	}
	
	/**
	 * A helper method that depending on the boolean returns the appropriate prevous dataModel. If
	 * onlySuccessful is checked the previous successfully read imageDataModel is returned, else the next image in the
	 * arrayList is returned
	 * @param i
	 * @param onlySuccessful
	 * @return
	 */
	public ImageDataModel getPreviousDataModel(int i, boolean onlySuccessful) {
		if(onlySuccessful) {
			//TODO: Inefficent if very many failed images inbetween
			for(int j = i-1; j>=0; j--) {
				if(this.model.getDataModel(j).getStatus() == Status.SUCCESS) {
					this.row = j;
					return this.model.getDataModel(j);
				}
			}
			return this.model.getDataModel(i);
		}
		else {
			//If the onlySuccessful is false, the previous imageDataModel is returned.
			this.row = i-1;
			return this.model.getDataModel(this.row);			
		}
	}
	
	
	public void addChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	

	private void saveChanges() {
		this.threePhasePanel.close();
	}
}
