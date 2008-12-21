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

package edu.ur.ir.researcher;

import java.util.List;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.item.GenericItem;

/**
 * Interface for adding files, folders, publications and links to the researcher page
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ResearcherService {
	
	/**
	 * Delete researcher and all related information within it.
	 * 
	 * @param id - unique id of the researcher
	 */
	public void deleteResearcher(Researcher researcher) ;
	
	/**
	 * Get the researcher by id.
	 * 
	 * @param id - id of the researcher
     * @param lock - upgrade the lock
     * 
     * @return the researcher if found otherwise null.
	 */
	public Researcher getResearcher(Long id, boolean lock) ;

	/**
	 * Make the researcher persistent.
	 * 
	 * @param researcher
	 */
	public void saveResearcher(Researcher researcher) ;

    /**
     * Get all the researchers.
     * 
     * @return all researchers
     */
	public List<Researcher> getAllResearchers();
	
	/**
	 * Allows a researcher to create a new folder at root level.
	 * 
	 * @param researcher Researcher creating folder
	 * @param folderName name of the folder
	 * 
	 * @return new folder
	 * 
	 * @throws DuplicateNameException 
	 */
	public ResearcherFolder createNewRootFolder(Researcher researcher, String folderName) throws DuplicateNameException ;
	
	/**
	 * Create a researcher folder setting the parent as the parent folder.
	 * 
	 * @param parentFolder Parent folder of new folder to be created
	 * @param folderName Name of folder
	 * 
	 * @return New folder
	 * 
	 * @throws DuplicateNameException
	 */
	public ResearcherFolder createNewFolder(ResearcherFolder parentFolder,
			String folderName) throws DuplicateNameException;
	
	/**
	 * Get sub folders within parent folder for a researcher 
	 * 
	 * @param researcherId Id of the researcher
	 * @param parentFolderId Id of the parent folder
	 * 
	 * @return List of folders within the parent folder
	 * 
	 */
	public List<ResearcherFolder> getFoldersForResearcher(Long researcherId, Long parentFolderId) ;

	/**
	 * Get the specified folders for the researcher.
	 * 
	 * @param researcherId Id of researcher
	 * @param folderIds List of folder ids
	 * 
	 * @return List of folders
	 */
	public List<ResearcherFolder> getFolders(Long researcherId, List<Long> folderIds);
	
	/**
	 * Delete all sub folders, files, publicatons and links from the system for the specified folder id.
	 * This physically removes the files stored on the file system.
	 * 
	 * @param Folder to be deleted
	 */
	public void deleteFolder(ResearcherFolder researcherFolder) ;

	/**
	 * Get the researcher folder with the speicified name and parent id.
	 * 
	 * @param name Name of folder
	 * @param parentId Id of the parent folder
	 */
	public ResearcherFolder getResearcherFolder(String name, Long parentId) ;

	/**
	 * Get the researcher folder with the specified id.
	 * 
	 * @param id - id of the researcher folder
     * @param lock - upgrade the lock
     * 
     * @return the researcher folder if found otherwise null.
     */
	public ResearcherFolder getResearcherFolder(Long id, boolean lock) ;

	/**
	 * Get root researcher folder with the specified name for the specified researcher.
	 * 
	 * @param  name Name of root folder
	 * @param researcherId Id of researcher
	 * 
	 * @return Folder if found
	 */
	public ResearcherFolder getRootResearcherFolder(String name, Long researcherId) ;

	/**
	 * Get all folders for the specified researcher.
	 * 
	 * @param researcherId Id of researcher to get all folders
	 * 
	 * @return List of all folders
	 */
	public List<ResearcherFolder> getAllFoldersForResearcher(Long researcherId) ;
	
	/**
	 * Save the researcher folder.
	 * 
	 * @param entity researcher folder to be saved
	 */
	public void saveResearcherFolder(ResearcherFolder entity);

	/**
	 * Get the researcher folders within the specified parent folder.
	 * 
	 * @param researcherId Id of researcher
	 * @param parentFolderId Id of parent folder
	 * 
	 * @return List of folders
	 */
	public List<ResearcherFolder> getResearcherFolders( 
			Long researcherId, 
			Long parentFolderId);

	/**
     * Create a researcher file in the system with the specified ir file for the
     * given parent folder. This is created at the root level (added to the researcher)
     * 
     * @param parentFolder - Folder to add the file to.
     * @param irFile - file to add
     * @param versionNumber - IrFile version number
     * 
     * @return the created researcher file
     * @throws DuplicateNameException 
     */
	public ResearcherFile addFileToResearcher(
			ResearcherFolder parentFolder,
			IrFile irFile, int versionNumber) ;

    /**
	 * Create the root publication.
	 * 
	 * @param researcher researcher creating the publication
	 * @param item Item to add to the researcher
	 */
	public ResearcherPublication createRootPublication(Researcher researcher, GenericItem item);

	/**
	 * Create the root institutional item.
	 * 
	 * @param researcher researcher creating the publication
	 * @param item INstitutional Item to add to the researcher
	 */
	public ResearcherInstitutionalItem createRootInstitutionalItem(Researcher researcher, InstitutionalItem item);

	
	/**
	 * Delete a researcher file.
	 * 
	 * @param rf Researcher file to delete
	 */
	public void deleteFile(ResearcherFile rf);

	/**
	 * Delete a researcher publication.
	 * 
	 * @param rp researcher publication to delete
	 */
	public void deletePublication(ResearcherPublication rp);
	
	/**
	 * Delete a researcher Institutional Item.
	 * 
	 * @param ri researcher Institutional Item to delete
	 */
	public void deleteInstitutionalItem(ResearcherInstitutionalItem ri);
	
	/**
	 * Delete a researcher link.
	 * 
	 * @param rl researcher link to delete
	 */
	public void deleteLink(ResearcherLink rl);

	/** 
	 * Get the path to a specified folder with the specified folder id
	 * 
	 * @param researcherFolderId folder id to find the path for
	 * 
	 * @return Path of the folder
	 */
	public List<ResearcherFolder> getResearcherFolderPath(Long researcherFolderId);

	/**
	 * Get researcher files within the specified parent folder
	 * 
	 * @param researcherId Id of researcher holding the files
	 * @param parentFolderId Id of parent folder
	 * 
	 * @return List of files within the folder
	 */
	public List<ResearcherFile> getResearcherFiles(Long researcherId, Long parentFolderId);
	
	/**
	 * Get researcher publications within the specified parent folder
	 * 
	 * @param researcherId Id of researcher holding the publications
	 * @param parentFolderId Id of parent folder
	 * 
	 * @return List of publications within the folder
	 */
	public List<ResearcherPublication> getResearcherPublications(Long researcherId, Long parentFolderId);
	
	/**
	 * Get researcher Institutional Item within the specified parent folder
	 * 
	 * @param researcherId Id of researcher holding the Institutional Items
	 * @param parentFolderId Id of parent folder
	 * 
	 * @return List of publications within the folder
	 */
	public List<ResearcherInstitutionalItem> getResearcherInstitutionalItems(Long researcherId, Long parentFolderId);


	/**
	 * Get the researcher file with the specified id.
	 * 
	 * @param id - id of the researcher file
     * @param lock - upgrade the lock
     * 
     * @return the researcher file if found otherwise null.	 
     */
	public ResearcherFile getResearcherFile(Long id, boolean lock);

	/**
	 * Allows a researcher to create a new publication.
	 *
	 * @param parentFolder Folder to create the publication in
	 * @param item Item to create publication
	 *
	 * @return Newly created publication
	 */
	public ResearcherPublication createPublication(ResearcherFolder parentFolder, GenericItem item);

	/**
	 * Allows a researcher to create a new Institutional Item.
	 *
	 * @param parentFolder Folder to create the Institutional Item in
	 * @param item Item to create Institutional Item
	 *
	 * @return Newly created Institutional Item
	 */
	public ResearcherInstitutionalItem createInstitutionalItem(ResearcherFolder parentFolder, InstitutionalItem institutionalItem);

	
	/**
	 * Get root researcher link with the specified name for the specified researcher.
	 * 
	 * @param name Name of link
	 * @param researcherId Id of researcher
	 * 
	 * @return link if found
	 */
	public ResearcherLink getRootResearcherLink(String name, Long researcherId);

	/**
	 * Get researcher link with the specified name for the specified folder.
	 * 
	 * @param name Name of link
	 * @param parentId Id of parent folder
	 * 
	 * @return link if found
	 */
	public ResearcherLink getResearcherLink(String name, Long parentId) ;
	
	/**
	 * Get the researcher link with the specified id.
	 * 
	 * @param id - id of the researcher link
     * @param lock - upgrade the lock
     * 
     * @return the researcher link if found otherwise null.	 
     */
	public ResearcherLink getResearcherLink(Long id, boolean lock);
	
	/**
	 * Get links for a researcher in the specified folder
	 * 
	 * @param researcherId Id of researcher
	 * @param parentFolderId Id of parent folder
	 * 
	 *  @return List of researcher links found
	 * 
	 */
	public List<ResearcherLink> getResearcherLinks(Long researcherId, Long parentFolderId);

	/**
	 * Get the researcher publication with the specified id.
	 * 
	 * @param id - id of the researcher publication
     * @param lock - upgrade the lock
     * 
     * @return the researcher publication if found otherwise null.	 
     */
	public ResearcherPublication getResearcherPublication(Long id, boolean lock);

	/**
	 * Get the researcher Institutional Item with the specified id.
	 * 
	 * @param id - id of the researcher Institutional Item
     * @param lock - upgrade the lock
     * 
     * @return the researcher Institutional Item if found otherwise null.	 
     */
	public ResearcherInstitutionalItem getResearcherInstitutionalItem(Long id, boolean lock);

	/**
	 * Save the researcher link
	 * 
	 * @param researcherLink Link to be saved
	 */
	public void saveResearcherLink(ResearcherLink researcherLink);

	/**
	 * Get the count of researcher files using this Irfile
	 * 
	 * @param irFile irFile used by researcher file
	 * 
	 * @return Number of researcher files using this IrFile
	 */
	public Long getResearcherFileCount(IrFile irFile) ;
	
	/**
	 * Get the count of researcher publication using this generic item
	 * 
	 * @param item Item to be searched for in the researcher publication
	 * 
	 * @return Count of researcher publications using this item
	 * 
	 */
	public Long getResearcherPublicationCount(GenericItem item) ;

	/**
	 * Get the count of researcher Institutional Item using this generic item
	 * 
	 * @param item Item to be searched for in the researcher Institutional Item
	 * 
	 * @return Count of researcher Institutional Items using this item
	 * 
	 */
	public Long getResearcherInstitutionalItemCount(InstitutionalItem item) ;
	
	/**
	 * Get all researchers having researcher page public
	 *  
     * @return List of researchers found
     */
	public List<Researcher> getAllPublicResearchers();
	
	/**
	 * Get a count of researchers in the system.
	 * 
	 * @return a count of the researchers in the system
	 */
	public Long getResearcherCount();
	
	/**
	 * Get researchers orderd by last name then first name
	 * 
	 * @param offset - position in the list to start grabbing
	 * @param maxNumToFetch - maximum number to fetch
	 * @return - found researchers
	 */
	public List<Researcher>  getPublicResearchersOrderedByLastFirstName( final int offset, final int maxNumToFetch);


	/**
	 * Get researchers starting from the specified row and end at specified row.
     * The rows will be sorted by the specified parameter in given order.
	 *  
     * @param rowStart Start row to fetch the data from
     * @param rowEnd End row to get data
     * @param propertyName The property to sort on
     * @param orderType The order to sort by (ascending/descending)
     *
     * @return List of researchers 
     */	
	public List<Researcher> getResearchers(final int rowStart, final int rowEnd, final String propertyName, final String orderType) ;
	
	/**
	 * Save the researcher file.
	 * 
	 * @param entity researcher file to be saved
	 */
	public void saveResearcherFile(ResearcherFile entity);

	/**
	 * Get list of researcher institutional item containing this item
	 * 
	 * @param institutionalItem institutional item to be searched for
	 * 
	 */
	public void deleteResearcherInstitutionalItem(InstitutionalItem institutionalItem);
	
	/**
	 * Get a count of public researchers in the system.
	 * 
	 * @return
	 */
	public Long getPublicResearcherCount();

	/**
	 * Return the list of found researchers 
	 * 
	 * @param researcherIds Ids of researchers
	 * 
	 * @return List of researchers
	 */
	public List<Researcher> getResearchers(List<Long> researcherIds);
}
