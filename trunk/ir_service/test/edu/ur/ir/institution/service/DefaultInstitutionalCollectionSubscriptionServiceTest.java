/**  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  

package edu.ur.ir.institution.service;


import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

import org.testng.annotations.Test;


import edu.ur.ir.repository.service.test.helper.ContextHolder;



/**
 * Tests for the Institutional collection service.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultInstitutionalCollectionSubscriptionServiceTest {
	
	/** Spring application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	Scheduler scheduler = (Scheduler)ctx.getBean("quartzScheduler");
	
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultInstitutionalCollectionSubscriptionServiceTest.class);

	
	/**
	 * Test sending emails for subscriptions.
	 * 
	 * @throws MessagingException 
	 * @throws SchedulerException 
	 */
	public void testSendSubscriptionEmails() throws MessagingException, SchedulerException
	{
		System.out.println("checking scheduler factory bean started = " + scheduler.isStarted());
		
		String[] groupNames = scheduler.getJobGroupNames();
		System.out.println("Checking group names = " + groupNames.length);
		
		for( String g : groupNames)
		{
			System.out.println(" group name = " + g);
		}
		String[] jobs = scheduler.getJobNames("DEFAULT");
		
		System.out.println("Checking jobs jobs length = " + jobs.length);
		for( String j : jobs)
		{
			System.out.println("Found job " + j);
		}
		
		String[] triggerGroups = scheduler.getTriggerGroupNames();
		
		System.out.println("Checking triggerGroups size = " + triggerGroups.length);
		
		for( String g : triggerGroups)
		{
			System.out.println("Trigger group names = " + g);
		}
		
		String[] triggerNames = scheduler.getTriggerNames("DEFAULT");
		System.out.println("Checking triggerNames size = " + triggerNames.length);
		
	}

}
