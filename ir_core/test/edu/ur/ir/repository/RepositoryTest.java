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

package edu.ur.ir.repository;


import java.util.Properties;

import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;


/**
 * Test the Repository Class
 * 
 * @author Nathan Sarr
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class RepositoryTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		Repository repository  = new Repository();
		repository.setDescription("myDescription");
		repository.setName("myName");
		
		assert repository.getDescription().equals("myDescription") : "Descriptions should be equal";
		assert repository.getName().equals("myName") : "Names should be equal";
	}
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
		Repository repository1  = new Repository();
		Repository repository2  = new Repository();
		Repository repository3  = new Repository();
		
		repository1.setName("name1");
		repository2.setName("name2");
		repository3.setName("name1");
		
		assert repository1.equals(repository3): "Repositorys should be the same";
		assert !repository2.equals(repository1): "The repositorys should not be the same";
		assert repository1.hashCode() == repository3.hashCode() : "Hash codes should be the same";
		assert repository2.hashCode() != repository3.hashCode() : "Hash codes should not be the same";
	}
	
	/**
	 * Test adding/removing an irCollection from a repository
	 * @throws DuplicateNameException 
	 */
	public void testAddCollection() throws DuplicateNameException
	{
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"a_test_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
		
		InstitutionalCollection c = repo.createInstitutionalCollection("myCollection");
		
		
		assert repo.getInstitutionalCollections().contains(c) : "Repository " + repo.toString() + " should "
		 + "contain collection c " + c.toString();
		
		assert c.getPath().equals("/"): "Path should equal /" +
		 " but equals " + c.getPath();
		
		assert c.getFullPath().equals("/myCollection/"): "Path should equal /myCollection/ " + 
		 "but equals " + c.getFullPath();
		
		assert repo.removeInstitutionalCollection(c) : "Collection " + c.toString() + 
		    "should be removed";
		
		assert !repo.getInstitutionalCollections().contains(c) : "Repository should no longer "
			+ "contain collection " + c.toString();
		
		repoHelper.cleanUpRepository();
	}
	
	/**
	 * Test adding/removing an irCollection from a repository
	 * @throws DuplicateNameException 
	 */
	public void testGetCollectionByName() throws DuplicateNameException
	{
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"a_test_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
		
		InstitutionalCollection c = repo.createInstitutionalCollection("myCollection");
		
		assert repo.getInstitutionalCollection("myCollection").equals(c) : "Repository " + repo.toString() + " should "
		 + "contain collection c " + c.toString();
		
		assert repo.removeInstitutionalCollection(c) : "Collection " + c.toString() + 
		    "should be removed";
		
		assert repo.getInstitutionalCollection("myCollection") == null : "Repository should no longer "
			+ "contain collection " + c.toString();
		
		repoHelper.cleanUpRepository();
	}
	
	/**
	 * Test moving a repository from inside the tree to 
	 * a top level tree. 
	 * @throws DuplicateNameException 
	 */
	public void testMakeIrCollectionTopLevel() throws DuplicateNameException
	{
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"a_test_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
		
		InstitutionalCollection c = repo.createInstitutionalCollection("myCollection");
		InstitutionalCollection subCollection = c.createChild("subCollection");
		
		assert subCollection.getLeftValue() == 2L : "Sub collection should have left value of 2 but is " + 
		    subCollection.getLeftValue();
		
		assert subCollection.getRightValue() == 3L : "Sub collection should have right value of 3 but is " + 
		    subCollection.getRightValue();
		
		assert c.getLeftValue() == 1L : "Sub collection should have left value of  1 but is " + c.getLeftValue();
		assert c.getRightValue() == 4L : "Sub collection should have right value of 4 but is " + c.getRightValue();
		
		assert c.getChildren().contains(subCollection) : "Collection c should contain child " + subCollection;
		
		assert subCollection.getPath().equals("/myCollection/") : "Path should "
			+ "equal /myCollection/ but equals " + subCollection.getPath();

		assert subCollection.getFullPath().equals("/myCollection/subCollection/") : "Path should "
			+ "equal /myCollection/subCollection/ but equals " + subCollection.getFullPath();
		
		
		repo.addInstitutionalCollection(subCollection);

		assert subCollection.getLeftValue() == 1L : "Sub collection should now have a left value of 1 but is " +
		    subCollection.getLeftValue();
		
		assert subCollection.getRightValue() == 2L : "Sub collection should now have a right value of 2 but is " +
		    subCollection.getRightValue();
		
		assert subCollection.getPath().equals("/") : "Path should "
			+ "equal / but equals " + subCollection.getPath();

		assert subCollection.getFullPath().equals("/subCollection/") : "Path should "
			+ "equal /subCollection/ but equals " + subCollection.getFullPath();

		assert !c.getChildren().contains(subCollection) : "Collection c should NOT contain child " + subCollection;
		assert c.getParent() == null : "The parent should be null but is " + c.getParent();
		
		assert c.getTreeRoot().equals(c) : "The root should be equal to itself but equals " + c.getTreeRoot(); 
		
		repoHelper.cleanUpRepository();
	}
}
