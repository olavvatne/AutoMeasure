package view.imageFolderDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;



import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.joda.time.DateTime;

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
	
	/*fikse alt som har med listene � gj�re*/
	
	public ArrayList<File[]> files = new ArrayList<File[]>();
	private byte counter = 1;
	final JFileChooser fc = new JFileChooser();
	private JList list;
	private JScrollPane scroll;
	private DefaultListModel model;
	private PropertyChangeSupport pcs;
	
	public OpenFoldersPanel(JPanel parentPanel) {
		super();
		initComponents(parentPanel);
		pcs = new PropertyChangeSupport(this);
	}
	
	
	
	private void initComponents(JPanel parentPanel) {
		JButton button = new JButton("Open folder");
		button.addActionListener(this);
		this.add(button);
		button.addActionListener((ActionListener)parentPanel);
		
		model = new DefaultListModel<String>();
		list = new JList<String>();
		scroll = new JScrollPane(list);
		list.setModel(model);
		
		this.add(scroll);
		
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
					DateTime previous = (new DateTime()).withYear(0);
					
					int size = 0;
					for ( int j = 0; j<files.size(); j++) {
						for (int i = 0 ; i<files.get(j).length; i++) {
							DateTime date = getDate(files.get(j)[i]);
							
							if(date != null) {
								//TODO: Somehow tell the user how many invalid images was found.
								System.out.println(date);
								
								list.add(new ImageDataModel(date, files.get(j)[i].getAbsolutePath()));
								if(date.isBefore(previous)) {
									//TODO: REFACTOR
									JOptionPane.showMessageDialog(null, "The image order is not correct! This might cause problems for the excel writer. Error detected in " + files.get(j)[i].getName());
								}
								previous = date;
							}
							
							if(i % 30 == 0) {
								pcs.firePropertyChange(Measurer.PROGRESS_UPDATE, null, new Integer(size+i));
							}
							
						}
						size+=files.get(j).length;
					}
				
					pcs.firePropertyChange(Measurer.FINISHED, null, list);
				}
			};
			work.start();
			//sjekke sortering. M� legges til i rekkef�lge
			
		}
		else {
			JOptionPane.showMessageDialog(null, "No images in folder.", "Error", JOptionPane.WARNING_MESSAGE);
			
		}
	}
	
	public static DateTime getDate(File file) {
		Metadata metadata = null;
		try {
			metadata = ImageMetadataReader.readMetadata(file);
		} catch (ImageProcessingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		//nullpointer exception m� h�ndtere n�r filer ikke inneholder metadata!
		if(metadata != null) {
			ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);
			DateTime date = null;
			try {
				date = new DateTime(directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
			} catch (Exception e) {
				//TODO: BETTER ERROR HANDLING __INVALID IMAGES GETS HERE
				return null;
			}
			date = date.minuteOfDay().roundFloorCopy();
			return date;
		}
		return null;
	}
	
	
	private boolean findPictureFiles(String title) {
		File file;
		
		fc.setFileFilter(new ImageFilter());
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		int returnVal = fc.showDialog(null, title);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			if(file.isDirectory()) {
				this.files.add(file.listFiles());
			}
			else {
				this.files.add(file.getParentFile().listFiles());				
			}
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
		boolean state = findPictureFiles("Choose folder " + counter);
		if(state) {
			counter ++;
		}
		
		
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);

	}
}
