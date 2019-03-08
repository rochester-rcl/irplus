/**  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  

package edu.ur.dspace.load;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helps with getting a dspace date into a format for processing.
 * 
 * @author Nathan Sarr
 *
 */
public class DspaceDateHelper {
	
	/**  Logger */
	private static final Logger log = LogManager.getLogger(DspaceDateHelper.class);

	
	/**
	 *  Handles a date in the format yyyy-MM-ddTHH:mm:ssZ
	 *  
	 * @param dspaceDate - dspace date to parse
	 * 
	 * @return gregorian calendar with the given date and time does not include time zone
	 * if the dspaceDate is null, returns a null gregorian Calendar
	 * @throws ParseException 
	 */
	public GregorianCalendar parseDate(String dspaceDate) throws ParseException
	{
		GregorianCalendar gregorianCalendar = null;
		
		if(dspaceDate == null)
		{
			return gregorianCalendar;
		}
		log.debug("Tring to parse date " + dspaceDate);
		// split on the name
		String[] dateParts = dspaceDate.split("T");
		
		log.debug( " dateParts.length = " + dateParts.length); 
	
		if( dateParts.length >= 2 )
		{
		     String yearMonthDay = dateParts[0];
		    //remove the Z
		    String hourMinuteSecond = dateParts[1].substring(0, dateParts[1].length() -1);
		
		    String dateAndTime = yearMonthDay + " " +  hourMinuteSecond;
		    log.debug("parsing date " + dateAndTime);
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    gregorianCalendar = new GregorianCalendar();
		    gregorianCalendar.setTime(simpleDateFormat.parse(dateAndTime));
		}
		
		return gregorianCalendar;
	}

}
