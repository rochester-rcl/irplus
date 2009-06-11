package edu.ur.ir.user.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexService;

/**
 * Delete file system objects from the system.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultDeleteFileSystemObjectsIndexJob implements Job{
	
	/** Application context from spring  */
	public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
	 
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultDeleteFileSystemObjectsIndexJob.class);

	
	/**
	 * Indexes the specified file or files.
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
        JobDetail jobDetail = arg0.getJobDetail();
		String beanName = jobDetail.getName();
		
		List<Long> fileIds = (List<Long>)jobDetail.getJobDataMap().get("fileIds");
		List<Long> folderIds = (List<Long>) jobDetail.getJobDataMap().get("folderIds");
		List<Long> itemIds = (List<Long>) jobDetail.getJobDataMap().get("itemIds");
		List<Long> sharedFileInboxIds = (List<Long>) jobDetail.getJobDataMap().get("sharedFileInboxIds");
		Long userId = jobDetail.getJobDataMap().getLong("userId");
		  
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
		  
		UserWorkspaceIndexService userWorkspaceIndexService = null;
		UserService userService = null;

		PlatformTransactionManager tm = null;
		TransactionDefinition td = null;
		try
		{
			userWorkspaceIndexService = (UserWorkspaceIndexService) applicationContext.getBean("userWorkspaceIndexService");
			userService = (UserService) applicationContext.getBean("userService");
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
		
		IrUser user = userService.getUser(userId, false);
		
		if( user != null )
		{
			log.debug("indexing workspace information for user " + user);
			
			try
			{
			    // process each file one at a time
			    if( fileIds != null )
			    {
				    for(Long fileId : fileIds)
				    {
					    userWorkspaceIndexService.deleteFileFromIndex(user, fileId);
				    }
			    }
			
			    if( folderIds != null )
			    {
				    for(Long folderId : folderIds )
				    {
					    userWorkspaceIndexService.deleteFolderFromIndex(user, folderId);
				    }
			    }
			    
			    if( itemIds != null )
			    {
			    	for(Long itemId : itemIds)
			    	{
			    		userWorkspaceIndexService.deleteItemFromIndex(user, itemId);
			    	}
			    }
			    
			    if( sharedFileInboxIds != null )
			    {
			    	for(Long inboxId : sharedFileInboxIds)
			    	{
			    		userWorkspaceIndexService.deleteInboxFileFromIndex(user, inboxId);
			    	}
			    }
			}
			catch(Exception e)
			{
				log.error("Unable to index workspace data for user " + user, e);
			}
			
		}
		tm.commit(ts);
		
	}
	
	

}
