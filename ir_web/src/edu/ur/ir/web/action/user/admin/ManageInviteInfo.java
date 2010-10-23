package edu.ur.ir.web.action.user.admin;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.ur.ir.user.InviteInfo;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Allows an administrator to manage invite info objects.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageInviteInfo extends Pager implements UserIdAware{

	/** eclipse generated id */
	private static final long serialVersionUID = 2170627808999956527L;
	
	/**  Logger for managing invite info objects*/
	private static final Logger log = Logger.getLogger(ManageInviteInfo.class);
	
	/** Invite user service */
	private InviteUserService inviteUserService;
	
	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of departments */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Set of departments for viewing the departments */
	private Collection<InviteInfo> inviteInfos;

    /** id of the user tyring to access the information */
    private long userId;
	
	/** service for accessing user information */
	private UserService userService;

	public ManageInviteInfo()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}
	
	/**
	 * Get the departments table data.
	 * 
	 * @return
	 */
	public String viewInviteInfos()
	{
		log.debug("invite info called");
		IrUser user = userService.getUser(userId, false);
		if(!user.hasRole(IrRole.ADMIN_ROLE))
		{
			return "accessDenied";
		}


		rowEnd = rowStart + numberOfResultsToShow;
	    
		inviteInfos = inviteUserService.getInviteInfosOrderByInviteor(rowStart, 
	    		numberOfResultsToShow, OrderType.getOrderType(sortType));
	    totalHits = inviteUserService.getInviteInfoCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
	}
	
	
	/**
	 * Get the total hits.
	 * 
	 * @see edu.ur.ir.web.table.Pager#getTotalHits()
	 */
	public int getTotalHits() {
		return totalHits;
	}

	/**
	 * Set the user id accessing the information.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Setthe invite user service.
	 * 
	 * @param inviteUserService
	 */
	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}

	/**
	 * Get the invite infos 
	 * 
	 * @return - invite info information.
	 */
	public Collection<InviteInfo> getInviteInfos() {
		return inviteInfos;
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
