/**  
   Copyright 2008 University of Rochester

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

package edu.ur.ir.institution.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSubscription;
import edu.ur.ir.institution.InstitutionalCollectionSubscriptionDAO;
import edu.ur.ir.institution.InstitutionalCollectionSubscriptionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.person.BirthDate;
import edu.ur.ir.person.DeathDate;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameTitle;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;

/**
 * Institutional Collection subscription service implementation.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultInstitutionalCollectionSubscriptionService implements InstitutionalCollectionSubscriptionService{

    /** eclipse generated id */
	private static final long serialVersionUID = -2058783531945128792L;

	/** Java mail sender to send emails */
    private transient JavaMailSender mailSender;
    
    /** Service for dealing with institutional items  */
    private InstitutionalItemService institutionalItemService;
    
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultInstitutionalCollectionSubscriptionService.class);
    
	/**  Used to get the url for a given item */
	private InstitutionalItemVersionUrlGenerator institutionalItemVersionUrlGenerator;

	/**  Institutional collection subscription data access object */
	private InstitutionalCollectionSubscriptionDAO institutionalCollectionSubscriptionDAO;
	
	/** Service for dealing with repositories */
	private RepositoryService repositoryService;
	
	/** format used for formatting dates */
	private String dateFormat;
	
	/** address the email should be from */
	private String fromAddress = "";
	
	/** email that users can use to ask questions */
	private String helpEmail = "";
	
	/**
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#delete(edu.ur.ir.institution.InstitutionalCollectionSubscription)
	 */
	public void delete(InstitutionalCollectionSubscription entity) {
		institutionalCollectionSubscriptionDAO.makeTransient(entity);
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#getAllSubscriptionsForUser(edu.ur.ir.user.IrUser)
	 */
	public List<InstitutionalCollectionSubscription> getAllSubscriptionsForUser(
			IrUser user) {
		return institutionalCollectionSubscriptionDAO.getAllSubscriptionsForUser(user);
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#getById(java.lang.Long, boolean)
	 */
	public InstitutionalCollectionSubscription getById(Long id, boolean lock) {
		return institutionalCollectionSubscriptionDAO.getById(id, lock);
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#getSubscriberCount(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public Long getSubscriberCount(
			InstitutionalCollection institutionalCollection) {
		return institutionalCollectionSubscriptionDAO.getSubscriberCount(institutionalCollection);
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#save(edu.ur.ir.institution.InstitutionalCollectionSubscription)
	 */
	public void save(InstitutionalCollectionSubscription entity) {
		institutionalCollectionSubscriptionDAO.makePersistent(entity);
	}

	public InstitutionalCollectionSubscriptionDAO getInstitutionalCollectionSubscriptionDAO() {
		return institutionalCollectionSubscriptionDAO;
	}

	public void setInstitutionalCollectionSubscriptionDAO(
			InstitutionalCollectionSubscriptionDAO institutionalCollectionSubscriptionDAO) {
		this.institutionalCollectionSubscriptionDAO = institutionalCollectionSubscriptionDAO;
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#isSubscribed(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.user.IrUser)
	 */
	public boolean isSubscribed(InstitutionalCollection collection, IrUser user) {
		return institutionalCollectionSubscriptionDAO.isSubscriberCount(collection, user) == 1l;
	}

	
	/**
	 * @throws MessagingException 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#sendSubriberEmail(edu.ur.ir.user.IrUser)
	 */
	public void sendSubscriberEmail(IrUser user, Repository repository, Date startDate, Date endDate) throws MessagingException 
	{	
		log.debug("Send emails for " + startDate + " to " +  endDate);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		boolean sendEmail = false;
		StringBuffer emailText = new StringBuffer();
		
		List<InstitutionalCollectionSubscription> subscriptions = getAllSubscriptionsForUser(user);
		log.debug("Subscriptions found " + subscriptions.size());
		if( subscriptions.size() > 0  )
		{
			emailText.append("New publications are available in the " + repository.getName() + " collections you have subscribed to \n\n");
			for( InstitutionalCollectionSubscription subscription : subscriptions)
			{
				log.debug("Looking at subscription " + subscription);
				InstitutionalCollection collection = subscription.getInstitutionalCollection();
			    List<InstitutionalItem> items = institutionalItemService.getItems(collection, startDate,  endDate);
			    log.debug("items found = " + items.size());
			    if( items.size() > 0 )
			    {
			    	sendEmail = true;
			    	emailText.append("New publications in " + collection.getName() +": " + items.size() + "\n\n");
			    	for( InstitutionalItem item : items )
			    	{
			    		// get the url to the most recent item
			    		String url = institutionalItemVersionUrlGenerator.createUrl(item, item.getVersionedInstitutionalItem().getCurrentVersion().getVersionNumber());
			    		emailText.append("Publication Name: " + item.getName() + "\n");
			    		emailText.append("URL: " + url + "\n\n");
			    		List<ItemContributor> contributors = item.getVersionedInstitutionalItem().getCurrentVersion().getItem().getContributors();
			    	
			    		
			    		for(ItemContributor c : contributors)
			    		{
			    			PersonName name = c.getContributor().getPersonName();
			    			String strName = this.showLastNameFirst(name, true);
			    			String contributionType = c.getContributor().getContributorType().getName();
			    			emailText.append(contributionType + ":" + strName + "\n");
			    		}
			    		emailText.append("\n\n");
			    	}
			    	 
			    }
			}
		}
		
		
    	emailText.append("Enjoy! \n");
    	emailText.append("Questions/problems? let us know: " + helpEmail);
    	
		if( sendEmail )
		{
		    log.debug("send subscribers emails");
		    MimeMessage message = mailSender.createMimeMessage();
		    String startDateStr = simpleDateFormat.format(startDate); 
		    String endDateStr = simpleDateFormat.format(endDate); 
		    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, false, "UTF-8");
		    String subject = "New " + repository.getName() + " Publications for dates: " + startDateStr + " - " + endDateStr;
		    mimeMessageHelper.setSubject(subject);
		    mimeMessageHelper.setFrom(fromAddress);
		    mimeMessageHelper.setTo(user.getDefaultEmail().getEmail());
		    mimeMessageHelper.setText(emailText.toString());
		    mailSender.send(message);
		}
		
		emailText = null;
	}
	
	/**
	 * Set the mail sender.
	 * 
	 * @param mailSender
	 */
	public void setMailSender(JavaMailSender javaMailSender) {
	    this.mailSender = javaMailSender;
	}

	
	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#getUniqueSubsciberUserIds()
	 */
	public List<Long> getUniqueSubsciberUserIds() {
		return institutionalCollectionSubscriptionDAO.getUniqueSubsciberUserIds();
	}

	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public InstitutionalItemVersionUrlGenerator getInstitutionalItemVersionUrlGenerator() {
		return institutionalItemVersionUrlGenerator;
	}

	public void setInstitutionalItemVersionUrlGenerator(
			InstitutionalItemVersionUrlGenerator institutionalItemVersionUrlGenerator) {
		this.institutionalItemVersionUrlGenerator = institutionalItemVersionUrlGenerator;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	private String showLastNameFirst(PersonName personName, boolean displayDates)
	{
		StringBuffer output = new StringBuffer("");
		if( personName.getSurname() != null &&  personName.getSurname().trim().length() > 0)
    	{
    		output.append(personName.getSurname().trim());
    	}
		if( personName.getForename() != null && personName.getForename().trim().length() > 0)
    	{
			if(output.length() > 0 )
			{
				output.append(", ");
			}
    		output.append(personName.getForename().trim());
    	}
		if( personName.getMiddleName() != null &&  personName.getMiddleName().trim().length() > 0)
    	{
			if(output.length() > 0 )
			{
				output.append(", ");
			}
    		output.append(personName.getMiddleName().trim());
    	}
		if( personName.getNumeration() != null && personName.getNumeration().trim().length() > 0)
    	{
			if(output.length() > 0 )
			{
				output.append(", ");
			}
    		output.append(personName.getNumeration().trim());
    	}
		if( personName.getPersonNameTitles() != null && personName.getPersonNameTitles().size() > 0)
    	{
			if(output.length() > 0 )
			{
				output.append(", ");
			}
    	    for( PersonNameTitle title: personName.getPersonNameTitles())
    	    {
    		    output.append(title.getTitle().trim() + " ");
    	    }
    	}
    	
    	
    	if(displayDates)
    	{
    		output.append(" ");
    	    BirthDate birthDate = personName.getPersonNameAuthority().getBirthDate();
    	    DeathDate deathDate = personName.getPersonNameAuthority().getDeathDate();
    	    int birthYear = 0;
    	    int deathYear = 0;
    	    
    	    if(birthDate != null)
    	    {
    	    	birthYear = birthDate.getYear();
    	    }
    	    
    	    if(deathDate != null)
    	    {
    	    	deathYear = deathDate.getYear();
    	    }
    	    
    	    
    		if( birthYear > 0 || deathYear > 0 )
    		{
    		    output.append("(");
    		    if( birthYear > 0)
    		    {
    			    output.append(birthYear);
    		    }
    		    output.append(" - ");
    		
    		    if(  deathYear > 0 )
    		    {
    			    output.append(deathYear);
    		    }
    		    output.append(")");
    	    }
    	}
    	return output.toString();
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public String getHelpEmail() {
		return helpEmail;
	}

	public void setHelpEmail(String helpEmail) {
		this.helpEmail = helpEmail;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	/**
	 * Return the subscription for the user
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionSubscriptionService#getSubscription(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.user.IrUser)
	 */
	public InstitutionalCollectionSubscription getSubscription(InstitutionalCollection collection, IrUser user) {
		return institutionalCollectionSubscriptionDAO.getSubscriptionForUser(collection, user);
	}

}
