/**  
   Copyright 2008 - 2010 University of Rochester

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

import java.io.File;
import java.io.Serializable;

/**
 * This will re-index all users
 * 
 * @author Nathan Sarr
 *
 */
public interface ReIndexUserService extends Serializable{
	
	/**
	 * Re-Index the users in the institutional repository
	 * 
	 * @param batchSize - number of users to index at a time
	 * @param userIndexFolder - location of the user index
	 * @return total number of users processed.
	 */
	public int reIndexUsers(int batchSize, File userIndexFolder);


}
