
package analyze;

import java.util.ArrayList;
import java.util.List;

import model.ImageDataModel;
import model.Status;

public class Analyzer extends Thread {
	
	private int idNumber;
	private int elementToAnalyze;
	private AnalyzerThreadsMonitor mon;
	private FaultDetector fd;
	private boolean running = false;
	
	public Analyzer(int id, AnalyzerThreadsMonitor monitor) {
		this.idNumber = id;
		this.mon = monitor;
		this.fd = new FaultDetector();
	}
	
	public void run() {
		this.running = true;
		elementToAnalyze = mon.getNextElement();
		ImageDataModel image = mon.getImageDataModel(elementToAnalyze);
		
		System.out.println("kjører" + elementToAnalyze);
		while(image != null) {
			//crude implementation of pausing thread. Should use some sort of monitor
			 while (!running) {
				 //Super crude. Busy waiting.
				 Thread.yield();
			 }
	               
			mon.setStatus(Status.WORKED_ON, elementToAnalyze); 
			
			
			List<ImageMarkerPoint> markers =ImageMarkerFinder.run(image.getFilePath());
			if(fd.isMarkersCorrect(markers)) {
				List<Double> values =ImageValueFinder.getValues(image.getFilePath(), markers);
				if(fd.isValuesCorrect(values)) {
					mon.setStatus(Status.SUCCESS, elementToAnalyze); 
					//TODO: Convert from using double array to a arraylist
					double[] arrVal = new double[values.size()];
					for(int i= 0; i<arrVal.length; i++) {
						arrVal[i] = values.get(i);
					}
					image.setValues(arrVal);
				}
				System.out.println("VALUES IN ANALYZER " + values.get(0) + " - " + values.get(1));
				//image.setStatus(Status.SUCCESS);
				//TODO: Use previous values or something.
				
				image.setMarkers(markers);
				
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
