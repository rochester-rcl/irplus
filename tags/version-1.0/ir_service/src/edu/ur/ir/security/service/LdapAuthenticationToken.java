package edu.ur.ir.security.service;

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


import org.springframework.security.GrantedAuthority;
import org.springframework.security.providers.AbstractAuthenticationToken;

/**
 * NOTE: Based on UserAuthenticationToken in spring security.
 * 
 * This represents an Ldap authentication token.
 * 
 * 
 * @author Nathan Sarr
 *
 */
public class LdapAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 1L;
    private Object credentials;
    private Object principal;


    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link
     * #isAuthenticated()} will return <code>false</code>.
     *
     */
    public LdapAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or <code>AuthenticationProvider</code>
     * implementations that are satisfied with producing a trusted (ie {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     *
     * @param principal
     * @param credentials
     * @param authorities
     */
    public LdapAuthenticationToken(Object principal, Object credentials, GrantedAuthority[] authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }

    //~ Methods ========================================================================================================

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                "Cannot set this token to trusted - use constructor containing GrantedAuthority[]s instead");
        }

        super.setAuthenticated(false);
    }
}

