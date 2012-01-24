package edu.ur.ir.web.action.groupspace;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Allows a user to manage a group workspace project page.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageGroupWorkspaceProjectPage extends ActionSupport implements  UserIdAware{

	/*  eclipse generated id */
	private static final long serialVersionUID = 1465553406874848632L;

	/* id of the user accessing the information */
	private Long userId;
	
	/* id of the group workspace project page to access */
	private Long groupWorkspaceProjectPageId;
	
	/* service to deal with group workspace project page information */
	private GroupWorkspaceProjectPageService groupWorkspaceProjectPageService;
	
	/* user service for dealing with user information */
	private UserService userService;
	


	/* group workspace project page */
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;


	public String execute()
	{
		if( groupWorkspaceProjectPageId != null )
		{
		    groupWorkspaceProjectPage = groupWorkspaceProjectPageService.getById(groupWorkspaceProjectPageId, false);
		}
		
		if( groupWorkspaceProjectPage != null )
		{
			IrUser user = userService.getUser(userId, false);
			GroupWorkspaceUser workspaceUser = groupWorkspaceProjectPage.getGroupWorkspace().getUser(user);
			if( workspaceUser != null && workspaceUser.isOwner())
			{
		        return SUCCESS;
			}
			else
			{
				return "accessDenied";
			}
		}
		else
		{
			return "notFound";
		}
	}
	
	
	
	/**
	 * Get the grou pworkspace project page id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceProjectPageId() {
		return groupWorkspaceProjectPageId;
	}


	/**
	 * Set the group workspace project page id.
	 * @param groupWorkspaceProjectPageId
	 */
	public void setGroupWorkspaceProjectPageId(Long groupWorkspaceProjectPageId) {
		this.groupWorkspaceProjectPageId = groupWorkspaceProjectPageId;
	}


	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Set the group workspace project page service.
	 * 
	 * @param groupWorkspaceProjectPageService
	 */
	public void setGroupWorkspaceProjectPageService(
			GroupWorkspaceProjectPageService groupWorkspaceProjectPageService) {
		this.groupWorkspaceProjectPageService = groupWorkspaceProjectPageService;
	}
	
	/**
	 * Get the group workspace project page.
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPage getGroupWorkspaceProjectPage() {
		return groupWorkspaceProjectPage;
	}


	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
