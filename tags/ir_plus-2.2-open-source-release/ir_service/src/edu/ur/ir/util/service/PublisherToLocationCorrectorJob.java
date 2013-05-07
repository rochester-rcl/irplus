/**  
   Copyright 2008-2011 University of Rochester

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


package edu.ur.ir.util.service;

import java.util.LinkedList;
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

import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.PublicationLocationSplitter;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.ExternalPublishedItemDAO;
import edu.ur.ir.item.PlaceOfPublication;
import edu.ur.ir.item.PlaceOfPublicationService;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherService;

/**
 * This class will look at publishers and try to move
 * the location into the publisher.
 * 
 * @author Nathan Sarr
 *
 */
public class PublisherToLocationCorrectorJob implements StatefulJob{
	
	//  Logger 
	private static final Logger log = Logger.getLogger(PublisherToLocationCorrectorJob.class);

	/** Application context from spring  */
	public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
	 
	
	/**
	 * Fixes all of the publisher information
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext arg0) throws JobExecutionException 
	{
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
			
			// service to deal with institutional items.
			InstitutionalItemVersionService institutionalItemVersionService;
			
			// allows the publication to be split from the location
			PublicationLocationSplitter publicationLocationSplitter;
			
			// allows for dealing with place of publication
			PlaceOfPublicationService placeOfPublicationService;
			
			// service to deal with publisher information
			PublisherService publisherService;
			
			// external published item data access object
		    ExternalPublishedItemDAO externalPublishedItemDAO;
		    
			PlatformTransactionManager tm = null;
			TransactionDefinition td = null;
			
			IndexProcessingTypeService indexProcessingTypeService = null;
			
			InstitutionalItemService institutionalItemService = null;
			
			ErrorEmailService errorEmailService = null;
		    
		    try
			{
				tm = (PlatformTransactionManager) applicationContext.getBean("transactionManager");
				td = new DefaultTransactionDefinition(
						TransactionDefinition.PROPAGATION_REQUIRED);
				institutionalItemVersionService = (InstitutionalItemVersionService)applicationContext.getBean("institutionalItemVersionService");
				institutionalItemVersionService = (InstitutionalItemVersionService)applicationContext.getBean("institutionalItemVersionService");

				publicationLocationSplitter = (PublicationLocationSplitter)applicationContext.getBean("publicationLocationSplitter");
				placeOfPublicationService = (PlaceOfPublicationService)applicationContext.getBean("placeOfPublicationService");
				publisherService = (PublisherService)applicationContext.getBean("publisherService");
				externalPublishedItemDAO = (ExternalPublishedItemDAO)applicationContext.getBean("externalPublishedItemDAO");
				indexProcessingTypeService = (IndexProcessingTypeService)applicationContext.getBean("indexProcessingTypeService");
				institutionalItemService = (InstitutionalItemService)applicationContext.getBean("institutionalItemService");
				errorEmailService = (ErrorEmailService)applicationContext.getBean("errorEmailService");
				
			}
			catch(BeansException e1)
			{
			    throw new JobExecutionException("Unable to retrieve target bean that is to be used as a job source", e1);
			}
			
			// start a new transaction
			TransactionStatus ts = null;
			
			try
			{
				log.info("Begin fix ");
			    Long lastId = 0l;
				int maxResults = 100;
				List<InstitutionalItemVersion> items = new LinkedList<InstitutionalItemVersion>();
				do
				{
					ts = tm.getTransaction(td);
					items = institutionalItemVersionService.getItemsIdOrder(lastId, maxResults);
					lastId = fixItemPublishers(items, 
							publicationLocationSplitter, 
							placeOfPublicationService, 
							publisherService, 
							externalPublishedItemDAO,
							institutionalItemService,
							indexProcessingTypeService);
					items = institutionalItemVersionService.getItemsIdOrder(lastId, maxResults);
					tm.commit(ts);
				}while( items.size() > 0 );
				log.info("Fix completed");
			}
			catch (Exception e) {
				log.error(e);
				errorEmailService.sendError(e);
			}
			finally
			{
				if( ts != null )
				{
					if( tm != null && !ts.isCompleted())
					{
				        tm.commit(ts);
					}
				}
			}
	}
	

	
	/**
	 * Fix the item publishers - moving the location into the location position.
	 * 
	 * @param versions
	 * @return
	 */
	public Long fixItemPublishers(List<InstitutionalItemVersion> versions, 
			PublicationLocationSplitter publicationLocationSplitter,
			PlaceOfPublicationService placeOfPublicationService,
			PublisherService publisherService,
			ExternalPublishedItemDAO externalPublishedItemDAO,
			InstitutionalItemService institutionalItemService,
			IndexProcessingTypeService indexProcessingTypeService)
	{
		Long lastId = 0l;
		for(InstitutionalItemVersion v : versions)
		{
		    lastId = v.getId();	
		    log.debug("Working with version id " + v.getId());
		    ExternalPublishedItem externalPublishedItem =  v.getItem().getExternalPublishedItem();
		    if(externalPublishedItem != null && externalPublishedItem.getPlaceOfPublication() == null)
		    {
		    	Publisher publisher = externalPublishedItem.getPublisher();
		    	if( publisher != null )
		    	{
		    		log.debug("Found publisher " + publisher);
		    		String[] values = publicationLocationSplitter.split(publisher.getName());
		    		log.debug("Values.length = " + values.length + " values[0] = " + values[0]);
		    		if( !values[0].equals(""))
		    		{
		    			PlaceOfPublication placeOfPublication = placeOfPublicationService.get(values[0]);
		    			log.debug("Place of publication = " + placeOfPublication);
		    			if( placeOfPublication == null )
		    			{
		    				log.debug("Adding new place of publicaiton " + values[0]);
		    				placeOfPublication = new PlaceOfPublication(values[0]);
		    				placeOfPublicationService.save(placeOfPublication);
		    			}
		    			
		    			log.debug("Setting place of publicaiton " + placeOfPublication);
		    			externalPublishedItem.setPlaceOfPublication(placeOfPublication);
		    			
		    			log.debug(" Values[1] = " + values[1]);
		    			if( !values[1].equals(""))
		    			{
		    				Publisher newPublisher = publisherService.getPublisher(values[1]);
		    				log.debug("Publisher = " + newPublisher);
		    				if( newPublisher == null )
		    				{
		    					log.debug("Adding new publisher " + values[1]);
		    					newPublisher = new Publisher(values[1]);
		    					publisherService.savePublisher(newPublisher);
		    				}
		    				log.debug("Setting new publisher " + newPublisher);
		    				externalPublishedItem.setPublisher(newPublisher);
		    				
		    			}
		    			else
		    			{
		    				log.debug("Setting publisher to null");
		    				externalPublishedItem.setPublisher(null);
		    			}
		    			
		    			log.debug("Saving external published item");
		    			externalPublishedItemDAO.makePersistent(externalPublishedItem);
		    			
		    			//check to see if publisher is orphaned
		    			Long publisherCount = externalPublishedItemDAO.getCountForPublisher(publisher.getId());
		    			log.debug("Count for publisher with id " + publisher.getId() + " count = " + publisherCount);
		    			if( publisherCount <= 0 )
		    			{
		    				log.debug("Deleting publisher with id " + publisher.getId());
		    				publisherService.deletePublisher(publisher.getId());
		    			}
		    			institutionalItemService.markAllInstitutionalItemsForIndexing(v.getItem().getId(), indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));

		    		}
		    	}
		    }
		    else
		    {
		    	log.debug("no change");
		    }
		    log.debug("spacer");
			log.debug("spacer");
			log.debug("spacer");
		}
		log.info("returning last id " + lastId);
		
		return lastId;
	}
	

	
}
