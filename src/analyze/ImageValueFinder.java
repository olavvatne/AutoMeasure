package analyze;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;

public class ImageValueFinder  {
	
	public static final int OW_HIGH = 3;
	public static final int OW_LOW = 2;
	public static final int OG_HIGH = 0;
	public static final int OG_LOW = 1;
	public static final int WHITE = 255;
	
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
		imp2 = crop(imp2, m.get(OW_LOW), m.get(OW_HIGH));
		
		List<Double> values = new ArrayList<Double>();
		double value1 = strictFindValue(imp, true, 120, m.get(OG_HIGH).getX());
		double value2 = strictFindValue(imp2, false, 120,  m.get(OW_LOW).getX());
			values.add(value1);
			values.add(value2);			
		return values;
	}
	
	/**
	 * The strictFindValue method assume that the image it gets is cropped and contain only the measurement area.
	 * I.E just the glass part of the image is present, and all rows is available to me tested.
	 * A number of rows will be tested, and the number is decided by sampleSize. After sampling, all
	 * sample values is sorted. From the sorted 
	 * @param imp Cropped image, of the surface to be measured
	 * @param left The direction the samples should be taken in. Left = true, Right = false
	 * @param sampleSize How many row should be sampled from the image surface.
	 * @return Return the average where invalid samples has been removed.
	 */
	
	private static double strictFindValue(ImagePlus imp, boolean left, int sampleSize, double toBeAdded) {
		IJ.run(imp, "8-bit", "");
		IJ.run(imp, "Smooth", "");
		IJ.run(imp, "Smooth", "");
		IJ.setAutoThreshold(imp,"Default"); 
		IJ.run(imp, "Convert to Mask", ""); 
		int[] samples = null;
		if(left) {
			samples = findSamplesReverse(imp, sampleSize);	
		}
		else {
			samples = findSamples(imp, sampleSize);			
		}
		Arrays.sort(samples);
		
		int firstOverNegative = -1;
		for ( int t = 0; t<samples.length; t++) {
			if(samples[t] > -1) {
				firstOverNegative = t;
				break;
			}
		}
		if(firstOverNegative == -1) {
			return -1;
		}
		
		int samplePrune = 0;
		if((samples.length-1 -samplePrune) -(firstOverNegative + samplePrune) <= 0) {
			//to few valid samples;
			return -1;
		}
		System.out.println("SAMPLES LEFT: " + ((samples.length-1 -samplePrune) - (firstOverNegative + samplePrune)));
		//Only if outliers is removed
		//int diff = samples[samples.length-1] - samples[firstOverNegative];
		//int maxDiff = (int)(2*diff*widthHeightRatio);
		//if(diff > maxDiff) {
			//return -1;
		//}
		int average = 0;
		int valid = 0;

		for ( int s = firstOverNegative+ samplePrune; s<samples.length- samplePrune; s ++ ) {
				valid ++;
				average += samples[s];
			
		}
		
		//TODO: Not removed outliers
		
	
			return (average/valid) + toBeAdded;			
		
	
	}
	
	//Bad code duplication, same code for reversal almost.
	private static int[] findSamples(ImagePlus imp, int sampleSize) {
		Random generator = new Random();
		int width = imp.getWidth();
		int height = imp.getHeight();
		int[] samples = new int[sampleSize];
		
		for (int i = 0; i<sampleSize; i++) {
			int counter = 0;
			int value = 0;
			int sampleRow = generator.nextInt(height);
			boolean sampleTaken = false;
			for (int j = 1; j<width; j++) {
				
				//Binary image so if sentence is valid. Count transition from white to black and black to white
				if (imp.getPixel(j, sampleRow)[0] != imp.getPixel(j-1, sampleRow)[0]) {
					if(!sampleTaken && imp.getPixel(j, sampleRow)[0] < WHITE) {
						//What pixel in the row the for loop is at now
						//If transition from black to white its go time.
						value = j;
						sampleTaken = true;
					}
					counter ++;
				}
			}
			
			if(counter <= 2 && sampleTaken) {
				samples[i] =value;
			}
			else {
				//All invalid samples with to many transitions is set to -1
				samples[i] = -1;
			}
		}
		return samples;
	}
	
	//Bad code duplication, same code for reversal almost.
		private static int[] findSamplesReverse(ImagePlus imp, int sampleSize) {
			Random generator = new Random();
			int width = imp.getWidth();
			int height = imp.getHeight();
			int[] samples = new int[sampleSize];
			
			for (int i = 0; i<sampleSize; i++) {
				int counter = 0;
				int value = 0;
				boolean sampleTaken = false;
				int sampleRow = generator.nextInt(height);
				for (int j = width -1; j>0; j--) {
					
					//Binary image so if sentence is valid. Count transition from white to black and black to white
					if (imp.getPixel(j, sampleRow)[0] != imp.getPixel(j+1, sampleRow)[0]) {
						if(!sampleTaken && imp.getPixel(j, sampleRow)[0] < WHITE) {
							//What pixel in the row the for loop is at now
							value = j;
							sampleTaken = true;
						}
						counter ++;
					}
				}
				
				if(counter <= 2 && sampleTaken) {
					samples[i] =value;
				}
				else {
					//All invalid samples with to many transitions is set to -1
					samples[i] = -1;
				}
			}
			return samples;
		}
	/**
	 * Crop will crop a image after p1 and p2. The height of the image is decided by an width height ratio that probably wont change often.
	 * The apparatus is often similar. Can be a value changed in the settings, or set in a calibrate panel. 
	 * @param img The image to be cropped.
	 * @param p1 The point where the image starts from the left.
	 * @param p2 Point where image should be cropped to the right.
	 * @return A cropped image.
	 */
	private static ImagePlus crop(ImagePlus img, ImageMarkerPoint p1,ImageMarkerPoint p2 ) {
		int x = (int)p1.getX();
		int x2 = (int) p2.getX();
		int w = Math.abs(x2-x);
		int h = (int) (w*widthHeightRatio); //height of apparatus //h can change so must have a setting or something
		int y = (int) p1.getY() -(h/2); 
		
		img.setRoi(new Rectangle(x,y,w,h ));
		IJ.run(img, "Crop", "");
		return img;
	}
	
}
