/**  
   Copyright 2008-2010 University of Rochester

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
package edu.ur.ir.oai.metadata.provider.service;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.CopyrightStatement;
import edu.ur.ir.item.DuplicateContributorException;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.FirstAvailableDate;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.OriginalItemCreationDate;
import edu.ur.ir.item.PublishedDate;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.Series;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingService;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeService;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.user.IrUser;

/**
 * Class for testing the dublin core metadata provider.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DublinCoreMetadataProviderTest {
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

	/** Contributor type mapping services */
	ContributorTypeDublinCoreMappingService contributorTypeDublinCoreMappingService = (ContributorTypeDublinCoreMappingService) ctx.getBean("contributorTypeDublinCoreMappingService");

	/** Contributor type services */
	ContributorTypeService contributorTypeService = (ContributorTypeService) ctx.getBean("contributorTypeService");

	/** dublin core metadata provider */
	DefaultDublinCoreOaiMetadataProvider provider = (DefaultDublinCoreOaiMetadataProvider) ctx.getBean("dublinCoreOaiMetadataProvider");
	
	
	public void basicDublinCoreMetadaProviderTest() throws DuplicateContributorException, CollectionDoesNotAcceptItemsException
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		ContributorType contributorType = new ContributorType("a contributor");
		contributorTypeService.save(contributorType);
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		GenericItem item = new GenericItem("genericItem");
		
		ContentType contentType = new ContentType("aContentType");
		ContentType secondContentType = new ContentType("anotherContentType");
			
		PersonName personName = new PersonName();
		personName.setForename("forename");
		personName.setSurname("surname");
		PersonNameAuthority personNameAuthority = new PersonNameAuthority(personName);
		personNameAuthority.addBirthDate(1900);
		personNameAuthority.addDeathDate(2000);
		
		CopyrightStatement copyrightStatement = new CopyrightStatement("this is the copyright");
		
		
		Contributor contributor = new Contributor();
		contributor.setContributorType(contributorType);
		contributor.setPersonName(personName);
		
		ExternalPublishedItem externalPublishedItem = new ExternalPublishedItem();
		Publisher publisher = new Publisher("publisher");
		externalPublishedItem.setPublishedDate(new PublishedDate(12,10,2008));
		externalPublishedItem.setPublisher(publisher);
		
		
		IdentifierType identifier = new IdentifierType();
		identifier.setName("identifiername");
		item.createLink("name", "url");
		
		IrUser owner = new IrUser("name", "password");
		
		Sponsor sponsor = new Sponsor();
		sponsor.setName("sponserer");
				
		item.setDescription("description");
		item.setPrimaryContentType(contentType);
		item.addSecondaryContentType(secondContentType);
		item.addContributor(contributor);
		item.setExternalPublishedItem(externalPublishedItem);
		item.setFirstAvailableDate(new FirstAvailableDate(1, 30, 2008));
		item.setId(10l);
		item.setThesis(false);
		item.setItemAbstract("itemAbstract");
		item.setCopyrightStatement(copyrightStatement);
		
		item.addSubTitle("my other title", null);
		item.addItemIdentifier("value", identifier);
		
		item.setItemKeywords("itemKeywords");
		item.addReport(new Series("series", "Series10"), "Report15");
		item.setLanguageType(new LanguageType("English"));
		item.setName("Itemname");
		item.setOwner(owner);
		item.setPublishedToSystem(true);
		item.setOriginalItemCreationDate(new OriginalItemCreationDate(10,25,2008));
		item.setReleaseDate(new Date());
		item.addItemSponsor(sponsor);
		item.addSubTitle("Title 2", "The articles");

	    
		Repository repository  = new Repository();
		repository.setDescription("myDescription");
		repository.setName("repositoryName");
		

		// create a new institution
		InstitutionalCollection institutionalCollection = 
			new InstitutionalCollection(repository, "myInstitution");
		institutionalCollection.setId(87l);
				
		// create the owner of the personal item
		InstitutionalItem institutionalItem1 =  institutionalCollection.createInstitutionalItem(item);
		institutionalItem1.getVersionedInstitutionalItem().getCurrentVersion().setId(22l);
		String xml = provider.getXml(institutionalItem1.getVersionedInstitutionalItem().getCurrentVersion());
		System.out.println(xml);
		tm.commit(ts);
		
		
		
		 // Start the transaction 
		ts = tm.getTransaction(td);
		contributorTypeService.delete(contributorTypeService.get(contributorType.getId(), false));
		tm.commit(ts);
	}
}
