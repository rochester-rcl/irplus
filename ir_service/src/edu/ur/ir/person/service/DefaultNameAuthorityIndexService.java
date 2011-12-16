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
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.person.NameAuthorityIndexService;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;

/**
 * The default indexing service of file information.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultNameAuthorityIndexService implements NameAuthorityIndexService {
	
	/** eclipse generated id  */
	private static final long serialVersionUID = -2917625239480866680L;

	/** Analyzer for dealing with text indexing */
	private transient Analyzer analyzer;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultNameAuthorityIndexService.class);
	
	/** Person name authority id */ 
	public static final String PERSON_NAME_AUTHORITY_ID = "person_name_authority_id";
	
	/** Contains all the fields of person name  */
	public static final String NAMES = "names";
	
	/** Service for sending email errors */
	private ErrorEmailService errorEmailService;
	
	/**
	 * Analyzer used to analyze the name.
	 * 
	 * @return the analyzer used to analyze the information
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * Set the analyzer user to index the information.
	 * 
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	

	/**
	 * Add the set of names to the index.
	 * 
	 * @see edu.ur.ir.person.NameAuthorityIndexService#addNames(java.util.List, java.io.File, boolean)
	 */
	public void addNames(List<PersonNameAuthority> names, File nameAuthorityIndexFolder,
			boolean overwriteExistingIndex) {
			
	    LinkedList<Document> docs = new LinkedList<Document>();
			
		for(PersonNameAuthority name : names)
		{
			docs.add(getDocument(name));
		}
			
		IndexWriter writer = null;
		Directory directory = null;
		try {
			directory = FSDirectory.open(nameAuthorityIndexFolder);
			
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
		    docs = null;
		}
	}

	/**
	 * Write the list of documents to the index in the directory.
	 * 
	 * @param directoryPath - location where the directory exists.
	 * @param documents - documents to add to the directory.
	 */
	private void writeDocument(File directoryPath, Document document)
	{
		IndexWriter writer = null;
		Directory directory = null;
		try {
			directory = FSDirectory.open(directoryPath);
			writer = getWriter(directory);
			writer.addDocument(document);
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
	 * Add the name to the index.
	 * @throws NoIndexFoundException 
	 * 
	 * @see edu.ur.ir.person.NameAuthorityIndexService#addToIndex(edu.ur.ir.person.PersonNameAuthority, java.io.File)
	 */
	public void addToIndex(PersonNameAuthority personNameAuthority, File nameAuthorityFolder) throws NoIndexFoundException
	{
		if (nameAuthorityFolder == null) {
			throw new NoIndexFoundException("Name index folder not found ");
		} 

		writeDocument(nameAuthorityFolder, getDocument(personNameAuthority));
	}

	/**
	 * Delete the name from the index.
	 * 
	 * @see edu.ur.ir.person.NameAuthorityIndexService#deleteFromIndex(edu.ur.ir.person.PersonNameAuthority)
	 */
	public void deleteFromIndex(PersonNameAuthority personNameAuthority, File nameAuthorityIndexFolder) 
	{
		// if the name index folder doesnot exist 
		// do nothing.
		if (nameAuthorityIndexFolder == null) {
			return;
		} 

		Directory directory = null;
		IndexWriter writer = null;
		
		try {
			directory = FSDirectory.open(nameAuthorityIndexFolder);
			writer = getWriter(directory);
			Term term = new Term(PERSON_NAME_AUTHORITY_ID, personNameAuthority.getId().toString());
			writer.deleteDocuments(term);
			writer.close();
			
		} catch (Exception e) {
			log.error(e);
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
	 * Update the index with the name.
	 */
	public void updateIndex(PersonNameAuthority personNameAuthority, File nameAuthorityIndexFolder) throws NoIndexFoundException {
		deleteFromIndex(personNameAuthority, nameAuthorityIndexFolder);
		addToIndex(personNameAuthority, nameAuthorityIndexFolder);
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
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_35, analyzer);
		IndexWriter writer = new IndexWriter(directory, indexWriterConfig);
		return writer;
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
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_35, analyzer);
		indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter  writer = new IndexWriter(directory, indexWriterConfig);
		return writer;
	}

	
	private Document getDocument(PersonNameAuthority personNameAuthority)
	{
		Document doc = new Document();
		doc.add(new Field(PERSON_NAME_AUTHORITY_ID, 
				personNameAuthority.getId().toString(), 
				Field.Store.YES, 
				Field.Index.NOT_ANALYZED));
		
		StringBuffer name =  new StringBuffer();
		Set<PersonName> names = personNameAuthority.getNames();
		
		for (PersonName personName: names ) {

			if( personName.getForename() != null )
			{
				name.append(" " + personName.getForename() + " ");
			}

			if( personName.getMiddleName()!= null )
			{
				name.append(personName.getMiddleName() + " ");
			}

			if( personName.getFamilyName() != null )
			{
				name.append(personName.getFamilyName() + " ");
			}

			if( personName.getSurname() != null )
			{
				name.append(personName.getSurname() + " ");
			}

			name.append(":");
		}
		
		
		doc.add(new Field(NAMES, 
				names.toString(), 
				Field.Store.YES, 
				Field.Index.ANALYZED));
		
		return doc;

	}

	/**
	 * Service for sending email errors.
	 * 
	 * @return - email error service
	 */
	public ErrorEmailService getErrorEmailService() {
		return errorEmailService;
	}

	/**
	 * Service for sending error emails.
	 * 
	 * @param errorEmailService
	 */
	public void setErrorEmailService(ErrorEmailService errorEmailService) {
		this.errorEmailService = errorEmailService;
	}

}
