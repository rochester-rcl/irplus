package edu.ur.ir.institution.service;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;


import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import edu.ur.ir.institution.InstitutionalCollectionSubscriptionService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;


/**
 * This is used to send emails for the Job
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultSubscriptionEmailJob implements Job{
	 
	/** Application context from spring  */
	public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
	 
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultSubscriptionEmailJob.class);


	/**
	 * Sends emails to all subscribers with notes about new publications added
	 * to institutional collections.
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext arg0) throws JobExecutionException 
	{

        JobDetail jobDetail = arg0.getJobDetail();
		String beanName = jobDetail.getName();
		  
		if (log.isDebugEnabled()) 
		{
		    log.info ("Running SpringBeanDelegatingJob - Job Name ["+jobDetail.getName()+"], Group Name ["+jobDetail.getGroup()+"]");
		    log.info ("Delegating to bean ["+beanName+"]");
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
		  
		InstitutionalCollectionSubscriptionService subscriptionService = null;
		UserService userService = null;
		RepositoryService repositoryService = null;
		try
		{
		    subscriptionService = (InstitutionalCollectionSubscriptionService)applicationContext.getBean("institutionalCollectionSubscriptionService");
			userService = (UserService)applicationContext.getBean("userService");
			repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");
		}
		catch(BeansException e1)
		{
		    throw new JobExecutionException("Unable to retrieve target bean that is to be used as a job source", e1);
		}
		  
		List<Long> uniqueSubscriberIds = subscriptionService.getUniqueSubsciberUserIds();
		
		// make the start date today
		Date startDate = new Date();
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		for( Long id : uniqueSubscriberIds )
		{
		    IrUser user = userService.getUser(id, false);
			
			
			try {
				subscriptionService.sendSubscriberEmail(user, startDate, repository.getLastSubscriptionProcessEmailDate());
				
			} catch (MessagingException e) {
				log.error(e);
			}
		}
		repository.setLastSubscriptionProcessEmailDate(new Timestamp(startDate.getTime()));
		repositoryService.saveRepository(repository);
	}

}
