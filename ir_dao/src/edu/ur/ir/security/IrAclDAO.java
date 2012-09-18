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

package edu.ur.ir.security;


import java.util.List;
import java.util.Set;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;

/**
 * Interface for perisisting Access control lists.
 * 
 * @author Nathan Sarr
 *
 */
public interface IrAclDAO extends CountableDAO, CrudDAO<IrAcl>{
	
    /**
     * Obtains the ACL that apply to the specified domain instance.
     *
     * @param objectId object id for which ACL information is required (never <code>null</code>)
     * @param className class name for which ACL information is required (never <code>null</code>)
     *
     * @return the ACL that apply, or <code>null</code> if no ACLs apply to the specified domain instance
     */
    public IrAcl getAcl(Long objectId, String className);
    
    /**
     * Obtains the ACLs that apply to the specified domain instance, but only including those ACLs which have
     * been granted to the presented <code>Authentication</code> object
     *
     * @param objectId object id for which ACL information is required (never <code>null</code>)
     * @param className class name for which ACL information is required (never <code>null</code>)
     * @param the sid for which ACL information should be filtered (never <code>null</code>)
     *
     * @return only the ACL applying to the domain instance that have been granted to the principal (or
     *         <code>null</code>) if no such ACL is found
     */
    public IrAcl getAcl(Long objectId, String className, Sid sid);
    
    /**
     * Determines if the secure id has permission for the specified domain object.  This implementation
     * uses direct database selects and should be much faster than using the object graph.
     *
     * @param objectId object id - id of the object 
     * @param className class name - class name of the object
     * @param the sid (secure id) - the sid to check to see if they have the permission
     * @param the permission - the permission to check for
     * 
     * @return the number of times this user has the specified permission - 0 indicates the sid does
     * not have the permission
     */
    public Long hasPermission(Long objectId, String className, Sid sid, String permission);
    
    /**
     * Return all secure id's who have the identified permission
     *
     * @param objectId object id - id of the object 
     * @param className class name - class name of the object
     * @param the permission - the permission to check for
     * 
     * @return all sids who have the specified permission for the specified object
     */
    public Set<Sid> getSidsWithPermissionForObject(Long objectId, String className, String permission);
	
	/**
	 * @param sid - Secure id to get acls for
	 * 
	 * @return - return the list of acls found
	 */
	public List<IrAcl> getAllAclsForSid(Sid sid);
	
	/**
	 * Return all secure id's who have the identified permission and are within
	 * the specified set of sids.
	 * 
	 * @param objectId - object they must have permissions on
	 * @param className - object class
	 * @param permission - permission the sid must have
	 * @param specificSids - set of sids to check
	 * 
	 * @return all sids who meet the specified criteria
	 */
	public Set<Sid> getSidsWithPermissionForObject(Long objectId, String className,
			String permission, List<Sid> specificSids);

}
