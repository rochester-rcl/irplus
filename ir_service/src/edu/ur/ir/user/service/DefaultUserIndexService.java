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

import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.user.Department;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserIndexService;

/**
 * This updates the specified index with the given user information
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultUserIndexService implements UserIndexService{
	
	public static final String USER_ID = "user_id";
	public static final String USER_NAME = "user_name";
	public static final String USER_FIRST_NAME = "user_first_name";
	public static final String USER_LAST_NAME = "user_last_name";
	public static final String USER_EMAILS = "user_emails";
	public static final String USER_DEPARTMENTS = "user_departments";
	public static final String USER_NAMES = "user_names";
	
	/** separator used for multi-set data */
	public static final String SEPERATOR = "|";
	
	/** Analyzer for dealing with text indexing */
	private Analyzer analyzer;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultUserIndexService.class);
	
	/**  Service for sending an email when an error occurs */
	private ErrorEmailService errorEmailService;



	/**
	 * Analyzer used to analyze the file.
	 * 
	 * @return the analyzer used to analyze the information
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * Set the analyzer user to index the information.
	 * s
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	
	
	/**
	 * Add the user to the index.
	 * 
	 * @see edu.ur.ir.user.UserIndexService#addToIndex(edu.ur.ir.user.IrUser, java.io.File)
	 */
	public void addToIndex(IrUser user, File userIndexFolder)
			throws NoIndexFoundException {
		
		if( log.isDebugEnabled() )
		{
			log.debug("adding user: " + user + " to index folder " + userIndexFolder.getAbsolutePath());
		}
		
		if( !userIndexFolder.exists())
		{
			throw new NoIndexFoundException("the folder " + userIndexFolder.getAbsolutePath() + " could not be found");
		}
		
	    writeDocument(userIndexFolder.getAbsolutePath(), getDocument(user));
	}

	
	/**
	 * Delete the user from the specified index.
	 * 
	 * @see edu.ur.ir.user.UserIndexService#deleteFromIndex(edu.ur.ir.user.IrUser, java.io.File)
	 */
	public void deleteFromIndex(Long userId, File userIndexFolder) {
		if( log.isDebugEnabled() )
		{
			log.debug("deleting user id: " + userId + " from index folder " + userIndexFolder.getAbsolutePath());
		}
		// if the user does not have an index folder
		// don't need to do anything.
		if( userIndexFolder == null || !userIndexFolder.exists() || userIndexFolder.list() == null ||
				userIndexFolder.list().length == 0)
		{
			return;
		}
		
		Directory directory = null;
		IndexWriter writer = null;
		try {
			directory = FSDirectory.getDirectory(userIndexFolder.getAbsolutePath());
			writer = getWriter(directory);
			Term term = new Term(USER_ID, userId.toString());
			writer.deleteDocuments(term);
			
		} catch (IOException e) {
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
		}
		
	}

	
	/**
	 * Update the index with the specified existing user.
	 * 
	 * @see edu.ur.ir.user.UserIndexService#updateIndex(edu.ur.ir.user.IrUser, java.io.File)
	 */
	public void updateIndex(IrUser user, File userIndexFolder)
			throws NoIndexFoundException {
		if( log.isDebugEnabled() )
		{
			log.debug("updating index for user: " + user + " in index folder " + userIndexFolder.getAbsolutePath());
		}
		deleteFromIndex(user.getId(), userIndexFolder);
		addToIndex(user, userIndexFolder);
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
		IndexWriter writer = null;
		Directory directory = null;
		try {
			directory = FSDirectory.getDirectory(directoryPath);
			writer = getWriter(directory);
			writer.addDocument(document);
			writer.commit();
			writer.optimize();
			
		} catch (IOException e) {
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
	 * Add users to the index
	 * @see edu.ur.ir.user.UserIndexService#addUsers(java.util.List, java.io.File, boolean)
	 */
	public void addUsers(List<IrUser> users, File userIndexFolder,
			boolean overwriteExistingIndex) {
			
	    LinkedList<Document> docs = new LinkedList<Document>();
			
		for(IrUser user : users)
		{
			log.debug("adding user " + user);
			docs.add(getDocument(user));
		}
			
		IndexWriter writer = null;
		Directory directory = null;
		try {
			directory = FSDirectory.getDirectory(userIndexFolder.getAbsolutePath());
			
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
			writer.optimize();
			
		}
		catch (IOException e) 
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
	 * Create a document for the specified user.
	 * 
	 * @param user - user to create a document for.
	 * @return - the created document 
	 */
	private Document getDocument(IrUser user)
	{
		Document doc = new Document();
        
	    doc.add(new Field(USER_ID, 
	    		user.getId().toString(), 
			Field.Store.YES, 
			Field.Index.NOT_ANALYZED));
	    
	    doc.add(new Field(USER_NAME, 
				user.getUsername(), 
				Field.Store.YES, 
				Field.Index.ANALYZED));
	    
	    if( user.getFirstName() != null )
	    {
	        doc.add(new Field(USER_FIRST_NAME, 
				user.getFirstName(), 
				Field.Store.YES, 
				Field.Index.ANALYZED));
	    }
	    
	    if( user.getLastName() != null )
	    {
	        doc.add(new Field(USER_LAST_NAME, 
				user.getLastName(), 
				Field.Store.YES, 
				Field.Index.ANALYZED));
	    }
	    
	    String emails = "";
	    for( UserEmail email : user.getUserEmails())
	    {
	    	if( email != null )
	    	{
	    		emails += email.getEmail() + " ";
	    	}
	    }
	    
	    doc.add(new Field(USER_EMAILS, 
				emails, 
				Field.Store.YES, 
				Field.Index.ANALYZED));
	    
	    if( user.getDepartments() != null )
	    {
	    	StringBuffer departmentNames = new StringBuffer();
	    	for(Department d : user.getDepartments())
	    	{
	    		departmentNames.append(d.getName());
		    	departmentNames.append(SEPERATOR);
	    	}
	    	
	    	String names = departmentNames.toString();
		    if( names != null && !names.trim().equals(""))
		    {
	    	    doc.add(new Field(USER_DEPARTMENTS, 
	    	    		names, 
					Field.Store.YES, 
					Field.Index.ANALYZED));
		    }
	    }

	    if (user.getPersonNameAuthority() != null) {
		    StringBuffer names =  new StringBuffer();
		    
		    for( PersonName personName : user.getPersonNameAuthority().getNames()) {
	
				if( personName.getForename() != null )
				{
					names.append(" " + personName.getForename() + " ");
				}

				if( personName.getMiddleName()!= null )
				{
					names.append(personName.getMiddleName() + " ");
				}

				if( personName.getFamilyName() != null )
				{
					names.append(personName.getFamilyName() + " ");
				}

				if( personName.getSurname() != null )
				{
					names.append(personName.getSurname() + " ");
				}
				
				names.append(":");
			}

		    doc.add(new Field(USER_NAMES, 
					names.toString(), 
					Field.Store.YES, 
					Field.Index.ANALYZED));
	    }
	    
	    return doc;
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
		IndexWriter writer = new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
		return writer;
	}
	
	/**
	 * Email error service.
	 * 
	 * @return
	 */
	public ErrorEmailService getErrorEmailService() {
		return errorEmailService;
	}

	/**
	 * Service for dealing with emails.
	 * 
	 * @param errorEmailService
	 */
	public void setErrorEmailService(ErrorEmailService errorEmailService) {
		this.errorEmailService = errorEmailService;
	}


	

}
