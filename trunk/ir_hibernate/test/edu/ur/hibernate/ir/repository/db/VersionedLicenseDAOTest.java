package edu.ur.hibernate.ir.repository.db;

import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.ir.repository.License;
import edu.ur.ir.repository.VersionedLicense;
import edu.ur.ir.repository.VersionedLicenseDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;

/**
 * Test for a versioned license.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class VersionedLicenseDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/** transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");

    /** user data access  */
    VersionedLicenseDAO versionedLicenseDAO= (VersionedLicenseDAO) ctx.getBean("versionedLicenseDAO");

    /**
     * Test basic versioned license persistence 
     */
    public void basicVersionedLicenseTest()
    {
		TransactionStatus ts = tm.getTransaction(td);
    	
		UserEmail userEmail = new UserEmail("email");
    	
		// create a user add them to license
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		userDAO.makePersistent(user);
		
		License license = new License("license1", "this is the license", user);
		
		VersionedLicense versionedLicense = new VersionedLicense(user, "this is license text", "Main License");
		versionedLicenseDAO.makePersistent(versionedLicense);
		
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		VersionedLicense other = versionedLicenseDAO.getById(versionedLicense.getId(), false);
		assert other != null : "Should be able to find other";
		assert other.equals(versionedLicense) : "Should be equal other = " + other + " license = " + license;
		
		other = versionedLicenseDAO.findByUniqueName(versionedLicense.getName());
		assert other.equals(versionedLicense) : "Should be equal other = " + other + " license = " + license;
		tm.commit(ts);

		ts = tm.getTransaction(td);
		versionedLicenseDAO.makeTransient(versionedLicenseDAO.getById(versionedLicense.getId(), false));
		tm.commit(ts);

		ts = tm.getTransaction(td);
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		assert versionedLicenseDAO.getById(versionedLicense.getId(), false) == null : "Should not be able to find the versioned license " + versionedLicense;
		tm.commit(ts);
    }

}
