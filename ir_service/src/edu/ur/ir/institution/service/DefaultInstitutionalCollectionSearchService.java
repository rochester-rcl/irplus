package edu.ur.ir.institution.service;

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
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSearchService;
import edu.ur.ir.institution.InstitutionalCollectionService;

/**
 * Default Service to search for institutional collections.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultInstitutionalCollectionSearchService implements InstitutionalCollectionSearchService{

	/** eclipse generated id */
	private static final long serialVersionUID = -3343751267039009528L;
	
	/** Fields to be searched   */
	private String[] fields = {DefaultInstitutionalCollectionIndexService.DESCRIPTION,
			                   DefaultInstitutionalCollectionIndexService.NAME};
	
	/** Service to deal with institutional collection information. */
	private InstitutionalCollectionService institutionalCollectionService;



	/** Analyzer for dealing with analyzing the search */
	private Analyzer analyzer;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultInstitutionalCollectionSearchService.class);
	
	/**
	 * Returns search results for institutional collections.
	 * 
	 * @param institutionalCollectionIndexFolder - folder for the institutional collections
	 * @param query - query to execute
	 * @param offset - offset to start at
	 * @param numResults - number of results.
	 * 
	 * @return - set of users found for the query.
	 */
	public SearchResults<InstitutionalCollection> search(
			File institutionalCollectionIndexFolder, String query, int offset,
			int numResults) {
		SearchResults<InstitutionalCollection> searchResults = new SearchResults<InstitutionalCollection>();
		searchResults.setOriginalQuery(query);
		query = SearchHelper.prepareMainSearchString(query, true);
		ArrayList<InstitutionalCollection> collections = new ArrayList<InstitutionalCollection>();
		if( log.isDebugEnabled())
		{
			log.debug("User search results executing query " + query 
					+ " on index " + institutionalCollectionIndexFolder.getAbsolutePath());
		}
		
		IndexSearcher searcher = null;
		try {
			FSDirectory directory = FSDirectory.open(institutionalCollectionIndexFolder);
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
				
				Long collectionId = NumericUtils.prefixCodedToLong(d.get(DefaultInstitutionalCollectionIndexService.ID));;
				if( log.isDebugEnabled())
				{
				    log.debug( "collection id = " + collectionId);
				}
				    
				InstitutionalCollection collection = institutionalCollectionService.getCollection(collectionId, false);
				collections.add(collection);
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
		searchResults.setObjects(collections);
		return searchResults;
	}
	
	/**
	 * Set the institutional collection service.
	 * 
	 * @param institutionalCollectionService
	 */
	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

	/**
	 * Set the analyzer.
	 * 
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

}
