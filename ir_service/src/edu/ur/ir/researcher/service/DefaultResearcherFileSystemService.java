package edu.ur.ir.researcher.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherDAO;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFileDAO;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherFolderDAO;
import edu.ur.ir.researcher.ResearcherInstitutionalItem;
import edu.ur.ir.researcher.ResearcherInstitutionalItemDAO;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherLinkDAO;
import edu.ur.ir.researcher.ResearcherPublication;
import edu.ur.ir.researcher.ResearcherPublicationDAO;
import edu.ur.ir.user.UserFileSystemService;

/**
 * Default implementation of the default researcher file system service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultResearcherFileSystemService implements ResearcherFileSystemService {
	
	/** eclipse generated id */
	private static final long serialVersionUID = 8168747479279250084L;

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
	
	/** Service for dealing with the repository */
	private RepositoryService repositoryService;
	
	/** Service for dealing with items. */
	private ItemService itemService;
	
	/** service for dealing with user file system */
	private UserFileSystemService userFileSystemService;
	
	/** Service for dealing with researcher information */
	private ResearcherDAO researcherDAO;
	
	/**  Logger for add files to item action */
	private static final Logger log = Logger.getLogger(DefaultResearcherFileSystemService.class);

	
	/**
	 * Make the researcher link persistent.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#saveResearcherLink(ResearcherLink)
	 */
	public void saveResearcherLink(ResearcherLink researcherLink) {
		researcherLinkDAO.makePersistent(researcherLink);
	}
	
	
	/**
	 * Create the root publication.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#createRootPublication(edu.ur.ir.researcher.Researcher, edu.ur.ir.item.GenericItem, int)
	 */
	public ResearcherPublication createRootPublication(Researcher researcher, GenericItem item, int versionNumber) {
	
		ResearcherPublication researcherPublication = researcher.createRootPublication(item, versionNumber);
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
	 * Allows a researcher to create a new publication.
	 * 
	 *  
	 * @see edu.ur.ir.researcher.ResearcherService#createPublication(edu.ur.ir.researcher.ResearcherFolder, edu.ur.ir.item.GenericItem, int)
	 */
	public ResearcherPublication createPublication(ResearcherFolder parentFolder, GenericItem item, int versionNumber) {
		
		ResearcherPublication rp = parentFolder.createPublication(item, versionNumber);
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
		/*
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
		*/
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
	
	/**
	 * Get list of researcher institutional item containing this item and delete them
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
	 * Get the list of files for the specified researcher.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFileSystemService#getFiles(java.lang.Long, java.util.List)
	 */
	public List<ResearcherFile> getFiles(Long researcherId, List<Long> fileIds) {
		return researcherFileDAO.getFiles(researcherId, fileIds);
	}

	/**
	 * Get the list of researcher institutional items.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFileSystemService#getResearcherInstitutionalItems(java.lang.Long, java.util.List)
	 */
	public List<ResearcherInstitutionalItem> getResearcherInstitutionalItems(
			Long researcherId, List<Long> institutionalItemIds) {
		return researcherInstitutionalItemDAO.getResearcherInstitutionalItems(researcherId, institutionalItemIds);
	}

	/**
	 * Get the specified links for the speicifed researcher.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFileSystemService#getResearcherLinks(java.lang.Long, java.util.List)
	 */
	public List<ResearcherLink> getResearcherLinks(Long researcherId,
			List<Long> linkIds) {
		return researcherLinkDAO.getResearcherLinks(researcherId, linkIds);
	}

	/**
	 * Get the specified publications for the specified researcher.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFileSystemService#getResearcherPublications(java.lang.Long, java.util.List)
	 */
	public List<ResearcherPublication> getResearcherPublications(
			Long researcherId, List<Long> publicationIds) {
		return researcherPublicationDAO.getResearcherPublications(researcherId, publicationIds);
	}
	
	/**
	 * Allow a user to move files and folders into a given folder
	 * 
	 *
	 * @see edu.ur.ir.researcher.ResearcherFileSystemService#moveFolderSystemInformation(edu.ur.ir.researcher.ResearcherFolder, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List)
	 */
	public List<FileSystem> moveResearcherFileSystemInformation(ResearcherFolder destination,
			List<ResearcherFolder> foldersToMove, 
			List<ResearcherFile> filesToMove, 
			List<ResearcherLink> linksToMove,
			List<ResearcherInstitutionalItem> itemsToMove,
			List<ResearcherPublication> publicationsToMove) 
	{
		
		LinkedList<FileSystem> notMoved = new LinkedList<FileSystem>();
		
		// move folders first
		if( foldersToMove != null )
		{
		    for( ResearcherFolder folder : foldersToMove)
		    {
		    	log.debug("Adding folder " + folder + " to destination " + destination);
			    try {
			    	 destination.addChild(folder);
				} catch (DuplicateNameException e) {
					notMoved.add(folder);
				}
		    }
		}
	
		
		if( filesToMove != null  && notMoved.size() == 0)
		{
		    for( ResearcherFile file : filesToMove)
		    {
		    	log.debug("Adding file " + file + " to destination " + destination);
		    	if( !destination.getFiles().contains(file))
		    	{
		    		destination.addResearcherFile(file);
		    	}
		    	else
		    	{
		    		notMoved.add(file);
		    	}
		    }
		}
		
		if( linksToMove != null  && notMoved.size() == 0)
		{
		    for( ResearcherLink link : linksToMove)
		    {
		    	log.debug("Adding link " + link + " to destination " + destination);
		    	if( destination.getResearcherLink(link.getName()) == null)
		    	{
			        destination.addLink(link);
		    	}
		    	else
		    	{
		    		notMoved.add(link);
		    	}
		    }
		}
		
		if( itemsToMove != null  && notMoved.size() == 0)
		{
		    for( ResearcherInstitutionalItem institutionalItem : itemsToMove)
		    {
		    	log.debug("Adding item " +  institutionalItem + " to destination " + destination);
		    	if( !destination.getInstitutionalItems().contains(institutionalItem))
		    	{
		    		 destination.addInstitutionalItem(institutionalItem);
		    	}
		    	else
		    	{
		    		notMoved.add(institutionalItem);
		    	}
			   
		    }
		}
		
		if( publicationsToMove != null  && notMoved.size() == 0)
		{
		    for( ResearcherPublication publication : publicationsToMove)
		    {
		    	log.debug("Adding publication " +  publication + " to destination " + destination);
		    	
		    	if( !destination.getPublications().contains(publication))
		    	{
		    		   destination.addPublication(publication);
		    	}
		    	else
		    	{
		    		notMoved.add(publication);
		    	}
			 
		    }
		}
		
		if( notMoved.size() == 0)
		{
			researcherFolderDAO.makePersistent(destination);
		}
		
		return notMoved;
	}
	
	/**
	 * Move the folders into the root location of the researcher
	 * 
	 * @throws DuplicateNameException 
	 * 
	 */
	public List<FileSystem> moveResearcherFileSystemInformation(Researcher researcher,
			List<ResearcherFolder> foldersToMove, 
			List<ResearcherFile> filesToMove, 
			List<ResearcherLink> linksToMove,
			List<ResearcherInstitutionalItem> itemsToMove,
			List<ResearcherPublication> publicationsToMove)  {

		LinkedList<FileSystem> notMoved = new LinkedList<FileSystem>();
		
		// move folders first
		if( foldersToMove != null )
		{
		    for( ResearcherFolder folder : foldersToMove)
		    {
		    	log.debug("Adding folder " + folder + " to researcher " + researcher);
			    try {
			    	 researcher.addRootFolder(folder);
				} catch (DuplicateNameException e) {
					notMoved.add(folder);
				}
		    }
		}
	
		
		if( filesToMove != null  && notMoved.size() == 0)
		{
		    for( ResearcherFile file : filesToMove)
		    {
		    	log.debug("Adding file " + file + " to researcher " + researcher);
		    	if( !researcher.getRootFiles().contains(file))
		    	{
			        researcher.addRootFile(file);
		    	}
		    	else
		    	{
		    		notMoved.add(file);
		    	}
		    }
		}
		
		if( linksToMove != null  && notMoved.size() == 0)
		{
		    for( ResearcherLink link : linksToMove)
		    {
		    	log.debug("Adding link " + link + " to researcher " + researcher);
			    researcher.addRootLink(link);
		    }
		}
		
		if( itemsToMove != null  && notMoved.size() == 0)
		{
		    for( ResearcherInstitutionalItem institutionalItem : itemsToMove)
		    {
		    	log.debug("Adding item " +  institutionalItem + " to researcher " + researcher);
		    	if( !researcher.getRootInstitutionalItems().contains(institutionalItem))
		    	{
			        researcher.addRootInstitutionalItem(institutionalItem);
		    	}
		    	else
		    	{
		    		notMoved.add(institutionalItem);
		    	}
		    }
		}
		
		if( publicationsToMove != null  && notMoved.size() == 0)
		{
		    for( ResearcherPublication publication : publicationsToMove)
		    {
		    	log.debug("Adding publication " +  publication + " to researcher " + researcher);
		    	if( !researcher.getRootPublications().contains(publication))
		    	{
		    	    researcher.addRootPublication(publication);
		    	}
		    	else
		    	{
		    		notMoved.add(publication);
		    	}
		    }
		}
		
	
		if( notMoved.size() == 0)
		{
			researcherDAO.makePersistent(researcher);
		}
		
		return notMoved;
		
		
	}


	public ResearcherFolderDAO getResearcherFolderDAO() {
		return researcherFolderDAO;
	}


	public void setResearcherFolderDAO(ResearcherFolderDAO researcherFolderDAO) {
		this.researcherFolderDAO = researcherFolderDAO;
	}


	public ResearcherFileDAO getResearcherFileDAO() {
		return researcherFileDAO;
	}


	public void setResearcherFileDAO(ResearcherFileDAO researcherFileDAO) {
		this.researcherFileDAO = researcherFileDAO;
	}


	public ResearcherPublicationDAO getResearcherPublicationDAO() {
		return researcherPublicationDAO;
	}


	public void setResearcherPublicationDAO(
			ResearcherPublicationDAO researcherPublicationDAO) {
		this.researcherPublicationDAO = researcherPublicationDAO;
	}


	public ResearcherInstitutionalItemDAO getResearcherInstitutionalItemDAO() {
		return researcherInstitutionalItemDAO;
	}


	public void setResearcherInstitutionalItemDAO(
			ResearcherInstitutionalItemDAO researcherInstitutionalItemDAO) {
		this.researcherInstitutionalItemDAO = researcherInstitutionalItemDAO;
	}


	public ResearcherLinkDAO getResearcherLinkDAO() {
		return researcherLinkDAO;
	}


	public void setResearcherLinkDAO(ResearcherLinkDAO researcherLinkDAO) {
		this.researcherLinkDAO = researcherLinkDAO;
	}


	public RepositoryService getRepositoryService() {
		return repositoryService;
	}


	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}


	public ItemService getItemService() {
		return itemService;
	}


	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}


	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}


	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}


	public ResearcherDAO getResearcherDAO() {
		return researcherDAO;
	}


	public void setResearcherDAO(ResearcherDAO researcherDAO) {
		this.researcherDAO = researcherDAO;
	}


	public void saveResearcherInstitutionalItem(
			ResearcherInstitutionalItem entity) {
		researcherInstitutionalItemDAO.makePersistent(entity);
	}


	public void saveResearcherPublication(ResearcherPublication entity) {
		researcherPublicationDAO.makePersistent(entity);
	}
	

}
