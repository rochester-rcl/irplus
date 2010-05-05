package edu.ur.ir.security;


import java.io.Serializable;

import edu.ur.ir.user.IrUser;

/**
 * <h3>********** Warning this should only be called in very rare situations ********</h3>
 * 
 * This allows a user to be verified without true authentication -
 * this can be used to allow an administrator to login as the specified user for account
 * problems and diagnostics.
 * 
 * @author Nathan Sarr
 *
 */
public interface AuthenticateUserOverrideService extends Serializable{
	
	/**
	 * Authenticates the user and places them in the context.
	 * 
	 * @param user - user to authenticate as and to place in the context
	 */
	public void authenticateUser(IrUser user);

}
