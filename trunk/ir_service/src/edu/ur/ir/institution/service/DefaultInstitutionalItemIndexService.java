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


package edu.ur.ir.institution.service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumberTools;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.file.IrFileIndexingFailureRecord;
import edu.ur.ir.file.IrFileIndexingFailureRecordDAO;
import edu.ur.ir.index.FileTextExtractor;
import edu.ur.ir.index.FileTextExtractorService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemContentType;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemIdentifier;
import edu.ur.ir.item.ItemLink;
import edu.ur.ir.item.ItemReport;
import edu.ur.ir.item.ItemSponsor;
import edu.ur.ir.item.ItemTitle;
import edu.ur.ir.person.PersonName;

/**
 * Implementation of the Institutional Item index service. 
 * 
 *  There are analyzed and non analyzed fields.  The non
 *  analyzed allows for exact matching on particular fields - that
 *  is why some fields are both analyzed and non-analyzed. 
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultInstitutionalItemIndexService implements InstitutionalItemIndexService {
	
	/** eclipse generated id */
	private static final long serialVersionUID = 2088750504787725269L;

	/** Service for sending email errors */
	private ErrorEmailService errorEmailService;
	
	/** data access for indexing record failure data access */
	private IrFileIndexingFailureRecordDAO irFileIndexingFailureRecordDAO;
	
	/** Batch size for processing collections */
	private int collectionBatchSize = 1;
	
	/** Analyzer for dealing with text indexing */
	private Analyzer analyzer;
	
	/** Service that maintains file text extractors */
	private FileTextExtractorService fileTextExtractorService;
	
	/** Service for dealing with institutional items */
	private InstitutionalItemService institutionalItemService;
		
	/** separator used for multi-set data */
	public static final String SEPERATOR = "|";
	
	/** This is used to remove all separator characters for indexed values if needed */
	public static final String ESCAPED_SEPERATOR = "\\|";
	
	/** id of the item */
	public static final String ID = "id";
	
	/** names/title of the item */
	public static final String NAME = "name";
	
	/** description of the item */
	public static final String DESCRIPTION = "description";
	
	/** names of the links */
	public static final String LINK_NAMES = "link_names";
	
	/** text in the files of an item */
	public static final String FILE_TEXT = "file_text";
	
	/** names of the contributors - NOT analyzed*/
	public static final String CONTRIBUTOR_NAMES = "contributor_names";
	
	/** names of the contributors - been analyzed by analyzer*/
	public static final String CONTRIBUTOR_NAMES_ANALYZED = "contributor_names_analyzed";
	
	/** language the item is in - NOT analyzed*/
	public static final String LANGUAGE = "language";
	
	/** language the item is in - been analyzed by analyzer*/ 
	public static final String LANGUAGE_ANALYZED = "language_analyzed";
	
	/** identifiers for the item  */
	public static final String IDENTIFIERS = "identifiers";
	
	/** abstract for the item */
	public static final String ABSTRACT = "abstract";
	
	/** key words for the item - NOT analyzed*/
	public static final String KEY_WORDS = "keywords";
	
	/** key words for the item - been analyzed by analyzer*/
	public static final String KEY_WORDS_ANALYZED = "keywords_analyzed";
	
	/** sub titles for the item  */
	public static final String SUB_TITLES = "sub_titles";
	
	/** publisher of the item  - NOT analyzed*/
	public static final String PUBLISHER = "publisher";
	
	/** publisher of the item  - been analyzed by analyzer*/
	public static final String PUBLISHER_ANALYZED = "publisher_analyzed";
	
	/** sponsors of the item  - NOT analyzed*/
	public static final String SPONSORS = "sponsors";
	
	/** sponsors of the item  - been analyzed by analyzer*/
	public static final String SPONSORS_ANALYZED = "sponsors_analyzed";
	
	/** sponsors of the item  - been analyzed by analyzer*/
	public static final String SPONSORS_DESCRIPTION = "sponsors_description";
	
	/** citation if the item has been published */
	public static final String CITATION = "citation";
	
	/** Type of this item  - NOT analyzed*/
	public static final String CONTENT_TYPES = "content_type";
	
	/** Type of this item  - been analyzed by analyzer*/
	public static final String CONTENT_TYPES_ANALYZED = "content_type_analyzed";
	
	/** series for this item  - been analyzed by analyzer*/
	public static final String SERIES_ANALYZED = "series_analyzed";
		
	/** name of the collection the item is in */
	public static final String COLLECTION_NAME = "collection_name";
	
	/** name of the collection the item is in - been analyzed by analyzer*/
	public static final String COLLECTION_NAME_ANALYZED = "collection_name_analyzed";
	
	/** collection this item is in */
	public static final String COLLECTION_ID = "collection_id";
	
	/** repository id */
	public static final String REPOSITORY_ID = "repository_id";
	
	/** root id for a collection tree */
	public static final String COLLECTION_ROOT_ID = "collection_root_id";
	
	/** collection left value */
	public static final String COLLECTION_LEFT_VALUE = "collection_left_value";
	
	/** left value of the collection */
	public static final String COLLECTION_RIGHT_VALUE = "collection_right_value";
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultInstitutionalItemIndexService.class);
	
	
	/**
	 * Add a set of items to the index - this is generally used for batch processing of multiple institutional items.  
	 * This can also be used to re-index a set the existing set of items
	 * 
	 * @param items - set of items to add
	 * @param institutionalItemIndex - index to add it to
	 * @param overwriteExistingIndex - indicates this should overwrite the current index
	 */
	public void addItems(List<InstitutionalItem> items, File institutionalItemIndex, boolean overwriteExistingIndex)
	{
		
		LinkedList<Document> docs = new LinkedList<Document>();
		
		for(InstitutionalItem item : items)
		{
			docs.add(getDocument(item, true));
		}
		
		IndexWriter writer = null;
		Directory directory = null;
		try {
			directory = FSDirectory.open(institutionalItemIndex); 
			if( overwriteExistingIndex )
			{
			    writer = getWriterOverwriteExisting(directory);
			}
			else
			{
				writer = getWriter(directory);
			}
			    
			for(Document d : docs)
			{
				writer.addDocument(d);
			}
			writer.commit();
			
			
		} 
		catch (Exception e) 
		{
			log.error(e);
			errorEmailService.sendError(e);
		}
	    finally {
	    	
		    if (writer != null) {
			    try {
				    writer.close();
			    } catch (Exception e) {
				    log.error(e);
			    }
		    }
		    writer = null;
		    try {
				IndexWriter.unlock(directory);
			} 
	    	catch (IOException e1)
	    	{
				log.error(e1);
			}
		    
		    if( directory != null )
		    {
		    	try
		    	{
		    		directory.close();
		    	}
		    	catch (Exception e) {
				    log.error(e);
			    }
		    }
		    directory = null;
		    
	    }
	}
	

	
	
	/**
	 * Delete the institutional item from the index.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexService#deleteItem(edu.ur.ir.institution.InstitutionalItem, java.io.File)
	 */
	public void deleteItem(Long id, File institutionalItemIndex) {

		Directory directory = null;
		IndexWriter writer = null;
		
	    // if the index is empty or does not exist then do nothing
	    if( institutionalItemIndex == null || institutionalItemIndex.list() == null || 
	    		institutionalItemIndex.list().length == 0)
	    {
	    	return;
	    }
	    
		try 
		{
			directory = FSDirectory.open(institutionalItemIndex);
			writer = getWriter(directory);
			Term term = new Term(ID, NumberTools.longToString(id));
			writer.deleteDocuments(term);
			  
		} 
        catch (Exception e) 
        {
			log.error(e);
			errorEmailService.sendError(e);
		}
        finally 
        {
   	        if (writer != null) {
			    try {
				    writer.close();
			    } catch (Exception e) {
				    log.error(e);
			    }
		    }
		    writer = null;
		    try 
        	{
				IndexWriter.unlock(directory);
			} 
	    	catch (IOException e1)
	    	{
				log.error(e1);
			}
		    if( directory != null )
		    {
		    	try
		    	{
		    		directory.close();
		    	}
		    	catch (Exception e) {
				    log.error(e);
			    }
		    }
		    directory = null;
		    
	    }
		
	}
	
	/**
	 * Add an institutional item to the index.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexService#addItem(edu.ur.ir.institution.InstitutionalItem, java.io.File)
	 */
	public void addItem(InstitutionalItem institutionalItem, File institutionalItemIndex) throws NoIndexFoundException {
		if (institutionalItemIndex == null) {
			throw new NoIndexFoundException("Institutional item index folder not found ");
		} 
		writeDocument(institutionalItemIndex,	getDocument(institutionalItem, true ));
	}
	
	/**
	 * Create a document for the institutional item.
	 * 
	 * @param institutionalItem
	 * @return - the created document
	 */
	private Document getDocument(InstitutionalItem institutionalItem, boolean addFileTextFields)
	{
		GenericItem genericItem = institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getItem();
		
		Document doc = new Document();
		doc.add(new Field(ID, 
				NumberTools.longToString(institutionalItem.getId()), 
				Field.Store.YES, 
				Field.Index.NOT_ANALYZED));
		
		
		String name = genericItem.getName();
		if(name != null && !name.trim().equals(""))
		{
			doc.add(new Field(NAME, 
					name, 
					Field.Store.NO, 
					Field.Index.ANALYZED ));
		}
		
		String description = genericItem.getDescription();
		if(description != null && !description.trim().equals(""))
		{
			doc.add(new Field(DESCRIPTION, 
					description, 
					Field.Store.NO, 
					Field.Index.ANALYZED ));
		}
		
		String collectionName = institutionalItem.getInstitutionalCollection().getName();
		
		
		doc.add(new Field(COLLECTION_NAME, 
				collectionName, 
				Field.Store.YES, 
				Field.Index.NOT_ANALYZED ));
		
		//remove separator from name in index
		collectionName = collectionName.replaceAll(ESCAPED_SEPERATOR, " ");
		doc.add(new Field(COLLECTION_NAME_ANALYZED, 
				collectionName, 
				Field.Store.YES, 
				Field.Index.ANALYZED ));;
		
		String collectionId =  NumberTools.longToString(institutionalItem.getInstitutionalCollection().getId());
		doc.add(new Field(COLLECTION_ID, 
				collectionId, 
				Field.Store.YES, 
				Field.Index.ANALYZED ));
		
		String repositoryId = NumberTools.longToString(institutionalItem.getInstitutionalCollection().getRepository().getId());
		doc.add(new Field(REPOSITORY_ID, 
				repositoryId, 
				Field.Store.YES, 
				Field.Index.ANALYZED ));
		
		String rootCollectionId = NumberTools.longToString(institutionalItem.getInstitutionalCollection().getTreeRoot().getId());
		doc.add(new Field(COLLECTION_ROOT_ID, 
				rootCollectionId, 
				Field.Store.YES, 
				Field.Index.ANALYZED ));
		
		String collectionLeftValue = NumberTools.longToString(institutionalItem.getInstitutionalCollection().getLeftValue());		
		doc.add(new Field(COLLECTION_LEFT_VALUE, 
				collectionLeftValue, 
				Field.Store.YES, 
				Field.Index.ANALYZED ));
		
		String collectionRightValue = NumberTools.longToString(institutionalItem.getInstitutionalCollection().getRightValue());		
		doc.add(new Field(COLLECTION_RIGHT_VALUE, 
				collectionRightValue, 
				Field.Store.YES, 
				Field.Index.ANALYZED ));
		
		// get the contributors
		String contributorString = getContributorNames(genericItem);
		if(!contributorString.trim().equals(""))
		{
			doc.add(new Field(CONTRIBUTOR_NAMES_ANALYZED, 
					contributorString, 
					Field.Store.YES, 
					Field.Index.ANALYZED ));
		}
		
		// this allows for exact matches when faceted searching occurs
		List<String> contributors = getNotAnalyzedContributorNames(genericItem);
		for( String value : contributors )
		{
		    doc.add(new Field(CONTRIBUTOR_NAMES, 
				value, 
				Field.Store.YES, 
				Field.Index.NOT_ANALYZED ));
		}
				
		// get the contributors
		String linksString = getLinkNames(genericItem);
		if(!linksString.trim().equals(""))
		{
			doc.add(new Field(LINK_NAMES, 
					linksString, 
					Field.Store.NO, 
					Field.Index.ANALYZED ));
		}
		
		
		if( addFileTextFields )
		{
		    Set<ItemFile> files = genericItem.getItemFiles();
		    for(ItemFile itemFile : files)
		    {
			    // get the text for the files
			    String fileText = getDocumentBodyText(itemFile);
		        if(fileText != null && !fileText.trim().equals(""))
		        {
			        doc.add(new Field(FILE_TEXT, 
					    fileText, 
					    Field.Store.NO, 
					    Field.Index.ANALYZED ));
		        }
		    
		        fileText = null;
		    }
		}
		
		//index the language
		String language = null;
		if( genericItem.getLanguageType() != null )
		{
		    language = genericItem.getLanguageType().getName();
		}
		
		if(language != null && !language.trim().equals(""))
		{
			doc.add(new Field(LANGUAGE, 
					language, 
					Field.Store.YES, 
					Field.Index.NOT_ANALYZED ));
			
			language = language.replaceAll(ESCAPED_SEPERATOR, " ");
			
			doc.add(new Field(LANGUAGE_ANALYZED, 
					language, 
					Field.Store.YES, 
					Field.Index.ANALYZED ));
		}
		
		// index the identifiers
		String identifiers = getIdentifiers(genericItem);
		if(!identifiers.trim().equals(""))
		{
			doc.add(new Field(IDENTIFIERS, 
					identifiers, 
					Field.Store.NO, 
					Field.Index.ANALYZED ));
		}
		
		//index the item abstract
		String itemAbstract = genericItem.getItemAbstract();
		if(itemAbstract != null && !itemAbstract.equals(""))
		{
			doc.add(new Field(ABSTRACT, 
					itemAbstract, 
					Field.Store.NO, 
					Field.Index.ANALYZED ));
		}
		
		//keywords for the item
		String keywords = getSubjects(genericItem.getItemKeywords());
		
		if(keywords != null && !keywords.equals(""))
		{
			doc.add(new Field(KEY_WORDS_ANALYZED, 
					keywords, 
					Field.Store.YES, 
					Field.Index.ANALYZED ));
		}
		
		List<String> keywordValues = getNotAnalyzedSubjects(genericItem.getItemKeywords());
		for( String value : keywordValues)
		{
			doc.add(new Field(KEY_WORDS, 
					value, 
					Field.Store.YES, 
					Field.Index.NOT_ANALYZED ));
		}
		
		//subtitles for the item
		String subTitles = getSubTitles(genericItem);
		if(subTitles != null && !subTitles.equals(""))
		{
			doc.add(new Field(SUB_TITLES, 
					subTitles, 
					Field.Store.NO, 
					Field.Index.ANALYZED ));
		}	

		// Types for item
		String contentTypes = getContentTypes(genericItem);
		if(contentTypes != null && !contentTypes.equals(""))
		{
			
			
			doc.add(new Field(CONTENT_TYPES_ANALYZED, 
					contentTypes, 
					Field.Store.YES, 
					Field.Index.ANALYZED ));
		}	
		
		//series/report numbers for item
		String seriesReports = this.getSeries(genericItem);
	    if(seriesReports != null)
	    {
	    	doc.add(new Field(SERIES_ANALYZED, 
	    			seriesReports,
	    			Field.Store.YES,
	    			Field.Index.ANALYZED));
	    }
		
		List<String> contentValues = getNotAnalyzedContentTypes(genericItem);
		for(String value : contentValues)
		{
			doc.add(new Field(CONTENT_TYPES, 
					value, 
					Field.Store.YES, 
					Field.Index.NOT_ANALYZED ));
		}
		
		String sponsors = getSponsors(genericItem);
		if( sponsors != null && !sponsors.equals(""))
		{
			doc.add(new Field(SPONSORS_ANALYZED, 
					contentTypes, 
					Field.Store.YES, 
					Field.Index.ANALYZED ));
		}
		
		List<String> sponsorValues = getNotAnalyzedSponsors(genericItem);
		for(String value : sponsorValues)
		{
			doc.add(new Field(SPONSORS, 
					value, 
					Field.Store.YES, 
					Field.Index.NOT_ANALYZED ));
		}
		
		String sponsorDescriptions = getSponsorDescriptions(genericItem);
		if( sponsorDescriptions != null && !sponsorDescriptions.equals(""))
		{
			
			doc.add(new Field(SPONSORS_DESCRIPTION, 
					sponsorDescriptions, 
					Field.Store.NO, 
					Field.Index.ANALYZED ));
		}
		
		
		
		//publisher information
		if( genericItem.getExternalPublishedItem() != null  )
		{
			if(genericItem.getExternalPublishedItem().getPublisher() != null)
			{
				String publisherName = genericItem.getExternalPublishedItem().getPublisher().getName();
				if( publisherName != null && !publisherName.equals(""))
				{
					doc.add(new Field(PUBLISHER, 
							publisherName, 
							Field.Store.YES, 
							Field.Index.NOT_ANALYZED ));
					
					doc.add(new Field(PUBLISHER_ANALYZED, 
							publisherName, 
							Field.Store.YES, 
							Field.Index.ANALYZED ));
				}
			}
	
			//citation information
			String citation = genericItem.getExternalPublishedItem().getCitation();
			if( citation != null && !citation.equals(""))
			{
				doc.add(new Field(CITATION, 
						citation, 
						Field.Store.NO, 
						Field.Index.ANALYZED ));
			}
		}
		
		return doc;
	}

	
	/**
	 * Update the item in the index.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexService#updateItem(edu.ur.ir.institution.InstitutionalItem, java.io.File)
	 */
	public void updateItem(InstitutionalItem institutionalItem, File institutionalItemIndex, boolean filesChanged) throws NoIndexFoundException{
		deleteItem(institutionalItem.getId(), institutionalItemIndex);
		addItem(institutionalItem, institutionalItemIndex);
	}
	
	/**
	 * Write the list of documents to the index in the directory.
	 * 
	 * @param directoryPath - location where the directory exists.
	 * @param documents - documents to add to the directory.
	 */
	private void writeDocument(File path, Document document)
	{
		IndexWriter writer = null;
		Directory directory = null;
		try 
		{
		    directory = FSDirectory.open(path);
			writer = getWriter(directory);
		    writer.addDocument(document);
			writer.commit();
		} 
		catch (Exception e) 
		{
			log.error(e);
			errorEmailService.sendError(e);
		}
		finally 
        {
		    if (writer != null) {
			    try {
				    writer.close();
			    } catch (Exception e) {
				    log.error(e);
			    }
		    }
		    writer = null;
		    try 
		    {
				IndexWriter.unlock(directory);
			} 
	    	catch (Exception e1)
	    	{
				log.error(e1);
			}
		    if( directory != null )
		    {
		    	try
		    	{
		    		directory.close();
		    	}
		    	catch (Exception e) {
				    log.error(e);
			    }
		    }
		    directory = null;
		    
	    }
	}
	
	/**
	 * Get the contributor names.
	 * 
	 * @param item
	 * @return
	 */
	private String getContributorNames(GenericItem genericItem)
	{
		StringBuffer sb = new StringBuffer();
		List<ItemContributor> contributors = genericItem.getContributors();
		
		for(ItemContributor c : contributors)
		{
			PersonName personName = c.getContributor().getPersonName();
			if( personName.getForename() != null )
			{
				sb.append(" " + personName.getForename() + " ");
			}

			if( personName.getMiddleName()!= null )
			{
				sb.append(personName.getMiddleName() + " ");
			}

			if( personName.getFamilyName() != null )
			{
				sb.append(personName.getFamilyName() + " ");
			}

			if( personName.getSurname() != null )
			{
				sb.append(personName.getSurname() + " ");
			}
			sb.append(SEPERATOR);
			
		}
		
		
		return sb.toString();
	}
	
	/**
	 * Get the contributor names.
	 * 
	 * @param item
	 * @return
	 */
	private List<String> getNotAnalyzedContributorNames(GenericItem genericItem)
	{
		LinkedList<String> values = new LinkedList<String>();
		List<ItemContributor> contributors = genericItem.getContributors();
		
		for(ItemContributor c : contributors)
		{
			StringBuffer sb = new StringBuffer();
			PersonName personName = c.getContributor().getPersonName();
			if( personName.getForename() != null )
			{
				sb.append(personName.getForename() + " ");
			}

			if( personName.getMiddleName()!= null )
			{
				sb.append(personName.getMiddleName() + " ");
			}

			if( personName.getFamilyName() != null )
			{
				sb.append(personName.getFamilyName() + " ");
			}

			if( personName.getSurname() != null )
			{
				sb.append(personName.getSurname());
			}
			values.add(sb.toString());
		}
		
		return values;
	}

	/**
	 * Get the content types.
	 * 
	 * @param item
	 * @return
	 */
	private String getContentTypes(GenericItem genericItem)
	{
		StringBuffer sb = new StringBuffer();
		
		
		Set<ItemContentType> types = genericItem.getItemContentTypes();
		if (types != null && types.size() > 0) {
		
			for(ItemContentType c : types)
			{
				sb.append(" " + c.getContentType().getName() + " ");
				sb.append(SEPERATOR);
			}
		}
		
		
		return sb.toString();
	}
	
	
	/**
	 * Get each content type listed as a single string value
	 * 
	 * @param genericItem
	 * @return
	 */
	private List<String> getNotAnalyzedContentTypes(GenericItem genericItem)
	{
		LinkedList<String> contents = new LinkedList<String>();
		
		Set<ItemContentType> types = genericItem.getItemContentTypes();
		if (types != null && types.size() > 0) {
			
			for(ItemContentType c : types)
			{
				contents.add(c.getContentType().getName());
			}
		}
		return contents;
	}
	
	/**
	 * Get the contributor names.
	 * 
	 * @param item
	 * @return
	 */
	private String getLinkNames(GenericItem genericItem)
	{
		StringBuffer sb = new StringBuffer();
		Set<ItemLink> links = genericItem.getLinks();
		
		for(ItemLink link : links)
		{
			sb.append(" " + link.getName() + " ");
			sb.append(SEPERATOR);
		}
		return sb.toString();
	}
	
	/**
	 * Get the identifiers
	 * 
	 * @param item
	 * @return
	 */
	private String getIdentifiers(GenericItem genericItem)
	{
		StringBuffer sb = new StringBuffer();
		Set<ItemIdentifier> identifiers = genericItem.getItemIdentifiers();
		
		for(ItemIdentifier identifier : identifiers)
		{
			sb.append(" " + identifier.getIdentifierType().getName() + " ");
			sb.append(" " + identifier.getValue() + " ");
			sb.append(SEPERATOR);
		}
		return sb.toString();
	}
	
	/**
	 * Get the sponsors
	 * 
	 * @param item
	 * @return
	 */
	private String getSponsors(GenericItem genericItem)
	{
		StringBuffer sb = new StringBuffer();
		Set<ItemSponsor> sponsors = genericItem.getItemSponsors();
		
		for(ItemSponsor sponsor : sponsors)
		{
			sb.append(" " + sponsor.getSponsor().getName() + " ");
			sb.append(SEPERATOR);
		}
		return sb.toString();
	}
	
	/**
	 * Get the sponsors as a list of strings
	 * 
	 * @param item
	 * @return
	 */
	private List<String> getNotAnalyzedSponsors(GenericItem genericItem)
	{
		LinkedList<String> values = new LinkedList<String>();
		Set<ItemSponsor> sponsors = genericItem.getItemSponsors();
		
		for(ItemSponsor sponsor : sponsors)
		{
			values.add(sponsor.getSponsor().getName());
		}
		return values;
	}
	
	/**
	 * Get the sponsor descriptions
	 * 
	 * @param item
	 * @return
	 */
	private String getSponsorDescriptions(GenericItem genericItem)
	{
		StringBuffer sb = new StringBuffer();
		Set<ItemSponsor> sponsors = genericItem.getItemSponsors();
		
		for(ItemSponsor sponsor : sponsors)
		{
			sb.append(" " + sponsor.getDescription() + " ");
			sb.append(SEPERATOR);
		}
		return sb.toString();
	}
	
	/**
	 * Get the subtitles
	 * 
	 * @param item
	 * @return string of sub titles with separators
	 */
	private String getSubTitles(GenericItem genericItem)
	{
		StringBuffer sb = new StringBuffer();
		Set<ItemTitle> subTitles = genericItem.getSubTitles();
		
		for(ItemTitle subTitle : subTitles)
		{
			sb.append(" " + subTitle.getTitle() + " ");
			sb.append(SEPERATOR);
		}
		return sb.toString();
	}
	
	/**
	 * Extracts the text from the document.  If no text is extracted an
	 * empty string is returned.
	 * 
	 * @param version
	 * @return text for the document.
	 */
	private String getDocumentBodyText(ItemFile itemFile)
	{
		String text = "";
		String extension = itemFile.getIrFile().getFileInfo().getExtension();
		FileTextExtractor extractor = fileTextExtractorService.getFileTextExtractor(extension);
		if( extractor != null)
		{
		    log.debug("Extractor found for extension " + extension);
			File f = new File(itemFile.getIrFile().getFileInfo().getFullPath());
			
			try {
				if( !extractor.isFileTooLarge(f) )
				{
				    text = extractor.getText(f);
				}
				else
				{
					IrFileIndexingFailureRecord failureRecord = new IrFileIndexingFailureRecord(itemFile.getIrFile().getId(),"FILE IS TOO LARGE size in bytes = " + f.length());
				    irFileIndexingFailureRecordDAO.makePersistent(failureRecord);
				}
			}
			catch (Exception e) {
				log.error(e);
				IrFileIndexingFailureRecord failureRecord = new IrFileIndexingFailureRecord(itemFile.getIrFile().getId(), e.toString());
			    irFileIndexingFailureRecordDAO.makePersistent(failureRecord);
			    errorEmailService.sendError(e);
			}
		}
		else
		{
			log.debug("No extractor found for extension " + extension);
		}
		
		return text;
	}
	
	private String getSubjects(String subjectValues)
	{
		String keywords = "";
		if( subjectValues == null || subjectValues.trim().equals(""))
		{
			return keywords;
		}
		StringTokenizer tokenizer = new StringTokenizer(subjectValues, GenericItem.KEYWORD_SEPARATOR);
		boolean first = true;
		while(tokenizer.hasMoreElements())
		{
			String nextValue = tokenizer.nextToken().toLowerCase().trim();
			if( first )
			{
			    keywords = nextValue;
			    first = false;
			}
			else
			{
				keywords = keywords + " " + SEPERATOR + " " + nextValue;
			}
		}
		
		return keywords;
	}
	
	/**
	 * Get the list of subjects as keywords 
	 * 
	 * @param subjectValues
	 * @return
	 */
	private List<String> getNotAnalyzedSubjects(String subjectValues)
	{
		LinkedList<String> values = new LinkedList<String>();
		
		if( subjectValues == null || subjectValues.trim().equals(""))
		{
			return values;
		}
		StringTokenizer tokenizer = new StringTokenizer(subjectValues, GenericItem.KEYWORD_SEPARATOR);
		while(tokenizer.hasMoreElements())
		{
			String nextValue = tokenizer.nextToken().toLowerCase().trim();
			values.add(nextValue);
		}
		
		return values;
	}
	
	
	/**
	 * Get the series
	 * 
	 * @param item
	 * @return string of series
	 */
	private String getSeries(GenericItem genericItem)
	{
		StringBuffer sb = new StringBuffer();
		Set<ItemReport> reportSeries = genericItem.getItemReports();
		
		for(ItemReport report : reportSeries)
		{
			sb.append(" " + report.getSeries().getName() + " ");
			sb.append(" " + report.getReportNumber() + " ");
			sb.append(SEPERATOR);
		}
		return sb.toString();
	}
	
	
	/**
	 * Analyzer used to analyze the name.
	 * 
	 * @return the analyzer used to analyze the information
	 */
	public synchronized Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * Set the analyzer user to index the information.
	 * 
	 * @param analyzer
	 */
	public synchronized void  setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	
	/**
	 * Service that manages text extractors.
	 * 
	 * @return
	 */
	public FileTextExtractorService getFileTextExtractorService() {
		return fileTextExtractorService;
	}

	/**
	 * Service that manages text extractors.
	 * 
	 * @param fileTextExtractorService
	 */
	public void setFileTextExtractorService(
			FileTextExtractorService fileTextExtractorService) {
		this.fileTextExtractorService = fileTextExtractorService;
	}
	
	
	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public int getCollectionBatchSize() {
		return collectionBatchSize;
	}

	public void setCollectionBatchSize(int collectionBatchSize) {
		this.collectionBatchSize = collectionBatchSize;
	}
	
	/**
	 * All methods should use this to obtain a writer on the directory.  This will return 
	 * a null writer if the index is locked.  A while loop can be set up to determine if an index
	 * writer is available for the specified directory. This ensures that only one writer is writing to a 
	 * users index at once.
	 * 
	 * @param directory
	 * @return
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	private IndexWriter getWriter(Directory directory) throws CorruptIndexException, LockObtainFailedException, IOException
	{
		IndexWriter writer = new IndexWriter(directory, analyzer, IndexWriter.MaxFieldLength.LIMITED);
		return writer;
	}
	
	/**
	 * All methods should use this to obtain a writer on the directory.  This will return 
	 * a null writer if the index is locked.  A while loop can be set up to determine if an index
	 * writer is available for the specified directory. This ensures that only one writer is writing to a 
	 * users index at once.
	 * 
	 * @param directory
	 * @return
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	private IndexWriter getWriterOverwriteExisting(Directory directory) throws CorruptIndexException, LockObtainFailedException, IOException
	{
		IndexWriter  writer = new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
		return writer;
	}


	/**
	 * Optimize the index.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexService#optimize(java.io.File)
	 */
	public void optimize(File institutionalItemIndex) {
		IndexWriter writer = null;
		Directory directory = null;
		try 
		{
		    directory = FSDirectory.getDirectory(institutionalItemIndex.getAbsolutePath());
			writer = getWriter(directory);
			writer.optimize();
		} 
		catch (Exception e) 
		{
			log.error(e);
			errorEmailService.sendError(e);
		}
		finally 
        {
		    if (writer != null) {
			    try {
				    writer.close();
			    } catch (Exception e) {
				    log.error(e);
			    }
		    }
		    writer = null;
		    try 
		    {
				IndexWriter.unlock(directory);
			} 
	    	catch (Exception e1)
	    	{
				log.error(e1);
			}
		    if( directory != null )
		    {
		    	try
		    	{
		    		directory.close();
		    	}
		    	catch (Exception e) {
				    log.error(e);
			    }
		    }
		    directory = null;
		    
	    }
	}




	public ErrorEmailService getErrorEmailService() {
		return errorEmailService;
	}




	public void setErrorEmailService(ErrorEmailService errorEmailService) {
		this.errorEmailService = errorEmailService;
	}




	public IrFileIndexingFailureRecordDAO getIrFileIndexingFailureRecordDAO() {
		return irFileIndexingFailureRecordDAO;
	}




	public void setIrFileIndexingFailureRecordDAO(
			IrFileIndexingFailureRecordDAO irFileIndexingFailureRecordDAO) {
		this.irFileIndexingFailureRecordDAO = irFileIndexingFailureRecordDAO;
	}
	

}
