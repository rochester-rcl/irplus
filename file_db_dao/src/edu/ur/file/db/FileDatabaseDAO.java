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

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;

/**
 * Persist the file database to the database
 * 
 * @author Nathan Sarr
 *
 */
public interface FileDatabaseDAO extends CountableDAO, 
CrudDAO<FileDatabase>, NameListDAO
{
	/**
	 * Find a file database by it's file system name for the 
	 * specified file server.
	 * 
	 * @param name - name of the database
	 * @param fileServerId - file server id to look in
	 * @return
	 */
	public FileDatabase findByName(String name, Long fileServerId);
	
	/**
	 * Find a file datbase by it's display name for the specified file
	 * server
     *
	 * @param name - name of the file database
	 * @param fileServerId - id of the file server to look in
	 * @return
	 */
	public FileDatabase findByDisplayName( String name, Long fileServerId);
}
