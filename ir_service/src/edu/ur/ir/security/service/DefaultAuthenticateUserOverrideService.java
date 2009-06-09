package edu.ur.ir.security.service;


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
	

	/**
	 * Authenticate the user and places them in the context.
	 * 
	 * @see edu.ur.ir.security.AuthenticateUserOverrideService#authenticateUser(edu.ur.ir.user.IrUser)
	 */
	public void authenticateUser(IrUser user) {
		

		// creates authentication object
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getRoles().toArray(new IrRole[0]));
        
        // Places the authentication object in security context
        SecurityContext ctx = SecurityContextHolder.getContext() ;
        ctx.setAuthentication(auth);
        SecurityContextHolder.setContext(ctx);
		
    }

}
