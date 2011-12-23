package edu.ur.ir.web.action.groupspace;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Rename the group workspace file.
 * 
 * @author Nathan Sarr
 *
 */
public class RenameGroupWorkspaceFile extends ActionSupport implements UserIdAware{

	/* Eclipse generated Id	 */
	private static final long serialVersionUID = -7262077243101493697L;
	
	/* User file system service. */
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;
	
	/*  Indicates the file has been renamed */
	private boolean fileRenamed = false;
	
	/* Message that can be displayed to the user. */
	private String renameMessage;
	
	/* New file name */
	private String newFileName;
	
	/* description of the file */
	private String fileDescription;
	
	/*  Logger for action */
	private static final Logger log = Logger.getLogger(RenameGroupWorkspaceFile.class);
	
	/* Id of group workspace file */
	private Long groupWorkspaceFileId;
	
	/* id of the user trying to make the change */
	private Long userId;
	
	/* user service */
	private UserService userService;
	
	/* service to deal with security */
	private SecurityService securityService;


	/**
	 * Renames a file
	 * 
	 * @return
	 */
	public String rename() {
		
		log.debug("Rename group workspace file::" + groupWorkspaceFileId + " file name = " + newFileName);
		
		GroupWorkspaceFile groupWorkspaceFile = groupWorkspaceFileSystemService.getFile(groupWorkspaceFileId, false);
		IrUser user = userService.getUser(userId, false);
		
		if( !securityService.hasPermission(groupWorkspaceFile.getVersionedFile(), user, VersionedFile.EDIT_PERMISSION))
		{
			return "accessDenied";
		}
	
		
		groupWorkspaceFile.getVersionedFile().setDescription(fileDescription);
		
		// make sure there is a name change
		if( !groupWorkspaceFile.getVersionedFile().getNameWithExtension().equals(newFileName))
		{
			log.debug("renaming file:" + groupWorkspaceFileId + " file name = " + newFileName);
			boolean fileExists = false;
			GroupWorkspaceFolder folder = groupWorkspaceFile.getGroupWorkspaceFolder();
			if( folder  == null )
			{
				GroupWorkspace workspace = groupWorkspaceFile.getGroupWorkspace();
				if( workspace.getRootGroupFile(newFileName) != null )
				{
					fileExists = true;
				}
			}
			else
			{
				if( folder.getFile(newFileName) != null )
				{
					fileExists = true;
				}
			}
			
		    if (!fileExists) {
			    try {
				    groupWorkspaceFile.getVersionedFile().reName(newFileName);
			    } catch (IllegalFileSystemNameException e) {
				    renameMessage = getText("illegalNameError", new String[]{e.getName(), String.valueOf(e.getIllegalCharacters())});
				    addFieldError("illegalNameError", renameMessage);
			    }
			    groupWorkspaceFileSystemService.save(groupWorkspaceFile);
			    fileRenamed = true;
				
		    } else {
			    addFieldError("renameFileMessage", "File name " + newFileName + " already exists ");
			    
		    }
		}
		else
		{
			fileRenamed = true;
			// assume description change
			groupWorkspaceFileSystemService.save(groupWorkspaceFile);
		}
		
		return SUCCESS;
	}
	
	/*


	/**
	 * Get personal file name
	 * 
	 * @return
	 */
	public String get() {
		GroupWorkspaceFile groupWorkspaceFile = groupWorkspaceFileSystemService.getFile(groupWorkspaceFileId, false);
		newFileName = groupWorkspaceFile.getVersionedFile().getNameWithExtension();
		fileDescription = groupWorkspaceFile.getVersionedFile().getDescription();
		return "get";
	}


	/**
	 * Set User file system service
	 * 
	 * @param userFileSystemService
	 */
	public void setGroupWorkspaceFileSystemService(GroupWorkspaceFileSystemService groupWorkspaceFileSystemService) {
		this.groupWorkspaceFileSystemService = groupWorkspaceFileSystemService;
	}

	/**
	 * Get rename message
	 * 
	 * @return
	 */
	public String getRenameMessage() {
		return renameMessage;
	}

	/**
	 * Set rename message
	 * 
	 * @param renameMessage
	 */
	public void setRenameMessage(String renameMessage) {
		this.renameMessage = renameMessage;
	}

	/**
	 * Get personal file id
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceFileId() {
		return groupWorkspaceFileId;
	}

	/**
	 * Set group workspace file id
	 * 
	 * @param groupWorkspaceFileId
	 */
	public void setGroupWorkspaceFileId(Long groupWorkspaceFileId) {
		this.groupWorkspaceFileId = groupWorkspaceFileId;
	}

	/**
	 * Get whether file is renamed or not
	 * 
	 * @return
	 */
	public boolean isFileRenamed() {
		return fileRenamed;
	}

	/**
	 * Set whether file is renamed or not
	 * 
	 * @param fileRenamed
	 */
	public void setFileRenamed(boolean fileRenamed) {
		this.fileRenamed = fileRenamed;
	}

	/**
	 * Get new file name
	 * 
	 * @return
	 */
	public String getNewFileName() {
		return newFileName;
	}

	/**
	 * Set new file name
	 * 
	 * @param newFileName
	 */
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}

	
	public void injectUserId(Long userId) {
		this.userId = userId;
	}

	public String getFileDescription() {
		return fileDescription;
	}

	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}

	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Set the security service.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
}
