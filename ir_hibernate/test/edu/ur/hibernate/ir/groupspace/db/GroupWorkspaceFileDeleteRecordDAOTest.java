/**  
   Copyright 2008 - 2011 University of Rochester

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

package edu.ur.hibernate.ir.groupspace.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.groupspace.GroupWorkspaceFileDeleteRecord;
import edu.ur.ir.groupspace.GroupWorkspaceFileDeleteRecordDAO;

/**
 * Test for createing group workspace file delete records.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class GroupWorkspaceFileDeleteRecordDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	GroupWorkspaceFileDeleteRecordDAO groupWorkspaceFileDeleteRecordDAO = 
		(GroupWorkspaceFileDeleteRecordDAO)ctx.getBean("groupWorkspaceFileDeleteRecordDAO");
	
	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test personal file delete record persistence
	 */
	@Test
	public void baseGroupWorkspaceFileDeleteRecordDAOTest() throws Exception{

        TransactionStatus ts = tm.getTransaction(td);
        
        GroupWorkspaceFileDeleteRecord groupWrokspaceDeleteRecord = new GroupWorkspaceFileDeleteRecord(22l, 44l, 9l, "groupName", "/my/path/tofile/file.txt", "the file description");
	
        groupWorkspaceFileDeleteRecordDAO.makePersistent(groupWrokspaceDeleteRecord);
 		tm.commit(ts);
 		GroupWorkspaceFileDeleteRecord other = groupWorkspaceFileDeleteRecordDAO.getById(groupWrokspaceDeleteRecord.getId(), false);
        assert other.equals(groupWrokspaceDeleteRecord) : "group workspace files should be equal";
         
        assert groupWorkspaceFileDeleteRecordDAO.getCount() == 1 : "Should find one record but found " +  groupWorkspaceFileDeleteRecordDAO.getCount();
        groupWorkspaceFileDeleteRecordDAO.makeTransient(other);
        assert  groupWorkspaceFileDeleteRecordDAO.getById(other.getId(), false) == null : "Should no longer be able to find personal file";
	}


}
