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
	
	/**  Batch size for processing the records  */
	public static final int DEFAULT_BATCH_SIZE = 25;
	
	
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
		
		int batchSize = jobDetail.getJobDataMap().getInt("batchSize");
		if(batchSize <= 0)
		{
		    batchSize = DEFAULT_BATCH_SIZE;  	
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
			    applicationContext.getBean("downloadStatisticsService");
			errorEmailService = (ErrorEmailService)applicationContext.getBean("errorEmailService");
			tm = (PlatformTransactionManager) applicationContext.getBean("transactionManager");
			td = new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_REQUIRED);
		}
		catch(BeansException e1)
		{
		    throw new JobExecutionException("Unable to retrieve target bean that is to be used as a job source", e1);
		}
		
		
		TransactionStatus ts = null;
		try
		{
			ts = tm.getTransaction(td);
			downloadStatisticsService.removeIgnoreCountsFromDownloadInfo(batchSize);
			downloadStatisticsService.removeOkCountsFromIgnoreDownloadInfo(batchSize);
			downloadStatisticsService.updateAllRepositoryFileRollUpCounts();
		}
		catch(Exception e)
		{
			errorEmailService.sendError(e);
			throw new JobExecutionException("Problem preparing records for processing");
		}
		finally
		{
			if( ts != null )
			{
				if( tm != null )
				{
				    tm.commit(ts);
				}
			}
		}
		
		try
		{
			List<FileDownloadRollUpProcessingRecord> records = new LinkedList<FileDownloadRollUpProcessingRecord>();
			do
			{
				ts = tm.getTransaction(td);
			    records =  downloadStatisticsService.getDownloadRollUpProcessingRecords(0, batchSize);
			    log.debug("processing " + records.size() + " records ");
			    for( FileDownloadRollUpProcessingRecord record : records )
			    {
				     Long irFileId = record.getIrFileId();
				     Long downloadCount = downloadStatisticsService.getNumberOfFileDownloadsForIrFile(irFileId);
				     downloadStatisticsService.updateRollUpCount(irFileId, downloadCount);
				     downloadStatisticsService.delete(record);
			    }
			    tm.commit(ts);
			}
			while(records.size() > 0);
		}
		catch(Exception e)
		{
			errorEmailService.sendError(e);
		}
		finally
		{
			if( ts != null && !ts.isCompleted())
			{
				if( tm != null)
				{
				    tm.commit(ts);
				}
			}
				
		}
	}

}

