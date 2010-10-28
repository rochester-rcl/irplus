/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.groupspace;

import java.util.Collections;
import java.util.Set;

import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;
import edu.ur.simple.type.NameAware;


/**
 * Represents a space for a group of people to 
 * collaborate on a shared folder structure
 * 
 * @author Nathan Sarr
 *
 */
public class GroupSpace extends BasePersistent implements NameAware {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -6440204761384913224L;

	/**  Name of the group space */
	private String name;
	
	/** root folder for the group space  */
	private GroupFolder rootFolder;
	
	/** set of managers for this group space */
	private Set<IrUser> managers;
	
	/** Owner of the group space */
	private IrUser owner;

	/**  Package protected workspace  */
	GroupSpace(){}
	
    /**
     * Create a group space with the given name.
     * 
     * @param name - name of the group space
     * @param owner - owner of the group space
     */
    public GroupSpace(String name, IrUser owner)
    {
    	setName(name);
    	rootFolder = new GroupFolder(this, name, owner);
    }
    
	/**
	 * Get the name of the workspace
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the group.
	 * 
	 * @param name
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the root folder of the group.
	 * 
	 * @return root folder of the group
	 */
	public GroupFolder getRootFolder() {
		return rootFolder;
	}

	/**
	 * Set the root folder for the group.
	 * 
	 * @param rootFolder
	 */
	void setRootFolder(GroupFolder rootFolder) {
		this.rootFolder = rootFolder;
	}

	/**
	 * Get the managers for the group. 
	 * 
	 * @return Unmodifiable set of managers
	 */
	public Set<IrUser> getManagers() {
		return Collections.unmodifiableSet(managers);
	}

	/**
	 * Set of managers of the group.
	 * 
	 * @param managers
	 */
	void setManagers(Set<IrUser> managers) {
		this.managers = managers;
	}
	
	/**
	 * Owner of the group space.
	 * 
	 * @return
	 */
	public IrUser getOwner() {
		return owner;
	}

	/**
	 * Owner of the group space.
	 * 
	 * @param owner
	 */
	void setOwner(IrUser owner) {
		this.owner = owner;
	}

}
