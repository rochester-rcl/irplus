package edu.ur.ir.web.action.institution;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Creates a quartz job to Re-index the institutional collections.
 * 
 * @author Nathan Sarr
 *
 */
public class ReIndexInstitutionalCollections extends ActionSupport{

	/* eclipse generated id */
	private static final long serialVersionUID = -2807052774132964004L;
	
	/* Quartz scheduler instance to schedule jobs  */
	private transient Scheduler quartzScheduler;
	
	/*  Get the logger for this class */
	private static final Logger log = Logger.getLogger(ReIndexInstitutionalCollections.class);
		
	public String execute() throws Exception
	{
		log.debug("re institutional collections called");
		//create the job detail
		JobDetail jobDetail = new JobDetail("reIndexInstitutionalCollectionsJob", Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.institution.service.InstitutionalCollectionIndexProcessingJob.class);
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		trigger.setName("SingleReIndexPersonNameAuthoritiesJobFireNow");
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
