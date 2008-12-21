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


package edu.ur.ir.user.service;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;

import edu.ur.ir.user.LdapUserService;
import edu.ur.ir.user.UserService;

/**
 * Basic services for finding Ldap users and loading their information
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultLdapUserService implements LdapUserService  {

	/** user search  */
	private FilterBasedLdapUserSearch userSearch;
	
	/** loads user data from local db*/
	private UserService userService;
	
	public DirContextOperations searchForUser(String ldapUserName) {
		DirContextOperations dirContextOperations = null;
		try{
		     dirContextOperations = userSearch.searchForUser(ldapUserName);
		}
		catch(UsernameNotFoundException unfe)
		{
			 dirContextOperations = null;
		}
		
		return dirContextOperations;
	}

	public FilterBasedLdapUserSearch getUserSearch() {
		return userSearch;
	}

	public void setUserSearch(FilterBasedLdapUserSearch userSearch) {
		this.userSearch = userSearch;
	}

	
	public UserDetails mapUserFromContext(DirContextOperations arg0,
			String username, GrantedAuthority[] arg2) {
		return userService.getUserByLdapUserName(username);
	}

	
	public void mapUserToContext(UserDetails arg0, DirContextAdapter arg1) {
		// do nothing
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}



}
