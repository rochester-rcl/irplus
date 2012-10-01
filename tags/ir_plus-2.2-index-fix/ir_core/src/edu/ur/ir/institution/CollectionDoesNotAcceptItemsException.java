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

package edu.ur.ir.institution;

/**
 * Error thrown when a collection does not accept items.
 * 
 * @author Nathan Sarr
 *
 */
public class CollectionDoesNotAcceptItemsException extends Exception {

	/** eclipse generated id  */
	private static final long serialVersionUID = -6536946494687897366L;
	
	public CollectionDoesNotAcceptItemsException(String message)
	{
		super(message);
	}

}
