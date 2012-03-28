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

package edu.ur.ir.web.action.groupspace;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageMember;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageMemberService;
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
	
	/* group workspace project page member being edited */
	private GroupWorkspaceProjectPageMember member;
	
	/* id of the member to load */
	private Long memberId;

	/* group workspace project page member service */
    private GroupWorkspaceProjectPageMemberService groupWorkspaceProjectPageMemberService;


	/*  Logger. */
	private static final Logger log = Logger.getLogger(ManageGroupWorkspaceProjectPage.class);
	
	/* id of the group workspace user to add/remove to/from the project page */
	private Long groupWorkspaceUserId;
	


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
	 * Edit a group workspace member
	 * @return
	 */
	public String editMember()
	{
		if( member != null )
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
	 * Save the updated member data.
	 * 
	 * @return
	 */
	public String saveMemberData()
	{
		log.debug("save member data for member  = " + member);
		if( member != null )
		{
			IrUser user = userService.getUser(userId, false);
			GroupWorkspaceUser workspaceUser = member.getGroupWorkspaceProjectPage().getGroupWorkspace().getUser(user);
			if( workspaceUser != null && workspaceUser.isOwner())
			{
				groupWorkspaceProjectPageMemberService.save(member);
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
	 * View the images for a group workspace project page
	 * @return
	 */
	public String viewImages()
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
			    	member = groupWorkspaceProjectPage.addMember(gwu);
			    	groupWorkspaceProjectPageService.save(groupWorkspaceProjectPage);
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
			    	groupWorkspaceProjectPage.removeMember(groupWorkspaceProjectPage.getMember(gwu));
			    	groupWorkspaceProjectPageService.save(groupWorkspaceProjectPage);
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
	 * Move a project member up on the project page.
	 * 
	 * @return
	 */
	public String moveMemberUp()
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
			    	GroupWorkspaceProjectPageMember gwppm = groupWorkspaceProjectPage.getMember(gwu);
			    	groupWorkspaceProjectPage.moveMemberUp(gwppm);
			    	groupWorkspaceProjectPageService.save(groupWorkspaceProjectPage);
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
	 * Move a project member up on the project page.
	 * 
	 * @return
	 */
	public String moveMemberDown()
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
			    	GroupWorkspaceProjectPageMember gwppm = groupWorkspaceProjectPage.getMember(gwu);
			    	groupWorkspaceProjectPage.moveMemberDown(gwppm);
			    	groupWorkspaceProjectPageService.save(groupWorkspaceProjectPage);
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
		if( memberId != null )
		{
			member = groupWorkspaceProjectPageMemberService.getById(memberId, false);
			if( member != null )
			{
				groupWorkspaceProjectPage = member.getGroupWorkspaceProjectPage();
			}
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
	 * Get the project page member being edited.
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPageMember getMember() {
		return member;
	}
	
	/**
	 * Set the id of the member to load
	 * @param memberId
	 */
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	/**
	 * Set the group workspace project page member service.
	 * 
	 * @param groupWorkspaceProjectPageMemberService
	 */
	public void setGroupWorkspaceProjectPageMemberService(
			GroupWorkspaceProjectPageMemberService groupWorkspaceProjectPageMemberService) {
		this.groupWorkspaceProjectPageMemberService = groupWorkspaceProjectPageMemberService;
	}


}
