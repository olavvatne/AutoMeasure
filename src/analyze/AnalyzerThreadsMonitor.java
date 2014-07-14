package analyze;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import automeasurer.Measurer;
import model.ImageDataModel;
import model.ImageTableModel;
import model.Status;

//foreløping navn
public class AnalyzerThreadsMonitor {
	
	private ImageTableModel model;
	private int nextElement;
	private PropertyChangeSupport pcs;
	private List<Analyzer> analyzerList;
	
	public AnalyzerThreadsMonitor(ImageTableModel model) {
		this.model = model;
		this.nextElement = 0;
	
		pcs = new PropertyChangeSupport(this);
		
		
	}
	
	
	public AnalyzerThreadsMonitor(ImageTableModel model, PropertyChangeListener listener) {
		this(model);
		pcs.addPropertyChangeListener(listener);
		
		
	}
	
	public void initThreads(int nrOfThreads) {
		if(this.analyzerList == null) {
			System.out.println("tester");
			pcs.firePropertyChange(Measurer.SETMAX, null, new Integer(this.model.getRowCount()));
			this.analyzerList = new ArrayList<Analyzer>();
	    	for(int i= 0; i< nrOfThreads; i++) {
	    		Analyzer a = new Analyzer(i, this);
	    		
	    		a.start();
	    		this.analyzerList.add(a);
	    	}
		}
		else {
			startThreads();
		}
	}
	
	public synchronized void startThreads() {
		for(Analyzer a: this.analyzerList) {
	
					a.resumeThread();
				
		}
	}
	public synchronized void pauseThreads() {
		for(Analyzer a: this.analyzerList) {
			try {
				a.pauseThread();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public synchronized int getNextElement() {
		int next = nextElement;
		if(next < this.model.getRowCount()) {
			nextElement++;
			pcs.firePropertyChange(Measurer.PROGRESS_UPDATE, null, new Integer(next));
			return next;
			
		}
		else {
			pcs.firePropertyChange(Measurer.FINISHED, null, null);
			return -1;
		}				
	}
	
	public ImageDataModel getImageDataModel(int i) {
		if(i<0) {
			return null;
		}
		return this.model.getDataModel(i);
	}
	
	public synchronized void setStatus(Status status, String error, int row) {
		this.model.setValueAt((Object)status, row, 0);
		this.model.setErrorColumn(row, error);
	}
	
	
}
