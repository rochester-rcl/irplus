package edu.ur.ir.web.action.groupspace;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Allows a user to view and edit group workspace folder properties.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageGroupWorkspaceFolderPropertes extends ActionSupport 
implements  UserIdAware{

	/*eclipse generated id*/
	private static final long serialVersionUID = -5916633281950319826L;
	
	/* id of the user accessing the information */
	private Long userId;
	
	/* id of the group workspace folder */
	private Long groupWorkspaceFolderId;
	
	/* folder for the group workspace */
	private GroupWorkspaceFolder groupWorkspaceFolder;
	
	/* group workspace file system service */
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;


	/* security information */
	private SecurityService securityService;

	IrAcl folderAcl;


	public String execute()
	{
		if( groupWorkspaceFolderId != null )
		{
			groupWorkspaceFolder = groupWorkspaceFileSystemService.getFolder(groupWorkspaceFolderId, false);
		}
		
		folderAcl = securityService.getAcl(groupWorkspaceFolder);
		
		if( groupWorkspaceFolder != null )
		{
		    return SUCCESS;
	    }
		else
		{
			return "notFound";
		}
	}
	
	/**
	 * Get the folder access control entry.
	 * 
	 * @return
	 */
	public IrAcl getFolderAcl() {
		return folderAcl;
	}

	
	/**
	 * Set the security service.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	/**
	 * Get the group workspace folder id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceFolderId() {
		return groupWorkspaceFolderId;
	}

	/**
	 * Set the group workspace folder id.
	 * 
	 * @param groupWorkspaceFolderId
	 */
	public void setGroupWorkspaceFolderId(Long groupWorkspaceFolderId) {
		this.groupWorkspaceFolderId = groupWorkspaceFolderId;
	}

	/**
	 * Set the user id
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Get the group worskpace folder.
	 * 
	 * @return
	 */
	public GroupWorkspaceFolder getGroupWorkspaceFolder() {
		return groupWorkspaceFolder;
	}

	/**
	 * Set the group workspace folder.
	 * 
	 * @param groupWorkspaceFolder
	 */
	public void setGroupWorkspaceFolder(GroupWorkspaceFolder groupWorkspaceFolder) {
		this.groupWorkspaceFolder = groupWorkspaceFolder;
	}

	/**
	 * Set the group workspace file system service.
	 * 
	 * @param groupWorkspaceFileSystemService
	 */
	public void setGroupWorkspaceFileSystemService(
			GroupWorkspaceFileSystemService groupWorkspaceFileSystemService) {
		this.groupWorkspaceFileSystemService = groupWorkspaceFileSystemService;
	}

}
