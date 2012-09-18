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

package edu.ur.hibernate.ir.user.db;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.security.IrClassTypeDAO;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrClassTypePermissionDAO;
import edu.ur.ir.user.FolderAutoShareInfo;
import edu.ur.ir.user.FolderAutoShareInfoDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserManager;

/**
 * Test persistence of auto share information.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class FolderAutoShareInfoDAOTest 
{
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
	
     IrUserDAO userDAO= (IrUserDAO) ctx
     	.getBean("irUserDAO");

     FolderAutoShareInfoDAO folderAutoShareInfoDAO = (FolderAutoShareInfoDAO) ctx
		.getBean("folderAutoShareInfoDAO");
     
     VersionedFileDAO versionedFileDAO = (VersionedFileDAO) ctx
		.getBean("versionedFileDAO");
     
     IrClassTypeDAO irClassTypeDAO= (IrClassTypeDAO) ctx
		.getBean("irClassTypeDAO");
     
     IrClassTypePermissionDAO irClassTypePermissionDAO= (IrClassTypePermissionDAO) ctx
		.getBean("irClassTypePermissionDAO");

 	/** Properties file with testing specific information. */
 	PropertiesLoader propertiesLoader = new PropertiesLoader();
 	
 	/** Get the properties file  */
 	Properties properties = propertiesLoader.getProperties();

     
	/**
	 * Test PersonName persistance
	 */
	@Test
	public void baseFolderAutoShareInfoDAOTest()throws Exception{

		TransactionStatus ts = tm.getTransaction(td);
  		UserManager userManager = new UserManager();
		
  		// create sharing user
  		IrUser sharingUser = userManager.createUser("passowrd", "sharingUserName");
		userDAO.makePersistent(sharingUser);
		
		// create the collaborator
  		IrUser collaborator = userManager.createUser("passowrd", "collaboratorName");
		userDAO.makePersistent(collaborator);
		
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);

	
        // get a class type permission
		IrClassTypePermission permission 
			= irClassTypePermissionDAO.getClassTypePermissionByNameAndClassType("edu.ur.ir.file.VersionedFile", "VIEW");

		// create a root folder
		PersonalFolder folderToAutoShare = sharingUser.createRootFolder("my folder");
		
		Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.add(permission);
		
		FolderAutoShareInfo folderAutoShareInfo = new FolderAutoShareInfo(folderToAutoShare,permissions, collaborator);
		folderAutoShareInfoDAO.makePersistent(folderAutoShareInfo);
		
		//complete the transaction
		tm.commit(ts);
        
		ts = tm.getTransaction(td);
		FolderAutoShareInfo other = folderAutoShareInfoDAO.getById(folderAutoShareInfo.getId(), false);
		assert other.equals(folderAutoShareInfo) : "The folder share info " + other + " should equal " + folderAutoShareInfo;
		assert other.getPermissions().contains(permission) : "The folder share info should contain " + permission;
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		folderAutoShareInfoDAO.makeTransient(folderAutoShareInfoDAO.getById(other.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert folderAutoShareInfoDAO.getById(other.getId(), false) == null : "Should not be able to find other";
		userDAO.makeTransient(userDAO.getById(sharingUser.getId(), false));
		userDAO.makeTransient(userDAO.getById(collaborator.getId(), false));
		tm.commit(ts);
	}
}
