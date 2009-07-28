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
 * Represents the deletion of a personal file - captures basic information.
 * 
 * @author Nathan Sarr
 *
 */
public class PersonalFileDeleteRecord extends BasePersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 131579938691713460L;

	/** id of the user who deleted the file */
	private Long userId;
	
	/** id of the personal file */
	private Long personalFileId;
	
	/** date the failure record was created  */
	private Timestamp dateDeleted;
	
	/** full path to the file */
	private String fullPath;
	
	/** description of the personal item */
	private String description;
	
	/**
	 * Package protected constructor
	 */
	PersonalFileDeleteRecord(){}
	
	/**
	 * Default constructor.
	 * 
	 * @param userId - id of the user deleting the file
	 * @param fullPath - full path to the personal file
	 * @param description - description of the file
	 */
	public PersonalFileDeleteRecord(Long userId, Long personalFileId, String fullPath, String description)
	{
		setDateDeleted(new Timestamp(new Date().getTime()));
		setUserId(userId);
		setPersonalFileId(personalFileId);
		setFullPath(fullPath);
		setDescription(description);
	}

	/**
	 * Id of the user who deleted the file.
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
	 * Full path to the file.
	 * 
	 * @return
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * Full path to the file.
	 * 
	 * @param fullPath
	 */
	void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	/**
	 * Description of the personal file.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Description of the personal file.
	 * 
	 * @param description
	 */
	void setDescription(String description) {
		this.description = description;
	}
	
	public int hashCode()
	{
		int value = 0;
		value += personalFileId == null ? 0 : personalFileId.hashCode();
		return value;
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof PersonalFileDeleteRecord)) return false;

		final PersonalFileDeleteRecord other = (PersonalFileDeleteRecord) o;

		if( ( personalFileId != null && !personalFileId.equals(other.getPersonalFileId()) ) ||
			( personalFileId == null && other.getPersonalFileId() != null ) ) return false;


		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" user id = ");
		sb.append(userId);
		sb.append(" personal file id = " );
		sb.append(personalFileId);
		sb.append(" deleted date = ");
		sb.append(dateDeleted);
		sb.append(" full path = ");
		sb.append(fullPath);
		sb.append( " description = ");
		sb.append(description);
		sb.append("]");
		return sb.toString();
	}

	public Long getPersonalFileId() {
		return personalFileId;
	}

	void setPersonalFileId(Long personalFileId) {
		this.personalFileId = personalFileId;
	}

}
