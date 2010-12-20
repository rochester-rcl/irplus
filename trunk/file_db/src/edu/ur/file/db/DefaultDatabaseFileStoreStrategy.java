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

import java.io.File;
import java.io.Serializable;

import edu.ur.file.IllegalFileSystemNameException;

/**
 * Allows different file storing strategies for a 
 * default file databases.  This could be to store
 * a certain number of files in a folder etc.
 *  
 * @author Nathan Sarr
 *
 */
public interface DefaultDatabaseFileStoreStrategy extends Serializable{
	
	
	/**
	 * Store the file in the file database with the 
	 * given strategy.
	 * 
	 * @param fileDatabase - file database to store the file in
	 * @param f - file to store
	 * @param uniqueFileName - name to give the file
	 * 
	 * @return - the stored file
	 * @throws IllegalFileSystemNameException - if the file name is illegal for ANY type of file system
	 * @throws LocationAlreadyExistsException - if the location already exists
	 */
	public DefaultFileInfo addFile(DefaultFileDatabase fileDatabase, 
			File f, String unqiueFileName) throws IllegalFileSystemNameException;
	
	/**
	 * Create an <b>empty</b> the file database with the 
	 * given strategy.
	 * 
	 * @param fileDatabase - file database to store the file in
	 * @param uniqueFileName - name to give the empty file
	 * 
	 * @return - the stored file
	 * @throws IllegalFileSystemNameException - if the file name is illegal
	 * @throws LocationAlreadyExistsException - if the location already exists
	 */
	public DefaultFileInfo addFile(DefaultFileDatabase fileDatabase, 
			 String unqiueFileName) throws IllegalFileSystemNameException;

}
