package analyze;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;


public class ImageJTest {

	public static void main(String[] args) {
		//ImagePlus imp = IJ.openImage("C:/Users/Olav/Dropbox/iris/automaticMeasurer/Matlab/test3.jpg");  
		ImageMarkerFinder test = new ImageMarkerFinder();
		List<ImageMarkerPoint> markers =test.run("C:\\Users\\Olav\\Dropbox\\iris\\menisk\\DSC_0016.jpg");
		//imp.show();  
		for(ImageMarkerPoint point :markers) {
			System.out.println(point);
		}
	}

}
