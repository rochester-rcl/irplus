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

package edu.ur.ir.repository;

import java.io.File;
import java.util.List;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FolderInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.file.FileVersion;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.user.IrUser;

/**
 * Service for dealing with repositories.
 * 
 * @author Nathan Sarr
 *
 */
public interface RepositoryService {
	
	/**
	 * Creates a repository with the specified repository name and specified file database.
	 * 
	 * @param repositoryName - name to give the repository
	 * @param fileDatabase - file database to use to store files for the repository.
	 * 
	 * @return - the created repository.
	 */
	public Repository createRepository(String repositoryName, FileDatabase fileDatabase);
	
	
    /**
     * Delete a repository with the specified name.
     * 
     * @param repositoryId
     */
    public void deleteRepository(Repository repository);
    
    /**
     * Get a repository by name.
     * 
     * @param name - name of the repository.
     * @return - the found repository or null if the repository is not found.
     */
    public Repository getRepository(String name);
    
    /**
     * Get a repository by id
     * 
     * @param id - unique id of the repository.
     * @param lock - upgrade the lock on the data
     * @return - the found repository or null if the repository is not found.
     */
    public Repository getRepository(Long id, boolean lock);
    
    
	/**
	 * Create a versioned file in the repository.
	 * 
	 * @param user - user who owns the file
	 * @param repositoryId - id of the repository to create the versioned file in.
	 * @param f - file to add
	 * @param fileName - name user wishes to give the file.
	 * @param descrption - description of the file.
	 * 
	 * @return - the created versioned file.
	 * @throws edu.ur.ir.IllegalFileSystemNameException 
	 */
	public VersionedFile createVersionedFile(
			IrUser user,
			Repository repository, 
			File f, 
			String fileName, 
			String description) throws IllegalFileSystemNameException;


	/**
	 * Create a versioned file in the repository and the specified file is empty.
	 * 
	 * @param repositoryId - id of the repository to create the versioned file in.
	 * @param fileName - name user wishes to give the file.
	 * @param descrption - description of the file.
	 * 
	 * @return - the created versioned file.
	 * @throws edu.ur.ir.IllegalFileSystemNameException 
	 */
	public VersionedFile createVersionedFile(
			IrUser user,
			Repository repository, 
			String fileName,
			String description ) throws IllegalFileSystemNameException;
	
	/**
	 * Get the versioned file.
	 * 
	 * @param versionedFileId - unique id for the versioned file.
	 * @param lock - upgrade the lock mode.
	 * 
	 * @return the found versioned file or null if it is not found.
	 */
	public VersionedFile getVersionedFile(Long versionedFileId, boolean lock);
	
	/**
	 * Delete the versioned file from the system.  This permanetly 
	 * deletes the versioned file information.
	 * 
	 * @param versionedFileId - id of needed version
	 */
	public void deleteVersionedFile(VersionedFile versionedFile);
	
	/**
	 * Adds a new version of a file to the repository.
	 * 
	 * @param repository - the repository to add the file to.
	 * @param versionedFile - id of the versioned file to add the file to
	 * @param f - file to add
	 * @param fileName - name of the file when uploaded or added to the system
	 * @param versionCreator - User creating the user
	 * @throws IllegalFileSystemNameException 
	 * 
	 */
	public void addNewFileToVersionedFile( Repository repository, 
			VersionedFile versionedFile, 
			File f, 
			String fileName, 
			IrUser versionCreator) throws IllegalFileSystemNameException;
	
	/**
	 * Adds a new version of a file to the repository.
	 * 
	 * @param repository - the repository to add the file to.
	 * @param versionedFile - id of the versioned file to add the file to
	 * @param f - file to add
	 * @param fileName - name of the file when uploaded or added to the system
	 * @param description - description of the file / changes
	 * @param versionCreator - User creating the user
	 * @throws IllegalFileSystemNameException 
	 * 
	 */
	public void addNewFileToVersionedFile( Repository repository, 
			VersionedFile versionedFile, 
			File f, 
			String fileName, 
			String description,
			IrUser versionCreator) throws IllegalFileSystemNameException;
	
	/**
	 * Find a transformed file type by system code.
	 * 
	 * @param systemCode - unique system code.
	 * @return - the found trandformed file type.
	 */
	public TransformedFileType getTransformedFileTypeBySystemCode(String systemCode);
	
	/**
	 * Make the repository persistent.
	 * 
	 * @param repository
	 */
	public void saveRepository(Repository repository);
	
	
	/**
	 * Add a transformed file to  an ir file
	 * 
	 * @param repositoryId - repository to store the new file in
	 * @param irFileId - Ir file to add the transformed file to
	 * @param f - the transformed file.
	 * @param transformedFileName - name to give the transformed file
	 * @param transformedFileExtension - extension that should be used for the transformed file
	 * @param transformedFileType - type of transformed file created.
	 * @throws IllegalFileSystemNameException 
	 */
	public void addTransformedFile(Repository repository, 
			IrFile irFile, 
			File f, 
			String transformedFileName, 
			String transformedFileExtension, 
			TransformedFileType transformedFileType) throws IllegalFileSystemNameException;
    
	/**
	 * This locks a versioned file for editing.
	 * 
	 * @param versionedFile - file to lock
	 * @param user - user locking the file
	 * 
	 */
	public boolean lockVersionedFile(VersionedFile versionedFile, IrUser user);
	
	/**
	 * Determine if the user can lock the file.  This does not pay attention to
	 * if the file is already locked.  It only indicates that the user would
	 * have permission to lock the file if the file was unlocked.
	 * 
	 * @param versionedFile
	 * @param user
	 * 
	 * @return true if the user can unlock the file
	 */
	public boolean canLockVersionedFile(VersionedFile versionedFile, IrUser user);
	
	/**
	 * unlocks the file so long as the user has the ability to unlock the file.
	 * 
	 * @param versionedFile - file to be unlocked
	 * @param user - user who wants to unlock the file.
	 * 
	 * @return true if the versioned file is unlocked
	 */
	public boolean unlockVersionedFile(VersionedFile versionedFile, IrUser user);
	
	/**
	 * Returns true if the file can be unlocked by the specified user. 
	 * 
	 * @param versionedFile - versioned file to be locked
	 * @param user - user to use to unlock the file
	 * 
	 * @return true if the user can unlock the file
	 */
	public boolean canUnlockFile(VersionedFile versionedFile, IrUser user);
	
	/**
	 * Add the picture file to the specified repository.
	 * 
	 * @param repositoryId - id of the repository to add the file to.
	 * @param f - file that contains the picture.
	 * @param name - name to give the file
	 * @param description - description to give the file
	 * 
	 * @return the created irFile storedin the system.
	 * @throws IllegalFileSystemNameException 
	 */
	public IrFile addRepositoryPicture(Repository repository, 
			File f, 
			String name, 
			String description) throws IllegalFileSystemNameException;
	
	/**
	 * Remove the picture from the specified repository.
	 * 
	 * @param repositoryId - id of the repository
	 * @param irFile - picture file to remove.
	 * 
	 * @return - true if the picture is removed
	 */
	public boolean deleteRepositoryPicture(Repository repository, IrFile irFile);
	
	/**
	 * Add the file to the specified repository.
	 * 
	 * @param repository - repository to add the file to
	 * @param f - file to add
	 * @param fileName - name to give the file
	 * @param description - description of the file
	 * 
	 * @return - the file information
	 * @throws IllegalFileSystemNameException 
	 */
	public FileInfo createFileInfo(Repository repository, 
			File f, 
			String fileName, 
			String description) throws IllegalFileSystemNameException;
	
	/**
	 * Create the ir file.
	 * 
	 * @param repository - repository to create the ir file in
	 * @param f - file to add
	 * @param fileName - name of the file
	 * @param description - description of the ir file
	 * 
	 * @return - the created ir file.
	 * @throws edu.ur.ir.IllegalFileSystemNameException 
	 */
	public IrFile createIrFile(Repository repository, 
			File f, 
			String fileName, 
			String description)  throws IllegalFileSystemNameException;
	
	/**
	 * Get the ir file with the specified id.
	 * 
	 * @param id - id of the file
	 * @param lock - upgrade the lock mode.
	 * @return - the found ir file.
	 */
	public IrFile getIrFile(Long id, boolean lock);
	
	/**
	 * Get the file info object for the transformed file by the
	 * ir file id and system code.
	 * 
	 * @param irFileId - id of the ir file that has the transformed file
	 * @param systemCode - system code of the transformed file.
	 * 
	 * @return - the found transformed file
	 */
	public FileInfo getTransformByIrFileSystemCode(Long irFileId, String systemCode);
	
	/**
	 * Delete the specified ir file.
	 * 
	 * @param irFile - file to be deleted.
	 * @return true if the irFile is deleted.
	 */
	public boolean deleteIrFile(IrFile irFile);
	
	/**
	 * Delete the file info from this repository.
	 * 
	 * @param - repository the file info object resides in.
	 * @param fileInfo - the file to be removed
	 * @return - true if the file is deleted.
	 */
	public boolean deleteFileInfo (FileInfo fileInfo);
	
	
	/**
	 * Add a low level file info object to the repository.
	 * 
	 * @param repository - repository to place the file info object in
	 * @param f - file to add to use in creating the repository
	 * @param fileName - name of the file
	 * 
	 * @return Created file info object with the specified information
	 * @throws IllegalFileSystemNameException 
	 * 
	 * 
	 */	
	public FileInfo createFileInfo (Repository repository, 
			File f, 
			String fileName) throws IllegalFileSystemNameException;
	
	
	/**
	 * Create a file info object with an empty file.
	 * 
	 * @param repository - repository to add the file to
	 * @param fileName - name to give the file on the file system.
	 * 
	 * @return information about the empty file.
	 * @throws IllegalFileSystemNameException 
	 */
	public FileInfo createFileInfo(Repository repository, 
			String fileName) throws IllegalFileSystemNameException;
	
	/**
	 * Deletes the folder information
	 * 
	 * @param folderInfo - folder to be deleted.
	 * @return true if the information is deleted.
	 */
	public void deleteFolderInfo(FolderInfo folderInfo);
	
	/**
	 * Create a folder in the file system.
	 * 
	 * @param repository - repository to create the folder in 
	 * @param folderName - name of folder on the file system.
	 * 
	 * @return the created folder.
	 * @throws LocationAlreadyExistsException - if the folder location already exists
	 */
	public FolderInfo createFolderInfo(Repository repository, String folderName) throws LocationAlreadyExistsException;
	
	/**
	 * Get folder by name
	 * 
	 * @param name name of the folder
	 * @param fileDatabaseId Id of  the file database
	 * 
	 * @return
	 */
	public FolderInfo getFolderInfo(String name, Long fileDatabaseId);
 
	/**
	 * Get the versioned file containing the given IrFile id
	 * 
	 * @param irFile file to get the VersionedFile
	 * 
	 * @return VersionedFile containing the IrFile
	 */
	public VersionedFile getVersionedFileByIrFile(IrFile irFile);
	
	/**
	 * Get the file version with the specified id.
	 * 
	 * @param id - id of the file
	 * @param lock - upgrade the lock mode.
	 * @return - the found file version.
	 */
	public FileVersion getFileVersion(Long id, boolean lock);
	
	/**
	 * Get the versioned files for given item id
	 * 
	 * @param item item to get the versioned files
	 * 
	 * @return VersionedFiles containing the IrFile
	 */
	public List<VersionedFile> getVersionedFilesForItem(GenericItem item);

	/**
	 * Get the sum of versioned file size for a user
	 * 
	 * @param user user the VersionedFile belongs to
	 * 
	 * @return sum of versioned files size
	 */
	public Long getFileSystemSizeForUser(IrUser user);
	
	/**
	 * Get the set of repository licenses that can still be applied to this 
	 * repository.
	 * 
	 * @param repositoryId - id of the repository
	 * @return - list of available licenses
	 */
	public List<LicenseVersion> getAvailableRepositoryLicenses(Long repositoryId);
}
