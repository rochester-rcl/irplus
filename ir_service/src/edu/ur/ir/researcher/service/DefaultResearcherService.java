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


package edu.ur.ir.researcher.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherDAO;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFileDAO;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherFolderDAO;
import edu.ur.ir.researcher.ResearcherInstitutionalItem;
import edu.ur.ir.researcher.ResearcherInstitutionalItemDAO;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherLinkDAO;
import edu.ur.ir.researcher.ResearcherPublication;
import edu.ur.ir.researcher.ResearcherPublicationDAO;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.user.UserFileSystemService;

/**
 * Default Service for dealing with the researcher page
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultResearcherService implements ResearcherService{
	
	public static final long USE_RESEARCHER_AS_ROOT = 0L;
	
	/** Data access for researcher */
	private ResearcherDAO researcherDAO;
	
	/** Data access for researcher folder */
	private ResearcherFolderDAO researcherFolderDAO;

	/** Data access for researcher file */
	private ResearcherFileDAO researcherFileDAO;
	
	/** Data access for researcher publication*/
	private ResearcherPublicationDAO researcherPublicationDAO;

	/** Data access for researcher institutional item */
	private ResearcherInstitutionalItemDAO researcherInstitutionalItemDAO;

	/** Data access for researcher link*/
	private ResearcherLinkDAO researcherLinkDAO;
	
	private RepositoryService repositoryService;
	
	private ItemService itemService;
	
	private UserFileSystemService userFileSystemService;
	
	/**  Logger for add files to item action */
	private static final Logger log = Logger.getLogger(DefaultResearcherService.class);

	
	/**
	 * Delete researcher and all related information within it.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#deleteResearcher(edu.ur.ir.researcher.Researcher)
	 */
	public void deleteResearcher(Researcher researcher) {
		
		// delete the researcher's root files
		Set<ResearcherFile> files = researcher.getRootFiles();
		for(ResearcherFile rf : files)
		{
			researcher.removeRootFile(rf);
		    deleteFile(rf);
		}
		
		
		// delete the researcher's root files
		Set<ResearcherPublication> publications = researcher.getRootPublications();
		for(ResearcherPublication publication : publications)
		{
			researcher.removeRootPublication(publication);
			deletePublication(publication);
		    
		}

		// delete the researcher's root links
		Set<ResearcherLink> links = researcher.getRootLinks();
		for(ResearcherLink link : links)
		{
			researcher.removeRootLink(link);
			deleteLink(link);
		}

		// Delete researcher's root folders
		Set<ResearcherFolder> rootFolders = researcher.getRootFolders();
		for(ResearcherFolder rootFolder : rootFolders)
		{
			researcher.removeRootFolder(rootFolder);
			deleteFolder(rootFolder);
		}
		
		researcherDAO.makeTransient(researcher);
	}
	
	/**
	 * Get the researcher by id.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcher(java.lang.Long, boolean)
	 */
	public Researcher getResearcher(Long id, boolean lock) {
		return researcherDAO.getById(id, lock);
	}


	/**
	 * Make the researcher persistent.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#saveResearcher(edu.ur.ir.researcher.Researcher)
	 */
	public void saveResearcher(Researcher researcher) {
		researcherDAO.makePersistent(researcher);
	}
	
	/**
	 * Make the researcher link persistent.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#saveResearcherLink(ResearcherLink)
	 */
	public void saveResearcherLink(ResearcherLink researcherLink) {
		researcherLinkDAO.makePersistent(researcherLink);
	}

	/**
	 * Get researcher dao
	 * 
	 * @return
	 */
	public ResearcherDAO getResearcherDAO() {
		return researcherDAO;
	}

	/**
	 * Set researcher dao
	 * 
	 * @param researcherDAO
	 */
	public void setResearcherDAO(ResearcherDAO researcherDAO) {
		this.researcherDAO = researcherDAO;
	}

	/**
	 * Create the root publication.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#createRootPublication(Researcher, GenericItem)
	 */
	public ResearcherPublication createRootPublication(Researcher researcher, GenericItem item) {
	
		ResearcherPublication researcherPublication = researcher.createRootPublication(item);
		researcherPublicationDAO.makePersistent(researcherPublication);
		return researcherPublication;
	}

	/**
	 * Create the root publication.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#createRootPublication(Researcher, GenericItem)
	 */
	public ResearcherInstitutionalItem createRootInstitutionalItem(Researcher researcher, InstitutionalItem institutionalItem) {
	
		ResearcherInstitutionalItem researcherInstitutionalItem = researcher.createRootInstitutionalItem(institutionalItem);
		researcherInstitutionalItemDAO.makePersistent(researcherInstitutionalItem);
		return researcherInstitutionalItem;
	}
	
	/**
     * Create a researcher file in the system with the specified ir file for the
     * given parent folder. This is created at the root level (added to the researcher)
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#addFileToResearcher(ResearcherFolder, IrFile, int)
	 */
	public ResearcherFile addFileToResearcher(
			ResearcherFolder parentFolder,
			IrFile irFile, int versionNumber) {
		
			ResearcherFile researcherFile = parentFolder.addFile(irFile);
			researcherFile.setVersionNumber(versionNumber);
			saveResearcherFolder(parentFolder);
	       
	       
			return researcherFile;
	}
	
    /**
     * Get all the researchers.
     * 
     * @see edu.ur.ir.researcher.ResearcherService#getAllResearchers()
     */
    @SuppressWarnings("unchecked")
	public List<Researcher> getAllResearchers() {
    	return researcherDAO.getAll();
    }

	/**
	 * Allows a researcher to create a new publication.
	 * 
	 *  
	 * @see edu.ur.ir.researcher.ResearcherService#createPublication(ResearcherFolder, GenericItem)
	 */
	public ResearcherPublication createPublication(ResearcherFolder parentFolder, GenericItem item) {
		
		ResearcherPublication rp = parentFolder.createPublication(item);
		researcherFolderDAO.makePersistent(parentFolder);
		return rp;
	}

	/**
	 * Allows a researcher to create a new Institutional Item.
	 * 
	 *  
	 * @see edu.ur.ir.researcher.ResearcherService#createInstitutionalItem(ResearcherFolder, InstitutionalItem)
	 */
	public ResearcherInstitutionalItem createInstitutionalItem(ResearcherFolder parentFolder, InstitutionalItem item) {
		
		ResearcherInstitutionalItem ri = parentFolder.createInstitutionalItem(item);
		researcherFolderDAO.makePersistent(parentFolder);
		return ri;
	}
	
	/**
	 * Allows a researcher to create a new folder at root level.
	 * 
	 * @throws DuplicateNameException 
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#createNewRootFolder(edu.ur.ir.researcher.Researcher, java.lang.String)
	 */
	public ResearcherFolder createNewRootFolder(Researcher researcher, String folderName) throws DuplicateNameException {
		
		ResearcherFolder rf = researcher.createRootFolder(folderName);
		researcherFolderDAO.makePersistent(rf);
		return rf;
	}

	/**
	 * Create a researcher folder setting the parent as the parent folder.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#createNewFolder(edu.ur.ir.researcher.ResearcherFolder, java.lang.String)
	 */
	public ResearcherFolder createNewFolder(ResearcherFolder parentFolder,
			String folderName) throws DuplicateNameException{
		
		ResearcherFolder rf = parentFolder.createChild(folderName);
		researcherFolderDAO.makePersistent(rf);
		
		return rf;
	}

	
	/**
	 * Get sub folders within parent folder for a researcher 
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getFoldersForResearcher(Long, Long)
	 */
	public List<ResearcherFolder> getFoldersForResearcher(Long researcherId, Long parentFolderId) {
	    
		if( parentFolderId == null || parentFolderId == USE_RESEARCHER_AS_ROOT)
		{
			return  researcherFolderDAO.getRootFolders(researcherId);
		}
		else
		{
		   return researcherFolderDAO.getSubFoldersForFolder(researcherId, parentFolderId);
		}
	}

	/**
	 * Get the specified folders for the researcher.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getFolders(Long, List)
	 */
	public List<ResearcherFolder> getFolders(Long researcherId, List<Long> folderIds) {
		return researcherFolderDAO.getFolders(researcherId, folderIds);
	}

	/**
	 * Delete a researcher file.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#deleteFile(ResearcherFile)
	 */
	public void deleteFile(ResearcherFile rf)
	{
		IrFile irFile = rf.getIrFile();
		researcherFileDAO.makeTransient(rf);

		// Check if irFile is used by PersonalFile or Item or researcher 
		if ((userFileSystemService.getPersonalFileCount(irFile) == 0) && (itemService.getItemFileCount(irFile) == 0)
				&& (getResearcherFileCount(irFile) == 0)) {
			repositoryService.deleteIrFile(irFile);
		}

	}

	/**
	 * Delete a researcher publication.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#deletePublication(ResearcherPublication)
	 */
	public void deletePublication(ResearcherPublication rp)
	{
		GenericItem item = rp.getPublication();
		researcherPublicationDAO.makeTransient(rp);
		log.debug("itemService.getItemVersionCount(item)::"+itemService.getItemVersionCount(item));
		log.debug("getResearcherPublicationCount(item)item)::"+getResearcherPublicationCount(item));
		// Check if generic item is used in ItemVersion or researcher page 
		if ((itemService.getItemVersionCount(item) == 0)
				&& (getResearcherPublicationCount(item) == 0)) {
			
			log.debug("Delete item");
			itemService.deleteItem(item);
		}
	}

	/**
	 * Delete a researcher institutional item.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#deleteInstitutionalItem(ResearcherInstitutionalItem)
	 */
	public void deleteInstitutionalItem(ResearcherInstitutionalItem ri)
	{
		researcherInstitutionalItemDAO.makeTransient(ri);
	}
	
	/**
	 * Delete a researcher link.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#deleteLink(ResearcherLink)
	 */
	public void deleteLink(ResearcherLink rl)
	{
		researcherLinkDAO.makeTransient(rl);

	}

	/**
	 * Delete all sub folders, files, publicatons and links from the system for the specified folder id.
	 * This physically removes the files stored on the file system.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#deleteFolder(ResearcherFolder)
	 */
	public void deleteFolder(ResearcherFolder researcherFolder) {
		
		List<ResearcherFile> researcherFiles = researcherFolderDAO.getAllFilesForFolder(researcherFolder);
		log.debug("researcherFiles="+researcherFiles);
		List<ResearcherPublication> researcherPublications = researcherFolderDAO.getAllPublicationsForFolder(researcherFolder);
		log.debug("researcherPublications="+researcherPublications);
		List<ResearcherLink> researcherLinks = researcherFolderDAO.getAllLinksForFolder(researcherFolder);
		log.debug("researcherLinks="+researcherLinks);
		
		
		for( ResearcherFile aFile : researcherFiles)
		{
		    deleteFile(aFile);
		}

		for( ResearcherPublication aPublication : researcherPublications)
		{
		    deletePublication(aPublication);
		}
		
		for( ResearcherLink aLink : researcherLinks)
		{
		    deleteLink(aLink);
		}
		log.debug("Deleted all contents");
		researcherFolderDAO.makeTransient(researcherFolder);

	}

	/**
	 * Get the researcher folder with the specified name and parent id.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherFolder(java.lang.String, java.lang.Long)
	 */
	public ResearcherFolder getResearcherFolder(String name, Long parentId) {
		return researcherFolderDAO.getResearcherFolder(name, parentId);
	}

	/**
	 * Get the researcher folder with the specified id.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherFolder(java.lang.Long, boolean)
	 */
	public ResearcherFolder getResearcherFolder(Long id, boolean lock) {
		return researcherFolderDAO.getById(id, lock);
	}

	/**
	 * Get root researcher folder with the specified name for the specified researcher.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getRootResearcherFolder(java.lang.String, java.lang.Long)
	 */
	public ResearcherFolder getRootResearcherFolder(String name, Long researcherId) {
		return researcherFolderDAO.getRootResearcherFolder(name, researcherId);
	}

	/**
	 * Get root researcher link with the specified name for the specified researcher.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getRootResearcherLink(String, Long)
	 */
	public ResearcherLink getRootResearcherLink(String name, Long researcherId) {
		return researcherLinkDAO.getRootResearcherLink(name, researcherId);
	}

	/**
	 * Get researcher link with the specified name for the specified folder.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherLink(String, Long)
	 */
	public ResearcherLink getResearcherLink(String name, Long parentId) {
		return researcherLinkDAO.getResearcherLink(name, parentId);
	}

	/**
	 * Get all folders for the specified researcher.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getAllFoldersForResearcher(Long)
	 */
	public List<ResearcherFolder> getAllFoldersForResearcher(Long researcherId) {
		return researcherFolderDAO.getAllResearcherFoldersForResearcher(researcherId);
	}

	/**
	 * Save the researcher folder.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#saveResearcherFolder(ResearcherFolder)
	 */
	public void saveResearcherFolder(ResearcherFolder entity) {
		researcherFolderDAO.makePersistent(entity);
	}

	/**
	 * Save the researcher file.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#saveResearcherFile(ResearcherFile)
	 */
	public void saveResearcherFile(ResearcherFile entity) {
		researcherFileDAO.makePersistent(entity);
	}
	
	/**
	 * Get the researcher folders within the specified parent folder.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherFolders(Long, Long)
	 */
	public List<ResearcherFolder> getResearcherFolders( 
			Long researcherId, 
			Long parentFolderId) 
	{
		if( parentFolderId == null || parentFolderId == USE_RESEARCHER_AS_ROOT)
		{
			return researcherFolderDAO.getRootFolders(researcherId);
		}
		else
		{
			return researcherFolderDAO.getSubFoldersForFolder(researcherId, parentFolderId);
		}
	}

	/**
	 * Get researcher folder data access
	 * 
	 * @return
	 */
	public ResearcherFolderDAO getResearcherFolderDAO() {
		return researcherFolderDAO;
	}

	/**
	 * Set researcher folder data access
	 * 
	 * @param researcherFolderDAO
	 */
	public void setResearcherFolderDAO(ResearcherFolderDAO researcherFolderDAO) {
		this.researcherFolderDAO = researcherFolderDAO;
	}

	/**
	 * Get researcher file data access
	 * 
	 * @return
	 */
	public ResearcherFileDAO getResearcherFileDAO() {
		return researcherFileDAO;
	}

	/**
	 * Set researcher file data access
	 * 
	 * @param researcherFileDAO
	 */
	public void setResearcherFileDAO(ResearcherFileDAO researcherFileDAO) {
		this.researcherFileDAO = researcherFileDAO;
	}

	/**
	 * Get researcher publication data access
	 * 
	 * @return
	 */
	public ResearcherPublicationDAO getResearcherPublicationDAO() {
		return researcherPublicationDAO;
	}

	/**
	 * Set researcher publication data access
	 * 
	 * @param researcherPublicationDAO
	 */
	public void setResearcherPublicationDAO(
			ResearcherPublicationDAO researcherPublicationDAO) {
		this.researcherPublicationDAO = researcherPublicationDAO;
	}

	/**
	 * Get researcher link data access
	 * 
	 * @return
	 */
	public ResearcherLinkDAO getResearcherLinkDAO() {
		return researcherLinkDAO;
	}

	/**
	 * Set researcher link data access
	 * 
	 * @param researcherLinkDAO
	 */
	public void setResearcherLinkDAO(ResearcherLinkDAO researcherLinkDAO) {
		this.researcherLinkDAO = researcherLinkDAO;
	}

	/**
	 * Get repository service
	 * 
	 * @return
	 */
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	/**
	 * Set repository service
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**
	 * Get item service
	 * 
	 * @return
	 */
	public ItemService getItemService() {
		return itemService;
	}

	/**
	 * Set item service
	 * 
	 * @param itemService
	 */
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	/** 
	 * Get the path to a specified folder with the specified folder id
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherFolderPath(Long)
	 */
	public List<ResearcherFolder> getResearcherFolderPath(Long researcherFolderId) {
		if( researcherFolderId == null )
		{
			return new LinkedList<ResearcherFolder>();
		}
		
		ResearcherFolder f = this.getResearcherFolder(researcherFolderId, false);
		
		if( f != null )
		{	
		    return researcherFolderDAO.getPath(f);
		}
		else
		{
			return new LinkedList<ResearcherFolder>();
		}
	}
	
	/**
	 * Get researcher files within the specified parent folder
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherFiles(Long, Long)
	 */
	public List<ResearcherFile> getResearcherFiles(Long researcherId, Long parentFolderId) {
	    
		if( parentFolderId == null || parentFolderId == USE_RESEARCHER_AS_ROOT)
		{
			return  researcherFileDAO.getRootFiles(researcherId);
		}
		else
		{
		   return researcherFileDAO.getFilesInAFolderForResearcher(researcherId, parentFolderId);
		}
	}

	/**
	 * Get researcher publications within the specified parent folder
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherPublications(Long, Long)
	 */
	public List<ResearcherPublication> getResearcherPublications(Long researcherId, Long parentFolderId) {
		
		log.debug("In service parentFolderId :"+parentFolderId);
	    
		if( parentFolderId == null || parentFolderId == USE_RESEARCHER_AS_ROOT)
		{
			log.debug("In service parentFolderId NUll :"+parentFolderId);
			return  researcherPublicationDAO.getRootResearcherPublications(researcherId);
		}
		else
		{
			log.debug("In service parentFolderId not null :"+parentFolderId);
		   return researcherPublicationDAO.getSubResearcherPublications(researcherId, parentFolderId);
		}
	}
	
	/**
	 * Get researcher Institutional Item within the specified parent folder
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherInstitutionalItems(Long, Long)
	 */
	public List<ResearcherInstitutionalItem> getResearcherInstitutionalItems(Long researcherId, Long parentFolderId) {
		
		log.debug("In service parentFolderId :"+parentFolderId);
	    
		if( parentFolderId == null || parentFolderId == USE_RESEARCHER_AS_ROOT)
		{
			log.debug("In service parentFolderId NUll :"+parentFolderId);
			return  researcherInstitutionalItemDAO.getRootResearcherInstitutionalItems(researcherId);
		}
		else
		{
			log.debug("In service parentFolderId not null :"+parentFolderId);
		   return researcherInstitutionalItemDAO.getSubResearcherInstitutionalItems(researcherId, parentFolderId);
		}
	}	

	/**
	 * Get links for a researcher in the specified folder
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherLinks(Long, Long)
	 */
	public List<ResearcherLink> getResearcherLinks(Long researcherId, Long parentFolderId) {
		
		log.debug("In service parentFolderId :"+parentFolderId);
	    
		if( parentFolderId == null || parentFolderId == USE_RESEARCHER_AS_ROOT)
		{
			log.debug("In service parentFolderId NUll :"+parentFolderId);
			return  researcherLinkDAO.getRootResearcherLinks(researcherId);
		}
		else
		{
			log.debug("In service parentFolderId not null :"+parentFolderId);
		   return researcherLinkDAO.getSubResearcherLinks(researcherId, parentFolderId);
		}
	}
	
	/**
	 * Get the researcher file with the specified id.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherFile(Long, boolean)
	 */
	public ResearcherFile getResearcherFile(Long id, boolean lock)
	{
		return researcherFileDAO.getById(id, lock);
	}

	/**
	 * Get the researcher link with the specified id.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherLink(Long, boolean)
	 */
	public ResearcherLink getResearcherLink(Long id, boolean lock)
	{
		return researcherLinkDAO.getById(id, lock);
	}
	
	/**
	 * Get the researcher publication with the specified id.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherPublication(Long, boolean)
	 */
	public ResearcherPublication getResearcherPublication(Long id, boolean lock)
	{
		return researcherPublicationDAO.getById(id, lock);
	}

	/**
	 * Get the researcher Institutional Item with the specified id.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherInstitutionalItem(Long, boolean)
	 */
	public ResearcherInstitutionalItem getResearcherInstitutionalItem(Long id, boolean lock)
	{
		return researcherInstitutionalItemDAO.getById(id, lock);
	}
	
	/**
	 * Get the count of researcher files using this Irfile
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherFileCount(IrFile)
	 * 
	 */
	public Long getResearcherFileCount(IrFile irFile) {
		return researcherFileDAO.getResearcherFileCount(irFile.getId());
	}
	
	/**
	 * Get the count of researcher publication using this generic item
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherPublicationCount(GenericItem)
	 * 
	 */
	public Long getResearcherPublicationCount(GenericItem item) {
		return researcherPublicationDAO.getResearcherPublicationCount(item.getId());
	}

	/**
	 * Get the count of researcher Institutional Item using this instituional item
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherInstitutionalItemCount(GenericItem)
	 * 
	 */
	public Long getResearcherInstitutionalItemCount(InstitutionalItem item) {
		return researcherInstitutionalItemDAO.getResearcherInstitutionalItemCount(item.getId());
	}
	
	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	/**
	 * Get all researchers having researcher page public
	 *  
     * @see edu.ur.ir.researcher.ResearcherService#getAllPublicResearchers()
     */
	public List<Researcher> getAllPublicResearchers() {
   		return researcherDAO.getAllPublicResearchers();
   }

	/**
	 * Get researchers starting from the specified row and end at specified row.
     * The rows will be sorted by the specified parameter in given order.
	 *  
     * @return List of researchers 
     */
	public List<Researcher> getResearchers(final int rowStart, final int rowEnd, final String propertyName, final String orderType)  {
		return researcherDAO.getResearchers(rowStart, rowEnd, propertyName,orderType);
	
	}

	public ResearcherInstitutionalItemDAO getResearcherInstitutionalItemDAO() {
		return researcherInstitutionalItemDAO;
	}

	public void setResearcherInstitutionalItemDAO(
			ResearcherInstitutionalItemDAO researcherInstitutionalItemDAO) {
		this.researcherInstitutionalItemDAO = researcherInstitutionalItemDAO;
	}

	/**
	 * Get list of researcher institutional item containing this item
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#deleteResearcherInstitutionalItem(InstitutionalItem)
	 */
	public void deleteResearcherInstitutionalItem(InstitutionalItem institutionalItem) {
		
		List<ResearcherInstitutionalItem> items = researcherInstitutionalItemDAO.getResearcherInstitutionalItem(institutionalItem.getId());
		
		for(ResearcherInstitutionalItem item : items) {
			researcherInstitutionalItemDAO.makeTransient(item);
		}
		
	}

	
	/**
	 * Get a count of researchers.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherCount()
	 */
	public Long getResearcherCount() {
		return researcherDAO.getCount();
	}

	
	/**
	 * Get the public researchers
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getPublicResearchersOrderedByLastFirstName(int, int)
	 */
	public List<Researcher> getPublicResearchersOrderedByLastFirstName(int offset,
			int maxNumToFetch) {
	    return researcherDAO. getPublicResearchersOrderedByLastFirstName(offset, maxNumToFetch);
	}

	
	/**
	 * Get a count of public researchers.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getPublicResearcherCount()
	 */
	public Long getPublicResearcherCount() {
		return researcherDAO.getPublicResearcherCount();
	}

	/**
	 * Return the list of found researchers 
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearchers(java.util.List)
	 */
	public List<Researcher> getResearchers(List<Long> researcherIds) {
		return researcherDAO.getResearchers(researcherIds);
	}
}
