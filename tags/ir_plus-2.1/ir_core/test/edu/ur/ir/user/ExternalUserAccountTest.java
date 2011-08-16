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

import org.testng.annotations.Test;

/**
 * Test the external user account test.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ExternalUserAccountTest {
	
	/**
	 * Test case sensitive external user account data
	 */
	public void testCaseSensitiveExternalUserAccountAccountTypeFirst()
	{
		ExternalAccountType externalAccountType = new ExternalAccountType("caseSensitiveAccount");
		externalAccountType.setUserNameCaseSensitive(true);
		
		ExternalUserAccount externalUserAccount = new ExternalUserAccount();
		externalUserAccount.setExternalAccountType(externalAccountType);
		externalUserAccount.setExternalUserAccountName("UserName");

		assert externalUserAccount.getExternalUserAccountName().equals("UserName") : "Account name should equal UserName but equals " + externalUserAccount.getExternalUserAccountName();	
	}
	
	/**
	 * Test case sensitive external user account data
	 */
	public void testCaseSensitiveExternalUserAccountUserNameFirst()
	{
		ExternalAccountType externalAccountType = new ExternalAccountType("caseSensitiveAccount");
		externalAccountType.setUserNameCaseSensitive(true);
		
		ExternalUserAccount externalUserAccount = new ExternalUserAccount();
		externalUserAccount.setExternalUserAccountName("UserName");
		externalUserAccount.setExternalAccountType(externalAccountType);
		assert externalUserAccount.getExternalUserAccountName().equals("UserName") : "Account name should equal UserName but equals " + externalUserAccount.getExternalUserAccountName();	
	}
	
	/**
	 * Test case sensitive external user account data
	 */
	public void testCaseSensitiveExternalUserAccountConstructor()
	{
		ExternalAccountType externalAccountType = new ExternalAccountType("caseSensitiveAccount");
		externalAccountType.setUserNameCaseSensitive(true);
		
		ExternalUserAccount externalUserAccount = new ExternalUserAccount(null, "UserName", externalAccountType);
		assert externalUserAccount.getExternalUserAccountName().equals("UserName") : "Account name should equal UserName but equals " + externalUserAccount.getExternalUserAccountName();	
	}
	
	/**
	 * Test case sensitive external user account data
	 */
	public void testCaseInSensitiveExternalUserAccountAccountTypeFirst()
	{
		ExternalAccountType externalAccountType = new ExternalAccountType("caseSensitiveAccount");
		
		ExternalUserAccount externalUserAccount = new ExternalUserAccount();
		externalUserAccount.setExternalAccountType(externalAccountType);
		externalUserAccount.setExternalUserAccountName("UserName");

		assert externalUserAccount.getExternalUserAccountName().equals("username") : "Account name should equal username but equals " + externalUserAccount.getExternalUserAccountName();	
	}
	
	/**
	 * Test case sensitive external user account data
	 */
	public void testCaseInSensitiveExternalUserAccountUserNameFirst()
	{
		ExternalAccountType externalAccountType = new ExternalAccountType("caseSensitiveAccount");
		
		ExternalUserAccount externalUserAccount = new ExternalUserAccount();
		externalUserAccount.setExternalUserAccountName("UserName");
		externalUserAccount.setExternalAccountType(externalAccountType);
		assert externalUserAccount.getExternalUserAccountName().equals("username") : "Account name should equal username but equals " + externalUserAccount.getExternalUserAccountName();	
	}
	
	/**
	 * Test case sensitive external user account data
	 */
	public void testCaseInSensitiveExternalUserAccountConstructor()
	{
		ExternalAccountType externalAccountType = new ExternalAccountType("caseSensitiveAccount");
		
		ExternalUserAccount externalUserAccount = new ExternalUserAccount(null, "UserName", externalAccountType);
		assert externalUserAccount.getExternalUserAccountName().equals("username") : "Account name should equal username but equals " + externalUserAccount.getExternalUserAccountName();	
	}
	
	
	

}
