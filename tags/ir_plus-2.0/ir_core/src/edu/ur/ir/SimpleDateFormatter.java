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

package edu.ur.ir;

/**
 * Takes a date and returns a string output.
 * 
 * @author Nathan Sarr
 *
 */
public class SimpleDateFormatter {
	
	/**
	 * Convert the date into a format of Month:XX Day:XX Year:XXXX
	 * 
	 * @param date
	 * @return
	 */
	public String getDate(Date date)
	{
		String value = "";
		
		if( date.getMonth() != 0 )
		{
			value = "Month: " + date.getMonth() + " ";
		}
		if( date.getDay() != 0)
		{
			value += "Day: " + date.getDay() + " ";
		}
		if( date.getYear() != 0)
		{
			value += "Year: " + date.getYear() + " ";
		}
		return value;
		
	}

}
