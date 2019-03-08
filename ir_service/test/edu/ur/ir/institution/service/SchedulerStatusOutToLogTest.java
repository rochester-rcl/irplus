package edu.ur.ir.institution.service;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.Test;

import edu.ur.ir.repository.service.test.helper.ContextHolder;

/**
 * Output the scheduler information out to log file
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class SchedulerStatusOutToLogTest {
	
	/**  Get the logger for this class */
	private static final Logger log = LogManager.getLogger(SchedulerStatusOutToLogTest.class);
	
	/** Spring application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	Scheduler scheduler = (Scheduler)ctx.getBean("quartzScheduler");
	
	
	/**
	 * Test looking at the current scheduler status and prints it to a log file
	 * 
	 * @throws MessagingException 
	 * @throws SchedulerException 
	 */
	public void testSendSubscriptionEmails() throws MessagingException, SchedulerException
	{
		log.debug("checking scheduler factory bean started = " + scheduler.isStarted());
		
		String[] groupNames = scheduler.getJobGroupNames();
		log.debug("Checking group names = " + groupNames.length);
		
		for( String g : groupNames)
		{
			log.debug(" group name = " + g);
		}
		String[] jobs = scheduler.getJobNames("DEFAULT");
		
		log.debug("Checking jobs jobs length = " + jobs.length);
		for( String j : jobs)
		{
			log.debug("Found job " + j);
		}
		
		String[] triggerGroups = scheduler.getTriggerGroupNames();
		
		log.debug("Checking triggerGroups size = " + triggerGroups.length);
		
		for( String g : triggerGroups)
		{
			log.debug("Trigger group names = " + g);
		}
		
		String[] triggerNames = scheduler.getTriggerNames("DEFAULT");
		log.debug("Checking triggerNames size = " + triggerNames.length);
		
	}

}
