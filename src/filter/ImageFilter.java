package filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Filter for JFileChooser. Limit what file extensions are allowed to open.
 * @author Olav
 *
 */
public class ImageFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
 
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(tiff) ||
                extension.equals(tif) ||
                extension.equals(jpeg) ||
                extension.equals(jpg) ||
                extension.equals(png)) {
                    return true;
            } else {
                return false;
            }
        }
 
        return false;
    }
	
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Only images";
	}
	
	
	
	public final static String jpeg = "jpeg";
	public final static String jpg = "jpg";
	public final static String gif = "gif";
	public final static String tiff = "tiff";
	public final static String tif = "tif";
	public final static String png = "png";

	/*
	 * Get the extension of a file.
	 */
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 &&  i < s.length() - 1) {
			ext = s.substring(i+1).toLowerCase();
		}
		return ext;
	}
}

