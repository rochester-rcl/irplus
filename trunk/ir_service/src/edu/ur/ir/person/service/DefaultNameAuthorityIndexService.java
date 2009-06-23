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
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

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
	
	/** Analyzer for dealing with text indexing */
	private Analyzer analyzer;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultNameAuthorityIndexService.class);
	
	/** Person name authority id */ 
	public static final String PERSON_NAME_AUTHORITY_ID = "person_name_authority_id";
	
	/** Contains all the fields of person name  */
	public static final String NAMES = "names";
	
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
	 * Write the list of documents to the index in the directory.
	 * 
	 * @param directoryPath - location where the directory exists.
	 * @param documents - documents to add to the directory.
	 */
	private void writeDocument(String directoryPath, Document document)
	{
		IndexWriter writer = null;
		try {
			Directory directory = FSDirectory.getDirectory(directoryPath);
			writer = new IndexWriter(directory, analyzer, IndexWriter.MaxFieldLength.LIMITED);
			writer.addDocument(document);
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
	    }
	}

	/**
	 * Add the name to the index.
	 * @throws NoIndexFoundException 
	 * 
	 * @see edu.ur.ir.person.NameAuthorityIndexService#addToIndex(edu.ur.ir.person.PersonNameAuthority, java.io.File)
	 */
	public void addToIndex(PersonNameAuthority personNameAuthority, File nameAuthorityFolder) throws NoIndexFoundException  {
	
		
		
		if (nameAuthorityFolder == null) {
			throw new NoIndexFoundException("Name index folder not found ");
		} 

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

		writeDocument(nameAuthorityFolder.getAbsolutePath(),	doc);

	}

	/**
	 * Delete the name from the index.
	 * 
	 * @see edu.ur.ir.person.NameAuthorityIndexService#deleteFromIndex(edu.ur.ir.person.PersonNameAuthority)
	 */
	public void deleteFromIndex(PersonNameAuthority personNameAuthority, File nameAuthorityIndexFolder) {
		
		
		// if the name index folder doesnot exist 
		// do nothing.
		if (nameAuthorityIndexFolder == null) {
			return;
		} 

		Directory directory = null;
		IndexReader reader = null;
		
		try {
			directory = FSDirectory.getDirectory(nameAuthorityIndexFolder);
			if( IndexWriter.isLocked(directory) )
			{
				throw new RuntimeException("Users index directory " + nameAuthorityIndexFolder.getAbsolutePath() +
						" is locked ");
			}
			else
			{
				reader = IndexReader.open(directory);
				Term term = new Term(PERSON_NAME_AUTHORITY_ID, personNameAuthority.getId().toString());
			    reader.deleteDocuments(term);
			    reader.close();
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
	 * Update the index with the name.
	 */
	public void updateIndex(PersonNameAuthority personNameAuthority, File nameAuthorityIndexFolder) throws NoIndexFoundException {
		deleteFromIndex(personNameAuthority, nameAuthorityIndexFolder);
		addToIndex(personNameAuthority, nameAuthorityIndexFolder);
	}


}
