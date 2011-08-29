
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

package edu.ur.ir.groupspace;

import java.sql.Timestamp;
import java.util.Date;

import edu.ur.persistent.BasePersistent;

/**
 * Represents the deletion of a group workspace file - captures basic information.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceFileDeleteRecord extends BasePersistent{
	
	// eclipse generated id
	private static final long serialVersionUID = 8544647525547283680L;

	/* id of the user who deleted the file */
	private Long userId;
	
	/* id of the group workspace file */
	private Long groupWorkspaceFileId;

	/* date the delete record was created  */
	private Timestamp dateDeleted;
	
	/* full path to the file */
	private String fullPath;
	
	/* description of the workspace file */
	private String description;
	
	/* reason the file was deleted if any */
	private String deleteReason;
	
	/* id of the workspace this file belonged to */
	private Long groupWorkspaceId;

	/* name of the group workspace this file belonged to */
	private String groupWorkspaceName;
	
	/**
	 * Package protected constructor
	 */
	GroupWorkspaceFileDeleteRecord(){}
	
	/**
	 * Default constructor
	 * 
	 * @param userId - id of the user deleting the file
	 * @param groupWorkspaceFileId - id of the workspace file
	 * @param GroupWorkspaceId - id of the workspace this file was deleted from
	 * @param groupWorkspaceName - name of the workspace
	 * @param fullPath - full path of the workspace file
	 * @param description - description of the file
	 */
	public GroupWorkspaceFileDeleteRecord(Long userId, 
			Long groupWorkspaceFileId, 
			Long groupWorkspaceId, 
			String groupWorkspaceName, 
			String fullPath, 
			String description)
	{
		setDateDeleted(new Timestamp(new Date().getTime()));
		setUserId(userId);
		setGroupWorkspaceFileId(groupWorkspaceFileId);
		setGroupWorkspaceId(groupWorkspaceId);
		setGroupWorkspaceName(groupWorkspaceName);
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
		value += groupWorkspaceFileId == null ? 0 : groupWorkspaceFileId.hashCode();
		return value;
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceFileDeleteRecord)) return false;

		final GroupWorkspaceFileDeleteRecord other = (GroupWorkspaceFileDeleteRecord) o;

		if( ( groupWorkspaceFileId != null && !groupWorkspaceFileId.equals(other.getGroupWorkspaceFileId()) ) ||
			( groupWorkspaceFileId == null && other.getGroupWorkspaceFileId() != null ) ) return false;


		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" user id = ");
		sb.append(userId);
		sb.append(" group workspace file id = " );
		sb.append(groupWorkspaceFileId);
		sb.append(" deleted date = ");
		sb.append(dateDeleted);
		sb.append(" full path = ");
		sb.append(fullPath);
		sb.append( " description = ");
		sb.append(description);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Reason the file was deleted
	 * 
	 * @return
	 */
	public String getDeleteReason() {
		return deleteReason;
	}

	/**
	 * Set the delete file reason.
	 * 
	 * @param deleteReason
	 */
	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}
	
	/**
	 * The id of the group workspace file
	 * @return
	 */
	public Long getGroupWorkspaceFileId() {
		return groupWorkspaceFileId;
	}

	/**
	 * Id of the group workspace the file was deleted from.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceId() {
		return groupWorkspaceId;
	}

	/**
	 * Name of the group workspace the file was deleted from.
	 * 
	 * @return
	 */
	public String getGroupWorkspaceName() {
		return groupWorkspaceName;
	}
	
	/**
	 * Set the group workspace file id.
	 * 
	 * @param groupWorkspaceFileId
	 */
	void setGroupWorkspaceFileId(Long groupWorkspaceFileId) {
		this.groupWorkspaceFileId = groupWorkspaceFileId;
	}

	/**
	 * Set the group workspace id.
	 * 
	 * @param groupWorkspaceId
	 */
	void setGroupWorkspaceId(Long groupWorkspaceId) {
		this.groupWorkspaceId = groupWorkspaceId;
	}

	/**
	 * Set the group workspace name.
	 * 
	 * @param groupWorkspaceName
	 */
	void setGroupWorkspaceName(String groupWorkspaceName) {
		this.groupWorkspaceName = groupWorkspaceName;
	}
}
