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

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.security.IrClassType;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.security.IrUserGroupAccessControlEntryDAO;
import edu.ur.ir.user.IrUserGroup;

/**
 * DAO Implementation for user group access control entry
 * 
 * @author Nathan Sarr
 *
 */
public class HbIrUserGroupAccessControlEntryDAO implements 
IrUserGroupAccessControlEntryDAO 
{
	/** eclipse generated id */
	private static final long serialVersionUID = -6233995403157802235L;
	
	/** helper for persistence operations */
	private final HbCrudDAO<IrUserGroupAccessControlEntry> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbIrUserGroupAccessControlEntryDAO() {
		hbCrudDAO = new HbCrudDAO<IrUserGroupAccessControlEntry>(IrUserGroupAccessControlEntry.class);
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
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("irUserGroupAccessEntryCount"));
	}

	
	/**
	 * Get all user access control entries.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get the user access control entry by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public IrUserGroupAccessControlEntry getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the user access control entry persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(IrUserGroupAccessControlEntry entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the user access control entry transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(IrUserGroupAccessControlEntry entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Determine if the user group has the permission for the specified class type
	 * 
	 * @see edu.ur.ir.security.IrUserGroupAccessControlEntryDAO#getUserGroupPermissionCountForClassType(edu.ur.ir.user.IrUserGroup, edu.ur.ir.security.IrClassTypePermission, edu.ur.ir.security.IrClassType)
	 */
	public Long getUserGroupPermissionCountForClassType(IrUserGroup userGroup,
			IrClassTypePermission classTypePermission, IrClassType classType) {
		Long[] ids = new Long[] {classType.getId(),
				classTypePermission.getId(), 
				userGroup.getId() };
		Long count =  (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("permissionCountForClassType", 
					ids));
		return count;
	}

	/**
	 * Determine if the user group has the permission for the specified object 
	 * and class type.
	 * 
	 * @see edu.ur.ir.security.IrUserGroupAccessControlEntryDAO#getUserGroupPermissionCountForClassTypeObject(edu.ur.ir.user.IrUserGroup, edu.ur.ir.security.IrClassTypePermission, edu.ur.ir.security.IrClassType, java.lang.Long)
	 */
	public Long getUserGroupPermissionCountForClassTypeObject(
			IrUserGroup userGroup, IrClassTypePermission classTypePermission,
			IrClassType classType, Long objectId) {
		Long[] ids = new Long[] {classType.getId(),
				classTypePermission.getId(), 
				userGroup.getId(),
				objectId};
		Long count =  (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("permissionCountForClassTypeObject", 
					ids));
		return count;
	}

}
