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
import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
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
	public List<FileDownloadInfo> getAllDownloadInfoIgnored() {
		return (List<FileDownloadInfo>)hbCrudDAO.getHibernateTemplate().findByNamedQuery("getFileDownloadsIgnored");
	}
	
}
