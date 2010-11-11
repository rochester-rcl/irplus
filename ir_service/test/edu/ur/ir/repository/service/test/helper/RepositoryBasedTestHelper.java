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

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

import org.springframework.context.ApplicationContext;

import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.DefaultFileDatabaseInfo;
import edu.ur.file.db.FileServer;
import edu.ur.file.db.FileServerService;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.service.DefaultFileServerService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.PersonalFileDeleteRecordDAO;
import edu.ur.ir.user.PersonalItemDeleteRecordDAO;


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
	private RepositoryService repositoryService;
	private DefaultFileServerService fileServerService;
    private PersonalFileDeleteRecordDAO personalFileDeleteRecordDAO;
    private PersonalItemDeleteRecordDAO personalItemDeleteRecordDAO;
	
	
	private Repository repository;
	private FileServer fileServer;
	
	
	
	public RepositoryBasedTestHelper(ApplicationContext ctx)
	{
		this.ctx = ctx;
		 repositoryService = (RepositoryService)ctx.getBean("repositoryService");
		 fileServerService = (DefaultFileServerService)ctx.getBean("fileServerService");
		 personalFileDeleteRecordDAO = (PersonalFileDeleteRecordDAO)ctx.getBean("personalFileDeleteRecordDAO");
		 personalItemDeleteRecordDAO = (PersonalItemDeleteRecordDAO)ctx.getBean("personalItemDeleteRecordDAO");
	}
	
    /**
     * Creates a repository ready for use in testing.
     * 
     * @return the created repository.
     * @throws LocationAlreadyExistsException 
     * @throws IOException 
     */
    public Repository createTestRepositoryDefaultFileServer(Properties properties) throws LocationAlreadyExistsException
    {
    	
    	String fileServerName = "localFileServer";
    	String fileDatabaseDisplayName =  "displayName";
    	String fileDatabaseUniqueName = "file_database"; 
    	String repoName = "my_repository"; 
    	
    	// location for the default file database
		String fileDatabasePath = properties.getProperty("a_repo_path");
		
		// location to store person name index
		String nameIndexFolder = properties.getProperty("name_index_folder");
		
		// location to store item index folder
		String itemIndexFolder = properties.getProperty("item_index_folder");
		
		// location to store user index folder
		String userIndexFolder = properties.getProperty("user_index_folder");
		
		// location to store user personal workspace index folders
		String userWorkspaceIndexFolder = properties.getProperty("user_workspace_index_folder");
		
		// location to store institutional collection index folders
		String institutionalCollectionIndexFolder = properties.getProperty("institutional_collection_index_folder");
		
		// create each of the folders
		File f = new File(nameIndexFolder);
		if( !f.exists() )
		{
			try {
				FileUtils.forceMkdir(f);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		
		f = new File(itemIndexFolder);
		if( !f.exists() )
		{
			try {
				FileUtils.forceMkdir(f);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		
		f = new File(userIndexFolder);
		if( !f.exists() )
		{
			try {
				FileUtils.forceMkdir(f);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		
		f = new File(userWorkspaceIndexFolder);
		if( !f.exists() )
		{
			try {
				FileUtils.forceMkdir(f);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		
		f = new File(institutionalCollectionIndexFolder);
		if( !f.exists() )
		{
			try {
				FileUtils.forceMkdir(f);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		
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
		repository.setNameIndexFolder(nameIndexFolder);
		
		// create the institutional item index folder
		repository.setInstitutionalItemIndexFolder(itemIndexFolder);
		
		//create the index folder for user information
		repository.setUserIndexFolder(userIndexFolder);
		
		//set the user workspace index folders location
		repository.setUserWorkspaceIndexFolder(userWorkspaceIndexFolder);

		//set the user workspace index folders location
		repository.setInstitutionalCollectionIndexFolder(institutionalCollectionIndexFolder);

		
		repositoryService.saveRepository(repository);
		
		return repository;
    }
    
    /**
     * Delete the repository and related information that was created for testing. 
     */
    public void cleanUpRepository()
    {
    	// delete all personal file delete records
    	personalFileDeleteRecordDAO.deleteAll();
    	
       	// delete all personal item delete records
    	personalItemDeleteRecordDAO.deleteAll();
    	
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
