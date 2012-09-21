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

package edu.ur.hibernate.ir.handle.db;

import java.math.BigInteger;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.ir.handle.UniqueHandleNameGenerator;

/**
 * Returns the next unique handle name from a sequence.
 * 
 * @author Nathan Sarr
 *
 */
public class HbUniqueHandleNameDAO implements UniqueHandleNameGenerator {
	
    /** eclipse generated id */
	private static final long serialVersionUID = -4898778212292620797L;
	
    /** spring session factory */
    SessionFactory sessionFactory;
	
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
     * Gets the next unique handle name
     * 
     * @see edu.ur.ir.handle.UniqueHandleNameGenerator#nextName()
     */
	public String nextName() 
    {
	    Query query = sessionFactory.getCurrentSession().getNamedQuery("getUniqueHandleName");
		return ((BigInteger)query.uniqueResult()).toString();
	}

}
