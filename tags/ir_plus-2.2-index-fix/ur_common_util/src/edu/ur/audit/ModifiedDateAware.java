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

import java.util.Date;

/**
 * Represents an object that is aware that it has a 
 * modified date
 * 
 * @author Nathan Sarr
 *
 */
public interface ModifiedDateAware
{

    /**
     * Get the date the last object was modified.
     *
     * @return
     */
    Date getModifiedDate();

    /**
     * Set the date the object was last modified.
     *
     * @param d
     */
    void setModifiedDate(Date d);
}
