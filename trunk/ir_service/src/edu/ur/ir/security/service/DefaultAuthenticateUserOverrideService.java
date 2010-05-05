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


import org.apache.log4j.Logger;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import edu.ur.ir.security.AuthenticateUserOverrideService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;

/**
 * <h3>********** Warning this should only be called in very rare situations ********</h3>
 * 
 * This allows a user to be verified without the need for the password - only the username
 * this can be used to allow an administrator to login as the specified user.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultAuthenticateUserOverrideService implements AuthenticateUserOverrideService{
	

	/** eclipse generated id */
	private static final long serialVersionUID = -5262888575707357395L;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultAuthenticateUserOverrideService.class);
	
	/**
	 * Authenticate the user and places them in the context.
	 * 
	 * @see edu.ur.ir.security.AuthenticateUserOverrideService#authenticateUser(edu.ur.ir.user.IrUser)
	 */
	public void authenticateUser(IrUser user) 
	{
		log.debug("Authenticating user " + user);
		
		// creates authentication object
        Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getRoles().toArray(new IrRole[0]));
        
        // Places the authentication object in security context
        SecurityContext ctx = SecurityContextHolder.getContext() ;
        ctx.setAuthentication(auth);
        SecurityContextHolder.setContext(ctx);
		
    }

}
