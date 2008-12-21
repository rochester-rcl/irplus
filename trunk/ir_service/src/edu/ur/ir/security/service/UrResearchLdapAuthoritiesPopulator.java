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


package edu.ur.ir.security.service;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.ldap.LdapAuthoritiesPopulator;
import org.springframework.ldap.core.DirContextOperations;
     

/**
 * Populator for ldap - This class retrieves data out of the local database and populates 
 * the user information. Currently this class does nothing but return null.
 * 
 * @author Nathan Sarr
 *
 */
public class UrResearchLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

	
	/* (non-Javadoc)
	 * @see org.springframework.security.ldap.LdapAuthoritiesPopulator#getGrantedAuthorities(org.springframework.ldap.core.DirContextOperations, java.lang.String)
	 */
	public GrantedAuthority[]getGrantedAuthorities(DirContextOperations userData,
			String username)
	{
		return null;
	}


}
