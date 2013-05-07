/**  
   Copyright 2012 University of Rochester

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

package edu.ur.hibernate.file.db;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.file.db.FileInfoChecksumDAO;
import edu.ur.file.db.FileInfoChecksum;
import edu.ur.hibernate.HbCrudDAO;
import edu.ur.order.OrderType;

/**
 * DAO class for checksum info 
 * 
 * @author Nathan Sarr
 *
 */
public class HbDefaultFileInfoChecksumDAO implements FileInfoChecksumDAO{

	
	/* eclipse generated id  */
	private static final long serialVersionUID = 1613262779997311367L;
	
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<FileInfoChecksum> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbDefaultFileInfoChecksumDAO() {
		hbCrudDAO = new HbCrudDAO<FileInfoChecksum>(FileInfoChecksum.class);
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
	
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	public FileInfoChecksum getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(FileInfoChecksum entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(FileInfoChecksum entity) {
		hbCrudDAO.makePersistent(entity);
	}

	@SuppressWarnings("unchecked")
	public List<FileInfoChecksum> getOldestChecksumsForChecker(int start, int maxResults, Date beforeDate) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getOlderChecksumForChecker");
		q.setParameter("minDate", new Timestamp(beforeDate.getTime()));
		q.setFirstResult(start);
		q.setMaxResults(maxResults);
		return q.list();
	}

	
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("fileInfoChecksumCount");
		return (Long) q.uniqueResult();
	}

	/**
	 * Get the checksum file info's in date order 
	 * 
	 * @param start - start position.
	 * @param maxResults - maximum number of results to retrieve.
	 * @param onlyFails - get only the one's that have failed their checksum
	 * @param orderType - order date ascending or descending
	 * 
	 * @return - all checksum infos found
	 */
	@SuppressWarnings("unchecked")
	public List<FileInfoChecksum> getChecksumInfosDateOrder(int start,
			int maxResults, boolean onlyFails, OrderType orderType) {
		
		if( onlyFails )
		{
			if( OrderType.ASCENDING_ORDER.equals(orderType))
			{
		        Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getChecksumInfoFailsDateOrderAsc");
		        q.setFirstResult(start);
		        q.setMaxResults(maxResults);
		        return q.list();
			}
			else
			{
				Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getChecksumInfoFailsDateOrderDesc");
		        q.setFirstResult(start);
		        q.setMaxResults(maxResults);
		        return q.list();
			}
		}
		else
		{
			if( OrderType.ASCENDING_ORDER.equals(orderType))
			{
			    Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getChecksumInfoDateOrderAsc");
		        q.setFirstResult(start);
		        q.setMaxResults(maxResults);
		        return q.list();
			}
			else
			{
				Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getChecksumInfoDateOrderDesc");
		        q.setFirstResult(start);
		        q.setMaxResults(maxResults);
		        return q.list();
			}
		}
	}

	/**
	 * Get a count of fails for checksum infos.
	 * 
	 * @return count of failed checksums.
	 */
	public Long getChecksumInfoFailsCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getChecksumInfoFailsCount");
		return (Long) q.uniqueResult();
	}

}
