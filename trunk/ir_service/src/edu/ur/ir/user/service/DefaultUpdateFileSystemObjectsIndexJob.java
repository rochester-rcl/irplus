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

import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexService;

/**
 * This can be used to update existing documents in a users workspace.
 *  
 * @author Nathan Sarr
 *
 */
public class DefaultUpdateFileSystemObjectsIndexJob implements Job{
	
	/** Application context from spring  */
	public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
	 
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultUpdateFileSystemObjectsIndexJob.class);

	
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
		RepositoryService repositoryService = null;
		UserFileSystemService userFileSystemService = null;
		UserPublishingFileSystemService userPublishingFileSystemService = null;

		PlatformTransactionManager tm = null;
		TransactionDefinition td = null;
		try
		{
			repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");
			userFileSystemService = (UserFileSystemService) applicationContext.getBean("userFileSystemService");
			userWorkspaceIndexService = (UserWorkspaceIndexService) applicationContext.getBean("userWorkspaceIndexService");
			userService = (UserService) applicationContext.getBean("userService");
			userPublishingFileSystemService = (UserPublishingFileSystemService) applicationContext.getBean("userPublishingFileSystemService");
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
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		if( user != null && repository != null)
		{
			log.debug("indexing workspace information for user " + user);
			
			try
			{
			    // process each file one at a time
			    if( fileIds != null )
			    {
				    for(Long fileId : fileIds)
				    {
					    PersonalFile personalFile = userFileSystemService.getPersonalFile(fileId, false);
					    userWorkspaceIndexService.updateIndex(repository, personalFile);
				    }
			    }
			
			    if( folderIds != null )
			    {
				    for(Long folderId : folderIds )
				    {
					    PersonalFolder personalFolder = userFileSystemService.getPersonalFolder(folderId, false);
					    userWorkspaceIndexService.updateIndex(repository, personalFolder);
				    }
			    }
			    
			    if( itemIds != null )
			    {
			    	for(Long itemId : itemIds)
			    	{
			    		PersonalItem personalItem = userPublishingFileSystemService.getPersonalItem(itemId, false);
			    		userWorkspaceIndexService.updateIndex(repository, personalItem);
			    	}
			    }
			    
			    if( sharedFileInboxIds != null )
			    {
			    	for(Long inboxId : sharedFileInboxIds)
			    	{
			    		SharedInboxFile inboxFile = userFileSystemService.getSharedInboxFile(inboxId, false);
			    		userWorkspaceIndexService.updateIndex(repository, inboxFile);
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
