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


package edu.ur.ir.user.service;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import edu.ur.ir.user.ExternalAccountType;
import edu.ur.ir.user.ExternalAccountTypeService;
import edu.ur.ir.user.ExternalUserAccount;
import edu.ur.ir.user.LdapUserService;
import edu.ur.ir.user.UserService;

/**
 * This class always returns a null dirContext operations.  This can be
 * used when you don't have a system user or when you cannot anonymously 
 * bind to the ldap directory.
 * 
 * @author Nathan Sarr
 *
 */
public class FakeLdapUserService implements LdapUserService  {

	/** loads user data from local db*/
	private UserService userService;
	
	/** the external account type   */
	private ExternalAccountTypeService externalAccountTypeService;
	
	/** the external ldap account type this search is for  */
	private String externalAccountTypeName;
	
	/**
	 * This actually does not search but always returns null.
	 * 
	 * @see edu.ur.ir.user.LdapUserService#searchForUser(java.lang.String)
	 */
	public DirContextOperations searchForUser(String ldapUserName) {
		return null;
	}

	public UserDetails mapUserFromContext(DirContextOperations arg0,
			String username, GrantedAuthority[] arg2) {
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
