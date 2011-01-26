package edu.ur.ir.groupspace;

import java.io.Serializable;
import java.util.List;

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
	 * @return - the group workspace folder
	 */
	public GroupWorkspaceFolder getFolder(Long id, boolean lock);
	
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
	 * Get personal files for a group workspace in the specified folder
	 * 
	 * @param workspaceId Id of the user having the files
	 * @param parentFolderId Id of the folder contaiing the files
	 * 
	 * @return List of files in the folder
	 */
	public List<GroupWorkspaceFile> getFiles(Long workspaceId, Long parentFolderId);
	
}
