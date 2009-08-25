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

package edu.ur.hibernate.ir.statistics.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.person.ContributorDAO;
import edu.ur.ir.person.ContributorTypeDAO;
import edu.ur.ir.person.PersonNameAuthorityDAO;
import edu.ur.ir.statistics.FileDownloadInfo;
import edu.ur.ir.statistics.FileDownloadInfoDAO;
import edu.ur.ir.statistics.IgnoreIpAddressDAO;
import edu.ur.ir.user.IrUserDAO;


/**
 * Test file download info persistance
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class FileDownloadInfoDAOTest {

	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	FileDownloadInfoDAO fileDownloadInfoDAO = (FileDownloadInfoDAO) ctx
	.getBean("fileDownloadInfoDAO");
	
	/** persistance for dealing with ignoring ip addesses */
	IgnoreIpAddressDAO ignoreIpAddressDAO = (IgnoreIpAddressDAO) ctx
	.getBean("ignoreIpAddressDAO");
	
	/** persistance for dealing with contributor Type*/
	ContributorTypeDAO contributorTypeDAO = (ContributorTypeDAO) ctx
	.getBean("contributorTypeDAO");
	
	/** persistance for dealing with contributor */
	ContributorDAO contributorDAO = (ContributorDAO) ctx
	.getBean("contributorDAO");
	
	PersonNameAuthorityDAO personNameAuthorityDAO = (PersonNameAuthorityDAO) ctx.getBean("personNameAuthorityDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);

	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	 /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");
    
    /** ir file data access */
    IrFileDAO irFileDAO = 
    	(IrFileDAO) ctx.getBean("irFileDAO");
    
    /** Generic item data access */
    GenericItemDAO itemDAO = (GenericItemDAO) ctx.getBean("itemDAO");
    
	/** Institution collection data access.  */
	InstitutionalCollectionDAO institutionalCollectionDAO = (InstitutionalCollectionDAO) ctx
	.getBean("institutionalCollectionDAO");
    
	/**
	 * Test download info persistance
	 */
	@Test
	public void baseDownloadInfoDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);

	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy");
	    Date d = simpleDateFormat.parse("1/1/2008");
        FileDownloadInfo downloadInfo1 = new FileDownloadInfo("123.0.0.1", 1l,d);
        downloadInfo1.setDownloadCount(1);
        
        fileDownloadInfoDAO.makePersistent(downloadInfo1);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		FileDownloadInfo other = fileDownloadInfoDAO.getById(downloadInfo1.getId(), false);
        assert other.equals(downloadInfo1) : "File download info should be equal other = \n" + other + "\n downloadInfo1 = " + downloadInfo1;
         
        fileDownloadInfoDAO.makeTransient(other);
        assert  fileDownloadInfoDAO.getById(other.getId(), false) == null : "Should no longer be able to find file download info";
	    tm.commit(ts);
	}
	

	


}
