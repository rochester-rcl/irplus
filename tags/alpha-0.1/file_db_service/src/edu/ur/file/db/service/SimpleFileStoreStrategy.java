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


package edu.ur.file.db.service;

import java.io.File;

import edu.ur.file.db.DefaultDatabaseFileStoreStrategy;
import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.DefaultFileInfo;

/**
 * Stores a file directly into the file database 
 * 
 * @author Nathan Sarr
 *
 */
public class SimpleFileStoreStrategy implements DefaultDatabaseFileStoreStrategy{

	
	/**
	 * Stores the file directly into the file database.
	 * 
	 * @see edu.ur.file.db.DefaultDatabaseFileStoreStrategy#addFile(edu.ur.file.db.DefaultFileDatabase, java.io.File)
	 */
	public DefaultFileInfo addFile(DefaultFileDatabase fileDatabase, 
			File f, String unqiueFileName) {
		
		return fileDatabase.addFile(f, unqiueFileName);
	}

	
	/**
	 * Create an emtyp file with the strategy.
	 * 
	 * @see edu.ur.file.db.DefaultDatabaseFileStoreStrategy#addFile(edu.ur.file.db.DefaultFileDatabase, java.lang.String)
	 */
	public DefaultFileInfo addFile(DefaultFileDatabase fileDatabase,
			String unqiueFileName) {
		return fileDatabase.addFile(unqiueFileName);
	}

}
