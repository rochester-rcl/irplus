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
import edu.ur.file.db.FileDatabaseDAO;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.TreeFolderInfo;
import edu.ur.file.db.TreeFolderInfoDAO;
import edu.ur.file.db.UniqueNameGenerator;

/**
 * Strategy that only stores a certain amount of files per folder.  Once
 * the limit is reached a new default folder is created in the file
 * database.
 * 
 * @author Nathan Sarr
 *
 */
public class MaxFilesStoreStrategy implements 
    DefaultDatabaseFileStoreStrategy{

	/** Tree folder info database access  */
	private TreeFolderInfoDAO treeFolderInfoDAO;
	
	/** Maximum number of files to store in a folder before creating a 
	 * new folder default is 1000. 
	 */
	private Long maxNumberOfFilesPerFolder = 1000l;
	
	/** generates unique names for the files and folders */
	private UniqueNameGenerator uniqueNameGenerator;
	
	/** File database data access  */
	private FileDatabaseDAO fileDatabaseDAO;


	/**
	 * Stores the file directly into the file database.
	 * 
	 * @see edu.ur.file.db.DefaultDatabaseFileStoreStrategy#addFile(edu.ur.file.db.DefaultFileDatabase, java.io.File)
	 */
	public DefaultFileInfo addFile(DefaultFileDatabase fileDatabase, 
			File f, String unqiueFileName) {
		
		if( isTooBig(fileDatabase))
		{
			createNewDatabaseFolder(fileDatabase);
		}
		return fileDatabase.addFile(f, unqiueFileName);
	}
	
	/**
	 * Create an empty file with the strategy.
	 * 
	 * @see edu.ur.file.db.DefaultDatabaseFileStoreStrategy#addFile(edu.ur.file.db.DefaultFileDatabase, java.lang.String)
	 */
	public DefaultFileInfo addFile(DefaultFileDatabase fileDatabase,
			String unqiueFileName) {
		
		if( isTooBig(fileDatabase))
		{
		    createNewDatabaseFolder(fileDatabase);
		}
		return fileDatabase.addFile(unqiueFileName);
	}
	
	/**
	 * Determines if the file datbase folder has too many files within it.
	 * 
	 * @param fileDatabase - file database to check
	 * @return true if too many files are within the folder
	 */
	private boolean isTooBig(DefaultFileDatabase fileDatabase)
	{
		TreeFolderInfo folder = fileDatabase.getCurrentFileStore();
		Long count = treeFolderInfoDAO.getFileCount(folder.getId());
		
		// if the count is greater than or equal to the maximum
		// number of files then we need to create a new folder
		if( count.compareTo( maxNumberOfFilesPerFolder) >= 0 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	/**
	 * Create a new database folder and set it as the current file store
	 * @param fileDatabase
	 * @throws LocationAlreadyExistsException - if the location already exists
	 */
	private void createNewDatabaseFolder(DefaultFileDatabase fileDatabase) 
	{
		String nextName = uniqueNameGenerator.getNextName();
		TreeFolderInfo folder;
		try {
			folder = fileDatabase.createRootFolder(nextName, nextName);
		} catch (LocationAlreadyExistsException e) {
			throw new IllegalStateException("file database location already exists", e);
		}
		folder.setDescription("Created by MaxFilesStoreStrategy - max file reached");
		fileDatabase.setCurrentFileStore(nextName);
		treeFolderInfoDAO.makePersistent(folder);
		fileDatabaseDAO.makePersistent(fileDatabase);
	}


	/**
	 * Get the maximum number of files per folder allowed
	 * 
	 * @return
	 */
	public Long getMaxNumberOfFilesPerFolder() {
		return maxNumberOfFilesPerFolder;
	}


	/**
	 * Set the maximum nuber of files per folder allowed.
	 * 
	 * @param maxNumberOfFilesPerFolder
	 */
	public void setMaxNumberOfFilesPerFolder(Long maxNumberOfFilesPerFolder) {
		this.maxNumberOfFilesPerFolder = maxNumberOfFilesPerFolder;
	}

	/**
	 * Get the tree folder info Data Access Object
	 * 
	 * @return
	 */
	public TreeFolderInfoDAO getTreeFolderInfoDAO() {
		return treeFolderInfoDAO;
	}


	/**
	 * Set the tree folder info data access object.
	 * 
	 * @param treeFolderInfoDAO
	 */
	public void setTreeFolderInfoDAO(TreeFolderInfoDAO treeFolderInfoDAO) {
		this.treeFolderInfoDAO = treeFolderInfoDAO;
	}
	
	/**
	 * Get the unique name generator.
	 * 
	 * @return
	 */
	public UniqueNameGenerator getUniqueNameGenerator() {
		return uniqueNameGenerator;
	}

	/**
	 * Set the unique name generator.
	 * 
	 * @param uniqueNameGenerator
	 */
	public void setUniqueNameGenerator(UniqueNameGenerator uniqueNameGenerator) {
		this.uniqueNameGenerator = uniqueNameGenerator;
	}

	/**
	 * Get the file database object.
	 * 
	 * @return
	 */
	public FileDatabaseDAO getFileDatabaseDAO() {
		return fileDatabaseDAO;
	}

	/**
	 * Set the file database data access object.
	 * 
	 * @param fileDatabaseDAO
	 */
	public void setFileDatabaseDAO(FileDatabaseDAO fileDatabaseDAO) {
		this.fileDatabaseDAO = fileDatabaseDAO;
	}



}
