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

package edu.ur.ir.user;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.repository.Repository;

/**
 * Service for dealing with user file system.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserFileSystemService {
	
	/** Default id for the root folder for each users*/
	public static final long ROOT_FOLDER_ID = 0L;

	
	/**
	 * Find the folder for the specified userId and specified 
	 * folder name.
	 * 
	 * @param name - name of the root personal folder
	 * @param userId - id of the user
	 * 
	 * @return - the found personal folder or null if no folder found.
	 */
	public PersonalFolder getRootPersonalFolder(String name, Long userId);
	
	/**
	 * Find the personal folder for the specified folder name and 
	 * parent id.
	 * 
	 * @param name of the folder
	 * @param parentId - id of the parent folder
	 * 
	 * @return the found folder or null if the folder is not found.
	 */
	public PersonalFolder getPersonalFolder(String name, Long parentId);

	/**
	 * Get the personal folders for the specified user.
	 * 
	 * @param userId
	 * @return
	 */
	public List<PersonalFolder> getAllPersonalFoldersForUser(Long userId);
	
	/**
	 * Gets the path to the collection starting from the top parent all the way
	 * down to the specified child.  Only includes parents of the specified 
	 * collection.  The list is ordered highest level parent to last child.  This
	 * is useful for displaying the path to a given collection.
	 * 
	 * @param collection 
	 * @return list of parent collections.
	 * 
	 */
	public List<PersonalFolder> getPersonalFolderPath(Long personalFolderId);

	/**
	 * Get the personal folder by id.
	 * 
	 * @param id - id of the personal folder
	 * @param lock - upgrade the lock mode  
	 * @return - the found folder or null if not folder is found.
	 */
	public PersonalFolder getPersonalFolder(Long id, boolean lock);

	/**
	 * Make the personal folder persistent.
	 * 
	 * @param personalFolder to save
	 */
	public void makePersonalFolderPersistent(PersonalFolder personalFolder);
	
	/**
	 * Get a personal file by it's unique id.
	 * 
	 * @param id - unique id of the file
	 * @param lock - upgrade the lock mode.
	 * 
	 * @return - the personal file or null if no personal file is found.
	 */
	public PersonalFile getPersonalFile(Long id, boolean lock);
	
	/**
	 * Delete a personal folder and all related information.  
	 * 
	 * @param personal folder to delete
	 */
	public void deletePersonalFolder(PersonalFolder personalFolder);
	
	/**
	 * Get all personal folders not within the specified set of folders.
	 * 
	 * @param personalFolder
	 * @param user who owns the folders
	 * @param id of the parent folder
	 * 
	 * @return all folders not within the specified trees.
	 */
    public List<PersonalFolder> getAllFoldersNotInChildFolders(List<Long> folderIds, 
			Long userId,
			Long parentFolderId);
    
    
     /**
     * Create a personal versioned file in the system with the specified file for the
     * given user. 
     * 
     * @param repositoryId - the repository to add the file to.
     * @param f - file to add
     * @param personalFolder - personal folder to add the file to.  
     * @param fileName - The name to give the file.
     * @param description - description of the file.
     * 
     * @return the created personal file
     */
    public PersonalFile addFileToUser(Repository repository, 
    		File f, 
    		PersonalFolder personalFolder, 	
    		String fileName, 
    		String description ) throws DuplicateNameException, IllegalFileSystemNameException;
    
    /**
     * Create a personal versioned file in the system with the specified file for the
     * given user. This is created at the root level (added to the user)
     * 
     * @param repositoryId - the repository to add the file to.
     * @param f - file to add
     * @param userId - Unique user id 
     * @param fileName - The name to give the file.
     * @param description - description of the file.
     * 
     * @return the created personal file
     */
    public PersonalFile addFileToUser(Repository repository, 
    		File f, 
    		IrUser user, 
    		String fileName, 
    		String description )throws DuplicateNameException, IllegalFileSystemNameException;
    
    /**
     * Create a personal versioned file in the system with an empty file for the
     * given user. 
     * 
     * @param repository - the repository to add the file to.
     * @param personalFolder - folder to add the file to.  
     * @param fileName - The name to give the file.
     * @param description - description of the file.
     * 
     * @return the created personal file
     */
    public PersonalFile addFileToUser(Repository repository, 
    		PersonalFolder personalFolder, 
    		String fileName, 
    		String description )throws DuplicateNameException, IllegalFileSystemNameException;
    
    /**
     * Create a personal versioned file in the system with an empty file for the
     * given user. This is created at the root level (added to the user)
     * 
     * @param Repository - the repository to add the file to.
     * @param user - User to add the file to
     * @param fileName - The name to give the file.
     * @param description - description of the file.
     * 
     * @return the created personal file
     */
    public PersonalFile addFileToUser(Repository repository, 
    		IrUser user, 
    		String fileName, 
    		String description )throws DuplicateNameException, IllegalFileSystemNameException;
    
    /**
     * Create a new personal folder with the given parent folder as the parent
     * If the parent folder id is null , the folder is
     * added to the user as a root folder.
     * 
     * @param parentFolder - parent folder to add the new folder to.
     * @param folderName - name of the folder.
     * 
     * @return the newly created personal folder.
     */
    public PersonalFolder createNewFolder( PersonalFolder personalFolder, 
    		String folderName)throws DuplicateNameException,  IllegalFileSystemNameException;
    
    /**
     * Create a new personal folder with the given parent folder id.
     * The folder is added to the user as a root folder.
     * 
     * @param user - user to add the folder to.
     * @param folderName - name of the folder.
     * 
     * @return the newly created personal folder.
     */
    public PersonalFolder createNewFolder(IrUser user, 
    		String folderName) throws DuplicateNameException, IllegalFileSystemNameException;
    
	/**
	 * Delete a personal file.
	 * 
	 * @param personalFileId
	 */
	public void deletePersonalFile(PersonalFile personalFile);

	
	/**
	 * Allow a user to move folders and files to a given location.
	 * 
	 * @param destination
	 * @param foldersToMove
	 * @param filesToMove
	 * 
	 * @return list of files and folders that cannot be moved.
	 */
	public List<FileSystem> moveFolderSystemInformation(PersonalFolder destination, 
			List<PersonalFolder> foldersToMove, 
			List<PersonalFile> filesToMove);
	
	
	/**
	 * Allow user information to be moved to the root level.
	 * 
	 * @param user - user to add the information to.
	 * @param foldersToMove - folders to move
	 * @param filesToMove - files to move
	 * 
	 * @return list of files and folders that cannot be moved.
	 */
	public List<FileSystem> moveFolderSystemInformation(IrUser user, 
			List<PersonalFolder> foldersToMove, 
			List<PersonalFile> filesToMove);
	
	
	/**
	 * Get folders for the specified user with the given folder ids.
	 * 
	 * @param userId - id of the user to get the folders for
	 * @param folderIds - id's of the folder to get
	 * 
	 * @return the list of found folders
	 */
	public List<PersonalFolder> getFolders(Long userId, List<Long> folderIds);
	
	/**
	 * Get files for the specified user with the given folder ids.
	 * 
	 * @param userId - id of the user to get the folders for
	 * @param folderIds - id's of the folder to get
	 * 
	 * @return the list of found folders
	 */
	public List<PersonalFile> getFiles(Long userId, List<Long> fileIds);

	
	/**
	 * Get personal files for a user in the specified folder
	 * 
	 * @param userId Id of the user having the files
	 * @param parentFolderId Id of the folder contaiing the files
	 * 
	 * @return List of files in the folder
	 */
	public List<PersonalFile> getPersonalFilesInFolder(Long userId, Long parentFolderId);
	
	/**
	 * Get sub folders within parent folder for a user 
	 * 
	 * @param userId Id of the user having the folders
	 * @param parentFolderId Id of the parent folder
	 * 
	 * @return List of sub folders within the parent folder
	 */
	public List<PersonalFolder> getPersonalFoldersForUser(Long userId, Long parentFolderId);

	
	/**
	 * Delete personal file
	 * 
	 * @param personalFile personal file to be deleted
	 */
	public void makePersonalFileTransient(PersonalFile personalFile);	

	/**
	 * Save personal file
	 * 
	 * @param personalFile personal file to be saved
	 */
	public void makePersonalFilePersistent(PersonalFile personalFile);	

	/**
	 * Delete the Acess control list for the specified versioned file.
	 * 
	 * @param versionedFile - versioned file to remove the ACL from
	 * @param user - user to remove the ACL from.
	 */
	public void deleteAclForVersionedFile(VersionedFile versionedFile, IrUser user);
	
	/**
	 * Get all versioned files for the specified folder.
	 * 
	 * @param folder - versioned file to get all personal folders for.
	 * @return - all versioned files for the specified files.
	 */
	public List<VersionedFile> getAllVersionedFilesForFolder(PersonalFolder folder);
	

	/**
	 * Get the personal file for user with specified ir file
	 * 
	 * @param user User having the personal file
	 * @param irFile ir file the personal file pointing to
	 * 
	 * @return Personal file of the user
	 */
	public PersonalFile getPersonalFile(IrUser user, IrFile irFile);

	/**
	 * Get the personal file for user holding the specified versioned file
	 * 
	 * @param user User having the personal file
	 * @param versioned file the personal file pointing to
	 * 
	 * @return Personal file of the user or null if not found
	 */
	public PersonalFile getPersonalFile(IrUser user, VersionedFile versionedFile);

	
	/**
	 * Create an index folder for the user in the specified repository.
	 * 
	 * @param user - user to add a folder for indexing
	 * @param repository - repository to add the index to.
	 * @param folderName - name to give the folder in the file system.
	 * @throws LocationAlreadyExistsException - if the folder location already exists
	 * @throws IOException - if IO exception occurs when creating the folder
	 */
	public void createIndexFolder(IrUser user, Repository repository, String folderName) throws LocationAlreadyExistsException, IOException;
	
	
	/**
	 * Delete a users index folder if it exists.
	 * 
	 * @param user - user who's folder is to be deleted.
	 * @throws IOException - if an error occurs during delete.
	 */
	public void deleteIndexFolder(IrUser user) throws IOException;
	
	/**
	 * Determine if the irFile is used by any personal files.  This
	 * checks all users in the system.
	 * 
	 * @param irFile Ir File to be searched for
	 * 
	 * @return Personal files using this IrFile
	 */
	public Long getPersonalFileCount(IrFile irFile);
	
	/**
	 * Get a count of the shared files in this user's In-box
	 * 
	 * @param user - user to get the shared file in-box count
	 * @return - the number of files in the users in-box.
	 */
	public Long getSharedFileInboxCount(IrUser user);
	
	/**
	 * Get the shared inbox files for a user
	 * 
	 * @param user - user to get the shared file in-box files
	 * @return - the inbox files
	 */
	public List<SharedInboxFile> getSharedInboxFiles(IrUser user);
	
	/**
	 * Get the shared in-box file with the specified id.
	 * 
	 * @param id - id of the shared in-box file 
	 * @param lock - upgrade the lock on the data
	 * @return the shared in-box file or null if not found
	 */
	public SharedInboxFile getSharedInboxFile(Long id, boolean lock);
 
    
    /**
     * Adds the inbox file to the specified personal folder.  Deletes the shared inbox file after
     * the move.
     * 
     * @param folder
     * @param inboxFile
     * @return the created personal file
     * 
     * @throws DuplicateNameException - if the file name already exists in the folder
     */
    public PersonalFile addSharedInboxFileToFolders(PersonalFolder folder, SharedInboxFile inboxFile) 
        throws DuplicateNameException;
    
    /**
     * Adds the shared inbox file to the user as a root file.  Deletes the inbox file after
     * the move.
     * 
     * @param user
     * @param inboxFile
     * @return the created personal file
     * 
     * @throws DuplicateNameException - if the file name already exists in the folder
     */
    public PersonalFile addSharedInboxFileToFolders(IrUser user, SharedInboxFile inboxFile) 
    throws DuplicateNameException;
    
    /**
     * Make the specified inbox file persistent.
     * 
     * @param inboxFile
     */
    public void makeSharedInboxFilePersistent(SharedInboxFile inboxFile);
    
    /**
     * Make the specified inbox file persistent.
     * 
     * @param inboxFile
     */
    public void makeSharedInboFileTransient(SharedInboxFile inboxFile);
    
    /**
     * Returns the sub folders within the given parent folder
     * 
     * @param userId Owner Id of the folder
     * @param parentFolderId Id of the parent folder
     * @return sub folders within the given parent folder
     */
    public List<PersonalFolder> getSubFoldersForFolder(
			Long userId, 
			Long parentFolderId) ;
    
	/**
	 * Returns all files within a folder and its sub folder
	 * 
	 * @param folderId Folder id to get the files from 
	 * @param userId User id of the folder
	 * 
	 * @return all files within a folder and its sub folder
	 */
	public List<PersonalFile> getAllFilesInFolderAndSubFolder(Long folderId, Long userId) ;

	/**
	 * Get shared inbox files for specified user and ids
	 * 
	 * @param userId User having the files in shared inbox files
	 * @param fileIds Id of Shared inbox files
	 *  
	 * @return List of shared inbox files
	 */
	public List<SharedInboxFile> getSharedInboxFiles(final Long userId, final List<Long> fileIds);

	/**
	 * Returns size for a folder
	 * 
	 * @param user User the folder belongs to
	 * @param folder Folder containing the files
	 * 
	 * @return Size of folder
	 */
	public Long getFolderSize(IrUser user, PersonalFolder folder);

	/**
	 * Get the sum of versioned file size for a user
	 * 
	 * @param user user the VersionedFile belongs to
	 * 
	 * @return sum of versioned files size
	 */
	public Long getFileSystemSizeForUser(IrUser user);
}
