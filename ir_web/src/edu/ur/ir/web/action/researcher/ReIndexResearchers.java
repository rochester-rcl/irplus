package edu.ur.ir.web.action.researcher;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import com.opensymphony.xwork2.ActionSupport;



/**
 * Action that fires off a job to re-index researchers.
 * 
 * @author Nathan Sarr
 *
 */
public class ReIndexResearchers extends ActionSupport{
	

	/** eclipse generated id */
	private static final long serialVersionUID = -3469784982224968731L;

	/** Quartz scheduler instance to schedule jobs  */
	private Scheduler quartzScheduler;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(ReIndexResearchers.class);
	
	/** Default Batch Size */
	private int batchSize = 25;
	
	public String execute() throws Exception
	{
		log.debug("re index users called");
		//create the job detail
		JobDetail jobDetail = new JobDetail("reIndexResearchersJob", Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.researcher.service.DefaultReIndexResearchersJob.class);
		
		jobDetail.getJobDataMap().put("batchSize", new Integer(batchSize));
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		trigger.setName("SingleReIndexResearchersJobFireNow");
		quartzScheduler.scheduleJob(jobDetail, trigger);
		
		return SUCCESS;
	}
	
	public Scheduler getQuartzScheduler() {
		return quartzScheduler;
	}

	public void setQuartzScheduler(Scheduler quartzScheduler) {
		this.quartzScheduler = quartzScheduler;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
}
