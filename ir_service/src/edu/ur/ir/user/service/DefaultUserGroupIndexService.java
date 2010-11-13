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
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.NumericUtils;

import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupIndexService;

/**
 * @author ideazoft
 *
 */
public class DefaultUserGroupIndexService implements UserGroupIndexService
{
	/** eclipse generated id */
	private static final long serialVersionUID = -1776477271740814948L;
	

	/** Analyzer for dealing with text indexing */
	private Analyzer analyzer;
	
	/** Service for sending email errors */
	private ErrorEmailService errorEmailService;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultUserGroupIndexService.class);

	/** Id of collection */
	public static final String ID = "id";
	
	/** name of the collection */
	public static final String NAME = "name";
	
	/** description of the collection */
	public static final String DESCRIPTION = "description";
	

	/**
	 * Add the user group to the index.
	 * 
	 * @param userGroup - user group to add.
	 * @param userGroupIndexFolder - folder that holds the user group index.
	 */
	public void add(IrUserGroup userGroup, File userGroupIndexFolder) throws NoIndexFoundException
	{
		if( log.isDebugEnabled() )
		{
			log.debug("adding user group: " + userGroup + " to index folder " + userGroupIndexFolder.getAbsolutePath());
		}
		
		if( !userGroupIndexFolder.exists())
		{
			throw new NoIndexFoundException("the folder " + userGroupIndexFolder.getAbsolutePath() + " could not be found");
		}
	    writeDocument(userGroupIndexFolder.getAbsolutePath(), getDocument(userGroup));
	}
	
	/**
	 * Update the user group in the index.
	 * 
	 * @param userGroup - userGroup to add.
	 * @param userGroupIndexFolder - folder which holds the user groups.
	 */
	public void update(IrUserGroup userGroup, File userGroupIndexFolder) throws NoIndexFoundException
	{
		if( log.isDebugEnabled() )
		{
			log.debug("updating index for user group : " + userGroup + " in index folder " + userGroupIndexFolder.getAbsolutePath());
		}
		delete(userGroup.getId(), userGroupIndexFolder);
		add(userGroup, userGroupIndexFolder);
	}
	
	/**
	 * Delete the user group in the index.
	 * 
	 * @param userGroupId - id of the user
	 * @param userIndexFolder  - folder location of the collection index
	 */
	public void delete(Long userGroupId, File userGroupIndexFolder)
	{
		if( log.isDebugEnabled() )
		{
			log.debug("deleting user group id : " + userGroupId + " from index folder " + userGroupIndexFolder.getAbsolutePath());
		}
		// if there is not collection folder then don't do anything
		if( userGroupIndexFolder == null || !userGroupIndexFolder.exists() || userGroupIndexFolder.list() == null ||
				userGroupIndexFolder.list().length == 0)
		{
			return;
		}
		
		Directory directory = null;
		IndexWriter writer = null;
		try {
			directory = FSDirectory.open(userGroupIndexFolder);
			writer = new IndexWriter(directory, analyzer, IndexWriter.MaxFieldLength.LIMITED);
			Term term = new Term(ID, NumericUtils.longToPrefixCoded(userGroupId));
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
	 * Re-index the specified user groups.  This can be used to re-index 
	 * all user groups
	 * 
	 * @param userGroups - user groups to re index
	 * @param userGroupIndexFolder - folder location of the index
	 * @param overwriteExistingIndex - if set to true, will overwrite the exiting index.
	 */
	public void add(List<IrUserGroup> userGroups, File userGroupIndexFolder,
			boolean overwriteExistingIndex)
	{
        LinkedList<Document> docs = new LinkedList<Document>();
		
		for(IrUserGroup g : userGroups)
		{
			log.debug("Adding user group " + g);
			docs.add(getDocument(g));
		}
			
		IndexWriter writer = null;
		Directory directory = null;
		try {
			directory = FSDirectory.open(userGroupIndexFolder);
			
			if(overwriteExistingIndex)
			{
			    writer = new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
			}
			else
			{
				writer = new IndexWriter(directory, analyzer, IndexWriter.MaxFieldLength.LIMITED);
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
	 * Optimize the specified user group index.
	 * 
	 * @param userGroupIndex
	 */
	public void optimize(File userGroupIndex)
	{
		IndexWriter writer = null;
		Directory directory = null;
		try 
		{
		    directory = FSDirectory.open(userGroupIndex);
			writer = new IndexWriter(directory, analyzer, IndexWriter.MaxFieldLength.LIMITED);
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
	 * Get the document for the user group
	 * 
	 * @param user group - user group to create the document from
	 * @return - the created document
	 */
	private Document getDocument(IrUserGroup userGroup)
	{
		Document doc = new Document();
 	    doc.add(new Field(ID, 
 	    		NumericUtils.longToPrefixCoded(userGroup.getId()), 
	            Field.Store.YES, 
			    Field.Index.NOT_ANALYZED));
	    
	    String name = userGroup.getName();
	    doc.add(new Field(NAME, 
				name, 
				Field.Store.YES, 
				Field.Index.ANALYZED));
	    
	    if(userGroup.getDescription() != null && !userGroup.getDescription().equals(""))
		{
			doc.add(new Field(DESCRIPTION, 
					userGroup.getDescription(), 
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
			writer =  new IndexWriter(directory, analyzer, IndexWriter.MaxFieldLength.LIMITED);
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
	 * Set the analyzer
	 * 
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * Set the email service.
	 * 
	 * @param errorEmailService
	 */
	public void setErrorEmailService(ErrorEmailService errorEmailService) {
		this.errorEmailService = errorEmailService;
	}

}
