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

import edu.ur.ir.file.IrFile;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.ir.statistics.FileDownloadInfo;
import edu.ur.ir.statistics.FileDownloadRollUpProcessingRecord;
import edu.ur.ir.statistics.IpIgnoreFileDownloadInfo;

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
	public static final int DEFAULT_BATCH_SIZE = 1000;
	
	
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
		  
		DownloadStatisticsService downloadStatisticsService = null;
		ErrorEmailService errorEmailService;
		RepositoryService repositoryService = null;
		

		PlatformTransactionManager tm = null;
		TransactionDefinition td = null;
		try
		{
			downloadStatisticsService = (DownloadStatisticsService)
			    applicationContext.getBean("downloadStatisticsService");
			errorEmailService = (ErrorEmailService)applicationContext.getBean("errorEmailService");
			repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");
			
			tm = (PlatformTransactionManager) applicationContext.getBean("transactionManager");
			td = new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_REQUIRED);
		}
		catch(BeansException e1)
		{
		    throw new JobExecutionException("Unable to retrieve target bean that is to be used as a job source", e1);
		}
		
		
		removeIgnoreCountsFromDownloadInfo(errorEmailService, tm, td, downloadStatisticsService, batchSize);
		removeOkCountsFromIgnoreDownloadInfo(errorEmailService, tm, td, downloadStatisticsService, batchSize);
		updateAllRepositoryCounts(errorEmailService, tm, td, downloadStatisticsService);
		
		TransactionStatus ts = null;
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
				     IrFile irFile = repositoryService.getIrFile(irFileId, false);
				     irFile.setDownloadCount(downloadCount);
				     repositoryService.save(irFile);
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
	
	/**
	 * @param errorEmailService
	 * @param tm
	 * @param td
	 * @param downloadStatisticsService
	 * @param batchSize
	 * @return
	 * @throws JobExecutionException
	 */
	private int removeIgnoreCountsFromDownloadInfo(ErrorEmailService errorEmailService,
			PlatformTransactionManager tm,
			TransactionDefinition td,
			DownloadStatisticsService downloadStatisticsService,
			int batchSize) throws JobExecutionException
	{
		int start = 0;
		int totalProcessed = 0;
		TransactionStatus ts = null;
		try
		{
		    List<FileDownloadInfo> infos = new LinkedList<FileDownloadInfo>();
		    do
		    {
		    	ts = tm.getTransaction(td);
			    if(log.isDebugEnabled())
			    {
				    log.debug("removeIgnoreCountsFromDownloadInfo total processed = " + totalProcessed + " batch Size = " + batchSize);
			    }
			    infos = downloadStatisticsService.getIgnoreCountsFromDownloadInfo(start, batchSize);
			    log.debug("infos size = " + infos.size());
			    for( FileDownloadInfo info : infos)
			    {
				    totalProcessed = totalProcessed + 1;
				    log.debug("Processing info record " + info);
				    IpIgnoreFileDownloadInfo ignoreRecord = downloadStatisticsService.getIpIgnoreFileDownloadInfo(info.getIpAddress(), info.getIrFileId(), info.getDownloadDate());
				    if( ignoreRecord == null )
				    {
					    ignoreRecord = new IpIgnoreFileDownloadInfo(info.getIpAddress(), info.getIrFileId(), info.getDownloadDate());
					    ignoreRecord.setDownloadCount(info.getDownloadCount());
				    }
				    else
				    {
					    ignoreRecord.setDownloadCount(ignoreRecord.getDownloadCount() + info.getDownloadCount());
				    }
				    downloadStatisticsService.save(ignoreRecord);
				    downloadStatisticsService.delete(info);
			    }
			    tm.commit(ts);
			
		    }while(infos.size() > 0 );
		}
		catch(Exception e)
		{
			errorEmailService.sendError(e);
			throw new JobExecutionException("Problem preparing records for processing");
		}
		finally
		{
			if( ts != null && !ts.isCompleted())
			{
				if( tm != null )
				{
				    tm.commit(ts);
				}
			}
		}
		
		return new Integer(totalProcessed);
	}
	
	/**
	 * Remove all ok counts from ingnore download info.
	 * 
	 * @param errorEmailService
	 * @param tm
	 * @param td
	 * @param downloadStatisticsService
	 * @param batchSize
	 * @return
	 * @throws JobExecutionException
	 */
	private int removeOkCountsFromIgnoreDownloadInfo(ErrorEmailService errorEmailService,
			PlatformTransactionManager tm,
			TransactionDefinition td,
			DownloadStatisticsService downloadStatisticsService,
			int batchSize) throws JobExecutionException
	{
		int start = 0;
		int totalProcessed = 0;
		TransactionStatus ts = null;
		try
		{
			List<IpIgnoreFileDownloadInfo> okCounts = new LinkedList<IpIgnoreFileDownloadInfo>();
		    do
		    {
		    	ts = tm.getTransaction(td);
		    	okCounts = downloadStatisticsService.getIgnoreInfoNowAcceptable(start, batchSize );
		    	if(log.isDebugEnabled())
				{
					log.debug(" removeOkCountsFromIgnoreDownloadInfo total processed = " + totalProcessed + " batch Size = " + batchSize);
				}
				for( IpIgnoreFileDownloadInfo okInfo : okCounts)
			    {
			    	totalProcessed = totalProcessed + 1;
			    	log.debug("processing ignore info record " + okInfo);
			    	FileDownloadInfo downloadRecord = downloadStatisticsService.getFileDownloadInfo(okInfo.getIpAddress(), okInfo.getIrFileId(), okInfo.getDownloadDate());
			    	if( downloadRecord == null )
			    	{
			    		downloadRecord = new FileDownloadInfo(okInfo.getIpAddress(), okInfo.getIrFileId(), okInfo.getDownloadDate());
			    		downloadRecord.setDownloadCount(okInfo.getDownloadCount());
			    	}
			    	else
			    	{
			    		downloadRecord.setDownloadCount(okInfo.getDownloadCount() + downloadRecord.getDownloadCount());
			    	}
			    	downloadStatisticsService.save(downloadRecord);
			    	downloadStatisticsService.delete(okInfo);
			    }
			    tm.commit(ts);
			
		    }while(okCounts.size() > 0 );
		}
		catch(Exception e)
		{
			errorEmailService.sendError(e);
			throw new JobExecutionException("Problem preparing records for processing");
		}
		finally
		{
			if( ts != null && !ts.isCompleted())
			{
				if( tm != null )
				{
				    tm.commit(ts);
				}
			}
		}
		
		return new Integer(totalProcessed);
	}
	
	/**
	 * Update all repository counts to be re-counted.
	 * 
	 * @param errorEmailService
	 * @param tm
	 * @param td
	 * @param downloadStatisticsService
	 * @throws JobExecutionException
	 */
	private void updateAllRepositoryCounts(ErrorEmailService errorEmailService,
			PlatformTransactionManager tm,
			TransactionDefinition td,
			DownloadStatisticsService downloadStatisticsService) throws JobExecutionException
	{
		TransactionStatus ts = null;
		try
		{
			ts = tm.getTransaction(td);
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

	}

}

