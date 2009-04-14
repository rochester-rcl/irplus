package edu.ur.ir.web.action.file.storage;

import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileServer;
import edu.ur.file.db.FileServerService;
import edu.ur.file.db.FolderInfo;
import edu.ur.file.db.LocationAlreadyExistsException;

/**
 * Class for managing file database information.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageFileDatabase extends ActionSupport implements Preparable{

	
	/** Eclipse generated id  */
	private static final long serialVersionUID = -1712815183938029916L;

	/**  Logger for managing content types*/
	private static final Logger log = Logger.getLogger(ManageFileDatabase.class);
	
	/** Service for dealing with file servers */
	private FileServerService fileServerService;
	
	/** id of a specific file server */
	private Long fileServerId;
	
	/** name for the database  */
	private String name;
	
	/** Path for the database */
	private String path;
	
	/** id of the file database */
	private Long fileDatabaseId;

	/** description of the file server */
	private String description;
	
	/** File Server for file access */
	private FileServer fileServer;
	
	/** File database  */
	private FileDatabase fileDatabase;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the file database has been added*/
	private boolean added = false;
	
	/**  Indicates the file database has been deleted*/
	private boolean deleted = false;
	
	public String get()
	{
		return "get";
	}
	

	/**
	 * View a specific file database.
	 * 
	 * @return view if the database exists
	 */
	public String view()
	{
		return "view";
	}
	
	/**
	 * View a specific file database for a file server.
	 * 
	 * @return Success
	 */
	public String getAll()
	{
		return "getAll";
	}
	
	/**
	 * Delete a specific file server.
	 * 
	 * @return Success
	 */
	public String update()
	{
		log.debug("update called");
		added = true;
		if( fileDatabase != null)
		{
		   fileDatabase.setDescription(description);
		   fileServerService.saveFileServer(fileServer); 
		}
		return "update";
	}
	
	/**
	 * Delete a specific file server.
	 * 
	 * @return Success
	 */
	@SuppressWarnings("unchecked")
	public String delete()
	{
		log.debug("delete called");
		if( fileDatabase != null)
		{
			deleted = true;
		   
			Set<FolderInfo> folders = (Set<FolderInfo>)fileDatabase.getRootFolders();
		    
			// only delete if there are no folders - otherwise a user could delete all of 
			// their files
		    if(folders == null || folders.size() == 0)
		    {
		        fileServer.deleteDatabase(fileDatabase.getName());
		        fileServerService.saveFileServer(fileServer); 
		    }
		}
		return "deleted";
	}
	
	/**
	 * Create a specified file server.
	 * 
	 * @return
	 */
	public String create()
	{
		
		log.debug("creating a file database = " + name + " path = " + path);
		added = false;
		FileDatabase other = fileServerService.getDatabaseByName(fileServerId, name);
		if( other == null)
		{
		    try {
				fileDatabase = fileServer.createFileDatabase(name, name, path, description);
				 fileServerService.saveFileServer(fileServer);
				 added = true;
			} catch (LocationAlreadyExistsException e) {
				message = getText("folderLocationAlreadyExists", 
						new String[]{fileServer.getName()});
				addFieldError("folderLocationAlreadyExists", message);
			}
		   
		}
		else
		{
			message = getText("fileDatabaseNameError", 
					new String[]{fileServer.getName()});
			addFieldError("fileDatabaseAlreadyExists", message);
		}
        return "added";
	}
	

	public FileServerService getFileServerService() {
		return fileServerService;
	}


	public void setFileServerService(FileServerService fileServerService) {
		this.fileServerService = fileServerService;
	}
	
	public Long getFileServerId() {
		return fileServerId;
	}

	public void setFileServerId(Long fileServerId) {
		this.fileServerId = fileServerId;
	}
	
	public FileServer getFileServer() {
		return fileServer;
	}

	public void setFileServer(FileServer fileServer) {
		this.fileServer = fileServer;
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

	public void prepare() throws Exception {
		log.debug("perpare called fileServerId = " + fileServerId + " fileDatabaseId = " + fileDatabaseId);
		if( fileServerId != null)
		{
			fileServer = fileServerService.getFileServer(fileServerId, false);
			if(fileDatabaseId != null)
			{
				fileDatabase = fileServer.getFileDatabase(fileDatabaseId);
			}
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public Long getFileDatabaseId() {
		return fileDatabaseId;
	}

	public void setFileDatabaseId(Long fileDatabaseId) {
		this.fileDatabaseId = fileDatabaseId;
	}
	
	public FileDatabase getFileDatabase() {
		return fileDatabase;
	}

	public void setFileDatabase(FileDatabase fileDatabase) {
		this.fileDatabase = fileDatabase;
	}

	
}
