/**  
   Copyright 2008 - 2011 University of Rochester

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

package edu.ur.ir.user;

/**
 * Represents an error when sharing a file
 * 
 * @author Nathan Sarr
 *
 */
public class FileSharingException extends Exception{

	/** Eclipse generated id */
	private static final long serialVersionUID = -5898715228696933909L;
	
	/**
	 * Throw a file sharing exception with the given message
	 * 
	 * @param message
	 */
	public FileSharingException(String message)
	{
		super(message);
	}

}
