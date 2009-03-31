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

package edu.ur.hibernate.ir.person.db;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDAO;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorDAO;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeDAO;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonNameAuthorityDAO;
import edu.ur.ir.person.PersonNameDAO;
import edu.ur.ir.person.PersonNameTitle;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.order.OrderType;

/**
 * Test the persistance methods for Institution Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PersonNameDAOTest {

	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

     /** Person name data access */
    PersonNameDAO personNameDAO = (PersonNameDAO) ctx
	.getBean("personNameDAO");
     
	/** Person data access  */
	PersonNameAuthorityDAO personNameAuthorityDAO = (PersonNameAuthorityDAO) ctx
	.getBean("personNameAuthorityDAO");

	/** Transaction management  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
		.getBean("transactionManager");
    
	/** Transaction definition  */
	TransactionDefinition td = new DefaultTransactionDefinition(
		TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Institution collection data access.  */
	InstitutionalCollectionDAO institutionalCollectionDAO = (InstitutionalCollectionDAO) ctx
	.getBean("institutionalCollectionDAO");
	
	/** Institution item data access.  */
	InstitutionalItemDAO institutionalItemDAO = (InstitutionalItemDAO) ctx
	.getBean("institutionalItemDAO");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	   /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");
    
    /** Generic item data access*/
    GenericItemDAO itemDAO = (GenericItemDAO)ctx.getBean("itemDAO");
    
    /** contributor type data access */
    ContributorTypeDAO contributorTypeDAO = (ContributorTypeDAO) ctx
	.getBean("contributorTypeDAO");

    /** contributor data access */
     ContributorDAO contributorDAO = (ContributorDAO) ctx
		.getBean("contributorDAO");

	
	/**
	 * Test PersonName persistance
	 */
	@Test
	public void basePersonNameDAOTest()throws Exception{
 		PersonName name = new PersonName();
		name.setFamilyName("familyName");
		name.setForename("forename");
		name.setInitials("n.d.s.");
		name.setMiddleName("MiddleName");
		name.setNumeration("III");
		name.setSurname("surname");
		
		// give the person some titles
		PersonNameTitle title = new PersonNameTitle("Dr.", name);
		PersonNameTitle title2 = new PersonNameTitle("Chef", name);
		
		name.addPersonNameTitle("Dr.");
		name.addPersonNameTitle("Chef");
		
		PersonNameAuthority p = new PersonNameAuthority(name);
		p.addBirthDate(1,1,2005);
		p.addDeathDate(1, 1, 2105);

        TransactionStatus ts = tm.getTransaction(td);
		personNameAuthorityDAO.makePersistent(p);
		
		
		PersonName other = personNameDAO.getById(name.getId(), false);
		assert other.equals(name) : "other should be equal to name";
		
		for( PersonNameTitle pnt : other.getPersonNameTitles() )
		{
			System.out.println("Title = " + pnt.getTitle());
		}
		
		assert other.getPersonNameTitles().contains(title) : "other name should have " + title;
		assert other.getPersonNameTitles().contains(title2) : "other name should have " + title2;
		
		//complete the transaction
		tm.commit(ts);

        ts = tm.getTransaction(td);
		assert personNameDAO.getCount() == 1 : "Should have one person name in the database";

		personNameDAO.getAllNameOrder();
		personNameDAO.getAllOrderByName(1, 2);
		
		// make sure we can delete the authoritative name
		personNameDAO.makeTransient(personNameDAO.getById(name.getId(), false));
		assert personNameDAO.getById(name.getId(), false) == null : "Should no longer be able to find person name";	
		personNameAuthorityDAO.makeTransient( personNameAuthorityDAO.getById(p.getId(), false));
		tm.commit(ts);

	}
	
	/**
	 * Test searching for a person name 
	 */
	@Test
	public void personNameSearchingDAOTest()throws Exception{
		
 		PersonName name = new PersonName();
		name.setForename("forename1");
		name.setSurname("surname1");
		
		PersonNameAuthority p = new PersonNameAuthority(name);
		p.addBirthDate(1,1,2005);
		p.addDeathDate(1, 1, 2105);

        // Start the transaction this is for lazy loading
        TransactionStatus ts = tm.getTransaction(td);

		personNameAuthorityDAO.makePersistent(p);
		
		
		PersonName name2 = new PersonName();
		name2.setForename("fore2name");
		name2.setSurname("sur2name");
		
		PersonNameAuthority p2 = new PersonNameAuthority(name2);
		p2.addBirthDate(1,1,2005);
		p2.addDeathDate(1, 1, 2105);
		
		personNameAuthorityDAO.makePersistent(p2);
		p2.addName(name2, true);
		personNameDAO.makePersistent(name2);
		tm.commit(ts);

		ts = tm.getTransaction(td);
		List<PersonName> names = personNameDAO.findPersonLikeFirstName("fore", 0, 10);
		assert names.size() == 2 : "Should have 2 people found in the database but found " + names.size();
		
		names = personNameDAO.findPersonLikeFirstName("foreName", 0, 10);
		assert names.size() == 1 : "Should have one person found in the database but found " + names.size();

		names = personNameDAO.findPersonLikeLastName("sur", 0, 10);
		assert names.size() == 2 : "Should have 2 people found in the database but found " + names.size();
		
		names = personNameDAO.findPersonLikeLastName("surname", 0, 10);
		assert names.size() == 1 : "Should have one person found in the database but found " + names.size();

		names = personNameDAO.findPersonLikeFirstLastName( "fore", "sur",0, 10);
		assert names.size() == 2 : "should find both recrods but found " + names.size();
		
		names = personNameDAO.findPersonLikeFirstLastName( "fore", "surname", 0, 10);
		assert names.size() == 1 : "should only find one name but found " + names.size();
		
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p.getId(), false));
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p2.getId(), false));
		tm.commit(ts);

	}
	
	/**
	 * Test getting a person by their last name first character
	 */
	@Test
	public void personNameGetByFirstCharTest()throws Exception{
		
 		PersonName name = new PersonName();
		name.setForename("forename1");
		name.setSurname("surname1");
		
		PersonNameAuthority p = new PersonNameAuthority(name);
		p.addBirthDate(1,1,2005);
		p.addDeathDate(1, 1, 2105);

        // Start the transaction this is for lazy loading
        TransactionStatus ts = tm.getTransaction(td);

		personNameAuthorityDAO.makePersistent(p);
		
		
		PersonName name2 = new PersonName();
		name2.setForename("Bore2name");
		name2.setSurname("bur2name");
		
		PersonNameAuthority p2 = new PersonNameAuthority(name2);
		p2.addBirthDate(1,1,2005);
		p2.addDeathDate(1, 1, 2105);
		
		personNameAuthorityDAO.makePersistent(p2);
		p2.addName(name2, true);
		personNameDAO.makePersistent(name2);
		
		PersonName name3 = new PersonName();
		name3.setForename("Robore2name");
		name3.setSurname("bar2name");
		
		PersonNameAuthority p3 = new PersonNameAuthority(name3);
		p3.addBirthDate(1,1,2005);
		p3.addDeathDate(1, 1, 2105);
		
		personNameAuthorityDAO.makePersistent(p3);
		p3.addName(name3, true);
		personNameDAO.makePersistent(name3);
		
		tm.commit(ts);

		ts = tm.getTransaction(td);
		
		// test single character counts
		Long count = personNameDAO.getCount('B');
		assert count == 2l : "Count should equal 2 but equals " + count;
		
		count = personNameDAO.getCount('s');
		assert count == 1l : "Count should equal 1 but equals " + count;
		
		// test single character
		List<PersonName> names = personNameDAO.getPersonNamesByChar(0, 20, 'B', OrderType.ASCENDING_ORDER);
		assert names.size() == 2;
		
	
		assert names.contains(name2) : "Should contain " + name2;
		assert names.contains(name3) : "Should contain " + name3;
		
		// check order
		assert names.get(0).equals(name3) : "Index 0 should contain " + name3;
		
		names = personNameDAO.getPersonNamesByChar(0, 20, 's', OrderType.ASCENDING_ORDER);
		assert names.size() == 1;
		assert names.contains(name) : " should contain " + name;
		
		names = personNameDAO.getPersonNamesByChar(0, 20, 's', OrderType.ASCENDING_ORDER);
		
		
		// test range counts
		count = personNameDAO.getCount('B', 'C');
		assert count == 2l : "Count should equal 2 but equals " + count;
		names = personNameDAO.getPersonNamesBetweenChar(0, 20, 'B', 'c', OrderType.ASCENDING_ORDER);
		
		assert names.size() == 2l : "Count should equal 2 but equals " + count;
		
		
		count = personNameDAO.getCount('B', 'S');
		assert count == 3l : "Count should equal 3 but equals " + count;
		names = personNameDAO.getPersonNamesBetweenChar(0, 20, 'b', 'S', OrderType.ASCENDING_ORDER);
		assert names.size() == 3l : "Count should equal 3 but equals " + count;
		
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p.getId(), false));
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p2.getId(), false));
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p3.getId(), false));
		tm.commit(ts);

	}
	
	
	/**
	 * Test getting a person by their last name first character within collections
	 */
	@Test
	public void personNameGetByFirstCharInCollectionTest()throws Exception{
		
	    // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// create a collection
		InstitutionalCollection col1 = repo.createInstitutionalCollection("col1");
		col1.setDescription("col1Description");
		institutionalCollectionDAO.makePersistent(col1);

		
		// create a collection
		InstitutionalCollection col2 = repo.createInstitutionalCollection("col2");
		col2.setDescription("col2Description");
		
		// create a child collection in collection 2
		InstitutionalCollection col3 = col2.createChild("subCol2");
		
		//save both collections
		institutionalCollectionDAO.makePersistent(col2);
		
		// create contributors
 		ContributorType ct = new ContributorType("ctName");
 		ct.setDescription("ctDescription");
 		
		ContributorType ct2 = new ContributorType("ctName2");
 		ct2.setDescription("ctDescription2");

        contributorTypeDAO.makePersistent(ct);
        contributorTypeDAO.makePersistent(ct2);
         
        // create 3 people
		PersonName name = new PersonName();
		name.setForename("forename1");
		name.setSurname("surname1");
		
		PersonNameAuthority p = new PersonNameAuthority(name);
		p.addBirthDate(1,1,2005);
		p.addDeathDate(1, 1, 2105);
		personNameAuthorityDAO.makePersistent(p);
		
		
		PersonName name2 = new PersonName();
		name2.setForename("Bore2name");
		name2.setSurname("bur2name");
		
		PersonNameAuthority p2 = new PersonNameAuthority(name2);
		p2.addBirthDate(1,1,2005);
		p2.addDeathDate(1, 1, 2105);
		
		personNameAuthorityDAO.makePersistent(p2);
		p2.addName(name2, true);
		personNameDAO.makePersistent(name2);
		
		PersonName name3 = new PersonName();
		name3.setForename("Robore2name");
		name3.setSurname("bar2name");
		
		PersonNameAuthority p3 = new PersonNameAuthority(name3);
		p3.addBirthDate(1,1,2005);
		p3.addDeathDate(1, 1, 2105);
		
		personNameAuthorityDAO.makePersistent(p3);
		p3.addName(name3, true);
		personNameDAO.makePersistent(name3);
		
		Contributor contrib = new Contributor(name, ct);
		contributorDAO.makePersistent(contrib);
		
		Contributor contrib2 = new Contributor(name2, ct2);
		contributorDAO.makePersistent(contrib2);
		
		Contributor contrib3 = new Contributor(name3, ct);
		contributorDAO.makePersistent(contrib3);

		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);
		
		UserEmail userEmail = new UserEmail("email");
				
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);

        userDAO.makePersistent(user);
        
        // create item for collection 1
		col1 = institutionalCollectionDAO.getById(col1.getId(), false);
		GenericItem genericItem = new GenericItem("genericItem");
		genericItem.addContributor(contrib);
		InstitutionalItem institutionalItem = col1.createInstitutionalItem(genericItem);
		institutionalItemDAO.makePersistent(institutionalItem);
		
		// create item for collection 2
		col2 = institutionalCollectionDAO.getById(col2.getId(), false);
		GenericItem genericItem2 = new GenericItem("genericItem2");
		genericItem2.addContributor(contrib);
		genericItem2.addContributor(contrib2);
		genericItem2.addContributor(contrib3);
		InstitutionalItem institutionalItem2 = col2.createInstitutionalItem(genericItem2);
		institutionalItemDAO.makePersistent(institutionalItem2);
		
		// create item for collection 3
		col3 = institutionalCollectionDAO.getById(col3.getId(), false);
		GenericItem genericItem3 = new GenericItem("genericItem3");
		genericItem3.addContributor(contrib3);
		InstitutionalItem institutionalItem3 = col3.createInstitutionalItem(genericItem3);
		institutionalItemDAO.makePersistent(institutionalItem3);

		tm.commit(ts);

		
		
		// test tree based selects
		ts = tm.getTransaction(td);
		Long count = personNameDAO.getCount(col1, 'S');
		assert count == 1l : "count should be 1 but is " + count;
		List<PersonName> names = personNameDAO.getCollectionPersonNamesByChar(0, 20, col1, 's', OrderType.ASCENDING_ORDER);
		assert names.size() == 1 : "Names size should be 1 but is " + names.size();
		assert names.contains(name) : " Should contain name " + name;
		
		count = personNameDAO.getCount(col2, 'B');
		assert count == 2l : "count should be 2 but is " + count;
		names = personNameDAO.getCollectionPersonNamesByChar(0, 20, col2, 'b', OrderType.ASCENDING_ORDER);
        assert names.size() == 2 : " Should only be 2 name found but found " + names.size();
        assert names.contains(name2) : "Should contain name 2";
        assert names.contains(name3) : " Should contain name 3";
        
        count = personNameDAO.getCount(col2, 'S');
		assert count == 1l : "count should be 1 but is " + count;
        names = personNameDAO.getCollectionPersonNamesByChar(0, 20, col2, 's', OrderType.DESCENDING_ORDER);
        assert names.size() == 1 : " Should only be 1 name found but found " + names.size();
        
        count = personNameDAO.getCount(col3, 'B');
		assert count == 1l : "count should be 1 but is " + count;
        names = personNameDAO.getCollectionPersonNamesByChar(0, 20, col3, 'b', OrderType.ASCENDING_ORDER);
        assert names.size() == 1 : " Should only be 1 name found but found " + names.size();

        count = personNameDAO.getCount(col1, 'a', 'z');
		assert count == 1l : "count should be 1 but is " + count;
        names = personNameDAO.getCollectionPersonNamesBetweenChar(0, 20, col1, 'a', 'z', OrderType.DESCENDING_ORDER);
		assert names.size() == 1 : "Should contain 1 name but contains " + names.size();
       
        
        count = personNameDAO.getCount(col2, 'a', 'z');
		assert count == 3l : "count should be 1 but is " + count;
        names = personNameDAO.getCollectionPersonNamesBetweenChar(0, 20, col2, 'a', 'z', OrderType.ASCENDING_ORDER);
		assert names.size() == 3 : "Should contain 3 names but contains " + names.size();
      
		tm.commit(ts);
		
		
		
		//create a new transaction
		ts = tm.getTransaction(td);
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col1.getId(), false));
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col2.getId(), false));
		
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItem2.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItem3.getId(), false));

		userDAO.makeTransient(userDAO.getById(user.getId(), false));

		contributorDAO.makeTransient(contributorDAO.getById(contrib.getId(), false));
		contributorDAO.makeTransient(contributorDAO.getById(contrib2.getId(), false));
		contributorDAO.makeTransient(contributorDAO.getById(contrib3.getId(), false));
		
		contributorTypeDAO.makeTransient(contributorTypeDAO.getById(ct.getId(), false));
		contributorTypeDAO.makeTransient(contributorTypeDAO.getById(ct2.getId(), false));
		
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p.getId(), false));
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p2.getId(), false));
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p3.getId(), false));
		
		repoHelper.cleanUpRepository();
		
		tm.commit(ts);	

	}
}
