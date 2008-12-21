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

package edu.ur.ir.institution;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

import edu.ur.ir.FacetSearchHelper;
import edu.ur.ir.search.FacetFilter;

/** 
 * Interface for searching for institutional items.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalItemSearchService {
	
	public static final String AUTHOR_MAP = "authors";
	public static final String LANGUAGE_MAP = "languages";
	public static final String SUBJECT_MAP = "subjects";
	public static final String FORMAT_MAP = "formats";
	public static final String COLLECTION_MAP = "collections";
	
	/**
	 * Get the facets and search results 
	 * 
	 * @param mainQueryString - main query to execute
	 * @param indexFolder - folder where index exists
	 * @param numberOfHitsToProcessForFacets - number of hits to look through when processing facets
	 * @param numberOfResultsToCollectForFacets - for each facet how many to grab for searching
	 * @param numberOfFactsToShow - once the facet count has been determined, how many of the top facets should be shown
	 * @param numberOfIdsToCollect - number of ids to collect and return.
	 * @param idsToCollectStartPosition - location in the list to start extracting ids
	 * 
	 * @return Search helper which contains the found information.
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 */
	public FacetSearchHelper executeSearchWithFacets(String mainQueryString,
			String indexFolder, 
			int numberOfHitsToProcessForFacets, 
			int numberOfResultsToCollectForFacets,
			int numberOfFactsToShow,
			int numberOfIdsToCollect,
			int idsToCollectStartPosition) 
	        throws CorruptIndexException, IOException, ParseException;
	
	/**
	 * Get the facets and search results for the specified collection.  This searches all children
	 * collections as well
	 * 
	 * @param mainQueryString - main query to execute
	 * @param indexFolder - folder where index exists
	 * @param numberOfHitsToProcessForFacets - number of hits to look through when processing facets
	 * @param numberOfResultsToCollectForFacets - for each facet how many to grab
	 * @param numberOfFactsToShow - once the facet count has been determined, how many of the top facets should be shown
	 * @param numberOfIdsToCollect - number of ids to collect and return.
	 * @param idsToCollectStartPosition - location in the list to start extracting ids
	 * @param institutionalCollection - top most collection to start in
	 * 
	 * @return Search helper which contains the found information.
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 */
	public FacetSearchHelper executeSearchWithFacets(String mainQueryString,
			String indexFolder, 
			int numberOfHitsToProcessForFacets, 
			int numberOfResultsToCollectForFacets,
			int numberOfFactsToShow,
			int numberOfIdsToCollect,
			int idsToCollectStartPosition,
			InstitutionalCollection institutionalCollection) 
	        throws CorruptIndexException, IOException, ParseException;
	
	
	/**
	 * Get the facets and search results - applying the specified filters for the specified collection.  This searches all children
	 * of the collections as well
	 * 
	 * @param mainQueryString - main query to execute
	 * @param filters - filters to apply to the main query (path user has taken)
	 * @param indexFolder - folder where index exists
	 * @param numberOfHitsToProcessForFacets - number of hits to look through when processing facets
	 * @param numberOfResultsToCollectForFacets - for each facet how many results to collect
	 * @param numberOfFactsToShow - once the facet count has been determined, how many of the top facets should be shown
	 * @param numberOfIdsToCollect - number of ids to collect and return.
	 * @param idsToCollectStartPosition - location in the list to start extracting ids
	 * 
	 * @return Search helper which contains the found information.
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 */
	public FacetSearchHelper executeSearchWithFacets(String mainQueryString,
			List<FacetFilter> filters,
			String indexFolder, 
			int numberOfHitsToProcessForFacets, 
			int numberOfResultsToCollectForFacets,
			int numberOfFactsToShow,
			int numberOfIdsToCollect,
			int idsToCollectStartPosition) 
	        throws CorruptIndexException, IOException, ParseException;
	
	/**
	 * Get the facets and search results - applying the specified filters
	 * 
	 * @param mainQueryString - main query to execute
	 * @param filters - filters to apply to the main query (path user has taken)
	 * @param indexFolder - folder where index exists
	 * @param numberOfHitsToProcessForFacets - number of hits to look through when processing facets
	 * @param numberOfResultsToCollectForFacets - for each facet how many results to collect 
	 * @param numberOfFactsToShow - once the facet count has been determined, how many of the top facets should be shown
	 * @param numberOfIdsToCollect - number of ids to collect and return.
	 * @param idsToCollectStartPosition - location in the list to start extracting ids
	 * @param collection - collection to look in - this includes all children
	 * @return
	 */
	public FacetSearchHelper executeSearchWithFacets(
			String mainQueryString, 
			List<FacetFilter> filters, 
			String indexFolder,
			int numberOfHitsToProcessForFacets,
			int numberOfResultsToCollectForFacets,
			int numberOfFactsToShow,
			int numberOfIdsToCollect,
			int idsToCollectStartPosition,
			InstitutionalCollection collection)throws CorruptIndexException, IOException, ParseException ;
	
	/**
	 * Analyzer for search.
	 * 
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer);

}
