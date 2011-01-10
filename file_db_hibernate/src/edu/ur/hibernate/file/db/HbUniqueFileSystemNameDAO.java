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


import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.file.db.UniqueFileSystemNameDAO;

/**
 * Returns a sequence number that is always gauranteed to be unique
 * so long as the sequence is the only name generator used to give 
 * the names. 
 * 
 * @author Nathan Sarr
 *
 */
public class HbUniqueFileSystemNameDAO implements UniqueFileSystemNameDAO {
	
    /** eclipse generated id */
	private static final long serialVersionUID = 576526276230070841L;
	
	// session factory
	private SessionFactory sessionFactory;
	
	/**
	 * Set the session factory.  
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
    {
       this.sessionFactory = sessionFactory;
    }
    
    /**
     * Gets a long value from the named query
     * 
     * @see edu.ur.file.db.FileInfoDAO#getNextUniqueFileName()
     */
   public String getNextName() {
    	Query query = sessionFactory.getCurrentSession().getNamedQuery("getNextFileName");
    	return query.uniqueResult().toString();
	}
}
