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

package edu.ur.ir.user;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * Class to create users. 
 * 
 * @author Nathan Sarr
 *
 */
public class UserManager {
	
	private MessageDigestPasswordEncoder encoder = new ShaPasswordEncoder();
	
	/**
	 * Create a user with the specified values.
	 * 
	 * @param password
	 * @param userName
	 */
	public IrUser createUser(String password, String username)
	{
		IrUser user = new IrUser();
		user.setPassword(encoder.encodePassword(password, null));
		user.setUsername(username);
		user.setPasswordEncoding(encoder.getAlgorithm());
		return user;
	}
	
	public void updatePassword(String password, IrUser user)
	{
		user.setPassword(encoder.encodePassword(password, null));
		user.setPasswordEncoding(encoder.getAlgorithm());
	}
	
	/**
	 * Enocde the password using the encoder.  This can 
	 * be used to compare passwords to see if
	 * when encoded they are the same.
	 * 
	 * @param password
	 * @return
	 */
	public String encodePassword(String password)
	{
		return encoder.encodePassword(password, null);
	}

	/**
	 * Get the encoder for the user manager
	 * @return
	 */
	public MessageDigestPasswordEncoder getEncoder() {
		return encoder;
	}

	/**
	 * Set the encoder for the user manager.
	 * 
	 * @param encoder
	 */
	public void setEncoder(MessageDigestPasswordEncoder encoder) {
		this.encoder = encoder;
	}
	
	
	public static void main(String[] args)
	{
		UserManager manager = new UserManager();
		
		if( args.length < 2)
		{
			System.out.println("Usage java edu.ur.ir.security.UserManager <password> <username>");
		}
		IrUser user = manager.createUser(args[0], args[1]);
		System.out.println("password = " + user.getPassword());
		System.out.println("algorithm = " + user.getPasswordEncoding());
	}
}
