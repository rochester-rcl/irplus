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

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDownloadCount;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.statistics.FileDownloadInfo;
import edu.ur.ir.statistics.FileDownloadInfoDAO;

/**
 * File download info persistance
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbFileDownloadInfoDAO implements FileDownloadInfoDAO {

	/**
	 * Helper for persisting information using hibernate. 
	 */
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
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("fileDownloadCount"));
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
	 * Get the number of downloads for a collection - does not include child collection file downloads.
	 * 
	 * @see edu.ur.ir.statistics.FileDownloadInfoDAO#getNumberOfFileDownloadsForCollection(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public Long getNumberOfFileDownloadsForCollection(InstitutionalCollection institutionalCollection) {
		Long value =   (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getCollectionFileDownloadCount", institutionalCollection.getId()));
	
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
	 *   Get the number of downloads for a collection - includes child collection file downloads.
	 * @see edu.ur.ir.statistics.FileDownloadInfoDAO#getNumberOfFileDownloadsForCollectionIncludingChildren(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public Long getNumberOfFileDownloadsForCollectionIncludingChildren(InstitutionalCollection institutionalCollection) {
		
		Long[] ids = new Long[] {institutionalCollection.getLeftValue(), institutionalCollection.getRightValue(), 
				institutionalCollection.getTreeRoot().getId()};
		Long value =    (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getCollectionFileDownloadCountWithChildren", ids));
		
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
	 * Get the number of downloads for the entire repository.
	 * 
	 * @see edu.ur.ir.statistics.FileDownloadInfoDAO#getNumberOfFileDownloadsForRepository()
	 */
	public Long getNumberOfFileDownloadsForRepository() {
		Long value =   (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getRepositoryFileDownloadCount"));
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
	 * Get the count for a specified ir file across all dates.
	 * 
	 * @see edu.ur.ir.statistics.FileDownloadInfoDAO#getNumberOfFileDownloadsForIrFile(edu.ur.ir.file.IrFile)
	 */
	public Long getNumberOfFileDownloadsForIrFile(IrFile irFile) {
		Long value =    (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getIrFileDownloadCount", irFile.getId()));
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
	 * Get number of times the files in the specified item is downloaded.
	 * 
	 * @see edu.ur.ir.statistics.FileDownloadInfoDAO#getNumberOfFileDownloadsForItem(GenericItem)
	 */
	public Long getNumberOfFileDownloadsForItem(GenericItem item) {
		Long value =    (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getItemFileDownloadCount", item.getId()));
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
	 * Get most downloaded institutitonal item for specified person name ids.
	 * 
	 * @see edu.ur.ir.statistics.FileDownloadInfoDAO#getInstitutionalItemDownloadCountByPersonName(List)
	 */
	@SuppressWarnings("unchecked")	
	public InstitutionalItemDownloadCount getInstitutionalItemDownloadCountByPersonName(List<Long> personNameIds) {
		
	    Query q = hbCrudDAO.getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemDownloadCountByPersonName");
		
	    q.setParameterList("personNameIds", personNameIds);
	
	    InstitutionalItemDownloadCount mostDownloaded = null;

	    Iterator<Object[]> it = q.iterate();
	    
	    if (it.hasNext()) {
	    
	    	 Object[] row = (Object[]) it.next();
	    	 mostDownloaded = new InstitutionalItemDownloadCount((Long)row[0], (InstitutionalItem) row[2]);

	    }
	    
	    return mostDownloaded;
	}
	

	
}
;