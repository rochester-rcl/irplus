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


package edu.ur.ir.institution.service;

import java.util.HashSet;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSecurityService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupService;

/**
 * Test the service methods for the default repository security services.
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultInstitutionalCollectionSecurityServiceTest {
	
	/** Spring application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
    /** Repository service  */
    RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
    
    /** Repository security service  */
    InstitutionalCollectionSecurityService institutionalCollectionSecurityService = 
    	(InstitutionalCollectionSecurityService) ctx.getBean("institutionalCollectionSecurityService");
   
    /** Repository security service  */
    InstitutionalCollectionService institutionalCollectionService = 
    	(InstitutionalCollectionService) ctx.getBean("institutionalCollectionService");
    
    /** user group service  */
    UserGroupService userGroupService = 
    	(UserGroupService) ctx.getBean("userGroupService");

    
	/** Base Security data access */
	SecurityService securityService = (SecurityService) ctx
	.getBean("securityService");
	
	/** transaction manager for dealing with transactions  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	/** the transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	

	
	/**
	 * Test creating the default groups
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void assignParentAdminPermissionsTest() throws DuplicateNameException, LocationAlreadyExistsException
	{
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		
	 
		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		InstitutionalCollection child = collection.createChild("child1");
		InstitutionalCollection subChild = child.createChild("subChild");
		institutionalCollectionService.saveCollection(collection);
		
		IrUserGroup userGroup = new IrUserGroup("DEFAULT_ADMIN_GROUP");
		userGroupService.save(userGroup);
		
		IrClassTypePermission viewPermission = 
			securityService.getPermissionForClass(collection, 
				InstitutionalCollectionSecurityService.VIEW_PERMISSION.getPermission());
			
		IrClassTypePermission adminPermission = 
			securityService.getPermissionForClass(collection, 
					InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION.getPermission());

		IrClassTypePermission directSubmitPermission = 
			securityService.getPermissionForClass(collection, 
					InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION.getPermission());
		
		IrClassTypePermission reviewerPermission = 
			securityService.getPermissionForClass(collection, 
					InstitutionalCollectionSecurityService.REVIEWER_PERMISSION.getPermission());
		
		HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.add(viewPermission);
		permissions.add(adminPermission);
		permissions.add(directSubmitPermission);
		permissions.add(reviewerPermission);
		
		securityService.createPermissions(collection, userGroup, permissions);
		institutionalCollectionSecurityService.giveAdminPermissionsToParentCollections(child);
		institutionalCollectionSecurityService.giveAdminPermissionsToParentCollections(subChild);
		tm.commit(ts);
		
 	    // Start the transaction 
		ts = tm.getTransaction(td);
		IrAcl subChildAcl = securityService.getAcl(subChild);
		
		collection = institutionalCollectionService.getCollection(collection.getId(), false);
		userGroup = userGroupService.get("DEFAULT_ADMIN_GROUP");
		assert userGroup != null : "Should be able to find " +
		"DEFAULT_ADMIN_GROUP";
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION.getPermission(), 
				userGroup, false) : "Should have admin privileges";
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION.getPermission(), 
				userGroup, false): "Should have direct submit privileges";
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.REVIEWER_PERMISSION.getPermission(), 
				userGroup, false): "Should have direct reviewer privileges";
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.VIEW_PERMISSION.getPermission(), 
				userGroup, false): "Should have view privileges";
	
		
		child = institutionalCollectionService.getCollection(child.getId(), false);
		
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION.getPermission(), 
				userGroup, false) : "Should have admin privileges";
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION.getPermission(), 
				userGroup, false): "Should have direct submit privileges";
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.REVIEWER_PERMISSION.getPermission(), 
				userGroup, false): "Should have direct reviewer privileges";
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.VIEW_PERMISSION.getPermission(), 
				userGroup, false): "Should have view privileges";
		
		
		tm.commit(ts);
		
		
		
		
		ts = tm.getTransaction(td);
		
		securityService.deleteAcl(collection.getId(), collection.getClass().getName());
		securityService.deleteAcl(child.getId(), child.getClass().getName());
		securityService.deleteAcl(subChild.getId(), subChild.getClass().getName());
		userGroupService.delete(userGroupService.get(userGroup.getId(), false));
		helper.cleanUpRepository();
		tm.commit(ts);
	}
	
	/**
	 * Test creating the default groups
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void assignChildAdminPermissionsTest() throws DuplicateNameException, LocationAlreadyExistsException
	{
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		
		
	 
		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		InstitutionalCollection child = collection.createChild("child1");
		InstitutionalCollection subChild = child.createChild("subChild");
		institutionalCollectionService.saveCollection(collection);
		
		IrUserGroup userGroup = new IrUserGroup("DEFAULT_ADMIN_GROUP");
		userGroupService.save(userGroup);
		
		IrClassTypePermission viewPermission = 
			institutionalCollectionSecurityService.getClassTypePermission(InstitutionalCollectionSecurityService.VIEW_PERMISSION);
		
		assert viewPermission != null : "Should be able to find view permission";
			
		IrClassTypePermission adminPermission = 
			securityService.getPermissionForClass(collection, 
					InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION.getPermission());

		IrClassTypePermission directSubmitPermission = 
			securityService.getPermissionForClass(collection, 
					InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION.getPermission());
		
		IrClassTypePermission reviewerPermission = 
			securityService.getPermissionForClass(collection, 
					InstitutionalCollectionSecurityService.REVIEWER_PERMISSION.getPermission());
		
		HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.add(viewPermission);
		permissions.add(adminPermission);
		permissions.add(directSubmitPermission);
		permissions.add(reviewerPermission);
		
		securityService.createPermissions(collection, userGroup, permissions);
		institutionalCollectionSecurityService.giveAdminPermissionsToChildCollections(userGroup, collection);
		tm.commit(ts);
		
 	    // Start the transaction 
		ts = tm.getTransaction(td);
		IrAcl subChildAcl = securityService.getAcl(subChild);
		
		collection = institutionalCollectionService.getCollection(collection.getId(), false);
		userGroup = userGroupService.get("DEFAULT_ADMIN_GROUP");
		assert userGroup != null : "Should be able to find " +
		"DEFAULT_ADMIN_GROUP";
		
		
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION.getPermission(), 
				userGroup, false) : "Should have admin privileges";
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION.getPermission(), 
				userGroup, false): "Should have direct submit privileges";
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.REVIEWER_PERMISSION.getPermission(), 
				userGroup, false): "Should have direct reviewer privileges";
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.VIEW_PERMISSION.getPermission(), 
				userGroup, false): "Should have view privileges";
	
		
		child = institutionalCollectionService.getCollection(child.getId(), false);
		
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION.getPermission(), 
				userGroup, false) : "Should have admin privileges";
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION.getPermission(), 
				userGroup, false): "Should have direct submit privileges";
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.REVIEWER_PERMISSION.getPermission(), 
				userGroup, false): "Should have direct reviewer privileges";
		
		assert subChildAcl.isGranted(InstitutionalCollectionSecurityService.VIEW_PERMISSION.getPermission(), 
				userGroup, false): "Should have view privileges";
		
		
		tm.commit(ts);

		
		ts = tm.getTransaction(td);
		securityService.deleteAcl(collection.getId(), collection.getClass().getName());
		securityService.deleteAcl(child.getId(), child.getClass().getName());
		securityService.deleteAcl(subChild.getId(), subChild.getClass().getName());
		userGroupService.delete(userGroupService.get(userGroup.getId(), false));
		helper.cleanUpRepository();
		tm.commit(ts);
	}

}
