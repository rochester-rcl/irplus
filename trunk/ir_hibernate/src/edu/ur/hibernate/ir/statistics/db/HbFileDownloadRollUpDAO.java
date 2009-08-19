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

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
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

	
	/**
	 * Roll up the counts.
	 * 
	 * @see edu.ur.ir.statistics.FileDownloadRollUpDAO#rollUpCounts()
	 */
	public Long rollUpCounts() {
		// TODO Auto-generated method stub
		return null;
	}

}
