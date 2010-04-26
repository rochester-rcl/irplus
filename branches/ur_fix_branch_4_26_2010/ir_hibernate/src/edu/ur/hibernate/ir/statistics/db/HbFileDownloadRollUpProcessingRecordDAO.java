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
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.statistics.FileDownloadRollUpProcessingRecord;
import edu.ur.ir.statistics.FileDownloadRollUpProcessingRecordDAO;

/**
 * Implementation of the file download processing record data access object.
 * 
 * @author Nathan Sarr
 *
 */
public class HbFileDownloadRollUpProcessingRecordDAO implements FileDownloadRollUpProcessingRecordDAO{

	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<FileDownloadRollUpProcessingRecord> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbFileDownloadRollUpProcessingRecordDAO() {
		hbCrudDAO = new HbCrudDAO<FileDownloadRollUpProcessingRecord>(FileDownloadRollUpProcessingRecord.class);
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
	
	
	public Long updateAllRepositoryDownloadCounts() {
		return (Long) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		      
		        Query q = session.getNamedQuery("insertAllRepositoryFileIds");
			    return new Long(q.executeUpdate());
            }
		});
	}

	public List<FileDownloadRollUpProcessingRecord> getAll() {
		return hbCrudDAO.getAll();
	}

	public FileDownloadRollUpProcessingRecord getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	public void makePersistent(FileDownloadRollUpProcessingRecord entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(FileDownloadRollUpProcessingRecord entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	public FileDownloadRollUpProcessingRecord getByIrFileId(Long irFileId) {
		return (FileDownloadRollUpProcessingRecord)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getRollUpProcessingRecordByIrFileId", irFileId));
	}

	
	@SuppressWarnings("unchecked")
	public List<FileDownloadRollUpProcessingRecord> getProcessingRecords(
			final int start, final int maxResults) {
		return (List<FileDownloadRollUpProcessingRecord>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = session.getNamedQuery("getAllFileDownloadRollUpProcessingRecords");
		        q.setReadOnly(true);
		        q.setCacheable(false);
		        q.setFirstResult(start);
		        q.setMaxResults(maxResults);
		        q.setFetchSize(maxResults);
			    return (List<FileDownloadRollUpProcessingRecord>) q.list();
            }
		});
	}

}
