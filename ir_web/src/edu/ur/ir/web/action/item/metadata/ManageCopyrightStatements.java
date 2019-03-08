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


package edu.ur.ir.web.action.item.metadata;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.item.CopyrightStatement;
import edu.ur.ir.item.CopyrightStatementService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Action to deal with copyright statements.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageCopyrightStatements extends Pager implements Preparable, UserIdAware {
	
	/** generated version id. */
	private static final long serialVersionUID = 5929487586704016172L;
	
	/** copyright statement service */
	private CopyrightStatementService copyrightStatementService;
	
	/**  Logger for managing copyright statements*/
	private static final Logger log = LogManager.getLogger(ManageCopyrightStatements.class);
	
	/** Set of copyright statements for viewing the copyright statements */
	private Collection<CopyrightStatement> copyrightStatements;
	
	/**  Content type for loading  */
	private CopyrightStatement copyrightStatement;



	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the copyright statement has been added*/
	private boolean added = false;
	
	/** Indicates the copyright statements have been deleted */
	private boolean deleted = false;
	
	/** id of the copyright statement  */
	private Long id;
	
	/** type of order [ ascending | descending ] 
	 *  this is for incoming requests */
	private String orderType = "asc";

	/** Total number of copyright statements */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** id of the user making the change */
	private Long userId;
	
	/** Service for dealing with institutional item version services */
	private InstitutionalItemVersionService institutionalItemVersionService;
	
	/** Service for user information  */
	private UserService userService;



	/** Default constructor */
	public  ManageCopyrightStatements() 
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Method to create a new copyright statement.
	 * 
	 * Create a new copyright statement
	 */
	public String create()
	{
		log.debug("creating a copyright statement = " + copyrightStatement.getName());
		added = false;
		CopyrightStatement other = this.copyrightStatementService.get(copyrightStatement.getName());
		if( other == null)
		{
		    copyrightStatementService.save(copyrightStatement);
		    added = true;
		}
		else
		{
			message = getText("copyrightStatementNameError", 
					new String[]{copyrightStatement.getName()});
			addFieldError("copyrightStatementAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Method to update an existing copyright statement.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updating copyright statement = " + copyrightStatement);
		added = false;

		CopyrightStatement other = copyrightStatementService.get(copyrightStatement.getName());
		
		if( other == null || other.getId().equals(copyrightStatement.getId()))
		{
			copyrightStatementService.save(copyrightStatement);
			IrUser user = userService.getUser(userId, false);
			institutionalItemVersionService.setAllVersionsAsUpdatedForCopyrightStatement(copyrightStatement, user, "Copyright Statement " + copyrightStatement +" Change");			
			added = true;
		}
		else
		{
			message = getText("copyrightStatementNameError", 
					new String[]{copyrightStatement.getName()});
			
			addFieldError("copyrightStatementAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Get a copyright statement
	 * 
	 * @return the copyright statement
	 */
	public String get()
	{
		copyrightStatement = copyrightStatementService.get(id, false);	
		return "get";
	}
	
	/**
	 * Removes the selected items and collections.
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete copyright statements called");
		if( copyrightStatement != null)
		{
		    copyrightStatementService.delete(copyrightStatement);
		}
		deleted = true;
		return "deleted";
	}
 
	/**
	 * Get the copyright statements table data.
	 * 
	 * @return
	 */
	public String viewCopyrightStatements()
	{
		rowEnd = rowStart + numberOfResultsToShow;
	    OrderType theOrderType = OrderType.getOrderType(orderType);
		copyrightStatements = copyrightStatementService.getCopyrightStatementsOrderByName(rowStart, numberOfResultsToShow, theOrderType);
	    totalHits = copyrightStatementService.getCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;

	}

	/**
	 * Get the copyright statement service.
	 * 
	 * @return
	 */
	public CopyrightStatementService getCopyrightStatementService() {
		return copyrightStatementService;
	}

	/**
	 * Set the copyright statement service.
	 * 
	 * @param copyrightStatementservice
	 */
	public void setCopyrightStatementService(CopyrightStatementService copyrightStatementService) {
		this.copyrightStatementService = copyrightStatementService;
	}
	
	/**
	 * List of copyright statements for display.
	 * 
	 * @return
	 */
	public Collection<CopyrightStatement> getCopyrightStatements() {
		return copyrightStatements;
	}
	/**
	 * Set the list of copyright statements.
	 * 
	 * @param copyrightStatements
	 */
	public void setCopyrightStatements(Collection<CopyrightStatement> copyrightStatements) {
		this.copyrightStatements = copyrightStatements;
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

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public CopyrightStatement getCopyrightStatement() {
		return copyrightStatement;
	}

	public void prepare() throws Exception {
		if( id != null)
		{
			copyrightStatement = copyrightStatementService.get(id, false);
		}
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

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}
	
	public void setCopyrightStatement(CopyrightStatement copyrightStatement) {
		this.copyrightStatement = copyrightStatement;
	}

	/**
	 * Set the id of the user making the change.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
