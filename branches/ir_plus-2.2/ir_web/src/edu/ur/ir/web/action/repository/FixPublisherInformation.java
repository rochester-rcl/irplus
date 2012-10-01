/**  
   Copyright 2008 - 2011 University of Rochester

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


package edu.ur.ir.web.action.repository;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.web.action.user.ReIndexUsers;

/**
 * This will launch a job to re index 
 * 
 * @author Nathan Sarr
 *
 */
public class FixPublisherInformation  extends ActionSupport{
	
	// eclipse generated id
	private static final long serialVersionUID = 5343263062940988301L;
	
	/** Quartz scheduler instance to schedule jobs  */
	private Scheduler quartzScheduler;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(ReIndexUsers.class);
	
	public String execute() throws Exception
	{
		log.debug("re index users called");
		//create the job detail
		JobDetail jobDetail = new JobDetail("fixPublisherInfoJob", Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.util.service.PublisherToLocationCorrectorJob.class);
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		trigger.setName("SingleFixPublisherInfoJobFireNow");
		quartzScheduler.scheduleJob(jobDetail, trigger);
		return SUCCESS;
	}
	
	/**
	 * Set the quartz scheduler.
	 * 
	 * @param quartzScheduler
	 */
	public void setQuartzScheduler(Scheduler quartzScheduler) {
		this.quartzScheduler = quartzScheduler;
	}

}
