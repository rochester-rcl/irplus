package edu.ur.ir.web.action.groupspace;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFile;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.researcher.DownloadResearcherFile;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Download of the group workspace project page file.
 * 
 * @author Nathan Sarr
 *
 */
public class DownloadGroupWorkspaceProjectPageFile extends ActionSupport 
implements ServletResponseAware, ServletRequestAware, UserIdAware
{
	/** eclipse generated id  */
	private static final long serialVersionUID = -2078297656328960623L;

	// Service for dealing with researcher file system information 
	private GroupWorkspaceProjectPageFileSystemService groupWorkspaceProjectPageFileSystemService;

	//  Logger for file upload */
	private static final Logger log = Logger.getLogger(DownloadResearcherFile.class);
	
	//  File to download */
	private Long groupWorkspaceProjectPageFileId;

	// Servlet response to write to */
	private transient HttpServletResponse response;
	
	//  Servlet request made */
	private transient HttpServletRequest request;
	
	// id of the user downloading the file**/
	private Long userId;

	private WebIoUtils webIoUtils;
	
	private UserService userService;

	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
	    log.debug("Trying to download the file to user");
	    
        if (groupWorkspaceProjectPageFileId == null) {
        	log.debug("File  id is null");
        	return "notFound";
        }
        
        GroupWorkspaceProjectPageFile projectPageFile = 
        	groupWorkspaceProjectPageFileSystemService.getFile(groupWorkspaceProjectPageFileId, false);        
        
        if( projectPageFile == null )
        {
        	return "notFound";
        }
        
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = projectPageFile.getGroupWorkspaceProjectPage();
        IrUser user = userService.getUser(userId, false);
        
        if( !groupWorkspaceProjectPage.isPublic() && user == null )
        {
        	return "accessDenied";
        }
        
        GroupWorkspaceUser groupWorkspaceUser = groupWorkspaceProjectPage.getGroupWorkspace().getUser(user);
        
        if( !groupWorkspaceProjectPage.isPublic() && groupWorkspaceUser == null )
        {
        	return "accessDenied";
        }
        
        if( groupWorkspaceProjectPage.getPagePublic() || groupWorkspaceUser.isOwner())
        {	
            FileInfo fileInfo =  projectPageFile.getIrFile().getFileInfo();
            webIoUtils.streamFileInfo(projectPageFile.getName(), fileInfo, response, request, (1024*4), false, true);
            return SUCCESS;
        }
        else
        {
        	return "accessDenied";
        }
    }	

	/* (non-Javadoc)
	 * @see com.opensymphony.webwork.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}
	
	/**
	 * Get the user id.
	 * 
	 * @param userId
	 */
	public void injectUserId(Long userId)
	{
		this.userId = userId;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Set the web io utils.
	 * 
	 * @param webIoUtils
	 */
	public void setWebIoUtils(WebIoUtils webIoUtils) {
		this.webIoUtils = webIoUtils;
	}

	/**
	 * Get the group workspace project page file id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceProjectPageFileId() {
		return groupWorkspaceProjectPageFileId;
	}

	/**
	 * Set the group workspace project page file id.
	 * 
	 * @param groupWorkspaceProjectPageFileId
	 */
	public void setGroupWorkspaceProjectPageFileId(
			Long groupWorkspaceProjectPageFileId) {
		this.groupWorkspaceProjectPageFileId = groupWorkspaceProjectPageFileId;
	}

	/**
	 * Set the group workspace project page file system serivce.
	 * 
	 * @param groupWorkspaceProjectPageFileSystemService
	 */
	public void setGroupWorkspaceProjectPageFileSystemService(
			GroupWorkspaceProjectPageFileSystemService groupWorkspaceProjectPageFileSystemService) {
		this.groupWorkspaceProjectPageFileSystemService = groupWorkspaceProjectPageFileSystemService;
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
