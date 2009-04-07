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
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import java.util.Collections;

import edu.ur.persistent.CommonPersistent;

/**
 * This file database server is currently a top
 * level object that holds file databases
 * where all files will be put.
 * 
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultFileServer extends CommonPersistent implements FileServer{

	/**  Generated serial version id */
	private static final long serialVersionUID = -307751939970925673L;

	/**  The set of file databases in the system */
	private Set<DefaultFileDatabase> fileDatabases = new HashSet<DefaultFileDatabase>();
	
	/**
	 * Default Constructor
	 */
	DefaultFileServer(){}
	
	/**
	 * Create a file server with the specified name.
	 * 
	 * @param name
	 */
	public DefaultFileServer(String name){this.name = name;}
	
	
	/**
	 * Get an un modifiable set of the set of 
	 * file databases managed by this
	 * server.
	 * @return
	 */
	public Set<DefaultFileDatabase> getFileDatabases() {
		
		return Collections.unmodifiableSet(fileDatabases);
	}
	
	/**
	 * Find a file database by its name
	 * 
	 * @param name of the file database.
	 * @return the found file database or null
	 * if none exists.
	 */
	public DefaultFileDatabase getFileDatabase(String name) {
		DefaultFileDatabase db = null;
		boolean found = false;
		Iterator<DefaultFileDatabase> iter = fileDatabases.iterator();
		while (iter.hasNext() && !found)
		{
			DefaultFileDatabase temp = iter.next();
			if( temp.getName().equals(name) )
			{
				found = true;
			    db = temp;	
			}
		}
		return db;
	}

	/**
	 * Get the file database by the specified id.
	 * 
	 * @param id of the file database
	 * 
	 * @return the file database or null if no file database
	 * is found.
	 */
	public DefaultFileDatabase getFileDatabase(Long id) {
		DefaultFileDatabase db = null;
		boolean found = false;
		Iterator<DefaultFileDatabase> iter = fileDatabases.iterator();
		
		while (iter.hasNext() && !found)
		{
			DefaultFileDatabase temp = iter.next();
			if( temp.getId().equals(id) )
			{
				found = true;
			    db = temp;	
			}
		}
		return db;
	}

	/**
	 * Create a new file database  with the specified
	 * name and unique folder name.  
	 * 
	 * @param displayName name displayed to the user
	 * 
	 * @param name of the file database used when creating the 
	 *        dabase in the file system.
	 *        
	 * @param path of the file database
	 * 
	 * @return the created file database
	 */	
	public DefaultFileDatabase createFileDatabase(String displayName, String name, 
			String path, String description) {
		
		if( path == null )
		{
			throw new IllegalStateException("The path cannot be null");
		}
		
		if(name == null)
		{
			throw new IllegalStateException("the name cannot be null");
		}
		
		//create the new file database 
		DefaultFileDatabase FileDatabaseImpl = new DefaultFileDatabase();
		FileDatabaseImpl.setDisplayName(displayName);
		FileDatabaseImpl.setName(name);
		FileDatabaseImpl.setPath(path);
		FileDatabaseImpl.setFileServer(this);
		
		// create a folder for this file database
		FileSystemManager.createDirectory(FileDatabaseImpl.getFullPath());
		
		fileDatabases.add(FileDatabaseImpl);
		return FileDatabaseImpl;
	}
	
	/**
	 * Rename the file database.
	 * 
	 * @param oldName - old name of the file database
	 * @param name - new name to give the file database.
	 * 
	 * @return true if the the file database is renamed.
	 */
	public boolean renameFileDatabase(String currentName, String newName) {
		boolean renamed = false;
		DefaultFileDatabase fd = getFileDatabase(currentName);
		if (fileDatabases.contains(fd)) {

			// make sure the new folder name does not exist
			File f = new File(fd.getPath() + newName);
			if (!f.exists()) {
				if (!FileSystemManager.renameFile(
						new File(fd.getFullPath()), new File(fd.getPath()
								+ newName))) {
					throw new IllegalStateException("Could not rename file");
				}
				fileDatabases.remove(fd);
			}
			fd.setName(newName);
			fileDatabases.add(fd);
			
			renamed = true;
		}

		return renamed;
	}
	
	/**
	 * Deletes the file server and all databases belonging to it.
	 * 
	 * @return true if all file servers deleted.
	 */
	public boolean deleteFileServer()
	{
		boolean allDeleted = true;
		Iterator<DefaultFileDatabase> iter = fileDatabases.iterator();
		while (iter.hasNext() && allDeleted )
		{
			allDeleted = deleteDatabase(iter.next().getName());
		}
		return allDeleted;
	}
	
	/**
	 * Get the hash code
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		
		value += name == null ? 0 : name.hashCode();
		return value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof DefaultFileServer)) return false;

		final DefaultFileServer other = (DefaultFileServer) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;

		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[File Server id = ");
		sb.append(id);
		sb.append(" name = ");
		sb.append(name);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Helper method to delete a file Database.
	 * 
	 * @param name the name of the database to delete
	 * @return true if the file datbase is deleted.
	 */
	public boolean deleteDatabase(String name)
	{
		DefaultFileDatabase fd = getFileDatabase(name);
		boolean removed = false;
		
		if( fileDatabases.contains(fd))
		{
			FileSystemManager.deleteDirectory(fd.getFullPath());
		
			if(!fileDatabases.remove(fd))
			{
			    throw new IllegalStateException("Folder was deleted from file system" +
					   	    "but file database " + fd.getName() +
						    " could not be removed from database server");
			}
			else
			{
			    removed = true;
			}
		}
		return removed;
	}
}