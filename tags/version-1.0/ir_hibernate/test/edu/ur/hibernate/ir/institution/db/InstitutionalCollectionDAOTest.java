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

package edu.ur.hibernate.ir.institution.db;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.institution.InstitutionalCollectionLink;
import edu.ur.ir.institution.InstitutionalCollectionSubscription;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserManager;
import edu.ur.util.FileUtil;

/**
 * Test the persistence methods for an institutional Collection Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class InstitutionalCollectionDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Institution collection data access.  */
	InstitutionalCollectionDAO institutionalCollectionDAO = (InstitutionalCollectionDAO) ctx
	.getBean("institutionalCollectionDAO");
	
	/** User service */
	 IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");
	
	/** Ir File relational data access.  */
	IrFileDAO irFileDAO = (IrFileDAO) ctx.getBean("irFileDAO");
	
	
	
	/**
	 * Test Institutional Collection persistence
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void baseCollectionDAOTest() throws DuplicateNameException, LocationAlreadyExistsException {

	    // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		//commit the transaction 
		// create a collection
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");
		
		institutionalCollectionDAO.makePersistent(col);
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);

		InstitutionalCollection other = institutionalCollectionDAO.getById(col.getId(), false);
		assert other != null : "Should be able to find " +
		"the collection by id";
		
		assert other.equals(col) : "Collections should be equal";
		assert institutionalCollectionDAO.getRootCollection("colName", repo.getId()).equals(other) : "Should find collection";

		// delete the institutional collection
		institutionalCollectionDAO.makeTransient(other);
		tm.commit(ts);
		
		assert institutionalCollectionDAO.getById(other.getId(), false ) == null : "Should not be able to find " +
			"other";
		
		//create a new transaction
		ts = tm.getTransaction(td);
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	
	
	/**
	 * Test Institutional Collection child persistence
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void addChildrenCollectionDAOTest() throws DuplicateNameException, LocationAlreadyExistsException {

		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
		
		// create a collection
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");
		institutionalCollectionDAO.makePersistent(col);
		
        //commit the transaction 
		tm.commit(ts);
		
        // Start a new transaction
		ts = tm.getTransaction(td);

		InstitutionalCollection other = institutionalCollectionDAO.getById(col.getId(), false);
		assert other != null : "Should be able to find "
				+ "the collection by id";

		assert other.equals(col) : "Collections should be equal";
		
		// add the children
		InstitutionalCollection childCol1 = other.createChild("child1");
		InstitutionalCollection childCol2 = other.createChild("child2");
		
		InstitutionalCollection subChild1 = childCol1.createChild("subChild1");
		InstitutionalCollection subChild2 = childCol1.createChild("subChild2");
		
		InstitutionalCollection subSubChild1 = subChild1.createChild("subSubChild1");      		
		InstitutionalCollection subChildA = childCol2.createChild("subChild2");
		
		institutionalCollectionDAO.makePersistent(other);
		// commit the transaction 
		
	
		// make sure the get collection finds the child
		assert institutionalCollectionDAO.getCollection("child1", other.getId()).equals(childCol1) : 
			"Child should be found";
		
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		
		
		// check the paths to make sure the path select works correctly.
		List<InstitutionalCollection> path = 
			institutionalCollectionDAO.getPath(institutionalCollectionDAO.getById(subChildA.getId(), false));
		assert path.size() == 3;
		
		assert path.get(0).equals(col) : "Top parent should be equal to " + col + " but equals " 
		+ path.get(0);
		assert path.get(1).equals(childCol2) : "Second parent should be equal to " + childCol2 
		    + " but equals " + path.get(1);
		assert path.get(2).equals(subChildA) : "Second parent should be equal to " + subChildA 
	    + " but equals " + path.get(2);
		
		InstitutionalCollection parent = institutionalCollectionDAO.getById(other.getId(), false);
		
		InstitutionalCollection otherChildCol1 = parent.getChild("child1");
		assert parent.getLeftValue() == 1 : " Parent left value should equal 1 but equals "
			+ parent.getLeftValue();
		
		assert parent.getRightValue() == 14 : " Parent left value should equal 14 but " +
		    "equals " + parent.getRightValue();
		
		assert otherChildCol1.equals(childCol1) : "Collections should be equal other = " +
		otherChildCol1.toString() + " and childCol1 = " + childCol1.toString();
		
		assert otherChildCol1.getLeftValue() == 2 : "Child left value should equal 2 but is " + 
		    otherChildCol1.getLeftValue();
		
		assert otherChildCol1.getRightValue() == 9 : "Child right value should equal 9 " +
		    " but equals " + otherChildCol1.getRightValue();
		
		InstitutionalCollection otherChildCol2 = parent.getChild("child2");
		assert otherChildCol2.equals(childCol2) : "Collections should be equal other = " +
		otherChildCol2.toString() + " and childCol2 = " + childCol2.toString();
		
		assert otherChildCol2.getLeftValue() == 10 : "Child left value should equal 10 but is " + 
	    otherChildCol2.getLeftValue();
	
	    assert otherChildCol2.getRightValue() == 13 : "Child right value should equal 13 " +
	    " but equals " + otherChildCol2.getRightValue();
	    
	    
		InstitutionalCollection otherSubChild1 = otherChildCol1.getChild("subChild1");
		assert otherSubChild1.equals(subChild1) : "Collections should be equal other = " +
		otherSubChild1.toString() + " and subChild1 = " + subChild1.toString();

		assert otherSubChild1.getLeftValue() == 3 : "Child left value should equal 3 but is " + 
		otherSubChild1.getLeftValue();
	
	    assert otherSubChild1.getRightValue() == 6 : "Child right value should equal 6 " +
	    " but equals " + otherSubChild1.getRightValue();

		InstitutionalCollection otherSubChild2 = otherChildCol1.getChild("subChild2");
		assert otherSubChild2.equals(subChild2) : "Collections should be equal other = " +
		otherSubChild2.toString() + " and subChild1 = " + subChild2.toString();

		assert otherSubChild2.getLeftValue() == 7 : "Child left value should equal 7 but is " + 
		otherSubChild2.getLeftValue();
	
	    assert otherSubChild2.getRightValue() == 8 : "Child right value should equal 8 " +
	    " but equals " + otherSubChild2.getRightValue();

	    
		InstitutionalCollection otherSubSubChild1 = otherSubChild1.getChild("subSubChild1");
		assert otherSubSubChild1.equals(subSubChild1) : "Collections should be equal other = " +
		otherSubSubChild1.toString() + " and subChild1 = " + subSubChild1.toString();

		assert otherSubSubChild1.getLeftValue() == 4 : "Child left value should equal 4 but is " + 
		otherSubSubChild1.getLeftValue();
	
	    assert otherSubSubChild1.getRightValue() == 5 : "Child right value should equal 5 " +
	    " but equals " + otherSubSubChild1.getRightValue();
	    
		InstitutionalCollection otherSubChildA = otherChildCol2.getChild("subChild2");
		assert otherSubChildA.equals(subChildA) : "Collections should be equal other = " +
		otherSubChildA.toString() + " and subChildA = " + subChildA.toString();
		
		assert otherSubChildA.getLeftValue() == 11 : "Child left value should equal 11 but is " + 
		otherSubChildA.getLeftValue();
	
	    assert otherSubChildA.getRightValue() == 12 : "Child right value should equal 12 " +
	    " but equals " + otherSubChildA.getRightValue();
	    
		// commit the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		
		assert repo.getInstitutionalCollections().size() == 1 : "Should only have 1 top level " +
				"collection but has " + repo.getInstitutionalCollections().size();
		
		Set<InstitutionalCollection> collections = repoHelper.getRepository().getInstitutionalCollections();
		
		InstitutionalCollection myColl = null;
		for( InstitutionalCollection c : collections)
		{
			myColl = c;
		}
		
		assert myColl.getTreeSize() == 7 : "left value = " + myColl.getLeftValue() 
		+ " right value = " + myColl.getRightValue() + " Collection = " + myColl 
		+ " Tree size should be 7 but is " + myColl.getTreeSize();
		
		assert myColl.getChildren().size() == 2 : "Should have two children but has " +
		  myColl.getChildren().size();
		
		assert myColl.getChildren().contains(childCol1) : "Should contain child 1";
		assert myColl.getChildren().contains(childCol2) : "Should contain child 2";

		tm.commit(ts);
		

		ts = tm.getTransaction(td);
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
	 

	/**
	 * Test Searching for root institutional Collection
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void searchRootCollectionDAOTest() throws DuplicateNameException, LocationAlreadyExistsException {

		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// create a collection
		InstitutionalCollection col1 = repo.createInstitutionalCollection("aCollection1");
		col1.setDescription("aCollection1");
		
		col1.createChild("aChild1");
		col1.createChild("aChild2");
		
		InstitutionalCollection col2 = repo.createInstitutionalCollection("bCollection2");
		col2.setDescription("bCollection2");

		InstitutionalCollection col3 = repo.createInstitutionalCollection("cCollection3");
		col3.setDescription("cCollection3");
		
		InstitutionalCollection col4 = repo.createInstitutionalCollection("dCollection4");
		col4.setDescription("cCollection4");

		InstitutionalCollection col5 = repo.createInstitutionalCollection("eCollection5");
		col5.setDescription("eCollection5");
		
		institutionalCollectionDAO.makePersistent(col1);
		institutionalCollectionDAO.makePersistent(col2);
		institutionalCollectionDAO.makePersistent(col3);
		institutionalCollectionDAO.makePersistent(col4);
		institutionalCollectionDAO.makePersistent(col5);
		
        //commit the transaction
		tm.commit(ts);
		
        // Start the transaction
		ts = tm.getTransaction(td);
		 
		List<InstitutionalCollection> collections = 
			institutionalCollectionDAO.getRootInstituionalCollections(repo.getId(), 
					 0, 5, "asc");
			
		assert collections.size() == 5 : "Collection size should be 5 but is " + collections.size();
		assert collections.contains(col1) : "Collection should contain col1 but does not";
		
		//filter on repository
		assert repo.getId() != null : "Repository should not be null";
		Long count = institutionalCollectionDAO.getRootInstitutionalCollectionsCount(repo.getId());
		assert  count.intValue() == 5 : "Count should be one but is " + count;
		
		tm.commit(ts);
		
		//start a new transaction
		ts = tm.getTransaction(td);
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * test adding pictures to the institutional collection
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void addInstitutionalCollectionPictures() throws DuplicateNameException,
	 	IllegalFileSystemNameException, LocationAlreadyExistsException
	{
		TransactionStatus ts = tm.getTransaction(td);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// create a collection
		InstitutionalCollection col1 = repo.createInstitutionalCollection("aCollection1");
		col1.setDescription("aCollection1");
        //commit the transaction
		tm.commit(ts);
		
        // helper to create the file
		// we are only testing the ability to add a file to
		// the institutional collection rather than an actual picture.
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "testFile", 
				"Hello  - irFile This is text in a file");

		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "newFile1");
		fileInfo1.setDescription("testThis");
		IrFile irFile = new IrFile(fileInfo1, "newName");
		
		irFileDAO.makePersistent(irFile);
		
		// file to represent the primary picture
		File primaryFile = testUtil.creatFile(directory, "testFile2", 
		"Hello  - irFile This is text in a file");

        FileInfo primaryInfo1 = repo.getFileDatabase().addFile(primaryFile, "newFile2");
        primaryInfo1.setDescription("testThis2");
        IrFile primaryIrFile = new IrFile(fileInfo1, "newName2");
        
        irFileDAO.makePersistent(primaryIrFile);
		
		// add the file to the collection
        ts = tm.getTransaction(td);
        col1 = institutionalCollectionDAO.getById(col1.getId(), false);
		col1.addPicture(irFile);
		col1.setPrimaryPicture(primaryIrFile);
		institutionalCollectionDAO.makePersistent(col1);
		tm.commit(ts);
		
		//create a new transaction
		ts = tm.getTransaction(td);
		//reload the repository
		col1 = institutionalCollectionDAO.getById(col1.getId(), false);
		assert col1.getPicture(irFile.getId()) != null : "The picture should be found";
		assert col1.getPrimaryPicture() != null : "The primary picture should exist";
		tm.commit(ts);	
		
		//create a new transaction
		ts = tm.getTransaction(td);
		col1 = institutionalCollectionDAO.getById(col1.getId(), false);
		Set<IrFile> pictures = col1.getPictures();
		
		for(IrFile picture: pictures)
		{
			col1.removePicture(picture);
			irFileDAO.makeTransient(picture);
		}
		
		irFileDAO.makeTransient(col1.getPrimaryPicture());
		col1.setPrimaryPicture(null);
		
		institutionalCollectionDAO.makeTransient(col1);
		tm.commit(ts);
		
		//start a new transaction
		ts = tm.getTransaction(td);
		repoHelper.cleanUpRepository();
		tm.commit(ts);
	}
	
	/**
	 * Test Institutional Collection child persistence
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void getCollectionChildrenDAOTest() throws DuplicateNameException, LocationAlreadyExistsException {

		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
		
		// create a collection
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");
		
		// add the children
		InstitutionalCollection childCol1 = col.createChild("child1");
		InstitutionalCollection childCol2 = col.createChild("child2");
		
		InstitutionalCollection subChild1 = childCol1.createChild("subChild1");
		InstitutionalCollection subChild2 = childCol1.createChild("subChild2");
		
		InstitutionalCollection subSubChild1 = subChild1.createChild("subSubChild1");      		
		InstitutionalCollection subChildA = childCol2.createChild("subChild2");
		
		institutionalCollectionDAO.makePersistent(col);
		// commit the transaction 
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		List<InstitutionalCollection> children = institutionalCollectionDAO.getAllChildrenForCollection(childCol2);
		assert children.contains(subChildA);
		assert children.size() == 1 : "Size of children should be 1 but is : " + children.size();
		
		children = institutionalCollectionDAO.getAllChildrenForCollection(childCol1);
		assert children.size() == 3 : "Size of children should be 3 but is : " + children.size();
		assert children.contains(subChild1);
		assert children.contains(subChild2);
		assert children.contains(subSubChild1);
		
		children = institutionalCollectionDAO.getAllChildrenForCollection(col);
		assert children.size() == 6 : "Size of children should be 6 but is : " + children.size();
		assert children.contains(childCol1);
		assert children.contains(childCol2);
		assert children.contains(subChildA);
		assert children.contains(subChild1);
		assert children.contains(subChild2);
		assert children.contains(subSubChild1);

		tm.commit(ts);	
	

		ts = tm.getTransaction(td);
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}

	/**
	 * Test Institutional Collection subscription
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void collectionSubscriptionTest() throws DuplicateNameException, LocationAlreadyExistsException {

		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
		
		// create a collection
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");
		
		institutionalCollectionDAO.makePersistent(col);
		// commit the transaction 
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);
		
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		userDAO.makePersistent(user);
		
		tm.commit(ts);
		
		// start new transaction
		ts = tm.getTransaction(td);
		col = institutionalCollectionDAO.getById(col.getId(), false);
		col.addSuscriber(user);
		institutionalCollectionDAO.makePersistent(col);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		InstitutionalCollection otherCollection = institutionalCollectionDAO.getById(col.getId(), false);
		assert otherCollection.getSubscriptions().size() == 1 : "Should have 1 subscriber";
		assert otherCollection.hasSubscriber(user) :"Should have subscriber - " + user.getUsername();
		otherCollection.removeSubscriber(user);
		institutionalCollectionDAO.makePersistent(otherCollection);
		tm.commit(ts);

		ts = tm.getTransaction(td);
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test Institutional Collection link persistence
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void collectionLinkDAOTest() throws DuplicateNameException, LocationAlreadyExistsException {

	    // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		//commit the transaction 
		// create a collection
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");
		
		InstitutionalCollectionLink link0 = col.addLink("link0", "http://theservierside.com");
		InstitutionalCollectionLink link1 = col.addLink("link1", "http://www.hotmail.com");
		InstitutionalCollectionLink link2 = col.addLink("link2", "http://www.hotmail.com");
		InstitutionalCollectionLink link3 = col.addLink("link3", "http://www.hotmail.com");
		institutionalCollectionDAO.makePersistent(col);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		col = institutionalCollectionDAO.getById(col.getId(), false);
		List<InstitutionalCollectionLink> links = col.getLinks();
		
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 0 : "link 0 order should be 0 but is " + link0.getOrder();
		
		assert links.contains(link1) : "links should contain " + link1;
		assert link1.getOrder() == 1 : "link 1 order should be 1 but is " + link1.getOrder();
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 2 : "link 2 order should be 2 but is " + link2.getOrder();

		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 3 : "link 3 order should be 3 but is " + link3.getOrder();
				
		col.moveLink(link3, 8);
		institutionalCollectionDAO.makePersistent(col);
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		col = institutionalCollectionDAO.getById(col.getId(), false);
		links = col.getLinks();
		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 3 : "link 3 order should be 3 but is " + link3.getOrder();
		
		assert col.moveLink(link1, 3) : " link 1 should be moved ";
		
		links = col.getLinks();
		
		link0 = col.getLink("link0");
		link2 = col.getLink("link2");
		link3 = col.getLink("link3");
		link1 = col.getLink("link1");
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 0 : "link 0 order should be 0 but is " + link0.getOrder();
		
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 1 : "link 2 order should be 1 but is " + link2.getOrder();

		
		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 2 : "link 3 order should be 2 but is " + link3.getOrder();
		
		
		assert links.contains(link1) : "links should contain " + link1;
		assert link1.getOrder() == 3 : "link 3 order should be 3 but is " + link1.getOrder();
		
		
		links = col.getLinks();
		
		for(InstitutionalCollectionLink l : links)
		{
			System.out.println("link = " + l);
		}
		
		
		institutionalCollectionDAO.makePersistent(col);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		col = institutionalCollectionDAO.getById(col.getId(), false);
		links = col.getLinks();
		link0 = col.getLink("link0");
		link2 = col.getLink("link2");
		link3 = col.getLink("link3");
		link1 = col.getLink("link1");
		
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 0 : "link 0 order should be 0 but is " + link0.getOrder();
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 1 : "link 1 order should be 1 but is " + link2.getOrder();

		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 2 : "link 2 order should be 2 but is " + link3.getOrder();
		
		assert links.contains(link1) : "links should contain " + link1;
		assert link1.getOrder() == 3 : "link 3 order should be 3 but is " + link1.getOrder();
		tm.commit(ts);
		
		// remove link 1
		ts = tm.getTransaction(td);
		col = institutionalCollectionDAO.getById(col.getId(), false);
		link1 = col.getLink("link1");
		col.removLink(link1);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		col = institutionalCollectionDAO.getById(col.getId(), false);
		links = col.getLinks();
		link0 = col.getLink("link0");
		link2 = col.getLink("link2");
		link3 = col.getLink("link3");
		
		assert links.contains(link0) : "links should contain " + link0;
		assert link0.getOrder() == 0 : "link 0 order should be 0 but is " + link0.getOrder();
		
		assert links.contains(link2) : "links should contain " + link2;
		assert link2.getOrder() == 1 : "link 1 order should be 1 but is " + link2.getOrder();

		assert links.contains(link3) : "links should contain " + link3;
		assert link3.getOrder() == 2 : "link 2 order should be 2 but is " + link3.getOrder();
		
		assert col.getLink("link1") == null : "links should not contain link 1";
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);

		InstitutionalCollection other = institutionalCollectionDAO.getById(col.getId(), false);
		assert other != null : "Should be able to find " +
		"the collection by id";

		// delete the institutional collection
		institutionalCollectionDAO.makeTransient(other);
		tm.commit(ts);
		
		
		
		//create a new transaction
		ts = tm.getTransaction(td);
		assert institutionalCollectionDAO.getById(other.getId(), false ) == null : "Should not be able to find " +
		"other";
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test Institutional Collection link persistence
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void collectionSubscriberDAOTest() throws DuplicateNameException, LocationAlreadyExistsException {

	    // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		//commit the transaction 
		// create a collection
		InstitutionalCollection collection = repo.createInstitutionalCollection("colName");
		collection.setDescription("colDescription");

		institutionalCollectionDAO.makePersistent(collection);
		
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		userDAO.makePersistent(user);
		
		tm.commit(ts);
		
		
		// start a new transaction
		ts = tm.getTransaction(td);
		collection = institutionalCollectionDAO.getById(collection.getId(), false);
		assert collection.getSubscriptions().size() == 0 : "Should have 0 subscriptions but has " + collection.getSubscriptions().size();
		
		InstitutionalCollectionSubscription subscription = collection.addSuscriber(user);
		institutionalCollectionDAO.makePersistent(collection);
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);
		collection = institutionalCollectionDAO.getById(collection.getId(), false);
		assert collection.getSubscriptions().size() == 1 : "Should have 1 subscriptions but has " + collection.getSubscriptions().size();

		assert subscription.getUser().equals(user) : "Collection subscription user should = " + user 
		+ " but = " + subscription.getUser();
		
		subscription = collection.getSubscription(user);
		assert subscription.getInstitutionalCollection().equals(collection) : "Collection subscription collection should = " + collection 
		+ " but = " + subscription.getInstitutionalCollection();
		
		assert collection.hasSubscriber(user) : " Collection should have user " + user;
		
		// remove the subscriber
		collection.removeSubscriber(user);
		institutionalCollectionDAO.makePersistent(collection);
		tm.commit(ts);
		
			
		
  	    // start a new transaction
		ts = tm.getTransaction(td);
		
		collection = institutionalCollectionDAO.getById(collection.getId(), false);

		assert !collection.hasSubscriber(user) : " Collection should NOT have user " + user;
		// delete the institutional collection
		institutionalCollectionDAO.makeTransient(collection);
		repoHelper.cleanUpRepository();
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		

	}

}
