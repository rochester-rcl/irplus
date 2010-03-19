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

package edu.ur.hibernate.ir.test.helper;

import org.springframework.context.ApplicationContext;

import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.DefaultFileDatabaseInfo;
import edu.ur.file.db.FileServer;
import edu.ur.file.db.FileServerService;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.service.DefaultFileServerService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryDAO;


/**
 * Helper to deal with creating 
 * 
 * @author Nathan Sarr
 *
 */
public class RepositoryBasedTestHelper {
	
	// initialize the repository
	RepositoryDAO repositoryDAO;
	DefaultFileServerService fileServerService;

	
	private Repository repository;
	private FileServer fileServer;
	
	public RepositoryBasedTestHelper(ApplicationContext ctx)
	{
		 repositoryDAO = (RepositoryDAO)ctx.getBean("repositoryDAO");
		 fileServerService = (DefaultFileServerService)ctx.getBean("fileServerService");
	}
	
    /**
     * Creates a repository ready for use in testing.
     * 
     * @return the created repository.
     * @throws LocationAlreadyExistsException 
     */
    public Repository createRepository(String fileServerName, 
    		String fileDatabaseDisplayName,
    		String fileDatabaseUniqueName, 
    		String repoName, 
    		String fileDatabasePath,
    		String defaultFolderDispalyName) throws LocationAlreadyExistsException
    {

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
		
		
		repository = new Repository(repoName, fileDatabase);
		repositoryDAO.makePersistent(repository);
		return repository;
    }
    
    /**
     * Delete the repository and related information that was created for testing. 
     */
    public void cleanUpRepository()
    {
		// delete the repository
		repositoryDAO.makeTransient(repositoryDAO.getById(repository.getId(), false));
		
		// delete the file server
		fileServerService.deleteFileServer(fileServerService.getFileServer(fileServer.getId(), false));
    }

	public FileServerService getFileServerService() {
		return fileServerService;
	}

	public Repository getRepository() {
		return repositoryDAO.getById(repository.getId(), false);
	}

	public FileServer getFileServer() {
		return fileServer;
	}


	public RepositoryDAO getRepositoryDAO() {
		return repositoryDAO;
	}

 
}
