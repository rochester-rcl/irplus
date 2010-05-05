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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.NumberTools;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.misc.ChainedFilter;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.OpenBitSet;
import org.apache.lucene.util.OpenBitSetDISI;

import edu.ur.ir.FacetSearchHelper;
import edu.ur.ir.SearchHelper;
import edu.ur.ir.researcher.ResearcherSearchService;
import edu.ur.ir.search.FacetFilter;
import edu.ur.ir.search.FacetResult;
import edu.ur.ir.search.FacetResultHitComparator;

/**
 * Excecute the search and determine facets
 * 
 * @author Sharmila Rangaanthan
 *
 */
public class DefaultResearcherSearchService implements ResearcherSearchService {

	/** eclipse generated id */
	private static final long serialVersionUID = -4377980407262168156L;

	/** Analyzer to use for parsing the queries */
	private Analyzer analyzer;
	
	/**  Logger for editing a file database. */
	private static final Logger log = Logger.getLogger(DefaultResearcherSearchService.class);
	
	/** max number of hits to retrieve */
	private int maxNumberOfMainQueryHits = 10000;
	
	
	/** fields to search in the index*/
	private static final String[] fields = 
	{DefaultResearcherIndexService.DEPARTMENT, 
	 DefaultResearcherIndexService.FIRST_NAME,
	 DefaultResearcherIndexService.LAST_NAME,
	 DefaultResearcherIndexService.TITLE,
	 DefaultResearcherIndexService.FIELD,
	 DefaultResearcherIndexService.RESEARCH_INTEREST,
	 DefaultResearcherIndexService.TEACHING_INTEREST,
	 DefaultResearcherIndexService.EMAIL,
	 DefaultResearcherIndexService.KEY_WORDS};
	
	
	/**
	 * Get the facets and results
	 * @see edu.ur.ir.researcher.ResearcherSearchService#executeSearchWithFacets(java.lang.String, java.lang.String, int, int, int, int)
	 */
	public FacetSearchHelper executeSearchWithFacets(
			String mainQueryString,
			String indexFolder, 
			int numberOfHitsToProcessForFacets, 
			int numberOfResultsToCollectForFacets,
			int numberOfFactsToShow,
			int numberOfIdsToCollect,
			int idsToCollectStartPosition ) 
	        throws CorruptIndexException, IOException, ParseException 
	{
		if( searchDirectoryIsEmpty(indexFolder) || isInvalidQuery(mainQueryString))
		{
			return new FacetSearchHelper(new HashSet<Long>(), 0, new HashMap<String, Collection<FacetResult>>(), mainQueryString);
		}
		
		IndexSearcher searcher = new IndexSearcher(indexFolder);
		IndexReader reader = searcher.getIndexReader();
		QueryParser parser = new MultiFieldQueryParser(fields, analyzer);
		parser.setDefaultOperator(QueryParser.AND_OPERATOR);
		
		
		HashMap<String, Collection<FacetResult>> facetResults = new HashMap<String, Collection<FacetResult>>();
		
		// execute the main query - we will use this to extract data to determine the facet searches
		String executedQuery = SearchHelper.prepareMainSearchString(mainQueryString, true);
		Query mainQuery = parser.parse(executedQuery);
		if( log.isDebugEnabled() )
		{
			log.debug("main query = " + executedQuery );
		}
		
		TopDocs hits = searcher.search(mainQuery, maxNumberOfMainQueryHits);
		
		// determine the set of data we should use to determine facets
		HashMap<String, HashMap<String, FacetResult>> possibleFacets = this.generateFacetSearches(hits, 
				numberOfHitsToProcessForFacets, 
				numberOfResultsToCollectForFacets,
				searcher);

		QueryWrapperFilter mainQueryWrapper = new QueryWrapperFilter(mainQuery);
		log.debug("executeSearchWithFacets 1 query = " + mainQuery);
		
	    // get the bitset for main query
		DocIdSet mainQueryBits = mainQueryWrapper.getDocIdSet(reader);
		

		// process the data and determine the facets
        FacetSearchHelper helper = this.processPossibleFacets(possibleFacets, 
        		reader, 
        		mainQueryBits, 
        		facetResults, 
        		hits, 
        		numberOfIdsToCollect, 
        		idsToCollectStartPosition,
        		numberOfFactsToShow,
        		mainQueryString, 
        		searcher);
		helper.setExecutedQuery(executedQuery);
        searcher.close();
        return helper;
	}
	
	/**
	 * Get the number of hits for a given facet using the bitsets.
	 * 
	 * @param baseBitSet - original query bitset
	 * @param filterBitSet - filtered bit set
	 * @return the count for the number of hits
	 */
	private long getFacetHitCount(OpenBitSet baseBitSet, OpenBitSet filterBitSet)
	{
		filterBitSet.and(baseBitSet);
		return filterBitSet.cardinality();
	}
	
	/**
	 * This determines the possible facets for each of the categories.  For example - possible authors 
	 * for the display.  This does not care about counts later on counts will be important.
	 * 
	 * @param hits
	 * @param numberOfHitsToProcess
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	private HashMap<String, HashMap<String, FacetResult>> generateFacetSearches(TopDocs hits, 
			int numberOfHitsToProcess, 
			int numberOfResultsToCollect,
			IndexSearcher searcher) throws CorruptIndexException, IOException
	{
		
		HashMap<String, HashMap<String, FacetResult>> facets = new HashMap<String, HashMap<String, FacetResult>>();
		HashMap<String, FacetResult> departmentsMap = new HashMap<String, FacetResult>();
		HashMap<String, FacetResult> fieldsMap = new HashMap<String, FacetResult>();
		HashMap<String, FacetResult> keywordsMap = new HashMap<String, FacetResult>();
		
		facets.put(DEPARTMENT_MAP, departmentsMap);
		facets.put(FIELD_MAP, fieldsMap);
		facets.put(KEYWORD_MAP, keywordsMap);
		
		int length = hits.totalHits;
		
		if (length <= numberOfHitsToProcess)
		{
			numberOfHitsToProcess = length;
		}
		
		for( int index = 0; index < numberOfHitsToProcess; index++)
		{
			Document doc = searcher.doc(hits.scoreDocs[index].doc);
			
			String departments = doc.get(DefaultResearcherIndexService.DEPARTMENT);
			
			String fields = doc.get(DefaultResearcherIndexService.FIELD);
			
			String keywords = doc.get(DefaultResearcherIndexService.KEY_WORDS);
			
			if( fields != null )
			{
				fields = fields.trim();
			}
			
			if( departments != null && departmentsMap.size() < numberOfResultsToCollect)
			{
			    StringTokenizer tokenizer = new StringTokenizer(departments, DefaultResearcherIndexService.SEPERATOR);
			    while(tokenizer.hasMoreElements() && departmentsMap.size() < numberOfResultsToCollect)
			    {
			    	String department = tokenizer.nextToken().trim();
			    	FacetResult f = departmentsMap.get(department);
					if( f == null )
					{
						f = new FacetResult(1l, DefaultResearcherIndexService.DEPARTMENT, department);
						departmentsMap.put(department, f);
					}
			    }
			}
			
			if( fields != null && fieldsMap.size() < numberOfResultsToCollect )
			{
			    StringTokenizer tokenizer = new StringTokenizer(fields, DefaultResearcherIndexService.SEPERATOR);
			    while(tokenizer.hasMoreElements() && fieldsMap.size() < numberOfResultsToCollect)
			    {
			    	String field = tokenizer.nextToken().trim();
			    	FacetResult f = fieldsMap.get(field);
			    	if( f == null )
			    	{
						f = new FacetResult(1l, DefaultResearcherIndexService.FIELD, field);
						fieldsMap.put(field, f);
			    	}
			    }
			}
			
			if( keywords != null && keywordsMap.size() < numberOfResultsToCollect)
			{
			    StringTokenizer tokenizer = new StringTokenizer(keywords, DefaultResearcherIndexService.SEPERATOR);
			    while(tokenizer.hasMoreElements() && keywordsMap.size() < numberOfResultsToCollect)
			    {
			    	String keyword = tokenizer.nextToken().trim();
			    	FacetResult f = keywordsMap.get(keyword);
			    	if( f == null )
			    	{
			    		f = new FacetResult(1l, DefaultResearcherIndexService.KEY_WORDS, keyword);
			    		keywordsMap.put(keyword, f);
			    	}
			    }
			}
		}
		return facets;
	}
	
	
	/**
	 * Determines the number of hits for each facet across the main query.
	 * 
	 * @param facets
	 * @param reader
	 * @param mainQueryBits
	 * @throws ParseException
	 * @throws IOException
	 */
	private void processFacetCategory(Collection<FacetResult> facets, 
			IndexReader reader, 
			DocIdSet mainQueryBits) 
	   throws ParseException, IOException
	{
		for(FacetResult f : facets )
		{
			QueryParser subQueryParser = new QueryParser(f.getField(), analyzer);
			subQueryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
			String fixedQuery = SearchHelper.prepareFacetSearchString(f.getFacetName(), false);
			fixedQuery = "\"" + fixedQuery + "\"";
			Query subQuery = subQueryParser.parse(fixedQuery);
			
			if(log.isDebugEnabled())
			{
				log.debug("Fiexed query in process Facet Category = " + fixedQuery);
			}
			
			QueryWrapperFilter subQueryWrapper = new QueryWrapperFilter(subQuery);
		    DocIdSet subQueryBits = subQueryWrapper.getDocIdSet(reader);
		
		    OpenBitSetDISI mainQuerybitSet = new OpenBitSetDISI(mainQueryBits.iterator(), maxNumberOfMainQueryHits);
		    OpenBitSetDISI subQuerybitSet = new OpenBitSetDISI(subQueryBits.iterator(), maxNumberOfMainQueryHits);
			
			long count = getFacetHitCount(mainQuerybitSet, subQuerybitSet);
			f.setHits(count);
		}
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public String[] getFields() {
		return fields;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.researcher.ResearcherSearchService#executeSearchWithFacets(java.lang.String, java.util.Set, java.lang.String, int, int, int)
	 */
	public FacetSearchHelper executeSearchWithFacets(
			String mainQueryString, 
			List<FacetFilter> filters, 
			String indexFolder,
			int numberOfHitsToProcessForFacets,
			int numberOfResultsToCollectForFacets, 
			int numberOfFactsToShow,
			int numberOfIdsToCollect,
			int idsToCollectStartPosition)
			throws CorruptIndexException, IOException, ParseException {

		
		if( searchDirectoryIsEmpty(indexFolder) || isInvalidQuery(mainQueryString))
		{
			return new FacetSearchHelper(new HashSet<Long>(), 0, new HashMap<String, Collection<FacetResult>>(), mainQueryString);
		}
		
		IndexSearcher searcher = new IndexSearcher(indexFolder);
		IndexReader reader = searcher.getIndexReader();
		QueryParser parser = new MultiFieldQueryParser(fields, analyzer);
		parser.setDefaultOperator(QueryParser.AND_OPERATOR);
		
		HashMap<String, Collection<FacetResult>> facetResults = new HashMap<String, Collection<FacetResult>>();
		
		// execute the main query - we will use this to extract data to determine the facet searches
		String executedQuery = SearchHelper.prepareMainSearchString(mainQueryString, true);
		
		if( log.isDebugEnabled() )
		{
			log.debug("parsed query = " +  executedQuery.trim());
		}
		Query mainQuery = parser.parse( executedQuery);

		
		//create a filter for the main query
		QueryWrapperFilter mainQueryWrapper = new QueryWrapperFilter(mainQuery);
		
	    // get the bitset for main query
		DocIdSet mainQueryBits = mainQueryWrapper.getDocIdSet(reader);
	    
	    
		TopDocs hits = null;
		
		if( filters.size() > 0 )
		{
		    // create a filter that will match the main query plus all other filters
			List<Filter> luceneFilters = getSubQueryFilters(filters, searcher);		
		    Filter filter = new ChainedFilter(luceneFilters.toArray(new Filter[luceneFilters.size()]), ChainedFilter.AND);
		    if(log.isDebugEnabled())
		    {
		    	log.debug("filter = " + filter);
		    }
		    
		    // apply the facets and include them in the main query bit set
			DocIdSet filterQueryBits = filter.getDocIdSet(reader);
			 
			// apply the facets and include them in the main query bit set
		    OpenBitSetDISI mainQueryBitSet = new OpenBitSetDISI(mainQueryBits.iterator(), maxNumberOfMainQueryHits);
		    OpenBitSetDISI filterBitSet = new OpenBitSetDISI(filterQueryBits.iterator(), maxNumberOfMainQueryHits);
		    mainQueryBitSet.and(filterBitSet);
		    
		    hits = searcher.search(mainQuery, filter, maxNumberOfMainQueryHits);
		    log.debug(" executeSearchWithFacets 2 = mainQuery = " + executedQuery + " filter = " + filter);	    
		}
		else
		{
			hits = searcher.search(mainQuery, maxNumberOfMainQueryHits);
		    log.debug(" executeSearchWithFacets 3 = mainQuery = " + mainQuery);	    

		}
		
		// determine the set of data we should use to determine facets
		HashMap<String, HashMap<String, FacetResult>> possibleFacets = this.generateFacetSearches(hits, 
				numberOfHitsToProcessForFacets, numberOfResultsToCollectForFacets, searcher);


		FacetSearchHelper helper = processPossibleFacets(possibleFacets, 
        		reader, 
        		mainQueryBits, 
        		facetResults, 
        		hits, 
        		numberOfIdsToCollect, 
        		idsToCollectStartPosition,
        		numberOfFactsToShow,
        		mainQueryString,
        		searcher);
		
        helper.setExecutedQuery(executedQuery);
        helper.setFacetTrail(filters);
        
        searcher.close();
        return helper;
	}
	
	
	/**
	 * Execute the sub query facets and return the search results
	 * @throws ParseException 
	 * @throws IOException 
	 */
	private List<Filter> getSubQueryFilters( List<FacetFilter> filters, 
			IndexSearcher searcher) throws ParseException, IOException
	{
		List<Filter> luceneFilters = new LinkedList<Filter>();
		
		for(FacetFilter filter : filters)
		{	
			if(log.isDebugEnabled())
			{
				log.debug("adding filter for field " + filter.getField() + " and query " + filter.getQuery());
			}
		    QueryParser subQueryParser = new QueryParser(filter.getField(), analyzer);
		    subQueryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
		    String fixedQuery = SearchHelper.prepareFacetSearchString(filter.getQuery(), false);
		    fixedQuery = "\"" + fixedQuery + "\"";
		    Query subQuery = subQueryParser.parse(fixedQuery);
		    if(log.isDebugEnabled())
			{
				log.debug("sub query ing getSubQueryFilters is " + fixedQuery);
			}
		    luceneFilters.add(new QueryWrapperFilter(subQuery));
		}
		
		return luceneFilters;
	}
	
	
	/** 
	 * Process the possible facets and determine the number of hits for each facet accross the main query.
	 * 
	 * @param possibleFacets - possible facets to show to the user
	 * @param reader - lucene reader
	 * @param mainQueryBits - bitset from the main query
	 * @param facetResults - set of facet results
	 * @param hits - number of hits
	 * @param numberOfIdsToCollect - number of ids to collect and show to user
	 * @param mainQueryString - main query 
	 * 
	 * @return - search helper
	 * @throws ParseException
	 * @throws IOException
	 */
	private FacetSearchHelper processPossibleFacets(HashMap<String, HashMap<String, FacetResult>> possibleFacets, 
			IndexReader reader, 
			DocIdSet mainQueryBits, 
			HashMap<String, 
			Collection<FacetResult>> facetResults, 
			TopDocs hits,
			int numberOfIdsToCollect, 
			int idsToCollectStartPosition,
			int numberOfFacetsToShow,
			String mainQueryString,
			IndexSearcher searcher) throws ParseException, IOException
	{
		FacetResultHitComparator facetResultHitComparator = new FacetResultHitComparator();
		// get the authors and create a facet for each author
		// determine the number of hits the author has in the main query
		HashMap<String, FacetResult> departmentFacetMap = possibleFacets.get(DEPARTMENT_MAP);
		LinkedList<FacetResult> departmentFacets = new LinkedList<FacetResult>();
		departmentFacets.addAll(departmentFacetMap.values()); 
		processFacetCategory(departmentFacets, reader, mainQueryBits);	
		Collections.sort(departmentFacets, facetResultHitComparator );

		// final holder of facets
		LinkedList<FacetResult> finalDepartmentFacets;
		
		if( departmentFacets.size() < numberOfFacetsToShow )
		{
			finalDepartmentFacets = departmentFacets;
		}
		else
		{
			finalDepartmentFacets = new LinkedList<FacetResult>();
			for( int index = 0; index < numberOfFacetsToShow; index++ )
			{
				finalDepartmentFacets.add(departmentFacets.get(index));
			}
		}
        
		facetResults.put(DEPARTMENT_MAP,finalDepartmentFacets);
		
		// get the subjects and create a facet for each subject
		// determine the number of hits the subject has in the main query
		HashMap<String, FacetResult> keywordFacetMap = possibleFacets.get(KEYWORD_MAP);
		LinkedList<FacetResult> keywordFacets = new LinkedList<FacetResult>();
		keywordFacets.addAll(keywordFacetMap.values()); 
		processFacetCategory(keywordFacets, reader, mainQueryBits);		
		Collections.sort(keywordFacets, facetResultHitComparator);
		
		// final holder of facets
		LinkedList<FacetResult> finalKeywordFacets;
		
		if( keywordFacets.size() < numberOfFacetsToShow )
		{
			finalKeywordFacets = keywordFacets;
		}
		else
		{
			finalKeywordFacets = new LinkedList<FacetResult>();
			for( int index = 0; index < numberOfFacetsToShow; index++ )
			{
				finalKeywordFacets.add(keywordFacets.get(index));
			}
		}
		
		
		facetResults.put(KEYWORD_MAP, finalKeywordFacets);
		
		// get the language and create a facet for each language
		// determine the number of hits the language has in the main query
		HashMap<String, FacetResult> fieldFacetMap = possibleFacets.get(FIELD_MAP);
		LinkedList<FacetResult> fieldFacets = new LinkedList<FacetResult>();
		fieldFacets.addAll(fieldFacetMap.values()); 
		processFacetCategory(fieldFacets, reader, mainQueryBits);
		Collections.sort(fieldFacets, facetResultHitComparator);
		
		// final holder of facets
		LinkedList<FacetResult> finalFieldFacets;
		
		if( fieldFacets.size() < numberOfFacetsToShow )
		{
			finalFieldFacets = fieldFacets;
		}
		else
		{
			finalFieldFacets = new LinkedList<FacetResult>();
			for( int index = 0; index < numberOfFacetsToShow; index++ )
			{
				finalFieldFacets.add(fieldFacets.get(index));
			}
		}
		
		facetResults.put(FIELD_MAP, finalFieldFacets);
		
		HashSet<Long> ids = new HashSet<Long>();
		
		// end position of ids to collect will be start position plus the number to collect
		int endPosition = idsToCollectStartPosition + numberOfIdsToCollect;
		
		// make sure that the end position is set up correctly
	    if(hits.totalHits < endPosition )
	    {
	    	endPosition = hits.totalHits;
	    }

	    
	    for( int index = idsToCollectStartPosition; index < endPosition; index ++ )
	    {
	    	Document doc = searcher.doc(hits.scoreDocs[index].doc);
	    	ids.add(NumberTools.stringToLong(doc.get(DefaultResearcherIndexService.ID)));
	    }
        FacetSearchHelper helper = new FacetSearchHelper(ids, hits.totalHits, facetResults, mainQueryString);
        return helper;
	}
	
	/**
	 * Determine if the search directory is empty.
	 * 
	 * @param indexFolder
	 * @return
	 */
	private boolean searchDirectoryIsEmpty(String indexFolder )
	{
		
		File indexFolderLocation = new File(indexFolder);
		if( indexFolderLocation.isDirectory() && indexFolderLocation.exists())
		{
			// do not search if the folder is empty
			String[] files = indexFolderLocation.list();
			if( files == null || files.length <= 0 )
			{
				log.debug("no index files found");
				return true;
			}
			return false;
		}
		else
		{
			log.debug("search directory is not a directory or does not exist " + indexFolder);
	        return true;
		}
	}
	
	private boolean isInvalidQuery(String mainQuery)
	{
		log.debug("problem with main query " + mainQuery);
		return (mainQuery == null || mainQuery.trim().equals("") || mainQuery.trim().equals("*"));
		
	}

	public int getMaxNumberOfMainQueryHits() {
		return maxNumberOfMainQueryHits;
	}

	public void setMaxNumberOfMainQueryHits(int maxNumberOfMainQueryHits) {
		this.maxNumberOfMainQueryHits = maxNumberOfMainQueryHits;
	}

	
	

}
