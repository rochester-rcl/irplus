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

import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageImage;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Get the next group workspace picture.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceProjectPageView 
extends ActionSupport implements UserIdAware {

    //  Eclipse generated id 
	private static final long serialVersionUID = -2160078044974663801L;

	//  Logger
	private static final Logger log = Logger.getLogger(GroupWorkspaceProjectPageView.class);
	
	// determine what the user is trying to do 
	
	// this if when the page is first initalized
	public static final String INIT = "INIT";
	
	// get the next picture
	public static final String NEXT = "NEXT";
	
	// get the previous picture
	public static final String PREV = "PREV";
	
	// Group Workspace
	private GroupWorkspace groupWorkspace;
	

	// Id of group workspace
	private Long groupWorkspaceId;

	// Service class for group workspaces
	private GroupWorkspaceService groupWorkspaceService;
	
	// Service for user information
	private UserService userService;

	// Researcher picture IrFile 
	private IrFile irFile;
	
	// Represents the location of current picture displayed */
	private int currentLocation;
	
	// Type represents NEXT, PREVIOUS, INIT 
	private String type;
	
	// id of the user trying to access the data 
	private Long userId;
	
	// number of images for the group workspace
	private int numImages = 0;
	

	/**
	 * View the researcher page.
	 * 
	 * @return
	 */
	public String view()
	{
		log.debug("View group workspace project page:groupWorkspaceId="+ groupWorkspaceId);
		GroupWorkspaceProjectPage projectPage =  null;
		if( groupWorkspaceId != null && groupWorkspaceId > 0)
		{	
		    groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		    projectPage = groupWorkspace.getGroupWorkspaceProjectPage();
		}

		if (projectPage != null) {
			
			// if the group workspace project page is not public and the user accessing the file
			// is not the owner
		 	if( !projectPage.getPagePublic())
	    	{
		 		IrUser user = userService.getUser(userId, false);
		 		GroupWorkspaceUser groupUser = groupWorkspace.getUser(user);
		 		
		 		// if project page is private and the user is not a member then
		 		// access is denied
		 		if( groupUser == null )
		 		{
		 			groupWorkspace = null;
	    		    return "accessDenied";
		 		}
	    	}
		}
		else
		{
			return "notFound";
		}

		
		return SUCCESS;
	}

	/**
     * Gets the next ir file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String getImage() {
    	
    	if( log.isDebugEnabled())
    	{
    		log.debug("Next group workspace project page Picture");
    	}

    	groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
    	GroupWorkspaceProjectPage projectPage =  null;
		if( groupWorkspaceId != null && groupWorkspaceId > 0)
		{	
		    groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		    projectPage = groupWorkspace.getGroupWorkspaceProjectPage();
		}

		if (projectPage != null) {
			
			// if the group workspace project page is not public and the user accessing the file
			// is not the owner
		 	if( !projectPage.getPagePublic())
	    	{
		 		IrUser user = userService.getUser(userId, false);
		 		GroupWorkspaceUser groupUser = groupWorkspace.getUser(user);
		 		
		 		// if project page is private and the user is not a member then
		 		// access is denied
		 		if( groupUser == null )
		 		{
	    		    return "accessDenied";
		 		}
	    	}
		}
		else
		{
			return "notFound";
		}
    	
    	List<GroupWorkspaceProjectPageImage> images = projectPage.getImagesByOrder();
        if( images != null && images.size() > 0 )
        {
        	numImages = images.size();
    	    if( images.size() == 1 )
    	    {
    	    	currentLocation = 0;
    	    }
    	    else if ( type.equals(INIT))
    	    {
    	    	if (currentLocation < 0) {
    		    	currentLocation = 0;
    		    }
    		    
    	    }
       	    else if( type.equals(NEXT))
    	    {
    		    if( (currentLocation + 1) >= images.size())
    		    {
    			    currentLocation = 0;
    		    }
    		    else
    		    {
    			    currentLocation += 1;
    		    }
    	    }
    	    else if( type.equals(PREV))
    	    {
    		    if( (currentLocation -1 ) < 0 )
    		    {
    			    currentLocation = images.size() - 1;
    		    }
    		    else
    		    {
    			    currentLocation -= 1;
    		    }
    	    }
    	    irFile = images.get(currentLocation).getImageFile();
        }
        return SUCCESS;
    }
    
    
 
	/**
	 * Get the ir file 
	 * @return
	 */
	public IrFile getIrFile() {
		return irFile;
	}

	/**
	 * Get the current location in the list.
	 * @return
	 */
	public int getCurrentLocation() {
		return currentLocation;
	}

	/**
	 * Set the current location
	 * 
	 * @param currentLocation
	 */
	public void setCurrentLocation(int currentLocation) {
		this.currentLocation = currentLocation;
	}

	/**
	 * Get the type of action NEXT, PREVIOUS, INIT
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the type of action NEXT, PREVIOUS, INIT
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}


	/**
	 * Set the group workspace service.
	 * 
	 * @param groupWorkspaceService
	 */
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}
	
	/**
	 * Set the group workspace id.
	 * 
	 * @param groupWorkspaceId
	 */
	public void setGroupWorkspaceId(Long groupWorkspaceId) {
		this.groupWorkspaceId = groupWorkspaceId;
	}
	
	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Get the number of images for this group workspace.
	 * 
	 * @return
	 */
	public int getNumImages() {
		return numImages;
	}

	/**
	 * Get the group workspace.
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}


}
