package edu.ur.ir.researcher.service;

import java.io.File;

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

import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.ReIndexResearchersService;
import edu.ur.ir.user.service.DefaultReIndexUsersJob;

/**
 * @author Nathan Sarr
 *
 */
public class DefaultReIndexResearchersJob implements StatefulJob{
	

	/** Application context from spring  */
	public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
	 
	/**  Get the logger for this class */
	private static final Logger log = LogManager.getLogger(DefaultReIndexUsersJob.class);
	
	/**  Batch size for processing the records  */
	public static final int DEFAULT_BATCH_SIZE = 25;

	
	/**
	 * Re index all researchers in the system.
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException 
	{
		 JobDetail jobDetail = context.getJobDetail();
		 String beanName = jobDetail.getName();
			
		int batchSize = jobDetail.getJobDataMap().getInt("batchSize");
			  
		if (log.isDebugEnabled()) 
		{
		    log.info ("Running SpringBeanDelegatingJob - Job Name ["+jobDetail.getName()+"], Group Name ["+jobDetail.getGroup()+"]");
		    log.info ("Delegating to bean ["+beanName+"]");
		}
			
		if( batchSize <= 0 )
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
			  
		RepositoryService repositoryService = null;
		ReIndexResearchersService reIndexResearchersService = null;
		ErrorEmailService errorEmailService;
		PlatformTransactionManager tm = null;
		TransactionDefinition td = null;
		try
		{
			repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");
			reIndexResearchersService = (ReIndexResearchersService) applicationContext.getBean("reIndexResearchersService");
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
		    Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
			
		    if( repository != null )
		    {
		 	    log.debug("re indexing resarchers for repository " + repository);
			    repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
			    int count = reIndexResearchersService.reIndexResearchers(batchSize, new File(repository.getResearcherIndexFolder()));
		        log.debug(" Number of researchers indexed = " + count);
		    }
		}
		catch(Exception e)
		{
			errorEmailService.sendError(e);
		}
		finally
		{
			if( ts != null)
			{
				if( tm != null )
				{
		            tm.commit(ts);
				}
			}
		}
		
	}

}
