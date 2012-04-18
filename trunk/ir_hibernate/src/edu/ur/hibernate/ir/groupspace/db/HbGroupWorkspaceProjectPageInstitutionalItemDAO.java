package edu.ur.hibernate.ir.groupspace.db;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageInstitutionalItem;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageInstitutionalItemDAO;

public class HbGroupWorkspaceProjectPageInstitutionalItemDAO implements GroupWorkspaceProjectPageInstitutionalItemDAO{

	// eclipse generated id
	private static final long serialVersionUID = -5557160047915579723L;
	
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<GroupWorkspaceProjectPageInstitutionalItem> hbCrudDAO;

	/**  Logger for add files to item action */
	private static final Logger log = Logger.getLogger(HbGroupWorkspaceProjectPageInstitutionalItemDAO.class);

	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceProjectPageInstitutionalItemDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceProjectPageInstitutionalItem>(GroupWorkspaceProjectPageInstitutionalItem.class);
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
	 * Return ResearcherInstitutionalItem by id
	 */
	public GroupWorkspaceProjectPageInstitutionalItem getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupWorkspaceProjectPageInstitutionalItem entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupWorkspaceProjectPageInstitutionalItem entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get the root researcher institutional Items for given researcher
	 * 
	 * @see edu.ur.ir.researcher.ResearcherInstitutionalItemDAO#getRootResearcherInstitutionalItems(Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageInstitutionalItem> getRootItems(final Long projectPageId)
	{
		log.debug("getRootProjectPageInstitutionalItems::");
		Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
        criteria.createCriteria("groupWorkspaceProjectPage").add(Restrictions.idEq(projectPageId));
        criteria.add(Restrictions.isNull("parentFolder"));
        return criteria.list();
	}
    
	/**
	 * Get researcher institutional Items for specified researcher and specified parent folder
	 * 
	 * @see edu.ur.ir.researcher.ResearcherInstitutionalItemDAO#getSubResearcherInstitutionalItems(Long, Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageInstitutionalItem> getItems(final Long projectPageId, final Long parentCollectionId)
	{
		log.debug("getChildGroupWorkspaceProjectPageInstitutionalItems::");
		Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
		criteria.createCriteria("groupWorkspaceProjectPage").add(Restrictions.idEq(projectPageId));
        criteria.createCriteria("parentFolder").add(Restrictions.idEq(parentCollectionId));
        return criteria.list();
	}


	/**
	 * Find the specified items for the given researcher.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherInstitutionalItemDAO#getResearcherInstitutionalItems(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageInstitutionalItem> getItems(final Long projectPageId, final List<Long> itemIds) {
		List<GroupWorkspaceProjectPageInstitutionalItem> foundItems = new LinkedList<GroupWorkspaceProjectPageInstitutionalItem>();
		if( itemIds.size() > 0 )
		{
			Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
			criteria.createCriteria("groupWorkspaceProjectPage").add(Restrictions.idEq(projectPageId));
            criteria.add(Restrictions.in("id", itemIds));
            foundItems =  criteria.list();
		}
		return foundItems;
	}

	/**
	 * Get a count of the researcher institutional Items containing this item
	 * 
	 * @see edu.ur.ir.researcher.ResearcherInstitutionalItemDAO#getResearcherInstitutionalItemCount(Long)
	 */
	public Long getCount(Long itemId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWorkspaceProjectPageInstitutionalItemCount");
		q.setLong("institutionalItemId", itemId);
		return (Long)q.uniqueResult();
	}

	/**
	 * Get a researcher institutional Items containing this item
	 * 
	 * @see edu.ur.ir.researcher.ResearcherInstitutionalItemDAO#getResearcherInstitutionalItem(Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPageInstitutionalItem> getItems(Long institutionalItemId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWorkspaceProjectPageInstitutionalItem");
		q.setLong("institutionalItemId", institutionalItemId);
		return (List<GroupWorkspaceProjectPageInstitutionalItem>) q.list();
	}

}
