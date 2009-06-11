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

package edu.ur.ir.web.action.repository;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.Validateable;

import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileServerService;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.handle.HandleService;
import edu.ur.ir.handle.HandleNameAuthority;
import edu.ur.ir.repository.LicenseService;
import edu.ur.ir.repository.LicenseVersion;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryIndexerService;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserIndexService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


/**
 * View/Edit repository actions.
 *  
 * @author Nathan Sarr
 *
 */
public class EditRepository extends ActionSupport implements Preparable, 
Validateable, UserIdAware{
	
	/**  Logger for editing a file database. */
	private static final Logger log = Logger.getLogger(EditRepository.class);
	
	/**  Name of the repository */
	private String repositoryName;
	
	/**  Name of the institution */
	private String institutionName;
	
	/**  Generated version id */
	private static final long serialVersionUID = -6421997690248407461L;
	
	/**  The new repository */
	private Repository repository;

	/** Service for dealing with repository */
	private RepositoryService repositoryService;
	
	/** Delete the picture from the repository */
	private Long irFilePictureId;
	
	/** id of the name authority to set on the repository */
	private Long handleNameAuthorityId;
	
	/** default id for file databases */
	private Long defaultFileDatabaseId;
	
	/** location of the name index folder  */
	private String nameIndexFolder;
	
	/** location of the institutional item index folder */
	private String institutionalItemIndexFolder;
	
	/** location of the researcher index folder. */
	private String researcherIndexFolder;
	
	/** user index folder */
	private String userIndexFolder;
	
	/** location for user workspace folders  */
	private String userWorkspaceIndexFolder;
	
	/** Service for re-indexing repository information */
	private RepositoryIndexerService repositoryIndexerService;
	
	/** Service for dealing with handle information  */
	private HandleService handleService;
	
	/** Service for file server/database information */
	private FileServerService fileServerService;
	
	/** Set of handle name authorities in the system  */
	private List<HandleNameAuthority> handleNameAuthorities;
	
	/** List of available file databases  */
	private List<FileDatabase> fileDatabases;
	
	/** Service for dealing with user information  */
	private UserService userService;
	
	/** Service for dealing with user information  */
	private UserIndexService userIndexService;
	
	/** batch size for re-indexing repository information - number of records to process*/
	private int batchSize = 10;
	
	/** id of the default versioned license for the repository */
	private Long defaultLicenseVersionId;
	
	/** indicates that subscriptions should be suspended  */
	private boolean suspendSubscriptions = false;
	
	/** Service for dealing with license information  */
	private LicenseService licenseService;
	
	/** Set of licenses */
	private List<LicenseVersion> licenses;
	
	private Long userId;
	
	/**
	 * Prepare the repository.
	 * 
	 * @see com.opensymphony.xwork.Preparable#prepare()
	 */
	public void prepare() throws Exception{
		log.debug("prepare called");
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		log.debug("repository " + repository + " found ");
		handleNameAuthorities = handleService.getAllNameAuthorities();
		fileDatabases = fileServerService.getFileDatabases();
		if( repository != null )
		{
		    licenses = repositoryService.getAvailableRepositoryLicenses(repository.getId());
		}
		else
		{
			licenses = licenseService.getAllLicenseVersions();
		}
	}
	
	/**
	 * Create a new repository.
	 * 
	 * @return
	 * @throws NoIndexFoundException 
	 */
	public String create() throws NoIndexFoundException
	{
		log.debug("Create called");
		FileDatabase fileDatabase = null;
		IrUser user = userService.getUser(userId, false);
		if( defaultFileDatabaseId != -1l  )
		{
			fileDatabase = fileServerService.getDatabaseById(defaultFileDatabaseId, false);
		}
		
		if( handleNameAuthorityId != -1l )
		{
		    HandleNameAuthority authority = handleService.getNameAuthority(handleNameAuthorityId, false);
		    repository.setDefaultHandleNameAuthority(authority);
		}
		
		if( defaultLicenseVersionId  != -1l )
		{
			LicenseVersion defaultLicense = licenseService.getLicenseVersion(defaultLicenseVersionId, false);
			repository.updateDefaultLicense(user, defaultLicense);
			
		}
		
		repository = repositoryService.createRepository(repositoryName, fileDatabase);
		repository.setInstitutionName(institutionName);
		Timestamp lastEmailDate = new Timestamp(new Date().getTime());
		repository.setLastSubscriptionProcessEmailDate(lastEmailDate);
		repository.setSuspendSuscriptionEmails(suspendSubscriptions);
		
		
		try {
			updateNameIndexFolder(repository, nameIndexFolder);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			updateUserWorkspaceIndexFolder(repository, userWorkspaceIndexFolder);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			updateInstitutionalItemIndexFolder(repository, institutionalItemIndexFolder);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			updateResearcherIndexFolder(repository, researcherIndexFolder);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			updateUserIndexFolder(repository, userIndexFolder);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
		if(!repository.getInitalized())
		{
			repository.setInitalized(true);
		}
		
		repositoryService.saveRepository(repository);
		
		return SUCCESS;
		
	}
	
	/**
	 * Update the repository
	 * 
	 * @return
	 */
	public String update() {
		log.debug("update called");
		if (repository == null) {
			throw new IllegalStateException("repository is null");
		}
		repository.setName(repositoryName);
		repository.setInstitutionName(institutionName);
		IrUser user = userService.getUser(userId, false);
		if(repositoryService == null )
		{
			throw new IllegalStateException ("repository service is null");
		}
		
		repository.setSuspendSuscriptionEmails(suspendSubscriptions);
		
		if( handleNameAuthorityId == -1l  )
		{
			repository.setDefaultHandleNameAuthority(null);
		}
		else
		{
			HandleNameAuthority repositoryHandleAuthority = repository.getDefaultHandleNameAuthority();
			
			// the handle name authority is being changed
			if( repositoryHandleAuthority == null || 
				!repositoryHandleAuthority.getId().equals(handleNameAuthorityId) )
			{
			    HandleNameAuthority authority = handleService.getNameAuthority(handleNameAuthorityId, false);
			    repository.setDefaultHandleNameAuthority(authority);
			}
		}
		
		log.debug("Default file database id = " + defaultFileDatabaseId);
		if( defaultFileDatabaseId == -1l  )
		{
			repository.setFileDatabase(null);
		}
		else
		{
			FileDatabase repositoryFileDatabase = repository.getFileDatabase();
			
			// the handle name authority is being changed
			if( repositoryFileDatabase == null || 
				!repositoryFileDatabase.getId().equals(defaultFileDatabaseId) )
			{
				FileDatabase fileDatabase = fileServerService.getDatabaseById(defaultFileDatabaseId, false);
			    repository.setFileDatabase(fileDatabase);
			}
		}
		
		log.debug("Default license version id = " + defaultLicenseVersionId);
		if( defaultLicenseVersionId == -1l  )
		{
			repository.updateDefaultLicense(user, null);
		}
		else
		{
			LicenseVersion defaultLicense = repository.getDefaultLicense();
			
			// the handle name authority is being changed
			if( defaultLicense == null || 
				!defaultLicense.getId().equals(defaultLicenseVersionId) )
			{
				LicenseVersion newDefaultLicense = licenseService.getLicenseVersion(defaultLicenseVersionId, false);
			    repository.updateDefaultLicense(user, newDefaultLicense);
			}
		}
		
		
		try {
			updateNameIndexFolder(repository, nameIndexFolder);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			updateUserWorkspaceIndexFolder(repository, userWorkspaceIndexFolder);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			updateInstitutionalItemIndexFolder(repository, institutionalItemIndexFolder);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			updateResearcherIndexFolder(repository, researcherIndexFolder);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			updateUserIndexFolder(repository, userIndexFolder);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoIndexFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if( log.isDebugEnabled())
		{
		    log.debug("Saving repository " + repository);
		}

			
		repositoryService.saveRepository(repository);
		return SUCCESS;
	}
	
	/**
	 * Delete the picture from the repository.
	 * 
	 * @return
	 */
	public String deletePicture()
	{
		IrFile file = repository.getPicture(irFilePictureId);
		repositoryService.deleteRepositoryPicture(repository, file);
		return SUCCESS;
	}
	
	/**
	 * Cancel called
	 * 
	 * @return cancel
	 */
	public String cancel() {
		return SUCCESS;
	}

	/**
	 * The repository being edited.
	 * 
	 * @return
	 */
	public Repository getRepository() {
		return repository;
	}
	

	/**
	 * Set the file server to create
	 * 
	 * @param fileServer
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	/**
	 * Validate the repository information.
	 * 
	 * @see com.opensymphony.xwork.ActionSupport#validate()
	 */
	public void validate()
	{
		if( repository == null )
		{
			throw new IllegalStateException("repository cannot be null");
		}
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public Long getIrFilePictureId() {
		return irFilePictureId;
	}

	public void setIrFilePictureId(Long irFilePictureId) {
		this.irFilePictureId = irFilePictureId;
	}

	public RepositoryIndexerService getRepositoryIndexerService() {
		return repositoryIndexerService;
	}

	public void setRepositoryIndexerService(
			RepositoryIndexerService repositoryIndexerService) {
		this.repositoryIndexerService = repositoryIndexerService;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	
	public HandleService getHandleService() {
		return handleService;
	}

	public void setHandleService(HandleService handleService) {
		this.handleService = handleService;
	}
	
	public List<HandleNameAuthority> getHandleNameAuthorities() {
		return handleNameAuthorities;
	}

	public void setHandleNameAuthorities(
			List<HandleNameAuthority> handleNameAuthorities) {
		this.handleNameAuthorities = handleNameAuthorities;
	}
	
	public Long getHandleNameAuthorityId() {
		return handleNameAuthorityId;
	}

	public void setHandleNameAuthorityId(Long handleNameAuthorityId) {
		this.handleNameAuthorityId = handleNameAuthorityId;
	}

	public List<FileDatabase> getFileDatabases() {
		return fileDatabases;
	}

	public void setFileDatabases(List<FileDatabase> fileDatabases) {
		this.fileDatabases = fileDatabases;
	}

	public FileServerService getFileServerService() {
		return fileServerService;
	}

	public void setFileServerService(FileServerService fileServerService) {
		this.fileServerService = fileServerService;
	}

	public Long getDefaultFileDatabaseId() {
		return defaultFileDatabaseId;
	}

	public void setDefaultFileDatabaseId(Long defaultFileDatabaseId) {
		this.defaultFileDatabaseId = defaultFileDatabaseId;
	}

	public String getNameIndexFolder() {
		return nameIndexFolder;
	}

	public void setNameIndexFolder(String nameIndexFolder) {
		this.nameIndexFolder = nameIndexFolder;
	}

	public String getInstitutionalItemIndexFolder() {
		return institutionalItemIndexFolder;
	}

	public void setInstitutionalItemIndexFolder(String institutionalItemIndexFolder) {
		this.institutionalItemIndexFolder = institutionalItemIndexFolder;
	}

	public String getResearcherIndexFolder() {
		return researcherIndexFolder;
	}

	public void setResearcherIndexFolder(String researcherIndexFolder) {
		this.researcherIndexFolder = researcherIndexFolder;
	}

	public String getUserIndexFolder() {
		return userIndexFolder;
	}

	public void setUserIndexFolder(String userIndexFolder) {
		this.userIndexFolder = userIndexFolder;
	}
	
	private void updateUserIndex() throws NoIndexFoundException
	{
		// handle any pre-loaded users
		List<IrUser> users = userService.getAllUsers();	
		log.debug("User size = " + users.size());
		File userIndexFolder = new File(repository.getUserIndexFolder());
		for( IrUser user : users)
		{
			userIndexService.addToIndex(user, userIndexFolder);
		}
	}
	
	private void updateNameIndexFolder(Repository r, String folder) throws IOException
	{

		String oldFolder = r.getNameIndexFolder();
		boolean change = true;
		if( folder != null)
		{
			// add the end seperator
		    if (folder.charAt(folder.length() - 1) != IOUtils.DIR_SEPARATOR) 
		    {
		    	folder = folder + IOUtils.DIR_SEPARATOR;
		    }
			File f = new File(folder);
			if(oldFolder != null)
			{
				File oldFolderFile = new File(oldFolder);
				
				if( f.getAbsolutePath().equals(oldFolderFile.getAbsolutePath()))
			    {
				    change = false;
			    }
			}
			
			if(change)
			{
				FileUtils.forceMkdir(f);
			    r.setNameIndexFolder(folder);
			    if( oldFolder != null)
				{
				    FileUtils.deleteQuietly(new File(oldFolder));
				}
			}
		}
		else
		{
			if(oldFolder != null)
			{
				FileUtils.deleteQuietly(new File(oldFolder));
			}
		}
	}
	
	private void updateInstitutionalItemIndexFolder(Repository r, String folder) throws IOException
	{

		String oldFolder = r.getInstitutionalItemIndexFolder();
		boolean change = true;
		if( folder != null)
		{
			// add the end seperator
		    if (folder.charAt(folder.length() - 1) != IOUtils.DIR_SEPARATOR) 
		    {
		    	folder = folder + IOUtils.DIR_SEPARATOR;
		    }
			File f = new File(folder);
			if(oldFolder != null)
			{
				File oldFolderFile = new File(oldFolder);
				
				if( f.getAbsolutePath().equals(oldFolderFile.getAbsolutePath()))
			    {
				    change = false;
			    }
			}
			
			if(change)
			{
				FileUtils.forceMkdir(f);
			    r.setInstitutionalItemIndexFolder(folder);
			    if( oldFolder != null)
				{
				    FileUtils.deleteQuietly(new File(oldFolder));
				}
			}
		}
		else
		{
			if(oldFolder != null)
			{
				FileUtils.deleteQuietly(new File(oldFolder));
			}
		}
	}
	
	private void updateResearcherIndexFolder(Repository r, String folder) throws IOException
	{

		String oldFolder = r.getResearcherIndexFolder();
		boolean change = true;
		if( folder != null)
		{
			// add the end seperator
		    if (folder.charAt(folder.length() - 1) != IOUtils.DIR_SEPARATOR) 
		    {
		    	folder = folder + IOUtils.DIR_SEPARATOR;
		    }
			File f = new File(folder);
			if(oldFolder != null)
			{
				File oldFolderFile = new File(oldFolder);
				
				if( f.getAbsolutePath().equals(oldFolderFile.getAbsolutePath()))
			    {
				    change = false;
			    }
			}
			
			if(change)
			{
				FileUtils.forceMkdir(f);
			    r.setResearcherIndexFolder(folder);
			    if( oldFolder != null)
				{
				    FileUtils.deleteQuietly(new File(oldFolder));
				}
			}
		}
		else
		{
			if(oldFolder != null)
			{
				FileUtils.deleteQuietly(new File(oldFolder));
			}
		}
	}
	
	private void updateUserIndexFolder(Repository r, String folder) throws IOException, NoIndexFoundException
	{
		
		String oldFolder = r.getUserIndexFolder();
		boolean change = true;
		if( folder != null)
		{
			// add the end seperator
		    if (folder.charAt(folder.length() - 1) != IOUtils.DIR_SEPARATOR) 
		    {
		    	folder = folder + IOUtils.DIR_SEPARATOR;
		    }
			File f = new File(folder);
			if(oldFolder != null)
			{
				File oldFolderFile = new File(oldFolder);
				
				if( f.getAbsolutePath().equals(oldFolderFile.getAbsolutePath()))
			    {
				    change = false;
			    }
			}
			
			if(change)
			{
				FileUtils.forceMkdir(f);
			    r.setUserIndexFolder(folder);
			    if( oldFolder != null)
				{
				    FileUtils.deleteQuietly(new File(oldFolder));
				}
			    updateUserIndex();
			}
		}
		else
		{
			if(oldFolder != null)
			{
				FileUtils.deleteQuietly(new File(oldFolder));
			}
		}
	}
	
	private void updateUserWorkspaceIndexFolder(Repository r, String folder) throws IOException
	{
		
		log.debug("update user workspace index folder " + folder);
		String oldFolder = r.getUserWorkspaceIndexFolder();
		boolean change = true;
		if( folder != null)
		{
			// add the end seperator
		    if (folder.charAt(folder.length() - 1) != IOUtils.DIR_SEPARATOR) 
		    {
		    	folder = folder + IOUtils.DIR_SEPARATOR;
		    }
			File f = new File(folder);
			if(oldFolder != null)
			{
				File oldFolderFile = new File(oldFolder);
				
				if( f.getAbsolutePath().equals(oldFolderFile.getAbsolutePath()))
			    {
				    change = false;
			    }
			}
			
			if(change)
			{
				FileUtils.forceMkdir(f);
				log.debug("setting user workspace index folder to " + folder);
			    r.setUserWorkspaceIndexFolder(folder);
			    if( oldFolder != null)
				{
				    FileUtils.deleteQuietly(new File(oldFolder));
				}
			}
		}
		else
		{
			if(oldFolder != null)
			{
				FileUtils.deleteQuietly(new File(oldFolder));
			}
		}
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
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

	public String getUserWorkspaceIndexFolder() {
		return userWorkspaceIndexFolder;
	}

	public void setUserWorkspaceIndexFolder(String userWorkspaceIndexFolder) {
		this.userWorkspaceIndexFolder = userWorkspaceIndexFolder;
	}

	public boolean getSuspendSubscriptions() {
		return suspendSubscriptions;
	}

	public void setSuspendSubscriptions(boolean suspendSubscriptions) {
		this.suspendSubscriptions = suspendSubscriptions;
	}

	public Long getDefaultLicenseVersionId() {
		return defaultLicenseVersionId;
	}

	public void setDefaultLicenseVersionId(Long defaultLicenseVersionId) {
		this.defaultLicenseVersionId = defaultLicenseVersionId;
	}

	public LicenseService getLicenseService() {
		return licenseService;
	}

	public void setLicenseService(LicenseService licenseService) {
		this.licenseService = licenseService;
	}

	public List<LicenseVersion> getLicenses() {
		return licenses;
	}

	public void setLicenses(List<LicenseVersion> licenses) {
		this.licenses = licenses;
	}

	
	public void setUserId(Long userId) {
		this.userId = userId;
	}


}