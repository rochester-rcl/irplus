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
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumberTools;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import edu.ur.file.db.FolderInfo;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.file.FileCollaborator;
import edu.ur.ir.file.FileVersion;
import edu.ur.ir.index.FileTextExtractor;
import edu.ur.ir.index.FileTextExtractorService;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.item.ItemExtent;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemIdentifier;
import edu.ur.ir.item.ItemLink;
import edu.ur.ir.item.ItemReport;
import edu.ur.ir.item.ItemSponsor;
import edu.ur.ir.item.ItemTitle;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserWorkspaceIndexService;

/**
 * The default indexing service of user information.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultUserWorkspaceIndexService implements UserWorkspaceIndexService {
	
	/** Analyzer for dealing with text indexing */
	private Analyzer analyzer;
	
	/** Service that maintains file text extractors */
	private FileTextExtractorService fileTextExtractorService;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultUserWorkspaceIndexService.class);
	
	/** File system service for users. */
	private UserFileSystemService userFileSystemService;
	
	/** separator used for multi-set data */
	public static final String SEPERATOR = "|";

	
	public static final String PERSONAL_FILE_ID = "personal_file_id";
	
	public static final String FILE_DESCRIPTION = "personal_file_description";
	
	public static final String FILE_VERSION_ID = "file_version_id";
	
	public static final String VERSIONED_FILE_ID = "versioned_file_id";
	
	public static final String BASE_VERSIONED_FILE_NAME = "base_versioned_file_name";

	public static final String FULL_VERSIONED_FILE_NAME = "full_versioned_file_name";
	
	public static final String VERSIONED_FILE_NAME_EXTENSION = "versioned_file_name_extension";

	public static final String FILE_VERSION_CREATOR = "file_version_creator";
	
	public static final String FILE_BODY_TEXT = "file_body_text";
	
	public static final String TYPE = "type";
	
	
	
	public static final String PERSONAL_FOLDER_NAME = "personal_folder_name";
	
	public static final String PERSONAL_FOLDER_DESCRIPTION = "personal_folder_description";
	
	public static final String PERSONAL_FOLDER_ID = "personal_folder_id";
	
	
	
	
	public static final String SHARED_INBOX_FILE_ID = "inbox_file_id";
	
	public static final String COLLABORATORS = "collaborators";
	
	
	
	
	/** id of the item */
	public static final String PERSONAL_ITEM_ID = "item_id";
	
	/** names/title of the item */
	public static final String PERSONAL_ITEM_NAME = "item_name";
	
	/** description of the item */
	public static final String PERSONAL_ITEM_DESCRIPTION = "item_description";
	
	/** names of the links */
	public static final String PERSONAL_ITEM_LINK_NAMES = "item_link_names";
	
	/** text in the files of an item */
	public static final String PERSONAL_ITEM_FILE_NAME = "item_file_name";
	
	/** names of the contributors */
	public static final String PERSONAL_ITEM_CONTRIBUTOR_NAMES = "item_contributor_names";
	
	/** language the item is in  */
	public static final String PERSONAL_ITEM_LANGUAGE = "item_language";
	
	/** identifiers for the item  */
	public static final String PERSONAL_ITEM_IDENTIFIERS = "item_identifiers";
	
	/** abstract for the item */
	public static final String PERSONAL_ITEM_ABSTRACT = "item_abstract";
	
	/** key words for the item */
	public static final String PERSONAL_ITEM_KEY_WORDS = "item_keywords";
	
	/** sub titles for the item  */
	public static final String PERSONAL_ITEM_SUB_TITLES = "item_sub_titles";
	
	/** publisher of the item  */
	public static final String PERSONAL_ITEM_PUBLISHER = "item_publisher";
	
	/** citation if the item has been published */
	public static final String PERSONAL_ITEM_CITATION = "item_citation";
	
	/** citation if the item has been published */
	public static final String PERSONAL_ITEM_CONTENT_TYPE = "item_content_type";
	
	/** Item submitted by */
	public static final String PERSONAL_ITEM_SUBMITTER = "item_submitter";

	/** Series and report for Item */
	public static final String PERSONAL_ITEM_SERIES = "item_series";

	/** Extent type for Item */
	public static final String PERSONAL_ITEM_EXTENTS = "item_extents";
	
	/** Sponsor for Item */
	public static final String PERSONAL_ITEM_SPONSORS = "item_sponsors";
	
	/** name of the collection the item is in */
	public static final String PERSONAL_ITEM_COLLECTION_NAME = "collection_name";
	
	/** collection this item is in */
	public static final String PERSONAL_ITEM_COLLECTION_ID = "collection_id";
	
	/** collection left value */
	public static final String PERSONAL_ITEM_COLLECTION_LEFT_VALUE = "collection_left_value";
	
	/** left value of the collection */
	public static final String PERSONAL_ITEM_COLLECTION_RIGHT_VALUE = "collection_right_value";
	
	
	
	
	/**
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#updateUser(edu.ur.ir.user.IrUser)
	 */
	public void updateUserIndex(Repository repository, IrUser user) {
		// TODO Auto-generated method stub
		
	}

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
	
	/**
	 * Extracts the text from the document.  If no text is extracted an
	 * empty string is returned.
	 * 
	 * @param version
	 * @return text for the document.
	 */
	private String getDocumentBodyText(FileVersion version)
	{
		String text = "";
		String extension = version.getVersionedFile().getExtension();
		File f = new File(version.getIrFile().getFileInfo().getFullPath());
		FileTextExtractor extractor = fileTextExtractorService.getFileTextExtractor(extension);
		if( extractor != null)
		{
			log.debug("Extractor found for extension " + extension);
		    text = extractor.getText(f);
		}
		else
		{
			log.debug("No extractor found for extension " + extension);
		}
		return text;
	}

	/**
	 * Add the personal file to the index.
	 * @throws NoUserIndexFolderException 
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#addToIndex(java.io.File, edu.ur.ir.user.PersonalFile)
	 */
	public void addToIndex(Repository repository, PersonalFile personalFile){
		
		File personalIndexFolder = this.getPersonalIndexFolder(personalFile.getOwner(), repository);
        
        Document doc = new Document();
        
	    doc.add(new Field(PERSONAL_FILE_ID, 
	    		NumberTools.longToString(personalFile.getId()), 
			Field.Store.YES, 
			Field.Index.UN_TOKENIZED));
	    
	    String fileDescription = "" + personalFile.getVersionedFile().getDescription();
	    
	    doc.add( new Field(FILE_DESCRIPTION,
	        fileDescription,
	        Field.Store.NO,
	        Field.Index.TOKENIZED) );
	    
	    doc.add(new Field(TYPE, 
				personalFile.getFileSystemType().getType(), 
				Field.Store.YES,
				Field.Index.TOKENIZED));
	   
	    doc.add(new Field(VERSIONED_FILE_ID, 
	    		NumberTools.longToString(personalFile.getVersionedFile().getId()), 
		 	    Field.Store.YES, 
			    Field.Index.TOKENIZED));

	    FileVersion mostRecentVersion = personalFile.getVersionedFile().getCurrentVersion();

	    
	    String name = mostRecentVersion.getVersionedFile().getName() + "";
	    String extension = mostRecentVersion.getVersionedFile().getExtension() + "";
	    String nameWithExtension = mostRecentVersion.getVersionedFile().getNameWithExtension() + "";
	    String creator = mostRecentVersion.getVersionCreator().getUsername() + "";
	    String text = getDocumentBodyText(mostRecentVersion) + "";
	    String collaboratos = "";
	    
	    Set<FileCollaborator> collabs = personalFile.getVersionedFile().getCollaborators();
	    
	    for(FileCollaborator collaborator : collabs)
	    {
	    	collaboratos = collaboratos + 
	    	collaborator.getCollaborator().getFirstName() + " " +
	    	collaborator.getCollaborator().getLastName() + " " + 
	    	collaborator.getCollaborator().getUsername() + " ";
	    }
		
		doc.add(new Field(BASE_VERSIONED_FILE_NAME, 
					name, 
					Field.Store.YES, 
					Field.Index.TOKENIZED));
			
		doc.add(new Field(VERSIONED_FILE_NAME_EXTENSION, 
					extension, 
					Field.Store.YES, 
					Field.Index.TOKENIZED));
			
		doc.add(new Field(FULL_VERSIONED_FILE_NAME, 
				nameWithExtension, 
				Field.Store.YES, 
				Field.Index.TOKENIZED));
					
		doc.add(new Field(FILE_VERSION_CREATOR,
				creator, 
				Field.Store.YES, 
				Field.Index.TOKENIZED));
		    
		doc.add(new Field(FILE_BODY_TEXT, 
			 	    text, 
				    Field.Store.NO, 
				    Field.Index.TOKENIZED));
		
		doc.add(new Field(VERSIONED_FILE_ID, 
				NumberTools.longToString(personalFile.getVersionedFile().getId()), 
		 	    Field.Store.YES, 
			    Field.Index.UN_TOKENIZED));
			
        
        writeDocument(personalIndexFolder.getAbsolutePath(),  doc);
		
	}
	
	/**
	 * Write the list of documents to the index in the directory.
	 * 
	 * @param directoryPath - location where the directory exists.
	 * @param documents - documents to add to the directory.
	 */
	private void writeDocument(String directoryPath, Document document)
	{
		log.debug("write document to directory " + directoryPath );
		IndexWriter writer = null;
		try {
			
			synchronized(this)
			{
			    Directory directory = FSDirectory.getDirectory(directoryPath);
			    writer = new IndexWriter(directory, analyzer);
			    writer.addDocument(document);
			    writer.flush();
			    writer.optimize();
			}
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
	    }
	}

	/**
	 * Update the index with the personal file
	 * @throws NoUserIndexFolderException 
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#updateIndex(java.io.File, edu.ur.ir.user.PersonalFile)
	 */
	public void updateIndex(Repository repository, PersonalFile personalFile) {
		deleteFromIndex(personalFile);
		addToIndex(repository, personalFile);
	}

	/**
	 * Delete the personal file from the index.
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#deleteFromIndex(edu.ur.ir.user.PersonalFile)
	 */
	public void deleteFromIndex(PersonalFile personalFile) {
		
		FolderInfo info = personalFile.getOwner().getPersonalIndexFolder();
		File personalIndexFolder = null;
		
		// if the user does not have an index folder
		// don't need to do anything.
		if( info == null )
		{
			return;
		}
		else
		{
			personalIndexFolder = new File(info.getFullPath());
			if( !personalIndexFolder.exists()  || personalIndexFolder.list() == null || personalIndexFolder.list().length == 0)
			{
				return;
			}
		}
		
		Directory directory = null;
		IndexReader reader = null;
		try {
			synchronized(this)
			{
			    directory = FSDirectory.getDirectory(personalIndexFolder.getAbsolutePath());
			    if( IndexReader.isLocked(directory) )
			    { 
				    throw new RuntimeException("Users index directory " + personalIndexFolder.getAbsolutePath() +
						" is locked ");
			    }
			    else
			    {
				    reader = IndexReader.open(directory);
				    Term term = new Term(PERSONAL_FILE_ID, NumberTools.longToString(personalFile.getId()));
			        reader.deleteDocuments(term);
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			if( reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
		
	}

	/**
	 * Add the folder to the users index.
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#addToIndex(java.io.File, edu.ur.ir.user.PersonalFolder)
	 */
	public void addToIndex(Repository repository, PersonalFolder personalFolder){
		File personalIndexFolder = this.getPersonalIndexFolder(personalFolder.getOwner(), repository);
		
		Document doc = new Document();
		doc.add(new Field(PERSONAL_FOLDER_NAME, 
				personalFolder.getName(), 
				Field.Store.YES, 
				Field.Index.TOKENIZED));
		
		String description = "";
		if( personalFolder.getDescription() != null )
		{
			description = personalFolder.getDescription();
		}
		
		doc.add(new Field(PERSONAL_FOLDER_DESCRIPTION, 
				description, 
				Field.Store.YES, 
				Field.Index.TOKENIZED));
		
		doc.add(new Field(PERSONAL_FOLDER_ID, 
				NumberTools.longToString(personalFolder.getId()), 
				Field.Store.YES, 
				Field.Index.UN_TOKENIZED));
		
		doc.add(new Field(TYPE, 
				personalFolder.getFileSystemType().getType(), 
				Field.Store.YES, 
				Field.Index.TOKENIZED));
		
		writeDocument(personalIndexFolder.getAbsolutePath(), 
				doc);

	}

	/**
	 * Delete the folder from the index.
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#deleteFromIndex(edu.ur.ir.user.PersonalFolder)
	 */
	public void deleteFromIndex(PersonalFolder personalFolder) {
		
		// if the user does not have an index folder
		// don't need to do anything.
		FolderInfo info = personalFolder.getOwner().getPersonalIndexFolder();
		File personalIndexFolder = null;
		
		// if the user does not have an index folder
		// don't need to do anything.
		if( info == null )
		{
			return;
		}
		else
		{
			personalIndexFolder = new File(info.getFullPath());
			if( !personalIndexFolder.exists())
			{
				return;
			}
		}
		
		Directory directory = null;
		IndexReader reader = null;
		try {
			synchronized(this)
			{
			    directory = FSDirectory.getDirectory(personalIndexFolder.getAbsolutePath());
			    if( IndexReader.isLocked(directory) )
			    {
				    throw new RuntimeException("Users workspace index directory " + personalIndexFolder.getAbsolutePath() +
						" is locked ");
			    }
			    else
			    {
				    reader = IndexReader.open(directory);
				    Term term = new Term(PERSONAL_FOLDER_ID, NumberTools.longToString(personalFolder.getId()));
			        reader.deleteDocuments(term);
			        reader.close();
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			if( reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
	}

	/**
	 * Update the index with the personal folder.
	 * @throws NoIndexFoundException 
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#updateIndex(java.io.File, edu.ur.ir.user.PersonalFolder)
	 */
	public void updateIndex(Repository repository, PersonalFolder personalFolder){
		deleteFromIndex(personalFolder);
		addToIndex(repository, personalFolder);
	}

	
	/**
	 * Add a shared inbox file to the index.
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#addToIndex(java.io.File, edu.ur.ir.user.SharedInboxFile)
	 */
	public void addToIndex(Repository repository, SharedInboxFile inboxFile){
		File personalIndexFolder = this.getPersonalIndexFolder(inboxFile.getSharedWithUser(), repository);
        Document doc = new Document();
        
        doc.add(new Field(SHARED_INBOX_FILE_ID,
        		NumberTools.longToString(inboxFile.getId()), 
			Field.Store.YES, 
			Field.Index.UN_TOKENIZED));
        
	    String fileDescription = "" + inboxFile.getVersionedFile().getDescription();
	    
	    doc.add( new Field(FILE_DESCRIPTION,
	        fileDescription,
	        Field.Store.NO,
	        Field.Index.TOKENIZED) );
	    
	    doc.add(new Field(TYPE, 
	    		inboxFile.getFileSystemType().getType(), 
				Field.Store.YES,
				Field.Index.UN_TOKENIZED));
	   
	    doc.add(new Field(VERSIONED_FILE_ID, 
	    		inboxFile.getVersionedFile().getId().toString(), 
		 	    Field.Store.YES, 
			    Field.Index.UN_TOKENIZED));

	    FileVersion mostRecentVersion = inboxFile.getVersionedFile().getCurrentVersion();

	    
	    String name = mostRecentVersion.getVersionedFile().getName() + "";
	    String extension = mostRecentVersion.getVersionedFile().getExtension() + "";
	    String nameWithExtension = mostRecentVersion.getVersionedFile().getNameWithExtension() + "";
	    String creator = mostRecentVersion.getVersionCreator().getUsername() + "";
	    String text = getDocumentBodyText(mostRecentVersion) + "";
	    String collaboratos = "";
	    
	    Set<FileCollaborator> collabs = inboxFile.getVersionedFile().getCollaborators();
	    
	    for(FileCollaborator collaborator : collabs)
	    {
	    	collaboratos = collaboratos + 
	    	collaborator.getCollaborator().getFirstName() + " " +
	    	collaborator.getCollaborator().getLastName() + " " + 
	    	collaborator.getCollaborator().getUsername() + " ";
	    }
		
		doc.add(new Field(BASE_VERSIONED_FILE_NAME, 
					name, 
					Field.Store.YES, 
					Field.Index.TOKENIZED));
			
		doc.add(new Field(VERSIONED_FILE_NAME_EXTENSION, 
					extension, 
					Field.Store.YES, 
					Field.Index.TOKENIZED));
			
		doc.add(new Field(FULL_VERSIONED_FILE_NAME, 
				nameWithExtension, 
				Field.Store.YES, 
				Field.Index.TOKENIZED));
					
		doc.add(new Field(FILE_VERSION_CREATOR,
				creator, 
				Field.Store.YES, 
				Field.Index.TOKENIZED));
		    
		doc.add(new Field(FILE_BODY_TEXT, 
			 	    text, 
				    Field.Store.NO, 
				    Field.Index.TOKENIZED));
		
		doc.add(new Field(VERSIONED_FILE_ID, 
				inboxFile.getVersionedFile().getId().toString(), 
		 	    Field.Store.YES, 
			    Field.Index.UN_TOKENIZED));
		
        
        writeDocument(personalIndexFolder.getAbsolutePath(), 
        		doc);
		
	}

	/**
	 * Delete the shared inbox file from the index.
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#deleteFromIndex(java.io.File, edu.ur.ir.user.SharedInboxFile)
	 */
	public void deleteFromIndex(SharedInboxFile inboxFile) {
		
		// if the user does not have an index folder
		// don't need to do anything.
		FolderInfo info = inboxFile.getSharedWithUser().getPersonalIndexFolder();
		File personalIndexFolder = null;
		
		// if the user does not have an index folder
		// don't need to do anything.
		if( info == null )
		{
			return;
		}
		else
		{
			personalIndexFolder = new File(info.getFullPath());
			if( !personalIndexFolder.exists())
			{
				return;
			}
		}
		
		Directory directory = null;
		IndexReader reader = null;
		try {
			synchronized(this)
			{
			    directory = FSDirectory.getDirectory(personalIndexFolder.getAbsolutePath());
			    if( IndexReader.isLocked(directory) )
			    {
				    throw new RuntimeException("Users workspace index directory " + personalIndexFolder.getAbsolutePath() +
						" is locked ");
			    }
			    else
			    {
				    reader = IndexReader.open(directory);
				    Term term = new Term(SHARED_INBOX_FILE_ID, NumberTools.longToString(inboxFile.getId()));
			        reader.deleteDocuments(term);
			        reader.close();
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			if( reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
		
	}

	/**
	 * Update the index whith the new shared file.
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#updateIndex(java.io.File, edu.ur.ir.user.SharedInboxFile)
	 */
	public void updateIndex(Repository repository, SharedInboxFile inboxFile){
		deleteFromIndex(inboxFile);
		addToIndex(repository, inboxFile);
	}

	
	/* (non-Javadoc)
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#updateAllIndexes(edu.ur.ir.user.PersonalFile)
	 */
	public void updateAllIndexes(Repository repository, PersonalFile personalFile) {
		
        IrUser owner = personalFile.getOwner();
		PersonalFile ownerFile = userFileSystemService.getPersonalFile(owner, 
		    		personalFile.getVersionedFile().getCurrentVersion().getIrFile());
		updateIndex(repository, ownerFile);
    	
    	//add the new version to all users
    	Set<FileCollaborator> collaborators = personalFile.getVersionedFile().getCollaborators();
    	for(FileCollaborator collaborator : collaborators)
    	{
     	    PersonalFile collaboratorFile = userFileSystemService.getPersonalFile(collaborator.getCollaborator(), 
    	        personalFile.getVersionedFile().getCurrentVersion().getIrFile());
    	    	 
    	    if( collaboratorFile != null )
    	    {
    	        updateIndex(repository, collaboratorFile);
			}
    	    else
    	    {
    	        SharedInboxFile sharedInboxFile = collaborator.getCollaborator().getSharedInboxFile(personalFile.getVersionedFile());
    	    	if( sharedInboxFile != null)
    	    	{
    	    	    updateIndex(repository, sharedInboxFile);
    	    	}
    	    }
    	}
	}
	
	
	/**
	 * Removes the personal file from all indexes.
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#deleteFromAllIndexes(edu.ur.ir.user.PersonalFile)
	 */
	public void deleteFromAllIndexes(PersonalFile personalFile) {
        IrUser owner = personalFile.getOwner();
		PersonalFile ownerFile = userFileSystemService.getPersonalFile(owner, 
		    		personalFile.getVersionedFile().getCurrentVersion().getIrFile());
		deleteFromIndex(ownerFile);
    	
    	//add the new version to all users
    	Set<FileCollaborator> collaborators = personalFile.getVersionedFile().getCollaborators();
    	for(FileCollaborator collaborator : collaborators)
    	{
     	    PersonalFile collaboratorFile = userFileSystemService.getPersonalFile(collaborator.getCollaborator(), 
    	        personalFile.getVersionedFile().getCurrentVersion().getIrFile());
    	    	 
    	    if( collaboratorFile != null )
    	    {
    	    	deleteFromIndex(collaboratorFile);
			}
    	    else
    	    {
    	        SharedInboxFile sharedInboxFile = collaborator.getCollaborator().getSharedInboxFile(personalFile.getVersionedFile());
    	    	if( sharedInboxFile != null)
    	    	{
    	    		deleteFromIndex(sharedInboxFile);
    	    	}
    	    }
    	}
		
	}

	/**
	 * Add the personal item to the index.
	 * @throws NoUserIndexFolderException 
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#addToIndex(java.io.File, edu.ur.ir.user.PersonalFile)
	 */
	public void addToIndex(Repository repository, PersonalItem personalItem)
	{
		File personalIndexFolder = getPersonalIndexFolder(personalItem.getOwner(), repository);
		this.writeDocument(personalIndexFolder.getAbsolutePath(), getDocument(personalItem));
	}
	
	/**
	 * Delete the personal item from the index.
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#deleteFromIndex(edu.ur.ir.user.PersonalItem)
	 */
	public void deleteFromIndex(PersonalItem personalItem)
	{
		// if the user does not have an index folder
		// don't need to do anything.
		FolderInfo info = personalItem.getOwner().getPersonalIndexFolder();
		File personalIndexFolder = null;
		
		// if the user does not have an index folder
		// don't need to do anything.
		if( info == null )
		{
			return;
		}
		else
		{
			personalIndexFolder = new File(info.getFullPath());
			if( !personalIndexFolder.exists())
			{
				return;
			}
		}
		
		Directory directory = null;
		IndexReader reader = null;
		try {
			synchronized(this)
			{
			    directory = FSDirectory.getDirectory(personalIndexFolder.getAbsolutePath());
			    if( IndexReader.isLocked(directory) )
			    {
				    throw new RuntimeException("Users workspace index directory " + personalIndexFolder.getAbsolutePath() +
						" is locked ");
			    }
			    else
			    {
				    reader = IndexReader.open(directory);
				    Term term = new Term(PERSONAL_ITEM_ID, NumberTools.longToString(personalItem.getId()));
			        reader.deleteDocuments(term);
			        reader.close();
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			if( reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
	}
	
	/**
	 * Update the index for the specified item.
	 * 
	 * @see edu.ur.ir.user.UserWorkspaceIndexService#updateIndex(edu.ur.ir.repository.Repository, edu.ur.ir.user.PersonalItem)
	 */
	public void updateIndex(Repository repository, PersonalItem personalItem)
	{
		deleteFromIndex(personalItem);
		addToIndex(repository, personalItem);
	}

	
	/**
	 * Gets the personal index folder if it exists otherwise a personal index folder
	 * is created.
	 * 
	 * @param user - user to get the personal index folder
	 * @param repository - repository to create the folder in if the user does not have an index folder
	 * @return the folder 
	 */
	private File getPersonalIndexFolder(IrUser user, Repository repository)
	{
		if( user.getPersonalIndexFolder() == null )
		{
			userFileSystemService.createIndexFolder(user, repository, 
					user.getUsername() + " Index Folder");
		}
		
		File personalIndexFolder = new File(user.getPersonalIndexFolder().getFullPath());
		return personalIndexFolder;
	}

	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}
	
	/**
	 * Create a document for the institutional item.
	 * 
	 * @param institutionalItem
	 * @return - the created document
	 */
	private Document getDocument(PersonalItem personalItem)
	{
		GenericItem genericItem = personalItem.getVersionedItem().getCurrentVersion().getItem();
		
		Document doc = new Document();
		doc.add(new Field(PERSONAL_ITEM_ID, 
				NumberTools.longToString(personalItem.getId()), 
				Field.Store.YES, 
				Field.Index.UN_TOKENIZED));
		
		
		String name = genericItem.getName();
		if(name != null && !name.trim().equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_NAME, 
					name, 
					Field.Store.NO, 
					Field.Index.TOKENIZED));
		}
		
		String description = genericItem.getDescription();
		if(description != null && !description.trim().equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_DESCRIPTION, 
					description, 
					Field.Store.NO, 
					Field.Index.TOKENIZED));
		}
		
		if( personalItem.getPersonalCollection() != null)
		{
		    String collectionName = personalItem.getPersonalCollection().getName();
		
		    doc.add(new Field(PERSONAL_ITEM_COLLECTION_NAME, 
				collectionName, 
				Field.Store.YES, 
				Field.Index.TOKENIZED));
		
		    String collectionId =  NumberTools.longToString(personalItem.getPersonalCollection().getId());
		    doc.add(new Field(PERSONAL_ITEM_COLLECTION_ID, 
				collectionId, 
				Field.Store.YES, 
				Field.Index.TOKENIZED));
		
		
		    String collectionLeftValue = NumberTools.longToString(personalItem.getPersonalCollection().getLeftValue());		
		    doc.add(new Field(PERSONAL_ITEM_COLLECTION_LEFT_VALUE, 
				collectionLeftValue, 
				Field.Store.YES, 
				Field.Index.TOKENIZED));
		
		    String collectionRightValue = NumberTools.longToString(personalItem.getPersonalCollection().getRightValue());		
		    doc.add(new Field(PERSONAL_ITEM_COLLECTION_RIGHT_VALUE, 
				collectionRightValue, 
				Field.Store.YES, 
				Field.Index.TOKENIZED));
		}
		// get the contributors
		String contributorString = getContributorNames(genericItem);
		if(!contributorString.trim().equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_CONTRIBUTOR_NAMES, 
					contributorString, 
					Field.Store.YES, 
					Field.Index.TOKENIZED));
		}
		
		// get the contributors
		String linksString = getLinkNames(genericItem);
		if(!linksString.trim().equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_LINK_NAMES, 
					linksString, 
					Field.Store.NO, 
					Field.Index.TOKENIZED));
		}
		
		// get the text for the files
		String fileNames = getFileNames(genericItem);
		if(!fileNames.trim().equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_FILE_NAME, 
					fileNames, 
					Field.Store.YES, 
					Field.Index.TOKENIZED));
		}
		
		//index the language
		String language = null;
		if( genericItem.getLanguageType() != null )
		{
		    language = genericItem.getLanguageType().getName();
		}
		if(language != null && !language.trim().equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_LANGUAGE, 
					language, 
					Field.Store.YES, 
					Field.Index.TOKENIZED));
		}
		
		//index the submitter
		if( genericItem.getOwner() != null )
		{
			doc.add(new Field(PERSONAL_ITEM_SUBMITTER, 
					genericItem.getOwner().getFirstName() + " " + genericItem.getOwner().getLastName(), 
					Field.Store.YES, 
					Field.Index.TOKENIZED));
		}

		// index the series
		String series = getSeries(genericItem);
		if(!series.trim().equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_SERIES, 
					series, 
					Field.Store.NO, 
					Field.Index.TOKENIZED));
		}

		// index the extent
		String extents = getExtentTypes(genericItem);
		if(!extents.trim().equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_EXTENTS, 
					extents, 
					Field.Store.NO, 
					Field.Index.TOKENIZED));
		}
		
		// index the sponsors
		String sponsors = getSponsors(genericItem);
		if(!sponsors.trim().equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_SPONSORS, 
					sponsors, 
					Field.Store.NO, 
					Field.Index.TOKENIZED));
		}
		
		// index the identifiers
		String identifiers = getIdentifiers(genericItem);
		if(!identifiers.trim().equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_IDENTIFIERS, 
					identifiers, 
					Field.Store.YES, 
					Field.Index.TOKENIZED));
		}
		
		//index the item abstract
		String itemAbstract = genericItem.getItemAbstract();
		if(itemAbstract != null && !itemAbstract.equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_ABSTRACT, 
					itemAbstract, 
					Field.Store.NO, 
					Field.Index.TOKENIZED));
		}
		
		//keywords for the item
		String keywords = getSubjects(genericItem.getItemKeywords());
		
		if(keywords != null && !keywords.equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_KEY_WORDS, 
					keywords, 
					Field.Store.YES, 
					Field.Index.TOKENIZED));
		}
		
		//subtitles for the item
		String subTitles = getSubTitles(genericItem);
		if(subTitles != null && !subTitles.equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_SUB_TITLES, 
					subTitles, 
					Field.Store.NO, 
					Field.Index.TOKENIZED));
		}	
		
		// Types for item
		String contentTypes = getContentTypes(genericItem);
		if(contentTypes != null && !contentTypes.equals(""))
		{
			doc.add(new Field(PERSONAL_ITEM_CONTENT_TYPE, 
					contentTypes, 
					Field.Store.YES, 
					Field.Index.TOKENIZED));
		}
	
		doc.add(new Field(TYPE, 
				personalItem.getFileSystemType().getType(), 
				Field.Store.YES,
				Field.Index.TOKENIZED));
		
		//publisher information
		if( genericItem.getExternalPublishedItem() != null  )
		{
			if(genericItem.getExternalPublishedItem().getPublisher() != null)
			{
				String publisherName = genericItem.getExternalPublishedItem().getPublisher().getName();
				if( publisherName != null && !publisherName.equals(""))
				{
					doc.add(new Field(PERSONAL_ITEM_PUBLISHER, 
							publisherName, 
							Field.Store.YES, 
							Field.Index.TOKENIZED));
				}
			}
	
			//citation information
			String citation = genericItem.getExternalPublishedItem().getCitation();
			if( citation != null && !citation.equals(""))
			{
				doc.add(new Field(PERSONAL_ITEM_CITATION, 
						citation, 
						Field.Store.NO, 
						Field.Index.TOKENIZED));
			}
		}
		
		return doc;
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
	 * Get the series
	 * 
	 * @param item
	 * @return
	 */
	private String getSeries(GenericItem genericItem)
	{
		StringBuffer sb = new StringBuffer();
		Set<ItemReport> series = genericItem.getItemReports();
		
		for(ItemReport s : series)
		{
			sb.append(" " + s.getSeries().getName() + " ");
			sb.append(" " + s.getReportNumber() + " ");
			sb.append(SEPERATOR);
		}
		return sb.toString();
	}
	
	/**
	 * Get the extent types
	 * 
	 * @param item
	 * @return
	 */
	private String getExtentTypes(GenericItem genericItem)
	{
		StringBuffer sb = new StringBuffer();
		Set<ItemExtent> extents = genericItem.getItemExtents();
		
		for(ItemExtent extent : extents)
		{
			sb.append(" " + extent.getExtentType().getName() + " ");
			sb.append(" " + extent.getValue() + " ");
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
		
		for(ItemSponsor sponsor: sponsors)
		{
			sb.append(" " + sponsor.getSponsor().getName() + " ");
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
	 * Item's file names
	 */
	private String getFileNames(GenericItem genericItem)
	{
		
		StringBuffer sb = new StringBuffer();
		Set<ItemFile> files = genericItem.getItemFiles();
		for(ItemFile itemFile : files)
		{
			sb.append(" " + itemFile.getIrFile().getNameWithExtension() + " ");
			sb.append(SEPERATOR);
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


}
