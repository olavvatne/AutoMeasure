package utilities;

import java.util.Date;

public class DateUtil {
	public static int compareDate(Date date1, Date date2) {
	    //depricated but maybe convert to yodatime later
		date1.setSeconds(0);
	    date2.setSeconds(0);
		if (date1.equals(date2)) {
	      return 0 ;
	    } 
	    else if (date1.before(date2)) {
	      return -1 ;
	   }
	   else {
	     return 1 ;
	   }
	}
}
