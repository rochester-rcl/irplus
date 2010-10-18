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

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import edu.ur.file.db.UniqueFileSystemNameDAO;
import edu.ur.hibernate.HbHelper;

/**
 * Returns a sequence number that is always gauranteed to be unique
 * so long as the sequence is the only name generator used to give 
 * the names. 
 * 
 * @author Nathan Sarr
 *
 */
public class HbUniqueFileSystemNameDAO implements UniqueFileSystemNameDAO {
	
    SessionFactory sessionFactory;
    HibernateTemplate hibernateTemplate;
	
	/**
	 * Set the session factory.  
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
    {
       this.sessionFactory = sessionFactory;
       this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }
	

    
    /**
     * Gets a long value from the named query
     * 
     * @see edu.ur.file.db.FileInfoDAO#getNextUniqueFileName()
     */
    @SuppressWarnings("unchecked")
   public String getNextName() {
		List value = (List)hibernateTemplate.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.getNamedQuery("getNextFileName");
				return query.list();
			}
		});
		
		Object name = HbHelper.getUnique(value);
		
		return name.toString();
	}
}
