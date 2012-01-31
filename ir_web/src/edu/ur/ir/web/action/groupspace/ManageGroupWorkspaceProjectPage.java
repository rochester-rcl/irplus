package edu.ur.ir.web.action.groupspace;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageService;
import edu.ur.ir.groupspace.GroupWorkspaceService;
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
public class ManageGroupWorkspaceProjectPage extends ActionSupport implements  UserIdAware, Preparable{

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
	
	/*  Logger. */
	private static final Logger log = Logger.getLogger(ManageGroupWorkspaceProjectPage.class);
	
	/* id of the group workspace user to add/remove to/from the project page */
	private Long groupWorkspaceUserId;
	
	/* group workspace service */
	private GroupWorkspaceService groupWorkspaceService;
	
	


	public String execute()
	{
		
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
	 * Allows the user to view and edit description.
	 * 
	 * @return
	 */
	public String viewDescription()
	{

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
	 * Save the project page description.
	 * 
	 * @return
	 */
	public String saveDescription()
	{
		log.debug("save description for group workspace = " + groupWorkspaceProjectPage);
		if( groupWorkspaceProjectPage != null )
		{
			IrUser user = userService.getUser(userId, false);
			GroupWorkspaceUser workspaceUser = groupWorkspaceProjectPage.getGroupWorkspace().getUser(user);
			if( workspaceUser != null && workspaceUser.isOwner())
			{
				groupWorkspaceProjectPageService.save(groupWorkspaceProjectPage);
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
	 * Allow the user to edit the members on the project page.
	 * 
	 * @return
	 */
	public String viewMembers()
	{
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
	 * Add a member to the project page.
	 * 
	 * @return
	 */
	public String addMember()
	{
		if( groupWorkspaceProjectPage != null )
		{
			IrUser user = userService.getUser(userId, false);
			GroupWorkspaceUser workspaceUser = groupWorkspaceProjectPage.getGroupWorkspace().getUser(user);
			if( workspaceUser != null && workspaceUser.isOwner())
			{
				GroupWorkspaceUser gwu = groupWorkspaceProjectPage.getGroupWorkspace().getUser(groupWorkspaceUserId);
			    if( gwu != null )
			    {
			    	gwu.setShowOnProjectPage(true);
			    	groupWorkspaceService.save(groupWorkspaceProjectPage.getGroupWorkspace());
			    }
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
	 * Add a member to the project page.
	 * 
	 * @return
	 */
	public String removeMember()
	{
		if( groupWorkspaceProjectPage != null )
		{
			IrUser user = userService.getUser(userId, false);
			GroupWorkspaceUser workspaceUser = groupWorkspaceProjectPage.getGroupWorkspace().getUser(user);
			if( workspaceUser != null && workspaceUser.isOwner())
			{
				GroupWorkspaceUser gwu = groupWorkspaceProjectPage.getGroupWorkspace().getUser(groupWorkspaceUserId);
			    if( gwu != null )
			    {
			    	gwu.setShowOnProjectPage(false);
			    	groupWorkspaceService.save(groupWorkspaceProjectPage.getGroupWorkspace());
			    }
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

	
	public void prepare() throws Exception {
		log.debug("Prepare called id = " + groupWorkspaceProjectPageId);
		if( groupWorkspaceProjectPageId != null )
		{
		    groupWorkspaceProjectPage = groupWorkspaceProjectPageService.getById(groupWorkspaceProjectPageId, false);
		    log.debug("Group workspace project page found value  = " + groupWorkspaceProjectPage);
		}
	}
	
	/**
	 * Get the group workspace user id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceUserId() {
		return groupWorkspaceUserId;
	}

	/**
	 * Set the group workspace user id.
	 * 
	 * @param groupWorkspaceUserId
	 */
	public void setGroupWorkspaceUserId(Long groupWorkspaceUserId) {
		this.groupWorkspaceUserId = groupWorkspaceUserId;
	}
	
	/**
	 * Set the group workspace service.
	 * 
	 * @param groupWorkspaceService
	 */
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}


}
