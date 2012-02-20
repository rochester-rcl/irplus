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

package edu.ur.ir.user;

import edu.ur.persistent.CommonPersistent;

/**
 * The affiliation of the user with the university.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class Affiliation extends CommonPersistent {
	
	// Eclipse generated id.
	private static final long serialVersionUID = 6616903691007174887L;

	//  Indicates whether the affiliation has a author permissions 
	private boolean author = false;

	//  Indicates whether the affiliation has a researcher permissions 
	private boolean researcher = false;
	
	//  Indicates whether the affiliation has create group workspace permissions 
	private boolean workspaceCreator = false;
	
	//  Indicates whether the affiliation needs to be approved by the admin 
	private boolean needsApproval = false;

	/**
	 * Indicates whether the affiliation has a author permissions 
	 * 
	 * @return
	 */
	public boolean getAuthor() {
		return author;
	}

	/**
	 * Sets whether the affiliation has a author permissions 
	 * 
	 * @param author
	 */
	public void setAuthor(boolean author) {
		this.author = author;
	}

	/**
	 * Indicates whether the affiliation has a researcher permissions 
	 * 
	 * @return
	 */
	public boolean getResearcher() {
		return researcher;
	}

	/**
	 * Sets whether the affiliation has a researcher permissions
	 *   
	 * @param researcher
	 */
	public void setResearcher(boolean researcher ) {
		this.researcher = researcher ;
	}

	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Affiliation)) return false;

		final Affiliation other = (Affiliation) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;


		return true;
	}
	
	/**
	 * Return the string representation.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Name = ");
		sb.append(name);
		sb.append(" description ");
		sb.append(description);
		sb.append(" id = ");
		sb.append(id);
		sb.append(" needs approval = ");
		sb.append(needsApproval);
		sb.append(" author ");
		sb.append(author);
		sb.append(" researcher ");
		sb.append(researcher);
		sb.append(" version = ");
		sb.append(version);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * If set to true a user with this affiliation has to be approved.
	 * 
	 * @return
	 */
	public boolean isNeedsApproval() {
		return needsApproval;
	}
	
	/**
	 * If true the user needs approval for this type of affilation.
	 * 
	 * @return
	 */
	public boolean getNeedsApproval()
	{
		return needsApproval;
	}

	/**
	 * Set the needs approval flag - if true user needs approval for
	 * the given affiliation.
	 * 
	 * @param needsApproval
	 */
	public void setNeedsApproval(boolean needsApproval) {
		this.needsApproval = needsApproval;
	}
	
	/**
	 * Returns true if the user has workspace creator privileges
	 * 
	 * @return
	 */
	public boolean isWorkspaceCreator() {
		return workspaceCreator;
	}
	
	/**
	 * Get workspace creator privileges.
	 * 
	 * @return
	 */
	public boolean getWorkspaceCreator() {
		return workspaceCreator;
	}

	/**
	 * Set workspace creator privileges
	 * 
	 * @param workspaceCreator
	 */
	public void setWorkspaceCreator(boolean workspaceCreator) {
		this.workspaceCreator = workspaceCreator;
	}
}
