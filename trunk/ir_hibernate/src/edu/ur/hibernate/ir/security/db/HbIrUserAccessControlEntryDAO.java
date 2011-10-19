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


import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserAccessControlEntry;
import edu.ur.ir.security.IrUserAccessControlEntryDAO;
import edu.ur.ir.user.IrUser;

/**
 * Persistance for a user access control entry.
 * 
 * @author Nathan Sarr
 *
 */
public class HbIrUserAccessControlEntryDAO implements 
IrUserAccessControlEntryDAO 
{
	/** eclipse generated id */
	private static final long serialVersionUID = 7996579141250699790L;
	
	/** helper for persistence operations */
	private final HbCrudDAO<IrUserAccessControlEntry> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbIrUserAccessControlEntryDAO() {
		hbCrudDAO = new HbCrudDAO<IrUserAccessControlEntry>(IrUserAccessControlEntry.class);
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
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irUserAccessEntryCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get the user access control entry by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public IrUserAccessControlEntry getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the user access control entry persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(IrUserAccessControlEntry entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the user access control entry transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(IrUserAccessControlEntry entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Create user control entries for the list of users for the
	 * specified acls.  This is a bulk operation
	 * 
	 * @param users - list of users to create the entries for
	 * @param acl - acl to add the entries to
	 * 
	 * @return number of entries created
	 */
	public int createUserControlEntriesForUsers(List<IrUser> users, List<IrAcl> acls )
	{
		// make sure data is flushed 
		hbCrudDAO.getSessionFactory().getCurrentSession().flush();
		StringBuffer userIdList = new StringBuffer("");
		boolean first = true;
		
		// build the list of ids
		for(IrUser user : users)
		{
			if( first )
			{
				userIdList.append(user.getId());
				first =false;
			}
			else
			{
				userIdList.append(",");
				userIdList.append(user.getId());
			}
		}
		
		StringBuffer aclIdList = new StringBuffer("");
		first = true;
		
		// build the list of acl ids
		for(IrAcl acl : acls)
		{
			if( first )
			{
				aclIdList.append(acl.getId());
				first =false;
			}
			else
			{
				aclIdList.append(",");
				aclIdList.append(acl.getId());
			}
		}
		
		Session s = hbCrudDAO.getSessionFactory().getCurrentSession();
		String sql = "insert into ir_security.user_control_entry(user_control_entry_id, acl_id, user_id, version) " + 
		" select nextval('ir_security.user_control_entry_seq'), acl_id, user_id, 0 " +
		" from ir_user.ir_user, ir_security.acl where ir_user.user_id in ( " 
		+ userIdList.toString() +") and acl.acl_id in (" + aclIdList.toString() + ")"; 

		Query q =  s.createSQLQuery(sql);
		int createdEntities = q.executeUpdate();
		
		return createdEntities;

	}
	
	/**
	 * Create the permissions for user control entries.
	 * 
	 * @param entries - list of entries
	 * @param permissions - list of permissions to give to each entry
	 * 
	 * @return
	 */
	public int createPermissionsForUserControlEntries(List<IrUserAccessControlEntry> entries,
			List<IrClassTypePermission> permissions)
	{
		// make sure data is flushed 
		hbCrudDAO.getSessionFactory().getCurrentSession().flush();
		StringBuffer entryIdList = new StringBuffer("");
		boolean first = true;
		
		// build the list of ids
		for(IrUserAccessControlEntry entry : entries)
		{
			if( first )
			{
				entryIdList.append(entry.getId());
				first =false;
			}
			else
			{
				entryIdList.append(",");
				entryIdList.append(entry.getId());
			}
		}
		
		StringBuffer permissionIdList = new StringBuffer("");
		first = true;
		
		// build the list of acl ids
		for(IrClassTypePermission permission : permissions)
		{
			if( first )
			{
				permissionIdList.append(permission.getId());
				first =false;
			}
			else
			{
				permissionIdList.append(",");
				permissionIdList.append(permission.getId());
			}
		}
		
		Session s = hbCrudDAO.getSessionFactory().getCurrentSession();
		String sql = "insert into ir_security.user_control_entry_permission(user_control_entry_id, class_type_permission_id) " + 
		" select user_control_entry_id, class_type_permission_id " +
		" from  ir_security.acl, ir_security.user_control_entry, ir_security.class_type_permission " + 
		" where  acl.acl_id = user_control_entry.acl_id and " +
		" acl.class_type_id = class_type_permission.class_type_id " +
		" and user_control_entry.user_control_entry_id in ( " 
		+ entryIdList.toString() +") and  " +
		"class_type_permission.class_type_permission_id in (" + permissionIdList.toString() + ")"; 

		Query q =  s.createSQLQuery(sql);
		int createdEntities = q.executeUpdate();
		
		return createdEntities;
	}
	
	/**
	 * Get the list of users for the given access control list.
	 * 
	 * @param acl - acl to the the access control entries for
	 * @param users - list of users to get for the acl
	 * 
	 * @return - list of users found.
	 */
	@SuppressWarnings("unchecked")
	public List<IrUserAccessControlEntry> getUserControlEntriesForUsers(IrAcl acl, List<IrUser> users)
	{
		LinkedList<Long> userIds = new LinkedList<Long>();
		for( IrUser user : users)
		{
			userIds.add(user.getId());
		}
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("userControlEntriesForUsers");
		q.setLong("aclId", acl.getId());
		q.setParameterList("userIds", userIds);
		return q.list();
	}
}
