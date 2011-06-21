package edu.ur.hibernate.ir.user.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;


import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeDAO;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecord;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordDAO;



/**
 * Test the persistence methods for a user workspace index processing record.
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class UserWorkspaceIndexProcessingRecordDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** index porcessing type data access object */
	IndexProcessingTypeDAO indexProcessingTypeDAO = (IndexProcessingTypeDAO) ctx
	.getBean("indexProcessingTypeDAO");
	
	/** institutional item index processing record data access object */
	UserWorkspaceIndexProcessingRecordDAO userWorkspaceIndexProcessingRecordDAO = (UserWorkspaceIndexProcessingRecordDAO) ctx
	.getBean("userWorkspaceIndexProcessingRecordDAO");;
	
	/** platform transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    

	
	/**
	 * Test Institutional item processing record persistence
	 * @throws LocationAlreadyExistsException 
	 * @throws IllegalFileSystemNameException 
	 * @throws DuplicateNameException 
	 */
	@Test
	public void baseInstitutionalItemIndexProcessingRecordTest() throws LocationAlreadyExistsException, IllegalFileSystemNameException, DuplicateNameException
	{
		TransactionStatus ts = tm.getTransaction(td);
		
		IndexProcessingType indexProcessingType = new IndexProcessingType("ipName");
 		indexProcessingType.setDescription("description");
 		indexProcessingTypeDAO.makePersistent(indexProcessingType);
	
		// save the data
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);
		Long userId = 55l;
		MyTestFileSystem myTestFileSystem = new MyTestFileSystem();
		UserWorkspaceIndexProcessingRecord record = new UserWorkspaceIndexProcessingRecord(myTestFileSystem, indexProcessingType, userId);
		userWorkspaceIndexProcessingRecordDAO.makePersistent(record);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		UserWorkspaceIndexProcessingRecord other = userWorkspaceIndexProcessingRecordDAO.getById(record.getId(), false);
        assert other != null : "Should be able to find other with id from record " + record;
		assert other.equals(record) : "other : " + other + " should equal record " + record;
		
		assert userWorkspaceIndexProcessingRecordDAO.getCount() == 1l : " Count should be 1 but is : " 
			+ userWorkspaceIndexProcessingRecordDAO.getCount();
		
		other = userWorkspaceIndexProcessingRecordDAO.get(myTestFileSystem, userId, indexProcessingType);
		assert other != null : "Should be able to find other with parameters user Id = " 
			+ userId + " processingType = " + indexProcessingType;
		
		assert other.equals(record) : "other : " + other + " should equal record " + record;
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		userWorkspaceIndexProcessingRecordDAO.makeTransient(userWorkspaceIndexProcessingRecordDAO.getById(record.getId(), false));
        indexProcessingTypeDAO.makeTransient(indexProcessingTypeDAO.getById(indexProcessingType.getId(), false));
        tm.commit(ts);

	}
	
	/**
	 * Simple class for testing
	 * @author Nathan Sarr
	 *
	 */
	class MyTestFileSystem implements FileSystem
	{
		
		/** eclipse generated test*/
		private static final long serialVersionUID = 8066616160887516079L;


		public FileSystemType getFileSystemType() {
			return FileSystemType.FILE;
		}

		public String getPath() {
			return "MyTestFileSystem - path";
		}

		public Long getId() {
			return 25l;
		}

		public int getVersion() {
			return 0;
		}

		public String getDescription() {
			return "MyTestFileSystem - description";
		}

		
		public String getName() {
			return "MyTestFileSystem - name";
		}
		
	}
	


}
