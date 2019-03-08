package edu.ur.ir.repository.service;

import java.util.GregorianCalendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import edu.ur.file.db.ChecksumCheckerService;
import edu.ur.file.db.FileInfoChecksum;
import edu.ur.file.db.FileInfoChecksumService;
import edu.ur.ir.ErrorEmailService;

/**
 * Job set up to run and check checksums of files.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultChecksumCheckerJob implements StatefulJob{

	/** Application context from spring  */
	public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
	 
	/**  Get the logger for this class */
	private static final Logger log = LogManager.getLogger(DefaultChecksumCheckerJob.class);
	
	/**
	 * Exceuction of the job
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException 
	{
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
		  
		ChecksumCheckerService checksumCheckerService = null;
		FileInfoChecksumService fileInfoChecksumService = null;
		ErrorEmailService errorEmailService;
		PlatformTransactionManager tm = null;
		TransactionDefinition td = null;
		try
		{
			checksumCheckerService = (ChecksumCheckerService) applicationContext.getBean("checksumCheckerService");
			fileInfoChecksumService = (FileInfoChecksumService) applicationContext.getBean("fileInfoChecksumService");
			errorEmailService = (ErrorEmailService)applicationContext.getBean("errorEmailService");

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
		    
		    GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(GregorianCalendar.DAY_OF_YEAR, -5);
			
			if (log.isDebugEnabled()) 
			{
				log.debug("getting checksums for before date " +  calendar.getTime());
			}
			List<FileInfoChecksum> checksums = fileInfoChecksumService.getOldestChecksumsForChecker(0, 1, calendar.getTime());
			
			
			if (log.isDebugEnabled()) 
			{
				log.debug("found  " +  checksums.size() + " checksums");
			}
			
			for(FileInfoChecksum checksum : checksums)
			{
				if (log.isDebugEnabled()) 
				{
					log.debug("processing checksum for  " +  checksum);
				}
				checksumCheckerService.checkChecksum(checksum);
				
				if(!checksum.getReCalculatedPassed()){
					
				}
			}
			tm.commit(ts);
		}
		catch(Exception e)
		{
			errorEmailService.sendError(e);
		}
		finally
		{
			if( ts != null && !ts.isCompleted() )
			{
				if( tm != null )
				{
		            tm.commit(ts);
				}
			}
		}
	}


}
