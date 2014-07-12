package analyze;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;

public class ImageValueFinder  {
	
	public static final int OG_END = 0;
	public static final int OW_END = 2;
	public static final int OW_HIGH = 3;
	public static final int OW_LOW = 2;
	public static final int OG_HIGH = 0;
	public static final int OG_LOW = 1;
	
	//Photoshop measured ratio. TODO: Settings panel
	public static double widthHeightRatio = 0.16;
	
	/**
	 * GetValues opens the picture, and crop out the area of the picture between the markers.
	 * FindValue is run for each cropped picture area. The result is values that indicate where
	 * the seperation of oil/gas oil/water is... Basically clear and black.
	 * @param name picture address
	 * @param m list markers in the picture.
	 * @return A list of measurements of where the seperation of dark and light is.
	 */
	public static List<Double> getValues(String name, List<ImageMarkerPoint> m) {
		ImagePlus imp = new Opener().openImage(name);
		ImagePlus imp2 = imp.duplicate();
		imp = crop(imp, m.get(OG_HIGH), m.get(OG_LOW));
		imp2 = crop(imp2, m.get(OW_LOW), m.get(OG_HIGH));
		
		List<Double> values = new ArrayList<Double>();
		values.add(findValue(imp, true, 100)+ m.get(OG_END).getX());
		values.add(findValue(imp2, false, 100) + m.get(OW_END).getX());
		String s = IJ.freeMemory();
		
		return values;
	}
	
	/**
	 * The findValue method assume that the image it gets is cropped and contain only the measurement area.
	 * I.E just the glass part of the image is present, and all rows is available to me tested.
	 * A number of rows will be tested, and the number is decided by sampleSize. After sampling, all
	 * sample values is sorted. From the sorted 
	 * @param imp Cropped image, of the surface to be measured
	 * @param left The direction the samples should be taken in. Left = true, Right = false
	 * @param sampleSize How many row should be sampled from the image surface.
	 * @return Return the median.
	 */
	public static double findValue(ImagePlus imp, boolean left, int sampleSize) {
		IJ.run(imp, "8-bit", "");
		IJ.run(imp, "Smooth", "");
		IJ.run(imp, "Smooth", "");
		IJ.run(imp, "Make Binary", "");
		
		
		int[] samples = new int[sampleSize];
		
		int outlierSize = (int)(sampleSize*0.10);
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
	
	/**
	 * Crop will crop a image after p1 and p2. The height of the image is decided by an width height ratio that probably wont change often.
	 * The apparatus is often similar. Can be a value changed in the settings, or set in a calibrate panel. 
	 * @param img The image to be cropped.
	 * @param p1 The point where the image starts from the left.
	 * @param p2 Point where image should be cropped to the right.
	 * @return A cropped image.
	 */
	public static ImagePlus crop(ImagePlus img, ImageMarkerPoint p1,ImageMarkerPoint p2 ) {
		int x = (int)p1.getX();
		int x2 = (int) p2.getX();
		int w = x2-x;
		int h = (int) (w*widthHeightRatio); //height of apparatus //h can change so must have a setting or something
		int y = (int) p1.getY() -(h/2); 
		
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
