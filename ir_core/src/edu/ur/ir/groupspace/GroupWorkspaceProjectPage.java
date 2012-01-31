/**  
   Copyright 2008-2012 University of Rochester

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

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.ur.persistent.BasePersistent;

/**
 * Represents the grouping of information for a group space that can be shown 
 * to the public.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceProjectPage extends BasePersistent {
	
	// eclipse generated id
	private static final long serialVersionUID = -7955753514253869132L;

	// group workspace for this project page
	private GroupWorkspace groupWorkspace;

	//description of the project
	private String description;
	
	//indicates the page is public
	private boolean pagePublic;
	
	// pictures for the group workspace
	private Set<GroupWorkspaceProjectPagePicture> pictures = new HashSet<GroupWorkspaceProjectPagePicture>();

	/* date this record was created */
	private Timestamp createdDate;


	/**
     * Package protected constructor 
     */
    GroupWorkspaceProjectPage(){}
    
    /**
     * Constructor 
     * 
     * @param groupWorkspace - group workspace this project page belongs to.
     * 
     * @param name - name of the group project page.
     */
    GroupWorkspaceProjectPage(GroupWorkspace groupWorkspace)
    {
    	setGroupWorkspace(groupWorkspace);
    	
    	createdDate = new Timestamp(new Date().getTime());
    }
    
	/**
	 * Get the description.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Get the group workspace.
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}

	/**
	 * Set the group workspace this project page is for.
	 * 
	 * @param groupWorkspace
	 */
	void setGroupWorkspace(GroupWorkspace groupWorkspace) {
		this.groupWorkspace = groupWorkspace;
	}
	
    /**
     * Get if this page is public.
     * 
     * @return true if the page is public
     */
    public boolean getPagePublic() {
		return pagePublic;
	}

	/**
	 * If Set to true if the page is public.
	 * 
	 * @param pagePublic
	 */
	public void setPagePublic(boolean pagePublic) {
		this.pagePublic = pagePublic;
	}
	
	/**
	 * Get the pictures 
	 * 
	 * @return
	 */
	public Set<GroupWorkspaceProjectPagePicture> getPictures() {
		return pictures;
	}

	/**
	 * Set the pictures for the group project page.
	 * 
	 * @param pictures
	 */
	void setPictures(Set<GroupWorkspaceProjectPagePicture> pictures) {
		this.pictures = pictures;
	}
	
	/**
	 * Hash code is based on the name of
	 * the group space
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value +=  getGroupWorkspace().getLowerCaseName() == null ? 0 : getGroupWorkspace().getLowerCaseName().hashCode();
		return value;
	}
	
	/**
	 * Equals is tested based on name ignoring case 
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceProjectPage)) return false;

		final GroupWorkspaceProjectPage other = (GroupWorkspaceProjectPage) o;

		if( ( getGroupWorkspace().getLowerCaseName() != null && !getGroupWorkspace().getLowerCaseName().equalsIgnoreCase(other.getGroupWorkspace().getLowerCaseName()) ) ||
			( getGroupWorkspace().getLowerCaseName() == null && other.getGroupWorkspace().getLowerCaseName() != null ) ) return false;

		return true;
	}
	
	/**
	 * Get the date this project page was created.
	 * 
	 * @return
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * Set the date this project page was created.
	 * 
	 * @param createdDate
	 */
	void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

}
