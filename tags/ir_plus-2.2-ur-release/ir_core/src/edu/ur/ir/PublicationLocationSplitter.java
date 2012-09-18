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


package edu.ur.ir;

/**
 * Publication location splitter.
 * 
 * @author Nathan Sarr
 *
 */
public interface PublicationLocationSplitter {

	/**
	 * Will split a Location / publisher combination into two
	 * parts Location and Publisher.  The array will always
	 * be of size 2.  An empty string will be returned if
	 * no location or publisher is returned.
	 *  
	 *  Location will always be in location 0 and Publisher
	 *  will always be in location 1.
	 *  
	 * @param publisher - Location/Publisher
	 * @return
	 */
	public String[] split(String publisher);
}
