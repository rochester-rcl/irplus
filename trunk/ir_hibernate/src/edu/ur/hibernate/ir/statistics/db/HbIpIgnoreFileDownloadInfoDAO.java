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
import edu.ur.ir.statistics.IpIgnoreFileDownloadInfo;
import edu.ur.ir.statistics.IpIgnoreFileDownloadInfoDAO;


/**
 * Ip ignore file download info persistance
 * 
 * @author Nathan Sarr
 *
 */
public class HbIpIgnoreFileDownloadInfoDAO implements IpIgnoreFileDownloadInfoDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 6559930974830950432L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<IpIgnoreFileDownloadInfo> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbIpIgnoreFileDownloadInfoDAO() {
		hbCrudDAO = new HbCrudDAO<IpIgnoreFileDownloadInfo>(IpIgnoreFileDownloadInfo.class);
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
	 * Get the ip ignore file download info.
	 * 
	 * @see edu.ur.ir.statistics.IpIgnoreFileDownloadInfoDAO#getIpIgnoreFileDownloadInfo(java.lang.String, java.lang.Long, java.util.Date)
	 */
	public IpIgnoreFileDownloadInfo getIpIgnoreFileDownloadInfo(
			String ipAddress, Long fileId, Date date) {
	    Object[] values = new Object[] {ipAddress, fileId, date};
		
		return (IpIgnoreFileDownloadInfo)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIpIgnoreFileDownloadInfo", values));
	}

	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("ipIgnoreFileDownloadInfoCount"));
	}

	public List<IpIgnoreFileDownloadInfo> getAll() {
		return hbCrudDAO.getAll();
	}

	public IpIgnoreFileDownloadInfo getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(IpIgnoreFileDownloadInfo entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(IpIgnoreFileDownloadInfo entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List<IpIgnoreFileDownloadInfo> getIgnoreInfoNowAcceptable(final int rowStart,
			final int maxResults) {
        List<IpIgnoreFileDownloadInfo> foundItems = new LinkedList<IpIgnoreFileDownloadInfo>();
		
		foundItems = (List<IpIgnoreFileDownloadInfo>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = session.getNamedQuery("getAcceptableFileDownloadsIgnored");
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

	public Long getIgnoreInfoNowAcceptableCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAcceptableFileDownloadsIgnoredCount"));
	}

}
