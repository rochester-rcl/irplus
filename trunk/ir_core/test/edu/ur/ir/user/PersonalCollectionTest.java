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

package edu.ur.ir.user;


import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.VersionedItem;

/**
 * Test the PersonalCollection class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PersonalCollectionTest {
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testBasicSets()  throws DuplicateNameException
	{
		PersonalCollection personalCollection = new PersonalCollection();
		
		personalCollection.setDescription("myDescription");
		personalCollection.setName("myName");
		
		assert personalCollection.getDescription().equals("myDescription") : "Descriptions should be equal";
		assert personalCollection.getName().equals("myName") : "Names should be equal";
		
		assert personalCollection.allowsChildren() : "Default is to allow children";
	}
	
	/**
	 * Test adding children.
	 */
	public void testAddChildren() throws DuplicateNameException
	{
		IrUser u = new IrUser("nate", "password");
		
		// create the root collection
		PersonalCollection personalCollection1 = new PersonalCollection(u, "nates_personal_collection");
		
		assert personalCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalCollection1.getLeftValue();
		assert personalCollection1.getRightValue() == 2 : "Right value should equal 2 but equals " + personalCollection1.getRightValue();
		
		assert personalCollection1.getOwner().equals(u) : "users should be the same";
		
		assert personalCollection1.getPath().equals("/") : "Path should "
			+ "equal/ but equals " + personalCollection1.getPath();

		
		assert personalCollection1.getFullPath().equals("/nates_personal_collection/") : "Path should "
			+ "equal /nates_personal_collection/ but equals " + personalCollection1.getFullPath();
		
		// add first child
		PersonalCollection personalCollection2 = personalCollection1.createChild("personalCollection2");
		
		assert personalCollection2.getFullPath().equals("/nates_personal_collection/personalCollection2/") : "Path should "
			+ "equal /nates_personal_collection/personalCollection2/ but equals " + personalCollection2.getFullPath();

		assert personalCollection2.getPath().equals("/nates_personal_collection/") : "Path should "
			+ "equal /nates_personal_collection/ but equals " + personalCollection2.getPath();

		
		assert personalCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalCollection1.getLeftValue();
		assert personalCollection1.getRightValue() == 4 : "Right value should equal 4 but equals " + personalCollection1.getRightValue();
		
		assert personalCollection2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ personalCollection2.getLeftValue();
		
		assert personalCollection2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ personalCollection2.getRightValue();
		
		
		// add second child child
		PersonalCollection personalCollection3 = personalCollection1.createChild("personalCollection3");

		assert personalCollection3.getPath().equals("/nates_personal_collection/") : "Path should "
			+ "equal /nates_personal_collection/ but equals " + personalCollection3.getPath();

		assert personalCollection3.getFullPath().equals("/nates_personal_collection/personalCollection3/") : "Path should "
			+ "equal /nates_personal_collection/personalCollection3/ but equals " + personalCollection3.getFullPath();

		
		assert personalCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalCollection1.getLeftValue();
		assert personalCollection1.getRightValue() == 6 : "Right value should equal 6 but equals " + personalCollection1.getRightValue();
		
		
		assert personalCollection2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ personalCollection2.getLeftValue();
		
		assert personalCollection2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ personalCollection2.getRightValue();
		
		assert personalCollection3.getLeftValue() == 4 : "Collection 4 left value should equal 4 but is " 
			+ personalCollection3.getLeftValue();
		
		assert personalCollection3.getRightValue() == 5 : "Collection 3 right value should equal 5 but is " 
			+ personalCollection3.getRightValue();

		
		// add sub collection
		PersonalCollection personalSubCollection1 = personalCollection2.createChild("personalSubCollection1");
		
		assert personalSubCollection1.getPath().equals("/nates_personal_collection/personalCollection2/") : "Path should "
			+ "equal /nates_personal_collection/personalCollection2/ but equals " + personalSubCollection1.getPath();

		assert personalSubCollection1.getFullPath().equals("/nates_personal_collection/personalCollection2/personalSubCollection1/") : "Path should "
			+ "equal /nates_personal_collection/personalCollection2/personalSubCollection1/ but equals " + personalSubCollection1.getFullPath();

		assert personalCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalCollection1.getLeftValue();
		assert personalCollection1.getRightValue() == 8 : "Right value should equal 8 but equals " + personalCollection1.getRightValue();
		assert personalCollection1.getOwner().equals(u);

		assert personalCollection2.getLeftValue() == 2 : "Personal Folder 2 left value should equal 2 but is " 
			+ personalCollection2.getLeftValue();
		assert personalCollection2.getOwner().equals(u);

		assert personalCollection2.getRightValue() == 5 : "Personal Folder 2 right value should equal 5 but is " 
			+ personalCollection2.getRightValue();
		
		assert personalSubCollection1.getLeftValue() == 3 : "sub Personal Folder 1 left value should equal 3 but is " 
			+ personalSubCollection1.getLeftValue();
		
		assert personalSubCollection1.getRightValue() == 4 : "Sub Personal Folder 1 right value should equal 4 but is " 
			+ personalSubCollection1.getRightValue();
		assert personalSubCollection1.getOwner().equals(u);
		
		
		assert personalCollection3.getLeftValue() == 6 : "Collection 3 left value should equal 6 but is " 
			+ personalCollection3.getLeftValue();
		
		assert personalCollection3.getRightValue() == 7 : "Collection 3 right value should equal 7 but is " 
			+ personalCollection3.getRightValue();
		assert personalCollection3.getOwner().equals(u);


		// add sub collection
		PersonalCollection personalSubCollection2 = personalCollection3.createChild("personalSubCollection2");

		assert personalSubCollection2.getPath().equals("/nates_personal_collection/personalCollection3/") : "Path should "
			+ "equal /nates_personal_collection/personalCollection3/ but equals " + personalSubCollection2.getPath();

		assert personalSubCollection2.getFullPath().equals("/nates_personal_collection/personalCollection3/personalSubCollection2/") : "Path should "
			+ "equal /nates_personal_collection/personalCollection3/personalSubCollection2/ but equals " + personalSubCollection1.getFullPath();

		
		assert personalCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalCollection1.getLeftValue();
		assert personalCollection1.getRightValue() == 10 : "Right value should equal 10 but equals " + personalCollection1.getRightValue();
		
		assert personalCollection2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ personalCollection2.getLeftValue();
		
		assert personalCollection2.getRightValue() == 5 : "Collection 2 right value should equal 5 but is " 
			+ personalCollection2.getRightValue();
		
		assert personalSubCollection1.getLeftValue() == 3 : "sub Collection 1 left value should equal 3 but is " 
			+ personalSubCollection1.getLeftValue();
		
		assert personalSubCollection1.getRightValue() == 4 : "Sub collection 1 right value should equal 4 but is " 
			+ personalSubCollection1.getRightValue();
		
		assert personalCollection3.getLeftValue() == 6 : "Collection 3 left value should equal 6 but is " 
			+ personalCollection3.getLeftValue();
		
		assert personalCollection3.getRightValue() == 9 : "Collection 3 right value should equal 7 but is " 
			+ personalCollection3.getRightValue();

		assert personalSubCollection2.getLeftValue() == 7 : "sub Collection 2 left value should equal 7 but is " 
			+ personalSubCollection2.getLeftValue();
		
		assert personalSubCollection2.getRightValue() == 8 : "Sub collection 2 right value should equal 8 but is " 
			+ personalSubCollection2.getRightValue();
	}

	/**
	 * Test removing a personal collection. 
	 */
	public void testRemovePersonalCollection() throws DuplicateNameException
	{
		// create the owner of the collection
		IrUser u = new IrUser("nate", "password");
		
		// create the root colleciton
		PersonalCollection personalCollection1 = new PersonalCollection(u, "personalCollection1");
		
		// add first child
		PersonalCollection personalCollection2 = personalCollection1.createChild("personalCollection2");
				
		// add second child child
		PersonalCollection personalCollection3 = personalCollection1.createChild("personalCollection3");
			
		// add sub collection
		PersonalCollection personalSubCollection1 = personalCollection2.createChild("personalSubCollection1");
		
		// add sub collection
		PersonalCollection personalSubCollection2 = personalCollection3.createChild("personalSubCollection2");
		
		// remove the irSubColleciton1
		assert personalCollection2.removeChild(personalSubCollection1) : "personalSubCollection1 should be removed";
		
		assert personalSubCollection1.getParent() == null : "Parent Should null but is " + 
		    personalSubCollection1.getParent().toString();
		assert personalSubCollection1.isRoot() : "Should be root";

		// check re-numbering
		assert personalCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalCollection1.getLeftValue();
		assert personalCollection1.getRightValue() == 8 : "Right value should equal 8 but equals " + personalCollection1.getRightValue();
		
		assert personalCollection2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ personalCollection2.getLeftValue();
		
		assert personalCollection2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ personalCollection2.getRightValue();
		
		assert personalCollection3.getLeftValue() == 4 : "Collection 3 left value should equal 4 but is " 
			+ personalCollection3.getLeftValue();
		
		assert personalCollection3.getRightValue() == 7 : "Collection 3 right value should equal 7 but is " 
			+ personalCollection3.getRightValue();

		assert personalSubCollection2.getLeftValue() == 5 : "sub Collection 2 left value should equal 5 but is " 
			+ personalSubCollection2.getLeftValue();
		
		assert personalSubCollection2.getRightValue() == 6 : "Sub collection 2 right value should equal 6 but is " 
			+ personalSubCollection2.getRightValue();
		
		// remove collection 3
		assert personalCollection1.removeChild(personalCollection3) : "Collection 3 should be removed"; 
		assert personalCollection1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalCollection1.getLeftValue();
		assert personalCollection1.getRightValue() == 4 : "Right value should equal 4 but equals " + personalCollection1.getRightValue();
		
		assert personalCollection2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ personalCollection2.getLeftValue();
		
		assert personalCollection2.getRightValue() == 3 : "Collection 2 right value should equal 5 but is " 
			+ personalCollection2.getRightValue();
		
	}
	
	/**
	 * Test adding versioned items to a personal collection.
	 */
	public void testAddVersionedItem()
	{
		GenericItem genericItem = new GenericItem("myItem");
		// create the owner of the folders
		IrUser u = new IrUser("nate", "password");
		VersionedItem versionedItem = new VersionedItem(u, genericItem);
		
		// create the root collection
		PersonalCollection personalCollection1 = new PersonalCollection(u, "personalCollection1");
		PersonalItem personalItem = new PersonalItem(u, versionedItem);
		personalItem.setId(44l);
		personalCollection1.addItem(personalItem);
		
		
		assert personalCollection1.getPersonalItems().contains(personalItem) : "Personal collection "
			+ " should have personal collection " + personalItem;
		
		assert personalCollection1.getPersonalItem(44l).equals(personalItem) : "Should be able to "
			+ " find personal item " + personalItem;
		
		assert personalCollection1.getPersonalItem(versionedItem.getCurrentVersion().getItem().getName()).equals(personalItem) : "Should be able to "
			+ " find personal item " + personalItem;
	
	}
	
	/**
	 * Test moving a personal folder to the root of the person.
	 * @throws DuplicateNameException 
	 */
	public void testMovePersonalFolderToRootOfUser() throws DuplicateNameException
	{
		// create the owner of the folders
		IrUser u = new IrUser("nate", "password");
		
		// create the root collection
		PersonalCollection personalCollection1 = new PersonalCollection(u, "personalCollection1");
		
		PersonalCollection personalCollection2 = null;
		PersonalCollection personalCollection3 = null;
		PersonalCollection personalSubCollection1 = null;
		PersonalCollection personalSubCollection2 = null;
		
		try
		{
		    // add first child
		    personalCollection2 = personalCollection1.createChild("personalCollection2");
				
		    // add second child child
		    personalCollection3 = personalCollection1.createChild("personalCollection3");
			
		    // add sub folder
		    personalSubCollection1 = personalCollection2.createChild("personalSubCollection1");
		
		    // add sub folder
		    personalSubCollection2 = personalCollection3.createChild("personalSubCollection2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert personalCollection1.getLeftValue() == 1 : "Right value should be 1 but is " +
		personalCollection1.getLeftValue();
		assert personalCollection1.getRightValue() == 10 : "Left value should be 10 but is " +
		personalCollection1.getRightValue();
		
		assert personalCollection2.getLeftValue() == 2 : "Right value should be 2 but is " +
		personalCollection1.getLeftValue();
		assert personalCollection2.getRightValue() == 5 : "Right value sould be  5 but is " +
		personalCollection2.getRightValue();
		
		assert personalCollection3.getLeftValue() == 6 : "Right value should be 6 but is " +
		personalCollection1.getLeftValue();
		assert personalCollection3.getRightValue() == 9 : "Right value sould be 9 but is " +
		personalCollection3.getRightValue();
		
		assert personalSubCollection1.getLeftValue() == 3 : "Right value should be 3 but is " +
		personalSubCollection1.getLeftValue();
		assert personalSubCollection1.getRightValue() == 4 : "Right value sould be 4 but is " +
		personalSubCollection1.getRightValue();
		
		assert personalSubCollection2.getLeftValue() == 7 : "Right value should be 7 but is " +
		personalSubCollection2.getLeftValue();
		assert personalSubCollection2.getRightValue() == 8 : "Right value sould be 8 but is " +
		personalSubCollection2.getRightValue();
		
		//make personal folder 2 a root folder by adding them to the user
		u.addRootCollection(personalCollection2);
		
		assert personalCollection2.getLeftValue() == 1 : "Right value should be 4 but is " 
			+ personalCollection2.getLeftValue();
		
		assert personalCollection2.getRightValue() == 4 : "Right value should be 4 but is " 
			+ personalCollection2.getRightValue();
		
		assert personalCollection2.getTreeRoot().equals(personalCollection2) : "personal folder 2 should be root but root = " + 
		personalCollection2.getTreeRoot();
		
		assert personalCollection2.getFullPath().equals("/personalCollection2/") : "Path should "
			+ "equal /personalCollection2/ but equals " + personalCollection2.getFullPath();

		assert personalSubCollection1.getLeftValue() == 2 : "Left value should be 2 but is " + personalSubCollection1.getLeftValue();
		assert personalSubCollection1.getRightValue() == 3 : "Right value should be 3 but is " + personalSubCollection1.getRightValue();
		
		assert personalSubCollection1.getFullPath().equals("/personalCollection2/personalSubCollection1/") : "Path should "
			+ "equal /personalCollection2/personalSubCollection1/ but equals " + personalSubCollection1.getFullPath();

	}
	
	/**
	 * Test re-naming folders.
	 * 
	 * @throws DuplicateNameException 
	 */
	public void testReNameFolder() throws DuplicateNameException
	{
		IrUser u = new IrUser("nate", "password");
		// create the root colleciton
		PersonalCollection personalCollection1 = new PersonalCollection(u, "nates_collection");
		
		
		assert personalCollection1.getOwner().equals(u) : "users should be the same";
		
		assert personalCollection1.getPath().equals("/") : "Path should "
			+ "equal / but equals " + personalCollection1.getPath();

		
		assert personalCollection1.getFullPath().equals("/nates_collection/") : "Path should "
			+ "equal /nates_collection/ but equals " + personalCollection1.getFullPath();
		
		// add first child
		PersonalCollection personalCollection2 = personalCollection1.createChild("personalCollection2");
		
		
		assert personalCollection2.getFullPath().equals("/nates_collection/personalCollection2/") : "Path should "
			    + "equal /nates_collection/personalCollection2/ but equals " + personalCollection2.getFullPath();

		assert personalCollection2.getPath().equals("/nates_collection/") : "Path should "
			    + "equal /nates_collection/ but equals " + personalCollection2.getPath();

		PersonalCollection personalCollection3 = personalCollection1.createChild("personalCollection3");
				
		assert personalCollection3.getPath().equals("/nates_collection/") : "Path should "
			+ "equal /nates_collection/ but equals " + personalCollection3.getPath();

		assert personalCollection3.getFullPath().equals("/nates_collection/personalCollection3/") : "Path should "
			+ "equal /nates_collection/personalCollection3/ but equals " + personalCollection3.getFullPath();

		
	
		PersonalCollection personalSubFolder1 =personalCollection2.createChild("personalSubFolder1");
	
		
		assert personalSubFolder1.getPath().equals("/nates_collection/personalCollection2/") : "Path should "
			+ "equal /nates_collection/personalCollection2/ but equals " + personalSubFolder1.getPath();

		assert personalSubFolder1.getFullPath().equals("/nates_collection/personalCollection2/personalSubFolder1/") : "Path should "
			+ "equal /nates_collection/personalCollection2/personalSubFolder1/ but equals " + personalSubFolder1.getFullPath();



		// add sub collection
		PersonalCollection personalSubCollection2 = personalCollection3.createChild("personalSubCollection2");

		assert personalSubCollection2.getPath().equals("/nates_collection/personalCollection3/") : "Path should "
			+ "equal /nates_collection/personalCollection3/ but equals " + personalSubCollection2.getPath();

		assert personalSubCollection2.getFullPath().equals("/nates_collection/personalCollection3/personalSubCollection2/") : "Path should "
			+ "equal /nates_collection/personalCollection3/personalSubCollection2/ but equals " + personalSubCollection2.getFullPath();

		

		// rename personalCollection3
		personalCollection3.reName("new3");
		
		assert personalSubCollection2.getPath().equals("/nates_collection/new3/") : "Path should "
			+ "equal /nates_collection/new3/ but equals " + personalSubCollection2.getPath();

		assert personalSubCollection2.getFullPath().equals("/nates_collection/new3/personalSubCollection2/") : "Path should "
			+ "equal /nates_collection/new3/personalSubCollection2/ but equals " + personalSubFolder1.getFullPath();
		
		
		// rename the root folder
		personalCollection1.reName("new1");
		
		assert personalSubCollection2.getPath().equals("/new1/new3/") : "Path should "
			+ "equal /new1/new3/ but equals " + personalSubCollection2.getPath();

		assert personalSubCollection2.getFullPath().equals("/new1/new3/personalSubCollection2/") : "Path should "
			+ "equal /new1/new3/personalSubCollection2/ but equals " + personalSubCollection2.getFullPath();
	}

	

}
