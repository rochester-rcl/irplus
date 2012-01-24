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

import edu.ur.order.Orderable;
import edu.ur.persistent.BasePersistent;

/**
 * Link for group project page.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceProjectPageLink extends BasePersistent implements Orderable {

	// eclipse generated id
	private static final long serialVersionUID = -259787022852873021L;

	// group project this link belongs to 
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;

	// string url 
	private String url;
	
	// determines the order of the link 
	private int order;
	
	// name given to the link 
	private String name;
	
	// description given to the link 
	private String description;
	
	
	/**
	 * Package protected constructor
	 */
	GroupWorkspaceProjectPageLink(){}
	
	/**
	 * Default constructor 
	 * 
	 * @param researcher - researcher who owns the link
	 * @param name - name of the link
	 * @param url - link to the resource
	 */
	public GroupWorkspaceProjectPageLink(GroupWorkspaceProjectPage groupWorkspaceProjectPage, String name, String url, int order)
	{
		setOrder(order);
		setUrl(url);
		setGroupWorkspaceProjectPage(groupWorkspaceProjectPage);
		setName(name);
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ name = ");
		sb.append(name);
		sb.append(" id = ");
		sb.append(id);
		sb.append(" url = ");
		sb.append(url);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Hash code for a personal link
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += getName() == null ? 0 : getName().hashCode();
		value += groupWorkspaceProjectPage == null ? 0 : groupWorkspaceProjectPage.hashCode();
		return value;
		
	}
	
	/**
	 * Equals method for a personal researcher link.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceProjectPageLink)) return false;

		final GroupWorkspaceProjectPageLink other = (GroupWorkspaceProjectPageLink) o;

		if( (other.getName() != null && !other.getName().equals(getName())) ||
			(other.getName() == null && getName() != null )	) return false;
		
		if( (other.getGroupWorkspaceProjectPage() != null && !other.getGroupWorkspaceProjectPage().equals(getGroupWorkspaceProjectPage())) ||
			(other.getGroupWorkspaceProjectPage() == null && getGroupWorkspaceProjectPage() != null )	) return false;

		return true;
	}
	
	/**
	 * Get the group project page for this link.
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPage getGroupWorkspaceProjectPage() {
		return groupWorkspaceProjectPage;
	}
	
	/**
	 * Set the group project page for this link.
	 * 
	 * @param groupProjectPage
	 */
	void setGroupWorkspaceProjectPage(GroupWorkspaceProjectPage groupWorkspaceProjectPage) {
		this.groupWorkspaceProjectPage = groupWorkspaceProjectPage;
	}

	
	/**
	 * Get the string url value.
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the string url value.
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see edu.ur.order.Orderable#getOrder()
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Set the order for this link.
	 * 
	 * @param order
	 */
	void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Get the name for the link
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name for the link.
	 * 
	 * @param name
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the description for the link.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the decription for the link.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
