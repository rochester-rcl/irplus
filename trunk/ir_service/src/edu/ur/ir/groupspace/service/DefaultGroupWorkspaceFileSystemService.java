/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.ir.groupspace.service;

import java.io.File;
import java.util.List;

import edu.ur.cgLib.CgLibHelper;
import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFileDeleteRecord;
import edu.ur.ir.groupspace.GroupWorkspaceFileDeleteRecordDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceFolderDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;

/**
 * Group workspace file system service to allow for the management and
 * retrieval for group workspace file system information.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultGroupWorkspaceFileSystemService implements GroupWorkspaceFileSystemService {
	
	/* eclipse generated id */
	private static final long serialVersionUID = -1638366740720252837L;

	/* Group workspace folder data access object. */
	private GroupWorkspaceFolderDAO groupWorkspaceFolderDAO;

	/* Group workspace file data access object */
	private GroupWorkspaceFileDAO groupWorkspaceFileDAO;
	
	/* Group workspace file delete record data access object */
	private GroupWorkspaceFileDeleteRecordDAO groupWorkspaceFileDeleteRecordDAO;

	// Service class for dealing with the repository  
	private RepositoryService repositoryService;
		
	// Service class for dealing with the (A)cess (C)ontrol (L)ists 
	private SecurityService securityService;


	/**
	 * Get the group workspace folder.
	 * 
	 * @param id - id of the group workspace folder
 	 * @param lock - if set to true upgrade the lock mode.
 	 * 
	 * @return - the group workspace folder
	 */
	public GroupWorkspaceFolder getFolder(Long id, boolean lock)
	{
		return groupWorkspaceFolderDAO.getById(id, lock);
	}
	
	/**
	 * Get the group workspace file.
	 * 
	 * @param id - id of the group workspace file
 	 * @param lock - if set to true upgrade the lock mode.
 	 * 
	 * @return - the group workspace file or null if not found
	 */
	public GroupWorkspaceFile getFile(Long id, boolean lock)
	{
		return groupWorkspaceFileDAO.getById(id, lock);
	}
	
	/**
	 * Get the path to the folder.
	 * 
	 * @param parentFolderId - id of the parent folder
	 * 
	 * @return - list of all folders in order - parent to child
	 */
	public List<GroupWorkspaceFolder> getFolderPath(GroupWorkspaceFolder folder)
	{
		return groupWorkspaceFolderDAO.getFolderPath(folder);
	}
	
	/**
	 * Get sub folders within parent folder for a group workspace
	 * 
	 * @param workspaceId Id of the group workspace containing the folders
	 * @param parentFolderId Id of the parent folder to start at - can be at any point
	 * in the tree
	 * 
	 * @return List of sub folders within the parent folder
	 */
	public List<GroupWorkspaceFolder> getFolders(Long workspaceId, Long parentFolderId )
	{
		if(  parentFolderId == null || parentFolderId == ROOT_FOLDER_ID )
		{
		    return groupWorkspaceFolderDAO.getRootFolders(workspaceId);
		}
		else
		{
		    return groupWorkspaceFolderDAO.getFolders(workspaceId, parentFolderId);
		}
	}

	/**
	 * Get personal files for a group workspace in the specified folder.  Using the root folder
	 * id indicates Group workspace files
	 * 
	 * @param workspaceId Id of the user having the files
	 * @param parentFolderId Id of the folder containing the files
	 * 
	 * @return List of files in the folder
	 */
	public List<GroupWorkspaceFile> getFiles(Long workspaceId, Long parentFolderId)
	{
		if(  parentFolderId == null || parentFolderId == ROOT_FOLDER_ID )
		{
		    return groupWorkspaceFileDAO.getRootFiles(workspaceId);
		}
		else
		{
		    return groupWorkspaceFileDAO.getFiles(workspaceId, parentFolderId);
		}
	}
	
	/**
	 * Save the group workspace folder into persistent storage.
	 * 
	 * @param groupWorkspaceFolder
	 */
	public void save(GroupWorkspaceFolder groupWorkspaceFolder)
	{
		groupWorkspaceFolderDAO.makePersistent(groupWorkspaceFolder);
	}
	
	/**
	 * Set the group workspace folder data access object.
	 * 
	 * @param groupWorkspaceFolderDAO
	 */
	public void setGroupWorkspaceFolderDAO(
			GroupWorkspaceFolderDAO groupWorkspaceFolderDAO) {
		this.groupWorkspaceFolderDAO = groupWorkspaceFolderDAO;
	}

	/**
	 * Set the group workspace file data access object.
	 * 
	 * @param groupWorkspaceFileDAO
	 */
	public void setGroupWorkspaceFileDAO(GroupWorkspaceFileDAO groupWorkspaceFileDAO) {
		this.groupWorkspaceFileDAO = groupWorkspaceFileDAO;
	}

   /**
     * Create a group workspace versioned file in the system with the specified file for the
     * given workspace. This is created at the root level (added to the group workspace)
     * 
     * @param repositoryId - the repository to add the file to.
     * @param group workspace - workspace to add the file to
     * @param f - file to add
     * @param name - name to give the file
     * @param description - description of the file.
     * 
     * @return the created group workspace file
     * @throws DuplicateNameException 
     * @throws IllegalFileSystemNameException 
     */
	public GroupWorkspaceFile addFile(Repository repository,
			GroupWorkspace workspace, 
			IrUser user,
			File f, 
			String name, 
			String description) throws DuplicateNameException, IllegalFileSystemNameException {
		if( name == null || name.trim().equals("") )
		{
			throw new IllegalStateException("File name is null");
		}
		
		GroupWorkspaceFile workspaceFile = workspace.getRootFile(name);
		
		if(  workspaceFile != null )
		{
			throw new DuplicateNameException("File with the name " + 
					name + " already exists ", name);
		}
		
		VersionedFile versionedFile = 
	    	repositoryService.createVersionedFile(user, repository, 
	    			f, name, description);
		
		try
		{
		   
	       workspaceFile = workspace.createRootFile(versionedFile);
		}
		catch(DuplicateNameException dne)
		{
			repositoryService.deleteVersionedFile(versionedFile);
			throw dne;
		}
	    groupWorkspaceFileDAO.makePersistent(workspaceFile);
	    
	    securityService.assignOwnerPermissions(workspaceFile.getVersionedFile(), 
        		user);
	    
	    return workspaceFile;
	}

	/**
	 * Set the repository service.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

   /**
     * Create a group workspace versioned file in the system with an empty file for the
     * given workspace. This is created at the root level (added to the group workspace)
     * 
     * @param Repository - the repository to add the file to.
     * @param workspace - group workspace to add the empty file to
     * @param user - User creating the file
     * @param fileName - The name to give the file.
     * @param description - description of the file.
     * 
     * @return the created workspace file
     */
    public GroupWorkspaceFile addFile(Repository repository, 
            GroupWorkspace workspace, 
    		IrUser user, 
    		String fileName, 
    		String description )throws DuplicateNameException, IllegalFileSystemNameException
    {
		if( fileName == null || fileName.trim().equals("") )
		{
			throw new IllegalStateException("File name is null");
		}
		
		GroupWorkspaceFile workspaceFile = workspace.getRootFile(fileName);
		
		if(  workspaceFile != null )
		{
			throw new DuplicateNameException("File with the name " + 
					fileName + " already exists ", fileName);
		}
		VersionedFile versionedFile = 
	    	repositoryService.createVersionedFile(user, repository, 
	    			fileName, description);
		try
		{
	        workspaceFile = workspace.createRootFile(versionedFile);
		}
		catch(DuplicateNameException dne)
		{
				repositoryService.deleteVersionedFile(versionedFile);
				throw dne;
		}
	    groupWorkspaceFileDAO.makePersistent(workspaceFile);
	    securityService.assignOwnerPermissions(workspaceFile.getVersionedFile(), user);
		
	    return workspaceFile;	
    }
    
    /**
     * Create a group workspace versioned file in the system with the specified file for the
     * given user. 
     * 
     * @param repositoryId - the repository to add the file to.
     * @param folder - group workspace folder to add the file to.  
     * @param user - user creating the file 
     * @param f - file to add
     * @param fileName - The name to give the file.
     * @param description - description of the file.
     * 
     * @return the created personal file
     */
    public GroupWorkspaceFile addFile(Repository repository, 
    		GroupWorkspaceFolder folder, 	
            IrUser user,
    		File f, 
    		String fileName, 
    		String description ) throws DuplicateNameException, IllegalFileSystemNameException
    {
		if( fileName == null || fileName.trim().equals("") )
		{
			throw new IllegalStateException("file name is null");
		}
		
		GroupWorkspaceFile workspaceFile = folder.getFile(fileName);
		if(  workspaceFile != null )
		{
			throw new DuplicateNameException("File with the name " + 
					fileName + " already exists ", fileName);
		}
		
        VersionedFile versionedFile = 
           repositoryService.createVersionedFile(user, 
        		        repository, 
            			f, 
            			fileName, 
            			description);
       
        try {
			workspaceFile = folder.addVersionedFile(versionedFile);
		} catch (DuplicateNameException e) {
			repositoryService.deleteVersionedFile(versionedFile);
			throw e;
		}
        
		save(folder);
       
        securityService.assignOwnerPermissions(workspaceFile.getVersionedFile(), 
        		user);
        
		return workspaceFile;
    }
    
    
    /**
     * Create a group workspace versioned file in the system with an empty file for the
     * given user. 
     * 
     * @param repository - the repository to add the file to.
     * @param folder - workspace folder to add the file to. 
     * @param user - user adding the file
     * @param fileName - The name to give the file.
     * @param description - description of the file.
     * 
     * @return the created group workspace file
     */
    public GroupWorkspaceFile addFile(Repository repository, 
    		GroupWorkspaceFolder folder, 
    		IrUser user,
    		String fileName, 
    		String description )throws DuplicateNameException, IllegalFileSystemNameException
    {
		if( fileName == null ||fileName.trim().equals("") )
		{
			throw new IllegalStateException("file name is null");
		}
		
		GroupWorkspaceFile workspaceFile = folder.getFile(fileName);
		
		if(  workspaceFile != null )
		{
			throw new DuplicateNameException("File with the name " + 
					fileName + " already exists ", fileName);
		}
		
        VersionedFile versionedFile = 
            	repositoryService.createVersionedFile(user, 
            			repository, 
            			fileName, 
            			description);
        
        // this is still needed 
        try {
				workspaceFile = folder.addVersionedFile(versionedFile);
		} catch (DuplicateNameException e) {
				repositoryService.deleteVersionedFile(versionedFile);
				throw e;
		}
        save(folder);
     
        securityService.assignOwnerPermissions(workspaceFile.getVersionedFile(), 
        		user);
        
        return workspaceFile;	
    }
    
    /**
     * Delete the group workspace folder. 
     * 
     * @param folder - folder to delete
     * @param deletingUser - user performing the delete
     * @param deleteReason - reason for the delete.
     */
	public void delete(GroupWorkspaceFolder folder, IrUser deletingUser, String deleteReason) {
		List<GroupWorkspaceFile> files = groupWorkspaceFolderDAO.getAllFilesForFolder(folder);

		// delete all the files within folder and sub folders
		for( GroupWorkspaceFile aFile :files)
		{
		    delete(aFile, deletingUser, deleteReason);
		}
		
		if( folder.getIsRoot())
		{
			GroupWorkspace owningWorkspace = folder.getGroupWorkspace();
			owningWorkspace.removeRootFolder(folder);
		}
		else
		{
			GroupWorkspaceFolder parent = folder.getParent();
			parent.removeChild(folder);
		}

		groupWorkspaceFolderDAO.makeTransient(folder);

	}
	
	
	/**
	 * Delete a group workspace file.
	 * 
	 * @param personalFileId
	 */
	public void delete(GroupWorkspaceFile gf, IrUser deletingUser, String deleteReason)
	{
		VersionedFile versionedFile = gf.getVersionedFile();
		GroupWorkspace groupWorkspace = gf.getGroupWorkspace();
		
		// create a delete record 
		GroupWorkspaceFileDeleteRecord groupWorkspaceFileDeleteRecord = new GroupWorkspaceFileDeleteRecord(deletingUser.getId(),
				gf.getId(),
				groupWorkspace.getId(),
				groupWorkspace.getName(),
				gf.getFullPath(), 
				gf.getDescription());
		groupWorkspaceFileDeleteRecord.setDeleteReason(deleteReason);
		
		// remove the file from the parent folder
		if( gf.getGroupWorkspaceFolder() != null )
		{
		    gf.getGroupWorkspaceFolder().removeGroupFile(gf);
		}
		
		// delete the personal file
		groupWorkspaceFileDAO.makeTransient(gf);
		
		groupWorkspaceFileDeleteRecordDAO.makePersistent(groupWorkspaceFileDeleteRecord);
		
	
		securityService.deleteAcl(versionedFile.getId(), CgLibHelper.cleanClassName(versionedFile.getClass().getName()));
		repositoryService.deleteVersionedFile(versionedFile);
			
	
	}
    
	/**
	 * Set the security service.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	/**
	 * Set the group workspace file delete record dao.
	 * 
	 * @return group workspace file delete record data access object
	 */
	public GroupWorkspaceFileDeleteRecordDAO getGroupWorkspaceFileDeleteRecordDAO() {
		return groupWorkspaceFileDeleteRecordDAO;
	}

	/**
	 * Set the group workspace file delete record data access object.
	 * 
	 * @param groupWorkspaceFileDeleteRecordDAO
	 */
	public void setGroupWorkspaceFileDeleteRecordDAO(
			GroupWorkspaceFileDeleteRecordDAO groupWorkspaceFileDeleteRecordDAO) {
		this.groupWorkspaceFileDeleteRecordDAO = groupWorkspaceFileDeleteRecordDAO;
	}
}
