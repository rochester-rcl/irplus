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
import edu.ur.ir.item.Series;
import edu.ur.ir.item.SeriesDAO;

/**
 * Test the persistence methods for a series
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class SeriesDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	SeriesDAO seriesDAO = (SeriesDAO) ctx
	.getBean("seriesDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test Series persistence
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseSeriesTypeDAOTest() throws Exception{

		Series series = new Series("seriesName", "10001");
		series.setDescription("seriesDescription");

         
        TransactionStatus ts = tm.getTransaction(td);

 		 seriesDAO.makePersistent(series);
 		 Series other = seriesDAO.getById(series.getId(), false);
         assert other.equals(series) : "Seriess should be equal";
         
         List<Series> series1 =  seriesDAO.getAllOrderByName(0, 1);
         assert series1.size() == 1 : "One series should be found";
         
         assert seriesDAO.getAllNameOrder().size() == 1 : "One series should be found";
         
         Series seriesByName =  seriesDAO.findByUniqueName(series.getName());
        
         assert seriesByName.equals(series) : "Series should be found";
         
         assert seriesDAO.getAll().size() == 1 :"Should be 1";
         
         assert seriesDAO.getAll().contains(series) == true :"Should be true";
         
         seriesDAO.makeTransient(other);
         assert  seriesDAO.getById(other.getId(), false) == null : 
        	 "Should no longer be able to find series";
         
         
        
         tm.commit(ts);
	}
}
