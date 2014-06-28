package filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;


public class ExcelFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
 
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(xls)) {
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
		return "Only excel-files";
	}
	
	
	
	public final static String xls = "xls";

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

