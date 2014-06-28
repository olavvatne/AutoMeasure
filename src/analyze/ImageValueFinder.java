package analyze;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;

public class ImageValueFinder  {
	
	public static final int OG_END = 0;
	public static final int OW_END = 2;
	
	public static double[] getValues(String name, List<ImageMarkerPoint> m) {
		ImagePlus imp = new Opener().openImage(name);
		ImagePlus imp2 = imp.duplicate();
		imp = crop(imp, m.get(OG_END), m.get(1));
		imp2 = crop(imp2, m.get(OW_END), m.get(3));
		
		double[] values = new double[2];
		values[0] =findValue(imp, true, 100)+ m.get(OG_END).getX();
		values[1] =findValue(imp2, false, 100) + m.get(OW_END).getX();
		String s = IJ.freeMemory();
		
		return values;
	}
	
	
	public static double findValue(ImagePlus imp, boolean left, int sampleSize) {
		IJ.run(imp, "8-bit", "");
		IJ.run(imp, "Smooth", "");
		IJ.run(imp, "Smooth", "");
		IJ.run(imp, "Make Binary", "");
		
		int[] samples = new int[sampleSize];
		int yStart = (imp.getHeight()/2)-50;
		//Må nok forbedre denne metoden, se kommenterte kode under.
		for (int i = 0; i<sampleSize; i++) {

			int j;
			for (j = 0; j<imp.getWidth(); j++) {
				if (imp.getPixel(j, yStart)[0]<255) {
					break;
				}
			}
			samples[i] =j;
			yStart ++;
		}
		Arrays.sort(samples);
		
		
		return (( samples[sampleSize/2] + samples[(sampleSize/2)-1] ) / 2);
	}
	
	
	public static ImagePlus crop(ImagePlus img, ImageMarkerPoint p1,ImageMarkerPoint p2 ) {
		int x = (int)p1.getX();
		int y = (int) p1.getY() -200; //må vite mål ut ifra en jpanel i starten. CalibratePanel eller noe.
		int w = (int) p2.getX()-(int)p1.getX();
		int h = 400;
		
		img.setRoi(new Rectangle(x,y,w,h ));
		IJ.run(img, "Crop", "");
		
		return img;
	}
	
	/*public static double findValue(ImagePlus imp, boolean left, int sampleSize) {
		IJ.run(imp, "8-bit", "");
		IJ.run(imp, "Gaussian Blur...", "sigma=2");
		IJ.run(imp, "Convolve...", "text1=[ -1 0 1 \n -2 0 2 \n -1 0 1 \n] normalize");
		IJ.run(imp, "Make Binary", "");
		IJ.run(imp, "Close-", "");
		IJ.run(imp, "Erode", "");
		IJ.run(imp, "Erode", "");
		
		//imp.show();
		
		int[] samples = new int[sampleSize];
		int yStart = (imp.getHeight()/2)-50;
		//System.out.println("YSTART ________________----------" + yStart);
		for (int i = 0; i<sampleSize; i++) {
			int samplePos = 0;
			int lastSample = 0;
			//finn bedre måte å ta mål på
			for ( int j = 0; j<imp.getWidth(); j++) {
				if(imp.getPixel(j, yStart)[0] < imp.getPixel(samplePos, samplePos)[0]) {
					break;
				}
				else {
					samplePos = j;
					lastSample =imp.getPixel(j, yStart)[0];
				}
				
			}
			System.out.println(samplePos);
			yStart ++;
			samples[i] = samplePos;
		}
		Arrays.sort(samples);
		
		return ( samples[sampleSize/2] + samples[(sampleSize/2)-1] ) / 2;
	}*/
}
