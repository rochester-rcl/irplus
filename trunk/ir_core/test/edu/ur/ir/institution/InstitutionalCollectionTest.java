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

package edu.ur.ir.institution;

import java.util.List;
import java.util.Properties;

import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.repository.Repository;

/**
 * Test the Institutional Collection class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class InstitutionalCollectionTest {
	
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
		InstitutionalCollection irCollection = new InstitutionalCollection();
		
		irCollection.setDescription("myDescription");
		irCollection.setName("myName");
		
		assert irCollection.getDescription().equals("myDescription") : "Descriptions should be equal";
		assert irCollection.getName().equals("myName") : "Names should be equal";
		
		assert irCollection.allowsChildren() : "Default is to allow children";
		assert irCollection.getAllowsItems() : "Default is to allow items";
	}

	/**
	 * Test adding children.
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testAddChildren() throws DuplicateNameException, LocationAlreadyExistsException
	{
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"repo", 
				properties.getProperty("a_repo_path"),
				"default_InstitutionalCollection");
		
		// create the root colleciton
		InstitutionalCollection irCollection1 = repo.createInstitutionalCollection("collection1");
		
		assert irCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + irCollection1.getLeftValue();
		assert irCollection1.getRightValue() == 2 : "Right value should equal 2 but equals " + irCollection1.getRightValue();
		
		assert irCollection1.getRepository().equals(repo);
		
		assert irCollection1.getPath().equals("/") : "Path should "
			+ "equal / but equals " + irCollection1.getPath();

		assert irCollection1.getFullPath().equals("/collection1/") : "Path should "
			+ "equal /colleciton1/ but equals " + irCollection1.getFullPath();
		
		// add first child
		InstitutionalCollection irCollection2 = irCollection1.createChild("collection2");
		
		assert irCollection2.getFullPath().equals("/collection1/collection2/") : "Path should "
			+ "equal /collection1/collection2/ but equals " + irCollection2.getFullPath();

		assert irCollection2.getPath().equals("/collection1/") : "Path should "
			+ "equal /collection1/ but equals " + irCollection2.getPath();

		
		assert irCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + irCollection1.getLeftValue();
		assert irCollection1.getRightValue() == 4 : "Right value should equal 4 but equals " + irCollection1.getRightValue();
		
		assert irCollection2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ irCollection2.getLeftValue();
		
		assert irCollection2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ irCollection2.getRightValue();
		
		// add second child child
		InstitutionalCollection irCollection3 = irCollection1.createChild("collection3");

		assert irCollection3.getPath().equals("/collection1/") : "Path should "
			+ "equal /collection1/ but equals " + irCollection3.getPath();

		assert irCollection3.getFullPath().equals("/collection1/collection3/") : "Path should "
			+ "equal /collection1/collection3/ but equals " + irCollection3.getFullPath();

		
		assert irCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + irCollection1.getLeftValue();
		assert irCollection1.getRightValue() == 6 : "Right value should equal 6 but equals " + irCollection1.getRightValue();
		
		
		assert irCollection2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ irCollection2.getLeftValue();
		
		assert irCollection2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ irCollection2.getRightValue();
		
		assert irCollection3.getLeftValue() == 4 : "Collection 4 left value should equal 4 but is " 
			+ irCollection3.getLeftValue();
		
		assert irCollection3.getRightValue() == 5 : "Collection 3 right value should equal 5 but is " 
			+ irCollection3.getRightValue();

		
		// add sub collection
		InstitutionalCollection irSubCollection1 = irCollection2.createChild("irSubCollection1");
		
		assert irSubCollection1.getPath().equals("/collection1/collection2/") : "Path should "
			+ "equal /collection1/collection2/ but equals " + irSubCollection1.getPath();

		assert irSubCollection1.getFullPath().equals("/collection1/collection2/irSubCollection1/") : "Path should "
			+ "equal /collection1/collection2/irSubCollection1/ but equals " + irSubCollection1.getFullPath();

		
		assert irCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + irCollection1.getLeftValue();
		assert irCollection1.getRightValue() == 8 : "Right value should equal 8 but equals " + irCollection1.getRightValue();
		assert irCollection1.getRepository().equals(repo);
		
		assert irCollection2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ irCollection2.getLeftValue();
		assert irCollection2.getRepository().equals(repo);
		
		assert irCollection2.getRightValue() == 5 : "Collection 2 right value should equal 5 but is " 
			+ irCollection2.getRightValue();
		
		assert irSubCollection1.getLeftValue() == 3 : "sub Collection 1 left value should equal 3 but is " 
			+ irSubCollection1.getLeftValue();
		
		assert irSubCollection1.getRightValue() == 4 : "Sub collection 1 right value should equal 4 but is " 
			+ irSubCollection1.getRightValue();
		assert irSubCollection1.getRepository().equals(repo);
		
		
		assert irCollection3.getLeftValue() == 6 : "Collection 3 left value should equal 6 but is " 
			+ irCollection3.getLeftValue();
		
		assert irCollection3.getRightValue() == 7 : "Collection 3 right value should equal 7 but is " 
			+ irCollection3.getRightValue();
		assert irCollection3.getRepository().equals(repo);

		// add sub collection
		InstitutionalCollection irSubCollection2 = irCollection3.createChild("irSubCollection2");

		assert irSubCollection2.getPath().equals("/collection1/collection3/") : "Path should "
			+ "equal /collection1/collection3/ but equals " + irSubCollection1.getPath();

		assert irSubCollection2.getFullPath().equals("/collection1/collection3/irSubCollection2/") : "Path should "
			+ "equal /collection1/collection3/irSubCollection2/ but equals " + irSubCollection1.getFullPath();

		
		assert irCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + irCollection1.getLeftValue();
		assert irCollection1.getRightValue() == 10 : "Right value should equal 10 but equals " + irCollection1.getRightValue();
		
		assert irCollection2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ irCollection2.getLeftValue();
		
		assert irCollection2.getRightValue() == 5 : "Collection 2 right value should equal 5 but is " 
			+ irCollection2.getRightValue();
		
		assert irSubCollection1.getLeftValue() == 3 : "sub Collection 1 left value should equal 3 but is " 
			+ irSubCollection1.getLeftValue();
		
		assert irSubCollection1.getRightValue() == 4 : "Sub collection 1 right value should equal 4 but is " 
			+ irSubCollection1.getRightValue();
		
		assert irCollection3.getLeftValue() == 6 : "Collection 3 left value should equal 6 but is " 
			+ irCollection3.getLeftValue();
		
		assert irCollection3.getRightValue() == 9 : "Collection 3 right value should equal 7 but is " 
			+ irCollection3.getRightValue();

		assert irSubCollection2.getLeftValue() == 7 : "sub Collection 2 left value should equal 7 but is " 
			+ irSubCollection2.getLeftValue();
		
		assert irSubCollection2.getRightValue() == 8 : "Sub collection 2 right value should equal 8 but is " 
			+ irSubCollection2.getRightValue();
		
		repoHelper.cleanUpRepository();
		
	}
	
	/**
	 * Test removing a collection. 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testRemoveCollections() throws DuplicateNameException, LocationAlreadyExistsException
	{
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"repo", 
				properties.getProperty("a_repo_path"),
				"default_InstitutionalCollection");

		
		// create the root colleciton
		InstitutionalCollection irCollection1 = repo.createInstitutionalCollection("collection1");
		
		// add first child
		InstitutionalCollection irCollection2 = irCollection1.createChild("collection2");
				
		// add second child child
		InstitutionalCollection irCollection3 = irCollection1.createChild("collection3");
			
		// add sub collection
		InstitutionalCollection irSubCollection1 = irCollection2.createChild("irSubCollection1");
		
		// add sub collection
		InstitutionalCollection irSubCollection2 = irCollection3.createChild("irSubCollection2");
		
		// remove the irSubColleciton1
		assert irCollection2.removeChild(irSubCollection1) : "irSubCollection1 should be removed";
		
		assert irSubCollection1.getParent() == null : "Parent Should null but is " + 
		    irSubCollection1.getParent().toString();
		assert irSubCollection1.isRoot() : "Should be root";

		// check re-numbering
		assert irCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + irCollection1.getLeftValue();
		assert irCollection1.getRightValue() == 8 : "Right value should equal 8 but equals " + irCollection1.getRightValue();
		
		assert irCollection2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ irCollection2.getLeftValue();
		
		assert irCollection2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ irCollection2.getRightValue();
		
		assert irCollection3.getLeftValue() == 4 : "Collection 3 left value should equal 4 but is " 
			+ irCollection3.getLeftValue();
		
		assert irCollection3.getRightValue() == 7 : "Collection 3 right value should equal 7 but is " 
			+ irCollection3.getRightValue();

		assert irSubCollection2.getLeftValue() == 5 : "sub Collection 2 left value should equal 5 but is " 
			+ irSubCollection2.getLeftValue();
		
		assert irSubCollection2.getRightValue() == 6 : "Sub collection 2 right value should equal 6 but is " 
			+ irSubCollection2.getRightValue();
		
		// remove collection 3
		assert irCollection1.removeChild(irCollection3) : "Collection 3 should be removed"; 
		assert irCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + irCollection1.getLeftValue();
		assert irCollection1.getRightValue() == 4 : "Right value should equal 4 but equals " + irCollection1.getRightValue();
		
		assert irCollection2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ irCollection2.getLeftValue();
		
		assert irCollection2.getRightValue() == 3 : "Collection 2 right value should equal 5 but is " 
			+ irCollection2.getRightValue();
		
		repoHelper.cleanUpRepository();
		
	}
	
	/**
	 * Test allows items setting
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testAllowsItems() throws DuplicateNameException, LocationAlreadyExistsException
	{
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"repo", 
				properties.getProperty("a_repo_path"),
				"default_InstitutionalCollection");

		
		InstitutionalCollection institutionalCollection = repo.createInstitutionalCollection("collection1");
		institutionalCollection.setAllowsItems(false);
		assert !institutionalCollection.getAllowsItems() : "Should not allow items";

		try
		{
			
			GenericItem i = new GenericItem("item1");
			institutionalCollection.createInstitutionalItem(i);
			assert false : "Should fail before this";
		}
		catch(IllegalStateException e)
		{
		    assert true;
		}
		
		repoHelper.cleanUpRepository();
		
	}
	
	/**
	 * Test creating items for a collection. 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testAddingItems() throws DuplicateNameException, LocationAlreadyExistsException
	{
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"repo", 
				properties.getProperty("a_repo_path"),
				"default_InstitutionalCollection");
		
		// create the root colleciton
		InstitutionalCollection institutionalCollection1 = repo.createInstitutionalCollection("collection1");
		
		// add first child
		InstitutionalCollection institutionalCollection2 = institutionalCollection1.createChild("collection2");
				
		// add sub collection
		InstitutionalCollection institutionalSubCollection1 = institutionalCollection2.createChild("irSubCollection1");
		

		GenericItem genericItem = new GenericItem("item1");
		InstitutionalItem item1 = institutionalCollection1.createInstitutionalItem(genericItem);
		
		assert item1.getPath().equals("/collection1/") : "path should equal "
			+ "/collection1/ but equals " + item1.getPath();
		
		assert item1.getFullPath().equals("/collection1/item1") : "path should equal " + 
				"/collection1/item1 but equals " + item1.getFullPath();
		
		
		GenericItem subItem = new GenericItem("subItem1");

		InstitutionalItem subItem1 = institutionalSubCollection1.createInstitutionalItem(subItem);
		

		assert subItem1.getPath().equals("/collection1/collection2/irSubCollection1/") : "path should equal "
			+ "/collection1/collection2/irSubCollection1/ but equals " + subItem1.getPath();
		
		assert subItem1.getFullPath().equals("/collection1/collection2/irSubCollection1/subItem1") : "path should equal " + 
				"/collection1/collection2/irSubCollection1/subItem1 " + subItem1.getFullPath();
		
		institutionalCollection1.addItem(subItem1);
		
		assert !institutionalSubCollection1.getItems().contains(subItem1): "Should not contain a child sub item";
		assert institutionalCollection1.getItems().contains(subItem1) : "Ir collection should now contain subItem1";
		assert subItem1.getInstitutionalCollection().equals(institutionalCollection1): "Sub item parent should be irCollection1";
		
		repoHelper.cleanUpRepository();
	}
	
	/**
	 * Test getting an item by name. 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testGetItemsByName() throws DuplicateNameException, LocationAlreadyExistsException
	{
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"repo", 
				properties.getProperty("a_repo_path"),
				"default_InstitutionalCollection");
		
		
		// create the root collection
		InstitutionalCollection institutionalCollection1 = repo.createInstitutionalCollection("collection1");
		
		// add first child
		InstitutionalCollection institutionalCollection2 = institutionalCollection1.createChild("collection2");
				
		// add sub collection
		InstitutionalCollection institutionalSubCollection1 = institutionalCollection2.createChild("irSubCollection1");
		
		GenericItem genericItem = new GenericItem("item1");
		InstitutionalItem item1 = institutionalCollection1.createInstitutionalItem(genericItem);
		
		GenericItem subItem = new GenericItem("subItem1");
		
		InstitutionalItem subItem1 = institutionalSubCollection1.createInstitutionalItem(subItem);
		
		assert institutionalCollection1.getItems("item1").contains(item1) : "Collection1 should contain " + 
		item1.toString();
		
		assert institutionalSubCollection1.getItems("subItem1").contains(subItem1): 
			"Should contain item 1";
		
		repoHelper.cleanUpRepository();
	}
	
	/**
	 * Test moving an institutional collection.
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testMoveInsitutionalCollectionWithinTree() throws DuplicateNameException, LocationAlreadyExistsException
	{
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"repo", 
				properties.getProperty("a_repo_path"),
				"default_InstitutionalCollection");
		
		
		// create the root collection
		InstitutionalCollection institutionalCollection1 = repo.createInstitutionalCollection("collection1");
		
		InstitutionalCollection institutionalCollection2 = null;
		InstitutionalCollection instiutionalCollection3 = null;
		InstitutionalCollection subInstitutionalCollection1 = null;
		InstitutionalCollection subInstitutionalCollection2 = null;
		try
		{

		    // add first child
		    institutionalCollection2 = institutionalCollection1.createChild("institutionalCollection2");
				
		    // add second child child
		    instiutionalCollection3 = institutionalCollection1.createChild("instiutionalCollection3");
			
		    // add sub InstitutionalCollection
		    subInstitutionalCollection1 = institutionalCollection2.createChild("subInstitutionalCollection1");
		
		    // add sub InstitutionalCollection
		    subInstitutionalCollection2 = instiutionalCollection3.createChild("subInstitutionalCollection2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert institutionalCollection1.getLeftValue() == 1 : "Right value should be 1 but is " +
		institutionalCollection1.getLeftValue();
		assert institutionalCollection1.getRightValue() == 10 : "Left value should be 10 but is " +
		institutionalCollection1.getRightValue();
		
		assert institutionalCollection2.getLeftValue() == 2 : "Right value should be 2 but is " +
		institutionalCollection1.getLeftValue();
		assert institutionalCollection2.getRightValue() == 5 : "Right value sould be  5 but is " +
		institutionalCollection2.getRightValue();
		
		assert instiutionalCollection3.getLeftValue() == 6 : "Right value should be 6 but is " +
		institutionalCollection1.getLeftValue();
		assert instiutionalCollection3.getRightValue() == 9 : "Right value sould be 9 but is " +
		instiutionalCollection3.getRightValue();
		
		assert subInstitutionalCollection1.getLeftValue() == 3 : "Right value should be 3 but is " +
		subInstitutionalCollection1.getLeftValue();
		assert subInstitutionalCollection1.getRightValue() == 4 : "Right value sould be 4 but is " +
		subInstitutionalCollection1.getRightValue();
		
		assert subInstitutionalCollection2.getLeftValue() == 7 : "Right value should be 7 but is " +
		subInstitutionalCollection2.getLeftValue();
		assert subInstitutionalCollection2.getRightValue() == 8 : "Right value sould be 8 but is " +
		subInstitutionalCollection2.getRightValue();
		
		
		// move subInstitutionalCollection2
		try
		{
		    institutionalCollection1.addChild(subInstitutionalCollection2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert subInstitutionalCollection2.getParent().equals(institutionalCollection1) :
			"Parent should be personal InstitutionalCollection 1 but is " + subInstitutionalCollection2.getParent();
		
		
		assert institutionalCollection1.getLeftValue() == 1 : "Right value should be 1 but is " +
		institutionalCollection1.getLeftValue();
		assert institutionalCollection1.getRightValue() == 10 : "Left value should be 10 but is " +
		institutionalCollection1.getRightValue();
		
		assert institutionalCollection2.getLeftValue() == 2 : "Right value should be 2 but is " +
		institutionalCollection1.getLeftValue();
		assert institutionalCollection2.getRightValue() == 5 : "Right value sould be  5 but is " +
		institutionalCollection2.getRightValue();
		
		assert instiutionalCollection3.getLeftValue() == 6 : "Right value should be 6 but is " +
		institutionalCollection1.getLeftValue();
		assert instiutionalCollection3.getRightValue() == 7 : "Right value sould be 7 but is " +
		instiutionalCollection3.getRightValue();
		
		assert subInstitutionalCollection1.getLeftValue() == 3 : "Right value should be 6 but is " +
		subInstitutionalCollection1.getLeftValue();
		assert subInstitutionalCollection1.getRightValue() == 4 : "Right value sould be 7 but is " +
		subInstitutionalCollection1.getRightValue();
		
		assert subInstitutionalCollection2.getLeftValue() == 8 : "Right value should be 8 but is " +
		subInstitutionalCollection2.getLeftValue();
		assert subInstitutionalCollection2.getRightValue() == 9 : "Right value sould be 9 but is " +
		subInstitutionalCollection2.getRightValue();
		
		try
		{
		    subInstitutionalCollection2.addChild(institutionalCollection2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert institutionalCollection2.getParent().equals(subInstitutionalCollection2);
		
		assert institutionalCollection1.getLeftValue() == 1 : "Right value should be 1 but is " +
		institutionalCollection1.getLeftValue();
		assert institutionalCollection1.getRightValue() == 10 : "Left value should be 10 but is " +
		institutionalCollection1.getRightValue();
		
		assert institutionalCollection2.getLeftValue() == 5 : "Right value should be 2 but is " +
		institutionalCollection1.getLeftValue();
		assert institutionalCollection2.getRightValue() == 8 : "Right value sould be  5 but is " +
		institutionalCollection2.getRightValue();
		
		assert instiutionalCollection3.getLeftValue() == 2 : "Right value should be 6 but is " +
		institutionalCollection1.getLeftValue();
		assert instiutionalCollection3.getRightValue() == 3 : "Right value sould be 7 but is " +
		instiutionalCollection3.getRightValue();
		
		assert subInstitutionalCollection1.getLeftValue() == 6 : "Right value should be 6 but is " +
		subInstitutionalCollection1.getLeftValue();
		assert subInstitutionalCollection1.getRightValue() == 7 : "Right value sould be 7 but is " +
		subInstitutionalCollection1.getRightValue();
		
		assert subInstitutionalCollection2.getLeftValue() == 4 : "Right value should be 8 but is " +
		subInstitutionalCollection2.getLeftValue();
		assert subInstitutionalCollection2.getRightValue() == 9 : "Right value sould be 9 but is " +
		subInstitutionalCollection2.getRightValue();
		
		repoHelper.cleanUpRepository();
	}
	
	/**
	 * Test moving a personal Collection to the root of the person.
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testMoveInstitutionalCollectionToRoot() throws DuplicateNameException, LocationAlreadyExistsException
	{
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"repo", 
				properties.getProperty("a_repo_path"),
				"default_InstitutionalCollection");
		
		// create the root collection
		InstitutionalCollection institutionalCollection1 = repo.createInstitutionalCollection("institutionalCollection1");
		
		InstitutionalCollection institutionalCollection2 = null;
		InstitutionalCollection institutionalCollection3 = null;
		InstitutionalCollection personalSubCollection1 = null;
		InstitutionalCollection personalSubCollection2 = null;
		
		try
		{
		    // add first child
		    institutionalCollection2 = institutionalCollection1.createChild("institutionalCollection2");
				
		    // add second child child
		    institutionalCollection3 = institutionalCollection1.createChild("institutionalCollection3");
			
		    // add sub Collection
		    personalSubCollection1 = institutionalCollection2.createChild("personalSubCollection1");
		
		    // add sub Collection
		    personalSubCollection2 = institutionalCollection3.createChild("personalSubCollection2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert institutionalCollection1.getLeftValue() == 1 : "Right value should be 1 but is " +
		institutionalCollection1.getLeftValue();
		assert institutionalCollection1.getRightValue() == 10 : "Left value should be 10 but is " +
		institutionalCollection1.getRightValue();
		
		assert institutionalCollection2.getLeftValue() == 2 : "Right value should be 2 but is " +
		institutionalCollection1.getLeftValue();
		assert institutionalCollection2.getRightValue() == 5 : "Right value sould be  5 but is " +
		institutionalCollection2.getRightValue();
		
		assert institutionalCollection3.getLeftValue() == 6 : "Right value should be 6 but is " +
		institutionalCollection1.getLeftValue();
		assert institutionalCollection3.getRightValue() == 9 : "Right value sould be 9 but is " +
		institutionalCollection3.getRightValue();
		
		assert personalSubCollection1.getLeftValue() == 3 : "Right value should be 3 but is " +
		personalSubCollection1.getLeftValue();
		assert personalSubCollection1.getRightValue() == 4 : "Right value sould be 4 but is " +
		personalSubCollection1.getRightValue();
		
		assert personalSubCollection2.getLeftValue() == 7 : "Right value should be 7 but is " +
		personalSubCollection2.getLeftValue();
		assert personalSubCollection2.getRightValue() == 8 : "Right value sould be 8 but is " +
		personalSubCollection2.getRightValue();
		
		//make personal Collection 2 a root Collection by adding them to the user
		repo.addInstitutionalCollection(institutionalCollection2);
		
		assert institutionalCollection2.getLeftValue() == 1 : "Right value should be 4 but is " 
			+ institutionalCollection2.getLeftValue();
		
		assert institutionalCollection2.getRightValue() == 4 : "Right value should be 4 but is " 
			+ institutionalCollection2.getRightValue();
		
		assert institutionalCollection2.getTreeRoot().equals(institutionalCollection2) : "personal Collection 2 should be root but root = " + 
		institutionalCollection2.getTreeRoot();

		repoHelper.cleanUpRepository();

	}
	
	/**
	 * Test adding links and make sure order is correct
	 * @throws DuplicateNameException 
	 */
	public void testLinkOrder() throws DuplicateNameException
	{
		InstitutionalCollection collection = new InstitutionalCollection();
		collection.setName("testLinksCollection");
		InstitutionalCollectionLink link0 = collection.addLink("link0", "http://theservierside.com");
		InstitutionalCollectionLink link1 = collection.addLink("link1", "http://www.hotmail.com");
		InstitutionalCollectionLink link2 = collection.addLink("link2", "http://www.hotmail.com");
		InstitutionalCollectionLink link3 = collection.addLink("link3", "http://www.hotmail.com");
		
		List<InstitutionalCollectionLink> links = collection.getLinks();
		
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 0 : "link 0 order should be 0 but is " + link0.getOrder();
		
		assert links.contains(link1) : "links should contain " + link1;
		assert link1.getOrder() == 1 : "link 1 order should be 1 but is " + link1.getOrder();
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 2 : "link 2 order should be 2 but is " + link2.getOrder();

		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 3 : "link 3 order should be 3 but is " + link3.getOrder();
		
		
		
		collection.moveLink(link3, 8);
		
		links = collection.getLinks();
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 0 : "link 0 order should be 0 but is " + link0.getOrder();
		
		assert links.contains(link1) : "links should contain " + link1;
		assert link1.getOrder() == 1 : "link 1 order should be 1 but is " + link1.getOrder();
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 2 : "link 2 order should be 2 but is " + link2.getOrder();

		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 3 : "link 3 order should be 3 but is " + link3.getOrder();
		
		
		// move link1 to the end
		collection.moveLink(link1, 3);
		
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 0 : "link 0 order should be 0 but is " + link0.getOrder();
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 1 : "link 2 order should be 1 but is " + link2.getOrder();

		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 2 : "link 3 order should be 2 but is " + link3.getOrder();
		
		assert links.contains(link1) : "links should contain " + link1;
		assert link1.getOrder() == 3 : "link 2 order should be 3 but is " + link1.getOrder();
		
		// move link2 to the beginning
		collection.moveLink(link2, 0);
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 0 : "link 2 order should be 1 but is " + link2.getOrder();
		
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 1 : "link 0 order should be 0 but is " + link0.getOrder();

		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 2 : "link 3 order should be 2 but is " + link3.getOrder();
		
		assert links.contains(link1) : "links should contain " + link1;
		assert link1.getOrder() == 3 : "link 1 order should be 3 but is " + link1.getOrder();
		
		// move link1 up one
		collection.moveLink(link1, 2);
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 0 : "link 2 order should be 1 but is " + link2.getOrder();
		
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 1 : "link 0 order should be 0 but is " + link0.getOrder();

		assert links.contains(link1) : "links should contain " + link1;
		assert link1.getOrder() == 2 : "link 1 order should be 2 but is " + link1.getOrder();
		
		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 3 : "link 3 order should be 3 but is " + link3.getOrder();
		
		
		
		
		// remove link 1
		collection.removLink(link3);
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 0 : "link 2 order should be 1 but is " + link2.getOrder();
		
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 1 : "link 0 order should be 0 but is " + link0.getOrder();

		assert links.contains(link1) : "links should contain " + link1;
		assert link1.getOrder() == 2 : "link 1 order should be 2 but is " + link1.getOrder();
		
		
		assert !collection.getLinks().contains(link3) : "links should not contain link 1";
	}
	
	/**
	 * Test subscription methods
	 */
	public void testSubscriptions()
	{
		// create the user for the subscription
		IrUser user = new IrUser("nate", "password");
		
		InstitutionalCollection collection = new InstitutionalCollection();
		collection.setName("testLinksCollection");
		
		assert collection.getSubscriptions().size() == 0 : "Should have 0 subscriptions but has " + collection.getSubscriptions().size();
		
		InstitutionalCollectionSubscription subscription = collection.addSuscriber(user);
		
		assert collection.getSubscriptions().size() == 1 : "Should have 1 subscriptions but has " + collection.getSubscriptions().size();

		assert subscription.getUser().equals(user) : "Collection subscription user should = " + user 
		+ " but = " + subscription.getUser();
		
		
		assert subscription.getInstitutionalCollection().equals(collection) : "Collection subscription collection should = " + collection 
		+ " but = " + subscription.getInstitutionalCollection();
		
		assert collection.hasSubscriber(user) : " Collection should have user " + user;
		
		collection.removeSubscriber(user);
		
		assert !collection.hasSubscriber(user) : " Collection should NOT have user " + user;
		
	}

	
}
