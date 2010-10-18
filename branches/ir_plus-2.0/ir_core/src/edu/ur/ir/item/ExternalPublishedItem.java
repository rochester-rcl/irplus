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


import edu.ur.persistent.BasePersistent;

/**
 * Information about the externally published item
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ExternalPublishedItem extends BasePersistent {

	/**  Eclipse generated id */
	private static final long serialVersionUID = -1703893157173707520L;
	
	/** Date the item was published */
	private PublishedDate publishedDate;
	
	/** Citation */
	private String citation;
	
	/** Publisher for the item  */
	private Publisher publisher;
	
	/**
	 * Get the date item was published
	 * 
	 * @return date the item was published
	 */
	public PublishedDate getPublishedDate() {
		return publishedDate;
	}

	/**
	 * Set the date item was published
	 * 
	 * @param datePublished date item was published
	 */
	public void setPublishedDate(PublishedDate publishedDate) {
		this.publishedDate = publishedDate;
		
	}
	
	/**
	 * Add the publish date
	 * 
	 * @param month
	 * @param day
	 * @param year
	 * @return publlished date
	 */
	public PublishedDate addPublishedDate(int month, int day, int year) {
		PublishedDate publishedDate = new PublishedDate(month, day, year);
		publishedDate.setExternalPublishedItem(this);
		this.publishedDate = publishedDate;
		
		return publishedDate;
	}
	
	/**
	 * Get the citation
	 * 
	 * @return
	 */
	public String getCitation() {
		return citation;
	}

	/**
	 * Set the citation
	 * 
	 * @param citation
	 */
	public void setCitation(String citation) {
		this.citation = citation;
	}

	/**
	 * Get publisher for the item
	 * 
	 * @return
	 */
	public Publisher getPublisher() {
		return publisher;
	}

	/**
	 * Set publisher for the item
	 * 
	 * @param publisher
	 */
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += publisher == null ? 0 : publisher.hashCode();
		value += citation == null ? 0 : citation.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" publishedDate = " );
		sb.append(publishedDate);
		sb.append(" citation = ");
		sb.append(citation);
		sb.append(" publisher = ");
		sb.append(publisher);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{

		if (this == o) return true;

		if (!(o instanceof ExternalPublishedItem)) return false;

		final ExternalPublishedItem other = (ExternalPublishedItem) o;
		
		if( ( citation != null && !citation.equals(other.getCitation()) ) ||
				( citation == null && other.getCitation() != null ) ) return false;
		
		if( ( publisher != null && !publisher.equals(other.getPublisher()) ) ||
				( publisher == null && other.getPublisher() != null ) ) return false;

		return true;
	}
}
