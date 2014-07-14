package analyze;

import ij.*;
import ij.measure.ResultsTable;
import ij.process.*;
import ij.gui.*;
import ij.io.Opener;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ij.plugin.*;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.frame.*;



public class ImageMarkerFinder {
	
	private static final int minHue = 65, minSat = 77, minBri = 21;
	private static final int maxHue = 129, maxSat = 255, maxBri = 192;
	
	public static List<ImageMarkerPoint> run(String name) {
		
		ImagePlus imp = new Opener().openImage(name);
		ImageStack stack = imp.getStack();
		
		int width = stack.getWidth();
		int height = stack.getHeight();
		int numPixels = width*height;
		
		ImageProcessor mask = new ByteProcessor(width, height);
		imp.setProperty("Mask", mask);
		
		
		byte[] hSource = new byte[numPixels];
		byte[] sSource = new byte[numPixels];
		byte[] bSource = new byte[numPixels];
		
		ImageProcessor ip = imp.getProcessor();
		ColorProcessor cp = (ColorProcessor)ip;
		
		cp.getHSB(hSource,sSource,bSource);
		
		ImageProcessor fillMaskIP = (ImageProcessor)imp.getProperty("Mask");
		if (fillMaskIP==null) return null;
		byte[] fillMask = (byte[])fillMaskIP.getPixels();
		byte fill = (byte)255;
		byte keep = (byte)0;
		for (int j = 0; j < numPixels; j++){
			int hue = hSource[j]&0xff;
			int sat = sSource[j]&0xff;
			int bri = bSource[j]&0xff;
			if (((hue < minHue)||(hue > maxHue)) || ((sat < minSat)||(sat > maxSat)) || ((bri < minBri)||(bri > maxBri))) {
				fillMask[j] = keep;
			}
			else {
				fillMask[j] = fill;
			}
		}
		
		
		int[] pixels = (int[])ip.getPixels();
		int fcolor = Prefs.blackBackground?0xffffffff:0xff000000;
		int bcolor = Prefs.blackBackground?0xff000000:0xffffffff;
		for (int i=0; i<numPixels; i++) {
			if (fillMask[i]!=0)
				pixels[i] = fcolor;
			else
				pixels[i]= bcolor;
		}
		IJ.run(imp, "Make Binary", "");
		IJ.run(imp, "Close", "");
		IJ.run(imp, "Fill Holes", "");
		IJ.run(imp, "Erode", "");
		IJ.run(imp, "Erode", "");
		IJ.run(imp, "Erode", "");
		IJ.run(imp, "Erode", "");
		
		//IJ.run(imp, "Analyze Particles...", "size=0-Infinity circularity=0.00-1.00 show=Outlines clear add");
		CustomParticleAnalyzer anal = new CustomParticleAnalyzer();
		anal.setup("", imp);
		
		anal.analyze(imp);
		
		ResultsTable table = anal.getResultsTable();

		if(table == null) {
			System.out.println("null");
		}
		List<ImageMarkerPoint> markers = new ArrayList<ImageMarkerPoint>();
		for(int i = 0; i<table.getCounter(); i++) {
			ImageMarkerPoint p =new ImageMarkerPoint(table.getValueAsDouble(ResultsTable.X_CENTER_OF_MASS, i), 
					table.getValueAsDouble(ResultsTable.Y_CENTER_OF_MASS, i),
					table.getValueAsDouble(ResultsTable.AREA, i) );
			markers.add(p);
		}
		Collections.sort(markers, new Comparator<ImageMarkerPoint>() {

			@Override
			public int compare(ImageMarkerPoint o1, ImageMarkerPoint o2) {
				//obs: markører må være langt i fra hverandre
				return (int)o1.getX()-(int)o2.getX();
			}
			
		});
		
		imp.flush();
		table.reset();
		imp = null;
		String s = IJ.freeMemory();
		//System.out.println(s + " FREEE MEMORY");
		
		//en eller annen referanse blir ikke slettet. GB er teit
		if(markers.size() ==4) {
			return markers;
		}
		else {
			//sjekk for mer enn 4 punkter der man ser på area og bestemmer om man kanskje har noe støv eller noe.
			return null;
		}
		
	}
	
	
}
