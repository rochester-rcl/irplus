/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.user;



import java.util.List;

import edu.ur.ir.user.ExternalUserAccount;
import edu.ur.dao.CrudDAO;

/**
 * Data access interface for an external user account.
 * 
 * @author Nathan Sarr
 *
 */
public interface ExternalUserAccountDAO extends CrudDAO<ExternalUserAccount>
{
	/**
	 * Get all external user accounts for a given user name.
	 * 
	 * @param externalUserName - the external user name used
	 * @return the list of external accounts found with the given external user name.
	 */
	public List<ExternalUserAccount> getByExternalUserName(String externalUserName);
	
	/**
	 * Get the external user name account type
	 * 
	 * @param externalUserName - external user name information
	 * @param externalAccountType - external account type
	 * 
	 * @return the external user account found or null if not found.
	 */
	public ExternalUserAccount getByExternalUserNameAccountType(String externalUserName, ExternalAccountType externalAccountType);
}
