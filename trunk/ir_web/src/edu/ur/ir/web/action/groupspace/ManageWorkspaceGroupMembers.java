package edu.ur.ir.web.action.groupspace;

import edu.ur.ir.web.action.UserIdAware;

/**
 * Allows a user to manage the members of a user group.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageWorkspaceGroupMembers implements UserIdAware
{


    private Long userId; 
    
    private Long groupId;
    
	
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
}
