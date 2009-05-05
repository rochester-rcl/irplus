package edu.ur.ir.institution.service;


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
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		  JobDetail jobDetail = arg0.getJobDetail();
		  
		  String beanName = jobDetail.getName();
		  
		  if (log.isDebugEnabled()) {
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
		  try
		  {
			  subscriptionService = (InstitutionalCollectionSubscriptionService)applicationContext.getBean("institutionalCollectionSubscriptionService");
		  }
		  catch(BeansException e1)
		  {
			  throw new JobExecutionException("Unable to retrieve target bean that is to be used as a job source", e1);
		  }
		  
		  try {
			subscriptionService.sendSubriberEmail(null);
		  } catch (MessagingException e) {
			throw new JobExecutionException("Unable send email ", e);
		  }
		 
		  
	}

}
