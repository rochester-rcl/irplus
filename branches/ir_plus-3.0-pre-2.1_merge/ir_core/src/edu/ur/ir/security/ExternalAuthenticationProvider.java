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

package edu.ur.ir.security;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * This interface allows all external authentication methods to be
 * tried 
 * 
 * @author Nathan Sarr
 *
 */
public interface ExternalAuthenticationProvider extends Serializable{
	
	/**
	 * Attempt to authenticate with the givein information
	 * 
	 * @param authentication - authentication information
	 * @return authentication - with credentials and details
	 *
	 * @throws AuthenticationException - if the authentication fails
	 */
	public Authentication authenticate(Authentication authentication) throws AuthenticationException;

}
