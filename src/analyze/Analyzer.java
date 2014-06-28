
package analyze;

import java.util.List;

import model.ImageDataModel;
import model.Status;

public class Analyzer extends Thread {
	
	private int idNumber;
	private int elementToAnalyze;
	private AnalyzerThreadsMonitor mon;
	private boolean running = false;
	
	public Analyzer(int id, AnalyzerThreadsMonitor monitor) {
		this.idNumber = id;
		this.mon = monitor;
	}
	
	public void run() {
		this.running = true;
		elementToAnalyze = mon.getNextElement();
		ImageDataModel image = mon.getImageDataModel(elementToAnalyze);
		
		System.out.println("kjører" + elementToAnalyze);
		while(image != null) {
			//crude implementation of pausing thread. Should use some sort of monitor
			 while (!running) {
				 this.yield();
			 }
	               
			mon.setStatus(Status.WORKED_ON, elementToAnalyze); 
			
			
			List<ImageMarkerPoint> markers =ImageMarkerFinder.run(image.getFilePath());
			if(markers != null) {
				double[] values =ImageValueFinder.getValues(image.getFilePath(), markers);
				System.out.println("VALUES IN ANALYZER " + values[0] + " - " + values[1]);
				//image.setStatus(Status.SUCCESS);
				mon.setStatus(Status.SUCCESS, elementToAnalyze); 
				image.setMarkers(markers);
				image.setValues(values);
				System.out.println("funket");
			}
			else {
				//image.setStatus(Status.FAILURE);
				mon.setStatus(Status.FAILURE, elementToAnalyze); 
				System.out.println("*******");
				
				System.out.println("*******");
			}
			
			
		
			image = null;
			elementToAnalyze = mon.getNextElement();
			image = mon.getImageDataModel(elementToAnalyze);
		}
		mon = null;
		return;
		
	}
	
	public void pauseThread() throws InterruptedException {
		running = false;
	}

	public void resumeThread() {
		running = true;
	}
}
