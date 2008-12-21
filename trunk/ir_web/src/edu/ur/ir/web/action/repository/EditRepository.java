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

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.Validateable;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryIndexerService;
import edu.ur.ir.repository.RepositoryService;


/**
 * View/Edit repository actions.
 *  
 * @author Nathan Sarr
 *
 */
public class EditRepository extends ActionSupport implements Preparable, 
Validateable{
	
	/**  Logger for editing a file database. */
	private static final Logger log = Logger.getLogger(EditRepository.class);
	
	/**  Generated version id */
	private static final long serialVersionUID = -6421997690248407461L;
	
	/**  The new repository */
	private Repository repository;

	/** Service for dealing with repository */
	private RepositoryService repositoryService;
	
	/** Delete the picture from the repository */
	private Long irFilePictureId;
	
	/** Service for re-indexing repository information */
	private RepositoryIndexerService repositoryIndexerService;
	
	private int batchSize = 10;
	
	/**
	 * Prepare the repository.
	 * 
	 * @see com.opensymphony.xwork.Preparable#prepare()
	 */
	public void prepare() throws Exception{
		log.debug("prepare called");
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
	}
	
	/**
	 * Save the repository
	 * 
	 * @return
	 */
	public String save() {
		if (repository == null) {
			throw new IllegalStateException("repository is null");
		}
		if(repositoryService == null )
		{
			throw new IllegalStateException ("repository service is null");
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
	
	public String reIndexInstitutionalItems()
	{
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		repositoryIndexerService.reIndexInstitutionalItems(repository, batchSize);
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
}