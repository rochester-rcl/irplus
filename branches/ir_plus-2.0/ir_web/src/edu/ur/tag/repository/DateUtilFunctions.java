package edu.ur.tag.repository;

import edu.ur.ir.Date;
import edu.ur.ir.SimpleDateFormatter;

/**
 * Helps deal with date information where all information may not be available
 * 
 *  missing month or day.  
 *  
 * @author Nathan Sarr
 *
 */
public class DateUtilFunctions {

	/**
	 * Get the date in MM/dd/yyyy format
	 * 
	 * @param date in MM/dd/yyyy format.  Month and day can be missing.
	 * 
	 * If both month and day are missing yyyy is returned.  If day is missing
	 * MM/yyyy is returned.
	 * 
	 * @return
	 */
	public static String getSlashedDate(Date date)
	{
		return SimpleDateFormatter.getSlashedDate(date);
	}
}
