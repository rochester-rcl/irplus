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
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.security.IrClassTypeDAO;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrClassTypePermissionDAO;
import edu.ur.ir.user.FolderInviteInfo;
import edu.ur.ir.user.FolderInviteInfoDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserManager;

/**
 * Allows for basic folder invite info data access to be
 * tested.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class FolderInviteInfoDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
	
     IrUserDAO userDAO= (IrUserDAO) ctx
     	.getBean("irUserDAO");

     FolderInviteInfoDAO folderInviteInfoDAO= (FolderInviteInfoDAO) ctx
 		.getBean("folderInviteInfoDAO");
     
     VersionedFileDAO versionedFileDAO= (VersionedFileDAO) ctx
		.getBean("versionedFileDAO");
     
     IrClassTypeDAO irClassTypeDAO= (IrClassTypeDAO) ctx
		.getBean("irClassTypeDAO");
     
     IrClassTypePermissionDAO irClassTypePermissionDAO= (IrClassTypePermissionDAO) ctx
		.getBean("irClassTypePermissionDAO");

 	/** Properties file with testing specific information. */
 	PropertiesLoader propertiesLoader = new PropertiesLoader();
 	
 	/** Get the properties file  */
 	Properties properties = propertiesLoader.getProperties();


	/** Unique name generator  */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) 
	ctx.getBean("uniqueNameGenerator");
	
    /** IrFile relational data access   */
    IrFileDAO fileDAO= (IrFileDAO) ctx
	.getBean("irFileDAO");
     
	/**
	 * Test PersonName persistance
	 */
	@Test
	public void baseInviteUserTokenDAOTest()throws Exception{

		TransactionStatus ts = tm.getTransaction(td);
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		PersonalFolder folder = user.createRootFolder("my folder");
		userDAO.makePersistent(user);
		
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);
		user = userDAO.getById(user.getId(), false);
		IrClassTypePermission permission 
			= irClassTypePermissionDAO.getClassTypePermissionByNameAndClassType("edu.ur.ir.file.VersionedFile", "VIEW");

		Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.add(permission);
		FolderInviteInfo folderInviteInfo = new FolderInviteInfo(user.getRootFolder("my folder"), "test@mail.com", permissions);
		

		folderInviteInfoDAO.makePersistent(folderInviteInfo);
		
		//complete the transaction
		tm.commit(ts);
        
		ts = tm.getTransaction(td);
		FolderInviteInfo other = folderInviteInfoDAO.getById(folderInviteInfo.getId(), false);
		assert other.equals(folderInviteInfo) : "The user inviteInfo information should be equal";
		assert folderInviteInfo.getEmail() == "test@mail.com" : "Email should be equal";
		assert folderInviteInfo.getPermissions().contains(permission) : "Permissions should exit";
		assert folderInviteInfo.getPersonalFolder().equals(folder) : "Folders should be equal";
		List<FolderInviteInfo> invites = folderInviteInfoDAO.getInviteInfoByEmail("TeSt@Mail.com");
		assert invites.size() == 1 : "Should find one invite but found " + invites.size();
		assert invites.get(0).equals(folderInviteInfo) : "invite " + invites.get(0) + " should equal " + folderInviteInfo;
		
		
		invites = folderInviteInfoDAO.getInviteInfoByEmail("test@mail.com");
		assert invites.size() == 1 : "Should find one invite but found " + invites.size();
		assert invites.get(0).equals(folderInviteInfo) : "invite " + invites.get(0) + " should equal " + folderInviteInfo;
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		folderInviteInfoDAO.makeTransient(folderInviteInfoDAO.getById(other.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert folderInviteInfoDAO.getById(other.getId(), false) == null : "Should not be able to find other";
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
		
		
	}
	

}
