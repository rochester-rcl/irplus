package edu.ur.ir.web.action.handle;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.handle.HandleNameAuthority;
import edu.ur.ir.handle.HandleService;


/**
 * Class to manage handle name authorities.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageHandleNameAuthority extends ActionSupport implements Preparable {
	
	/** generated version id. */
	private static final long serialVersionUID = -6442337609832744458L;

	/** content type service */
	private HandleService handleService;

	/**  Logger for managing handle name authorities*/
	private static final Logger log = Logger.getLogger(ManageHandleNameAuthority.class);
	
	/** Set of handle name authorities for viewing */
	private Collection<HandleNameAuthority> handleNameAuthorities;
	
	/**  handle name authority  */
	private HandleNameAuthority handleNameAuthority;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the name authority has been added*/
	private boolean added = false;
	
	/** Indicates the handle name authorities that have been deleted */
	private boolean deleted = false;
	
	/** id of the name authority  */
	private Long id;
	
	/** Set of name authority ids */
	private long[] nameAuthorityIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of content types */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public  ManageHandleNameAuthority()  {}

	/**
	 * Method to create a new content type.
	 * 
	 * Create a new content type
	 */
	public String create()
	{
		log.debug("creating a name authority = " + handleNameAuthority);
		added = false;
		HandleNameAuthority other = handleService.getNameAuthority(handleNameAuthority.getNamingAuthority());
		if( other == null)
		{
		    handleService.save(handleNameAuthority);
		    added = true;
		}
		else
		{
			message = getText("handleAuthorityNameError", 
					new String[]{handleNameAuthority.getNamingAuthority()});
			addFieldError("handleNameAuthorityExists", message);
		}
        return "added";
	}
	
	/**
	 * Method to update an existing handle authority.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing name authority id = " + handleNameAuthority.getId());
		added = false;

		HandleNameAuthority other = handleService.getNameAuthority(handleNameAuthority.getNamingAuthority());
		
		if( other == null || other.getId().equals(handleNameAuthority.getId()))
		{
			handleService.save(handleNameAuthority);
			added = true;
		}
		else
		{
			message = getText("handleAuthorityNameError", 
					new String[]{handleNameAuthority.getNamingAuthority()});
			
			addFieldError("handleNameAuthorityExists", message);
		}
        return "added";
	}
	
	/**
	 * Get a content type
	 * 
	 * @return the content type
	 */
	public String get()
	{
		handleNameAuthority = handleService.getNameAuthority(id, false);	
		return "get";
	}
	
	/**
	 * Removes the selected items and collections.
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete handle name authority called");
		if( nameAuthorityIds != null )
		{
		    for(int index = 0; index < nameAuthorityIds.length; index++)
		    {
			    log.debug("Deleting handle name authority with id " + nameAuthorityIds[index]);
			    // only delete if no handles attached
			    if( handleService.getHandleCountForNameAuthority(nameAuthorityIds[index]) <= 0)
			    {
			    	handleService.delete(handleService.getNameAuthority(nameAuthorityIds[index], false));
			    }
		    }
		}
		deleted = true;
		return "deleted";
	}
 
	/**
	 * Get the content types table data.
	 * 
	 * @return
	 */
	public String viewHandleNameAuthorities()
	{
		handleNameAuthorities = handleService.getAllNameAuthorities();
		return SUCCESS;
	}

	public Collection<HandleNameAuthority> getHandleNameAuthorities() {
		return handleNameAuthorities;
	}

	public void setHandleNameAuthorities(
			Collection<HandleNameAuthority> handleNameAuthorities) {
		this.handleNameAuthorities = handleNameAuthorities;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isAdded() {
		return added;
	}
	public void setAdded(boolean added) {
		this.added = added;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long[] getNameAuthorityIds() {
		return nameAuthorityIds;
	}

	public void setNameAuthorityIds(long[] nameAuthorityIds) {
		this.nameAuthorityIds = nameAuthorityIds;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public HandleNameAuthority getHandleNameAuthority() {
		return handleNameAuthority;
	}

	public void setHandleNameAuthority(HandleNameAuthority handleNameAuthority) {
		this.handleNameAuthority = handleNameAuthority;
	}

	public void prepare() throws Exception {
		if( id != null)
		{
			handleNameAuthority = handleService.getNameAuthority(id, false);		
		}
	}
	
	public HandleService getHandleService() {
		return handleService;
	}

	public void setHandleService(HandleService handleService) {
		this.handleService = handleService;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

	public int getTotalHits() {
		return totalHits;
	}

}
