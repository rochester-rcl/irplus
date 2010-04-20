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

package edu.ur.ir.institution;

import java.util.Date;

import edu.ur.ir.item.GenericItem;
import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;

/**
 * Items to be reviewed before submission
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ReviewableItem extends BasePersistent {

	/** Eclipse generated id */
	private static final long serialVersionUID = -7349044458123002919L;
	
	public static final String PENDING_REVIEW = "PENDING";
	
	public static final String ACCEPTED_REVIEW = "ACCEPTED";
	
	public static final String REJECTED_REVIEW = "REJECTED";

	/** Item to be reviewed */
	private GenericItem item;
	
	/** Collection the item belongs to. */
	private InstitutionalCollection institutionalCollection;	

	/** Date the item was accepted/rejected. */
	private Date date;
	
	/** Reason this item was rejected */
	private String reasonForRejection;
	
	/** User who accepted/rejected the item*/
	private IrUser reviewer;
	
	/** Indicates whether the item status is pending/accepted/rejected */
	private String reviewStatus;

	/**
	 * Package protected constructor.
	 */
	ReviewableItem(){};
	
	/**
	 * Constructor
	 * 
	 * @param item item to be reviewed
	 * @param institutionalCollection
	 */
	ReviewableItem(GenericItem item, InstitutionalCollection institutionalCollection) throws  CollectionDoesNotAcceptItemsException{
		if(!institutionalCollection.getAllowsItems())
		{
			throw new CollectionDoesNotAcceptItemsException("This collection does not allow items");
		}
		this.item = item;
		this.institutionalCollection = institutionalCollection;
		this.reviewStatus = PENDING_REVIEW;
	}
	
	public InstitutionalCollection getInstitutionalCollection() {
		return institutionalCollection;
	}

	public void setInstitutionalCollection(
			InstitutionalCollection institutionalCollection) {
		this.institutionalCollection = institutionalCollection;
	}

	/**
	 * The item is accepted
	 * 
	 * @param user
	 */
	public void accept(IrUser reviewer) {
		this.reviewer = reviewer;
		this.reviewStatus = ACCEPTED_REVIEW;
		this.item.setLocked(false);
	}

	/**
	 * Item is rejected
	 * 
	 * @param user
	 * @param reason
	 */
	public void reject(IrUser reviewer, String reason) {
		this.reviewer = reviewer;
		this.reviewStatus = REJECTED_REVIEW;
		this.reasonForRejection = reason;
		this.item.setLocked(false);
	}
	
	/**
	 * Get the item to be reviewed
	 * 
	 * @return Item item to be reviewed
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Set the item to be reviewed
	 * 
	 * @param item item to be reviewed
	 */
	public void setItem(GenericItem item) {
		this.item = item;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public IrUser getReviewer() {
		return reviewer;
	}

	public void setReviewer(IrUser reviewer) {
		this.reviewer = reviewer;
	}

	public String getReasonForRejection() {
		return reasonForRejection;
	}

	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = reasonForRejection;
	}

}
