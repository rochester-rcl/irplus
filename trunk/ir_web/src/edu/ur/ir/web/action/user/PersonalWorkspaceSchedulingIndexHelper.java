package edu.ur.ir.web.action.user;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.SharedInboxFile;

/**
 * Class to help with scheduling of indexing.
 * 
 * @author Nathan Sarr
 *
 */
public class PersonalWorkspaceSchedulingIndexHelper {
	
	/**  Logger for invite user action */
	private static final Logger log = Logger.getLogger(InviteUser.class);
	
	
	/** -------------------------------- NEW ---------------------------------------------------------- */
	
	/**
	 * Schedule the Indexing of the new personal folder.
	 * 
	 * @param pf - new personal folder to index
	 */
	public void scheduleIndexingNew(Scheduler quartzScheduler, PersonalFolder pf)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("indexing new folders for user " + pf.getOwner());
		}
		
		LinkedList<Long> folderIds = new LinkedList<Long>();
		folderIds.add(pf.getId());
	   
		
		log.debug("setting up quartz job to index files");
		//create the job detail
		JobDetail jobDetail = new JobDetail("indexPersonalFoldersJobFireNow" + pf.getId(), Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.user.service.DefaultUserNewFileSystemObjectsIndexJob.class);
		
		jobDetail.getJobDataMap().put("folderIds", folderIds);
		jobDetail.getJobDataMap().put("userId", pf.getOwner().getId());
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		
		// make it unique by appending the folder id
		trigger.setName("IndexPersonalFoldersJobFireNow " + pf.getId());
		try {
			quartzScheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Schedule Indexing of the personal file information.
	 * 
	 * @param user
	 * @param personalFiles
	 */
	public void scheduleIndexingNew(Scheduler quartzScheduler, IrUser user, 
			List<PersonalFile> personalFiles)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("indexing new files for user");
		    log.debug("personalFiles size = " + personalFiles.size());
		}
		
		LinkedList<Long> fileIds = new LinkedList<Long>();
		
		Long lastId = new Long(0);
		// create a list of all personal files
		for( PersonalFile pf : personalFiles)
		{
			log.debug("adding file " + pf);
			fileIds.add(pf.getId());
			lastId = pf.getId();
		}
		
		log.debug("setting up quartz job to index files");
		//create the job detail
		JobDetail jobDetail = new JobDetail("indexNewPersonalFilesJobFireNow" + lastId, Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.user.service.DefaultUserNewFileSystemObjectsIndexJob.class);
		
		jobDetail.getJobDataMap().put("fileIds", fileIds);
		jobDetail.getJobDataMap().put("userId", user.getId());
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		trigger.setName("IndexPersonalFilesJobFireNow" + lastId);
		try {
			quartzScheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Schedule Indexing of the new personal file information.
	 * 
	 * @param user
	 * @param personalFiles
	 */
	public void scheduleIndexingNew(Scheduler quartzScheduler,  
			PersonalFile personalFile)
	{
		LinkedList<PersonalFile> personalFiles = new LinkedList<PersonalFile>();
		personalFiles.add(personalFile);
		this.scheduleIndexingNew(quartzScheduler, personalFile.getOwner(), personalFiles);
	}
	
	/**
	 * Schedule the Indexing of the new inbox file.
	 * 
	 * @param sif - new shared inbox file
	 */
	public void scheduleIndexingNew(Scheduler quartzScheduler, SharedInboxFile sif)
	{
		LinkedList<SharedInboxFile> inboxFiles = new LinkedList<SharedInboxFile>();
		inboxFiles.add(sif);
		scheduleIndexingNew(quartzScheduler, inboxFiles, sif.getSharedWithUser());
	}
	
	/**
	 * Schedule the Indexing of the new inbox files.
	 * 
	 * @param sif - new shared inbox file
	 */
	public void scheduleIndexingNew(Scheduler quartzScheduler, Collection<SharedInboxFile> inboxFiles, IrUser owner)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("indexing new shared inbox files for user " + owner);
		}
		LinkedList<Long> sharedFileInboxIds = new LinkedList<Long>();
		
		Long lastId = new Long(0);
		
		for(SharedInboxFile sif : inboxFiles)
		{
			sharedFileInboxIds.add(sif.getId());
			lastId = sif.getId();
		}
	   
		
		log.debug("setting up quartz job to index files");
		//create the job detail
		JobDetail jobDetail = new JobDetail("indexNewSharedInboxFileJob" + lastId, Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.user.service.DefaultUserNewFileSystemObjectsIndexJob.class);
		
		jobDetail.getJobDataMap().put("sharedFileInboxIds", sharedFileInboxIds);
		jobDetail.getJobDataMap().put("userId", owner.getId());
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		trigger.setName("IndexInboxFilesJobFireNow" + lastId);
		try {
			quartzScheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Schedule the Indexing of the new personal folder.
	 * 
	 * @param pf - new personal folder to index
	 */
	public void scheduleIndexingNew(Scheduler quartzScheduler, PersonalItem pi)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("indexing new folders for user " + pi.getOwner());
		}
		
		LinkedList<Long> itemIds = new LinkedList<Long>();
		itemIds.add(pi.getId());
	   
		
		log.debug("setting up quartz job to index files");
		//create the job detail
		JobDetail jobDetail = new JobDetail("indexNewPersonalItemsJob" + pi.getId(), Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.user.service.DefaultUserNewFileSystemObjectsIndexJob.class);
		
		jobDetail.getJobDataMap().put("itemIds", itemIds);
		jobDetail.getJobDataMap().put("userId", pi.getOwner().getId());
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		trigger.setName("IndexPersonalItemsJobFireNow"+pi.getId());
		try {
			quartzScheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** -------------------------------- UPDATE ---------------------------------------------------------- */
	
	/**
	 * Schedule the Indexing of the new personal folder.
	 * 
	 * @param pf - new personal folder to index
	 */
	public void scheduleIndexingUpdate(Scheduler quartzScheduler, PersonalFolder pf)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("update indexing folders for user " + pf.getOwner());
		}
		
		LinkedList<Long> folderIds = new LinkedList<Long>();
		folderIds.add(pf.getId());
	   
		
		log.debug("setting up quartz job to index files");
		//create the job detail
		JobDetail jobDetail = new JobDetail("indexUpdatePersonalFoldersJobFireNow" + pf.getId(), Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.user.service.DefaultUpdateFileSystemObjectsIndexJob.class);
		
		jobDetail.getJobDataMap().put("folderIds", folderIds);
		jobDetail.getJobDataMap().put("userId", pf.getOwner().getId());
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		
		// make it unique by appending the folder id
		trigger.setName("IndexUpdatePersonalFoldersJobFireNow " + pf.getId());
		try {
			quartzScheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Schedule Indexing of the personal file information.
	 * 
	 * @param user
	 * @param personalFiles
	 */
	public void scheduleIndexingUpdate(Scheduler quartzScheduler, IrUser user, 
			List<PersonalFile> personalFiles)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("updating indexing files for user");
		    log.debug("personalFiles size = " + personalFiles.size());
		}
		
		LinkedList<Long> fileIds = new LinkedList<Long>();
		
		Long lastId = new Long(0);
		// create a list of all personal files
		for( PersonalFile pf : personalFiles)
		{
			log.debug("updating file " + pf);
			fileIds.add(pf.getId());
			lastId = pf.getId();
		}
		
		log.debug("setting up quartz job to index files");
		//create the job detail
		JobDetail jobDetail = new JobDetail("indexUpdatePersonalFilesJobFireNow" + lastId, Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.user.service.DefaultUpdateFileSystemObjectsIndexJob.class);
		
		jobDetail.getJobDataMap().put("fileIds", fileIds);
		jobDetail.getJobDataMap().put("userId", user.getId());
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		trigger.setName("IndexUpdatePersonalFilesJobFireNow" + lastId);
		try {
			quartzScheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Schedule Indexing of the update personal file information.
	 * 
	 * @param user
	 * @param personalFiles
	 */
	public void scheduleIndexingUpdate(Scheduler quartzScheduler,  
			PersonalFile personalFile)
	{
		LinkedList<PersonalFile> personalFiles = new LinkedList<PersonalFile>();
		personalFiles.add(personalFile);
		this.scheduleIndexingUpdate(quartzScheduler, personalFile.getOwner(), personalFiles);
	}
	
	/**
	 * Schedule the Indexing of the update inbox file.
	 * 
	 * @param sif - shared inbox file
	 */
	public void scheduleIndexingUpdate(Scheduler quartzScheduler, SharedInboxFile sif)
	{
		LinkedList<SharedInboxFile> inboxFiles = new LinkedList<SharedInboxFile>();
		inboxFiles.add(sif);
		scheduleIndexingUpdate(quartzScheduler, inboxFiles, sif.getSharedWithUser());
	}
	
	/**
	 * Schedule the Indexing of the update inbox files.
	 * 
	 * @param sif - new shared inbox file
	 */
	public void scheduleIndexingUpdate(Scheduler quartzScheduler, Collection<SharedInboxFile> inboxFiles, IrUser owner)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("indexing update shared inbox files for user " + owner);
		}
		LinkedList<Long> sharedFileInboxIds = new LinkedList<Long>();
		
		Long lastId = new Long(0);
		
		for(SharedInboxFile sif : inboxFiles)
		{
			sharedFileInboxIds.add(sif.getId());
			lastId = sif.getId();
		}
	   
		
		log.debug("setting up quartz job to index files");
		//create the job detail
		JobDetail jobDetail = new JobDetail("indexUpdateSharedInboxFileJob" + lastId, Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.user.service.DefaultUpdateFileSystemObjectsIndexJob.class);
		
		jobDetail.getJobDataMap().put("sharedFileInboxIds", sharedFileInboxIds);
		jobDetail.getJobDataMap().put("userId", owner.getId());
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		trigger.setName("IndexUpdateInboxFilesJobFireNow" + lastId);
		try {
			quartzScheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Schedule the Indexing of the update personal folder.
	 * 
	 * @param pf - update personal folder to index
	 */
	public void scheduleIndexingUpdate(Scheduler quartzScheduler, PersonalItem pi)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("indexing update folders for user " + pi.getOwner());
		}
		
		LinkedList<Long> itemIds = new LinkedList<Long>();
		itemIds.add(pi.getId());
	   
		
		log.debug("setting up quartz job to index files");
		//create the job detail
		JobDetail jobDetail = new JobDetail("indexUpdatePersonalItemsJob" + pi.getId(), Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.user.service.DefaultUpdateFileSystemObjectsIndexJob.class);
		
		jobDetail.getJobDataMap().put("itemIds", itemIds);
		jobDetail.getJobDataMap().put("userId", pi.getOwner().getId());
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		trigger.setName("IndexUpdatePersonalItemsJobFireNow"+pi.getId());
		try {
			quartzScheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** -------------------------------- Delete ---------------------------------------------------------- */
	
	/**
	 * Schedule the Indexing of the new personal folder.
	 * 
	 * @param pf - new personal folder to index
	 */
	public void scheduleIndexingDelete(Scheduler quartzScheduler, PersonalFolder pf)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("indexing delete folders for user " + pf.getOwner());
		}
		
		LinkedList<Long> folderIds = new LinkedList<Long>();
		folderIds.add(pf.getId());
	   
		
		log.debug("setting up quartz job to index files");
		//create the job detail
		JobDetail jobDetail = new JobDetail("indexDeletePersonalFoldersJobFireNow" + pf.getId(), Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.user.service.DefaultDeleteFileSystemObjectsIndexJob.class);
		
		jobDetail.getJobDataMap().put("folderIds", folderIds);
		jobDetail.getJobDataMap().put("userId", pf.getOwner().getId());
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		
		// make it unique by appending the folder id
		trigger.setName("IndexDeletePersonalFoldersJobFireNow " + pf.getId());
		try {
			quartzScheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Schedule Indexing of the personal file information.
	 * 
	 * @param user
	 * @param personalFiles
	 */
	public void scheduleIndexingDelete(Scheduler quartzScheduler, IrUser user, 
			List<PersonalFile> personalFiles)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("deleteing new files for user");
		    log.debug("personalFiles size = " + personalFiles.size());
		}
		
		LinkedList<Long> fileIds = new LinkedList<Long>();
		
		Long lastId = new Long(0);
		// create a list of all personal files
		for( PersonalFile pf : personalFiles)
		{
			log.debug("adding file " + pf);
			fileIds.add(pf.getId());
			lastId = pf.getId();
		}
		
		log.debug("setting up quartz job to index files");
		//create the job detail
		JobDetail jobDetail = new JobDetail("indexDeletePersonalFilesJobFireNow" + lastId, Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.user.service.DefaultDeleteFileSystemObjectsIndexJob.class);
		
		jobDetail.getJobDataMap().put("fileIds", fileIds);
		jobDetail.getJobDataMap().put("userId", user.getId());
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		trigger.setName("IndexDeletePersonalFilesJobFireNow" + lastId);
		try {
			quartzScheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Schedule Indexing of the update personal file information.
	 * 
	 * @param user
	 * @param personalFiles
	 */
	public void scheduleIndexingDelete(Scheduler quartzScheduler,  
			PersonalFile personalFile)
	{
		LinkedList<PersonalFile> personalFiles = new LinkedList<PersonalFile>();
		personalFiles.add(personalFile);
		this.scheduleIndexingDelete(quartzScheduler, personalFile.getOwner(), personalFiles);
	}
	
	/**
	 * Schedule the Indexing of the update inbox file.
	 * 
	 * @param sif - shared inbox file
	 */
	public void scheduleIndexingDelete(Scheduler quartzScheduler, SharedInboxFile sif)
	{
		LinkedList<SharedInboxFile> inboxFiles = new LinkedList<SharedInboxFile>();
		inboxFiles.add(sif);
		scheduleIndexingDelete(quartzScheduler, inboxFiles, sif.getSharedWithUser());
	}
	
	/**
	 * Schedule the Indexing of the update inbox files.
	 * 
	 * @param sif - new shared inbox file
	 */
	public void scheduleIndexingDelete(Scheduler quartzScheduler, Collection<SharedInboxFile> inboxFiles, IrUser owner)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("indexing delete shared inbox files for user " + owner);
		}
		LinkedList<Long> sharedFileInboxIds = new LinkedList<Long>();
		
		Long lastId = new Long(0);
		
		for(SharedInboxFile sif : inboxFiles)
		{
			sharedFileInboxIds.add(sif.getId());
			lastId = sif.getId();
		}
	   
		
		log.debug("setting up quartz job to index files");
		//create the job detail
		JobDetail jobDetail = new JobDetail("indexDeleteSharedInboxFileJob" + lastId, Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.user.service.DefaultDeleteFileSystemObjectsIndexJob.class);
		
		jobDetail.getJobDataMap().put("sharedFileInboxIds", sharedFileInboxIds);
		jobDetail.getJobDataMap().put("userId", owner.getId());
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		trigger.setName("IndexDeleteInboxFilesJobFireNow" + lastId);
		try {
			quartzScheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Schedule the Indexing of the update personal folder.
	 * 
	 * @param pf - update personal folder to index
	 */
	public void scheduleIndexingDelete(Scheduler quartzScheduler, PersonalItem pi)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("indexing delete folders for user " + pi.getOwner());
		}
		
		LinkedList<Long> itemIds = new LinkedList<Long>();
		itemIds.add(pi.getId());
	   
		
		log.debug("setting up quartz job to index files");
		//create the job detail
		JobDetail jobDetail = new JobDetail("indexDeletePersonalItemsJob" + pi.getId(), Scheduler.DEFAULT_GROUP, 
				edu.ur.ir.user.service.DefaultDeleteFileSystemObjectsIndexJob.class);
		
		jobDetail.getJobDataMap().put("itemIds", itemIds);
		jobDetail.getJobDataMap().put("userId", pi.getOwner().getId());
		
		//create a trigger that fires once right away
		Trigger trigger = TriggerUtils.makeImmediateTrigger(0,0);
		trigger.setName("IndexDeletePersonalItemsJobFireNow"+pi.getId());
		try {
			quartzScheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	
	
	


}
