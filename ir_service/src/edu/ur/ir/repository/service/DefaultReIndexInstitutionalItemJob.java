package edu.ur.ir.repository.service;

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

import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryIndexerService;
import edu.ur.ir.repository.RepositoryService;

/**
 * This re-indexes all current institutional items. This is a stateful job
 * so only a single instance will be running at once.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultReIndexInstitutionalItemJob implements StatefulJob {

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultReIndexInstitutionalItemJob.class);

	/** Application context from spring  */
	public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
        JobDetail jobDetail = arg0.getJobDetail();
		String beanName = jobDetail.getName();
		
		int batchSize = jobDetail.getJobDataMap().getInt("batchSize");
		  
		if (log.isDebugEnabled()) 
		{
		    log.info ("Running SpringBeanDelegatingJob - Job Name ["+jobDetail.getName()+"], Group Name ["+jobDetail.getGroup()+"]");
		    log.info ("Delegating to bean ["+beanName+"]");
		}
		
		if( batchSize <= 0 )
		{
			batchSize = 1;
		}
		  
		ApplicationContext applicationContext = null;
		  
		try 
		{
		    applicationContext = (ApplicationContext) arg0.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);
		} 
		catch (SchedulerException e2) 
		{
		    throw new JobExecutionException("problem with the Scheduler", e2);
		}
		  
		RepositoryService repositoryService = null;
		RepositoryIndexerService repositoryIndexerService = null;
		PlatformTransactionManager tm = null;
		TransactionDefinition td = null;
		try
		{
			repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");
			repositoryIndexerService = (RepositoryIndexerService) applicationContext.getBean("repositoryIndexerService");
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
			log.debug("re indexing items for repository " + repository);
			repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
			repositoryIndexerService.reIndexInstitutionalItems(repository, batchSize);
		}
		tm.commit(ts);
		
	}

}
