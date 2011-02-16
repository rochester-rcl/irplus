/**  
   Copyright 2008 - 2011 University of Rochester

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

package edu.ur.ir.web.action.user;


import java.util.LinkedList;
import java.util.List;

import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;

/**
 * Allows a user to view all files shared with a given user.
 * 
 * @author Nathan Sarr
 *
 */
public class ViewAllFilesSharedWithUser extends Pager implements UserIdAware
{

	/* eclipse generated id */
	private static final long serialVersionUID = 6793022086271982666L;

	/* id of the user accessing the information. */
	private Long userId;
	
	/* id of the user the file is shared with */
	private Long sharedWithUserId;
	
	/* service to deal with user file system information */
	private UserFileSystemService userFileSystemService;
	
	/* user the file is being shared with */
	private IrUser sharedWithUser;

    private UserService userService;



	/* list of personal files */
	private List<PersonalFile> personalFiles = new LinkedList<PersonalFile>();
	
	/* Total number of shared files */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	/**
	 * Get all files shared with a given user.
	 */
	public ViewAllFilesSharedWithUser()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 20;
	}
	
	/**
	 * inject the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Get the total number of hits.
	 * 
	 * @see edu.ur.ir.web.table.Pager#getTotalHits()
	 */
	public int getTotalHits() {
		return totalHits;
	}
	
	public String execute()
	{
		
		IrUser user = userService.getUser(userId, false);
		
		if( !user.hasRole(IrRole.AUTHOR_ROLE) )
		{
			return "accessDenied";
		}
		
		sharedWithUser = userService.getUser(sharedWithUserId, false);
		rowEnd = rowStart + numberOfResultsToShow;
		
		totalHits = userFileSystemService.getFilesSharedWithUserCount(userId, sharedWithUserId).intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
		
		// we load by injected user id
		personalFiles = userFileSystemService.getFilesSharedWithUser(rowStart, numberOfResultsToShow, userId, sharedWithUserId);
		
		return SUCCESS;
	}
	
	/**
	 * Set the personal file system service.
	 * 
	 * @param userFileSystemService
	 */
	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}
	
	
	/**
	 * Get the personal files found 
	 * @return
	 */
	public List<PersonalFile> getPersonalFiles() {
		return personalFiles;
	}

	/**
	 * Set the user id to check for shared files with.
	 * @param sharedWithUserId
	 */
	public void setSharedWithUserId(Long sharedWithUserId) {
		this.sharedWithUserId = sharedWithUserId;
	}
	
	/**
	 * Get the user information for th shared with user.
	 * 
	 * @return
	 */
	public IrUser getSharedWithUser() {
		return sharedWithUser;
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
