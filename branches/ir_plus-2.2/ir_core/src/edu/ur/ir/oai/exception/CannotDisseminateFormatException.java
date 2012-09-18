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

/**
 * Represents the metadata format identified by the value given for the
 * argument is not supported by the item or by the repository
 * 
 * @author Nathan Sarr
 *
 */
public class CannotDisseminateFormatException extends Exception{

	/** eclipse generated id */
	private static final long serialVersionUID = 1839068732692944803L;
	
	/**
	 * Default constructor
	 * 
	 * @param format - format that cannot be disseminated
	 */
	public  CannotDisseminateFormatException(String format)
	{
		super("OAI error cannot disseminate format: " + format);
	}

}
