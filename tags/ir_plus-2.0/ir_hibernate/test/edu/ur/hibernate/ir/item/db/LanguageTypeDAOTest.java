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

package edu.ur.hibernate.ir.item.db;

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

/**
 * Test the persistence methods for language type Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class LanguageTypeDAOTest {
	
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

    /** Language type data access */
    LanguageTypeDAO languageTypeDAO = (LanguageTypeDAO) ctx
	.getBean("languageTypeDAO");
    
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

     
	/**
	 * Test language type persistance
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseLanguageTypeDAOTest() throws Exception{
 		LanguageType lt = new LanguageType();
		lt.setName("languageName");
 		lt.setDescription("languageDescription");
         
	    TransactionStatus ts = tm.getTransaction(td);
        languageTypeDAO.makePersistent(lt);
        tm.commit(ts);
        
        ts = tm.getTransaction(td);
         LanguageType other = languageTypeDAO.getById(lt.getId(), false);
         assert other.equals(lt) : "Language types should be equal";
         
         List<LanguageType> languageTypes = languageTypeDAO.getAllOrderByName(0, 1);
         assert languageTypes.size() == 1 : "One language type should be found";
         
         LanguageType languageTypeByName = languageTypeDAO.findByUniqueName(lt.getName());
         assert languageTypeByName.equals(lt) : "Language should be found";
         
         languageTypeDAO.makeTransient(other);
         assert languageTypeDAO.getById(other.getId(), false) == null : "Should no longer be able to find language type";
         tm.commit(ts);
	}

}
