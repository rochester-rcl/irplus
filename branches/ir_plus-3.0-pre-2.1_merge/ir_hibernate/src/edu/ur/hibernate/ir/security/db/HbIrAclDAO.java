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
import edu.ur.ir.groupspace.GroupWorkspaceGroup;
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
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irAclCount");
		return (Long) q.uniqueResult();
	}

	/**
	 * Get the acl entries for this object.
	 * 
	 * @see org.acegisecurity.acl.AclManager#getAcls(java.lang.Object)
	 */
	public IrAcl getAcl(Long objectId, String className) {
		IrAcl acl = null;
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irAclByObjectIdentity");
		q.setParameter("objectId", objectId);
		q.setParameter("name", className);
		acl = (IrAcl) q.uniqueResult();
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
			Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irAclByUserAndObjectIdentity");
			q.setParameter("userId", user.getId());
			q.setParameter("objectId", objectId);
			q.setParameter("name", className);
			acl = (IrAcl) q.uniqueResult();
		}
		else if( sid instanceof IrUserGroup)
		{
			IrUserGroup userGroup = (IrUserGroup)sid;
			if( log.isDebugEnabled())
			{
			    log.debug("User Group id = " + userGroup.getId() + " identity id = "
					+ objectId + " class type = "+ className);
			}
			Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irAclByUserGroupAndObjectIdentity");
			q.setParameter("groupId", userGroup.getId());
			q.setParameter("objectId", objectId);
			q.setParameter("name", className);
			acl = (IrAcl) q.uniqueResult();
		}
		else if( sid instanceof IrRole)
		{
			IrRole role = (IrRole)sid;
			if( log.isDebugEnabled())
			{
			    log.debug("Role id = " + role.getId() + " identity id = "
					+ objectId + " class type = "+ className);
			}
			Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irAclByRoleAndObjectIdentity");
			q.setParameter("roleId", role.getId());
			q.setParameter("objectId", objectId);
			q.setParameter("name", className);
			acl = (IrAcl) q.uniqueResult();
		}
		else if( sid instanceof GroupWorkspaceGroup)
		{
			GroupWorkspaceGroup group = (GroupWorkspaceGroup)sid;
			if( log.isDebugEnabled())
			{
			    log.debug("GroupWorkspaceGroup id = " + group.getId() + " identity id = "
					+ objectId + " class type = "+ className);
			}
			Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irAclByGroupWorkspaceGroupAndObjectIdentity");
			q.setParameter("groupId", group.getId());
			q.setParameter("objectId", objectId);
			q.setParameter("name", className);
			acl = (IrAcl) q.uniqueResult();
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
			Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irAclByUser");
			q.setParameter("userId", user.getId());
			acls = (List<IrAcl>) q.list();
		}
		else if( sid instanceof IrUserGroup)
		{
			IrUserGroup userGroup = (IrUserGroup)sid;
			if( log.isDebugEnabled())
			{
			    log.debug("User Group id = " + userGroup.getId());
			}
			Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irAclByUserGroup");
			q.setParameter("groupId", userGroup.getId());
			acls = (List<IrAcl>) q.list();
		}
		else if( sid instanceof IrRole)
		{
			IrRole role = (IrRole)sid;
			if( log.isDebugEnabled())
			{
			    log.debug("Role id = " + role.getId());
			}
			Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irAclByRole");
			q.setParameter("roleId", role.getId());
			acls = (List<IrAcl>) q.list();
		}
		else if( sid instanceof GroupWorkspaceGroup)
		{
			GroupWorkspaceGroup group = (GroupWorkspaceGroup)sid;
			if( log.isDebugEnabled())
			{
			    log.debug("Group workspace Group id = " + group.getId());
			}
			Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("irAclByGroupWorkspaceGroup");
			q.setParameter("groupId", group.getId());
			acls = (List<IrAcl>) q.list();
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
			Query q1 = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("userHasPermissionDirectCount");
			q1.setParameter("userId", user.getId());
			q1.setParameter("objectId", objectId );
			q1.setParameter("className", className);
			q1.setParameter("permissionName", permission);
			count += (Long)q1.uniqueResult();
			
			Query q2 = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("userHasPermissionByGroupCount");
			q2.setParameter("userId", user.getId());
			q2.setParameter("objectId", objectId );
			q2.setParameter("className", className);
			q2.setParameter("permissionName", permission);
			count += (Long)q2.uniqueResult();
			
			Query q3 = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("userHasPermissionByGroupWorkspaceGroupCount");
			q3.setParameter("userId", user.getId());
			q3.setParameter("objectId", objectId );
			q3.setParameter("className", className);
			q3.setParameter("permissionName", permission);
			count += (Long)q3.uniqueResult();
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
		
		Query q1 = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("usersWithSpecifiedPermission");
		q1.setParameter("objectId", objectId);
		q1.setParameter("className", className);
		q1.setParameter("permissionName", permission);
		List<Sid> userSids = (List<Sid>) q1.list();
		
		Query q2 = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupsWithSpecifiedPermission");
		q2.setParameter("objectId", objectId);
		q2.setParameter("className", className);
		q2.setParameter("permissionName", permission);
		List<Sid> groupSids = (List<Sid>) q2.list();
		
		Query q3 = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupsWorksapceGroupsWithSpecifiedPermission");
		q3.setParameter("objectId", objectId);
		q3.setParameter("className", className);
		q3.setParameter("permissionName", permission);
		List<Sid> workspaceGroupSids = (List<Sid>) q3.list();
		
		sids.addAll(userSids);
		sids.addAll(groupSids);
		sids.addAll(workspaceGroupSids);
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
		Set<Long> groupWorkspaceGroupIds = new HashSet<Long>();
		
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
			else if( sid.getSidType().equals(GroupWorkspaceGroup.WORKSPACE_GROUP_SID_TYPE))
			{
				groupWorkspaceGroupIds.add( ((GroupWorkspaceGroup)sid).getId() );
			}
			else
			{
				throw new IllegalStateException("Unknown sid type " + sid.getSidType());
			}
		}
		
		
		if( userIds.size() > 0 )
		{
		    Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("specifiedUsersWithPermission");
	
		    q.setParameter("objectId", objectId);
		    q.setParameter("className", className);
		    q.setParameter("permissionName", permission);
		    q.setParameterList("userIds", userIds);
		
		    List<Sid> userSids = (List<Sid>)q.list();
		    sids.addAll(userSids);
		}

		if( groupIds.size() > 0 )
		{
		    Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("specifiedGroupsWithPermission");
		
		    q.setParameter("objectId", objectId);
		    q.setParameter("className", className);
		    q.setParameter("permissionName", permission);
		    q.setParameterList("groupIds", groupIds);
		
		    List<Sid> groupSids = (List<Sid>)q.list();
		    sids.addAll(groupSids);
		}
		
		if( groupWorkspaceGroupIds.size() > 0 )
		{
		    Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("specifiedGroupWorkspaceGroupsWithPermission");
		
		    q.setParameter("objectId", objectId);
		    q.setParameter("className", className);
		    q.setParameter("permissionName", permission);
		    q.setParameterList("groupIds", groupIds);
		
		    List<Sid> groupSids = (List<Sid>)q.list();
		    sids.addAll(groupSids);
		}
		return sids;
	}
}
