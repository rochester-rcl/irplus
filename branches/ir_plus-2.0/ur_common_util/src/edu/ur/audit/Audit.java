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

package edu.ur.audit;

import edu.ur.security.PersistentUser;

/**
 * Generic audit record.
 *
 * @author Nathan Sarr
 *
 */
public interface Audit
{

    /**
     * Message for the audit record.
     *
     * @return the message
     */
    String getMessage();

    /**
     * The class name that the change was made.
     *
     * @return string representing the class name
     */
    String getDomainClass();

    /**
     * The id of the domain class.
     *
     * @return id of the domain class
     */
    Long getId();

    /**
     * Get the user who made the change.
     *
     * @return the persistent user
     */
    PersistentUser getPersistentUser();
}
