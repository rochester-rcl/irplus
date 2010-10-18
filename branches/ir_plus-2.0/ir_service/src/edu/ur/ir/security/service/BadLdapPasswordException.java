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

import org.springframework.security.authentication.AccountStatusException;

/**
 * 
 * This exception prevents further authentication providers from being tried.
 * 
 * @author Nathan Sarr
 *
 */
public class BadLdapPasswordException extends AccountStatusException {
   
	//~ Constructors ===================================================================================================

    /** eclipse generated id */
	private static final long serialVersionUID = -637637144213460152L;

	/**
     * Constructs a <code>LockedException</code> with the specified message.
     *
     * @param msg the detail message.
     */
    public BadLdapPasswordException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>LockedException</code> with the specified message and
     * root cause.
     *
     * @param msg the detail message.
     * @param t root cause
     */
    public BadLdapPasswordException(String msg, Throwable t) {
        super(msg, t);
    }

    public BadLdapPasswordException(String msg, Object extraInformation) {
        super(msg, extraInformation);
    }
}