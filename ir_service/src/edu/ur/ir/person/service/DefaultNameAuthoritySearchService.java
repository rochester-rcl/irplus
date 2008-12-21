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


package edu.ur.ir.person.service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import edu.ur.ir.SearchResults;
import edu.ur.ir.SearchHelper;
import edu.ur.ir.person.NameAuthoritySearchService;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.repository.Repository;

/**
 * Implements the name authority search service.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultNameAuthoritySearchService implements NameAuthoritySearchService{
	
	/** Analyzer for dealing with analyzing the search */
	private Analyzer analyzer;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultNameAuthoritySearchService.class);
	
	/** Person service for loading person names */
	private PersonService personService;


	/** Fields to be searched   */
	
	private String[] fields = {DefaultNameAuthorityIndexService.PERSON_NAME_AUTHORITY_ID,
			DefaultNameAuthorityIndexService.NAMES};
	
	/**
	 * Analyzer used to parse the search.
	 * 
	 * @return - analyzer used to parse the search
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * Set the analyzer used to parse the search.
	 * 
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	
	/**
	 *  Execute the search
	 * @see edu.ur.ir.person.NameAuthoritySearchService#search(Repository, String, int, int)
	 */
	public  SearchResults<PersonNameAuthority> search(File nameAuthorityIndex, String query, int offset, int numResults) {
		
		SearchResults<PersonNameAuthority> nameSearchResults = new SearchResults<PersonNameAuthority>();
		nameSearchResults.setOriginalQuery(query);
		query = SearchHelper.prepareMainSearchString(query, true);
		List<PersonNameAuthority> personNameAurhorities = new LinkedList<PersonNameAuthority>();
		
		if( log.isDebugEnabled())
		{
			log.debug("Name search results executing query " + query );
		}
		
		
		
		// If the name index folder doesnot exist
		// then just return empty results
		if (nameAuthorityIndex == null) {
			return nameSearchResults;
		}
		
		String indexFolder = nameAuthorityIndex.getAbsolutePath();
		
		IndexSearcher searcher = null;
		try {
			searcher = new IndexSearcher(indexFolder);
			QueryParser parser = new MultiFieldQueryParser(fields, analyzer);
			parser.setDefaultOperator(QueryParser.AND_OPERATOR);
			
			Query luceneQuery = parser.parse(query);
			Hits hits = searcher.search(luceneQuery);
			nameSearchResults.setTotalHits(hits.length());
			
			log.debug( " No. of hits = " + hits.length() + " offset="+ offset + "  numResults=" + numResults);
			int position = offset;
			int addedResults = 0;
			while( hits.length() > position  && (addedResults < numResults))
			{
				if( log.isDebugEnabled())
				{
					log.debug( " adding document at position " + position);
				}
				Document d = hits.doc(position);
				
				personNameAurhorities.add(getPersonNameAuthority(d));
				position += 1;
				addedResults += 1;
			}
		} catch (Exception e) {
			log.error(e);
		}
		finally
		{
			if( searcher != null )
			{
				try {
					searcher.close();
				} catch (IOException e) {
					log.error("the searcher could not be closed", e);
				}
			}
		} 
		nameSearchResults.setObjects(personNameAurhorities);
		
		return nameSearchResults;
	}

	/**
	 * Gets the names from the database.
	 * 
	 * @param document - document containing the information
	 * @return the names loaded from the database.
	 */
	private PersonNameAuthority getPersonNameAuthority(Document document)
	{
		PersonNameAuthority personNameAuthority = null;

		Long nameAuthorityId = new Long(document.get(DefaultNameAuthorityIndexService.PERSON_NAME_AUTHORITY_ID));
		
		personNameAuthority = personService.getAuthority(nameAuthorityId, false);
		
		return personNameAuthority;
	}

	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
