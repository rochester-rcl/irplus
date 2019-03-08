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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.repository.VersionedFileLockStrategy;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;

/**
 * Allows a user to lock a file.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultVersionedFileLockStrategy implements VersionedFileLockStrategy{
	
	/**  Get the logger for this class */
	private static final Logger log = LogManager.getLogger(DefaultVersionedFileLockStrategy.class);
	
	/** Manager for access control lists */
	private SecurityService securityService;
	
	
	/**
	 * Returns true if the user can lock the file.  This does not look at if the file
	 * is already locked.  It only tells if the user would be able to lock the file.
	 * 
	 * @return true if the user can lock the specified version file 
	 * (admins, owners and collaborators can lock a file in this strategy)
	 */
	public boolean canLockFile(VersionedFile file, IrUser user) {
		
		if( log.isDebugEnabled() )
		{
			log.debug("can lock file");
		    log.debug("user.getRole(IrRole.ADMIN_ROLE)!= null : " + user.getRole(IrRole.ADMIN_ROLE)!= null );
		    log.debug("user.equals(file.getOwner()) : " + user.equals(file.getOwner()) );
		}
		
		// admins and owners can lock the file
		if( user.getRole(IrRole.ADMIN_ROLE) != null || user.equals(file.getOwner()) )
		{
			return true;
		}
		else
		{
			IrAcl acl = securityService.getAcl(file, user);
			if(log.isDebugEnabled())
			{
				log.debug(" acl = " + acl + " for user " + user);
			}
			if( acl != null )
			{
				if( log.isDebugEnabled())
				{
				    log.debug( "acl is not null");
				    log.debug("VIEW = " +  acl.isGranted("VIEW", user, false) );
				    log.debug("MANAGE = " +  acl.isGranted("MANAGE", user, false) );
				    log.debug("EDIT = " +  acl.isGranted("EDIT", user, false) );
				    log.debug("SHARE = " +  acl.isGranted("SHARE", user, false) );
				}
			    if( acl.isGranted("EDIT", user, false) || acl.isGranted("MANAGE", user, false) )
			    {
			    	log.debug("true");
				    return true;
			    }
			}
		}

		return false;
	}


	public SecurityService getSecurityService() {
		return securityService;
	}


	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

}
