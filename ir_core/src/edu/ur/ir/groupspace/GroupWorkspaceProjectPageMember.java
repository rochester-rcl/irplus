package edu.ur.ir.groupspace;

import edu.ur.order.Orderable;
import edu.ur.persistent.BasePersistent;

/**
 * Member of the project page.
 * 
 * @author NathanS
 *
 */
public class GroupWorkspaceProjectPageMember extends BasePersistent implements Orderable {
	
	// eclipse generated id
	private static final long serialVersionUID = 9165332666763201725L;

	// Group workspace project page
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;
	
	// group workspace user
	private GroupWorkspaceUser groupWorkspaceUser;
	
	// title for the member
	private String title;
	
	// description for the member
	private String description;
	
	// order of the member
	private int order;
	
  
	/**
     * Package protected constructor 
     */
    GroupWorkspaceProjectPageMember(){}
    
    /**
     * Package protected constructor.
     * 
     * @param groupWorkspaceProjectPage
     * @param groupWorkspaceUser
     */
    GroupWorkspaceProjectPageMember(GroupWorkspaceProjectPage groupWorkspaceProjectPage, GroupWorkspaceUser groupWorkspaceUser)
    {
    	setGroupWorkspaceProjectPage(groupWorkspaceProjectPage);
    	setGroupWorkspaceUser(groupWorkspaceUser);
    }
    
	/**
	 * Get the group workspace project page this member belongs to.
	 * 
	 * @return project page
	 */
	public GroupWorkspaceProjectPage getGroupWorkspaceProjectPage() {
		return groupWorkspaceProjectPage;
	}

	/**
	 * Set the project page this member belongs to.
	 * 
	 * @param groupWorkspaceProjectPage
	 */
	void setGroupWorkspaceProjectPage(
			GroupWorkspaceProjectPage groupWorkspaceProjectPage) {
		this.groupWorkspaceProjectPage = groupWorkspaceProjectPage;
	}

	/**
	 * Get the group workspace user.
	 * 
	 * @return
	 */
	public GroupWorkspaceUser getGroupWorkspaceUser() {
		return groupWorkspaceUser;
	}

	/**
	 * Set the group workspace user.
	 * 
	 * @param groupWorkspaceUser
	 */
	void setGroupWorkspaceUser(GroupWorkspaceUser groupWorkspaceUser) {
		this.groupWorkspaceUser = groupWorkspaceUser;
	}

	/**
	 * Get the title for the user.
	 * 
	 * @return title of the user
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title of the user.
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the description of the user.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description of the user.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Get the order of the project member
	 * 
	 * @return - place in the list
	 */
	public int getOrder() {
	    return order;
	}

	/**
	 * Set the order for the user.
	 * 
	 * @param order
	 */
	void setOrder(int order) {
	    this.order = order;
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
		value +=  groupWorkspaceProjectPage == null ? 0 : groupWorkspaceProjectPage.hashCode();
		value +=  groupWorkspaceUser == null ? 0 : groupWorkspaceUser.hashCode();
		return value;
	}
	
	/**
	 * Equals is tested based on group workspace user ang group workspace project page
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceProjectPageMember)) return false;

		final GroupWorkspaceProjectPageMember other = (GroupWorkspaceProjectPageMember) o;

		if( ( groupWorkspaceProjectPage != null && !groupWorkspaceProjectPage.equals(other.getGroupWorkspaceProjectPage()) ) ||
			( groupWorkspaceProjectPage == null && other.getGroupWorkspaceProjectPage() != null ) ) return false;

		if( ( groupWorkspaceUser != null && !groupWorkspaceUser.equals(other.getGroupWorkspaceUser()) ) ||
			( groupWorkspaceUser == null && other.getGroupWorkspaceUser() != null ) ) return false;
		return true;
	}

}
