package view.mainPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import view.imageViewer.MeasurementsPanel;
import view.imageViewer.Viewer;
import model.ImageDataModel;
import model.ImageTableModel;

public class ImageTablePanel extends JPanel implements MouseListener {
	private ImageTableModel model;
	private JTable imageTable;
	private JScrollPane listScroll;
	
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
		this.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
	}


	public void mousePressed(MouseEvent e) {
		if (e.getClickCount() == 2) {
			Point p = e.getPoint();
			int rowInTable = imageTable.getSelectedRow();
			int row = imageTable.convertRowIndexToModel(rowInTable);
			//pass på memory leak?? Sjekk det ut senere
			ImageDataModel data = this.model.getDataModel(row);
			Viewer panel = new MeasurementsPanel(data, model, row);
			JFrame frame = new JFrame();
			Dimension sz = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setMinimumSize(new Dimension((int)(sz.getWidth()/2), (int)(sz.getHeight()/2)));
			frame.setPreferredSize(sz);// må vekk for at minimering skal faktisk minimere, maximize forårsaker layout krøll med initflytting
			frame.pack();
			frame.add(panel);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			System.out.println(data.getFilePath());
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
		// TODO Auto-generated method stub
		
	}

	
	
	
}
