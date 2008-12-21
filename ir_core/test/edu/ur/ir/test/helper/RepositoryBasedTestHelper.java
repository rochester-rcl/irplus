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

package edu.ur.ir.test.helper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.ur.file.db.DefaultFileDatabaseInfo;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileServer;
import edu.ur.file.db.FileServerService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;


public class RepositoryBasedTestHelper {
	/** Application context for loading bean specific information */
	ApplicationContext ctx = new ClassPathXmlApplicationContext(
			"applicationContext.xml");
	
	// initialize the repository
	RepositoryService repositoryService = (RepositoryService)ctx.getBean("repositoryService");
	FileServerService fileServerService = (FileServerService)ctx.getBean("fileServerService");

	
	private Repository repository;
	private FileServer fileServer;
	
    /**
     * Creates a repository ready for use in testing.
     * 
     * @return the created repository.
     */
    public Repository createRepository(String fileServerName, 
    		String displayName,
    		String folderSystemName, 
    		String repoName, 
    		String fileDatabasePath,
    		String defaultFolderDispalyName)
    {

		// create the file server
		fileServer = fileServerService.createFileServer(fileServerName);
		fileServer.setId(1l);

		// create the file database
		DefaultFileDatabaseInfo fileDatabaseInfo = new DefaultFileDatabaseInfo(fileServer.getId(),
				displayName, folderSystemName, fileDatabasePath, defaultFolderDispalyName, "uniqueFolderName");
		FileDatabase fileDatabase = fileServerService.createFileDatabase(fileDatabaseInfo);
		
		// create the repository
		repository = repositoryService.createRepository(repoName, fileDatabase);
		repository.setId(1l);
		
		return repository;
    }
    
    /**
     * Delete the repository and related information that was created for testing. 
     */
    public void cleanUpRepository()
    {
		// delete the repository
		repositoryService.deleteRepository(repository);
		
		// delete the file server
		fileServerService.deleteFileServer(fileServer);
    }
 
}
