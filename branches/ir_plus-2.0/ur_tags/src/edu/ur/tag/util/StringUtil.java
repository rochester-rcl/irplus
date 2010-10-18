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


package edu.ur.tag.util;

/**
 * String utilities 
 * 
 * @author Nathan Sarr
 *
 */
public class StringUtil {
	
	/**
	 * Uses the string class replace all 
	 * 
	 * @param inString string to perform operation on
	 * @return - string with single quotes replaced with \'
	 */
	public static final String escapeSingleQuote(String inString)
	{
		String returnValue = inString.replaceAll("'", "\\\\'");
		return returnValue;
	}
	
	
	/**
	 * Main class for testing
	 * 
	 * @param args
	 */
	public static void main (String[] args)
	{
		String inString = args[0];
		System.out.println(StringUtil.escapeSingleQuote(inString));
	}

}
