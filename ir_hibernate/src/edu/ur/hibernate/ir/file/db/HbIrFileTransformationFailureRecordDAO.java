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

package edu.ur.hibernate.ir.file.db;

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.file.IrFileTransformationFailureRecord;
import edu.ur.ir.file.IrFileTransformationFailureRecordDAO;

/**
 * This represents a transformation failure record.
 * 
 * @author Nathan Sarr
 *
 */
public class HbIrFileTransformationFailureRecordDAO implements IrFileTransformationFailureRecordDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -5726591852950750095L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<IrFileTransformationFailureRecord> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbIrFileTransformationFailureRecordDAO() {
		hbCrudDAO = new HbCrudDAO<IrFileTransformationFailureRecord>(IrFileTransformationFailureRecord.class);
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

	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("irFileTransformationFailureRecordCount"));
	}

	public List<IrFileTransformationFailureRecord> getAll() {
		return hbCrudDAO.getAll();
	}

	public IrFileTransformationFailureRecord getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(IrFileTransformationFailureRecord entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(IrFileTransformationFailureRecord entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
