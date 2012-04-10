package edu.ur.hibernate.ir.groupspace.db;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemLink;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemLinkDAO;

public class HbGroupWorkspaceProjectPageFileSystemLinkDAO implements GroupWorkspaceProjectPageFileSystemLinkDAO{

	// eclipse generated id
	private static final long serialVersionUID = -8193641848152792345L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<GroupWorkspaceProjectPageFileSystemLink> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceProjectPageFileSystemLinkDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceProjectPageFileSystemLink>(GroupWorkspaceProjectPageFileSystemLink.class);
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
	 * Return GroupWorkspaceProjectPageFileSystemLink by id
	 */
	public GroupWorkspaceProjectPageFileSystemLink getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupWorkspaceProjectPageFileSystemLink entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupWorkspaceProjectPageFileSystemLink entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get the root researcher links for given researcher
	 * 
	 * @see edu.ur.ir.researcher.GroupWorkspaceProjectPageFileSystemLinkDAO#getRootGroupWorkspaceProjectPageFileSystemLinks(Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFileSystemLink> getRootLinks( final Long projectPageId)
	{
		Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
		criteria.createCriteria("groupWorkspaceProjectPage").add(Restrictions.idEq(projectPageId));
        criteria.add(Restrictions.isNull("parentFolder"));
        return criteria.list();
	}
    
	/**
	 * Get researcher links for specified researcher and specified parent folder
	 * 
	 * @see edu.ur.ir.researcher.GroupWorkspaceProjectPageFileSystemLinkDAO#getSubGroupWorkspaceProjectPageFileSystemLinks(Long, Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFileSystemLink> getLinks( final Long projectPageId, final Long parentCollectionId)
	{
		Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
		criteria.createCriteria("groupWorkspaceProjectPage").add(Restrictions.idEq(projectPageId));
        criteria.createCriteria("parentFolder").add(Restrictions.idEq(parentCollectionId));
        return criteria.list();
	}

	/**
	 * Find the specified items for the given researcher.
	 * 
	 * @see edu.ur.ir.researcher.GroupWorkspaceProjectPageFileSystemLinkDAO#getGroupWorkspaceProjectPageFileSystemLinks(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageFileSystemLink> getLinks(final Long projectPageId, final List<Long> itemIds) {
		List<GroupWorkspaceProjectPageFileSystemLink> foundItems = new LinkedList<GroupWorkspaceProjectPageFileSystemLink>();
		if( itemIds.size() > 0 )
		{
			 Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
	         criteria.createCriteria("groupWorkspaceProjectPage").add(Restrictions.idEq(projectPageId));
	         criteria.add(Restrictions.in("id", itemIds));
	         foundItems = criteria.list();
		}
		return foundItems;
	}

}
