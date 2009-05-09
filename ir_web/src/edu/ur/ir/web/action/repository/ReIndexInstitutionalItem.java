package edu.ur.ir.web.action.repository;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
/**
 * This will schedule the immediate action of re-indexing institutional items.
 * 
 * @author Nathan Sarr
 *
 */
public class ReIndexInstitutionalItem extends ActionSupport{
	
	/** Quartz scheduler instance to schedule jobs  */
	private Scheduler quartzScheduler;
	
	/** Default Batch Size */
	private int batchSize = 10;

	/** eclipse generated id */
	private static final long serialVersionUID = 673513182573887635L;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(ReIndexInstitutionalItem.class);
	
	public String execute()
	{
		log.debug("re index institutional items called");
		//create the job detail
		JobDetail jobDetail = new JobDetail("reIndexItemsJob", Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.repository.service.DefaultReIndexInstitutionalItemJob.class);
		
		jobDetail.getJobDataMap().put("batchSize", new Integer(batchSize));
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		trigger.setName("SingleReIndexItemsJobFireNow");
		try {
			quartzScheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			log.error(e);
		}
		
		
		return SUCCESS;
	}

	public Scheduler getQuartzScheduler() {
		return quartzScheduler;
	}

	public void setQuartzScheduler(Scheduler quartzScheduler) {
		this.quartzScheduler = quartzScheduler;
	}

}
