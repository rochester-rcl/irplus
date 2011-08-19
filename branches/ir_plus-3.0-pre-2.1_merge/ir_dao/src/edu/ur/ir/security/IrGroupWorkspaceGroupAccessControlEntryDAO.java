package edu.ur.ir.security;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceGroup;

public interface IrGroupWorkspaceGroupAccessControlEntryDAO extends CountableDAO, 
CrudDAO<IrGroupWorkspaceGroupAccessControlEntry>{
	
	/**
	 * Determine the count of permissions for the specified group workspace group, class type
	 * and classType permission 
	 * 
	 * @param userGroup - user group to check 
	 * @param classTypePermission - permission that group should have
	 * @param classType - class type  
	 * 
	 * @return count of the class types
	 */
	public Long getPermissionCountForClassType(GroupWorkspaceGroup group, 
			IrClassTypePermission classTypePermission, IrClassType classType);

	/**
	 * Determine if the group workspace group has the specified permission for the specified object id
	 * 
	 * @param userGroup - user group to check
	 * @param classTypePermission - permission the user group should have
	 * @param classType - class type of the object 
	 * @param objectId - id of the object the user group should have the object for
	 * 
	 * 
	 * @return
	 */
	public Long getPermissionCountForClassTypeObject(GroupWorkspaceGroup group, 
			IrClassTypePermission classTypePermission, IrClassType classType, Long objectId);

}
