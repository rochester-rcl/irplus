/**  
   Copyright 2008 - 2012 University of Rochester

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

package edu.ur.ir.groupspace.service;

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
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;

import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceIndexService;


/**
 * Default implementation for indexing group workspaces
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultGroupWorkspaceIndexService implements GroupWorkspaceIndexService {

	// eclipse generated id
	private static final long serialVersionUID = -8999945188335022796L;
	
	/** Id of group workspace */
	public static final String ID = "id";
	
	/** Name of the group workspace */
	public static final String GROUP_NAME = "group_name";
	
	/** Description of the group workspace */
	public static final String GROUP_DESCRIPTION = "group_description";

	/** Analyzer for dealing with text indexing */
	private transient Analyzer analyzer;
	
	/** Service for sending email errors */
	private ErrorEmailService errorEmailService;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultGroupWorkspaceIndexService.class);
	
	/**
	 * Analyzer used to analyze the file.
	 * 
	 * @return the analyzer used to analyze the information
	 */
	public Analyzer getAnalyzer() 
	{
		return analyzer;
	}

	/**
	 * Set the analyzer researcher to index the information.
	 * 
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) 
	{
		this.analyzer = analyzer;
	}
	
	
	public void add(GroupWorkspace groupWorkspace, File indexFolder)
			throws NoIndexFoundException 
	{
		if( log.isDebugEnabled() )
		{
			log.debug("adding researcher: " + groupWorkspace + " to index folder " + indexFolder.getAbsolutePath());
		}
		
		if( !indexFolder.exists())
		{
			throw new NoIndexFoundException("the folder " + indexFolder.getAbsolutePath() + " could not be found");
		}
	    writeDocument(indexFolder, getDocument(groupWorkspace));
		
	}

	public void add(List<GroupWorkspace> groupWorkspaces, File indexFolder,
			boolean overwriteExistingIndex) 
	{
	    LinkedList<Document> docs = new LinkedList<Document>();
		
		for(GroupWorkspace w : groupWorkspaces)
		{
			log.debug("Adding workspace " + w);
			docs.add(getDocument(w));
		}
			
		IndexWriter writer = null;
		Directory directory = null;
		try 
		{
			directory = FSDirectory.open(indexFolder);
			
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
			    try 
			    {
				    writer.close();
			    } 
			    catch (Exception e) 
			    {
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
		    docs = null;
		}
		
	}

	public void delete(Long groupWorkspaceId, File indexFolder) 
	{
		if( log.isDebugEnabled() )
		{
			log.debug("deleting researcher id : " + groupWorkspaceId + " from index folder " + indexFolder.getAbsolutePath());
		}
		// if the researcher does not have an index folder
		// don't need to do anything.
		if( indexFolder == null || !indexFolder.exists() || indexFolder.list() == null ||
				indexFolder.list().length == 0)
		{
			return;
		}
		
		Directory directory = null;
		IndexWriter writer = null;
		try {
			directory = FSDirectory.open(indexFolder);
			writer = getWriter(directory);
			Term term = new Term(ID, NumericUtils.longToPrefixCoded(groupWorkspaceId));
			writer.deleteDocuments(term);
			writer.commit();
			
		}
		catch (Exception e) 
		{
			log.error(e);
			errorEmailService.sendError(e);
		}
		finally {
			if (writer != null) 
			{
			    try 
			    {
				    writer.close();
			    } 
			    catch (Exception e) 
			    {
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

	public void update(GroupWorkspace groupWorkspace, File indexFolder)
			throws NoIndexFoundException 
	{
		if( log.isDebugEnabled() )
		{
			log.debug("updating index for group workspace: " + groupWorkspace + " in index folder " + indexFolder.getAbsolutePath());
		}
		delete(groupWorkspace.getId(), indexFolder);
		add(groupWorkspace, indexFolder);
		
	}
	
	/**
	 * Get the document for the researcher 
	 * 
	 * @param researcher - researcher to create the document from
	 * @return - the created document
	 */
	private Document getDocument(GroupWorkspace groupWorkspace)
	{
		Document doc = new Document();
        
	    doc.add(new Field(ID, 
	    	NumericUtils.longToPrefixCoded(groupWorkspace.getId()),
			Field.Store.YES, 
			Field.Index.NOT_ANALYZED));
	    
	    // name of the group workspace
	    String name = groupWorkspace.getName();
	    if( name != null && !name.trim().equals(""))
	    {
	    	
	    	doc.add(new Field(GROUP_NAME, 
				name, 
				Field.Store.YES, 
				Field.Index.ANALYZED));
	    }
	    
	    //description for group workspace
		String description = groupWorkspace.getDescription();
	    if(description != null && !description.equals(""))
		{
	    	doc.add(new Field(GROUP_DESCRIPTION, 
	    		description, 
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
	private void writeDocument(File directoryPath, Document document)
	{
		log.debug("write document to directory " + directoryPath );
		Directory directory = null;
		IndexWriter writer = null;
		try 
		{
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
	 * All methods should use this to obtain a writer on the directory.  
	 * 
	 * @param directory - that holds the index
	 * @return - writer that will not overwrite an existing directory if it exists.  This will create a directory if 
	 * one does not yet exist.
	 * 
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
	
	/**
	 * Service for dealing with emails.
	 * 
	 * @param errorEmailService
	 */
	public void setErrorEmailService(ErrorEmailService errorEmailService) 
	{
		this.errorEmailService = errorEmailService;
	}

}
