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


package edu.ur.ir.repository.service;

import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.repository.VersionedFileUnLockStrategy;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;

/**
 * Implementation of the unlock strategy.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultVersionedFileUnlockStrategy implements VersionedFileUnLockStrategy{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -7057217108521696958L;
	
	/** Service for dealing with access control lists*/
	private SecurityService securityService;


	/**
	 * Returns true if the user can unlock the file.
	 * 
	 * @param file - versioned file the user would like to unlock
	 * @param user - the user who wishes to unlock the file
	 * 
	 * @return true if the user can unlock the file. 
	 */
	public boolean canUnLockFile(VersionedFile file, IrUser user) {
		
		
		
		// admins, owners, and collaborators can lock the file
		if( file.getLockedBy().equals(user) || 
				user.getRole(IrRole.ADMIN_ROLE) != null || 
				user.equals(file.getOwner()) )
		{
			return true;
		}
		else
		{
			// check for manage permissions
			IrAcl acl = securityService.getAcl(file, user);
			if( acl != null )
			{
			    if( acl.isGranted("MANAGE", user, false))
			    {
				    return true;
			    }
			}
		}

		return false;
		
	}
	
	/**
	 * Get the security service.
	 * 
	 * @return the security service
	 */
	public SecurityService getSecurityService() {
		return securityService;
	}

	/**
	 * Set the security service.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}


}
