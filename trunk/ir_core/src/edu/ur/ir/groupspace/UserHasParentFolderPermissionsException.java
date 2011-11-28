package edu.ur.ir.groupspace;

/**
 *  Should be thrown when changes in permissions are tried for a given user and that
 *  user has permissions on a parent folder that would supersede the specified changes.
 *  
 * @author Nathan Sarr
 *
 */
public class UserHasParentFolderPermissionsException extends Exception {
	
	/* eclipse generated id */
	private static final long serialVersionUID = -3625829573785185187L;

	public UserHasParentFolderPermissionsException(String message)
	{
		super(message);
	}

}
