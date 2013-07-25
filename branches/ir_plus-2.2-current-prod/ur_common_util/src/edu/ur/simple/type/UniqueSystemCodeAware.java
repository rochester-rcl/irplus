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

package edu.ur.simple.type;

/**
 * Represents an interface where there is a system code for
 * internal usage of the system.  This allows developers 
 * to identify particular instances of data.  this also frees 
 * developers from relying on the name of the data for internationalization
 * purposes. 
 * 
 * @author nathans
 *
 */
public interface UniqueSystemCodeAware
{

    /**
     * Returns the system code for the given item.
     *
     * @return string representing the uniqu system code
     */
     String getUniqueSystemCode();
}
