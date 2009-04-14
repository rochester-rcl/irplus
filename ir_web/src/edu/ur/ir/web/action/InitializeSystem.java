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


package edu.ur.ir.web.action;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.Validateable;

import edu.ur.file.db.DefaultFileDatabaseInfo;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileServer;
import edu.ur.file.db.FileServerService;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.service.DefaultFileServerService;

import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserIndexService;
import edu.ur.ir.user.UserService;



/**
 * This allows users to initialize the repository from the web application.
 * 
 * @author Nathan Sarr
 *
 */
public class InitializeSystem extends ActionSupport implements Preparable, Validateable{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = 6451749242717873696L;

	/**  Logger for editing repository information. */
	private static final Logger log = Logger.getLogger(InitializeSystem.class);
	
	/**  Repository service for accessing repository information */
	private RepositoryService repositoryService;
	
	/** Service for dealing with user information  */
	private UserService userService;
	
	/** Service for dealing with user information  */
	private UserIndexService userIndexService;
	
	/** Service for accessing files */
	private DefaultFileServerService fileServerService;
	
	/** Unique name generation  */
	private UniqueNameGenerator uniqueNameGenerator;
	
    /** Repository for the system */
    private Repository repository;
    
	/**  Location of where to start creating files  */
	private String fileLocation;
	
	/**  Name of the repository to create */
	private String repositoryName;
	
	/** Name of the index folder where the person name indexes are stored */
	public final String NAMES_INDEX_FOLDER = "name_index_folder";
	
	/** Name of the index folder where the person name indexes are stored */
	public final String USER_INDEX_FOLDER = "user_index_folder";
	
	/** folder where an index of institutional items will be stored */
	public final String INSTITUTIONAL_ITEM_INDEX_FOLDER = "institutional_item_index_folder";

	/** folder where an index of researchers will be stored */
	public final String RESEARCHER_INDEX_FOLDER = "researcher_index_folder";

	/**
	 * Prepare the ur published object
	 * 
	 * @see com.opensymphony.xwork.Preparable#prepare()
	 */
	public void prepare() throws Exception{
		log.debug("prepare called");
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
	}
	
	/**
	 * Save the urPublished
	 * 
	 * @return
	 * @throws NoIndexFoundException 
	 * @throws LocationAlreadyExistsException 
	 */
	public String save() throws NoIndexFoundException, LocationAlreadyExistsException {
		
		log.debug("Initalizing system with name " + repositoryName + 
				" fileLocation " + fileLocation);
		
		String fileServerName = repositoryName.trim().replace(' ', '_') + "_file_server";
		
		// create the file server
		FileServer fileServer = fileServerService.createFileServer(fileServerName);
		
		String fileDatabaseUniqueName = repositoryName.trim().replace(' ', '_');
		String fileDatabaseDisplayName = repositoryName + " File Database";
		String fileDatabasePath = fileLocation;
		String defaultFolderDisplayName = "default_repository_folder";
		
		if( uniqueNameGenerator == null )
		{
			throw new RuntimeException("UNIQUE NAME NOT FOUND!!!");
		}

		// create the file database
		DefaultFileDatabaseInfo fileDatabaseInfo = new DefaultFileDatabaseInfo(fileServer.getId(),
				fileDatabaseDisplayName,
				fileDatabaseUniqueName, 
				fileDatabasePath, 
				defaultFolderDisplayName, 
				uniqueNameGenerator.getNextName());
		
		FileDatabase fileDatabase = fileServerService.createFileDatabase(fileDatabaseInfo);
		
		repository = repositoryService.createRepository(repositoryName, fileDatabase);

		// Creating the name index folder
		repository.setNameIndexFolder(repositoryService.createFolderInfo(repository, NAMES_INDEX_FOLDER));
		repository.setUserIndexFolder(repositoryService.createFolderInfo(repository, USER_INDEX_FOLDER));
		repository.setInstitutionalItemIndexFolder(repositoryService.createFolderInfo(repository, INSTITUTIONAL_ITEM_INDEX_FOLDER));
		repository.setResearcherIndexFolder(repositoryService.createFolderInfo(repository, RESEARCHER_INDEX_FOLDER));
		repositoryService.saveRepository(repository);
		
		// handle any pre-loaded users
		List<IrUser> users = userService.getAllUsers();		
		File userIndexFolder = new File(repository.getUserIndexFolder().getFullPath());
		for( IrUser user : users)
		{
			userIndexService.addToIndex(user, userIndexFolder);
		}
		
		return SUCCESS;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public FileServerService getFileServerService() {
		return fileServerService;
	}

	public void setFileServerService(FileServerService fileServerService) {
		this.fileServerService = (DefaultFileServerService)fileServerService;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public UniqueNameGenerator getUniqueNameGenerator() {
		return uniqueNameGenerator;
	}

	public void setUniqueNameGenerator(UniqueNameGenerator uniqueNameGenerator) {
		this.uniqueNameGenerator = uniqueNameGenerator;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserIndexService getUserIndexService() {
		return userIndexService;
	}

	public void setUserIndexService(UserIndexService userIndexService) {
		this.userIndexService = userIndexService;
	}

	public void setFileServerService(DefaultFileServerService fileServerService) {
		this.fileServerService = fileServerService;
	}
}