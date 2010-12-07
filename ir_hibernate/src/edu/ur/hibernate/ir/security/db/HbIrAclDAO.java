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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Query;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrAclDAO;
import edu.ur.ir.security.Sid;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;


/**
 * Persistence for an IR Access Control list.
 * 
 * @author Nathan Sarr
 *
 */
public class HbIrAclDAO implements IrAclDAO {
	
	/** eclipse generated id */
	private static final long serialVersionUID = 149241236415018110L;

	/** helper for persistence operations */
	private final HbCrudDAO<IrAcl> hbCrudDAO;
	
	/** logger */
	private static final Logger log = Logger.getLogger(HbIrAclDAO.class);
	
	/**
	 * Default Constructor
	 */
	public HbIrAclDAO() {
		hbCrudDAO = new HbCrudDAO<IrAcl>(IrAcl.class);
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
	 * Get a count of the Access control lists.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("irAclCount"));
	}

	/**
	 * Get the acl entries for this object.
	 * 
	 * @see org.acegisecurity.acl.AclManager#getAcls(java.lang.Object)
	 */
	public IrAcl getAcl(Long objectId, String className) {
		IrAcl acl = null;
		Object[] ids = new Object[] {objectId, className};
		acl = (IrAcl) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("irAclByObjectIdentity", ids));

		return acl;
	}

	
	/**
	 * Get the ACL entries for this object.
	 * 
	 * @see org.acegisecurity.acl.AclManager#getAcls(java.lang.Object, org.acegisecurity.Authentication)
	 */
	public IrAcl getAcl(Long objectId, String className, Sid sid) {
		IrAcl acl = new IrAcl();
		
		if( sid instanceof IrUser)
		{
			IrUser user = (IrUser)sid;
			if( log.isDebugEnabled())
			{
			    log.debug("User id = " + user.getId() + " identity id = "
					+ objectId + " class type = "+ className);
			}
			Object[] ids = new Object[] {user.getId(), objectId, className};
			acl = (IrAcl) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("irAclByUserAndObjectIdentity", ids));
		}
		else if( sid instanceof IrUserGroup)
		{
			IrUserGroup userGroup = (IrUserGroup)sid;
			if( log.isDebugEnabled())
			{
			    log.debug("User Group id = " + userGroup.getId() + " identity id = "
					+ objectId + " class type = "+ className);
			}
			Object[] ids = new Object[] {userGroup.getId(), objectId, className};
			acl = (IrAcl) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("irAclByUserGroupAndObjectIdentity", ids));
		}
		else if( sid instanceof IrRole)
		{
			IrRole role = (IrRole)sid;
			if( log.isDebugEnabled())
			{
			    log.debug("Role id = " + role.getId() + " identity id = "
					+ objectId + " class type = "+ className);
			}
			Object[] ids = new Object[] {role.getId(), objectId, className};
			acl = (IrAcl) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("irAclByRoleAndObjectIdentity", ids));
		}
		else
		{
			throw new IllegalStateException("No select for sid " + sid);
		}
		return acl;
	}
	
	/**
	 * Get the acl by id.
	 *
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public IrAcl getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	/**
	 * Make the acl persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(IrAcl entity) {
		hbCrudDAO.makePersistent(entity);
	}

	
	/**
	 * Make the acl transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(IrAcl entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Returns all access control lists for the selected secure id.
	 * 
	 * @param sid - secure id to get all acls for the specified sid
	 * @return all acls that have permissions for the specified sid.
	 */
	@SuppressWarnings("unchecked")
	public List<IrAcl> getAllAclsForSid(Sid sid)
	{
		List<IrAcl> acls = null;
		
		if( sid instanceof IrUser)
		{
			IrUser user = (IrUser)sid;
			if( log.isDebugEnabled())
			{
			    log.debug("User id = " + user.getId());
			}
			
			acls = (List<IrAcl>) (hbCrudDAO.getHibernateTemplate().findByNamedQuery("irAclByUser", user.getId()));
		}
		else if( sid instanceof IrUserGroup)
		{
			IrUserGroup userGroup = (IrUserGroup)sid;
			if( log.isDebugEnabled())
			{
			    log.debug("User Group id = " + userGroup.getId());
			}
			acls = (List<IrAcl>) (hbCrudDAO.getHibernateTemplate().findByNamedQuery("irAclByUserGroup", userGroup.getId()));
		}
		else if( sid instanceof IrRole)
		{
			IrRole role = (IrRole)sid;
			if( log.isDebugEnabled())
			{
			    log.debug("Role id = " + role.getId());
			}
			acls = (List<IrAcl>) (hbCrudDAO.getHibernateTemplate().findByNamedQuery("irAclByRole", role.getId()));
		}
		else
		{
			throw new IllegalStateException("No select for sid " + sid);
		}
		
		return acls;
		
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.security.IrAclDAO#hasPermission(java.lang.Long, java.lang.String, edu.ur.ir.security.Sid, java.lang.String)
	 */
	public Long hasPermission(Long objectId, String className, Sid sid,
			String permission) {
		Long count = 0l;
		if( sid instanceof IrUser)
		{
			IrUser user = (IrUser)sid;
			if( log.isDebugEnabled())
			{
			    log.debug("User id = " + user.getId() + " identity id = "
					+ objectId + " class type = "+ className);
			}
			Object[] ids = new Object[] {user.getId(), objectId, className, permission};
			count += (Long) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("userHasPermissionDirectCount", ids));
            count += (Long) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("userHasPermissionByGroupCount", ids));
		}
		else
		{
			throw new IllegalStateException("No select for sid " + sid);
		}
		
		return count;
	}

	/**
	 * Return all sids who have the specified permission
	 * 
	 * @see edu.ur.ir.security.IrAclDAO#hasPermission(java.lang.Long, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Set<Sid> getSidsWithPermissionForObject(Long objectId, String className,
			String permission) {
		HashSet<Sid> sids = new HashSet<Sid>();
		Object[] ids = new Object[] { objectId, className, permission};
		List<Sid> userSids = (List<Sid>)hbCrudDAO.getHibernateTemplate().findByNamedQuery("usersWithSpecifiedPermission", ids);
		List<Sid> groupSids = (List<Sid>)hbCrudDAO.getHibernateTemplate().findByNamedQuery("groupsWithSpecifiedPermission", ids);
		sids.addAll(userSids);
		sids.addAll(groupSids);
		return sids;
	}
	
	/**
	 * Return sepcified sids who have the specified permission
	 * 
	 * @see edu.ur.ir.security.IrAclDAO#hasPermission(java.lang.Long, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Set<Sid> getSidsWithPermissionForObject(Long objectId, String className,
			String permission, List<Sid> specificSids) {
		HashSet<Sid> sids = new HashSet<Sid>();
		Set<Long> userIds = new HashSet<Long>();
		Set<Long> groupIds = new HashSet<Long>();
		
		for(Sid sid : specificSids)
		{
			if( sid.getSidType().equals(IrUser.USER_SID_TYPE))
			{
			    userIds.add( ((IrUser)sid).getId() );	
			}
			else if( sid.getSidType().equals(IrUserGroup.GROUP_SID_TYPE))
			{
				groupIds.add( ((IrUserGroup)sid).getId() );
			}
			else
			{
				throw new IllegalStateException("Unknown sid type " + sid.getSidType());
			}
		}
		
		
		if( userIds.size() > 0 )
		{
		    Query q = hbCrudDAO.getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("specifiedUsersWithPermission");
	
		    q.setLong(0, objectId);
		    q.setString(1, className);
		    q.setString(2, permission);
		    q.setParameterList("userIds", userIds);
		
		    List<Sid> userSids = (List<Sid>)q.list();
		    sids.addAll(userSids);
		}

		if( groupIds.size() > 0 )
		{
		    Query q = hbCrudDAO.getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("specifiedGroupsWithPermission");
		
		    q.setLong(0, objectId);
		    q.setString(1, className);
		    q.setString(2, permission);
		    q.setParameterList("groupIds", groupIds);
		
		    List<Sid> groupSids = (List<Sid>)q.list();
		    sids.addAll(groupSids);
		}
		return sids;
	}
}
