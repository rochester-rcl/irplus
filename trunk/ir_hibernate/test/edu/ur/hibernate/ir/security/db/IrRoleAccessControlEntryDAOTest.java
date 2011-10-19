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

package edu.ur.hibernate.ir.security.db;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeDAO;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrAclDAO;
import edu.ur.ir.security.IrClassType;
import edu.ur.ir.security.IrClassTypeDAO;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrClassTypePermissionDAO;
import edu.ur.ir.security.IrRoleAccessControlEntry;
import edu.ur.ir.security.IrRoleAccessControlEntryDAO;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrRoleDAO;

/**
 * Test the persistance methods for Role Access Control Entry
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrRoleAccessControlEntryDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
    
    /** Class type data access  */
    IrClassTypeDAO irClassTypeDAO = (IrClassTypeDAO) ctx
    .getBean("irClassTypeDAO");
    
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/** Transaction definition.  */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Language type data access */
    LanguageTypeDAO languageTypeDAO = (LanguageTypeDAO) ctx
	.getBean("languageTypeDAO");
    
	/** Role relational data access */
	IrRoleDAO irRoleDAO = (IrRoleDAO) ctx
	.getBean("irRoleDAO");
	
	/** Class type permission relational data access  */
	IrClassTypePermissionDAO classTypePermissionDAO = (IrClassTypePermissionDAO) 
	ctx.getBean("irClassTypePermissionDAO");
	
    /** Role access control entry data access */
    IrRoleAccessControlEntryDAO raceDAO = (IrRoleAccessControlEntryDAO) ctx
    .getBean("irRoleAccessControlEntryDAO");
    
    /** the Institutional repository acl relational data access */
    IrAclDAO irAclDAO = (IrAclDAO) ctx.getBean("irAclDAO");
    
	/**
	 * Test User Access Control Entry DAO
	 */
	@Test
	public void baseUserControlEntryDAOTest() throws Exception{

	    IrClassType languageTypeClass = new IrClassType(LanguageType.class);
        irClassTypeDAO.makePersistent(languageTypeClass);
	
		LanguageType lt = new LanguageType();
		lt.setName("languageName");
 		lt.setDescription("languageDescription");
        languageTypeDAO.makePersistent(lt);
        
		TransactionStatus ts = tm.getTransaction(td);
		
		IrAcl irAcl = new IrAcl(lt, languageTypeClass);
		IrAclDAO irAclDAO = (IrAclDAO) ctx.getBean("irAclDAO");
		
		irAclDAO.makePersistent(irAcl);
        //complete the transaction
		tm.commit(ts);
	
		IrRole role = new IrRole();
		role.setName("roleName");
 		role.setDescription("roleDescription");
 		
 		irRoleDAO.makePersistent(role);
 		
		// create the user access control entry
		IrRoleAccessControlEntry  race = irAcl.createRoleAccessControlEntry(role);

 	
	    IrClassTypePermission classTypePermission = new IrClassTypePermission(languageTypeClass);
	    classTypePermission.setName("permissionName");
	    classTypePermission.setDescription("permissionDescription");
	    
	    // save the class type permission
	    classTypePermissionDAO.makePersistent(classTypePermission);
	    race.addPermission(classTypePermission);
	    raceDAO.makePersistent(race);    
	    
		// start a new transaction
		ts = tm.getTransaction(td);
		IrRoleAccessControlEntry other = raceDAO.getById( race.getId(), false);
		
		assert other.equals(race) : "User access control entries should be equal";
		assert race.getAcl().equals(irAcl) : "Acl's should be equal";
		assert irAcl.getRoleAccessControlEntry(race.getId()).equals(race) : "Access control should bin in the irAcl";
		assert race.getPermissions().size() == 1 : "Should have at least one permission";
		assert race.getIrClassTypePermissions().contains(classTypePermission) : "Should equal the class type permission";
		assert race.getIrRole().equals(role) : "Users should be equal";
		assert raceDAO.getCount() == 1 : "Should have one race";
		
		//make sure we can get the acl for the role
		List<IrAcl> acls = irAclDAO.getAllAclsForSid(role);
		assert acls.size() == 1 : "Should be able to find 1 acl for role " + role +
		" but found " + acls.size();
		
		// commit the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		// clean up the database
		irAclDAO.makeTransient(irAclDAO.getById(irAcl.getId(), false));
		
		assert raceDAO.getById(race.getId(), false) == null : "Should not be able to find the access control entry";
		
 	    classTypePermissionDAO.makeTransient(classTypePermissionDAO.getById(classTypePermission.getId(), false));
 	    irClassTypeDAO.makeTransient(irClassTypeDAO.getById(languageTypeClass.getId(), false));
 		irRoleDAO.makeTransient(irRoleDAO.getById(role.getId(), false));
 		languageTypeDAO.makeTransient(languageTypeDAO.getById(lt.getId(), false));
 		
 		tm.commit(ts);
	}
}
