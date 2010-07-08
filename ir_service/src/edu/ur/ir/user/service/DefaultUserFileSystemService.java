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


package edu.ur.ir.user.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import edu.ur.cgLib.CgLibHelper;
import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.file.FileCollaborator;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFileDAO;
import edu.ur.ir.user.PersonalFileDeleteRecord;
import edu.ur.ir.user.PersonalFileDeleteRecordDAO;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.PersonalFolderDAO;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.SharedInboxFileDAO;
import edu.ur.ir.user.UserFileSystemService;

/**
 * Service for dealing with user file system.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultUserFileSystemService implements UserFileSystemService{
	

	/** eclipse generated id */
	private static final long serialVersionUID = 4772256208913792044L;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultUserFileSystemService.class);

	/** Service class for dealing with the repository  */
	private RepositoryService repositoryService;
	
	/** Shared Inbox file data access  */
	private SharedInboxFileDAO sharedInboxFileDAO;
	
	/**  User data access  */
	private IrUserDAO irUserDAO;
	
	/** Service class for dealing with the (A)cess (C)ontrol (L)ists */
	private SecurityService securityService;
	
	/** Folder data access object */
	private PersonalFolderDAO personalFolderDAO;
	
	/**   Data access for personal files */
	private PersonalFileDAO personalFileDAO;
	
	/** Data access for trakcing personal file deletions */
	private PersonalFileDeleteRecordDAO personalFileDeleteRecordDAO;
	
	/** Service class for dealing with file sharing*/
	private InviteUserService inviteUserService;
	
	/**
	 * Get the personal folder with the speicified name and parent id.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getPersonalFolder(java.lang.String, java.lang.Long)
	 */
	public PersonalFolder getPersonalFolder(String name, Long parentId) {
		return personalFolderDAO.getPersonalFolder(name, parentId);
	}

	/**
	 * Get the personal folder with the specified id.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getPersonalFolder(java.lang.Long, boolean)
	 */
	public PersonalFolder getPersonalFolder(Long id, boolean lock) {
		return personalFolderDAO.getById(id, lock);
	}
	
	/**
	 * Get the personal file with the specified id.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getPersonalFile(java.lang.Long, boolean)
	 */
	public PersonalFile getPersonalFile(Long id, boolean lock)
	{
		return personalFileDAO.getById(id, lock);
	}

	/** 
	 * Get the path to a specified folder with the specified personal folder id
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getPersonalFolderPath(java.lang.Long)
	 */
	public List<PersonalFolder> getPersonalFolderPath(Long personalFolderId) {
		if( personalFolderId == null )
		{
			return new LinkedList<PersonalFolder>();
		}
		
		PersonalFolder p = this.getPersonalFolder(personalFolderId, false);
		
		if( p != null )
		{	
		    return personalFolderDAO.getPath(p);
		}
		else
		{
			return new LinkedList<PersonalFolder>();
		}
	}

	/**
	 * Get root personal folder with the specified name for the specified user.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getRootPersonalFolder(java.lang.String, java.lang.Long)
	 */
	public PersonalFolder getRootPersonalFolder(String name, Long userId) {
		return personalFolderDAO.getRootPersonalFolder(name, userId);
	}

	/**
	 * Get all personal folders for the specified user.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getAllPersonalFoldersForUser(java.lang.Long)
	 */
	public List<PersonalFolder> getAllPersonalFoldersForUser(Long userId) {
		return personalFolderDAO.getAllPersonalFoldersForUser(userId);
	}

	
	/**
	 * Save the personal folder.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#makePersonalFolderPersistent(edu.ur.ir.user.PersonalFolder)
	 */
	public void makePersonalFolderPersistent(PersonalFolder entity) {
		personalFolderDAO.makePersistent(entity);
	}

	
	/**
	 * Get the personal folder data access object.
	 * 
	 * @return
	 */
	public PersonalFolderDAO getPersonalFolderDAO() {
		return personalFolderDAO;
	}

	/**
	 * Set the personal folder data access object.
	 * 
	 * @param personalFolderDAO
	 */
	public void setPersonalFolderDAO(PersonalFolderDAO personalFolderDAO) {
		this.personalFolderDAO = personalFolderDAO;
	}
	
	
	/**
	 * Get the personal folders within the parent folder.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getPersonalFolders(java.util.List, java.lang.Long, java.lang.Long, int, int)
	 */
	public List<PersonalFolder> getSubFoldersForFolder(
			Long userId, 
			Long parentFolderId) 
	{
		return personalFolderDAO.getSubFoldersForFolder(userId, parentFolderId);
	}

	
	/**
	 * Get the personal file for user with specified ir file
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getPersonalFile(IrUser, IrFile)
	 */
	public PersonalFile getPersonalFile(IrUser user, IrFile irFile) {
		return personalFileDAO.getFileForUserWithSpecifiedIrFile(user.getId(), 
				irFile.getId());
	}
	
	/**
	 * Get the personal file with specified ir file
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getPersonalFileCount(IrFile)
	 */
	public Long getPersonalFileCount(IrFile irFile) {
		
		return personalFileDAO.getFileWithSpecifiedIrFile(irFile.getId());
		
	}
	
	
	/**
	 * Add a file to a user.
	 * @throws DuplicateNameException 
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#addFileToUser(edu.ur.ir.repository.Repository, java.io.File, edu.ur.ir.user.PersonalFolder, java.lang.String, java.lang.String, java.lang.String)
	 */
	public PersonalFile addFileToUser(Repository repository, 
			File f, 
			PersonalFolder personalFolder, 
			String fileName,
			String description) throws DuplicateNameException, IllegalFileSystemNameException
	{
	
		if( fileName == null || fileName.trim().equals("") )
		{
			throw new IllegalStateException("Both file name and original " +
					"file name were null");
		}
		
		PersonalFile personalFile = personalFolder.getFile(fileName);
		if(  personalFile != null )
		{
			throw new DuplicateNameException("File with the name " + 
					fileName + " already exists ", fileName);
		}
		
        VersionedFile versionedFile = 
           repositoryService.createVersionedFile(personalFolder.getOwner(), 
        		        repository, 
            			f, 
            			fileName, 
            			description);
       
        try {
			personalFile = personalFolder.addVersionedFile(versionedFile);
		} catch (DuplicateNameException e) {
			repositoryService.deleteVersionedFile(versionedFile);
			throw e;
		}
        makePersonalFolderPersistent(personalFolder);
       
        securityService.assignOwnerPermissions(personalFile.getVersionedFile(), 
        		personalFolder.getOwner());
        
		return personalFile;
	}
	

	

	/**
	 * Add an empty file to a user.  Added to the personal folder
	 * @throws DuplicateNameException 
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#addFileToUser(java.lang.Long, java.lang.Long, java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
	 */
	public PersonalFile addFileToUser(Repository repository, 
			PersonalFolder personalFolder,
			String fileName, 
			String description) throws DuplicateNameException, IllegalFileSystemNameException {
		
		if( fileName == null ||fileName.trim().equals("") )
		{
			throw new IllegalStateException("Both file name and original " +
					"file name were null");
		}
		
		PersonalFile personalFile = personalFolder.getFile(fileName);
		
		if(  personalFile != null )
		{
			throw new DuplicateNameException("File with the name " + 
					fileName + " already exists ", fileName);
		}
		
        VersionedFile versionedFile = 
            	repositoryService.createVersionedFile(personalFolder.getOwner(), 
            			repository, 
            			fileName, 
            			description);
        
        // this is still needed 
        try {
				personalFile = personalFolder.addVersionedFile(versionedFile);
		} catch (DuplicateNameException e) {
				repositoryService.deleteVersionedFile(versionedFile);
				throw e;
		}
        makePersonalFolderPersistent(personalFolder);
     
        securityService.assignOwnerPermissions(personalFile.getVersionedFile(), 
        		personalFolder.getOwner());
        
        return personalFile;
	}
	

	/**
	 * Add the file to the user.
	 * @throws DuplicateNameException 
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#addFileToUser(edu.ur.ir.repository.Repository, edu.ur.ir.user.IrUser, java.lang.String, java.lang.String, java.lang.String)
	 */
	public PersonalFile addFileToUser(Repository repository, 
			IrUser user,
			String fileName, 
			String description) throws DuplicateNameException, IllegalFileSystemNameException {
		
		
		if( fileName == null || fileName.trim().equals("") )
		{
			throw new IllegalStateException("File name is null");
		}
		
		PersonalFile personalFile = user.getRootFile(fileName);
		
		if(  personalFile != null )
		{
			throw new DuplicateNameException("File with the name " + 
					fileName + " already exists ", fileName);
		}
		VersionedFile versionedFile = 
	    	repositoryService.createVersionedFile(user, repository, 
	    			fileName, description);
		try
		{
	        personalFile = user.createRootFile(versionedFile);
		}
		catch(DuplicateNameException dne)
		{
				repositoryService.deleteVersionedFile(versionedFile);
				throw dne;
		}
	    personalFileDAO.makePersistent(personalFile);
	    securityService.assignOwnerPermissions(personalFile.getVersionedFile(), user);
		
	    return personalFile;
	}
	

  
	
    /**
     * Create a personal versioned file in the system with the specified file for the
     * given user. If the file name is null, the original file name is used. 
     * This is created at the root level (added to the user)
     * 
     * @param repositoryId - repository to add the file to.
     * @param f - file to add
     * @param user - user to add the file to
     * @param fileName - The name to give the file.
     * @param description - description of the file.
     * @param originalFileName - original file name uploaded from the file system
     * 
     * @return the created personal file
     * @throws DuplicateNameException 
     */
    public PersonalFile addFileToUser(Repository repository, 
    		File f, 
    		IrUser user, 
    		String fileName, 
    		String description) throws DuplicateNameException, IllegalFileSystemNameException
    {
		if( fileName == null || fileName.trim().equals("") )
		{
			throw new IllegalStateException("Both file name and original " +
					"file name were null");
		}
		
		PersonalFile personalFile = user.getRootFile(fileName);
		
		if(  personalFile != null )
		{
			throw new DuplicateNameException("File with the name " + 
					fileName + " already exists ", fileName);
		}
		
		VersionedFile versionedFile = 
	    	repositoryService.createVersionedFile(user, repository, 
	    			f, fileName, description);
		
		try
		{
	        personalFile = user.createRootFile(versionedFile);
		}
		catch(DuplicateNameException dne)
		{
			repositoryService.deleteVersionedFile(versionedFile);
			throw dne;
		}
	    personalFileDAO.makePersistent(personalFile);
	    
	    securityService.assignOwnerPermissions(personalFile.getVersionedFile(), 
        		user);
	    
	    return personalFile;
    }
	
	/**
	 * Delete a personal file.
	 * 
	 * @param personalFileId
	 */
	public void delete(PersonalFile pf, IrUser deletingUser, String deleteReason)
	{
		VersionedFile versionedFile = pf.getVersionedFile();
		
		// create a delete record 
		PersonalFileDeleteRecord personalFileDeleteRecord = new PersonalFileDeleteRecord(deletingUser.getId(),
				pf.getId(),
				pf.getFullPath(), 
				pf.getDescription());
		personalFileDeleteRecord.setDeleteReason(deleteReason);
		personalFileDeleteRecordDAO.makePersistent(personalFileDeleteRecord);
		
		personalFileDAO.makeTransient(pf);
		
		// Delete versioned file only if requested by its owner
		if (pf.getOwner().equals(versionedFile.getOwner())) {
			
			// unshare users with whom this file is currently shared with
			Set<FileCollaborator> fileCollaborators = new HashSet<FileCollaborator>();
			fileCollaborators.addAll(versionedFile.getCollaborators());
			
			for (FileCollaborator fileCollaborator: fileCollaborators) {
				inviteUserService.unshareFile(fileCollaborator, deletingUser);
			}

			deleteAclForVersionedFile(versionedFile, pf.getOwner());
			repositoryService.deleteVersionedFile(versionedFile);
			
		} else {
			// unshare the file that is shared with this user
			inviteUserService.unshareFile(versionedFile.getCollaborator(pf.getOwner()), deletingUser);
		}

	}

	


	/**
	 * Get personal files for a user in the specified folder
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getPersonalFilesInFolder(java.util.Long, java.lang.Long)
	 */
	public List<PersonalFile> getPersonalFilesInFolder(Long userId, Long parentFolderId) {
	    
		if( parentFolderId == null || parentFolderId == ROOT_FOLDER_ID)
		{
			return  personalFileDAO.getRootFiles(userId);
		}
		else
		{
		   return personalFileDAO.getFilesInFolderForUser(userId, parentFolderId);
		}
	}
	
	/**
	 * Get sub folders within parent folder for a user 
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getPersonalFoldersForUser(java.util.Long, java.lang.Long)
	 */
	public List<PersonalFolder> getPersonalFoldersForUser(Long userId, Long parentFolderId) {
	    
		if( parentFolderId == null || parentFolderId == ROOT_FOLDER_ID)
		{
			return  personalFolderDAO.getRootFolders(userId);
		}
		else
		{
		   return personalFolderDAO.getSubFoldersForFolder(userId, parentFolderId);
		}
	}
	
	/**
	 * Get the personal file data access object.
	 * 
	 * @return
	 */
	public PersonalFileDAO getPersonalFileDAO() {
		return personalFileDAO;
	}

	/**
	 * Set the personal file data access object.
	 * 
	 * @param personalFileDAO
	 */
	public void setPersonalFileDAO(PersonalFileDAO personalFileDAO) {
		this.personalFileDAO = personalFileDAO;
	}

	/**
	 * Delete all sub folders and files from the system for the specified folder id.
	 * This physically removes the files stored on the file system.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#deletePersonalFolder(edu.ur.ir.user.PersonalFolder, edu.ur.ir.user.IrUser, java.lang.String)
	 */
	public void deletePersonalFolder(PersonalFolder personalFolder, IrUser deletingUser, String deleteReason) {
		List<PersonalFile> personalFiles = personalFolderDAO.getAllFilesForFolder(personalFolder);

		for( PersonalFile aFile : personalFiles)
		{
		    delete(aFile, deletingUser, deleteReason);
		}
		
		if( personalFolder.getIsRoot())
		{
			IrUser owner = personalFolder.getOwner();
			owner.removeRootFolder(personalFolder);
		}
		else
		{
			PersonalFolder parent = personalFolder.getParent();
			parent.removeChild(personalFolder);
		}

		personalFolderDAO.makeTransient(personalFolder);

	}

	
	/** 
	 * @see edu.ur.ir.user.UserFileSystemService#getAllFoldersNotInChildFolders(java.util.List, 
	 * java.lang.Long, java.lang.Long)
	 */
	public List<PersonalFolder> getAllFoldersNotInChildFolders(
			List<Long> folders, Long userId, Long parentFolderId) {
		
		
		HashMap<Long, LinkedList<PersonalFolder>> foldersGroupedByRoot = 
			new HashMap<Long, LinkedList<PersonalFolder>>();
		LinkedList<Long> rootFolderIds = new LinkedList<Long>();
		// get the folder id's
		for(Long folderId : folders)
		{
			// find the folder
			PersonalFolder p = getPersonalFolder(folderId, false);
			
			// see if we already have a group for it's root folder if not add it
			LinkedList<PersonalFolder> folderGroup = foldersGroupedByRoot.get(p.getTreeRoot().getId());
			if( folderGroup == null)
			{
				rootFolderIds.add(p.getTreeRoot().getId());
				folderGroup = new LinkedList<PersonalFolder>();
				folderGroup.add(p);
				foldersGroupedByRoot.put(p.getTreeRoot().getId(), folderGroup);
			}
			else
			{
				folderGroup.add(p);
			}
		}
		
		// for each set of excluded folders for a root folder owned by the user execute the query.
		Iterator<Long> rootFolderIterator = foldersGroupedByRoot.keySet().iterator();
		LinkedList<PersonalFolder> availableFolders = new LinkedList<PersonalFolder>();
		
		while(rootFolderIterator.hasNext())
		{
			    Long rootFolderId = rootFolderIterator.next();
			    List<PersonalFolder> folderGroup = foldersGroupedByRoot.get(rootFolderId); 
				availableFolders.addAll(personalFolderDAO.getAllFoldersNotInChildFolders(folderGroup, 
						userId, rootFolderId));
				
				if(log.isDebugEnabled())
				{
					log.debug("Current available folders");
					for(PersonalFolder f: availableFolders)
					{
						log.debug("Folder id = " + f.getId());
					}
				}
		}
		
		// all other root folders can be added
		if(log.isDebugEnabled())
		{
			for( Long id : rootFolderIds)
			{
				log.debug("Adding the following id " + id);
			}
		}
		availableFolders.addAll(personalFolderDAO.getAllOtherRootFolders(rootFolderIds, 
				userId));
		return availableFolders;
		
	}

	
	/**
	 * Allow a user to move files and folders into a given folder
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#moveFolderSystemInformation(java.lang.Long, java.util.List, java.util.List)
	 */
	public List<FileSystem> moveFolderSystemInformation(PersonalFolder destination,
			List<PersonalFolder> foldersToMove, List<PersonalFile> filesToMove) 
	{
		
		LinkedList<FileSystem> notMoved = new LinkedList<FileSystem>();
		
		// move folders first
		if( foldersToMove != null )
		{
		    for( PersonalFolder folder : foldersToMove)
		    {
		    	log.debug("Adding folder " + folder + " to destination " + destination);
			   
			    try {
			    	 destination.addChild(folder);
				} catch (DuplicateNameException e) {
					notMoved.add(folder);
				}
			    
		    }
		}
	
		
		if( filesToMove != null  && notMoved.size() == 0)
		{
		    for( PersonalFile file : filesToMove)
		    {
		    	log.debug("Adding file " + file + " to destination " + destination);
			    try {
			        destination.addPersonalFile(file);
				} catch (DuplicateNameException e) {
					notMoved.add(file);
				}
		    	
		    }
		}
		
		if( notMoved.size() == 0)
		{
			personalFolderDAO.makePersistent(destination);
		}
		
		return notMoved;
	}
	
	/**
	 * Move the folders into the root location of the user.
	 * 
	 * @throws DuplicateNameException 
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#moveFolderSystemInformation(edu.ur.ir.user.IrUser, java.util.List, java.util.List)
	 */
	public List<FileSystem> moveFolderSystemInformation(IrUser user,
			List<PersonalFolder> foldersToMove, List<PersonalFile> filesToMove)  {

		LinkedList<FileSystem> notMoved = new LinkedList<FileSystem>();
		
		// move folders first
		if( foldersToMove != null )
		{
		    for( PersonalFolder folder : foldersToMove)
		    {
		    	log.debug("Adding folder " + folder + " to root of user " + user);
			    try {
			    	user.addRootFolder(folder);
				} catch (DuplicateNameException e) {
					notMoved.add(folder);
				}		    	
		    	
		    }
		}
		
		// then move the files
		if( filesToMove != null && notMoved.size() == 0)
		{
		    for( PersonalFile file : filesToMove)
		    {
		    	log.debug("Adding file " + file + " to root of user " + user);

			    try {
			        user.addRootFile(file);
			    } catch (DuplicateNameException e) {
					notMoved.add(file);
				}		        
		    }
		}

		if( notMoved.size() == 0)
		{
			irUserDAO.makePersistent(user);
		}
		
		return notMoved;
		
		
	}

	/**
	 * Create a personal folder setting the parent as the parent folder.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#createNewFolder(edu.ur.ir.user.PersonalFolder, java.lang.String)
	 */
	public PersonalFolder createNewFolder(PersonalFolder parentFolder,
			String folderName) throws DuplicateNameException, IllegalFileSystemNameException {
		
		PersonalFolder pf = parentFolder.createChild(folderName);
		personalFolderDAO.makePersistent(pf);
		
		return pf;
	}
	
	/**
	 * Allows a user to create a new folder.
	 * @throws DuplicateNameException 
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#createNewFolder(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	public PersonalFolder createNewFolder(IrUser user, String folderName) throws DuplicateNameException,  IllegalFileSystemNameException{
		
		PersonalFolder pf = user.createRootFolder(folderName);
		personalFolderDAO.makePersistent(pf);
		return pf;
	}

	
	/**
	 * Get the files for the specified user.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getFiles(java.lang.Long, java.util.List)
	 */
	public List<PersonalFile> getFiles(Long userId, List<Long> fileIds) {
		return  personalFileDAO.getFiles(userId, fileIds);
	}

	/**
	 * Get the folders for the specified user.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getFolders(java.lang.Long, java.util.List)
	 */
	public List<PersonalFolder> getFolders(Long userId, List<Long> folderIds) {
		return personalFolderDAO.getFolders(userId, folderIds);
	}

	/**
	 * Remove all access to collaborators for a versioned file.
	 * 
	 * @param versionedFile - versioned file that has an acl
	 * @param user - user to remove the access to.
	 */
	public void deleteAclForVersionedFile(VersionedFile versionedFile, IrUser user) {
		// Delete ACL for this file
		securityService.deleteAcl(versionedFile.getId(), CgLibHelper.cleanClassName(versionedFile.getClass().getName()));
	}


	/**
	 * Save personal file
	 * 
	 * @see edu.ur.ir.user.service.UserFileSystemService#makePersonalFilePersistent(PersonalFile personalFile)
	 */
	public void makePersonalFilePersistent(PersonalFile personalFile) {
		personalFileDAO.makePersistent(personalFile);
	}

	/**
	 * Repository service.
	 * 
	 * @return
	 */
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	/**
	 * Repository service.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**
	 * User data access object.
	 * 
	 * @return
	 */
	public IrUserDAO getIrUserDAO() {
		return irUserDAO;
	}

	/**
	 * Set the user data access object.
	 * 
	 * @param irUserDAO
	 */
	public void setIrUserDAO(IrUserDAO irUserDAO) {
		this.irUserDAO = irUserDAO;
	}

	/**
	 * Access control list service.
	 * 
	 * @return
	 */
	public SecurityService getSecurityService() {
		return securityService;
	}

	/**
	 * Access control list service.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	/**
	 * Service for inviting users to collaborate on files.
	 * 
	 * @return
	 */
	public InviteUserService getInviteUserService() {
		return inviteUserService;
	}

	/**
	 * Service for inviting users to collaborate.
	 * 
	 * @param inviteUserService
	 */
	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}

	/**
	 * Get all versioned files for the specified folder.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getAllVersionedFilesForFolder(edu.ur.ir.user.PersonalFolder)
	 */
	public List<VersionedFile> getAllVersionedFilesForFolder(
			PersonalFolder folder) {
		return personalFolderDAO.getAllVersionedFilesForFolder(folder);
	}
	
	/**
	 * Creates an index folder for a user if one does not exist.  The creates a folder
	 * with the given name in the top level repository user index location.
	 * 
	 * @param user - user 
	 * @param repository 
	 * @param folderName
	 * @throws LocationAlreadyExistsException - if the folder location already exists
	 * @throws IOException 
	 */
	public void createIndexFolder(IrUser user, Repository repository, String folderName) throws LocationAlreadyExistsException, IOException
	{
		if( user.getPersonalIndexFolder() == null || user.getPersonalIndexFolder().equals(""))
		{
			// get the top level folder structure for user index folders
			String userTopLevelIndexFolder = repository.getUserWorkspaceIndexFolder();
			// make sure end separator exists
			if (userTopLevelIndexFolder.charAt(userTopLevelIndexFolder.length() - 1) != IOUtils.DIR_SEPARATOR) 
		    {
				userTopLevelIndexFolder = userTopLevelIndexFolder + IOUtils.DIR_SEPARATOR;
		    }
			
			String userFolder = userTopLevelIndexFolder + folderName + IOUtils.DIR_SEPARATOR;
			
			File f = new File(userFolder);
			
			if( f.exists())
			{
				throw new LocationAlreadyExistsException("location already exists", userFolder);
			}
			
			FileUtils.forceMkdir(f);
			
			user.setPersonalIndexFolder(userFolder);
			irUserDAO.makePersistent(user);
		}
	}
	
	/**
	 * Delete a users index folder location.  
	 * 
	 * @param user - user who's folder is to be deleted.
	 * @throws IOException
	 */
	public void deleteIndexFolder(IrUser user) throws IOException
	{
		 String path = user.getPersonalIndexFolder();
		 if( path != null && !path.trim().equals(""))
		 {
	         File workspaceIndexFolder = new File(path);
	         if( !workspaceIndexFolder.isDirectory())
	         {
	    	     throw new IllegalStateException("user's workspace folder is not a directory " + workspaceIndexFolder.getAbsolutePath());
	         }
	         if( workspaceIndexFolder.exists())
	         {
	    	     if( log.isDebugEnabled())
	    	     {
	    	         log.debug("deleting user workspace index folder " + workspaceIndexFolder.getAbsolutePath());
	    	     }
	    	 
	    	     FileUtils.forceDelete(workspaceIndexFolder);
	    	     user.setPersonalIndexFolder(null);
	    	     irUserDAO.makePersistent(user);
	         }
	     }
	     else
	     {
	    	 log.debug("no workspace folder for user " + user);
	     }
	}
	
	/**
	 * Return a count of the files in the specified users 
	 * shared in-box.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getSharedFileInboxCount(edu.ur.ir.user.IrUser)
	 */
	public Long getSharedFileInboxCount(IrUser user) {
		return sharedInboxFileDAO.getSharedInboxFileCount(user);
	}
	
	/**
	 * Get the shared inbox files for specified user
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getSharedFileInboxCount(edu.ur.ir.user.IrUser)
	 */
	public List<SharedInboxFile> getSharedInboxFiles(IrUser user) {
		return sharedInboxFileDAO.getSharedInboxFiles(user);
	}

	/**
	 * Data access object for dealing with shared in-box files
	 * 
	 * @return
	 */
	public SharedInboxFileDAO getSharedInboxFileDAO() {
		return sharedInboxFileDAO;
	}

	/**
	 * Data access object for dealing with shared in-box files
	 * 
	 * @param sharedInboxFileDAO
	 */
	public void setSharedInboxFileDAO(SharedInboxFileDAO sharedInboxFileDAO) {
		this.sharedInboxFileDAO = sharedInboxFileDAO;
	}
	
	/**
	 * Get the shared in-box file for the user.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getSharedInboxFile(java.lang.Long, boolean)
	 */
	public SharedInboxFile getSharedInboxFile(Long id, boolean lock) {
		return sharedInboxFileDAO.getById(id, lock);
	}
	
	/**
	 * Adds the shared inbox file to the specified folder.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#addSharedInboxFileToFolders(edu.ur.ir.user.PersonalFolder, edu.ur.ir.user.SharedInboxFile)
	 */
	public PersonalFile addSharedInboxFileToFolders(PersonalFolder folder,
			SharedInboxFile inboxFile) throws DuplicateNameException {
		PersonalFile pf = folder.addVersionedFile(inboxFile.getVersionedFile());
		personalFolderDAO.makePersistent(folder);	
		return pf;
	}

	/**
	 * Adds the shared inbox file to the user as a root file.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#addSharedInboxFileToFolders(edu.ur.ir.user.IrUser, edu.ur.ir.user.SharedInboxFile)
	 */
	public PersonalFile addSharedInboxFileToFolders(IrUser user,
			SharedInboxFile inboxFile) throws DuplicateNameException {
		PersonalFile pf = user.createRootFile(inboxFile.getVersionedFile());
		personalFileDAO.makePersistent(pf);
		irUserDAO.makePersistent(user);
		return pf;
	}
	
	/**
	 * Delete the inbox file
	 * @see edu.ur.ir.user.UserFileSystemService#makeSharedInboFileTransient(edu.ur.ir.user.SharedInboxFile)
	 */
	public void makeSharedInboFileTransient(SharedInboxFile inboxFile) {
		IrUser user = inboxFile.getSharedWithUser();
		user.removeFromSharedFileInbox(inboxFile);
		sharedInboxFileDAO.makeTransient(inboxFile);
		
	}

	/** Save the inbox file
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#makeSharedInboxFilePersistent(edu.ur.ir.user.SharedInboxFile)
	 */
	public void makeSharedInboxFilePersistent(SharedInboxFile inboxFile) {
		sharedInboxFileDAO.makePersistent(inboxFile);	
	}
	
	/**
	 * Returns all files within a folder and its sub folders
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getAllFilesInFolderAndSubFolder(Long, Long)
	 */
	public List<PersonalFile> getAllFilesForFolder(PersonalFolder personalFolder) {
		return personalFolderDAO.getAllFilesForFolder(personalFolder);
	}

	/**
	 * Return the personal file found for the user.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getPersonalFile(edu.ur.ir.user.IrUser, edu.ur.ir.file.VersionedFile)
	 */
	public PersonalFile getPersonalFile(IrUser user, VersionedFile versionedFile) {
		
		return personalFileDAO.getFileForUserWithSpecifiedVersionedFile(user.getId(), versionedFile.getId());
	}
	
	/**
	 * Get the shared inbox files for the specified user.
	 * 
	 * @see edu.ur.ir.user.UserFileSystemService#getSharedInboxFiles(java.lang.Long, java.util.List)
	 */
	public List<SharedInboxFile> getSharedInboxFiles(Long userId, List<Long> fileIds) {
		return sharedInboxFileDAO.getSharedInboxFiles(userId, fileIds);
	} 
	
	/**
	 * Returns size for a folder
	 * 
	 *  @see edu.ur.ir.user.UserFileSystemService#getFolderSize(IrUser, PersonalFolder)
	 */
	public Long getFolderSize(IrUser user, PersonalFolder folder) {
		return personalFolderDAO.getFolderSize(user.getId(), folder.getId());
	}	

	/**
	 * Get the sum of versioned file size for a user
	 * 
	 * @param user user the VersionedFile belongs to
	 * 
	 * @return sum of versioned files size
	 */
	public Long getFileSystemSizeForUser(IrUser user) {
		return repositoryService.getFileSystemSizeForUser(user);
	}

	/**
	 * Get data access for dealing with personal file delete records.
	 * 
	 * @return
	 */
	public PersonalFileDeleteRecordDAO getPersonalFileDeleteRecordDAO() {
		return personalFileDeleteRecordDAO;
	}

	/**
	 * Set data access for dealing with personal file delete records.
	 * 
	 * @param personalFileDeleteRecordDAO
	 */
	public void setPersonalFileDeleteRecordDAO(
			PersonalFileDeleteRecordDAO personalFileDeleteRecordDAO) {
		this.personalFileDeleteRecordDAO = personalFileDeleteRecordDAO;
	}

}
