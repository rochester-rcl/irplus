package edu.ur.ir.statistics.service;

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

import edu.ur.ir.ErrorEmailService;

import edu.ur.ir.statistics.FileDownloadRollUpProcessingRecord;

/**
 * Job to process download roll up requests.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultFileDownloadStatsUpdateJob implements StatefulJob{

	/** Application context from spring  */
	public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
	 
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultFileDownloadStatsUpdateJob.class);
	
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
		  
		DefaultDownloadStatisticsService downloadStatisticsService = null;
		ErrorEmailService errorEmailService;
		

		PlatformTransactionManager tm = null;
		TransactionDefinition td = null;
		try
		{
			downloadStatisticsService = (DefaultDownloadStatisticsService)
			    applicationContext.getBean("institutionalItemIndexProcessingRecordService");
			errorEmailService = (ErrorEmailService)applicationContext.getBean("errorEmailService");
			tm = (PlatformTransactionManager) applicationContext.getBean("transactionManager");
			td = new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_REQUIRED);
		}
		catch(BeansException e1)
		{
		    throw new JobExecutionException("Unable to retrieve target bean that is to be used as a job source", e1);
		}
		
		List<FileDownloadRollUpProcessingRecord> records = new LinkedList<FileDownloadRollUpProcessingRecord>();
		log.debug("processing " + records.size() + " records ");
		
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
	
		for( FileDownloadRollUpProcessingRecord record : records )
		{
			try
			{
			    Long irFileId = record.getIrFileId();
			    downloadStatisticsService.updateRollUpCount(irFileId);
			    downloadStatisticsService.delete(record);
			}
			catch(Exception e)
			{
				errorEmailService.sendError(e);
			}
		}
		
		tm.commit(ts);
		
	}

}

