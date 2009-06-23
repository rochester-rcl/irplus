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

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.index.FileTextExtractor;
import edu.ur.ir.index.FileTextExtractorService;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemIdentifier;
import edu.ur.ir.item.ItemLink;
import edu.ur.ir.item.ItemTitle;
import edu.ur.ir.person.PersonName;

/**
 * Implementation of the Institutional Item index service.  
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultInstitutionalItemIndexService implements InstitutionalItemIndexService {
	
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
	
	/** This is used to remove all seperator characters for indexed values if needed */
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
	
	/** names of the contributors */
	public static final String CONTRIBUTOR_NAMES = "contributor_names";
	
	/** language the item is in  */
	public static final String LANGUAGE = "language";
	
	/** identifiers for the item  */
	public static final String IDENTIFIERS = "identifiers";
	
	/** abstract for the item */
	public static final String ABSTRACT = "abstract";
	
	/** key words for the item */
	public static final String KEY_WORDS = "keywords";
	
	/** sub titles for the item  */
	public static final String SUB_TITLES = "sub_titles";
	
	/** publisher of the item  */
	public static final String PUBLISHER = "publisher";
	
	/** citation if the item has been published */
	public static final String CITATION = "citation";
	
	/** Type of this item  */
	public static final String CONTENT_TYPES = "content_type";
	
	/** name of the collection the item is in */
	public static final String COLLECTION_NAME = "collection_name";
	
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
			docs.add(getDocument(item));
		}
		
		IndexWriter writer = null;
		Directory directory = null;
		try {
			directory = FSDirectory.getDirectory(institutionalItemIndex.getAbsolutePath());
			while(writer == null )
			{
				if( overwriteExistingIndex )
				{
				    writer = getWriterOverwriteExisting(directory);
				}
				else
				{
					writer = getWriter(directory);
				}
			}
			    
			for(Document d : docs)
			{
				writer.addDocument(d);
			}
			writer.commit();
			writer.optimize();
			
		} catch (IOException e) {
			log.error(e);
			throw new RuntimeException(e);
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
	 * (non-Javadoc)
	 * @see edu.ur.ir.institution.InstitutionalItemIndexService#deleteItemsForCollection(edu.ur.ir.institution.InstitutionalCollection, java.io.File)
	 */
	public void deleteItemsForCollection(InstitutionalCollection institutionalCollection, File institutionalItemIndex)
	{
		Directory directory = null;
		IndexWriter writer = null;
		
	    // if the index is empty or does not exist then do nothing
	    if( institutionalItemIndex == null || institutionalItemIndex.list() == null || 
	    		institutionalItemIndex.list().length == 0)
	    {
	    	return;
	    }
	    
		try {
			directory = FSDirectory.getDirectory(institutionalItemIndex.getAbsolutePath());
			while(writer == null )
			{
			    writer =  getWriter(directory);
			}
			Term term = new Term(COLLECTION_ID, NumberTools.longToString(institutionalCollection.getId()));
			writer.deleteDocuments(term);
		} 
		catch (IOException e) 
		{
	        throw new RuntimeException(e);
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
	public void deleteItem(InstitutionalItem institutionalItem, File institutionalItemIndex) {

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
			directory = FSDirectory.getDirectory(institutionalItemIndex.getAbsolutePath());
			while(writer == null )
			{
				writer = getWriter(directory);
			}
			
			Term term = new Term(ID, NumberTools.longToString(institutionalItem.getId()));
			writer.deleteDocuments(term);
			  
		} 
        catch (IOException e) 
        {
			log.error(e);
	        throw new RuntimeException(e);
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
		writeDocument(institutionalItemIndex.getAbsolutePath(),	getDocument(institutionalItem));
	}
	
	
	/**
	 * Create a document for the institutional item.
	 * 
	 * @param institutionalItem
	 * @return - the created document
	 */
	private Document getDocument(InstitutionalItem institutionalItem)
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
		
		//remove separator from name in index
		collectionName = collectionName.replaceAll(ESCAPED_SEPERATOR, " ");
		doc.add(new Field(COLLECTION_NAME, 
				collectionName, 
				Field.Store.YES, 
				Field.Index.ANALYZED ));
		
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
			doc.add(new Field(CONTRIBUTOR_NAMES, 
					contributorString, 
					Field.Store.YES, 
					Field.Index.ANALYZED ));
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
		
		
		
		Set<ItemFile> files = genericItem.getItemFiles();
		for(ItemFile itemFile : files)
		{
			// get the text for the files
			String fileText = getDocumentBodyText(itemFile);
		    if(!fileText.trim().equals(""))
		    {
			    doc.add(new Field(FILE_TEXT, 
					fileText, 
					Field.Store.NO, 
					Field.Index.ANALYZED ));
		    }
		    
		    fileText = null;
		}
		
		//index the language
		String language = null;
		if( genericItem.getLanguageType() != null )
		{
		    language = genericItem.getLanguageType().getName();
		}
		
		if(language != null && !language.trim().equals(""))
		{
			language = language.replaceAll(ESCAPED_SEPERATOR, " ");
			doc.add(new Field(LANGUAGE, 
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
					Field.Store.YES, 
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
			doc.add(new Field(KEY_WORDS, 
					keywords, 
					Field.Store.YES, 
					Field.Index.ANALYZED ));
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
			doc.add(new Field(CONTENT_TYPES, 
					contentTypes, 
					Field.Store.YES, 
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
	public void updateItem(InstitutionalItem institutionalItem, File institutionalItemIndex) throws NoIndexFoundException{
		deleteItem(institutionalItem, institutionalItemIndex);
		addItem(institutionalItem, institutionalItemIndex);
	}
	
	/**
	 * Write the list of documents to the index in the directory.
	 * 
	 * @param directoryPath - location where the directory exists.
	 * @param documents - documents to add to the directory.
	 */
	private void writeDocument(String directoryPath, Document document)
	{
		IndexWriter writer = null;
		Directory directory = null;
		try 
		{
		    directory = FSDirectory.getDirectory(directoryPath);
		    while(writer == null )
			{
				writer = getWriter(directory);
			}
		    writer.addDocument(document);
			writer.commit();
			writer.optimize();
		} 
		catch (IOException e) 
		{
			log.error(e);
			throw new RuntimeException(e);
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
	 * Get the content types.
	 * 
	 * @param item
	 * @return
	 */
	private String getContentTypes(GenericItem genericItem)
	{
		StringBuffer sb = new StringBuffer();
		ContentType primaryContentType = genericItem.getPrimaryContentType();
		
		if (primaryContentType != null ) {
			sb.append(" " + primaryContentType.getName() + " ");
		}
		
		Set<ContentType> types = genericItem.getSecondaryContentTypes();
		if (types != null && types.size() > 0) {
			sb.append(SEPERATOR);
		
			for(ContentType c : types)
			{
				sb.append(" " + c.getName() + " ");
				
				sb.append(SEPERATOR);
				
			}
		}
		
		
		return sb.toString();
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
	 * Get the identifiers
	 * 
	 * @param item
	 * @return
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
		
		StringBuffer sb = new StringBuffer();
		String extension = itemFile.getIrFile().getFileInfo().getExtension();
		FileTextExtractor extractor = fileTextExtractorService.getFileTextExtractor(extension);
		if( extractor != null)
		{
		    log.debug("Extractor found for extension " + extension);
			File f = new File(itemFile.getIrFile().getFileInfo().getFullPath());
			sb.append(" " + extractor.getText(f) + " ");
		}
		else
		{
			log.debug("No extractor found for extension " + extension);
		}
		
		return sb.toString();
	}
	
	private String getSubjects(String subjectValues)
	{
		String keywords = "";
		if( subjectValues == null || subjectValues.trim().equals(""))
		{
			return keywords;
		}
		StringTokenizer tokenizer = new StringTokenizer(subjectValues, ",");
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
	private synchronized IndexWriter getWriter(Directory directory) throws CorruptIndexException, LockObtainFailedException, IOException
	{
		IndexWriter writer = null;
		if( !IndexWriter.isLocked(directory) )
	    {
			writer = new IndexWriter(directory, analyzer, IndexWriter.MaxFieldLength.LIMITED);
	    }
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
	private synchronized IndexWriter getWriterOverwriteExisting(Directory directory) throws CorruptIndexException, LockObtainFailedException, IOException
	{
		IndexWriter writer = null;
		if( !IndexWriter.isLocked(directory) )
	    {
			writer = new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
	    }
		return writer;
	}
	

}
