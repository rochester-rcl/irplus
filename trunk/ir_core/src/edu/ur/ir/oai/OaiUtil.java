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

package edu.ur.ir.oai;

import java.text.ParseException;
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
	/** oai verbs **/
	public static final String IDENTIFY_VERB = "Identify";
	public static final String LIST_METADATA_FORMATS_VERB = "ListMetadataFormats";
	public static final String LIST_SETS_VERB = "ListSets";
	public static final String GET_RECORD_VERB = "GetRecord";
	public static final String LIST_IDENTIFIERS_VERB = "ListIndentifiers";
	public static final String LIST_RECORDS_VERB = "ListRecords";
	
	public static final String longFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String shortFormat = "yyyy-MM-dd";

	
	/**
	 * Determine if the oai verb is valid.  This method ignores case.
	 * 
	 * @param verb - verb to check
	 * @return true if the verb is valid
	 */
	public static boolean isValidOaiVerb(String verb)
	{
		if( verb == null ) return false;
		if( verb.equalsIgnoreCase(GET_RECORD_VERB)) return true;
		if( verb.equalsIgnoreCase(IDENTIFY_VERB)) return true;
		if( verb.equalsIgnoreCase(LIST_IDENTIFIERS_VERB)) return true;
		if( verb.equalsIgnoreCase(LIST_METADATA_FORMATS_VERB)) return true;
		if( verb.equalsIgnoreCase(LIST_SETS_VERB)) return true;
		if( verb.equalsIgnoreCase(LIST_RECORDS_VERB)) return true;
		
		return false;
	}
	
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
		SimpleDateFormat longDateFormat = new SimpleDateFormat(longFormat);
		String strDate = "";
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(d);
		int utcOffsetInMinutes =  -(calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / (60 * 1000);
		calendar.add(Calendar.MINUTE, utcOffsetInMinutes);
		strDate = longDateFormat.format(calendar.getTime());
		return strDate;
	}
	
	/**
	 * Get the current time based off of zulu date.
	 * 
	 * @param zuluDate - the zulu date time used
	 * @return the current date time with no offset
	 */
	public static Date currentTime(Date zuluDate)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(zuluDate);
		int utcOffsetInMinutes =  (calendar.get(Calendar.ZONE_OFFSET) - calendar.get(Calendar.DST_OFFSET)) / (60 * 1000);
		calendar.add(Calendar.MINUTE, utcOffsetInMinutes);
		return calendar.getTime();
	}
	
	/**
	 * Get the date - throws an illegal state exception if it is not one of the
	 * acceptable date values.
	 * 
	 * @param value
	 * @return
	 */
	public static Date getDate(String value)
	{
		SimpleDateFormat longDateFormat = new SimpleDateFormat(longFormat);
		SimpleDateFormat shortDateFormat = new SimpleDateFormat(shortFormat);

		Date d = null;
			try {
			 d = longDateFormat.parse(value);
		} catch (ParseException e) {
			 try {
				d = shortDateFormat.parse(value);
			} catch (ParseException e1) {
				throw new IllegalStateException("Could not parse from date " + value);
			}
		}
		return d;
	}
	
	/**
	 * Get the date - throws an illegal state exception if it is not one of the
	 * acceptable date values.
	 * 
	 * @param value
	 * @return
	 */
	public static String getLongDateFormat(Date d)
	{
		SimpleDateFormat longDateFormat = new SimpleDateFormat(longFormat);
		return longDateFormat.format(d);
	}
}
