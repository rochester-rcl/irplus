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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

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
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserAccessControlEntry;
import edu.ur.ir.security.PermissionNotGrantedException;
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
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultGroupWorkspaceFileSystemService.class);


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
	 * Get all the folders for a group workspace
	 * @param workspaceId - id of the group workspace.
	 * 
	 * @return list of group workspace folders
	 */
	public List<GroupWorkspaceFolder> getAllFolders(Long workspaceId)
	{
		return groupWorkspaceFolderDAO.getAllFolders(workspaceId);
	}
	
	/**
	 * Get all files for the workspace.
	 * 
	 * @param groupWorkspaceId - id of the group workspace
	 * @return all the files within the group workspace
	 */
	public List<GroupWorkspaceFile> getAllFiles(Long groupWorkspaceId)
	{
		return groupWorkspaceFileDAO.getAllFiles(groupWorkspaceId);
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
 * @throws PermissionNotGrantedException 
     */
	public GroupWorkspaceFile addFile(Repository repository,
			GroupWorkspace workspace, 
			IrUser user,
			File f, 
			String name, 
			String description) throws DuplicateNameException, IllegalFileSystemNameException, PermissionNotGrantedException {
		if( name == null || name.trim().equals("") )
		{
			throw new IllegalStateException("File name is null");
		}
		GroupWorkspaceFile workspaceFile = null;
		String editPermission = GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION;
		if( securityService.hasPermission(workspace, user, editPermission) > 0 )
		{
		
		    workspaceFile = workspace.getRootFile(name);
		
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
		}
		else
		{
			throw new PermissionNotGrantedException("User does not have permissions to add the file");
		}
	    
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
 * @throws PermissionNotGrantedException 
     */
    public GroupWorkspaceFile addFile(Repository repository, 
            GroupWorkspace workspace, 
    		IrUser user, 
    		String fileName, 
    		String description )throws DuplicateNameException, IllegalFileSystemNameException, PermissionNotGrantedException
    {
    	GroupWorkspaceFile workspaceFile = null;
		String editPermission = GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION;
		if( securityService.hasPermission(workspace, user, editPermission) > 0 )
		{
		    if( fileName == null || fileName.trim().equals("") )
		    {
			    throw new IllegalStateException("File name is null");
		    }
		
		    workspaceFile = workspace.getRootFile(fileName);
		
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
		}
		else
		{
			throw new PermissionNotGrantedException("User does not have permissions to add the file");
		}
		
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
     * @throws PermissionNotGrantedException 
     */
    public GroupWorkspaceFile addFile(Repository repository, 
    		GroupWorkspaceFolder folder, 	
            IrUser user,
    		File f, 
    		String fileName, 
    		String description ) throws DuplicateNameException, IllegalFileSystemNameException, PermissionNotGrantedException
    {
    	GroupWorkspaceFile workspaceFile = null;
		String addPermission = GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION;
		String editPermission = GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION;
		if( securityService.hasPermission(folder, user, addPermission) > 0 ||
				securityService.hasPermission(folder, user, editPermission) > 0	)
		{
		    if( fileName == null || fileName.trim().equals("") )
		    {
			    throw new IllegalStateException("file name is null");
		    }
		
		    workspaceFile = folder.getFile(fileName);
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
       
           try 
           {
			    workspaceFile = folder.addVersionedFile(versionedFile);
		   }
           catch (DuplicateNameException e)
           {
			    repositoryService.deleteVersionedFile(versionedFile);
			    throw e;
		   }
        
		    save(folder);
       
            securityService.assignOwnerPermissions(workspaceFile.getVersionedFile(), 
        		user);
		}
		else
		{
			throw new PermissionNotGrantedException("User does not have permissions to add the file");
		}
        
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
     * @throws PermissionNotGrantedException 
     */
    public GroupWorkspaceFile addFile(Repository repository, 
    		GroupWorkspaceFolder folder, 
    		IrUser user,
    		String fileName, 
    		String description )throws DuplicateNameException, IllegalFileSystemNameException, PermissionNotGrantedException
    {
    	GroupWorkspaceFile workspaceFile = null;
		String addPermission = GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION;
		String editPermission = GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION;
		if( securityService.hasPermission(folder, user, addPermission) > 0 ||
				securityService.hasPermission(folder, user, editPermission) > 0	)
		{
		    if( fileName == null ||fileName.trim().equals("") )
		    {
			    throw new IllegalStateException("file name is null");
		    }
		
		    workspaceFile = folder.getFile(fileName);
		
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
		}
		else
		{
			throw new PermissionNotGrantedException("User does not have permissions to add the file");
		}
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
		List<GroupWorkspaceFolder> folders = groupWorkspaceFolderDAO.getAllFoldersForFolder(folder);

		// delete all the files within folder and sub folders
		for( GroupWorkspaceFile aFile :files)
		{
		    delete(aFile, deletingUser, deleteReason);
		}
		
		//delete all acls for the child folders
		for( GroupWorkspaceFolder childFolder : folders)
		{
			IrAcl acl = securityService.getAcl(childFolder);
			securityService.deleteAcl(acl);
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

		IrAcl fileAcl = securityService.getAcl(folder);
		if( fileAcl != null )
		{
			securityService.deleteAcl(fileAcl);
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
	 * Gives the user permissions for the given group - to all files and folders within the group workspace.
	 * 
	 * @param users - list of users to give permissions for the group to
	 * @param groupWorkspace - group workspace
	 * @param permissions - set of permissions to give
	 */
	public void giveUsersPermissionsToGroupFileSystem(List<IrUser> users, GroupWorkspace groupWorkspace, List<IrClassTypePermission> permissions)
	{
		List<GroupWorkspaceFile> files = getAllFiles(groupWorkspace.getId());
		List<GroupWorkspaceFolder> folders = getAllFolders(groupWorkspace.getId());
		
		List<IrAcl> acls = new LinkedList<IrAcl>();
		
		// get all acls for files and folders within group workspace
		for(GroupWorkspaceFile f : files )
		{
			IrAcl acl = securityService.getAcl(f.getVersionedFile());
			if( acl == null )
			{
				acl = securityService.createAclForObject(f.getVersionedFile());
			}
			acls.add(acl);
		}
		
		for( GroupWorkspaceFolder f : folders)
		{
			IrAcl acl = securityService.getAcl(f);
			if( acl == null )
			{
				acl = securityService.createAclForObject(f);
			}
			acls.add(acl);
		}
		
		if( acls.size() > 0 )
		{
		    securityService.createUserControlEntriesForUsers(users, acls);
			List<IrUserAccessControlEntry> entries = new LinkedList<IrUserAccessControlEntry>();
		    
		    for( IrAcl acl : acls)
			{
				entries.addAll(securityService.getUserControlEntriesForUsers(acl, users));
			}
		    
		    securityService.createPermissionsForUserControlEntries(entries, permissions);
		}
		
	}
	
	/**
	 * Add the root folder to the group workspace.  This will set up all security permissions as needed
	 * based on the group workspace settings
	 * 
	 * @param groupWorkspace - group workspace to add the folder to
	 * @param folderName - name of the folder
	 * @param description - folder description
	 * @param user - user who is adding the folder
	 * 
	 * @return GroupWorkspaceFolder - folder created 
	 * 
	 * @throws DuplicateNameException - if the name already exists in the group workspace
	 * @throws IllegalFileSystemNameException - if there are illegal characters in the file name
	 * @throws PermissionNotGrantedException - if the user does not have permission to edit the group workspace
	 */
	public GroupWorkspaceFolder addFolder(GroupWorkspace groupWorkspace, 
			String folderName, 
			String description, 
			IrUser user) throws DuplicateNameException, IllegalFileSystemNameException, PermissionNotGrantedException
	{
		String editPermission = GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION;
		GroupWorkspaceFolder folder = null;
		// make sure user has permission
		if( securityService.hasPermission(groupWorkspace, user, editPermission) > 0 )
		{
			folder = groupWorkspace.createRootFolder(user, folderName);
			folder.setDescription(description);
			save(folder);
			
			// get the list of permissions for each user in the workspace
			IrAcl acl = securityService.getAcl(groupWorkspace);
			Set<IrUserAccessControlEntry> userEntries = acl.getUserEntries();
			for(IrUserAccessControlEntry entry : userEntries)
			{
				Set<IrClassTypePermission> groupPermissions = entry.getIrClassTypePermissions();
				IrUser entryUser = entry.getIrUser();
				
				// set of permissions to give each user for the folder
				List<IrClassTypePermission> fileSystemPermissions = new LinkedList<IrClassTypePermission>();
			    for(IrClassTypePermission permission : groupPermissions)
			    {
				    if( permission.getName().equals(GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION))
				    {
				    	fileSystemPermissions.addAll(securityService.getClassTypePermissions(GroupWorkspaceFolder.class.getName()));
				    }
				    else if( permission.getName().equals(GroupWorkspace.GROUP_WORKSPACE_ADD_FILE_PERMISSION))
				    {
				    	fileSystemPermissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_READ_PERMISSION));
				    	fileSystemPermissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION));
				    }
				    else if( permission.getName().equals(GroupWorkspace.GROUP_WORKSPACE_READ_PERMISSION))
				    {
				    	fileSystemPermissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_READ_PERMISSION));
				    }
			    }
			    
			    if( fileSystemPermissions.size() > 0 )
			    {
			        securityService.createPermissions(folder, entryUser, fileSystemPermissions);
			    }
			    
			}
		}
		else
		{
			throw new PermissionNotGrantedException(GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
		}
		
		return folder;
	}
	
	/**
	 * Create a folder in the given parent folder.  This will set up all security permissions as needed
	 * based on the group workspace settings.
	 * 
	 * @param parentFolder - parent folder to add the folder to
	 * @param name - name to give the folder 
	 * @param description
	 * @param user
	 * 
	 * @throws IllegalFileSystemNameException  - name contains illegal characters
	 * @throws DuplicateNameException  - name already exists in the folder
	 * @throws PermissionNotGrantedException  - user does not have permission to edit the parent folder
	 */
	public GroupWorkspaceFolder addFolder(GroupWorkspaceFolder parentFolder, String name, String description, IrUser user) throws DuplicateNameException, IllegalFileSystemNameException, PermissionNotGrantedException
	{
		String editPermission = GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION;
		GroupWorkspaceFolder folder = null;
		// make sure user has permission
		if( securityService.hasPermission(parentFolder, user, editPermission) > 0 )
		{
			folder = parentFolder.createChild(name, user);
			folder.setDescription(description);
			save(folder);
			
			// get the list of permissions for each user in the parent folder
			IrAcl acl = securityService.getAcl(parentFolder);
			Set<IrUserAccessControlEntry> userEntries = acl.getUserEntries();
			for(IrUserAccessControlEntry entry : userEntries)
			{
				Set<IrClassTypePermission> groupPermissions = entry.getIrClassTypePermissions();
				IrUser entryUser = entry.getIrUser();
				
				// set of permissions to give each user for the folder
				List<IrClassTypePermission> fileSystemPermissions = new LinkedList<IrClassTypePermission>();
			    for(IrClassTypePermission permission : groupPermissions)
			    {
				    if( permission.getName().equals(GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION))
				    {
				    	fileSystemPermissions.addAll(securityService.getClassTypePermissions(GroupWorkspaceFolder.class.getName()));
				    }
				    else if( permission.getName().equals(GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION))
				    {
				    	fileSystemPermissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_READ_PERMISSION));
				    	fileSystemPermissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION));
				    }
				    else if( permission.getName().equals(GroupWorkspaceFolder.FOLDER_READ_PERMISSION))
				    {
				    	fileSystemPermissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_READ_PERMISSION));
				    }
			    }
			    
			    if( fileSystemPermissions.size() > 0 )
			    {
			        securityService.createPermissions(folder, entryUser, fileSystemPermissions);
			    }
			    
			}
		}
		else
		{
			throw new PermissionNotGrantedException(GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
		}
		
		return folder;
	}
	
	/**
	 * Remove all permissions from the group file system for the given user.
	 * 
	 * @param user - user to remove all permissions from
	 * @param groupWorkspace - group workspace to remove all permissions from.
	 */
	public void removeUserPermissionsFromGroupFileSystem(IrUser user, GroupWorkspace groupWorkspace)
	{
		List<GroupWorkspaceFile> files = getAllFiles(groupWorkspace.getId());
		List<GroupWorkspaceFolder> folders = getAllFolders(groupWorkspace.getId());
		// get all acls for files and folders within group workspace
		for(GroupWorkspaceFile f : files )
		{
			securityService.deletePermissions(f.getId(), CgLibHelper.cleanClassName(f.getClass().getName()), user);
		}
		
		for( GroupWorkspaceFolder f : folders)
		{
			securityService.deletePermissions(f.getId(), CgLibHelper.cleanClassName(f.getClass().getName()), user);
		}
		
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
