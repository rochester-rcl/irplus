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

package edu.ur.hibernate.ir.person.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.person.PersonNameTitle;
import edu.ur.ir.person.PersonNameTitleDAO;

/**
 * Hibernate persistance of the person name title.
 * 
 * @author Nathan Sarr
 *
 */
public class HbPersonNameTitleDAO  extends 
    HbCrudDAO<PersonNameTitle> implements PersonNameTitleDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -1869309053580253259L;

	/**
	 * Default Constructor
	 */
	public HbPersonNameTitleDAO() {
		super(PersonNameTitle.class);
	}

	/**
	 * Get a count of the contributors in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hibernateTemplate.findByNamedQuery("personNameTitleCount"));
	}

	/**
	 * Returns all person name title with a person name id like the specified value.
	 * 
	 * @param personNameId
	 * @param startRecord
	 * @param numRecords
	 */
	@SuppressWarnings("unchecked")
	public List<PersonNameTitle> findPersonNameTitleForPersonNameID(final int personNameId, final int startRecord, final int numRecords)
	{
		
	  	return (List<PersonNameTitle>) hibernateTemplate.executeFind( new HibernateCallback() 
		{
	         public Object doInHibernate(Session session)
	         {
	     	    Query query = session.getNamedQuery("findPersonNameTitleForPersonNameID");
	     		query.setFirstResult(startRecord);
	     		query.setMaxResults(numRecords);
	     		query.setInteger(0, personNameId);
	     		query.setFetchSize(numRecords);
                return query.list();
	         }
		});
	}

}
