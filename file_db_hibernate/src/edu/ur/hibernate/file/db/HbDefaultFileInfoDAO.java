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

package edu.ur.hibernate.file.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import edu.ur.file.db.DefaultFileInfo;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FileInfoDAO;
import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;

/**
 * Persist a File Info object
 * 
 * @author Nathan Sarr
 *
 */
public class HbDefaultFileInfoDAO implements FileInfoDAO {
	
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<DefaultFileInfo> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbDefaultFileInfoDAO() {
		hbCrudDAO = new HbCrudDAO<DefaultFileInfo>(DefaultFileInfo.class);
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
     * Return all folders orderd by Name.  This list 
     * can be extreamly large and it is recomened that
     * paging is used instead
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<DefaultFileInfo> getAllNameOrder()
    {
    	DetachedCriteria dc = DetachedCriteria.forClass(DefaultFileInfo.class);
    	dc.addOrder(Order.asc("displayName"));
    	return (List<DefaultFileInfo>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
    }
    
    /**
     * Get the number of folders in the system
     * 
     * @return the number of folders
     */
    public Long getCount()
    {
    	return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("fileCount"));
    }
    
    /**
     * Get all folders starting at the start record and get up to 
     * the numRecords - it will be ordered by name
     *  
     * @param startRecord - the index to start at
     * @param numRecords - the number of records to get
     * @return the records found
     */
    public List<DefaultFileInfo> getAllOrderByName(int startRecord, int numRecords)
    {
    	return hbCrudDAO.getByQuery("getFileInfoByDisplayName", startRecord, numRecords);
    }

	/** 
     * Find a set of files by their display name
     * 
     * @param name - name of the file.
     * @return the list of files found with the specified name.
	 */
    @SuppressWarnings("unchecked")
	public List<DefaultFileInfo> findByName(String name) {
	  	return (List<DefaultFileInfo>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getFileInfoByDisplayName", name);
	}

	/** 
     * Find a file by it's file name
     * 
     * @param name - name of the file.
     * @return the found file information
	 */
	public DefaultFileInfo findByUniqueName(String name) {
		return (DefaultFileInfo) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getFileInfoByFileName", name));
	}
	
	/**
	 * 
	 * @see edu.ur.file.db.FileInfoDAO#findByDisplayName(java.lang.String, java.lang.Long)
	 */
	public DefaultFileInfo findByDisplayName(String name, Long parentFolderId)
	{
		Object[] values = {name, parentFolderId};
		return (DefaultFileInfo) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getFileByDisplayParent", values));
	}

	public List<DefaultFileInfo> getAll() {
		return hbCrudDAO.getAll();
	}

	public DefaultFileInfo getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * <b>File infor should be a FileInfoImpl if it is not a class
	 * cast exception will be thrown.</b>
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(FileInfo entity) {
		
		hbCrudDAO.makePersistent((DefaultFileInfo)entity);
		
	}

	/**
	 * <b>File infor should be a FileInfoImpl if it is not a class
	 * cast exception will be thrown.</b>
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(FileInfo entity) {
		hbCrudDAO.makeTransient((DefaultFileInfo)entity);
	}


}
