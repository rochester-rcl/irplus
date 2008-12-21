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

package edu.ur.hibernate.ir.institution.db;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.item.GenericItem;

/**
 * Persist collection information using hibernate.
 * 
 * @author Nathan Sarr
 *
 */
public class HbInstitutionalCollectionDAO implements InstitutionalCollectionDAO {

	private final HbCrudDAO<InstitutionalCollection> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbInstitutionalCollectionDAO() {
		hbCrudDAO = new HbCrudDAO<InstitutionalCollection>(InstitutionalCollection.class);
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
	 * Get a count of the collections.
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("irCollectionCount"));
	}

	/**
	 * Get all the collections with the specified name.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalCollection> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(hbCrudDAO.getClazz());
    	dc.addOrder(Order.asc("name"));
    	return (List<InstitutionalCollection>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all collections ordered by name.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<InstitutionalCollection> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllIrCollectionNameAsc", startRecord, numRecords);
	}

	/**
	 * Find a collection listed by name.
	 * 
	 * @see edu.ur.NonUniqueNameDAO#findByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalCollection> findByName(String name) {
	  	return (List<InstitutionalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrCollectionByName", name);
	}
	
	/**
	 * Find the Collection for the specified repositoryId and specified 
	 * collection name.
	 * 
	 * @param name of the collection
	 * @param repositoryId id of the repository
	 * @return the found collection or null if the collection is not found.
	 */
	public InstitutionalCollection getRootCollection(String name, Long repositoryId)
	{
		Object[] values = new Object[] {name, repositoryId};
		return (InstitutionalCollection) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getRootIrCollectionByNameRepository", 
				values));
	}
	
	/**
	 * Find the collection for the specified collection name and 
	 * parent id.
	 * 
	 * @param name of the collection
	 * @param parentId id of the parent collection
	 * @return the found collection or null if the collection is not found.
	 */
	public InstitutionalCollection getCollection(String name, Long parentId)
	{
		Object[] values = new Object[] {name, parentId};
		return (InstitutionalCollection) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrCollectionByNameParent", 
				values));
	}
	
	/**
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getNodesLeftRightGreaterEqual(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalCollection> getNodesLeftRightGreaterEqual(Long value, Long rootId) {
		Long[] values = new Long[] {value, value, rootId};
		return (List<InstitutionalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllIrCollectionsValueGreaterEqual", values);
	}
	
	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getAllNodesNotInChildTree(edu.ur.ir.institution.InstitutionalCollection)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalCollection> getAllNodesNotInChildTree(InstitutionalCollection irCollection)
	{
		Long[] ids = new Long[] {irCollection.getLeftValue(), irCollection.getRightValue(), 
				irCollection.getTreeRoot().getId()};
		List<InstitutionalCollection> listTree =  (List<InstitutionalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllIrCollectionsNotInChildTree", ids);
		return listTree;
	}
	
	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getAllIrCollectionsInRepository(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalCollection> getAllIrCollectionsInRepository(Long repositoryId) {
		List<InstitutionalCollection> collections =  (List<InstitutionalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrCollectionsInRepository", 
				repositoryId);
		return collections;
	}
	
	/**
	 * Get all collections in the repository except for the specified collection
	 * and all it's children.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getAllNodesNotInChildTreeRepo(edu.ur.ir.institution.InstitutionalCollection)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalCollection> getAllNodesNotInChildTreeRepo(InstitutionalCollection collection) {
		Long[] ids = new Long[] {collection.getLeftValue(), collection.getRightValue(), 
				collection.getTreeRoot().getId(), collection.getRepository().getId(),
				collection.getRepository().getId(), collection.getTreeRoot().getId()};
		List<InstitutionalCollection> listTree =  (List<InstitutionalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllFoldersNotInChild", ids);
		return listTree;
	}
	
	/**
	 * Gets the path to the collection starting from the top parent all the way
	 * down to the specified child.  Only includes parents of the specified 
	 * collection.  The list is ordered highest level parent to last child.  This
	 * is useful for displaying the path to a given collection.
	 * 
	 * @param collection 
	 * @return list of parent collections.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getPath(edu.ur.ir.institution.InstitutionalCollection)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalCollection> getPath(InstitutionalCollection collection)
	{
		Long[] values = new Long[] {collection.getLeftValue(), collection.getTreeRoot().getId(), collection.getRepository().getId()};
		return (List<InstitutionalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrCollectionPath", values);
	}

	/**
	 * Get all institutional collections.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get an institutional collection by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public InstitutionalCollection getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make an institutinal collection persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(InstitutionalCollection entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the institutional collection transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(InstitutionalCollection entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the sub or child institutional collections.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getSubInstituionalCollections(java.lang.Long, java.lang.Long, int, int, String)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalCollection> getSubInstituionalCollections(
			final Long repositoryId,
			final Long parentCollectionId,
			final int rowStart, final int maxNumToFetch, final String orderType) {
		List<InstitutionalCollection> institutionalCollections = (List<InstitutionalCollection>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (orderType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getSubCollectionsByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getSubCollectionsByNameDesc");
		        }
		        q.setParameter("repositoryId", repositoryId);
		        q.setParameter("parentCollectionId", parentCollectionId);
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxNumToFetch);
			    q.setReadOnly(true);
			    q.setFetchSize(maxNumToFetch);
	            return q.list();
            }
        });

        return institutionalCollections;
	}
	
	/**
	 * Get the root institutional collections.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getRootInstituionalCollections(java.lang.Long, int, int, String)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalCollection> getRootInstituionalCollections(
			final Long repositoryId,
			final int rowStart, final int maxNumToFetch, final String orderType) {
		List<InstitutionalCollection> institutionalCollections = (List<InstitutionalCollection>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (orderType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getRootCollectionsByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getRootCollectionsByNameDesc");
		        }
		        q.setParameter("repositoryId", repositoryId);
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxNumToFetch);
			    q.setReadOnly(true);
			    q.setFetchSize(maxNumToFetch);
	            return q.list();            }
        });

        return institutionalCollections;
	}


	/**
	 * Get a count of the sub institutional collections
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getSubInstitutionalCollectionsCount( 
	 * java.lang.Long, java.lang.Long,  java.lang.String)
	 */
	public Long getSubInstitutionalCollectionsCount(final Long repositoryId,
			final Long parentCollectionId) {
    	Long count = (Long) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
	        	q = session.getNamedQuery("getSubCollectionsCount");
		        q.setParameter("repositoryId", repositoryId);
		        q.setParameter("parentCollectionId", parentCollectionId);
	            return q.uniqueResult();
            }
        });
    	return count;
	}
	
	/**
	 * Get a count of the root institutional collections
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getRootInstitutionalCollectionsCount(java.lang.Long)
	 */
	public Long getRootInstitutionalCollectionsCount(final Long repositoryId) {
    	Long count = (Long) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
	        	q = session.getNamedQuery("getRootCollectionsCount");
		        q.setParameter("repositoryId", repositoryId);
	            return q.uniqueResult();
            }
        });
    	return count;

	}

	
	/**
	 * Get the institutional collections for given collection Ids
	 * 
	 * @param collectionIds - List of collection ids
	 * 
	 * @return List of institutional collection
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalCollection> getInstituionalCollections(final List<Long> collectionIds) {
	
		List<InstitutionalCollection> foundCollections = new LinkedList<InstitutionalCollection>();
			if( collectionIds.size() > 0 )
	        {
				foundCollections = (List<InstitutionalCollection>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
			    public Object doInHibernate(Session session)
	                throws HibernateException, SQLException {
	                    Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
	                    criteria.add(Restrictions.in("id",collectionIds));
	                return criteria.list();
	                }
	             });
	        }
			return foundCollections;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.repository.InstitutionalCollectionDAO#getAllChildrenForCollection(edu.ur.ir.repository.InstitutionalCollection)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalCollection> getAllChildrenForCollection(
			InstitutionalCollection parentCollection) {
		Long[] ids = new Long[] {parentCollection.getLeftValue(), parentCollection.getRightValue(), 
				parentCollection.getTreeRoot().getId()};
		List<InstitutionalCollection> listTree =  (List<InstitutionalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllInstitutionalCollectionChildren", ids);
		return listTree;
	}
	
	/**
     * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getAllChildrenCountForCollection(edu.ur.ir.institution.InstitutionalCollection)
     */	
	public Long getAllChildrenCountForCollection(
			InstitutionalCollection parentCollection) {
		Long[] ids = new Long[] {parentCollection.getLeftValue(), parentCollection.getRightValue(), 
				parentCollection.getTreeRoot().getId()};
		return (Long) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllInstitutionalCollectionChildrenCount", ids));
		 
	}

	/**
     * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getIrFileIdsForCollection(Long)
     */	
	@SuppressWarnings("unchecked")
	public List<Long> getIrFileIdsForCollection(Long collectionId) {
		return (List<Long>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrFileIdsForCollection", collectionId);
	}
	
	/**
     * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getIrFileIdsForCollectionAndSubCollections(edu.ur.ir.institution.InstitutionalCollection)
     */	
	@SuppressWarnings("unchecked")
	public List<Long> getIrFileIdsForCollectionAndItsChildren(InstitutionalCollection parentCollection) {
		
		Long[] ids = new Long[] {parentCollection.getLeftValue(), parentCollection.getRightValue(), 
				parentCollection.getTreeRoot().getId()};
		
		return (List<Long>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrFileIdsForCollectionAndItsSubCollection", ids);
	}
	
	/**
     * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getItemIrFileIdsForAllCollections()
     */	
	@SuppressWarnings("unchecked")
	public List<Long> getIrFileIdsForAllCollections() {
		return (List<Long>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrFileIdsForAllCollections");
	}

	
	/**
	 * Get all generic items including generic items used in the children.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionDAO#getAllGenericItemsIncludingChildren(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GenericItem> getAllGenericItemsIncludingChildren(
			InstitutionalCollection parentCollection) {
		
		Long[] ids = new Long[] {parentCollection.getLeftValue(), parentCollection.getRightValue(), 
				parentCollection.getTreeRoot().getId()};
		List<GenericItem> listTree =  (List<GenericItem>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllGenericItemsIncludingChildren", ids);
		return listTree;
	}
}
