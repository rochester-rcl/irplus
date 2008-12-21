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

package edu.ur.ir.web.action.institution;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

import edu.ur.ir.FacetSearchHelper;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemSearchService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.search.FacetFilter;
import edu.ur.ir.web.table.Pager;

/**
 * This is for searching across institutional items
 * 
 * @author Nathan Sarr
 *
 */
public class CollectionInstitutionalItemSearch extends Pager {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -7162234617452646016L;

	/**  Logger */
	private static final Logger log = Logger.getLogger(CollectionInstitutionalItemSearch.class);
	
	/** helper that contains data for the searches  */
	private FacetSearchHelper searchDataHelper;
	
	/** Search service for institutional items */
	private InstitutionalItemSearchService institutionalItemSearchService;
	
	/** service for helping with institutional collections */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Query executed by the user */
	private String query;
	
	/** comma separated set of facets passed in by user */
	private String facets;
	
	/** facet values */
	private String facetValues;
	
	/** Display names for the facets */
	private String facetDisplayNames;
	
	/** Facet index to remove if user wants to remove a facet */
	private int facetIndexToRemove = -1;
	
	/** most recent facet selected */
	private String mostRecentFacetName;
	
	/** most recent facet value  */
	private String mostRecentFacetValue;
	
	/** Display name for the facet */
	private String mostRecentFacetDisplayName;
	
	/** Service for dealing with repositories */
	private RepositoryService repositoryService;
	
	private int numberOfHitsToProcessForFacets = 100;
	
	private int numberOfResultsToCollectForFacets = 100;
	
	private int numberOfFacetsToShow = 10;
	
	/** Items retrieved from the list of ids */
	private HashSet<InstitutionalItem> searchInstitutionalItems = new HashSet<InstitutionalItem>();  
	
	/** indicates this the first time the user viewing the search */
	boolean searchInit = true;
	
	/** Service for dealing with institutional items. */
	private InstitutionalItemService institutionalItemService;
	
	/** Indicates this is a search view */
	private String viewType = "search";
	
	/** id of the collection to search */
	private Long collectionId;
	
	/** institutional collection being searched */
	private InstitutionalCollection institutionalCollection;
	
	/** Path for a the set of collections */
	private List<InstitutionalCollection> collectionPath;
	
	/** End position of the search  */
	private int rowEnd;
	
	public CollectionInstitutionalItemSearch()
	{
		numberOfResultsToShow = 20;
		numberOfPagesToShow = 10;
	}
	
	/**
	 * Start searching for items.
	 * 
	 * @return
	 */
	public String execute()
	{
		log.debug("COLLECTION ID = " + collectionId);
		institutionalCollection = institutionalCollectionService.getCollection(collectionId, false);
		collectionPath = institutionalCollectionService.getPath(institutionalCollection);
		log.debug("institutional collection = " + institutionalCollection);
		return SUCCESS;
	}
	
	public String searchCollectionInstitutionalItems() throws CorruptIndexException, IOException, 
	ParseException
	{
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		institutionalCollection = institutionalCollectionService.getCollection(collectionId, false);
		collectionPath = institutionalCollectionService.getPath(institutionalCollection);
		searchInit = false;
		rowEnd = rowStart + numberOfResultsToShow;
		
		searchDataHelper = institutionalItemSearchService.executeSearchWithFacets(query,
				repository.getInstitutionalItemIndexFolder().getFullPath(), 
				numberOfHitsToProcessForFacets, 
				numberOfResultsToCollectForFacets, 
				numberOfFacetsToShow,
				numberOfResultsToShow,
			    rowStart,
				institutionalCollection);
		
		if(rowEnd > searchDataHelper.getHitSize())
		{
			rowEnd = searchDataHelper.getHitSize();
		}
		LinkedList<Long> ids = new LinkedList<Long>();
		ids.addAll(searchDataHelper.getHitIds());
		
		searchInstitutionalItems.addAll(institutionalItemService.getInstitutionalItems(ids));
		
		return SUCCESS;
	}

	
	/**
	 * Excecute the search for the user this is for the initial search.
	 * 
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 */
	public String filteredSearchCollectionInstitutionalItems() throws CorruptIndexException, IOException, 
	ParseException
	{
		rowEnd = rowStart + numberOfResultsToShow;
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		institutionalCollection = institutionalCollectionService.getCollection(collectionId, false);
		collectionPath = institutionalCollectionService.getPath(institutionalCollection);
		searchInit = false;
		
		log.debug("Executing query  with facets : " + query);
		log.debug("Facet Names =  " + facets);
		log.debug("Facet Values =  " + facetValues);

		log.debug("mostRecentFacetName =  " + mostRecentFacetName );
		log.debug("mostRecentFacetValue =  " + mostRecentFacetValue );
		
		LinkedList<String> facetDisplayNames = new LinkedList<String>();
		LinkedList<String> facetNames = new LinkedList<String>();
		LinkedList<String> facetValues = new LinkedList<String>();
		
		// parse the facet internal names
		
		if( facets != null )
		{
		    StringTokenizer tokenizer = new StringTokenizer(facets, "|");
		    while(tokenizer.hasMoreElements())
		    {
			    String nextFacet = tokenizer.nextToken();
			    facetNames.add(nextFacet.trim());
		    }
		}
		
		if( this.facetDisplayNames != null )
		{
		    // parse the facet display names
			StringTokenizer tokenizer = new StringTokenizer(this.facetDisplayNames, "|");
		
		
		    while(tokenizer.hasMoreElements())
		    {
			    String nextFacetDisplayName = tokenizer.nextToken();
			    facetDisplayNames.add(nextFacetDisplayName.trim());
		    }
		}
		
		if( this.facetValues != null )
		{
		    // parse the facet values
			StringTokenizer tokenizer = new StringTokenizer(this.facetValues, "|");
		   
		
		    while(tokenizer.hasMoreElements())
		    {
			    String nextFacet = tokenizer.nextToken();
			    facetValues.add(nextFacet.trim());
		    }
		}
		
		if( facetValues.size() != facetNames.size())
		{
			throw new IllegalStateException( "facet values and facet names must be the same size" +
					" facet values size = " + facetValues.size() + 
					" facet names size = " + facetNames.size() );
		}
		
		LinkedList<FacetFilter> filters = new LinkedList<FacetFilter>();
		
		log.debug("facetValues size = " + facetValues.size());
		log.debug("facet names size = " + facetNames.size());
		
		for( int index = 0 ; index < facetValues.size(); index++ )
		{
			// don't add the facet if the user has choosen to remove it
			if( this.facetIndexToRemove != index )
			{
		        filters.add(new FacetFilter(facetNames.get(index), facetValues.get(index), facetDisplayNames.get(index)));	
			}
		}
		
		
		if( mostRecentFacetName != null && !mostRecentFacetName.trim().equals(""))
		{
		    filters.add(new FacetFilter(mostRecentFacetName, mostRecentFacetValue, mostRecentFacetDisplayName));
		    if( facets != null && !facets.trim().equals(""))
		    {
		        facets = facets + "|" + mostRecentFacetName;
		        this.facetValues = this.facetValues + "|" + mostRecentFacetValue;
		        this.facetDisplayNames = this.facetDisplayNames + "|" + mostRecentFacetDisplayName;
		    }
		    else
		    {
		    	facets = mostRecentFacetName;
		    	this.facetValues = mostRecentFacetValue;
		    	this.facetDisplayNames = mostRecentFacetDisplayName;
		    }
		}
		
		
	
		
		if(facetIndexToRemove >= 0 )
		{
			facets = "";
			this.facetValues = "";
			this.facetDisplayNames = "";
		
		    // rebuild the facets if the user has selected to remove one
		    for(int index = 0; index < facetValues.size(); index ++)
		    {
			    if( index != facetIndexToRemove)
			    {
			    	if( facets != null && !facets.trim().equals(""))
				    {
			    	    facets = facets + "|" + facetNames.get(index);
			    	    this.facetValues = this.facetValues + "|" + facetValues.get(index);
			    	    this.facetDisplayNames = this.facetDisplayNames + "|" + facetDisplayNames.get(index);
				    }
			    	else
			    	{
			    		facets = facetNames.get(index);
				    	this.facetValues = facetValues.get(index);
				    	this.facetDisplayNames = facetDisplayNames.get(index);
			    	}
			    }
		    }
	    }
		
		
		
		log.debug("Filter Size = " + filters.size() );

		searchDataHelper = institutionalItemSearchService.executeSearchWithFacets(query,
				filters,
				repository.getInstitutionalItemIndexFolder().getFullPath(), 
				numberOfHitsToProcessForFacets, 
				numberOfResultsToCollectForFacets,
				numberOfFacetsToShow,
				numberOfResultsToShow,
			    rowStart,
				institutionalCollection);
		
		log.debug("searchDataHelper hit size = " + searchDataHelper.getHitSize());
		if(rowEnd > searchDataHelper.getHitSize())
		{
			rowEnd = searchDataHelper.getHitSize();
		}
		LinkedList<Long> ids = new LinkedList<Long>();
		ids.addAll(searchDataHelper.getHitIds());
		
		searchInstitutionalItems.addAll(institutionalItemService.getInstitutionalItems(ids));
		
		
		return SUCCESS;
		
	}

	

	
	/**
	 * Returns the total number of hits 
	 * 
	 * @see edu.ur.ir.web.table.Pager#getTotalHits()
	 */
	public int getTotalHits() {
		return searchDataHelper.getHitSize();
	}


	public InstitutionalItemSearchService getInstitutionalItemSearchService() {
		return institutionalItemSearchService;
	}


	public void setInstitutionalItemSearchService(
			InstitutionalItemSearchService institutionalItemSearchSevice) {
		this.institutionalItemSearchService = institutionalItemSearchSevice;
	}


	public RepositoryService getRepositoryService() {
		return repositoryService;
	}


	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}


	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public FacetSearchHelper getSearchDataHelper() {
		return searchDataHelper;
	}


	public void setSearchDataHelper(FacetSearchHelper searchDataHelper) {
		this.searchDataHelper = searchDataHelper;
	}


	public HashSet<InstitutionalItem> getSearchInstitutionalItems() {
		return searchInstitutionalItems;
	}


	public void setSearchInstitutionalItems(HashSet<InstitutionalItem> insitutionalItems) {
		this.searchInstitutionalItems = insitutionalItems;
	}


	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}


	public void setInstitutionalItemService(
			InstitutionalItemService instituionalItemService) {
		this.institutionalItemService = instituionalItemService;
	}


	public String getViewType() {
		return viewType;
	}


	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public boolean isSearchInit() {
		return searchInit;
	}

	public void setSearchInit(boolean searchInit) {
		this.searchInit = searchInit;
	}

	public String getFacets() {
		return facets;
	}

	public void setFacets(String facets) {
		this.facets = facets;
	}

	public String getCurrentFacet() {
		return mostRecentFacetName;
	}

	public void setCurrentFacet(String currentFacet) {
		this.mostRecentFacetName = currentFacet;
	}

	public String getFacetValues() {
		return facetValues;
	}

	public void setFacetValues(String facetValues) {
		this.facetValues = facetValues;
	}

	public String getMostRecentFacetValue() {
		return mostRecentFacetValue;
	}

	public void setMostRecentFacetValue(String mostRecentFacetValue) {
		this.mostRecentFacetValue = mostRecentFacetValue;
	}

	public String getMostRecentFacetName() {
		return mostRecentFacetName;
	}

	public void setMostRecentFacetName(String mostRecentFacetName) {
		this.mostRecentFacetName = mostRecentFacetName;
	}

	public String getMostRecentFacetDisplayName() {
		return mostRecentFacetDisplayName;
	}

	public void setMostRecentFacetDisplayName(String mostRecentFacetDisplayName) {
		this.mostRecentFacetDisplayName = mostRecentFacetDisplayName;
	}

	public String getFacetDisplayNames() {
		return facetDisplayNames;
	}

	public void setFacetDisplayNames(String facetDisplayNames) {
		this.facetDisplayNames = facetDisplayNames;
	}

	public int getFacetIndexToRemove() {
		return facetIndexToRemove;
	}

	public void setFacetIndexToRemove(int facetIndexToRemove) {
		this.facetIndexToRemove = facetIndexToRemove;
	}

	public Long getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}

	public InstitutionalCollection getInstitutionalCollection() {
		return institutionalCollection;
	}

	public void setInstitutionalCollection(InstitutionalCollection collection) {
		this.institutionalCollection = collection;
	}

	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}



	public List<InstitutionalCollection> getCollectionPath() {
		return collectionPath;
	}



	public void setCollectionPath(List<InstitutionalCollection> collectionPath) {
		this.collectionPath = collectionPath;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

}
