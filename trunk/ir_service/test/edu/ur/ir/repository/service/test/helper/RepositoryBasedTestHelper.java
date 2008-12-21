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


package edu.ur.ir.repository.service.test.helper;

import java.util.Properties;

import org.springframework.context.ApplicationContext;

import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.DefaultFileDatabaseInfo;
import edu.ur.file.db.FileServer;
import edu.ur.file.db.FileServerService;
import edu.ur.file.db.service.DefaultFileServerService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;


/**
 * Helper to deal with creating and removing a repository
 * 
 * @author Nathan Sarr
 *
 */
public class RepositoryBasedTestHelper {
	/** Application context for loading bean specific information */
	ApplicationContext ctx;
	
	// initialize the repository
	RepositoryService repositoryService;
	DefaultFileServerService fileServerService;

	
	private Repository repository;
	private FileServer fileServer;
	
	public RepositoryBasedTestHelper(ApplicationContext ctx)
	{
		this.ctx = ctx;
		 repositoryService = (RepositoryService)ctx.getBean("repositoryService");
		 fileServerService = (DefaultFileServerService)ctx.getBean("fileServerService");
	}
	
    /**
     * Creates a repository ready for use in testing.
     * 
     * @return the created repository.
     */
    public Repository createTestRepositoryDefaultFileServer(Properties properties)
    {
    	
    	String fileServerName = "localFileServer";
    	String fileDatabaseDisplayName =  "displayName";
    	String fileDatabaseUniqueName = "file_database"; 
    	String repoName = "my_repository"; 
		String fileDatabasePath =properties.getProperty("a_repo_path");
		String defaultFolderDispalyName = "default_folder";

		// create the file server
		fileServer = fileServerService.createFileServer(fileServerName);

		// create the file database
		DefaultFileDatabaseInfo fileDatabaseInfo = new DefaultFileDatabaseInfo(fileServer.getId(),
				fileDatabaseDisplayName,
				fileDatabaseUniqueName, 
				fileDatabasePath, 
				defaultFolderDispalyName, 
				"uniqueFolderName");
		
		DefaultFileDatabase fileDatabase = fileServerService.createFileDatabase(fileDatabaseInfo);
		repository = repositoryService.createRepository(repoName, fileDatabase);
		
		// Creating the name index folder
		repository.setNameIndexFolder(repositoryService.createFolderInfo(repository, "name_index_folder"));
		
		// create the institutional item index folder
		repository.setInstitutionalItemIndexFolder(repositoryService.createFolderInfo(repository, "institutional_item_index_folder"));
		
		repositoryService.saveRepository(repository);
		
		return repository;
    }
    
    /**
     * Delete the repository and related information that was created for testing. 
     */
    public void cleanUpRepository()
    {
		// delete the repository
		repositoryService.deleteRepository(repositoryService.getRepository(repository.getId(), false));
		
		// delete the file server
		fileServerService.deleteFileServer(fileServerService.getFileServer(fileServer.getId(), false));
    }

	public FileServerService getFileServerService() {
		return fileServerService;
	}

	public Repository getRepository() {
		return repositoryService.getRepository(repository.getId(), false);
	}

	public FileServer getFileServer() {
		return fileServer;
	}




 
}
