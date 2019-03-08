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

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class responsible for splitting up a combined series and report number
 * 
 * @author Nathan Sarr
 *
 */
public class SeriesReportNumberSplitter {
	
	/**  Logger */
	private static final Logger log = LogManager.getLogger(SeriesReportNumberSplitter.class);
	/**
	 * Creates a list of a series and report number.  The first value in the list
	 * is the series, the second value is the report number if one is found.
	 * 
	 * @param seriesReportNumber - series report number combination
	 * @param delimiter - delimiter used to seperate the series from the report number
	 * 
	 * @return - the series first value and report number second value.
	 */
	List<String> splitSeriesReport(String seriesReportNumber, String delimiter)
	{
		log.debug("Trhing to split series report number " + seriesReportNumber);
		LinkedList<String> splitSeriesReportNumber = new LinkedList<String>();
		
		if( seriesReportNumber != null && !seriesReportNumber.trim().equals(""))
		{
		    // split on the name
		    String[] seriesReportParts = seriesReportNumber.split(delimiter);
		
		    if(seriesReportParts.length  <= 1 )
		    {
			    splitSeriesReportNumber.add(seriesReportNumber);
		    }
		    else if( seriesReportParts.length >= 2)
		    {
		 	    splitSeriesReportNumber.add(seriesReportParts[0]);
			    splitSeriesReportNumber.add(seriesReportParts[1]);
		    }
		}
		
		return splitSeriesReportNumber;
		
	    
	}

}
