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
import edu.ur.ir.statistics.FileDownloadRollUp;
import edu.ur.ir.statistics.FileDownloadRollUpDAO;

/**
 * Implementation of the file download rollup DAO.
 * 
 * @author Nathan Sarr
 *
 */
public class HbFileDownloadRollUpDAO implements FileDownloadRollUpDAO{

	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<FileDownloadRollUp> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbFileDownloadRollUpDAO() {
		hbCrudDAO = new HbCrudDAO<FileDownloadRollUp>(FileDownloadRollUp.class);
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
	 * Get all file download roll up objects.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<FileDownloadRollUp> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get the roll up object by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public FileDownloadRollUp getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Save the roll up object.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(FileDownloadRollUp entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Delete the roll up object.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(FileDownloadRollUp entity) {
		hbCrudDAO.makeTransient(entity);
	}

	public FileDownloadRollUp getByIrFileId(Long irFileId) {
		return (FileDownloadRollUp)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getRollUpByIrFileId", irFileId));
	}
	
	/**
	 * Get the number of downloads for a collection - does not include child collection file downloads.
	 * 
	 * @see edu.ur.ir.statistics.FileDownloadInfoDAO#getNumberOfFileDownloadsForCollection(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public Long getNumberOfFileDownloadsForCollection(InstitutionalCollection institutionalCollection) {
		Long value =   (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getCollectionRollUpFileDownloadCount", institutionalCollection.getId()));
	
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
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getCollectionRollUpFileDownloadCountWithChildren", ids));
		
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
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getRepositoryRollUpFileDownloadCount"));
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
		Long value = (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getRollUpFileDownloadCount", irFile.getId()));
		if( value != null )
		{
		    return value.longValue();
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
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getItemRollUpFileDownloadCount", item.getId()));
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
		
	    Query q = hbCrudDAO.getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemRollUpDownloadCountByPersonName");
		
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
