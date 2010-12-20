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

package edu.ur.ir.index;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.lucene.analysis.StandardWithISOLatin1AccentFilter;
import edu.ur.util.FileUtil;

/**
 * Test getting the text from a power point document
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultPowerPointXmlTextExtractorTest {

	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

	/**
	 * Setup for testing
	 * 
	 * this deletes exiting test directories if they exist
	 */
	@BeforeMethod
	public void cleanDirectory() {
		try {
			File f = new File(properties.getProperty("a_repo_path"));
			if (f.exists()) {
				FileUtils.forceDelete(f);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Test basic search within a plain text document
	 * 
	 * @param description
	 * @throws Exception 
	 */
	public void testIndexPowerPointDocument() throws Exception {

		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName", "file_database", "my_repository", properties
						.getProperty("a_repo_path"), "default_folder");

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_core_temp_directory");
		File directory = new File(tempDirectory);

		// helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		String baseLocation = properties.getProperty("ir_core_location");
		String powerPointXmlFile = properties.getProperty("power_point_xml_file");
		File f1 = new File(baseLocation + powerPointXmlFile);
		
		assert f1 != null : "File should not be null";
		assert f1.canRead(): "Should be able to read the file " 
			+ f1.getAbsolutePath();

		FileInfo info = repo.getFileDatabase().addFile(f1, "indexed_power_point_file");
		info.setExtension("pptx");

		FileTextExtractor documentCreator = new DefaultPowerPointXmlTextExtractor();
		assert documentCreator.canExtractText(info.getExtension()) : "Cannot create document for extension "
				+ info.getExtension();

		String text = documentCreator
				.getText(new File(info.getFullPath()));

		Document doc = new Document();
		doc.add(new Field("body", text, Field.Store.NO, Field.Index.ANALYZED));
		assert doc != null : "Document should be created";

		// create the lucene directory in memory
		Directory dir;
		try {
			dir = new RAMDirectory();
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}

		// store the document
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(dir,
					new StandardWithISOLatin1AccentFilter(), 
					true,
					IndexWriter.MaxFieldLength.LIMITED);
			writer.addDocument(doc);
			writer.optimize();
			writer.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					// do nothing
				}
			}
		}

		// search the document
		try {
			int hits = executeQuery("body", "irFile", dir);
			assert hits == 1 : "Hit count should equal 1 but equals " + hits;

			hits = executeQuery("body", "hello", dir);

			assert hits == 1 : "Hit count should equal 1 but equals " + hits;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		repoHelper.cleanUpRepository();

	}

	/**
	 * Executes the query returning the number of hits.
	 * 
	 * @param field - field to search on
	 * @param queryString - query string
	 * @param dir - lucene index to search
	 * 
	 * @return - number of hits
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 */
	private int executeQuery(String field, String queryString, Directory dir)
			throws CorruptIndexException, IOException, ParseException {
		IndexSearcher searcher = new IndexSearcher(dir, true);
		QueryParser parser = new QueryParser(Version.LUCENE_29, field, new StandardWithISOLatin1AccentFilter());
		Query q1 = parser.parse(queryString);

		TopDocs hits = searcher.search(q1, 1000);
		int hitCount = hits.totalHits;

		searcher.close();

		return hitCount;
	}

}
