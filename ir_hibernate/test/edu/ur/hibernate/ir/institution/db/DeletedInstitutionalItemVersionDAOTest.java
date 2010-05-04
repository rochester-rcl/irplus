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

package edu.ur.hibernate.ir.institution.db;

import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.institution.DeletedInstitutionalItem;
import edu.ur.ir.institution.DeletedInstitutionalItemDAO;
import edu.ur.ir.institution.DeletedInstitutionalItemVersion;
import edu.ur.ir.institution.DeletedInstitutionalItemVersionDAO;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDAO;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;

/**
 * Test the persistence of deleted insitutional item versions.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DeletedInstitutionalItemVersionDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

	/** Institution item data access.  */
	InstitutionalItemDAO institutionalItemDAO = (InstitutionalItemDAO) ctx
	.getBean("institutionalItemDAO");
	
	  /** Generic item data access*/
    GenericItemDAO itemDAO = (GenericItemDAO)ctx.getBean("itemDAO");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");

    /** Deleted Institutional Item data access*/
    DeletedInstitutionalItemDAO deletedInstitutionalItemDAO = (DeletedInstitutionalItemDAO)ctx.getBean("deletedInstitutionalItemDAO");

    /** Deleted Institutional Item data access*/
    DeletedInstitutionalItemVersionDAO deletedInstitutionalItemVersionDAO = (DeletedInstitutionalItemVersionDAO)ctx.getBean("deletedInstitutionalItemVersionDAO");

    
	/** Institution collection data access.  */
	InstitutionalCollectionDAO institutionalCollectionDAO = (InstitutionalCollectionDAO) ctx
	.getBean("institutionalCollectionDAO");

	
	/**
	 * Test deleted Institutional item persistence
	 * @throws CollectionDoesNotAcceptItemsException 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 * 
	 */
	@Test
	public void baseDeletedInstitutionalItemVersionDAOTest() throws CollectionDoesNotAcceptItemsException, LocationAlreadyExistsException, DuplicateNameException {

	    // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		UserEmail userEmail = new UserEmail("email");
				
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		
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

        userDAO.makePersistent(user);
        
		col = institutionalCollectionDAO.getById(col.getId(), false);
		GenericItem genericItem = new GenericItem("genericItem");
		
		InstitutionalItem institutionalItem = col.createInstitutionalItem(genericItem);
		institutionalItemDAO.makePersistent(institutionalItem);
        
        tm.commit(ts);
        
        
        ts = tm.getTransaction(td);
        
        DeletedInstitutionalItem deletedInstitutionalItem = new DeletedInstitutionalItem(institutionalItem);
        deletedInstitutionalItem.setDeletedBy(user);
        deletedInstitutionalItem.setDeletedDate(new Date());
		deletedInstitutionalItemDAO.makePersistent(deletedInstitutionalItem);
		Set<DeletedInstitutionalItemVersion> versions = deletedInstitutionalItem.getDeletedInstitutionalItemVersions();
		assert versions.size() == 1 : "Should have at least one deleted version but has "  + versions.size();
		tm.commit(ts);

		ts = tm.getTransaction(td);
		
		DeletedInstitutionalItemVersion version = (DeletedInstitutionalItemVersion) versions.toArray()[0];
		assert version != null : "Should find the version";
		
		DeletedInstitutionalItemVersion other = deletedInstitutionalItemVersionDAO.getById(version.getId(), false);
		
		assert other.equals(version) : "version " + version + " should be eqal to " + other;
		
		other = deletedInstitutionalItemVersionDAO.get(institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getId());
        assert other != null : "Should be able to find deleted version by original version identifier " + institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getId();
        
		other = deletedInstitutionalItemVersionDAO.get(institutionalItem.getId(), institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getVersionNumber());
        assert other != null : "Should be able to find deleted version by original item id and version number id = " 
        	+ institutionalItem.getId() + "version = " + institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getVersionNumber();
		
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);

		deletedInstitutionalItemDAO.makeTransient(deletedInstitutionalItemDAO.getById(deletedInstitutionalItem.getId(), false));
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		
		
		tm.commit(ts);	
	}

}
