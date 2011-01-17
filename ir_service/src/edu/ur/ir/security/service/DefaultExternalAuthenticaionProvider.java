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


package edu.ur.ir.security.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.authentication.AuthenticationProvider;

import edu.ur.ir.security.ExternalAuthenticationProvider;

/**
 * Default external authentication provider.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultExternalAuthenticaionProvider implements ExternalAuthenticationProvider{

	/** eclipse generated id  */
	private static final long serialVersionUID = -8552847690615321354L;

	/** list of authentication providers.  */
	private List<AuthenticationProvider> authenticationProviders = new LinkedList<AuthenticationProvider>();
	
    protected transient MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	
	/**
	 * Authenticate against the given external providers.
	 * 
	 * @see edu.ur.ir.security.ExternalAuthenticationProvider#authenticate(org.springframework.security.Authentication)
	 */
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {

		AuthenticationException ae = null;
        for( AuthenticationProvider provider : authenticationProviders)
        {
        	try
        	{
        		// return out of loop as soon as authentication occurs
        		if( provider.supports(authentication.getClass()))
        		{
        	        Authentication auth = provider.authenticate(authentication);
        	        return auth;
        		}
        	}
        	catch(AuthenticationException exception)
        	{
        		ae = exception;
        	}
        }
        
        if( ae != null )
        {
        	throw ae;
        }
        else
        {
        	 throw new BadCredentialsException(messages.getMessage("ProviderManager.providerNotFound",
             authentication.getClass().getName()));
        }
       
	}

	/**
	 * Get the list of authentication providers
	 * @return - unmodifiable linked list
	 */
	public List<AuthenticationProvider> getAuthenticationProviders() {
		return Collections.unmodifiableList(authenticationProviders);
	}

	/**
	 * Set the list of authentication providers.
	 * 
	 * @param authenticationProviders
	 */
	public void setAuthenticationProviders(
			List<AuthenticationProvider> authenticationProviders) {
		this.authenticationProviders = authenticationProviders;
	}

}
