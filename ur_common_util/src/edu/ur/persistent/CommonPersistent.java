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

package edu.ur.persistent;

import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;

/**
 * Abstract class for many simple type classes.
 * 
 * @author Nathan Sarr
 *
 */
public abstract class CommonPersistent extends BasePersistent
        implements DescriptionAware, NameAware
{

    /** eclipse generated serial id */
	private static final long serialVersionUID = -2896794034127562638L;

	/**  Name for the object. */
    protected String name;
    
    /**  Generic description. */
    protected String description;

    /**
     * Get the description 
     * 
     * @see edu.ur.simple.type.DescriptionAware#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set the description.
     *
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Get the name 
     * 
     * @see edu.ur.simple.type.NameAware#getName()
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name.  this performs a trim on the name 
     *
     * @param name
     */
    public void setName(String name)
    {
        this.name = name.trim();
    }
}
