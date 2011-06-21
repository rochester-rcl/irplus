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

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * Represents an individual permission assignment within an ACL(Access control lists). 
 * 
 * The basis of this code is from Acegi Security written by Ben Alex
 * 
 */
public interface AccessControlEntry {

    /**
     * Get the parent ACL (Access Control List)
     * 
     * @return
     */
    public Acl getAcl();

    /**
     * Obtains an identifier that represents this ACE.
     *
     * @return the identifier, or <code>null</code> if unsaved
     */
    public Serializable getId();

    /**
     * Get all permissions as the atomic
     * permissions for this ACE.
     * 
     * @return
     */
    public List<String> getPermissionNames();

    /**
     * Get all permissions as the atomic
     * permissions for this ACE.
     * 
     * @return
     */
    public Set<IrClassTypePermission> getIrClassTypePermissions();
    
    /**
     * Get the secure id for this access control entry.
     * 
     * @return
     */
    public Sid getSid();

    /**
     * Returns true if this Access control entry allows the operation(s).
     * 
     * @return
     */
    public boolean isGranting(String permission, Sid sid);
}
