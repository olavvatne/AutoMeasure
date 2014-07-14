package view.mainPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import automeasurer.Measurer;
import view.imageViewer.MeasurementsPanel;
import model.ImageDataModel;
import model.ImageTableModel;
/**
 * JPanel for displaying table inside a scroll window, containing a row for each image.
 * 
 * @author Olav
 *
 */
public class ImageTablePanel extends JPanel implements MouseListener, PropertyChangeListener {
	private ImageTableModel model;
	private JTable imageTable;
	private JScrollPane listScroll;
	private ImagePopupMenu popup;
	
	public ImageTablePanel(ImageTableModel model) {
		this.setLayout(new BorderLayout());
		this.model = model;
		imageTable = new JTable();
		imageTable.setModel(model);
		imageTable.addMouseListener(this);
		//this.model.addTableModelListener(imageTable);
		listScroll = new JScrollPane();
		
		imageTable.setDefaultRenderer(Enum.class, new ColorRenderer(true));
		listScroll.setViewportView(imageTable);
		this.add(listScroll, BorderLayout.CENTER);
		
		popup = new ImagePopupMenu(this.model);
		this.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
	}
	
	
	
	
	
	private void openMeasurementPanel(MouseEvent e) {
		int rowInTable = imageTable.getSelectedRow();
		int row = imageTable.convertRowIndexToModel(rowInTable);
		//pass p� memory leak?? Sjekk det ut senere
		ImageDataModel data = this.model.getDataModel(row);
		JFrame frame = new JFrame();
		MeasurementsPanel panel = new MeasurementsPanel(frame, data, model, row);
		panel.addChangeListener(this);
		
		Dimension sz = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setMinimumSize(new Dimension((int)(sz.getWidth()/2), (int)(sz.getHeight()/2)));
		frame.setPreferredSize(sz);// m� vekk for at minimering skal faktisk minimere, maximize for�rsaker layout kr�ll med initflytting
		frame.pack();
		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	public void mousePressed(MouseEvent e) {
		if (e.getClickCount() == 2) {
			openMeasurementPanel(e);
		}

	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger())
        {
            JTable source = (JTable)e.getSource();
            if(source instanceof JTable) {
            	int row = source.rowAtPoint( e.getPoint() );
                int column = source.columnAtPoint( e.getPoint() );

                if (! source.isRowSelected(row))
                    source.changeSelection(row, column, false, false);

                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(Measurer.TABLE_SELECTION.equals(evt.getPropertyName())) {
			imageTable.setRowSelectionInterval((int)evt.getNewValue(), (int)evt.getNewValue());
		}
	}

	
	
	
}
