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

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.NumericUtils;

import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionIndexService;
import edu.ur.ir.institution.InstitutionalCollectionService;

/**
 * Default implementation of the institutional collection index service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultInstitutionalCollectionIndexService implements InstitutionalCollectionIndexService {

	/** eclipse generated id  */
	private static final long serialVersionUID = -4049136120970451472L;
	
	/** Service to deal with institutional collection information */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Analyzer for dealing with text indexing */
	private transient Analyzer analyzer;
	
	/** Service for sending email errors */
	private ErrorEmailService errorEmailService;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultInstitutionalCollectionIndexService.class);

	/** Id of collection */
	public static final String ID = "id";
	
	/** name of the collection */
	public static final String NAME = "name";
	
	/** description of the collection */
	public static final String DESCRIPTION = "description";
	
	/**
	 * Re-index the specified collections.  This can be used to re-index 
	 * all collections
	 * 
	 * @param collections - collections to re index
	 * @param collectionIndexFolder - folder location of the index
	 * @param overwriteExistingIndex - if set to true, will overwrite the exiting index.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionIndexService#add(java.util.List, java.io.File, boolean)
	 */
	public void add(List<InstitutionalCollection> collections,
			File collectionIndexFolder, boolean overwriteExistingIndex) {
	    LinkedList<Document> docs = new LinkedList<Document>();
		
		for(InstitutionalCollection c : collections)
		{
			log.debug("Adding collection " + c);
			docs.add(getDocument(c));
		}
			
		IndexWriter writer = null;
		Directory directory = null;
		try {
			directory = FSDirectory.open(collectionIndexFolder);
			
			if(overwriteExistingIndex)
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
		finally 
		{
			if (writer != null) 
			{
			    try 
			    {
				    writer.close();
			    }
			    catch (Exception e) 
			    {
				    log.error(e);
				    try
				    {
				    	if( IndexWriter.isLocked(directory))
						{
				            IndexWriter.unlock(directory);
						}
			     	}
			    	catch (IOException e1)
			    	{
						log.error(e1);
					}
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
		    docs = null;
		}
	}

	/**
	 * Add the institutional collection to the index.
	 * 
	 * @param collection - institutional collection to add.
	 * @param collectionIndexFolder - folder which holds the institutional collection index.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionIndexService#addToIndex(edu.ur.ir.institution.InstitutionalCollection, java.io.File)
	 */
	public void add(InstitutionalCollection collection,
			File collectionIndexFolder) throws NoIndexFoundException {
		if( log.isDebugEnabled() )
		{
			log.debug("adding institutional collection: " + collection + " to index folder " + collectionIndexFolder.getAbsolutePath());
		}
		
		if( !collectionIndexFolder.exists())
		{
			throw new NoIndexFoundException("the folder " + collectionIndexFolder.getAbsolutePath() + " could not be found");
		}
	    writeDocument(collectionIndexFolder.getAbsolutePath(), getDocument(collection));
		
	}

	/**
	 * Delete the collection in the index.
	 * 
	 * @param collectionId - id of the collection
	 * @param collectionIndexFolder  - folder location of the collection index
	 * @see edu.ur.ir.institution.InstitutionalCollectionIndexService#deleteFromIndex(java.lang.Long, java.io.File)
	 */
	public void delete(Long collectionId, File collectionIndexFolder) {
		if( log.isDebugEnabled() )
		{
			log.debug("deleting collection id : " + collectionId + " from index folder " + collectionIndexFolder.getAbsolutePath());
		}
		// if there is not collection folder then don't do anything
		if( collectionIndexFolder == null || !collectionIndexFolder.exists() || collectionIndexFolder.list() == null ||
				collectionIndexFolder.list().length == 0)
		{
			return;
		}
		
		Directory directory = null;
		IndexWriter writer = null;
		try {
			directory = FSDirectory.open(collectionIndexFolder);
			writer = getWriter(directory);
			Term term = new Term(ID, NumericUtils.longToPrefixCoded(collectionId));
			writer.deleteDocuments(term);
			writer.commit();
			
		} catch (Exception e) {
			log.error(e);
			errorEmailService.sendError(e);
		}
		finally {
			try 
		    {
			    writer.close();
		    }
		    catch (Exception e) 
		    {
			    log.error(e);
			    try
			    {
			    	if( IndexWriter.isLocked(directory))
					{
			            IndexWriter.unlock(directory);
					}
		     	}
		    	catch (IOException e1)
		    	{
					log.error(e1);
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
	 * Optimize the specified collection index.
	 * 
	 * @param collectionIndex - folder location of the institutional collection index
	 * @see edu.ur.ir.institution.InstitutionalCollectionIndexService#optimize(java.io.File)
	 */
	public void optimize(File collectionIndex) {
		IndexWriter writer = null;
		Directory directory = null;
		try 
		{
		    directory = FSDirectory.open(collectionIndex);
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
		    	try 
			    {
				    writer.close();
			    }
			    catch (Exception e) 
			    {
				    log.error(e);
				    try
				    {
				    	if( IndexWriter.isLocked(directory))
						{
				            IndexWriter.unlock(directory);
						}
			     	}
			    	catch (IOException e1)
			    	{
						log.error(e1);
					}
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
	 * Update the institutional collection to the index.
	 * 
	 * @param collection - institutional collection to add.
	 * @param collectionIndexFolder - folder which holds the institutional collection index.
	 * @see edu.ur.ir.institution.InstitutionalCollectionIndexService#updateIndex(edu.ur.ir.institution.InstitutionalCollection, java.io.File)
	 */
	public void update(InstitutionalCollection collection,
			File collectionIndexFolder) throws NoIndexFoundException {
		if( log.isDebugEnabled() )
		{
			log.debug("updating index for institutional collection : " + collection + " in index folder " + collectionIndexFolder.getAbsolutePath());
		}
		delete(collection.getId(), collectionIndexFolder);
		add(collection, collectionIndexFolder);
		
	}
	
	/**
	 * Service to deal with institutional collection information.
	 * 
	 * @return service used to deal with institutional collection information
	 */
	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	/**
	 * Service to deal with institutional collection information.
	 * 
	 * @param institutionalCollectionService - service to deal with institutional collection information.
	 */
	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}
	
	/**
	 * Get the analyzer used for indexing text.
	 * 
	 * @return
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * Set the analyzer used for indexing text.
	 * 
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	
	/**
	 * Service for dealing with emailing errors.
	 * 
	 * @return - service used for sending email errors.
	 */
	public ErrorEmailService getErrorEmailService() {
		return errorEmailService;
	}

	/**
	 * Set the error email service for dealing with errors.
	 * 
	 * @param errorEmailService
	 */
	public void setErrorEmailService(ErrorEmailService errorEmailService) {
		this.errorEmailService = errorEmailService;
	}

	/**
	 * Get the document for the institutional collection
	 * 
	 * @param institutional collection - institutional collection to create the document from
	 * @return - the created document
	 */
	private Document getDocument(InstitutionalCollection collection)
	{
		Document doc = new Document();
 	    doc.add(new Field(ID, 
 	    		NumericUtils.longToPrefixCoded(collection.getId()), 
	            Field.Store.YES, 
			    Field.Index.NOT_ANALYZED));
	    
	    String name = collection.getName();
	    doc.add(new Field(NAME, 
				name, 
				Field.Store.YES, 
				Field.Index.ANALYZED));
	    
	    if(collection.getDescription() != null && !collection.getDescription().equals(""))
		{
			doc.add(new Field(DESCRIPTION, 
					collection.getDescription(), 
					Field.Store.YES, 
					Field.Index.ANALYZED));
		}
		return doc;
	}

	
	/**
	 * Write the document to the index in the directory.
	 * 
	 * @param directoryPath - location where the directory exists.
	 * @param documents - documents to add to the directory.
	 */
	private void writeDocument(String directoryPath, Document document)
	{
		log.debug("write document to directory " + directoryPath );
		Directory directory = null;
		IndexWriter writer = null;
		try {
			directory = FSDirectory.open(new File(directoryPath));
			writer = getWriter(directory);
			writer.addDocument(document);
			writer.commit();
		} catch (Exception e) {
			log.error(e);
			errorEmailService.sendError(e);
		}
		finally
		{
			try 
		    {
			    writer.close();
		    }
		    catch (Exception e) 
		    {
			    log.error(e);
			    try
			    {
			    	if( IndexWriter.isLocked(directory))
					{
			            IndexWriter.unlock(directory);
					}
		     	}
		    	catch (IOException e1)
		    	{
					log.error(e1);
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
	 * All methods should use this to obtain a writer on the directory.  
	 * 
	 * @param directory - directory that holds the index
	 * @return - writer that will not overwrite an existing directory if it exists.  
	 * 
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	private IndexWriter getWriter(Directory directory) throws CorruptIndexException, LockObtainFailedException, IOException
	{
	    return new IndexWriter(directory, analyzer, IndexWriter.MaxFieldLength.LIMITED);
	}
	
	/**
	 * All methods should use this to obtain a writer on the directory.  This will return 
	 * a null writer if the index is locked.  A while loop can be set up to determine if an index
	 * writer is available for the specified directory. This ensures that only one writer is writing to a 
	 * users index at once.
	 * 
	 * @param directory
	 * @return - writer set to overwrite the existing index
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	private IndexWriter getWriterOverwriteExisting(Directory directory) throws CorruptIndexException, LockObtainFailedException, IOException
	{
		IndexWriter  writer = new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
		return writer;
	}
	
}
