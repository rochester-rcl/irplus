package edu.ur.ir.groupspace;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.user.IrUser;

/**
 * Group file system service to deal with group file system information. 
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupWorkspaceFileSystemService extends Serializable
{
	
	/** Default id for the root folder for each workspace - this indicates get files or folders
	 * from the workspace object */
	public static final long ROOT_FOLDER_ID = 0L;

	/**
	 * Get the group workspace folder.
	 * 
	 * @param id - id of the group workspace folder
 	 * @param lock - if set to true upgrade the lock mode.
 	 * 
	 * @return - the group workspace folder or null if not found
	 */
	public GroupWorkspaceFolder getFolder(Long id, boolean lock);
	
	/**
	 * Get the group workspace file.
	 * 
	 * @param id - id of the group workspace file
 	 * @param lock - if set to true upgrade the lock mode.
 	 * 
	 * @return - the group workspace file or null if not found
	 */
	public GroupWorkspaceFile getFile(Long id, boolean lock);
	
	/**
	 * Get the path to the folder.
	 * 
	 * @param parentFolderId - id of the parent folder
	 * 
	 * @return - list of all folders in order - parent to child
	 */
	public List<GroupWorkspaceFolder> getFolderPath(GroupWorkspaceFolder folder);
	
	/**
	 * Get sub folders within parent folder for a group workspace
	 * 
	 * @param workspaceId Id of the group workspace containing the folders
	 * @param parentFolderId Id of the parent folder to start at - can be at any point
	 * in the tree
	 * 
	 * @return List of sub folders within the parent folder
	 */
	public List<GroupWorkspaceFolder> getFolders(Long workspaceId, Long parentFolderId );
	
	/**
	 * Get all the folders for a group workspace
	 * @param workspaceId - id of the group workspace.
	 * 
	 * @return list of group workspace folders
	 */
	public List<GroupWorkspaceFolder> getAllFolders(Long workspaceId);

	/**
	 * Get personal files for a group workspace in the specified folder
	 * 
	 * @param workspaceId Id of the user having the files
	 * @param parentFolderId Id of the folder contaiing the files
	 * 
	 * @return List of files in the folder
	 */
	public List<GroupWorkspaceFile> getFiles(Long workspaceId, Long parentFolderId);
	
	/**
	 * Get all files for the workspace.
	 * 
	 * @param groupWorkspaceId - id of the group workspace
	 * @return all the files within the group workspace
	 */
	public List<GroupWorkspaceFile> getAllFiles(Long groupWorkspaceId);
	
	
	/**
	 * Save the group workspace folder into persistent storage.
	 * 
	 * @param groupWorkspaceFolder
	 */
	public void save(GroupWorkspaceFolder groupWorkspaceFolder);
	
   /**
     * Create a group workspace versioned file in the system with the specified file for the
     * given workspace. This is created at the root level (added to the group workspace)
     * 
     * @param repositoryId - the repository to add the file to.
     * @param workspace - workspace to add the file to
     * @param user - user/owner who is creating the file 
     * @param f - file to add
     * @param name - name to give the file
     * @param description - description of the file.
     * 
     * @return the created group workspace file
     * 
     * @throws PermissionNotGrantedException - if the user does not have permission to add the file
     */
	public GroupWorkspaceFile addFile(Repository repository, 
			                          GroupWorkspace workspace, 
			                          IrUser user, 
			                          File f,
			                          String name,
			                          String description) throws DuplicateNameException, IllegalFileSystemNameException, PermissionNotGrantedException;
	
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
     * @throws PermissionNotGrantedException - if the user does not have permission to add the file
     */
    public GroupWorkspaceFile addFile(Repository repository, 
            GroupWorkspace workspace, 
    		IrUser user, 
    		String fileName, 
    		String description )throws DuplicateNameException, IllegalFileSystemNameException, PermissionNotGrantedException;
    
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
     * @throws PermissionNotGrantedException  - user does not have permission to add files to the folder
     */
    public GroupWorkspaceFile addFile(Repository repository, 
    		GroupWorkspaceFolder folder, 	
            IrUser user,
    		File f, 
    		String fileName, 
    		String description ) throws DuplicateNameException, IllegalFileSystemNameException, PermissionNotGrantedException;
    
    
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
     * @throws PermissionNotGrantedException - user does not have permission to add files to the folder
     */
    public GroupWorkspaceFile addFile(Repository repository, 
    		GroupWorkspaceFolder folder, 
    		IrUser user,
    		String fileName, 
    		String description )throws DuplicateNameException, IllegalFileSystemNameException, PermissionNotGrantedException;
	
    /**
     * Delete the group workspace folder. 
     * 
     * @param folder - folder to delete
     * @param deletingUser - user performing the delete
     * @param deleteReason - reason for the delete.
     * 
     * @throws PermissionNotGrantedException - if the user does not have permissions to delete 
     */
    public void delete(GroupWorkspaceFolder folder, IrUser deletingUser, String deleteReason) throws PermissionNotGrantedException;
    
	/**
	 * Delete a group workspace file.
	 * 
	 * @param gf - group workspace file
	 * @param deletingUser - user deleting the file
	 * @param deleteReason - reason for deleting the group workspace file
	 */
	public void delete(GroupWorkspaceFile gf, IrUser deletingUser, String deleteReason);
	
	/**
	 * Gives the user permissions for the given group file system.
	 * 
	 * @param users - list of users to give permissions for the group to
	 * @param groupWorkspace - group workspace
	 * @param permissions - set of permissions to give
	 */
	public void giveUsersPermissionsToGroupFileSystem(List<IrUser> users, GroupWorkspace groupWorkspace, List<IrClassTypePermission> permissions);

	/**
	 * Remove all permissions from the group file system for the given user.
	 * 
	 * @param user - user to remove all permissions from
	 * @param groupWorkspace - group workspace to remove all permissions from.
	 */
	public void removeUserPermissionsFromGroupFileSystem(IrUser user, GroupWorkspace groupWorkspace);
	
	/**
	 * Change the permission on a given folder and it's children folders and files. If
	 * the permissions contain edit permissions for the folder, all child folders and files
	 * will be updated with the edit permission Regardless of the applyToChildren flag.
	 * 
	 * @param user - user to change the permissions for
	 * @param groupWorkspaceFolder - folder to change the permissions ons
	 * @param permissions - permissions to give
	 * 
	 * @throws UserHasParentFolderPermissionsException - if parent folder has permissions that would override the
	 *  change in any of the parent folders.
	 */
	public void changeUserPermissionsForFolder(IrUser user, GroupWorkspaceFolder 
			groupWorkspaceFolder, Set<IrClassTypePermission> permissions, boolean applyToChildren) throws UserHasParentFolderPermissionsException;
	
	/**
	 * Determines if the user has edit permissions in any of the parent folders for the  
	 * @param user - the user to check for edit permissions
	 * @param child - 
	 * @return
	 */
	public boolean userHasParentFolderEditPermissions(IrUser user, GroupWorkspaceFolder child);
	
	/**
	 * Get the list of parent folders that the user has edit permissions on.
	 * 
	 * @param user - user to get the permissions for 
	 * @param child - child folder
	 *  
	 * @return - list of parent folders the user has edit permissions on or an empty list if there are none.
	 */
	public List<GroupWorkspaceFolder> getFoldersWithEditPermission(IrUser user, GroupWorkspaceFolder child);
	
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
	 * @throws PermissionNotGrantedException - if the user does not have permission to add a folder to the group workspace
	 */
	public GroupWorkspaceFolder addFolder(GroupWorkspace groupWorkspace, 
			String folderName, 
			String description, 
			IrUser user) throws DuplicateNameException, IllegalFileSystemNameException, PermissionNotGrantedException;

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
	public GroupWorkspaceFolder addFolder(GroupWorkspaceFolder parentFolder, String name, String description, IrUser user) throws DuplicateNameException, IllegalFileSystemNameException, PermissionNotGrantedException;

}
