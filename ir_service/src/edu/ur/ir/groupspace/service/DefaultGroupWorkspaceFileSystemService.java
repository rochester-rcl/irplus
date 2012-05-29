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
import edu.ur.ir.FileSystem;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFileDeleteRecord;
import edu.ur.ir.groupspace.GroupWorkspaceFileDeleteRecordDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceFolderDAO;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.groupspace.UserHasParentFolderPermissionsException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserAccessControlEntry;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrRole;
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
	
	// service to deal with group worskpace information
	private GroupWorkspaceService groupWorkspaceService;
	


	//  Get the logger for this class 
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
	 * Find the folder for the specified folder name and 
	 * group workspace folder id.
	 * 
	 * @param name of the folder
	 * @param parentId id of the parent folder
	 * @return the found folder or null if the folder is not found.
	 */
	public GroupWorkspaceFolder getFolder(String name, Long parentId)
	{
		return groupWorkspaceFolderDAO.getFolder(name, parentId);
	}
	
	/**
	 * Find the root folder for the specified folder name and 
	 * group workspace id.
	 * 
	 * @param name of the folder
	 * @param groupWorkspaceId - id of the group workspace
	 * @return the found root folder or null if the folder is not found.
	 */
	public GroupWorkspaceFolder getRootFolder(String name, Long groupWorkspaceId)
	{
		return groupWorkspaceFolderDAO.getRootFolder(name, groupWorkspaceId);
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
		if( securityService.hasPermission(workspace, user, editPermission)  )
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
	        
	        addPermissionsForGroupUsers(workspace, workspaceFile);
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
		if( securityService.hasPermission(workspace, user, editPermission)  )
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
	        addPermissionsForGroupUsers(workspace, workspaceFile);
		}
		else
		{
			throw new PermissionNotGrantedException("User does not have permissions to add the file");
		}
		
	    return workspaceFile;	
    }
    
    //add permissions to the versioned file being added to the root of the group workspace for 
    // all users that have permissions 
    private void addPermissionsForGroupUsers(GroupWorkspace workspace, GroupWorkspaceFile file)
    {
        // get the list of permissions for each user in the workspace
		IrAcl acl = securityService.getAcl(workspace);
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
			    	fileSystemPermissions.addAll(securityService.getClassTypePermissions(VersionedFile.class.getName()));
			    }
			    else if( permission.getName().equals(GroupWorkspace.GROUP_WORKSPACE_ADD_FILE_PERMISSION) ||
			    		 permission.getName().equals(GroupWorkspace.GROUP_WORKSPACE_READ_PERMISSION) )
			    {
			    	fileSystemPermissions.add(securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.VIEW_PERMISSION));
			    }
			 
		    }
		    
		    if( fileSystemPermissions.size() > 0 )
		    {
		        securityService.createPermissions(file.getVersionedFile(), entryUser, fileSystemPermissions);
		    }
	    }
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
		if( securityService.hasPermission(folder, user, addPermission)  ||
				securityService.hasPermission(folder, user, editPermission) 	)
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
            addPermissionsForFolderUsers(folder, workspaceFile);
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
		if( securityService.hasPermission(folder, user, addPermission) ||
				securityService.hasPermission(folder, user, editPermission) 	)
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
            addPermissionsForFolderUsers(folder, workspaceFile);
		}
		else
		{
			throw new PermissionNotGrantedException("User does not have permissions to add the file");
		}
        return workspaceFile;	
    }
    
     //add permissions to the versioned file being added to the folder in the group workspace for 
    // all users that have permissions 
    private void addPermissionsForFolderUsers(GroupWorkspaceFolder folder, GroupWorkspaceFile file)
    {
    	
    	// get the list of permissions for each user in the workspace
		IrAcl acl = securityService.getAcl(folder);
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
			    	fileSystemPermissions.addAll(securityService.getClassTypePermissions(VersionedFile.class.getName()));
			    }
			    else if( permission.getName().equals(GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) ||
			    		 permission.getName().equals(GroupWorkspaceFolder.FOLDER_READ_PERMISSION) )
			    {
			    	fileSystemPermissions.add(securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.VIEW_PERMISSION));
			    }
			 
		    }
		    
		    if( fileSystemPermissions.size() > 0 )
		    {
		        securityService.createPermissions(file.getVersionedFile(), entryUser, fileSystemPermissions);
		    }
		    
		}
    }
    
    /**
     * Delete the group workspace folder. 
     * 
     * @param folder - folder to delete
     * @param deletingUser - user performing the delete
     * @param deleteReason - reason for the delete.
     * 
     * @throws PermissionNotGrantedException 
     */
	public void delete(GroupWorkspaceFolder folder, IrUser deletingUser, String deleteReason) throws PermissionNotGrantedException {
		
		GroupWorkspaceUser workspaceUser = folder.getGroupWorkspace().getUser(deletingUser);
		String editPermission = GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION;
		
		if( !deletingUser.hasRole(IrRole.ADMIN_ROLE) )
		{
			if( !workspaceUser.isOwner() &&  
				!securityService.hasPermission(folder, deletingUser, editPermission)  )
			{
				throw new PermissionNotGrantedException("User does not have permission to delete the folder");
			}
		}
		
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
			if( acl != null )
			{
			    securityService.deleteAcl(acl);
			}
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
	 * Change the user permissions for the specified file.
	 * 
	 * @param user - user to change the permissions for
	 * @param groupWorkspaceFile - the file to change the permissions on
	 * @param permissions - set of permissions the user should have for the file.  If no permissions
	 * are listed all permissions are removed for the file.
	 * 
	 * @throws UserHasParentFolderPermissionsException - if the permissions for the parent folder or group workspace give the
	 * user full control over the child file
	 */
	public void changeUserPermissionsForFile(IrUser user, GroupWorkspaceFile groupWorkspaceFile,
			Set<IrClassTypePermission> permissions) throws UserHasParentFolderPermissionsException
	{
		log.debug("adding permissions for user " + user);
		
		// figure out which permissions are set
		boolean hasRead = false;
		boolean hasEdit = false;
		
		// get file permissions
		IrClassTypePermission fileViewPermission = securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.VIEW_PERMISSION);
		IrClassTypePermission fileEditPermission = securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.EDIT_PERMISSION);
		IrClassTypePermission fileSharePermission = securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.SHARE_PERMISSION);
		
		
		if( permissions != null )
		{
		    for(IrClassTypePermission permission : permissions)
		    {
			    log.debug("permission = " + permission);
			    log.debug(" compare to " + fileViewPermission);
			    if(permission.equals(fileViewPermission) )
			    {
				    hasRead = true;
			    }
			    
			    log.debug(" compare to " + fileEditPermission);
			    if( permission.equals(fileEditPermission))
			    {
				    hasEdit = true;
			    }
		    }
		}
		GroupWorkspace groupWorkspace = groupWorkspaceFile.getGroupWorkspace();
		GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
		
		if( !hasEdit )
		{
			// this means a user is trying to remove permissions from a folder where a parent folder gives the user
			// full control over it's children
			if(workspaceUser.isOwner() || securityService.hasPermission(groupWorkspace, user, GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION) )
			{
				throw new UserHasParentFolderPermissionsException("user = " + user + "child file = " + groupWorkspaceFile);
			}
			if( groupWorkspaceFile.getGroupWorkspaceFolder() != null )
			{
				if( securityService.hasPermission( groupWorkspaceFile.getGroupWorkspaceFolder(), user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION)  ||
						userHasParentFolderEditPermissions(user,  groupWorkspaceFile.getGroupWorkspaceFolder()) )
				{
					throw new UserHasParentFolderPermissionsException("user = " + user + "child file = " + groupWorkspaceFile);
				}
				
			}
		}
		
		IrAcl acl = securityService.getAcl(groupWorkspaceFile.getVersionedFile());
		if( acl == null )
		{
			acl = securityService.createAclForObject(groupWorkspaceFile.getVersionedFile());
		}
		
		IrUserAccessControlEntry uace = acl.getUserAccessControlEntry(user.getId());
		if( uace == null )
		{
			uace = acl.createUserAccessControlEntry(user);
		}
		
		if( hasEdit )
		{
			log.debug("adding edit and share permission to file " + groupWorkspaceFile);
		    uace.addPermission(fileEditPermission); 
		    uace.addPermission(fileSharePermission);
		    hasRead = true;
		}
		else
		{
			uace.removePermission(fileEditPermission);
			uace.removePermission(fileSharePermission);
		}
		
		if(hasRead)
		{
			log.debug("adding read permission to file " + groupWorkspaceFile);
			uace.addPermission(fileViewPermission);
		}
		else
		{
			uace.removePermission(fileViewPermission);
		}
		securityService.save(acl);

		
	}
	
	/**
	 * Change the permission on a given folder and it's children folders and files.  If
	 * the permissions contain edit permissions for the folder, all child folders and files
	 * will be updated with the edit permission Regardless of the applyToChildren flag.
	 * 
	 * @param user - user to change the permissions for
	 * @param groupWorkspaceFolder - folder to change the permissions ons
	 * @param permissions - permissions to give
	 * @param applyToChildren - apply the permission to children
	 * 
	 * @throws UserHasParentFolderPermissionsException - if parent folder has edit permissions that would override the
	 *  change in any of the child files/folders.
	 */
	public void changeUserPermissionsForFolder(IrUser user, GroupWorkspaceFolder 
			groupWorkspaceFolder, 
			Set<IrClassTypePermission> permissions, 
			boolean applyToChildren) throws UserHasParentFolderPermissionsException
	{
		
		
		log.debug("changing permissions for user " + user);
		
		// figure out which permissions are set
		boolean hasRead = false;
		boolean hasEdit = false;
		boolean hasAddFile = false;
		
		// get folder permissions
		IrClassTypePermission folderReadPermission = securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_READ_PERMISSION);
		IrClassTypePermission folderAddFilePermission = securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION);
		IrClassTypePermission folderEditPermission = securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION);
		
		// get file permissions
		IrClassTypePermission fileViewPermission = securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.VIEW_PERMISSION);
		IrClassTypePermission fileEditPermission = securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.EDIT_PERMISSION);
		IrClassTypePermission fileSharePermission = securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.SHARE_PERMISSION);
		
		
		if( permissions != null )
		{
		    for(IrClassTypePermission permission : permissions)
		    {
			    log.debug("permission = " + permission);
			    log.debug(" compare to " + folderReadPermission);
			    if(permission.equals(folderReadPermission) )
			    {
				    hasRead = true;
			    }
			    log.debug(" compare to " + folderAddFilePermission);
			    if( permission.equals(folderAddFilePermission))
			    {
				    hasAddFile = true;
			    }
			    log.debug(" compare to " + folderEditPermission);
			    if( permission.equals(folderEditPermission))
			    {
				    hasEdit = true;
			    }
		    }
		}
		
		GroupWorkspace groupWorkspace = groupWorkspaceFolder.getGroupWorkspace();
		GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
		
		// this means a user is trying to remove permissions from a folder where a parent folder gives the user
		// full control over it's children
		if( !hasEdit && (workspaceUser.isOwner() || 
				         securityService.hasPermission(groupWorkspace, user, GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION)  ||
				         userHasParentFolderEditPermissions(user, groupWorkspaceFolder) ) 
		   )
		{
			throw new UserHasParentFolderPermissionsException("user = " + user + "child folder = " + groupWorkspaceFolder);
		}
		
		
		log.debug("hasRead " + hasRead + " has AddFile = " + hasAddFile + " hasEdit = " + hasEdit + " applytoChildren = " + applyToChildren);
		if( hasEdit || applyToChildren)
		{
			log.debug("hasEdit = " + hasEdit + " applytoChildren = " + applyToChildren);
			List<GroupWorkspaceFile> files = groupWorkspaceFolderDAO.getAllFilesForFolder(groupWorkspaceFolder);
			List<GroupWorkspaceFolder> folders = groupWorkspaceFolderDAO.getAllFoldersForFolder(groupWorkspaceFolder);
			
			// deal with files
			for(GroupWorkspaceFile f : files )
			{
				IrAcl acl = securityService.getAcl(f.getVersionedFile());
				if( acl == null )
				{
					acl = securityService.createAclForObject(f.getVersionedFile());
				}
				
				IrUserAccessControlEntry uace = acl.getUserAccessControlEntry(user.getId());
				if( uace == null )
				{
					uace = acl.createUserAccessControlEntry(user);
				}
				
				if( hasEdit )
				{
					log.debug("adding edit and share permission to file " + f);
				    uace.addPermission(fileEditPermission); 
				    uace.addPermission(fileSharePermission);
				    hasRead = true;
				}
				else
				{
					uace.removePermission(fileEditPermission);
					uace.removePermission(fileSharePermission);
				}
				
				if( hasAddFile )
				{
					hasRead = true;
				}
				
				
				if(hasRead)
				{
					log.debug("adding read permission to file " + f);
					uace.addPermission(fileViewPermission);
				}
				else
				{
					uace.removePermission(fileViewPermission);
				}
				securityService.save(acl);
			}
			
			// deal with folders
			for( GroupWorkspaceFolder f : folders)
			{
				IrAcl acl = securityService.getAcl(f);
				if( acl == null )
				{
					acl = securityService.createAclForObject(f);
				}
				IrUserAccessControlEntry uace = acl.getUserAccessControlEntry(user.getId());
				if( uace == null )
				{
					uace = acl.createUserAccessControlEntry(user);
				}
				
				if( hasEdit )
		        {
					log.debug("adding edit permission to folder " + f);
		        	uace.addPermission(folderEditPermission);
		        	hasRead = true;
		        	hasAddFile = true;
		        }
		        else
		        {
		        	uace.removePermission(folderEditPermission);
		        }
		        
		        if( hasAddFile )
		        {
		        	log.debug("adding add file permission to folder " + f);
		        	uace.addPermission(folderAddFilePermission);
		        	hasRead = true;
		        }
		        else
		        {
		        	uace.removePermission(folderAddFilePermission);
		        }
		        
		        if( hasRead )
		        {
		        	log.debug("adding has read permission to folder " + f);
		        	uace.addPermission(folderReadPermission);
		        }
		        else
		        {
		        	uace.removePermission(folderReadPermission);
		        }
		        securityService.save(acl);
			}
		}
	    
		IrAcl userAcl = securityService.getAcl(groupWorkspaceFolder, user);
		if( userAcl == null )
		{
			userAcl = securityService.createAclForObject(groupWorkspaceFolder);
		}
		
		
		IrUserAccessControlEntry uace = userAcl.getUserAccessControlEntryByUserId(user.getId());
		if( uace == null )
		{
			uace = userAcl.createUserAccessControlEntry(user);
		}
		
		if( hasEdit )
		{
		    uace.addPermission(folderEditPermission);
		    hasRead = true;
		    hasAddFile = true;
		}
		else
		{
		    uace.removePermission(folderEditPermission);
		}
		        
		if( hasAddFile )
		{
		    uace.addPermission(folderAddFilePermission);
		    hasRead = true;
		}
		else
		{
		    uace.removePermission(folderAddFilePermission);
		}
			        
		if( hasRead )
		{
		    uace.addPermission(folderReadPermission);
		}
		else
		{
		    uace.removePermission(folderReadPermission);
		}
		securityService.save(userAcl);
	}
	
	/**
	 * Determines if the user has edit permissions in any of the parent folders for the given child folder.
	 * 
	 * @param user - the user to check for edit permissions
	 * @param child - child folder to check parents of
	 * 
	 * @return true if a parent folder has edit permissions.
	 */
	public boolean userHasParentFolderEditPermissions(IrUser user, GroupWorkspaceFolder child)
	{
		return (getFoldersWithEditPermission(user, child).size() > 0);
	}
	
	/**
	 * Get the list of parent folders that the user has edit permissions on.
	 * 
	 * @param user - user to get the permissions for 
	 * @param child - child folder to check parents of
	 *  
	 * @return - list of parent folders the user has edit permissions on or an empty list if there are none.
	 */
	public List<GroupWorkspaceFolder> getFoldersWithEditPermission(IrUser user, GroupWorkspaceFolder child)
	{
		List<GroupWorkspaceFolder> folders = new LinkedList<GroupWorkspaceFolder>();
		
		// get parent folders
		List<GroupWorkspaceFolder> parentFolders = groupWorkspaceFolderDAO.getFolderPath(child);
		for(GroupWorkspaceFolder parent : parentFolders)
		{
			if(!parent.equals(child))
			{
				if( securityService.hasPermission(parent, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION)  )
				{
				    folders.add(parent);
				}
			}
		}
		return folders;
	}
	
	
	/**
	 * Delete a group workspace file.
	 * 
	 * @param personalFileId
	 */
	public void delete(GroupWorkspaceFile gf, IrUser deletingUser, String deleteReason) throws PermissionNotGrantedException
	{
		VersionedFile versionedFile = gf.getVersionedFile();
		GroupWorkspaceUser workspaceUser = gf.getGroupWorkspace().getUser(deletingUser);
		
		if( !deletingUser.hasRole(IrRole.ADMIN_ROLE) )
		{
			if( !workspaceUser.isOwner() &&  
				!securityService.hasPermission(versionedFile, deletingUser, VersionedFile.EDIT_PERMISSION)  )
			{
				throw new PermissionNotGrantedException("User does not have permission to delete the folder");
			}
		}
		
		
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
	public void giveUsersPermissionsToGroupFileSystem(List<IrUser> users, 
			GroupWorkspace groupWorkspace, 
			List<IrClassTypePermission> permissions)
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
		if( securityService.hasPermission(groupWorkspace, user, editPermission)  )
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
		if( securityService.hasPermission(parentFolder, user, editPermission) )
		{
			folder = parentFolder.createChild(name, user);
			folder.setDescription(description);
			save(folder);
			giveUsersFolderPermissions(parentFolder, folder);
		}
		else
		{
			throw new PermissionNotGrantedException(GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
		}
		
		return folder;
	}
	
	//give the new child folder permissions based on parent folder
	private void giveUsersFolderPermissions(GroupWorkspaceFolder parentFolder, GroupWorkspaceFolder newChildFolder)
	{
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
		        securityService.createPermissions(newChildFolder, entryUser, fileSystemPermissions);
		    }
		    
		}
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
			securityService.deletePermissions(f.getVersionedFile().getId(), CgLibHelper.cleanClassName(f.getVersionedFile().getClass().getName()), user);
		}
		
		for( GroupWorkspaceFolder f : folders)
		{
			securityService.deletePermissions(f.getId(), CgLibHelper.cleanClassName(f.getClass().getName()), user);
		}
		
	}
	
	/**
	 * Allow a user to move folders and files to a given group workspace.
	 * 
	 * @param User - user who is doing the moving
	 * @param destination - group workspace folder to move the files to
	 * @param foldersToMove - list of folders to move
	 * @param filesToMove - list of files to move
	 * 
	 * @return list of files and folders that cannot be moved.
	 * @throws PermissionNotGrantedException 
	 */
	public List<FileSystem> moveFolderSystemInformation(IrUser user, GroupWorkspaceFolder destination, 
			List<GroupWorkspaceFolder> foldersToMove, 
			List<GroupWorkspaceFile> filesToMove) throws PermissionNotGrantedException
	{
		
		LinkedList<FileSystem> notMoved = new LinkedList<FileSystem>();
		
		
		// move folders first
		if( foldersToMove != null )
		{
			// if they can't edit the destination then they can't move the folder
			if( !user.hasRole(IrRole.ADMIN_ROLE) && !securityService.hasPermission(destination, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) )
			{
				throw new PermissionNotGrantedException(GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION);
			}
			
			
		    for( GroupWorkspaceFolder folder : foldersToMove)
		    {
		    	
		    	log.debug("Adding folder " + folder + " to destination " + destination);
			   
			    try {
			    	 if( user.hasRole(IrRole.ADMIN_ROLE) || securityService.hasPermission(folder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) )
			    	 {
			    		 destination.addChild(folder);
			    	 }
			    	 else
			    	 {
			    		 notMoved.add(folder);
			    	 }
				} catch (DuplicateNameException e) {
					notMoved.add(folder);
				}
			    
		    }
		}
	
		
		if( filesToMove != null  && notMoved.size() == 0)
		{
			// if they can't add files then throw exception
			if( !user.hasRole(IrRole.ADMIN_ROLE) && !securityService.hasPermission(destination, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) )
			{
				throw new PermissionNotGrantedException(GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION);
			}
		    for( GroupWorkspaceFile file : filesToMove)
		    {
		    	log.debug("Adding file " + file + " to destination " + destination);
			    try {
			    	if( user.hasRole(IrRole.ADMIN_ROLE) || securityService.hasPermission(file.getVersionedFile(), user, VersionedFile.EDIT_PERMISSION) )
			    	{
			            destination.addGroupFile(file);
			    	}
			    	else
			    	{
			    		notMoved.add(file);
			    	}
				} catch (DuplicateNameException e) {
					notMoved.add(file);
				}
		    	
		    }
		}
		
		if( notMoved.size() == 0)
		{
			groupWorkspaceFolderDAO.makePersistent(destination);
			// deal with permissions 
			
			// fix all files selected for move
			if( filesToMove != null )
			{
			    for( GroupWorkspaceFile f : filesToMove )
			    {
				    this.addPermissionsForFolderUsers(destination, f);
			    }
			}
			
			if( foldersToMove != null )
			{
			    // fix all files and child folders for permissions
			    // based on parent folder
			    for(GroupWorkspaceFolder folder : foldersToMove)
			    {
				    List<GroupWorkspaceFile> files = groupWorkspaceFolderDAO.getAllFilesForFolder(folder);
				    for(GroupWorkspaceFile f : files)
				    {
					   this.addPermissionsForFolderUsers(destination, f);
				    }
				
				
				    List<GroupWorkspaceFolder> children = groupWorkspaceFolderDAO.getAllFoldersForFolder(folder);
				    for(GroupWorkspaceFolder child : children)
				    {
					    giveAdminFolderPermissions(destination, child);
				    }
				    giveAdminFolderPermissions(destination, folder);
			    }
			}
			
		}
		
		return notMoved;
	}
	
	//give the new child folder permissions only permissions where the parent folder user has edit permissions
	private void giveAdminFolderPermissions(GroupWorkspaceFolder parentFolder, GroupWorkspaceFolder newChildFolder)
	{
		// get the list of permissions for each user in the parent folder
		IrAcl acl = securityService.getAcl(parentFolder);
		Set<IrUserAccessControlEntry> userEntries = acl.getUserEntries();
		for(IrUserAccessControlEntry entry : userEntries)
		{
			Set<IrClassTypePermission> groupPermissions = entry.getIrClassTypePermissions();
			IrUser entryUser = entry.getIrUser();
			
			// sonly give permissions for users who have edit permissions on the given parent
			List<IrClassTypePermission> fileSystemPermissions = new LinkedList<IrClassTypePermission>();
		    for(IrClassTypePermission permission : groupPermissions)
		    {
			    if( permission.getName().equals(GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION))
			    {
			    	fileSystemPermissions.addAll(securityService.getClassTypePermissions(GroupWorkspaceFolder.class.getName()));
			    	fileSystemPermissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_READ_PERMISSION));
			    	fileSystemPermissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION));
			    	fileSystemPermissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_READ_PERMISSION));
			    }
		    }
		    
		    if( fileSystemPermissions.size() > 0 )
		    {
		        securityService.createPermissions(newChildFolder, entryUser, fileSystemPermissions);
		    }
		    
		}
	}
	
	
	/**
	 * Allow user information to be moved to the root level.
	 * 
	 * @param user - user moving the files and folders
	 * @param groupWorkspace - root group workspace location
	 * @param foldersToMove - folders to move
	 * @param filesToMove - files to move
	 * 
	 * @return list of files and folders that cannot be moved.
	 * @throws PermissionNotGrantedException 
	 */
	public List<FileSystem> moveFolderSystemInformation(IrUser user, GroupWorkspace groupWorkspace, 
			List<GroupWorkspaceFolder> foldersToMove, 
			List<GroupWorkspaceFile> filesToMove) throws PermissionNotGrantedException
	{
		LinkedList<FileSystem> notMoved = new LinkedList<FileSystem>();
		
		// move folders first
		if( foldersToMove != null )
		{
			// if they can't edit the destination then they can't move the folder
			if( !user.hasRole(IrRole.ADMIN_ROLE) && !securityService.hasPermission(groupWorkspace, user, GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION) )
			{
				throw new PermissionNotGrantedException(GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
			}
		    for( GroupWorkspaceFolder folder : foldersToMove)
		    {
		    	log.debug("Adding folder " + folder + " to root of user " + user);
			    try {
			    	if( user.hasRole(IrRole.ADMIN_ROLE) || securityService.hasPermission(folder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) )
			    	{
			    	    groupWorkspace.addRootFolder(folder);
			    	}
			    	else
			    	{
			    		notMoved.add(folder);
			    	}
				} catch (DuplicateNameException e) {
					notMoved.add(folder);
				}		    	
		    	
		    }
		}
		
		// then move the files
		if( filesToMove != null && notMoved.size() == 0)
		{
		    for( GroupWorkspaceFile file : filesToMove)
		    {
		    	log.debug("Adding file " + file + " to root of user " + user);

			    try {
			    	if( user.hasRole(IrRole.ADMIN_ROLE) || securityService.hasPermission(file.getVersionedFile(), user, VersionedFile.EDIT_PERMISSION) )
			    	{
			            groupWorkspace.addRootFile(file);
			    	}
			    	else
			    	{
			    		notMoved.add(file);
			    	}
			    } catch (DuplicateNameException e) {
					notMoved.add(file);
				}		        
		    }
		}

		if( notMoved.size() == 0)
		{
			groupWorkspaceService.save(groupWorkspace);
		}
		
		return notMoved;
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
	
	/**
	 * Get all folders for the given user that has the specified permission on the folder.
	 * 
	 * @param user - user to check
	 * @param permission - permission to check
	 * 
	 * @return all folders for which the user has the specified permission.
	 */
	public List<GroupWorkspaceFolder> getAllFoldersUserHasPermissionFor(
			IrUser user, String permission)
	{
		return groupWorkspaceFolderDAO.getAllFoldersUserHasPermissionFor(user, permission);
	}
	
	/**
	 * Get all files for the given user that has the specified permission on the file.
	 * 
	 * @param user - user to check
	 * @param permission - permission to check
	 * 
	 * @return all files for which the user has the specified permission.
	 */
	public List<GroupWorkspaceFile> getAllFilesUserHasPermissionFor(
			IrUser user, String permission)
	{
	    return groupWorkspaceFileDAO.getAllFilesUserHasPermissionFor(user, permission);	
	}

	/**
	 * Save the group workspace file into persistent storage.
	 * 
	 * @param groupWorkspaceFile
	 */
	public void save(GroupWorkspaceFile groupWorkspaceFile) {
		groupWorkspaceFileDAO.makePersistent(groupWorkspaceFile);
		
	}
	
	/**
	 * Get the group workspace service.
	 * 
	 * @return group workspace service
	 */
	public GroupWorkspaceService getGroupWorkspaceService() {
		return groupWorkspaceService;
	}

	/**
	 * Set the group workspace service.
	 * 
	 * @param groupWorkspaceService 
	 */
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}

	/**
	 * Get the files for group workspace id and listed file ids.  If the list of fileIds 
	 * is null no files are returned.
	 * 
	 * @param groupWorkspaceId - id of the group workspace to look in
	 * @param fileIds - list of file ids within the group workspace
	 * 
	 * @return the found files
	 */
	public List<GroupWorkspaceFile> getFilesByIds(Long groupWorkspaceId,
			List<Long> fileIds) {
		return groupWorkspaceFileDAO.getFiles(groupWorkspaceId, fileIds);
	}

	/**
	 * Get the group workspace folders in the given list with the specified ids.  If the list
	 * of folderIds is empty, no folders are returned.
	 * 
	 * @param groupWorkspaceId - id of the group workspace
	 * @param folderIds - list of folder ids to retrieve
	 * 
	 * @return the found folders
	 */
	public List<GroupWorkspaceFolder> getFoldersByIds(Long groupWorkspaceId,
			List<Long> folderIds) {
		return groupWorkspaceFolderDAO.getFolders(groupWorkspaceId, folderIds);
	}

	/**
	 * Return the count of group workspace files with the specified ir file 
	 * 
	 * @see edu.ur.ir.groupspace.GroupWorkspaceFileSystemService#getGroupWorkspaceFileCount(edu.ur.ir.file.IrFile)
	 */
	public Long getGroupWorkspaceFileCount(IrFile file) {
		return groupWorkspaceFileDAO.getFileCount(file.getId());
	}
	
	/**
	 * Get the files for group workspace id and versioned file id .
	 * 
	 * @param groupWorkspaceId
	 * @param versionedFileId
	 * 
	 * @return the found file
	 */
	public GroupWorkspaceFile getGroupWorkspaceFileWithVersionedFile(Long groupWorkspaceId, Long versionedFileId)
	{
		return groupWorkspaceFileDAO.getGroupWorkspaceFileWithVersionedFile(groupWorkspaceId, versionedFileId);
	}
	
}
