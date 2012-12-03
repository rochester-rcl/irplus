/**  
   Copyright 2008 - 2012 University of Rochester

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


package edu.ur.ir.repository.service;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.StatefulJob;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.Date;

import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.institution.InstitutionalCollectionStatsCacheService;
import edu.ur.ir.repository.RepositoryStatsCacheService;

/**
 * @author Nathan Sarr
 *
 */
public class UpdateStatsCacheJob  implements StatefulJob{

	/** Application context from spring  */
	public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
	 
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(UpdateStatsCacheJob .class);
	
	/**
	 * Exceuction of the job
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException 
	{
        JobDetail jobDetail = context.getJobDetail();
		String beanName = jobDetail.getName();
		
		if (log.isDebugEnabled()) 
		{
		    log.info ("Running SpringBeanDelegatingJob - Job Name ["+jobDetail.getName()+"], Group Name ["+jobDetail.getGroup()+"]");
		    log.info ("Delegating to bean ["+beanName+"]");
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
		  
		RepositoryStatsCacheService repositoryStatsCacheService = null;
		InstitutionalCollectionStatsCacheService institutionalCollectionStatsCacheService = null;
		ErrorEmailService errorEmailService;
		
		try
		{
			repositoryStatsCacheService = (RepositoryStatsCacheService) applicationContext.getBean("repositoryStatsCacheService");
			institutionalCollectionStatsCacheService = (InstitutionalCollectionStatsCacheService) applicationContext.getBean("institutionalCollectionStatsCacheService");
			log.debug("stats cache service run at " + new Date());
			errorEmailService = (ErrorEmailService)applicationContext.getBean("errorEmailService");
		}
		catch(BeansException e1)
		{
		    throw new JobExecutionException("Unable to retrieve target bean that is to be used as a job source", e1);
		}
		
		
		try
		{
		   repositoryStatsCacheService.forceCacheUpdate();
		   institutionalCollectionStatsCacheService.updateAllCollectionStats();
		}
		catch(Exception e)
		{
			errorEmailService.sendError(e);
		}
		
	}
}
