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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeDAO;
import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDAO;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;


/**
 * Test the persistence methods for an institutional item Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class InstitutionalItemIndexProcessingRecordDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** index porcessing type data access object */
	IndexProcessingTypeDAO indexProcessingTypeDAO = (IndexProcessingTypeDAO) ctx
	.getBean("indexProcessingTypeDAO");
	
	/** institutional item index processing record data access object */
	InstitutionalItemIndexProcessingRecordDAO institutionalItemIndexProcessingRecordDAO = (InstitutionalItemIndexProcessingRecordDAO) ctx
	.getBean("institutionalItemIndexProcessingRecordDAO");;
	
	/** platform transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** Institution collection data access.  */
	InstitutionalCollectionDAO institutionalCollectionDAO = (InstitutionalCollectionDAO) ctx
	.getBean("institutionalCollectionDAO");
	
	/** User service */
	 IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");
	 
	/** Institution item data access.  */
	InstitutionalItemDAO institutionalItemDAO = (InstitutionalItemDAO) ctx
		.getBean("institutionalItemDAO");
		
	/** Generic item data access*/
	GenericItemDAO itemDAO = (GenericItemDAO)ctx.getBean("itemDAO");
	
	/**
	 * Test Institutional item processing record persistence
	 */
	@Test
	public void baseInstitutionalItemIndexProcessingRecordTest()
	{

         
        TransactionStatus ts = tm.getTransaction(td);
		IndexProcessingType indexProcessingType = new IndexProcessingType("ipName");
 		indexProcessingType.setDescription("description");
 		indexProcessingTypeDAO.makePersistent(indexProcessingType);
 		
 		InstitutionalItemIndexProcessingRecord itemIndexProcessingRecord = 
 			new InstitutionalItemIndexProcessingRecord(33l, indexProcessingType);
 		
 		Timestamp t = new Timestamp(new Date().getTime());
        itemIndexProcessingRecord.setUpdatedDate(t);
 		institutionalItemIndexProcessingRecordDAO.makePersistent(itemIndexProcessingRecord);
 	    tm.commit(ts);
 	    
 
 	    
 	    ts = tm.getTransaction(td);
 	    InstitutionalItemIndexProcessingRecord other = institutionalItemIndexProcessingRecordDAO.getById(itemIndexProcessingRecord.getId(), false);
        assert other.equals(itemIndexProcessingRecord) : "index processing record " + itemIndexProcessingRecord + "should be equal" + other;
         
        List<InstitutionalItemIndexProcessingRecord> records = institutionalItemIndexProcessingRecordDAO.getAllOrderByItemIdUpdatedDate();
        
        assert records.size() == 1 : "Should have at 1 record but has " + records.size();
        assert records.contains(other) : "records should contain other";
        
        InstitutionalItemIndexProcessingRecord recordByItemIdProcessingType = 
        	institutionalItemIndexProcessingRecordDAO.get(33l, indexProcessingType);
        
        assert recordByItemIdProcessingType !=  null : " Should find record with id 33l and processing type " + indexProcessingType;
        assert recordByItemIdProcessingType.equals(other) : " Other " + other + " should equal " + recordByItemIdProcessingType;
        
        
        institutionalItemIndexProcessingRecordDAO.makeTransient(other);
        indexProcessingTypeDAO.makeTransient(indexProcessingTypeDAO.getById(indexProcessingType.getId(), false));
        assert institutionalItemIndexProcessingRecordDAO.getById(other.getId(), false) == null : "Should no longer be able to find index processing record";
	    tm.commit(ts);

	}
	
	@Test
	public void insertCollectionRecordsProcessingRecordTest() throws LocationAlreadyExistsException,
	DuplicateNameException,
	CollectionDoesNotAcceptItemsException
	{
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
		
		UserEmail userEmail = new UserEmail("email");
				
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);

        userDAO.makePersistent(user);
		col = institutionalCollectionDAO.getById(col.getId(), false);
		GenericItem genericItem = new GenericItem("genericItem");
		
		InstitutionalItem institutionalItem = col.createInstitutionalItem(genericItem);
		institutionalItemDAO.makePersistent(institutionalItem);
		
		GenericItem genericItem2 = new GenericItem("genericItem2");
		InstitutionalItem institutionalItem2 = col.createInstitutionalItem(genericItem2);
		institutionalItemDAO.makePersistent(institutionalItem2);

		// create the index processing type
		IndexProcessingType indexProcessingType = new IndexProcessingType("ipName");
 		indexProcessingType.setDescription("description");
 		indexProcessingTypeDAO.makePersistent(indexProcessingType);
		
 		
 		
 		tm.commit(ts);

		ts = tm.getTransaction(td);
		Long count = institutionalItemIndexProcessingRecordDAO.insertAllItemsForCollection(col, indexProcessingType);

		assert count == 2l : "two records should be inserted but found " + count;
		
		indexProcessingType = indexProcessingTypeDAO.findByUniqueName("ipName");
		InstitutionalItemIndexProcessingRecord rec1 = 
			institutionalItemIndexProcessingRecordDAO.get(institutionalItem.getId(), indexProcessingType);

		assert rec1 != null : "Record one should not be null";
		
		InstitutionalItemIndexProcessingRecord rec2 = 
			institutionalItemIndexProcessingRecordDAO.get(institutionalItem2.getId(), indexProcessingType);

		assert rec2 != null : "Record two should not be null ";
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItem2.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		
        institutionalItemIndexProcessingRecordDAO.makeTransient(institutionalItemIndexProcessingRecordDAO.getById(rec1.getId(),false));
        institutionalItemIndexProcessingRecordDAO.makeTransient(institutionalItemIndexProcessingRecordDAO.getById(rec2.getId(),false));
        indexProcessingTypeDAO.makeTransient(indexProcessingTypeDAO.getById(indexProcessingType .getId(), false));

		
		
		repoHelper.cleanUpRepository();
		
		tm.commit(ts);	
		
	
	}

}
