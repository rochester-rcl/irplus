/**  
   Copyright 2008 - 2010 University of Rochester

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

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.StatefulJob;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;
import edu.ur.ir.institution.InstitutionalItemIndexService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;

/**
 * Job that processes institutional items and indexes the data for searches
 * 
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultItemIndexProcessingRecordJob implements StatefulJob{

	/** Application context from spring  */
	public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
	 
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultItemIndexProcessingRecordJob.class);

	
	/**
	 * Retrieve the current records to be indexed and then process them.
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
		String beanName = jobDetail.getName();
		  
		if (log.isDebugEnabled()) 
		{
		    log.info ("Running SpringBeanDelegatingJob - Job Name ["+jobDetail.getName()+"], Group Name ["+jobDetail.getGroup()+"]");
		    log.info ("Delegating to bean ["+beanName+"]");
		}
		  
		ApplicationContext applicationContext = null;
		  
		try 
		{
		    applicationContext = (ApplicationContext) context.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);
		} 
		catch (SchedulerException e2) 
		{
		    throw new JobExecutionException("problem with the Scheduler", e2);
		}
		  
		InstitutionalItemIndexProcessingRecordService processingRecordService = null;
		InstitutionalItemIndexService institutionalItemIndexService = null;
		InstitutionalItemService institutionalItemService = null;
		RepositoryService repositoryService = null;
		PlatformTransactionManager tm = null;
		TransactionDefinition td = null;
		try
		{
			processingRecordService = (InstitutionalItemIndexProcessingRecordService)
			    applicationContext.getBean("institutionalItemIndexProcessingRecordService");
			institutionalItemService = (InstitutionalItemService)applicationContext.getBean("institutionalItemService");
			institutionalItemIndexService = (InstitutionalItemIndexService)applicationContext.getBean("institutionalItemIndexService");
			repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");

			
			tm = (PlatformTransactionManager) applicationContext.getBean("transactionManager");
			td = new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_REQUIRED);
		}
		catch(BeansException e1)
		{
		    throw new JobExecutionException("Unable to retrieve target bean that is to be used as a job source", e1);
		}
		
		// start a new transaction
		TransactionStatus ts = null;
		try
		{
		    ts = tm.getTransaction(td);
		    Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		    File f = null;
		    List <InstitutionalItemIndexProcessingRecord> records = new LinkedList<InstitutionalItemIndexProcessingRecord>();
		    if( repository != null )
		    {
		        String indexFolder = repository.getInstitutionalItemIndexFolder();
		        f = new File(indexFolder);
		        records = processingRecordService.getAllOrderByItemIdUpdatedDate();
		    }
		    log.debug("processing " + records.size() + " records ");
		    tm.commit(ts);
	
		    for( InstitutionalItemIndexProcessingRecord record : records )
		    {
			    ts = tm.getTransaction(td);
			    if( record.getIndexProcessingType().getName().equals(IndexProcessingTypeService.DELETE))
	            {
	                log.debug("deleting item  " + record.getInstitutionalItemId());
	        	    institutionalItemIndexService.deleteItem(record.getInstitutionalItemId(), f);
	        	    processingRecordService.delete(record);
	            }
			    else
			    {
		           InstitutionalItem i = institutionalItemService.getInstitutionalItem(record.getInstitutionalItemId(), false);
			
		            if( i != null)
		            {
		       
		                if(record.getIndexProcessingType().getName().equals(IndexProcessingTypeService.UPDATE))
		                {
		                    log.debug("updating item  " + i);
		                    try {
						        institutionalItemIndexService.updateItem(i, f, true);
						        processingRecordService.delete(record);
					        } 
		                    catch (NoIndexFoundException e) 
					        {
						        log.error(e);
					        }
		                }
		                else if(record.getIndexProcessingType().getName().equals(IndexProcessingTypeService.INSERT))
				        {
		        	        try
		        	        {
						        institutionalItemIndexService.addItem(i, f);
						        processingRecordService.delete(record);
					        } 
		        	        catch (NoIndexFoundException e)
		        	        {
						        log.error(e);
				            }     	
				         }
		                 else
		                 {
		        	        log.error("Can't process record type " + record.getIndexProcessingType() + " for item " + i);
		                 }
		            }
		            else
		            {
		            	// item does not exist - delete the record
		            	institutionalItemIndexService.deleteItem(record.getInstitutionalItemId(), f);
		            	processingRecordService.delete(record);
		            }
			    }
		        tm.commit(ts);
		    }
		    if( repository != null )
		    {
		        institutionalItemIndexService.optimize(f);
		    }
		}
		finally
		{
			if( ts != null  && !ts.isCompleted())
			{
				if( tm != null )
				{
				    tm.commit(ts);
				}
				
			}
		}
		
		
	}
		


}
