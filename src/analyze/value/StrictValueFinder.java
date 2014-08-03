package analyze.value;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import analyze.ImageMarkerPoint;

public class StrictValueFinder extends AbstractValueFinder {
	
	public static final int OW_HIGH = 3;
	public static final int OW_LOW = 2;
	public static final int OG_HIGH = 0;
	public static final int OG_LOW = 1;
	public static final int WHITE = 255;
	
	/**
	 * GetValues opens the picture, and crop out the area of the picture between the markers.
	 * FindValue is run for each cropped picture area. The result is values that indicate where
	 * the seperation of oil/gas oil/water is... Basically clear and black.
	 * @param name picture address
	 * @param m list markers in the picture.
	 * @return A list of measurements of where the seperation of dark and light is.
	 */
	@Override
	public List<Double> getValues(String name, List<ImageMarkerPoint> m) {
		ImagePlus imp = new Opener().openImage(name);
		ImagePlus imp2 = imp.duplicate();
		imp = this.crop(imp, m.get(OG_HIGH), m.get(OG_LOW));
		imp2 = this.crop(imp2, m.get(OW_LOW), m.get(OW_HIGH));
		
		List<Double> values = new ArrayList<Double>();
		double value1 = findValue(imp, true, 150, m.get(OG_HIGH).getX());
		double value2 = findValue(imp2, false, 150,  m.get(OW_LOW).getX());
			values.add(value1);
			values.add(value2);			
		return values;
	}
	
	private double findValue(ImagePlus imp, boolean left, int sampleSize, double toBeAdded) {
		IJ.run(imp, "8-bit", "");
		IJ.run(imp, "Smooth", "");
		IJ.run(imp, "Smooth", "");
		IJ.setMinAndMax(imp, 126, 128);
		IJ.setAutoThreshold(imp,"Default"); 
		IJ.run(imp, "Convert to Mask", ""); 
		int[] samples = null;	
		samples = findSamples(imp, sampleSize, left);			
		Arrays.sort(samples);
		
		int firstOverNegative = -1;
		for ( int t = 0; t<samples.length; t++) {
			if(samples[t] > -1) {
				firstOverNegative = t;
				break;
			}
		}
		if(firstOverNegative == -1) {
			errorMsg += "No valid samples. ";
			return -1;
		}
		int validSamples = samples.length-1 - firstOverNegative;
		int samplePrune = (int)Math.floor(validSamples*0.25);
		if((samples.length-1 -samplePrune) -(firstOverNegative + samplePrune) <= 0) {
			//to few valid samples;
			this.errorMsg += "To few valid samples. ";
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
	
	/**
	 * Find sample will traverse the image sampleSize times and extract the
	 * seperation point as best as it can, and return an array containing measurements, some
	 * invalid, some not accurate etc. But most of them hopefully correct.
	 * @param imp image of seperator, (menisk)
	 * @param sampleSize The amount of row that should be tested
	 * @param left - If samples should be collected by traversing the image in the left direction
	 * @return an int array of samples.
	 */
	private int[] findSamples(ImagePlus imp, int sampleSize, boolean left) {
		Random generator = new Random();
		int width = imp.getWidth();
		int height = imp.getHeight();
		int[] samples = new int[sampleSize];
		
		for (int i = 0; i<sampleSize; i++) {
			int sampleRow = generator.nextInt(height);
			if(left) {
				samples[i] = getSampleReverse(imp, sampleRow, width);	
			}
			else {
				samples[i] = getSample(imp, sampleRow, width);			
			}
		}
		return samples;
	}
	
	/**
	 * Get a single sample in the right direction
	 * @param imp
	 * @param sampleRow
	 * @param width
	 * @return
	 */
	private static int getSample(ImagePlus imp, int sampleRow, int width) {
		int counter = 0;
		int value = 0;
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
		
		if(sampleTaken) {
			return value;
		}
		else {
			//All invalid samples with to many transitions is set to -1
			return -1;
		}
	}
	
	/**
	 * Geta sample in the left direction
	 * @param imp
	 * @param sampleRow
	 * @param width
	 * @return
	 */
	private static int getSampleReverse(ImagePlus imp, int sampleRow, int width) {
		int counter = 0;
		int value = 0;
		boolean sampleTaken = false;
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
		
		if(sampleTaken) {
			return value;
		}
		else {
			//All invalid samples with to many transitions is set to -1
			return -1;
		}
	}
	
	

			@Override
			protected boolean isFinderSuccessful() {
				// TODO Auto-generated method stub
				return false;
			}
}
