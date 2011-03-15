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


package edu.ur.hibernate.ir.security.db;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceGroup;
import edu.ur.ir.security.IrClassType;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrGroupWorkspaceGroupAccessControlEntry;
import edu.ur.ir.security.IrGroupWorkspaceGroupAccessControlEntryDAO;

/**
 * Implementation for group workspace group access control entries.
 * 
 * @author Nathan Sarr
 *
 */
public class HbIrGroupWorkspaceGroupAccessControlEntryDAO implements 
IrGroupWorkspaceGroupAccessControlEntryDAO
{
	
	
	/* eclipse generated id */
	private static final long serialVersionUID = -1949888515822971852L;
	
	/* helper for persistence operations */
	private final HbCrudDAO<IrGroupWorkspaceGroupAccessControlEntry> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbIrGroupWorkspaceGroupAccessControlEntryDAO() {
		hbCrudDAO = new HbCrudDAO<IrGroupWorkspaceGroupAccessControlEntry>(IrGroupWorkspaceGroupAccessControlEntry.class);
	}
	
	/**
	 * Set the session factory.  
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
    {
        hbCrudDAO.setSessionFactory(sessionFactory);
    }

	/**
	 * Get a count of the user access cotnrol entries.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupWorkspaceGroupAccessEntryCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get the user access control entry by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public IrGroupWorkspaceGroupAccessControlEntry getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the user access control entry persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(IrGroupWorkspaceGroupAccessControlEntry entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the user access control entry transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(IrGroupWorkspaceGroupAccessControlEntry entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Determine if the user group has the permission for the specified class type
	 * 
	 * @see edu.ur.ir.security.IrUserGroupAccessControlEntryDAO#getUserGroupPermissionCountForClassType(edu.ur.ir.user.IrUserGroup, edu.ur.ir.security.IrClassTypePermission, edu.ur.ir.security.IrClassType)
	 */
	public Long getPermissionCountForClassType(GroupWorkspaceGroup group,
			IrClassTypePermission classTypePermission, IrClassType classType) {
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupWorkspaceGroupPermissionCountForClassType");
		q.setParameter("classTypeId", classType.getId());
		q.setParameter("permissionId", classTypePermission.getId() );
		q.setParameter("workspaceGroupId", group.getId());
		return (Long)q.uniqueResult();
		
	}

	/**
	 * Determine if the user group has the permission for the specified object 
	 * and class type.
	 * 
	 * @see edu.ur.ir.security.IrUserGroupAccessControlEntryDAO#getUserGroupPermissionCountForClassTypeObject(edu.ur.ir.user.IrUserGroup, edu.ur.ir.security.IrClassTypePermission, edu.ur.ir.security.IrClassType, java.lang.Long)
	 */
	public Long getPermissionCountForClassTypeObject(
			GroupWorkspaceGroup group, IrClassTypePermission classTypePermission,
			IrClassType classType, Long objectId) {
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupWorkspaceGroupPermissionCountForClassTypeObject");
		q.setParameter("classTypeId", classType.getId());
		q.setParameter("permissionId", classTypePermission.getId() );
		q.setParameter("workspaceGroupId", group.getId());
		q.setParameter("objectId", objectId);
		return (Long)q.uniqueResult();
	}
}
