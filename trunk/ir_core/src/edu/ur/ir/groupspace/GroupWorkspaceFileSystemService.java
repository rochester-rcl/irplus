package edu.ur.ir.groupspace;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.repository.Repository;
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
     */
	public GroupWorkspaceFile addFile(Repository repository, 
			                          GroupWorkspace workspace, 
			                          IrUser user, 
			                          File f,
			                          String name,
			                          String description) throws DuplicateNameException, IllegalFileSystemNameException;
	
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
    		String description )throws DuplicateNameException, IllegalFileSystemNameException;
    
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
    		String description ) throws DuplicateNameException, IllegalFileSystemNameException;
    
    
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
    		String description )throws DuplicateNameException, IllegalFileSystemNameException;
	
    /**
     * Delete the group workspace folder. 
     * 
     * @param folder - folder to delete
     * @param deletingUser - user performing the delete
     * @param deleteReason - reason for the delete.
     */
    public void deleteFolder(GroupWorkspaceFolder folder, IrUser deletingUser, String deleteReason);
}
