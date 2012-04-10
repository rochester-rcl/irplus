package edu.ur.hibernate.ir.groupspace.db;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPagePublication;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPagePublicationDAO;

public class HbGroupWorkspaceProjectPagePublicationDAO implements GroupWorkspaceProjectPagePublicationDAO{

	// eclipse generated id
	private static final long serialVersionUID = 5800098392790873845L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<GroupWorkspaceProjectPagePublication> hbCrudDAO;

	/**  Logger for add files to item action */
	private static final Logger log = Logger.getLogger(HbGroupWorkspaceProjectPagePublicationDAO.class);

	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceProjectPagePublicationDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceProjectPagePublication>(GroupWorkspaceProjectPagePublication.class);
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
	 * Return ResearcherPublication by id
	 */
	public GroupWorkspaceProjectPagePublication getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupWorkspaceProjectPagePublication entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupWorkspaceProjectPagePublication entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get the root researcher publications for given researcher
	 * 
	 * @see edu.ur.ir.researcher.ResearcherPublicationDAO#getRootResearcherPublications(Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPagePublication> getRootPublications(final Long projectPageId)
	{
		log.debug("getRootProjectPagePublications::");
		Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
		criteria.createCriteria("groupWorkspaceProjectPage").add(Restrictions.idEq(projectPageId));
        criteria.add(Restrictions.isNull("parentFolder"));
        return criteria.list();

	}
    
	/**
	 * Get researcher publications for specified researcher and specified parent folder
	 * 
	 * @see edu.ur.ir.researcher.ResearcherPublicationDAO#getSubResearcherPublications(Long, Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPagePublication> getPublications(final Long projectPageId, final Long parentCollectionId)
	{
		log.debug("getSubResearcherPublications::");
		Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
		criteria.createCriteria("groupWorkspaceProjectPage").add(Restrictions.idEq(projectPageId));
        criteria.createCriteria("parentFolder").add(Restrictions.idEq(parentCollectionId));
        return criteria.list();
	}


	/**
	 * Find the specified items for the given researcher.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherPublicationDAO#getResearcherPublications(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceProjectPagePublication> getPublications(final Long projectPageId, final List<Long> itemIds) {
		List<GroupWorkspaceProjectPagePublication> foundItems = new LinkedList<GroupWorkspaceProjectPagePublication>();
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
	 * Get a count of the researcher publications containing this item
	 * 
	 * @see edu.ur.ir.researcher.ResearcherPublicationDAO#getResearcherPublicationCount(Long)
	 */
	public Long getCount(Long itemId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWorkspaceProjectPagePublicationCount");
		q.setLong("itemId", itemId);
		return (Long)q.uniqueResult();
	}

}
