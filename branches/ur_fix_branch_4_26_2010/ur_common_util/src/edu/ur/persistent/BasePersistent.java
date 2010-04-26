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

import java.io.Serializable;

/**
 * Simple implementation of persistance methods
 * to extend for persistent classes.
 * 
 * @author Nathan Sarr
 *
 */
public abstract class BasePersistent implements LongPersistentId,
        PersistentVersioned, Serializable
{

    /** Eclipse generated id */
	private static final long serialVersionUID = -2154864965249341364L;

	/**
     *  The id of the object
     */
    protected Long id;
    /**
     * The version of the object.
     */
    protected int version;

    /* (non-Javadoc)
     * @see edu.ur.persistent.LongPersistentId#getId()
     */
    public Long getId()
    {
        return id;
    }

    /**
     * Set the id of this persistent class.
     *
     * @param id
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see edu.ur.persistent.PersistentVersioned#getVersion()
     */
    public int getVersion()
    {
        return version;
    }

    /**
     * Set the persistent version of this class.
     * @param version
     */
    public void setVersion(int version)
    {
        this.version = version;
    }
}
