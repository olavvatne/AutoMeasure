package analyze.value;

import ij.IJ;
import ij.ImagePlus;

import java.awt.Rectangle;
import java.util.List;

import analyze.ImageMarkerPoint;

public abstract class AbstractValueFinder {
	
	//TODO: Put in settings
	public static double widthHeightRatio = 0.16;
	
	protected boolean finished = false;
	protected boolean error = false;
	protected String errorMsg = "";
	public abstract List<Double> getValues(String name, List<ImageMarkerPoint> m);
	
	public boolean isCorrect() {
		if(isFinderSuccessful()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	protected abstract boolean isFinderSuccessful();
	/**
	 * Crop will crop a image after p1 and p2. The height of the image is decided by an width height ratio that probably wont change often.
	 * The apparatus is often similar. Can be a value changed in the settings, or set in a calibrate panel. 
	 * @param img The image to be cropped.
	 * @param p1 The point where the image starts from the left.
	 * @param p2 Point where image should be cropped to the right.
	 * @return A cropped image.
	 */
	protected ImagePlus crop(ImagePlus img, ImageMarkerPoint p1,ImageMarkerPoint p2 ) {
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
