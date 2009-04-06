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

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileServerService;
import edu.ur.file.db.FileServer;

/**
 * Class for managing file server information.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageFileServers extends ActionSupport{

	/** eclipse generated id */
	private static final long serialVersionUID = -2322171837033103309L;
	
	/** Service for dealing with file servers */
	private FileServerService fileServerService;
	
	/** list of file servers */
	private List<FileServer> fileServers;
	
	/** id of a specific file server */
	private Long fileServerId;
	
	/** File Server for file access */
	private FileServer fileServer;

	/**
	 * Load the file servers.
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		return SUCCESS;
	}
	
	/**
	 * View a specific file server.
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
	public String view()
	{
		if( fileServerId != null )
		{
		    fileServer = fileServerService.getFileServer(fileServerId, false);
		}
		return "view";
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

}
