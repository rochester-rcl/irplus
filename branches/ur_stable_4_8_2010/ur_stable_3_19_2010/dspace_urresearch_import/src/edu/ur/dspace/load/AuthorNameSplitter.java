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

import edu.ur.ir.person.PersonNameAuthority;

/**
 * This should be able to take a name and create a UrResearch person
 * 
 * @author Nathan Sarr
 *
 */
public interface AuthorNameSplitter {
	
	
	/**
	 * Creates a person name based on the given author name
	 * 
	 * @param authorName
	 * @return the created person name
	 */
	PersonNameAuthority splitName(String authorName);

}
