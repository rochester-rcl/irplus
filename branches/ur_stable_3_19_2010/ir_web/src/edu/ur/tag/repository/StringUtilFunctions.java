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


package edu.ur.tag.repository;

/**
 * Set of string utility functions.
 * 
 * @author Nathan Sarr 
 *
 */
public class StringUtilFunctions {
	
	/**
	 * Determines if a string value is empty.  A null value will
	 * return true or if trimming the value equals ""
	 * 
	 * @param value - value to check
	 * @return
	 */
	public static boolean isEmpty(String value)
	{
		return value == null || value.trim().equals("");
	}

}
