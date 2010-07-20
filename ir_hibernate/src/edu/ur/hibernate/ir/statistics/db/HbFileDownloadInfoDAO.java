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

package edu.ur.hibernate.ir.statistics.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.statistics.FileDownloadInfo;
import edu.ur.ir.statistics.FileDownloadInfoDAO;
import edu.ur.ir.statistics.IpDownloadCount;
import edu.ur.order.OrderType;

/**
 * File download info persistance
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbFileDownloadInfoDAO implements FileDownloadInfoDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 5675351756547973376L;

	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<FileDownloadInfo> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbFileDownloadInfoDAO() {
		hbCrudDAO = new HbCrudDAO<FileDownloadInfo>(FileDownloadInfo.class);
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
	 * Get a count of the FileCollaborator
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("fileDownloadInfoCount"));
	}
	
	public List<FileDownloadInfo> getAll() {
		return hbCrudDAO.getAll();
	}

	public FileDownloadInfo getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Save FileCollaborator
	 *  
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(FileDownloadInfo entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Delete FileCollaborator
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(FileDownloadInfo entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get a file download info for specified IP address, file Id and download date
	 * 
	 */
	public FileDownloadInfo getFileDownloadInfo(String ipAddress, Long fileId, Date date) {
		
		Object[] values = new Object[] {ipAddress, fileId, date};
		
		return (FileDownloadInfo)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getDownloadInfo", values));
	}

 
	/**
	 * Get the count for a specified ir file across all dates.
	 * 
	 * @see edu.ur.ir.statistics.FileDownloadInfoDAO#getNumberOfFileDownloadsForIrFile(edu.ur.ir.file.IrFile)
	 */
	public Long getNumberOfFileDownloadsForIrFile(Long irFileId) {
		Long value =    (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrFileDownloadCount", irFileId));
		if( value != null )
		{
		    return value;
		}
		else
		{
		    return 0l;
		}
	}

	/**
	 * This retrieves all file download info objects that are currently in the ignore
	 * ip ranges.
	 * 
	 * @return the list of file download info objects that are ignored.
	 * @see edu.ur.ir.statistics.FileDownloadInfoDAO#getAllDownloadInfoIgnored()
	 */
	@SuppressWarnings("unchecked")
	public List<FileDownloadInfo> getDownloadInfoIgnored(final int rowStart,
			final int maxResults) {
		List<FileDownloadInfo> foundItems = new LinkedList<FileDownloadInfo>();
		
		foundItems = (List<FileDownloadInfo>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = session.getNamedQuery("getFileDownloadsIgnored");
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setCacheable(false);
			    q.setReadOnly(true);
			    q.setFetchSize(maxResults);
	            return q.list();
		    }
	    });
        return foundItems;	
	}

	/**
	 * Get the count of file download info's that would be ignored.
	 * 
	 * @return count of ignored download info values.
	 */
	public Long getDownloadInfoIgnoredCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getFileDownloadsIgnoredCount"));
	}

	
	/**
	 * Get a sum of downlod coungs grouped by IP address order by download count.
	 * 
	 * @see edu.ur.ir.statistics.FileDownloadInfoDAO#getIpOrderByDownloadCount(int, int, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<IpDownloadCount> getIpOrderByDownloadCount(int rowStart,
			int maxResults, OrderType orderType) {
		
		Query q = null;
		if (orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getDownloadInfoCountSumDesc");
		} else {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getDownloadInfoCountSumAsc");
		}

		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return (List<IpDownloadCount>) q.list();
	}

	public Long deleteIgnoreCounts() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("deleteIgnoredFileDownloadInfoCounts");
	    return new Long(q.executeUpdate());
	}

	public Long insertIntoIgnoreFileDownloadInfoCounts(List<Long> fileInfoIds) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertIntoIgnoreFileDownloadInfo");
		q.setParameterList("ids", fileInfoIds);
	    return new Long(q.executeUpdate());
	}

	public Long delete(List<Long> ids) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("deleteFromFileDownloadInfoByIds");
		q.setParameterList("ids", ids);
	    return new Long(q.executeUpdate());
	}
	
}
