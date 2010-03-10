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

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionDownloadCount;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.statistics.DownloadStatisticsService;
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
	
	/** Statistics service*/
	private DownloadStatisticsService downloadStatisticsService;

	/** Total downloads */
	private long totalDownloads = 0l;
	
	/** Count of publications */
	private long publicationsCount = 01;
	
	/** Latest publication */
	private InstitutionalItemVersion latestItemVersion;

	/** Latest publication contributor type */
	private String latestPublicationContributorType;
	
	/** Maximum number of times an item is downloaded */
	private long mostDownloadedCount = 0l;
	
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
	
	/** Default constructor */
	public ViewContributorPage()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 20;
	}
	
	/**
	 * View the contributor page.
	 * 
	 * @return
	 */
	public String execute()
	{
		personName = personService.getName(personNameId, false);
		PersonNameAuthority authority = personName.getPersonNameAuthority();
		Set<PersonName> names = authority.getNames();

		IrUser user = userService.getUserByPersonNameAuthority(authority.getId());
		
		if (user != null && user.getResearcher() != null && user.getResearcher().isPublic()) {
			researcher = user.getResearcher();
		}
		
		Timestamp timeStamp = null;
	
		totalDownloads = institutionalItemService.getNumberOfDownlodsForPersonNames(names);
		
		 List<InstitutionalItemVersionDownloadCount> itemVersions = new LinkedList<InstitutionalItemVersionDownloadCount>();
		
		// Construct the object with item and download info for display
		if( sortElement.equals("title"))
		{
		    itemVersions = institutionalItemService.getPublicationVersionsForNamesByTitle(totalHits, numberOfResultsToShow, names, OrderType.getOrderType(sortType));
		} 
		else if( sortElement.equals("download"))
		{
			itemVersions = institutionalItemService.getPublicationVersionsForNamesByDownload(totalHits, numberOfResultsToShow, names, OrderType.getOrderType(sortType));
		}
		for (InstitutionalItemVersionDownloadCount itemVersionDownloadCount:itemVersions) {

			// Find the contributor type
			ContributorPublication cp = new ContributorPublication(itemVersionDownloadCount.getInstitutionalItemVersion());
			for(ItemContributor c :itemVersionDownloadCount.getInstitutionalItemVersion().getItem().getContributors()) {
				if (c.getContributor().getPersonName().getPersonNameAuthority().equals(authority)) {
					cp.setContributor(c.getContributor());
					break;
				}
			}
			
			// Determine the latest publication
			if (timeStamp == null ) {
				timeStamp = itemVersionDownloadCount.getInstitutionalItemVersion().getDateOfDeposit();
				latestItemVersion = itemVersionDownloadCount.getInstitutionalItemVersion();
				latestPublicationContributorType = cp.getContributor().getContributorType().getName();
			} else if (itemVersionDownloadCount.getInstitutionalItemVersion().getDateOfDeposit().compareTo(timeStamp) > 0 ) {
				latestItemVersion = itemVersionDownloadCount.getInstitutionalItemVersion();
				latestPublicationContributorType = cp.getContributor().getContributorType().getName();
			}
			
			// Calculate the total download count
			long downloadCountForItem = downloadStatisticsService.getNumberOfDownloadsForItem(itemVersionDownloadCount.getInstitutionalItemVersion().getItem());
			
			// Set download count for an item
			cp.setDownloadsCount(downloadCountForItem);
			contributorPublications.add(cp);
		}

		// Set most downloaded publication
		List<InstitutionalItemVersionDownloadCount> topDownloads  = institutionalItemService.getPublicationVersionsForNamesByDownload(0, 1, names, OrderType.DESCENDING_ORDER);
		
		if( topDownloads.size() > 0 )
		{
			InstitutionalItemVersionDownloadCount downloadInfo = topDownloads.get(0);
			mostDownloadedCount = downloadInfo.getDownloadCount();
			mostDownloadedItemVersion = downloadInfo.getInstitutionalItemVersion();
			Iterator<ItemContributor> contribIterator = downloadInfo.getInstitutionalItemVersion().getItem().getContributors().iterator();
			    
			boolean done = false;
			while(contribIterator.hasNext() && !done)
			{
			    ItemContributor c = contribIterator.next();
			    if (c.getContributor().getPersonName().getPersonNameAuthority().equals(authority)) {
				    mostDownloadedItemContributorType = c.getContributor().getContributorType().getName();
				    done = true;
				}
			}

		}

		publicationsCount = institutionalItemService.getPublicationCountByPersonName(names);
		
		return SUCCESS;
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


	public void setDownloadStatisticsService(
			DownloadStatisticsService downloadStatisticsService) {
		this.downloadStatisticsService = downloadStatisticsService;
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

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

	public void setTotalDownloads(long totalDownloads) {
		this.totalDownloads = totalDownloads;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}




}
