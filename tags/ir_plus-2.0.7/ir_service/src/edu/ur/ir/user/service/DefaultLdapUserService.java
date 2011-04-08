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

import java.util.Collection;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import edu.ur.ir.user.ExternalAccountType;
import edu.ur.ir.user.ExternalAccountTypeService;
import edu.ur.ir.user.ExternalUserAccount;
import edu.ur.ir.user.LdapUserService;
import edu.ur.ir.user.UserService;

/**
 * Basic services for finding Ldap users and loading their information
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultLdapUserService implements LdapUserService  {

	/** eclipse generated id */
	private static final long serialVersionUID = 1059520968635339038L;

	/** user search  */
	private FilterBasedLdapUserSearch userSearch;
	
	/** loads user data from local db*/
	private UserService userService;
	
	/** the external account type   */
	private ExternalAccountTypeService externalAccountTypeService;
	
	/** the external ldap account type this search is for  */
	private String externalAccountTypeName;
	
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

	
	/**
	 * This maps a user from the context information.
	 * This method ignores the Granted Authority[] 
	 * @see org.springframework.security.userdetails.ldap.UserDetailsContextMapper#mapUserFromContext(org.springframework.ldap.core.DirContextOperations, java.lang.String, org.springframework.security.GrantedAuthority[])
	 */
	public UserDetails mapUserFromContext(DirContextOperations arg0,
			String username, 
			Collection<GrantedAuthority> arg2) 
	{
		ExternalAccountType externalAccountType = externalAccountTypeService.get(externalAccountTypeName);
		ExternalUserAccount externalAccount = userService.getByExternalUserNameAccountType(username, externalAccountType);
		if( externalAccount != null )
		{
			return externalAccount.getUser();
		}
		else
		{
			return null;
		}
		
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

	public ExternalAccountTypeService getExternalAccountTypeService() {
		return externalAccountTypeService;
	}

	public void setExternalAccountTypeService(
			ExternalAccountTypeService externalAccountTypeService) {
		this.externalAccountTypeService = externalAccountTypeService;
	}

	public String getExternalAccountTypeName() {
		return externalAccountTypeName;
	}

	public void setExternalAccountTypeName(String externalAccountTypeName) {
		this.externalAccountTypeName = externalAccountTypeName;
	}

}
