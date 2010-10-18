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
package edu.ur.file.db;

import edu.ur.net.UriAware;
import edu.ur.simple.type.NameAware;

/**
 * A file system object that has a path
 *  
 * @author Nathan Sarr
 *
 */
public interface PathAware extends UriAware, NameAware {
	
	/**
	 * Returns the root of the path of this file system
	 * object - C:/ or ~/
	 * 
	 * @return the root of the path
	 */
	public String getPrefix();
	
	/**
	 * Get the path 
	 * 
	 * @return the path
	 */
	public String getPath();	
	
	/**
	 * Get the full path this includes its file.
	 * 
	 * @return full path
	 */
	public String getFullPath();
	
}
