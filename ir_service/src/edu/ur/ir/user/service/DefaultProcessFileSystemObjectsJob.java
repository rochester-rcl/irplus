package edu.ur.ir.user.service;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.StatefulJob;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecord;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.user.UserWorkspaceIndexService;

/**
 * Job that processes file system objects that need to be indexed.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultProcessFileSystemObjectsJob implements StatefulJob{
	
	/** Application context from spring  */
	public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
	 
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultProcessFileSystemObjectsJob.class);
	
	/**
	 * Indexes the specified file or files.
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
        JobDetail jobDetail = arg0.getJobDetail();
		String beanName = jobDetail.getName();
		
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
		ErrorEmailService errorEmailService = null;
		UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingService = null;
		
		PlatformTransactionManager tm = null;
		TransactionDefinition td = null;
		try
		{
			errorEmailService = (ErrorEmailService)applicationContext.getBean("errorEmailService");
			repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");
			userFileSystemService = (UserFileSystemService) applicationContext.getBean("userFileSystemService");
			userWorkspaceIndexService = (UserWorkspaceIndexService) applicationContext.getBean("userWorkspaceIndexService");
			userService = (UserService) applicationContext.getBean("userService");
			userPublishingFileSystemService = (UserPublishingFileSystemService) applicationContext.getBean("userPublishingFileSystemService");
			userWorkspaceIndexProcessingService = (UserWorkspaceIndexProcessingRecordService)applicationContext.getBean("userWorkspaceIndexProcessingRecordService");
			tm = (PlatformTransactionManager) applicationContext.getBean("transactionManager");
			td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		}
		catch(BeansException e1)
		{
			errorEmailService.sendError(e1);
		    throw new JobExecutionException("Unable to retrieve target bean that is to be used as a job source", e1);
		}
		
		TransactionStatus ts = null;
		try
		{
		    // start a new transaction
		    ts = tm.getTransaction(td);
		    List<UserWorkspaceIndexProcessingRecord> processingRecords = userWorkspaceIndexProcessingService.getAllOrderByIdDate();
		
            if(log.isDebugEnabled())
            {
        	    log.debug("Processing " + processingRecords.size() + " records ");
            }
        
		    for(UserWorkspaceIndexProcessingRecord record: processingRecords )
		    {
			    if( log.isDebugEnabled() )
			    {
			        log.debug("Processing record " + record);
			    }
		      
			    IrUser user = userService.getUser(record.getUserId(), false);
			    IndexProcessingType indexProcessingType = record.getIndexProcessingType();
			    Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
			
			    if( user == null )
			    {
			    	userWorkspaceIndexProcessingService.delete(record);
			    }
			    else if(!record.getSkipRecord())
			    {
				    if( indexProcessingType.getName().equals(IndexProcessingTypeService.DELETE))
				    {			
					    try 
					    {
						    processDelete(record, user, userWorkspaceIndexService);
						    userWorkspaceIndexProcessingService.delete(record);
					    } 
					    catch (Exception e) 
					    {
						    record.setSkipRecord(true);
						    record.setSkipReason("Procesing failed due to error");
						    userWorkspaceIndexProcessingService.save(record);
						    errorEmailService.sendError(e);
						    log.error("Unable to delete index record " + record + " for user", e);
					    }
				    }
				    else if( indexProcessingType.getName().equals(IndexProcessingTypeService.UPDATE))
				    {
				        try 
				        {
						    processUpdate( record, repository, userWorkspaceIndexService, userFileSystemService, 
							     userPublishingFileSystemService, user);
						    userWorkspaceIndexProcessingService.delete(record);
					    } 
				        catch (Exception e) 
				        {
						    record.setSkipRecord(true);
						    record.setSkipReason("Procesing failed due to error");
						    userWorkspaceIndexProcessingService.save(record);
						    errorEmailService.sendError(e);
						    log.error("Unable to update index record " + record + " for user", e);
					    }
				    }
				    else if( indexProcessingType.getName().equals(IndexProcessingTypeService.INSERT))
				    {
					    try 
					    {
						    processInsert( record, repository, userWorkspaceIndexService, userFileSystemService, 
							 userPublishingFileSystemService, user);
						    userWorkspaceIndexProcessingService.delete(record);
					    } 
					    catch (Exception e) 
					    {
						    record.setSkipRecord(true);
						    record.setSkipReason("Procesing failed due to error");
						    userWorkspaceIndexProcessingService.save(record);
						    errorEmailService.sendError(e);
						    log.error("Unable to insert index record " + record + " for user", e);
					    }
				    }
				    else
				    {
				    	IllegalStateException e = new IllegalStateException("Processing type " + indexProcessingType + "could not be found");
				    	errorEmailService.sendError(e);
				    }
			    }
			}
		}
		finally
		{
			if( ts != null )
			{
				if( tm != null )
				{
			        tm.commit(ts);
				}
			}
		}
		
	}
	
	/**
	 * Processing for delete action.
	 * 
	 * @param record
	 * @param user
	 * @param userWorkspaceIndexService
	 */
	private void processDelete(UserWorkspaceIndexProcessingRecord record, 
			IrUser user, 
			UserWorkspaceIndexService userWorkspaceIndexService)
	{
		FileSystemType fileSystemType = FileSystemType.getFileSystemType(record.getType());
		Long workspaceItemId = record.getWorkspaceItemId();
		
		if(fileSystemType.equals(FileSystemType.PERSONAL_FILE))
		{
		    userWorkspaceIndexService.deleteFileFromIndex(user, workspaceItemId);
		}
		if(fileSystemType.equals(FileSystemType.PERSONAL_FOLDER))
		{
			userWorkspaceIndexService.deleteFolderFromIndex(user, workspaceItemId);
		}
		if(fileSystemType.equals(FileSystemType.SHARED_INBOX_FILE))
		{
		    userWorkspaceIndexService.deleteInboxFileFromIndex(user, workspaceItemId);
		}
		if(fileSystemType.equals(FileSystemType.PERSONAL_ITEM))
		{
		    userWorkspaceIndexService.deleteItemFromIndex(user, workspaceItemId);
		}
		if(fileSystemType.equals(FileSystemType.PERSONAL_COLLECTION))
		{
		    userWorkspaceIndexService.deleteCollectionFromIndex(user, workspaceItemId);
		}
	}
	
	/**
	 * Processing for update action.
	 * 
	 * @param record
	 * @param user
	 * @param userWorkspaceIndexService
	 * @throws IOException 
	 * @throws LocationAlreadyExistsException 
	 */
	private void processUpdate(UserWorkspaceIndexProcessingRecord record, 
			Repository repository, 
			UserWorkspaceIndexService userWorkspaceIndexService,
			UserFileSystemService userFileSystemService,
			UserPublishingFileSystemService userPublishingFileSystemService, 
			IrUser user) throws LocationAlreadyExistsException, IOException
	{
		FileSystemType fileSystemType = FileSystemType.getFileSystemType(record.getType());
		Long workspaceItemId = record.getWorkspaceItemId();
		
		
		if(fileSystemType.equals(FileSystemType.PERSONAL_FILE))
		{
			PersonalFile file = userFileSystemService.getPersonalFile(workspaceItemId, false);
			if( file != null )
			{
		        userWorkspaceIndexService.updateIndex(repository, file);
			}
			else
			{
			    processDelete(record, user, userWorkspaceIndexService);
			}
		}
		if(fileSystemType.equals(FileSystemType.PERSONAL_FOLDER))
		{
			PersonalFolder folder = userFileSystemService.getPersonalFolder(workspaceItemId, false);
			if( folder != null )
			{
			    userWorkspaceIndexService.updateIndex(repository, folder);
			}
			else
			{
			    processDelete(record, user, userWorkspaceIndexService);
			}
		}
		if(fileSystemType.equals(FileSystemType.SHARED_INBOX_FILE))
		{
			SharedInboxFile inboxFile = userFileSystemService.getSharedInboxFile(workspaceItemId, false);
			if( inboxFile != null )
			{
			    userWorkspaceIndexService.updateIndex(repository, inboxFile);
			}
			else
			{
			    processDelete(record, user, userWorkspaceIndexService);
			}
		}
		if(fileSystemType.equals(FileSystemType.PERSONAL_ITEM))
		{
			PersonalItem item = userPublishingFileSystemService.getPersonalItem(workspaceItemId, false);
			if( item != null )
			{
		        userWorkspaceIndexService.updateIndex(repository, item);
			}
			else
			{
			    processDelete(record, user, userWorkspaceIndexService);
			}
		}
		if(fileSystemType.equals(FileSystemType.PERSONAL_COLLECTION))
		{
			PersonalCollection collection = userPublishingFileSystemService.getPersonalCollection(workspaceItemId, false);
			if( collection != null )
			{
			    userWorkspaceIndexService.updateIndex(repository, collection);
			}
			else
			{
			    processDelete(record, user, userWorkspaceIndexService);
			}
		}
	}
	
	/**
	 * Processing for insert action.
	 * 
	 * @param record
	 * @param user
	 * @param userWorkspaceIndexService
	 * @throws IOException 
	 * @throws LocationAlreadyExistsException 
	 */
	private void processInsert(UserWorkspaceIndexProcessingRecord record, 
			Repository repository, 
			UserWorkspaceIndexService userWorkspaceIndexService,
			UserFileSystemService userFileSystemService,
			UserPublishingFileSystemService userPublishingFileSystemService,
			IrUser user) throws LocationAlreadyExistsException, IOException
	{
		FileSystemType fileSystemType = FileSystemType.getFileSystemType(record.getType());
		Long workspaceItemId = record.getWorkspaceItemId();
		
		
		if(fileSystemType.equals(FileSystemType.PERSONAL_FILE))
		{
			PersonalFile file = userFileSystemService.getPersonalFile(workspaceItemId, false);
			if( file != null )
			{
		        userWorkspaceIndexService.addToIndex(repository, file);
			}
			else
			{
			    processDelete(record, user, userWorkspaceIndexService);
			}
		}
		if(fileSystemType.equals(FileSystemType.PERSONAL_FOLDER))
		{
			PersonalFolder folder = userFileSystemService.getPersonalFolder(workspaceItemId, false);
			if( folder != null )
			{
			    userWorkspaceIndexService.addToIndex(repository, folder);
			}
			else
			{
			    processDelete(record, user, userWorkspaceIndexService);
			}
		}
		if(fileSystemType.equals(FileSystemType.SHARED_INBOX_FILE))
		{
			SharedInboxFile inboxFile = userFileSystemService.getSharedInboxFile(workspaceItemId, false);
			if( inboxFile != null )
			{
			    userWorkspaceIndexService.addToIndex(repository, inboxFile);
			}
			else
			{
			    processDelete(record, user, userWorkspaceIndexService);
			}
		}
		if(fileSystemType.equals(FileSystemType.PERSONAL_ITEM))
		{
			PersonalItem item = userPublishingFileSystemService.getPersonalItem(workspaceItemId, false);
			if( item != null )
			{
		        userWorkspaceIndexService.addToIndex(repository, item);
			}
			else
			{
			    processDelete(record, user, userWorkspaceIndexService);
			}
		}
		if(fileSystemType.equals(FileSystemType.PERSONAL_COLLECTION))
		{
			PersonalCollection collection = userPublishingFileSystemService.getPersonalCollection(workspaceItemId, false);
			if( collection != null )
			{
		        userWorkspaceIndexService.addToIndex(repository, collection);
			}
			else
			{
			    processDelete(record, user, userWorkspaceIndexService);
			}
		}
	}

}
