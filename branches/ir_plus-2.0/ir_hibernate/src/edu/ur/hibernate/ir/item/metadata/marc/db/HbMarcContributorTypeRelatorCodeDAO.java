/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.hibernate.ir.item.metadata.marc.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.item.metadata.marc.MarcContributorTypeRelatorCode;
import edu.ur.ir.item.metadata.marc.MarcContributorTypeRelatorCodeDAO;


/**
 * Hibernate impelmentation of mapping marc contributor type relator codes
 * 
 * @author Nathan Sarr
 *
 */
public class HbMarcContributorTypeRelatorCodeDAO implements MarcContributorTypeRelatorCodeDAO{
	
	
	// eclipse generated id
	private static final long serialVersionUID = -4732942685566691684L;
	
	
	//  Helper for persisting information using hibernate.  	
	private final HbCrudDAO<MarcContributorTypeRelatorCode> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbMarcContributorTypeRelatorCodeDAO() {
		hbCrudDAO = new HbCrudDAO<MarcContributorTypeRelatorCode>(MarcContributorTypeRelatorCode.class);
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


	public MarcContributorTypeRelatorCode getByContributorType(
			Long contributorId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcContributorTypeRelatorCodeByContributorTypeId");
		q.setParameter("contributorId", contributorId);
		return (MarcContributorTypeRelatorCode)q.uniqueResult();
	}

	public MarcContributorTypeRelatorCode getByRelatorCode(Long relatorCodeId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcContributorTypeRelatorCodeByRelatorCodeId");
		q.setParameter("relatorCodeId", relatorCodeId);
		return (MarcContributorTypeRelatorCode)q.uniqueResult();
	}
	
	/**
	 * Returns the list of contributor types that have the specified relator code.
	 * 
	 * @param relatorCode - relator code 
	 * @return the list of records found with the contributor type relator codes.
	 */
	@SuppressWarnings("unchecked")
	public List<MarcContributorTypeRelatorCode> getByRelatorCode(String relatorCode)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcContributorTypeRelatorCodeByRelatorCode");
		q.setParameter("relatorCode",  relatorCode);
		return q.list();
	}

	public List<MarcContributorTypeRelatorCode> getAll() {
		return hbCrudDAO.getAll();
	}

	public MarcContributorTypeRelatorCode getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(MarcContributorTypeRelatorCode entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(MarcContributorTypeRelatorCode entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
