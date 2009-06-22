package edu.ur.ir.institution.service;

import java.io.File;
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
		TransactionStatus ts = tm.getTransaction(td);
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
	
		if( repository != null )
		{
			
			List<InstitutionalItemIndexProcessingRecord> records = processingRecordService.getAllOrderByItemIdUpdatedDate();
			String indexFolder = repository.getInstitutionalItemIndexFolder();
			File f = new File(indexFolder);
		    for( InstitutionalItemIndexProcessingRecord record : records )
		    {
		    	InstitutionalItem i = institutionalItemService.getInstitutionalItem(record.getInstitutionalItemId(), false);
		    	if( i != null)
		    	{
		            if( record.getIndexProcessingType().equals(IndexProcessingTypeService.DELETE))
		            {
		        	    log.debug("deleting item  " + i);
		        	    institutionalItemIndexService.deleteItem(i, f);
		            }
		            else if(record.getIndexProcessingType().equals(IndexProcessingTypeService.UPDATE))
		            {
		            	log.debug("updating item  " + i);
		            	try {
							institutionalItemIndexService.updateItem(i, f);
						} catch (NoIndexFoundException e) {
							log.error(e);
						}
		            }
		            else if(record.getIndexProcessingType().equals(IndexProcessingTypeService.INSERT))
				    {
		            	try {
							institutionalItemIndexService.addItem(i, f);
						} catch (NoIndexFoundException e) {
							log.error(e);
						}     	
				    }
		            else
		            {
		            	log.error("Can't process record type " + record.getIndexProcessingType() + " for item " + i);
		            }
		    	}
		    }
		
		    
		}
		tm.commit(ts);
		
	}

}
