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

package edu.ur.ir.repository;

import edu.ur.ir.user.IrUser;

/**
 * Exception thrown when a user has not accepted a repository license.
 * 
 * @author Nathan Sarr
 *
 */
public class RepositoryLicenseNotAcceptedException extends Exception{

	/** eclipse generated id  */
	private static final long serialVersionUID = -7213561636287996700L;
	
	/**
	 * Default constructor
	 * 
	 * @param user - user who has not accepted the license
	 * @param repo - repository the license should be accepted for
	 */
	public RepositoryLicenseNotAcceptedException(IrUser user, Repository repo)
	{
		super("User " + user + " has not accepted license for repostiory " + repo);
	}

}
