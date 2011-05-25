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

package edu.ur.ir.web.action.item;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionDownloadCount;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Action to display contributor page
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class ViewContributorPage extends Pager {

	/**  Eclipse generated id */
	private static final long serialVersionUID = -3059332380151731902L;

	/** Id of contributor */
	private Long personNameId;
	
	/** Service for dealing with person information */
	private PersonService personService;

	/** Service class for institutional Item */
	private InstitutionalItemService institutionalItemService;
	
	/** List of publications published by the contributor */
	private List<ContributorPublication> contributorPublications = new LinkedList<ContributorPublication>();
	
	/** User service */
	private UserService userService;
	
	/** Researcher */
	private Researcher researcher;
	
	/**  Repository */
	private Repository repository;
	
	/** Total downloads */
	private Long totalDownloads = 0l;
	
	/** Count of publications */
	private Long publicationsCount = 0l;
	
	/** Latest publication */
	private InstitutionalItemVersion latestItemVersion;

	/** Latest publication contributor type */
	private String latestPublicationContributorType;
	
	/** Maximum number of times an item is downloaded */
	private Long mostDownloadedCount = 0l;
	
	/** Maximum downloaded item version */
	private InstitutionalItemVersion mostDownloadedItemVersion ;
	
	/** Contribution type for Maximum downloaded item */
	private String mostDownloadedItemContributorType ;
	
	/**  Name for a person that was selected for viewing*/
	private PersonName personName;
	
	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** name of the element to sort on 
	 *   this is for incoming requests */
	private String sortElement = "title";

	/** Total number of institutional items*/
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Service to get repository information  */
	private RepositoryService repositoryService;
	
	/** Service to deal with institutional item version information */
	private InstitutionalItemVersionService institutionalItemVersionService;



	/** Default constructor */
	public ViewContributorPage()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 20;
	}
	
	/**
	 * Get the rss feed for the person.
	 * 
	 * @return
	 */
	public String getRss()
	{
		if(personNameId == null )
		{
			return "not_found";
		}
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		rowEnd = rowStart + numberOfResultsToShow;
		
		personName = personService.getName(personNameId, false);
		
		if( personName == null )
		{
		    return "not_found";	
		}
		
		PersonNameAuthority authority = personName.getPersonNameAuthority();
		Set<PersonName> names = authority.getNames();
		totalDownloads = institutionalItemVersionService.getNumberOfDownlodsForPersonNames(names);
		List<InstitutionalItemVersionDownloadCount> itemVersions = institutionalItemVersionService.getPublicationVersionsForNamesBySubmissionDate(rowStart, numberOfResultsToShow, names, OrderType.DESCENDING_ORDER);
		createContributorPublications(itemVersions, authority);
		return SUCCESS;
	}
	
	/**
	 * View the contributor page.
	 * 
	 * @return
	 */
	public String execute()
	{
		if( personNameId == null )
		{
		    return "not_found";	
		}
		rowEnd = rowStart + numberOfResultsToShow;
		personName = personService.getName(personNameId, false);
		
		if( personName == null )
		{
		    return "not_found";	
		}
		PersonNameAuthority authority = personName.getPersonNameAuthority();
		Set<PersonName> names = authority.getNames();

		IrUser user = userService.getUserByPersonNameAuthority(authority.getId());
		
		if (user != null && user.getResearcher() != null && user.getResearcher().isPublic()) {
			researcher = user.getResearcher();
		}
			
		totalDownloads = institutionalItemVersionService.getNumberOfDownlodsForPersonNames(names);
		
		 List<InstitutionalItemVersionDownloadCount> itemVersions = new LinkedList<InstitutionalItemVersionDownloadCount>();
		
		// Construct the object with item and download info for display
		if( sortElement.equals("title"))
		{
		    itemVersions = institutionalItemVersionService.getPublicationVersionsForNamesByTitle(rowStart, numberOfResultsToShow, names, OrderType.getOrderType(sortType));
		} 
		else if( sortElement.equals("download"))
		{
			itemVersions = institutionalItemVersionService.getPublicationVersionsForNamesByDownload(rowStart, numberOfResultsToShow, names, OrderType.getOrderType(sortType));
		}
		else if( sortElement.equals("submissionDate"))
		{
			itemVersions = institutionalItemVersionService.getPublicationVersionsForNamesBySubmissionDate(rowStart, numberOfResultsToShow, names, OrderType.getOrderType(sortType));
		}
		
		createContributorPublications(itemVersions, authority);
        getMostRecent(names, authority);
		getMostDownloaded(names, authority);
		publicationsCount = institutionalItemService.getPublicationCountByPersonName(names);
		totalHits = publicationsCount.intValue();
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
		return SUCCESS;
	}
	
	/**
	 * Set up the contributor publications.
	 * 
	 * @param itemVersions
	 * @param authority
	 */
	private void createContributorPublications( List<InstitutionalItemVersionDownloadCount> itemVersions, PersonNameAuthority authority)
	{
		for (InstitutionalItemVersionDownloadCount itemVersionDownloadCount:itemVersions) {

			// Find the contributor type
			ContributorPublication cp = new ContributorPublication(itemVersionDownloadCount.getInstitutionalItemVersion());
			ItemContributor contributor = getContributor(itemVersionDownloadCount, authority);
			if( contributor != null )
			{
			    cp.setContributor(contributor.getContributor());
			}
			
			// Set download count for an item
			cp.setDownloadsCount(itemVersionDownloadCount.getDownloadCount());
			contributorPublications.add(cp);
		}
		
	}
	
	
	/**
	 * Get the most recent submission for the name.
	 * 
	 * @param names
	 * @param authority
	 */
	private void getMostRecent(Set<PersonName> names, PersonNameAuthority authority)
	{
		// Set most downloaded publication
		List<InstitutionalItemVersionDownloadCount> mostRecent  = institutionalItemVersionService.getPublicationVersionsForNamesBySubmissionDate(0, 1, names, OrderType.DESCENDING_ORDER);
		
		if( mostRecent.size() > 0 )
		{
			InstitutionalItemVersionDownloadCount latest = mostRecent.get(0);
			latestItemVersion = latest.getInstitutionalItemVersion();
			ItemContributor contributor = getContributor( latest, authority);
			if( contributor != null )
			{
				latestPublicationContributorType = contributor.getContributor().getContributorType().getName();
			}
		}
	}
	
	/**
	 * Get the most downloaded value for a set of person names.
	 * 
	 * @param names
	 * @param authority
	 */
	private void getMostDownloaded(Set<PersonName> names, PersonNameAuthority authority)
	{
		// Set most downloaded publication
		List<InstitutionalItemVersionDownloadCount> topDownloads  = institutionalItemVersionService.getPublicationVersionsForNamesByDownload(0, 1, names, OrderType.DESCENDING_ORDER);
		
		if( topDownloads.size() > 0 )
		{
			InstitutionalItemVersionDownloadCount downloadInfo = topDownloads.get(0);
			mostDownloadedCount = downloadInfo.getDownloadCount();
			mostDownloadedItemVersion = downloadInfo.getInstitutionalItemVersion();
			
			ItemContributor contributor = getContributor( downloadInfo, authority);
			if( contributor != null )
			{
				 mostDownloadedItemContributorType = contributor.getContributor().getContributorType().getName();
			}
		}

	}
	
	/**
	 * Get the Contributor for a given item.
	 * 
	 * @param itemVersionDownloadCount
	 * @param authority
	 * @return
	 */
	private ItemContributor getContributor(InstitutionalItemVersionDownloadCount itemVersionDownloadCount, PersonNameAuthority authority)
	{
		for(ItemContributor c :itemVersionDownloadCount.getInstitutionalItemVersion().getItem().getContributors()) {
			if (c.getContributor().getPersonName().getPersonNameAuthority().equals(authority)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Simple class to help with the display of the 
	 * contributor name and their publications and versions
	 * 
	 * @author Sharmila Ranganathan
	 *
	 */
	public class ContributorPublication
	{
		private InstitutionalItemVersion institutionalItemVersion;
		
		private Contributor contributor;
		
		private Long downloadsCount;
		
		public ContributorPublication(InstitutionalItemVersion institutionalItemVersion)
		{
			this.institutionalItemVersion = institutionalItemVersion;
		}

		public InstitutionalItemVersion getInstitutionalItemVersion() {
			return institutionalItemVersion;
		}

		public void setInstitutionalItemVersion(
				InstitutionalItemVersion institutionalItemVersion) {
			this.institutionalItemVersion = institutionalItemVersion;
		}

		public Contributor getContributor() {
			return contributor;
		}

		public void setContributor(Contributor contributor) {
			this.contributor = contributor;
		}

		public Long getDownloadsCount() {
			return downloadsCount;
		}

		public void setDownloadsCount(Long downloadsCount) {
			this.downloadsCount = downloadsCount;
		}

	}
	
	
	public Long getPersonNameId() {
		return personNameId;
	}


	public void setPersonNameId(Long personNameId) {
		this.personNameId = personNameId;
	}


	public PersonService getPersonService() {
		return personService;
	}


	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}



	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}




	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public Researcher getResearcher() {
		return researcher;
	}


	public List<ContributorPublication> getContributorPublications() {
		return contributorPublications;
	}



	public long getPublicationsCount() {
		return publicationsCount;
	}


	public long getTotalDownloads() {
		return totalDownloads;
	}

	public String getLatestPublicationContributorType() {
		return latestPublicationContributorType;
	}


	public InstitutionalItemVersion getLatestItemVersion() {
		return latestItemVersion;
	}


	public long getMostDownloadedCount() {
		return mostDownloadedCount;
	}


	public String getMostDownloadedItemContributorType() {
		return mostDownloadedItemContributorType;
	}


	public InstitutionalItemVersion getMostDownloadedItemVersion() {
		return mostDownloadedItemVersion;
	}


	public PersonName getPersonName() {
		return personName;
	}

	
	public int getTotalHits() {
		return totalHits;
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



	public void setTotalDownloads(long totalDownloads) {
		this.totalDownloads = totalDownloads;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}
	
	public int getRowEnd() {
		return rowEnd;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	public Repository getRepository() {
		return repository;
	}

	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}


}
