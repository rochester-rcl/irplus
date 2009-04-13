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

package edu.ur.exception;

/**
 * Indicates a duplicate name has occured in the system
 * where duplicate names are not allowed.
 * 
 * @author Nathan Sarr
 *
 */
public class DuplicateNameException extends Exception
{

    /** Name that was duplicated */
    private String name;
    
    /** Indicates that a duplicate name has been used when it should not be */
    private static final long serialVersionUID = 8295922423572725111L;

    /**
     * Message and the name that was duplicated.
     *
     * @param message
     * @param name
     */
    public DuplicateNameException(String message, String name)
    {
        super(message);
        this.name = name;
    }

    /**
     * Name that was duplicated
     *
     * @return
     */
    public String getName()
    {
        return name;
    }
}
