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
	private long userId;
	
	/** date the failure record was created  */
	private Timestamp dateDeleted;
	
	/** full path to the file */
	private String fullPath;
	
	/** description of the personal item */
	private String description;
	
	/**
	 * Create a delete record of the personal item.
	 * 
	 * @param userId - user who performed the delete
	 * @param fullPath - full path to the item
	 * @param description - description of the item
	 */
	public PersonalItemDeleteRecord(Long userId, String fullPath, String description)
	{
		setDateDeleted(new Timestamp(new Date().getTime()));
		setUserId(userId);
		setFullPath(fullPath);
		setDescription(description);
	}

	/**
	 * Id of the suer who deleted the item.
	 * 
	 * @return
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Id of the user who deleted the file.
	 * 
	 * @param userId
	 */
	void setUserId(long userId) {
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
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" user id = ");
		sb.append(userId);
		sb.append(" deleted date = ");
		sb.append(dateDeleted);
		sb.append(" full path = ");
		sb.append(fullPath);
		sb.append( " description = ");
		sb.append(description);
		sb.append("]");
		return sb.toString();
	}
	
	

}
