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

package edu.ur.ir.web.action.file.storage;

import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;


import edu.ur.file.db.FileServerService;
import edu.ur.file.db.FileServer;

/**
 * Class for managing file server information.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageFileServers extends ActionSupport implements Preparable{

	/** eclipse generated id */
	private static final long serialVersionUID = -2322171837033103309L;
	
	/**  Logger for managing content types*/
	private static final Logger log = Logger.getLogger(ManageFileServers.class);
	
	/** Service for dealing with file servers */
	private FileServerService fileServerService;
	
	/** list of file servers */
	private List<FileServer> fileServers;
	
	/** id of a specific file server */
	private Long fileServerId;
	
	/** name of the file server */
	private String name;

	/** description of the file server */
	private String description;
	
	/** File Server for file access */
	private FileServer fileServer;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the file server has been added*/
	private boolean added = false;
	
	/**  Indicates the file server has been deleted*/
	private boolean deleted = false;
	



	/**
	 * Load the file servers.
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		log.debug("execute called");
		return SUCCESS;
	}
	
	/**
	 * View all file servers.
	 * 
	 * @return Success
	 */
	@SuppressWarnings("unchecked")
	public String getAll()
	{
		fileServers = fileServerService.getAllFileServers();
		return "getAll";
	}
	
	/**
	 * View a specific file server.
	 * 
	 * @return Success
	 */
	public String get()
	{
		if( fileServerId != null )
		{
		    fileServer = fileServerService.getFileServer(fileServerId, false);
		}
		return "get";
	}
	
	/**
	 * Delete a specific file server.
	 * 
	 * @return Success
	 */
	public String delete()
	{
		if( fileServerId != null )
		{
			deleted = true;
		    fileServer = fileServerService.getFileServer(fileServerId, false);
		    
		    // only delete if no file databases - otherwise user could delete
		    // all of their files - which could be very bad
		    if( fileServer.getFileDatabases() == null || fileServer.getFileDatabases().size() == 0)
		    {
		        fileServerService.deleteFileServer(fileServer);
		    }
		}
		return "deleted";
	}
	
	/**
	 * View a specific file server.
	 * 
	 * @return Success
	 */
	public String view()
	{
		if( fileServerId != null )
		{
		    fileServer = fileServerService.getFileServer(fileServerId, false);
		}
		return "view";
	}
	
	/**
	 * Create a specified file server.
	 * 
	 * @return
	 */
	public String create()
	{
		log.debug("creating a file server = " + name);
		added = false;
		FileServer other = fileServerService.getFileServer(name);
		if( other == null)
		{
		    fileServer = fileServerService.createFileServer(name);
		    fileServer.setDescription(description);
		    fileServerService.saveFileServer(fileServer);
		    added = true;
		}
		else
		{
			message = getText("fileServerNameError", 
					new String[]{fileServer.getName()});
			addFieldError("fileServerAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Update the file server with new information.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing file server id = " + fileServer.getId());
		added = false;

		FileServer other = fileServerService.getFileServer(name);
		
		if( other == null || other.getId().equals(fileServer.getId()))
		{
			fileServer.setName(name);
			fileServer.setDescription(description);
			fileServerService.saveFileServer(fileServer);
			added = true;
		}
		else
		{
			message = getText("fileServerNameError", 
					new String[]{fileServer.getName()});
			
			addFieldError("fileServerAlreadyExists", message);
		}
        return "added";
	}
	
	
	public FileServerService getFileServerService() {
		return fileServerService;
	}


	public void setFileServerService(FileServerService fileServerService) {
		this.fileServerService = fileServerService;
	}
	
	public List<FileServer> getFileServers() {
		return fileServers;
	}

	public void setFileServers(List<FileServer> fileServers) {
		this.fileServers = fileServers;
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
		if( fileServerId != null)
		{
			fileServer = fileServerService.getFileServer(fileServerId, false);
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

	
}
