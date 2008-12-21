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

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDownloadCount;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorService;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;

/**
 * Action to display contributor page
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ViewContributorPage extends ActionSupport  {

	/**  Eclipse generated id */
	private static final long serialVersionUID = -3059332380151731902L;

	/** Id of contributor */
	private Long contributorId;
	
	/** Service class for contributor */
	private ContributorService contributorService;
	
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
	
	/** Contributor */
	private Contributor contributor;
	
	/**
	 * View the contributor page.
	 * 
	 * @return
	 */
	public String execute()
	{
		contributor = contributorService.getContributor(contributorId, false);
		Set<PersonName> names = contributor.getPersonName().getPersonNameAuthority().getNames();

		IrUser user = userService.getUserByPersonNameAuthority(contributor.getPersonName().getPersonNameAuthority().getId());
		
		if (user != null && user.getResearcher() != null && user.getResearcher().isPublic()) {
			researcher = user.getResearcher();
		}
		
		Timestamp timeStamp = null;
		
		// Construct the object with item and download info for display
		List<InstitutionalItemVersion> itemVersions = institutionalItemService.getPublicationVersionsByPersonName(names);
		for (InstitutionalItemVersion itemVersion:itemVersions) {

			// Find the contributor type
			ContributorPublication cp = new ContributorPublication(itemVersion);
			for(ItemContributor c :itemVersion.getItem().getContributors()) {
				if (c.getContributor().getPersonName().getPersonNameAuthority().equals(contributor.getPersonName().getPersonNameAuthority())) {
					cp.setContributor(c.getContributor());
					break;
				}
			}
			
			// Determine the latest publication
			if (timeStamp == null ) {
				timeStamp = itemVersion.getDateOfDeposit();
				latestItemVersion = itemVersion;
				latestPublicationContributorType = cp.getContributor().getContributorType().getName();
			} else if (itemVersion.getDateOfDeposit().compareTo(timeStamp) > 0 ) {
				latestItemVersion = itemVersion;
				latestPublicationContributorType = cp.getContributor().getContributorType().getName();
			}
			
			// Calculate the total download count
			long downloadCountForItem = downloadStatisticsService.getNumberOfDownloadsForItem(itemVersion.getItem());
			totalDownloads = totalDownloads + downloadCountForItem;
			
			// Set download count for an item
			cp.setDownloadsCount(downloadCountForItem);
			contributorPublications.add(cp);
		}

		// Set most downloaded publication
		InstitutionalItemDownloadCount downloadInfo = downloadStatisticsService.getInstitutionalItemDownloadCountByPersonName(names);
		

		if( downloadInfo != null)
		{
			mostDownloadedCount = downloadInfo.getCount();
			InstitutionalItem mostDownloadedItem = downloadInfo.getInstitutionalItem();
			
		    for (int versionNumber = mostDownloadedItem.getVersionedInstitutionalItem().getLargestVersion(); versionNumber > 0 ; versionNumber--) {
			    InstitutionalItemVersion version = mostDownloadedItem.getVersionedInstitutionalItem().getInstitutionalItemVersion(versionNumber);
		
			    Iterator<ItemContributor> contribIterator = version.getItem().getContributors().iterator();
			    
			    boolean done = false;
			    while(contribIterator.hasNext() && !done)
			    {
			    	 ItemContributor c = contribIterator.next();
			    	 if (c.getContributor().getPersonName().getPersonNameAuthority().equals(contributor.getPersonName().getPersonNameAuthority())) {
			    		    mostDownloadedItemVersion = version;
						    mostDownloadedItemContributorType = c.getContributor().getContributorType().getName();
						    done = true;
					 }
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
	
	
	public void setContributorService(ContributorService contributorService) {
		this.contributorService = contributorService;
	}


	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}


	public void setContributorId(Long contributorId) {
		this.contributorId = contributorId;
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


	public Contributor getContributor() {
		return contributor;
	}




}
