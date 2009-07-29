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

package edu.ur.ir.user;

import java.sql.Timestamp;
import java.util.Date;

import edu.ur.persistent.BasePersistent;

/**
 * Represents a delete record for a personal item.
 * 
 * @author Nathan Sarr
 *
 */
public class PersonalItemDeleteRecord extends BasePersistent{

	/** eclipse generated id */
	private static final long serialVersionUID = 8718231697844695857L;
	
	/** id of the user who deleted the file */
	private Long userId;
	
	/** id of the personal file */
	private Long personalItemId;
	
	/** date the failure record was created  */
	private Timestamp dateDeleted;
	
	/** full path to the file */
	private String fullPath;
	
	/** description of the personal item */
	private String description;
	
	/** Reason for the delete if any */
	private String deleteReason;
	
	/**
	 * Package protected constructor
	 */
	PersonalItemDeleteRecord(){}
	
	/**
	 * Create a delete record of the personal item.
	 * 
	 * @param userId - user who performed the delete
	 * @param fullPath - full path to the item
	 * @param description - description of the item
	 */
	public PersonalItemDeleteRecord(Long userId, Long personalItemId, String fullPath, String description)
	{
		setDateDeleted(new Timestamp(new Date().getTime()));
		setPersonalItemId(personalItemId);
		setUserId(userId);
		setFullPath(fullPath);
		setDescription(description);
	}

	/**
	 * Id of the suer who deleted the item.
	 * 
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Id of the user who deleted the file.
	 * 
	 * @param userId
	 */
	void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Date the file was deleted.
	 * 
	 * @return
	 */
	public Timestamp getDateDeleted() {
		return dateDeleted;
	}

	/**
	 * Date the file was deleted.
	 * 
	 * @param dateDeleted
	 */
	void setDateDeleted(Timestamp dateDeleted) {
		this.dateDeleted = dateDeleted;
	}

	/**
	 * Full path to the personal item.
	 * 
	 * @return
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * Set the full path to the item.
	 * 
	 * @param fullPath
	 */
	void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	/**
	 * Description of the item.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Description of the item.
	 * 
	 * @param description
	 */
	void setDescription(String description) {
		this.description = description;
	}
	
	public int hashCode()
	{
		int value = 0;
		value += personalItemId == null ? 0 : personalItemId.hashCode();
		return value;
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof PersonalItemDeleteRecord)) return false;

		final PersonalItemDeleteRecord other = (PersonalItemDeleteRecord) o;

		if( ( personalItemId != null && !personalItemId.equals(other.getPersonalItemId()) ) ||
			( personalItemId == null && other.getPersonalItemId() != null ) ) return false;


		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" user id = ");
		sb.append(userId);
		sb.append(" personal item id = ");
		sb.append(personalItemId);
		sb.append(" deleted date = ");
		sb.append(dateDeleted);
		sb.append(" full path = ");
		sb.append(fullPath);
		sb.append( " description = ");
		sb.append(description);
		sb.append("]");
		return sb.toString();
	}

	public Long getPersonalItemId() {
		return personalItemId;
	}

	void setPersonalItemId(Long personalItemId) {
		this.personalItemId = personalItemId;
	}

	public String getDeleteReason() {
		return deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

	
	

}
