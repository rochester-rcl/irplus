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


package edu.ur.ir.user.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;

import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.SearchResults;
import edu.ur.ir.SearchHelper;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserWorkspaceSearchService;

/**
 * Implements the user search service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultUserWorkspaceSearchService implements UserWorkspaceSearchService{
	
	/* eclipse generated id */
	private static final long serialVersionUID = 8399684576467880725L;

	/* Analyzer for dealing with analyzing the search */
	private transient Analyzer analyzer;
	
	/*  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultUserWorkspaceSearchService.class);
	
	/* File system service for loading file system objects */
	private UserFileSystemService userFileSystemService;
	
	/* publishing service for user */
	private UserPublishingFileSystemService userPublishingFileSystemService;

	/* Fields to be searched   */
	private String[] fields = {DefaultUserWorkspaceIndexService.FULL_VERSIONED_FILE_NAME,
			DefaultUserWorkspaceIndexService.BASE_VERSIONED_FILE_NAME,
			DefaultUserWorkspaceIndexService.VERSIONED_FILE_NAME_EXTENSION,
			DefaultUserWorkspaceIndexService.FILE_VERSION_CREATOR,
			DefaultUserWorkspaceIndexService.FILE_BODY_TEXT,
			DefaultUserWorkspaceIndexService.TYPE,
			DefaultUserWorkspaceIndexService.FOLDER_DESCRIPTION,
			DefaultUserWorkspaceIndexService.FOLDER_NAME,
			DefaultUserWorkspaceIndexService.GROUP_WORKSPACE_FILE_ID,
			DefaultUserWorkspaceIndexService.GROUP_WORKSPACE_FOLDER_ID,
			DefaultUserWorkspaceIndexService.PERSONAL_COLLECTION_DESCRIPTION,
			DefaultUserWorkspaceIndexService.PERSONAL_COLLECTION_NAME,
			DefaultUserWorkspaceIndexService.COLLABORATORS,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_ABSTRACT,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_CITATION,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_COLLECTION_NAME,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_CONTENT_TYPE,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_CONTRIBUTOR_NAMES,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_DESCRIPTION,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_FILE_NAME,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_IDENTIFIERS,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_KEY_WORDS,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_LANGUAGE,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_LINK_NAMES,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_NAME,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_PUBLISHER,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_SUB_TITLES,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_SUBMITTER,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_SERIES,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_EXTENTS,
			DefaultUserWorkspaceIndexService.PERSONAL_ITEM_SPONSORS};


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
	 * @see edu.ur.ir.user.UserWorkspaceSearchService#search(java.io.File, java.lang.String, int, int)
	 */
	public SearchResults<FileSystem> search(File personalIndexFolder, String query, int offset,
			int numResults) {
		
		SearchResults<FileSystem> searchResults = new SearchResults<FileSystem>();
		searchResults.setOriginalQuery(query);
		
		query = SearchHelper.prepareMainSearchString(query, true);
		ArrayList<FileSystem> fileSystemObjects = new ArrayList<FileSystem>();
		if( log.isDebugEnabled())
		{
			log.debug("User search results executing query " + query + " on index " + personalIndexFolder.getAbsolutePath());
		}
		
		
		
		String indexFolder = personalIndexFolder.getAbsolutePath();
		IndexSearcher searcher = null;
		IndexReader reader = null;
		try {
			FSDirectory directory = FSDirectory.open(new File(indexFolder));
			reader = IndexReader.open(directory, true);
			searcher = new IndexSearcher(reader);
			QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_35, fields, analyzer);
			parser.setDefaultOperator(QueryParser.AND_OPERATOR);
			
			Query luceneQuery = parser.parse(query);
			TopDocs hits = searcher.search(luceneQuery, 1000);
			searchResults.setTotalHits(hits.totalHits);
			
			int position = offset;
			int addedResults = 0;
			while( hits.totalHits > position  && (addedResults <= numResults))
			{
				if( log.isDebugEnabled())
				{
					log.debug( " adding document at position " + position);
				}
				Document d = searcher.doc(hits.scoreDocs[position].doc);
				fileSystemObjects.add(getFileSystemObject(d));
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
			if( reader != null )
			{
				try {
					reader.close();
				} catch (IOException e) {
					log.error("the reader could not be closed", e);
				}
			}
		}
		searchResults.setObjects(fileSystemObjects);
		
		return searchResults;
	}
	
	
	/**
	 * Gets the file system objects from the database.
	 * 
	 * @param document - document containing the information
	 * @return the file system object loaded from the database.
	 */
	private  FileSystem getFileSystemObject(Document document)
	{
		FileSystem fileSystem = null;
		String type = document.get(DefaultUserWorkspaceIndexService.TYPE);
		log.debug("The type = " + type);
		log.debug("Personal file type " + FileSystemType.PERSONAL_FILE.getType());
		log.debug(" equals personal file " + type.equals(FileSystemType.PERSONAL_FILE.getType()));
		
		if( type.equals(FileSystemType.PERSONAL_FILE.getType()) )
		{
			log.debug( "personal file id = " + document.get(DefaultUserWorkspaceIndexService.PERSONAL_FILE_ID));
			Long personalFileId = NumericUtils.prefixCodedToLong(document.get(DefaultUserWorkspaceIndexService.PERSONAL_FILE_ID));
			fileSystem = userFileSystemService.getPersonalFile(personalFileId, false);
		}
		if( type.equals(FileSystemType.PERSONAL_FOLDER.getType()))
		{
			log.debug( "personal folder id = " + document.get(DefaultUserWorkspaceIndexService.PERSONAL_FOLDER_ID));
			Long personalFolderId = NumericUtils.prefixCodedToLong(document.get(DefaultUserWorkspaceIndexService.PERSONAL_FOLDER_ID));
			fileSystem = userFileSystemService.getPersonalFolder(personalFolderId, false);
		}
		if( type.equals(FileSystemType.SHARED_INBOX_FILE.getType()))
		{
			log.debug( "inbox file id = " + document.get(DefaultUserWorkspaceIndexService.SHARED_INBOX_FILE_ID));
			Long inboxFileId = NumericUtils.prefixCodedToLong(document.get(DefaultUserWorkspaceIndexService.SHARED_INBOX_FILE_ID));
			fileSystem = userFileSystemService.getSharedInboxFile(inboxFileId, false);
		}
		if( type.equals(FileSystemType.PERSONAL_ITEM.getType()))
		{
			log.debug( "personal id = " + document.get(DefaultUserWorkspaceIndexService.PERSONAL_ITEM_ID));
			Long personalItemId = NumericUtils.prefixCodedToLong(document.get(DefaultUserWorkspaceIndexService.PERSONAL_ITEM_ID));
			fileSystem = userPublishingFileSystemService.getPersonalItem(personalItemId, false);
		}
		if( type.equals(FileSystemType.PERSONAL_COLLECTION.getType()))
		{
			log.debug( "personal collection id = " + document.get(DefaultUserWorkspaceIndexService.PERSONAL_COLLECTION_ID));
			Long personalCollectionId = NumericUtils.prefixCodedToLong(document.get(DefaultUserWorkspaceIndexService.PERSONAL_COLLECTION_ID));
			fileSystem = userPublishingFileSystemService.getPersonalCollection(personalCollectionId, false);
		}

		
		return fileSystem;
	}


	/**
	 * Get the user file system service.
	 * 
	 * @return user file system service.
	 */
	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	/**
	 * Set the user file system serivce.
	 * 
	 * @param userFileSystemService
	 */
	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	/**
	 * Set the user publishing file system service.
	 * 
	 * @param userPublishingFileSystemService
	 */
	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}
	


}
