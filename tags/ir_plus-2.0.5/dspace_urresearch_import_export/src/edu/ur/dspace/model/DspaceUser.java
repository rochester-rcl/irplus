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

package edu.ur.dspace.model;

import java.util.Date;

/**
 * Record for a dspace user.
 * 
 * @author Nathan Sarr
 *
 */
public class DspaceUser {
	
	/** dspace id for the user  */
	public Long dspaceId;
	
	/** users email */
	public String email;
	
	/** password for the user  */
	public String password;
	
	/** first name for the user */
	public String firstName;
	
	/** last name for the user */
	public String lastName;
	
	/** indicates if the user registered themselves */
	public boolean selfRegistered = false;
	
	/** phone number for the user*/
	public String phoneNumber;
	
	/** date the user registered with the system  */
	public Date registrationDate;
	
	/** users net id */
	public String netId;
	
	/** determine if the user can log in */
	public boolean canLogIn = true;
	
	/** indicates the user is a dspace administrator */
	public boolean isAdmin = false;
	
	/** indicates the user has submit privileges to at least 1 collection */
	public boolean canSubmit;
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ email = ");
		sb.append(email);
		sb.append(" user idd = ");
		sb.append(dspaceId);
		sb.append(" firstName = ");
		sb.append(firstName);
		sb.append(" lastName = " );
		sb.append(lastName);
		sb.append(" self registered = ");
		sb.append(selfRegistered);
		sb.append(" phoneNumber = ");
		sb.append(phoneNumber);
		sb.append(" registration date = ");
		sb.append(registrationDate);
		sb.append(" net id = ");
		sb.append(netId);
		sb.append("]");
		return sb.toString();
	}
	
	

}
