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
import edu.ur.ir.repository.LicenseDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;

/**
 * Test for license class.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class LicenseDAOTest {
	
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
    LicenseDAO licenseDAO= (LicenseDAO) ctx.getBean("licenseDAO");

    
    /**
     * Test basic license persistence 
     */
    public void basicLicenseTest()
    {
		TransactionStatus ts = tm.getTransaction(td);
    	
		UserEmail userEmail = new UserEmail("email");
    	
		// create a user add them to license
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		userDAO.makePersistent(user);
		
		License license = new License("license1", "this is the license", user);
		licenseDAO.makePersistent(license);
		
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		License other = licenseDAO.getById(license.getId(), false);
		assert other != null : "Should be able to find other";
		assert other.equals(license) : "Should be equal other = " + other + " license = " + license;
		tm.commit(ts);

		ts = tm.getTransaction(td);
		licenseDAO.makeTransient(licenseDAO.getById(license.getId(), false));
		tm.commit(ts);

		ts = tm.getTransaction(td);
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		assert licenseDAO.getById(license.getId(), false) == null : "Should not be able to find the license " + license;
		tm.commit(ts);
    }


}
