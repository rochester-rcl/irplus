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


/**
 * Represents an access control list (ACL) for a domain object.
 * 
 * <p>
 * An <code>Acl</code> represents all ACL entries for a given domain object. In
 * order to avoid needing references to the domain object itself, this
 * interface handles indirection between a domain object and an ACL object
 * identity.
 * </p>
 * 
 * 
 * This code has been modified to fit the need of the UR Research system.  See
 * spring security for more information where this code was originally created.
 *
 * @author Nathan Sarr
 */
public interface Acl extends Serializable {
    //~ Methods ========================================================================================================

    /**
     * Returns all of the entries represented by the present <code>Acl</code> (not parents).<p>This method is
     * typically used for administrative purposes.</p>
     *  <p>The order that entries appear in the array is unspecified. However, if implementations use
     * particular ordering logic in authorization decisions, the entries returned by this method <em>MUST</em> be
     * ordered in that manner.</p>
     *  <p>Do <em>NOT</em> use this method for making authorization decisions. Instead use {@link
     * #isGranted(Permission[], Sid[])}.</p>
     *  <p>This method must operate correctly even if the <code>Acl</code> only represents a subset of
     * <code>Sid</code>s. The caller is responsible for correctly handling the result if only a subset of
     * <code>Sid</code>s is represented.</p>
     *
     * @return the list of entries represented by the <code>Acl</code>
     */
    public List<AccessControlEntry> getEntries();

    /**
     * A domain object may have a parent for the purpose of ACL inheritance. If there is a parent, its ACL can
     * be accessed via this method. In turn, the parent's parent (grandparent) can be accessed and so on.<p>This
     * method solely represents the presence of a navigation hierarchy between the parent <code>Acl</code> and this
     * <code>Acl</code>. For actual inheritance to take place, the {@link #isEntriesInheriting()} must also be
     * <code>true</code>.</p>
     *  <p>This method must operate correctly even if the <code>Acl</code> only represents a subset of
     * <code>Sid</code>s. The caller is responsible for correctly handling the result if only a subset of
     * <code>Sid</code>s is represented.</p>
     *
     * @return the parent <code>Acl</code>
     */
    public Acl getParentAcl();

    /**
     * Indicates whether the ACL entries from the {@link #getParentAcl()} should flow down into the current
     * <code>Acl</code>.<p>The mere link between an <code>Acl</code> and a parent <code>Acl</code> on its own
     * is insufficient to cause ACL entries to inherit down. This is because a domain object may wish to have entirely
     * independent entries, but maintain the link with the parent for navigation purposes. Thus, this method denotes
     * whether or not the navigation relationship also extends to the actual inheritence of entries.</p>
     *
     * @return <code>true</code> if parent ACL entries inherit into the current <code>Acl</code>
     */
    public boolean isEntriesInheriting();

    /**
     * This is the actual authorization logic method, and must be used whenever ACL authorization decisions are
     * required.<p>A <code>Sid</code> is are presented, representing the security identity of the current
     * principal. In addition, a <code>Permission</code> is presented which will have one or more bits set
     * in order to indicate the permissions needed for an affirmative authorization decision. 
     *  <p>This method must operate correctly even if the <code>Acl</code> only represents a subset of
     * <code>Sid</code>s. The caller is responsible for correctly handling the result if only a subset of
     * <code>Sid</code>s is represented.</p>
     *
     * @param permission the permission required
     * @param sids the security identity for the principal
     * @param administrativeMode if <code>true</code> denotes the query is for administrative purposes and no logger or
     *        auditing (if supported by the implementation) should be undertaken
     *
     * @return <code>true</code> is authorization is granted
     *
     * @throws UnloadedSidException thrown if the <code>Acl</code> does not have details for one or more of the
     *         <code>Sid</code>s passed as arguments
     */
    public boolean isGranted(String permission, Sid sid, boolean administrativeMode);

 
}
