package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;



import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import automeasurer.Measurer;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import model.ImageDataModel;
import filter.ImageFilter;

/**
 * OpenFoldersPanel is a customized JPanel to put into a pop up dialog. 
 * It contains a list and a file picker, to add folders of pictures to the application.
 * All added folders are put into the list, as feedback to the user.
 * @author Olav
 *
 */
public class OpenFoldersPanel extends JPanel implements ActionListener {
	
	/*fikse alt som har med listene å gjøre*/
	
	public ArrayList<File[]> files = new ArrayList<File[]>();
	private byte counter = 1;
	final JFileChooser fc = new JFileChooser();
	private JList list;
	private DefaultListModel model;
	private PropertyChangeSupport pcs;
	
	public OpenFoldersPanel(JPanel parentPanel) {
		super();
		initComponents(parentPanel);
		pcs = new PropertyChangeSupport(this);
	}
	
	
	
	private void initComponents(JPanel parentPanel) {
		JButton button = new JButton("åpne folder");
		button.addActionListener(this);
		this.add(button);
		button.addActionListener((ActionListener)parentPanel);
		
		model = new DefaultListModel<String>();
		list = new JList<String>();
		
		list.setModel(model);
		this.add(list);
		
	}
	private int getSizeOfFiles() {
		int maxsize = 0;
		for ( int t = 0; t<files.size(); t++) {
			maxsize += files.get(t).length;
		}
		return maxsize;
	}
	
	/**
	 * GetFiles should be called to start a thread creating ImageDataModel object for each
	 * picture file, adding pictures path to the object. After all pictures are loaded into a list, its 
	 * send via pcs. Method also dispatch events for progress bar updates.
	 */
	public void getFiles() {
		
		if(this.files.size()>0) {
			
			Thread work = new Thread() {
				public void run() {
					ArrayList<ImageDataModel> list = new ArrayList<ImageDataModel>();
					
					pcs.firePropertyChange(Measurer.SETMAX, null, new Integer(getSizeOfFiles()));
					
					int size = 0;
					for ( int j = 0; j<files.size(); j++) {
						for (int i = 0 ; i<files.get(j).length; i++) {
							list.add(new ImageDataModel(getDate(files.get(j)[i]), files.get(j)[i].getAbsolutePath()));
							//Inefficent to send a event for each file. A bit overkill. getDate later also maybe?
							pcs.firePropertyChange(Measurer.PROGRESS_UPDATE, null, new Integer(size+i));
						}
						size+=files.get(j).length;
					}
				
					pcs.firePropertyChange(Measurer.FINISHED, null, list);
				}
			};
			work.start();
			//sjekke sortering. Må legges til i rekkefølge
			
		}
		else {
			//funket ikke . feilmelding
			
		}
	}
	
	public static Date getDate(File file) {
		int ZERO_SECONDS_INT = 0;
		Metadata metadata = null;
		try {
			metadata = ImageMetadataReader.readMetadata(file);
		} catch (ImageProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//nullpointer exception må håndtere når filer ikke inneholder metadata!
		if(metadata != null) {
			ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);
			Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
			date.setSeconds(ZERO_SECONDS_INT);
			return date;
		}
		else return null;
	}
	
	
	private boolean findPictureFiles(String title) {
		File file;
		
		fc.setFileFilter(new ImageFilter());


		int returnVal = fc.showDialog(null, title);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			this.files.add(file.getParentFile().listFiles());
			this.model.addElement(file.getName());
			//pcs.firePropertyChange(Measurer.NEW_IMAGE_FOLDER, null, null);
			return true;
		}
		
		if(returnVal == JFileChooser.CANCEL_OPTION) {
			return false;
		}

		return false;
	}

	public void actionPerformed(ActionEvent e) {
		boolean state = findPictureFiles("Velg mappe " + counter);
		if(state) {
			counter ++;
		}
		
		
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);

	}
}
