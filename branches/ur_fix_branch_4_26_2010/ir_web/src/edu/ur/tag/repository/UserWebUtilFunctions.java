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


package edu.ur.tag.repository;

import java.util.StringTokenizer;

import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;

import edu.ur.ir.user.IrUser;

public class UserWebUtilFunctions {


	/**
	 * Executes the body if the user has the role.  This tag
     * optionally takes a condition.  If there are multiple roles allowed
     * this defaults to OR if the condition must be AND then it must
     * explicitly be set to AND.  This checks the user currently in the
     * session.  This does not take a user as an option - use the 
     * CheckHasUerRole function for this.
	 * 
	 * @param roles - roles to check 
	 * @param condition - AND / OR
	 * @return true if the user has the roles with the specified conditions
	 */
	public static boolean userHasRole(String roles, String condition)
	{
	     boolean hasConditions = false;	
         
         IrUser user = null;
         
         final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 		
         if( auth != null) {
 			 if(auth.getPrincipal() instanceof UserDetails) {
 				 user = (IrUser)auth.getPrincipal();
 			 }
         }
 		
         if( user != null )
         {
        	 hasConditions = userHasRoleConditions(user, roles, condition);
         }
         return hasConditions;
	}
	
	/**
	 * Determines if the user has the desired role conditions.
	 * 
	 * @return true if the role conditions are met.
	 */
	private static boolean userHasRoleConditions(IrUser user, String roles, String condition)
	{
		boolean result = false;
		if( (user != null) && (roles != null) ) {
			StringTokenizer tokens = new StringTokenizer(roles, ",");
			int i = 0;
			while (tokens.hasMoreTokens()) {
				String role = tokens.nextToken().trim();
				if (i == 0) {
					result = user.hasRole(role);
				} else {
					if (condition != null && condition.equalsIgnoreCase("AND")) {
						result = result && user.hasRole(role);
					} else {
						result = result || user.hasRole(role);
					}
				}
				i++;
			}
		}
		return result;
	}
	
	/**
	 * Check to see if the user has the specified role.
	 * 
	 * @param user - user to check
	 * @param roles - roles the user should have
	 * 
	 * @param condition - AND or OR  If there are multiple roles allowed
     * this defaults to OR if the condition must be AND then it must
     * explicitly be set to AND
     * 
	 * @return
	 */
	public static boolean checkUserHasRole(IrUser user, String roles, String condition)
	{
	     boolean hasConditions = false;	
        
         if( user != null )
         {
        	 hasConditions = userHasRoleConditions(user, roles, condition);
         }
         return hasConditions;
	}
}

