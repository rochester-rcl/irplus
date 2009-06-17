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


package edu.ur.ir.web.action.researcher;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.FileSystem;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherInstitutionalItem;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherPublication;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.user.FileSystemSortHelper;


/**
 * Action to view set of files, publications, links and folders for a given researcher.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ViewResearcherFolders extends ActionSupport implements UserIdAware {
	
	/**Eclipse gernerated id */
	private static final long serialVersionUID = -7255565089170626513L;
	
	/** id of the user accessing the data */
	private long userId;

	/**  Id of the researcher */
	private Long researcherId;
	
	/** The researcher who owns the folders  */
	private Researcher researcher;
	
	/**  Researcher information data access  */
	private ResearcherService researcherService;
	
	/** Service for dealing with researcher file system information */
	private ResearcherFileSystemService researcherFileSystemService;

    /** A collection of folders and files for a user in a given location of
        ther personal directory.*/
    private List<FileSystem> fileSystem;
    
    /** set of folders that are the path for the current folder */
    private Collection <ResearcherFolder> folderPath;
	
	/** The folder that owns the listed files and folders */
	private Long parentFolderId;
	
	/** list of folder ids to perform actions on*/
	private Long[] folderIds;
	
	/** list of file ids to perform actions on*/
	private Long[] fileIds;
	
	/** list of publication ids to perform actions on*/
	private Long[] publicationIds;
	
	/** list of link ids to perform actions on*/
	private Long[] linkIds;
	
	/** True indicates the folders were deleted */
	private boolean foldersDeleted = true;
	
	/** Message used to indicate there is a problem with deleting the folders. */
	private String foldersDeletedMessage;
	
	/**  Logger for vierw workspace action */
	private static final Logger log = Logger.getLogger(ViewResearcherFolders.class);

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** name of the element to sort on 
	 *   this is for incoming requests */
	private String sortElement = "name";
	
	/** use the type sort this is information for the page */
	private String folderTypeSort = "none";
	
	/** use the name sort this is information for the page */
	private String folderNameSort = "none";
	
	/** List of institutional item ids to perform action on */
	private Long[] institutionalItemIds;
	
	/**
	 * Get the researcher table
	 * 
	 * @return
	 */
	public String getTable()
	{
		log.debug("getTableCalled");
		researcher = researcherService.getResearcher(researcherId, false);
		
		// deny access if user is not owner of the data
		if( researcher.getUser().getId().equals(userId) )
		{
		    createFileSystem();
		}
		else
		{
			return "accessDenied";
		}
		return SUCCESS;
	}
	
	/**
	 * Removes the select files, publications, links and folders.
	 * 
	 * @return
	 */
	public String deleteFileSystemObjects()
	{
		log.debug("Delete folders called");
		researcher = researcherService.getResearcher(researcherId, false);
		
		// deny access if user is not owner of the data
		if( !researcher.getUser().getId().equals(userId) )
		{
			return "accessDenied";
		}
		
		if( folderIds != null )
		{
		    for(int index = 0; index < folderIds.length; index++)
		    {
			    log.debug("Deleting folder with id " + folderIds[index]);
			    ResearcherFolder pf = researcherFileSystemService.getResearcherFolder(folderIds[index], false);
			    
			    if( !pf.getResearcher().getUser().getId().equals(userId))
			    {
			    	return "accessDenied";
			    }

			    researcherFileSystemService.deleteFolder(pf);
		    }
		}
		
		if(fileIds != null)
		{
			for(int index = 0; index < fileIds.length; index++)
			{
				log.debug("Deleting file with id " + fileIds[index]);
				ResearcherFile rf = researcherFileSystemService.getResearcherFile( fileIds[index], false);
				
				if( !rf.getResearcher().getUser().getId().equals(userId))
				{
					return "accessDenied";
				}
				
				researcherFileSystemService.deleteFile(rf);
			}
		}
		
		if(publicationIds != null)
		{
			for(int index = 0; index < publicationIds.length; index++)
			{
				log.debug("Deleting publication with id " + publicationIds[index]);
				ResearcherPublication rp = researcherFileSystemService.getResearcherPublication( publicationIds[index], false);
				if( !rp.getResearcher().getUser().getId().equals(userId))
				{
					return "accessDenied";
				}
				researcherFileSystemService.deletePublication(rp);
			}
		}
		
		if(institutionalItemIds != null)
		{
			for(int index = 0; index < institutionalItemIds.length; index++)
			{
				log.debug("Deleting Institutional Item with id " + institutionalItemIds[index]);
				ResearcherInstitutionalItem ri = researcherFileSystemService.getResearcherInstitutionalItem(institutionalItemIds[index], false);
				if( !ri.getResearcher().getUser().getId().equals(userId))
				{
					return "accessDenied";
				}
				researcherFileSystemService.deleteInstitutionalItem(ri);
			}
		}		
		
		if(linkIds != null)
		{
			for(int index = 0; index < linkIds.length; index++)
			{
				log.debug("Deleting link with id " + linkIds[index]);
				ResearcherLink rl = researcherFileSystemService.getResearcherLink( linkIds[index], false);
				if( !rl.getResearcher().getUser().getId().equals(userId))
				{
					return "accessDenied";
				}
				researcherFileSystemService.deleteLink(rl);
			}
		}
		
		createFileSystem();
		return SUCCESS;
	}
	
	/**
	 * Create the researcher file system to view.
	 */
	private void createFileSystem()
	{
		
		
		if(parentFolderId != null && parentFolderId > 0)
		{
		    folderPath = researcherFileSystemService.getResearcherFolderPath(parentFolderId);
		}
		
		log.debug("Folder Path ::" + folderPath);
		log.debug("Parent Folder Id ::" + parentFolderId);
		
		Collection<ResearcherFolder> myResearcherFolders = researcherFileSystemService.getFoldersForResearcher(researcherId, parentFolderId);
		
		Collection<ResearcherFile> myResearcherFiles = researcherFileSystemService.getResearcherFiles(researcherId, parentFolderId);
		
		Collection<ResearcherPublication> myResearcherPublications = researcherFileSystemService.getResearcherPublications(researcherId, parentFolderId);

		Collection<ResearcherLink> myResearcherLinks = researcherFileSystemService.getResearcherLinks(researcherId, parentFolderId);

		Collection<ResearcherInstitutionalItem> myResearcherInstitutionalItems = researcherFileSystemService.getResearcherInstitutionalItems(researcherId, parentFolderId);

	    fileSystem = new LinkedList<FileSystem>();

	    fileSystem.addAll(myResearcherFiles);
	    fileSystem.addAll(myResearcherFolders);
	    fileSystem.addAll(myResearcherPublications);
	    fileSystem.addAll(myResearcherLinks);
	    fileSystem.addAll(myResearcherInstitutionalItems);

	    FileSystemSortHelper sortHelper = new FileSystemSortHelper();
	    if( sortElement.equals("type"))
	    {
	    	if(sortType.equals("asc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.TYPE_ASC);
	    		folderTypeSort = "asc";
	    	}
	    	else if( sortType.equals("desc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.TYPE_DESC);
	    		folderTypeSort = "desc";
	    	}
	    	else
	    	{
	    		folderTypeSort = "none";
	    	}
	    }
	    
	    if( sortElement.equals("name"))
	    {
	    	if(sortType.equals("asc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.NAME_ASC);
	    		folderNameSort = "asc";
	    	}
	    	else if( sortType.equals("desc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.NAME_DESC);
	    		folderNameSort = "desc";
	    	}
	    	else
	    	{
	    		folderNameSort = "none";
	    	}
	    }
	    

	}
	
	public Long getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public Long[] getFolderIds() {
		return folderIds;
	}

	public void setFolderIds(Long[] folderIds) {
		this.folderIds = folderIds;
	}

	public boolean isFoldersDeleted() {
		return foldersDeleted;
	}

	public void setFoldersDeleted(boolean foldersDeleted) {
		this.foldersDeleted = foldersDeleted;
	}

	public String getFoldersDeletedMessage() {
		return foldersDeletedMessage;
	}

	public void setFoldersDeletedMessage(String foldersDeletedMessage) {
		this.foldersDeletedMessage = foldersDeletedMessage;
	}
	
	@SuppressWarnings("unchecked")
	public List getFileSystem() {
		return fileSystem;
	}

	public void setFileSystem(List<FileSystem> fileSystem) {
		this.fileSystem = fileSystem;
	}
	

	public Long[] getFileIds() {
		return fileIds;
	}

	public void setFileIds(Long[] fileIds) {
		this.fileIds = fileIds;
	}

	public Collection<ResearcherFolder> getFolderPath() {
		return folderPath;
	}

	public Long getResearcherId() {
		return researcherId;
	}

	public void setResearcherId(Long researcherId) {
		this.researcherId = researcherId;
	}

	public Researcher getResearcher() {
		return researcher;
	}

	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}

	public ResearcherService getResearcherService() {
		return researcherService;
	}

	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

	public Long[] getPublicationIds() {
		return publicationIds;
	}

	public void setPublicationIds(Long[] publicationIds) {
		this.publicationIds = publicationIds;
	}

	public Long[] getLinkIds() {
		return linkIds;
	}

	public void setLinkIds(Long[] linkIds) {
		this.linkIds = linkIds;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getSortElement() {
		return sortElement;
	}

	public void setSortElement(String sortElement) {
		this.sortElement = sortElement;
	}

	public String getFolderTypeSort() {
		return folderTypeSort;
	}

	public void setFolderTypeSort(String folderTypeSort) {
		this.folderTypeSort = folderTypeSort;
	}

	public String getFolderNameSort() {
		return folderNameSort;
	}

	public void setFolderNameSort(String folderNameSort) {
		this.folderNameSort = folderNameSort;
	}

	public Long[] getInstitutionalItemIds() {
		return institutionalItemIds;
	}

	public void setInstitutionalItemIds(Long[] institutionalItemIds) {
		this.institutionalItemIds = institutionalItemIds;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public ResearcherFileSystemService getResearcherFileSystemService() {
		return researcherFileSystemService;
	}

	public void setResearcherFileSystemService(
			ResearcherFileSystemService researcherFileSystemService) {
		this.researcherFileSystemService = researcherFileSystemService;
	}


}
