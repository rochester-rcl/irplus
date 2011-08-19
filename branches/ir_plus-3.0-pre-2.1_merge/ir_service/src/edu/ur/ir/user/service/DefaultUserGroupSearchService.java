/**  
   Copyright 2008-2010 University of Rochester

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


package edu.ur.ir.user.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;

import edu.ur.ir.SearchHelper;
import edu.ur.ir.SearchResults;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupSearchService;
import edu.ur.ir.user.UserGroupService;

/**
 * Service to allows searching across the user group index.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultUserGroupSearchService implements UserGroupSearchService {
	
	/** serial version id */
	private static final long serialVersionUID = -4252567054791127116L;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultUserGroupSearchService.class);
	
	/** Service for dealing with user group information */
	private UserGroupService userGroupService;
	
	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}


	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}


	/** Fields to be searched   */
	private String[] fields = {DefaultUserGroupIndexService.NAME,
			DefaultUserGroupIndexService.DESCRIPTION};
	
	/** Analyzer for dealing with analyzing the search */
	private transient Analyzer analyzer;


	/**
	 * Returns search results for the user groups.
	 * 
	 * @param userGroupIndexFolder - location where the index folder is location 
	 * @param query - query to execute
	 * @param offset - offset to start at
	 * @param numResults - number of results.
	 * 
	 * @return - set of users groups found for the query.
	 */
	public SearchResults<IrUserGroup> search(File userGroupIndexFolder, String query, int offset, int numResults)
	{
		SearchResults<IrUserGroup> searchResults = new SearchResults<IrUserGroup>();
		searchResults.setOriginalQuery(query);
		query = SearchHelper.prepareMainSearchString(query, true);
		ArrayList<IrUserGroup> userGroups = new ArrayList<IrUserGroup>();
		if( log.isDebugEnabled())
		{
			log.debug("User search results executing query " + query 
					+ " on index " + userGroupIndexFolder.getAbsolutePath());
		}
		
		IndexSearcher searcher = null;
		try {
			FSDirectory directory = FSDirectory.open(userGroupIndexFolder);
			searcher = new IndexSearcher(directory, true);
			
			QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_29, fields, analyzer);
			parser.setDefaultOperator(QueryParser.AND_OPERATOR);
			
			Query luceneQuery = parser.parse(query);
			TopDocs hits = searcher.search(luceneQuery, 1000);
			       
			searchResults.setTotalHits(hits.totalHits);
			
			int position = offset;
			int addedResults = 0;
			while( hits.totalHits > position  && (addedResults < numResults))
			{
				if( log.isDebugEnabled())
				{
					log.debug( " adding document at position " + position);
					
				}
				
				Document d = searcher.doc(hits.scoreDocs[position].doc);
				
				Long userGroupId = NumericUtils.prefixCodedToLong(d.get(DefaultUserGroupIndexService.ID));;
				if( log.isDebugEnabled())
				{
				    log.debug( "user group id = " + userGroupId);
				}
				    
				IrUserGroup userGroup = userGroupService.get(userGroupId, false);
				userGroups.add(userGroup);
				addedResults += 1;
				position += 1;
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
		searchResults.setObjects(userGroups);
		return searchResults;
	}

}
