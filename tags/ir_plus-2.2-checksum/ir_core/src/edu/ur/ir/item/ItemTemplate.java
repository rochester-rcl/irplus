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

package edu.ur.ir.item;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.ur.ir.person.Contributor;
import edu.ur.ir.repository.License;
import edu.ur.persistent.CommonPersistent;

/**
 * Represents a template where the information will be copied 
 * from the specified item into the newly created item.
 * 
 * @author Nathan Sarr
 *
 */
public class ItemTemplate extends CommonPersistent {

	/**  Logger */
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ItemTemplate.class);
	
	/**  Set of links for information about this item. */
	private Set<ItemLink> links = new HashSet<ItemLink>();
	
	/** Generated id */
	private static final long serialVersionUID = 6356981607051131963L;
	
	/**  Indicates if the item has been withdrawn */
	private boolean withdrawn;
	
	/** Date the item was withdrawn. */
	private Date dateWithdrawn;
	
	/** Reason this item was withdrawn */
	private String withdrawnReason;
	
	/**  Date this item is or will be available to the public.*/
	private Date dateAvailable;
	
	/** Date added to the system. */
	private Date dateAccessioned;
	
	/**  Date this item was issued */
	private Date dateIssued;
	
	/**  Intellectual content date */
	private Date dateIntellectualContent;
	
	/**  Date and time this object was last modified. */
	private Timestamp lastModified;
	
	/**  People who have contributed to this item. */
	private Set<Contributor> contributors = new HashSet<Contributor>();
	
	/**  The content type for this item. For example Book, Music piece, etc. */
	private ContentType contentType;
	
	/**  Language used by this item. */
	private LanguageType languageType;
	
	/**  Set of licenses applicable to this item. */
	private Set<License> licenses = new HashSet<License>();
	
	/**  Set of identifiers for the item. */
	private Set<ItemIdentifier> itemIdentifiers = new HashSet<ItemIdentifier>();
	
	/**  The abstract for the item */
	private String itemAbstract;
	
	/**  Citation for this item. */
	private String citation;
	
	/**  The publisher for this item. */
	private Publisher publisher;
	
	/**
	 * Default constructor; 
	 */
	ItemTemplate(){}
	
	/**
	 * Create an item template with the specified name.
	 * 
	 * @param name
	 */
	public ItemTemplate(String name)
	{
		setName(name);
	}
	
	/**
	 * Get the date the file was added to the
	 * system.
	 * 
	 * @return
	 */
	public Date getDateAccessioned() {
		return dateAccessioned;
	}

	/**
	 * Set the accessioned date.
	 * 
	 * @param accessioned
	 */
	public void setDateAccessioned(Date dateAccessioned) {
		this.dateAccessioned = dateAccessioned;
	}

	/**
	 * Get the date this item is available to the 
	 * public.
	 * 
	 * @return
	 */
	public Date getDateAvailable() {
		return dateAvailable;
	}

	/**
	 * Set the date this item is available to the public.
	 * 
	 * @param available
	 */
	public void setDateAvailable(Date dateAvailable) {
		this.dateAvailable = dateAvailable;
	}
	

	/**
	 * Determine if this file is withdrawn.
	 * 
	 * @return true if the item is withdrawn
	 */
	public boolean getWithdrawn() {
		return withdrawn;
	}

	public void withdraw(String reason)
	{
	    if( !withdrawn )
	    {
	    	withdrawn = true;
	    	withdrawnReason = reason;
	    	dateWithdrawn = new Date(new java.util.Date().getTime());
	    }
	}
	/**
	 * Set this item's withdrawn status.
	 * 
	 * @param withDrawn
	 */
	void setWithdrawn(boolean withdrawn) {
		this.withdrawn = withdrawn;
	}
	
	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ItemTemplate)) return false;

		final ItemTemplate other = (ItemTemplate) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		return true;
	}
	
	/** 
	 * Return a string representation of the string buffer.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Item id = ");
		sb.append(id);
		sb.append(" version = " );
		sb.append(version);
		sb.append(" name = " );
		sb.append(" withdrawn = ");
		sb.append( withdrawn );
		sb.append("]");
		
		return  sb.toString();
	}

	/**
	 * Returns the links for this item as an unmodifiable set.
	 * 
	 * @return
	 */
	public Set<ItemLink> getLinks() {
		return Collections.unmodifiableSet(links);
	}

	/**
	 * Set the Item links for this item.
	 * 
	 * @param itemLinks
	 */
	void setLinks(Set<ItemLink> links) {
		this.links = links;
	}

	/**
	 * The data this item wass issued.
	 * 
	 * @return
	 */
	public Date getDateIssued() {
		return dateIssued;
	}

	/**
	 * The data this item was issued.
	 * 
	 * @param dateIssued
	 */
	public void setDateIssued(Date dateIssued) {
		this.dateIssued = dateIssued;
	}

	/**
	 * @return
	 */
	public Date getDateIntellectualContent() {
		return dateIntellectualContent;
	}

	/**
	 * @param dateIntellectualContent
	 */
	public void setDateIntellectualContent(Date dateIntellectualContent) {
		this.dateIntellectualContent = dateIntellectualContent;
	}

	/**
	 * The last time this item was modified.
	 * 
	 * @return
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}

	/**
	 * The last time this item was modified.
	 * 
	 * @param lastModified
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
	
	/**
	 * Get the contributors for this item.
	 * 
	 * @return the contributors.
	 */
	public Set<Contributor> getContributors() {
		return Collections.unmodifiableSet(contributors);
	}

	/**
	 * Set the list of contributors.
	 * 
	 * @param contributors
	 */
	void setContributors(Set<Contributor> contributors) {
		this.contributors = contributors;
	}
	
	/**
	 * Add a contributor to this item.
	 * 
	 * @param contributor
	 */
	public void addContributor(Contributor c)
	{
		contributors.add(c);
	}
	
	/**
	 * Find a contributor based on id
	 * 
	 * @param id
	 * @return
	 */
	public Contributor getContributor(Long id)
	{
		Contributor contributor = null;
	   for( Contributor c : contributors)
	   {
		   if( c.getId().equals(id))
		   {
			   contributor = c;
	       }
	   }
	   return contributor;
	}
	
	/**
	 * Remove a contributor from the list of contributors.
	 * 
	 * @param c
	 * @return
	 */
	public boolean removeContributor(Contributor c)
	{
		return contributors.remove(c);
	}
	
	/**
	 * Add a license to this item.
	 * 
	 * @param license
	 */
	public void addLicense(License l)
	{
		licenses.add(l);
	}
	
	/**
	 * Remove a license from the list of licenses.
	 * 
	 * @param l
	 * @return true ifthe license is removed
	 */
	public boolean removeLicense(License l)
	{
		return licenses.remove(l);
	}
	
	/**
	 * Find a license based on id
	 * 
	 * @param id
	 * @return the found license or null if not found
	 */
	public License getLicense(Long id)
	{
	   License license = null;
	   for( License l : licenses)
	   {
		   if( l.getId().equals(id))
		   {
			   license = l;
	       }
	   }
	   return license;
	}
	

	/**
	 * Get the item content type.
	 * 
	 * @return the item content type
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * Set the item content type.
	 * 
	 * @param itemContentType
	 */
	public void setContentType(ContentType itemContentType) {
		this.contentType = itemContentType;
	}

	/**
	 * Language used by this item.
	 * 
	 * @return the language type used.
	 */
	public LanguageType getLanguageType() {
		return languageType;
	}

	/**
	 * Set the language used by this item.
	 * 
	 * @param languageType
	 */
	public void setLanguageType(LanguageType languageType) {
		this.languageType = languageType;
	}

	/**
	 * Set of licenses applicable to this item.
	 * 
	 * @return
	 */
	public Set<License> getLicenses() {
		return Collections.unmodifiableSet(licenses);
	}

	/**
	 * Set of licenses applicable to this item.
	 * 
	 * @param licenses
	 */
	void setLicenses(Set<License> licenses) {
		this.licenses = licenses;
	}

	/**
	 * Get the item identifiers
	 * 
	 * @return an umodifiable set of this item identifiers.
	 */
	public Set<ItemIdentifier> getItemIdentifiers() {
		return Collections.unmodifiableSet(itemIdentifiers);
	}

	/**
	 * Set the item identifiers 
	 * 
	 * @param itemIdentifier
	 */
	public void setItemIdentifiers(Set<ItemIdentifier> itemIdentifier) {
		this.itemIdentifiers = itemIdentifier;
	}
	
	/**
	 * Remove the item identifier.
	 * 
	 * @param itemIdentifier
	 * @return
	 */
	public boolean removeItemIdentifier(ItemIdentifier itemIdentifier)
	{
		boolean removed = itemIdentifiers.remove(itemIdentifier);
		if( removed )
		{
			itemIdentifier.setItem(null);
		}
		return removed;
	}
	
	/**
	 * Add an item identifier to this item.  Removed the identifier from
	 * any other item it is already added to the identifier.
	 * 
	 * @param itemIdentifier
	 */
	public void addItemIdentifier(ItemIdentifier itemIdentifier)
	{
		if( itemIdentifier.getItem() != null && !itemIdentifier.getItem().equals(this))
		{
		    if (!itemIdentifier.getItem().removeItemIdentifier(itemIdentifier))
		    {
		    	throw new IllegalStateException("Item identifier could not be remvoed from other item");
		    }
		}
		itemIdentifiers.add(itemIdentifier);
	}

	/**
	 * The abstract for the item
	 * 
	 * @return the item abstract.
	 */
	public String getItemAbstract() {
		return itemAbstract;
	}

	/**
	 * Set the item abstract.
	 * 
	 * @param itemAbstract
	 */
	public void setItemAbstract(String itemAbstract) {
		this.itemAbstract = itemAbstract;
	}

	/**
	 * Get the citation for this item
	 * @return
	 */
	public String getCitation() {
		return citation;
	}

	/**
	 * Set the citation for this item.
	 * 
	 * @param citation
	 */
	public void setCitation(String citation) {
		this.citation = citation;
	}
	
	/**
	 * Remove the item link.
	 * 
	 * @param itemLink
	 * @return true if the link was removed
	 */
	public boolean removeLink(ItemLink link)
	{
		boolean removed = links.remove(link);
		if( removed )
		{
			link.setItem(null);
		}
		return removed;
	}
	
	/**
	 * Add an item link to this item.  Removed the link from
	 * any other item it has been added to.
	 * 
	 * @param itemIdentifier
	 */
	public void addLink(ItemLink link)
	{
		if( link.getItem() != null && !link.getItem().equals(this))
		{
		    if (!link.getItem().removeLink(link))
		    {
		    	throw new IllegalStateException("Item link could not be remvoed from other item");
		    }
		}
		links.add(link);
	}

	/**
	 * Get the publisher for this item.
	 * 
	 * @return
	 */
	public Publisher getPublisher() {
		return publisher;
	}

	/**
	 * Set the publisher for this item.
	 * 
	 * @param publisher
	 */
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	/**
	 * Get the date this item was with drawn.
	 * 
	 * @return
	 */
	public Date getDateWithdrawn() {
		return dateWithdrawn;
	}

	/**
	 * Set the date this item was withdrawn.
	 * 
	 * @param dateWithdrawn
	 */
	void setDateWithdrawn(Date dateWithdrawn) {
		this.dateWithdrawn = dateWithdrawn;
	}

	/**
	 * Get the reason this item was withdrawn.
	 * 
	 * @return
	 */
	public String getWithdrawnReason() {
		return withdrawnReason;
	}

	/**
	 * Set the reason this item was withdrawn.
	 * 
	 * @param withdrawnReason
	 */
	void setWithdrawnReason(String withdrawnReason) {
		this.withdrawnReason = withdrawnReason;
	}

}
