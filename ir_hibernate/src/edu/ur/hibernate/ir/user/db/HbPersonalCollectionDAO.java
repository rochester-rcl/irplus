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

package edu.ur.hibernate.ir.user.db;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.PersonalCollectionDAO;
import edu.ur.ir.user.PersonalItem;


/**
 * Hibernate implementation for storing personal collection information.
 * 
 * @author Nathan Sarr
 *
 */
public class HbPersonalCollectionDAO implements PersonalCollectionDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -8590531742101408339L;

	/** Logger */
	private static final Logger log = LogManager.getLogger(HbPersonalCollectionDAO.class);

	/** Helper for stroring hibernate information. */
	private final HbCrudDAO<PersonalCollection> hbCrudDAO;
	
	
	/**
	 * Default Constructor
	 */
	public HbPersonalCollectionDAO() {
		hbCrudDAO = new HbCrudDAO<PersonalCollection>(PersonalCollection.class);
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
	 * Get all personal items for a given collection.  This includes all items in sub-collections.
	 * 
	 * @see edu.ur.ir.user.PersonalCollectionDAO#getAllItemsForCollection(edu.ur.ir.user.PersonalCollection)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalItem> getAllItemsForCollection(PersonalCollection personalCollection) {
		Long[] ids = new Long[] {personalCollection.getLeftValue(),
				personalCollection.getRightValue(), 
				personalCollection.getTreeRoot().getId()};
		List<PersonalItem> personalItems =  (List<PersonalItem>) 
		hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllPersonalItemsForCollection", ids);
		return personalItems;

	}

	/**
	 * Get all nodes except for the node and its children.  This stays
	 * within it's own file database  
	 * 
	 * @param personal collection- the node we want the tree/subtree to exclude.
	 * 
	 * 
	 * @return the tree or null if no tree found
	 * @see edu.ur.ir.user.PersonalCollectionDAO#getAllNodesNotInChildTree(edu.ur.ir.user.PersonalCollection)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalCollection> getAllNodesNotInChildTree(PersonalCollection personalCollection) {
		Long[] ids = new Long[] {personalCollection.getOwner().getId(), 
				personalCollection.getTreeRoot().getId(), 
				personalCollection.getLeftValue(),personalCollection.getRightValue() };
		List<PersonalCollection> listTree =  (List<PersonalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllPersonalCollectionsNotInChildTree", ids);
		return listTree;

	}


	/**
	 * Get all personal collections for the specified user.
	 * @see edu.ur.ir.user.PersonalCollectionDAO#getAllPersonalCollectionsForUser(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalCollection> getAllPersonalCollectionsForUser(Long userId) {
		List<PersonalCollection> collections =  (List<PersonalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonalCollectionsForUser", 
				userId);
		return collections;

	}

	/**
	 * Get all nodes who have a left or right value greater than the specified value in 
	 * the tree with the specified root id.
	 * 
	 * @see edu.ur.ir.user.PersonalCollectionDAO#getNodesLeftRightGreaterEqual(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalCollection> getNodesLeftRightGreaterEqual(Long value, Long rootId) {
		Long[] values = new Long[] {value, value, rootId};
		return (List<PersonalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllPersonalCollectionsValueGreaterEqual", values);

	}

	/**
	 * Gets the path to the collection starting from the top parent all the way
	 * down to and including the specified child.  Only includes parents of the specified 
	 * collection.  The list is ordered highest level parent to last child.  This
	 * is useful for displaying the path to a given collection.
	 * 
	 * @param collection 
	 * @return list of parent collections.
	 * 
	 * @see edu.ur.ir.user.PersonalCollectionDAO#getPath(edu.ur.ir.user.PersonalCollection)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalCollection> getPath(PersonalCollection personalCollection) {
		Long[] values = new Long[] {personalCollection.getLeftValue(), personalCollection.getTreeRoot().getId(), 
				personalCollection.getOwner().getId()};
		return (List<PersonalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonalCollectionPath", values);

	}

	/**
	 * Find the personal folder for the specified folder name and 
	 * parent id.
	 * 
	 * @param name of the folder
	 * @param parentId id of the parent folder
	 * @return the found personal collection or null if the folder is not found.
	 * 
	 * @see edu.ur.ir.user.PersonalCollectionDAO#getPersonalCollection(java.lang.String, java.lang.Long)
	 */
	public PersonalCollection getPersonalCollection(String name, Long parentId) {
		Object[] values = new Object[] {name, parentId};
		return (PersonalCollection) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonalCollectionByNameParent", 
				values));
	}

	/**
	 * Get personal collections sorting according to the sort and filter information for the 
	 * specified user - this only returns root personal collection.
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
	 * @param userId
	 * @param rowStart
	 * @param maxNumToFetch
	 * @return List of root collections containing the specified information.
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalCollection> getRootPersonalCollections( final Long userId, 
			final int rowStart, final int maxNumToFetch) {
		List<PersonalCollection> personalCollections = (List<PersonalCollection>)hbCrudDAO.getHibernateTemplate().executeFind( new HibernateCallback() 
		{
	         public Object doInHibernate(Session session)
	         {
	     	    Query query = session.getNamedQuery("getRootPersonalCollections");
	     	    query.setParameter(1, userId);
	     		query.setFirstResult(rowStart);
	     		query.setMaxResults(maxNumToFetch);
	     		query.setFetchSize(maxNumToFetch);
               return query.list();
	         }
		});
        return personalCollections;
	}

	/**
	 * Find the personal collection for the specified userId and specified 
	 * folder name.
	 * 
	 * @param name of the collection
	 * @param userId - id of the user
	 * @return the found collection or null if the collection is not found. 
	 */
	public PersonalCollection getRootPersonalCollection(String name, Long userId) {
		Object[] values = new Object[] {name, userId};
		return (PersonalCollection) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getRootPersonalCollectionByNameUser", 
				values));
	}

	/**
	 * 
	 * Get personal collections sorting according to the specified criteria and for the 
	 * specified user - this returns sub folders.
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
	 * @param criteriaHelper - set of criteria that must be met.
     * @param parentFolderId - the id of the parent folder id for the folders
	 * @param rowStart - start position in paged set
	 * @param rowEnd - maximum number of results to fetch
	 * @return List of root collections containing the specified information.
	 * 
	 *  @see edu.ur.ir.user.PersonalCollectionDAO#getSubPersonalCollections(java.util.List, java.lang.Long, java.lang.Long, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalCollection> getSubPersonalCollections( final Long userId, 
			final Long parentPersonalCollectionId) {
		
		log.debug("get sub personal collections 2");
        Long[] values = new Long[] {userId, parentPersonalCollectionId};
		
        List<PersonalCollection> personalCollections  =  
			(List<PersonalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonalSubCollectionsForCollection", 
					values);
		return personalCollections;
		
	}


	/**
	 * Get a count of the folders
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("personalCollectionCount"));

	}

	/**
	 * Get all personal collections in the system.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get a personal collection.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public PersonalCollection getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 *  Save the collection
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(PersonalCollection entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the collection transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(PersonalCollection entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * 
	 * @see edu.ur.ir.user.PersonalCollectionDAO#getAllNodesNotInChildTrees(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalCollection> getAllCollectionsNotInChildCollections(
			final List<PersonalCollection> collections, final Long userId, 
			final Long rootCollectionId) {
		
		List<PersonalCollection> foundCollections = new LinkedList<PersonalCollection>();
		
		for(PersonalCollection c : collections)
		{
			Object[] values = {userId, rootCollectionId, c.getLeftValue(), c.getRightValue()};
			foundCollections.addAll((List<PersonalCollection>)hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllPersonalCollectionsNotInChildTree", values));
		}
			
		return foundCollections;
	}
	
	
	/**
	 * Get all root collections not within the given set.
	 * 
	 * @see edu.ur.ir.user.PersonalCollectionDAO#getAllOtherRootCollections(java.util.List, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalCollection> getAllOtherRootCollections(final List<Long> rootCollectionIds, 
			final Long userId) {
		String[] parameters = {"ownerId", "collections"};
		Object[] values = {userId, rootCollectionIds};
		List<PersonalCollection> foundCollections = new LinkedList<PersonalCollection>();
		if( rootCollectionIds.size() > 0 )
        {
			foundCollections = (List<PersonalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQueryAndNamedParam("getAllOtherRootCollections", parameters, values); 
        }
		return foundCollections;
	}

	/**
	 * Find the specified collections.
	 * 
	 * @see edu.ur.ir.user.PersonalCollectionDAO#getPersonalCollections(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalCollection> getPersonalCollections(final Long userId, final List<Long> collectionIds) {
		String[] parameters = {"ownerId", "collections"};
		Object[] values = {userId, collectionIds};
		List<PersonalCollection> foundCollections = new LinkedList<PersonalCollection>();
		if( collectionIds.size() > 0 )
        {
			foundCollections = (List<PersonalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQueryAndNamedParam("getAllPersonalCollectionsInList", parameters, values); 
        }
		return foundCollections;
	}

	
	/**
	 * Get the root collections for the specified user.
	 * 
	 * @see edu.ur.ir.user.PersonalCollectionDAO#getRootCollections(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalCollection> getRootPersonalCollections(Long userId) {
		List<PersonalCollection> collections =  
			(List<PersonalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getRootPersonalCollections", 
					userId);
		return collections;
	}

	
	/**
	 * Get the specified sub collections for the specified user id and collection id.
	 * 
	 * @see edu.ur.ir.user.PersonalCollectionDAO#getSubCollectionsForCollection(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalCollection> getPersonalSubCollectionsForCollection(Long userId,
			Long parentCollectionId) {
		Long[] values = new Long[] {userId, parentCollectionId};
		
		List<PersonalCollection> collections =  
			(List<PersonalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPersonalSubCollectionsForCollection", 
					values);
		return collections;
	}

}
