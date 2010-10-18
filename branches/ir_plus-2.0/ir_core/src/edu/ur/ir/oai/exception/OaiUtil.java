/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.oai.exception;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Nathan Sarr
 *
 */
public class OaiUtil
{
	
	/**
	 * Calculates offset from the current time zone and creates an OAI date also refered to
	 * as Zulu time
	 * 
	 * @param d
	 * @return the date as UTC (Coordinated Universal Time)  
	 * with the current hosts offset - this will be in the UTC format
	 * yyyy-MM-ddTHH:mm:ssZ
	 * 
	 */
	public static String zuluTime(Date d)
	{
		String strDate = "";
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(d);
		int utcOffsetInMinutes =  -(calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / (60 * 1000);
		calendar.add(Calendar.MINUTE, utcOffsetInMinutes);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		strDate = dateFormat.format(calendar.getTime());
		return strDate;
	}
}
