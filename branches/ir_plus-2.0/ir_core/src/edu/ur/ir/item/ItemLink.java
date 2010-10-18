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

import java.net.MalformedURLException;
import java.net.URL;

import edu.ur.net.UrlAware;
import edu.ur.persistent.CommonPersistent;

/**
 * Represents a URL link for an Item.  This can be an item which
 * stores no content in the repository but links to content in 
 * a dfferent location and is not mananged by the repository.
 * 
 *   
 * @author Nathan Sarr
 *
 */
public class ItemLink extends CommonPersistent implements UrlAware, ItemObject {

	/** String value of the url; */
	private String urlValue;
	
	/** The item this url belongs to. */
	private GenericItem item;
	
	/**  ordering of the file with respect to other item files */
	private int order;
	
	/** Type of the object */
	public static final String TYPE = "URL";
	
	/**
	 * Generated id.
	 */
	private static final long serialVersionUID = 3154339722048458642L;

	
	/**
	 * Package protected constructor.
	 */
	ItemLink(){};
	
	/**
	 * Create a researcher link with a null researcher folder.  This means this
	 * is a root researcher link.
	 * 
	 * @param linkVersion
	 */
	ItemLink(GenericItem item, String name, String link)
	{
		setItem(item);
		setUrlValue(link);
		setName(name);
	}
	
	/**
	 * The item this url belongs to.
	 * 
	 * @return
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Set the item this url belongs to.
	 * 
	 * @param item
	 */
	public void setItem(GenericItem item) {
		this.item = item;
	}
	
	/**
	 * The url for this link.
	 * 
	 * @see edu.ur.net.UrlAware#getUrl()
	 */
	public URL getUrl() throws MalformedURLException {
		return new URL(urlValue);
	}

	/**
	 * The url value 
	 * 
	 * @return
	 */
	public String getUrlValue() {
		return urlValue;
	}

	/**
	 * Set the url value.
	 * 
	 * @param urlValue
	 */
	public void setUrlValue(String urlValue)  {
		this.urlValue = urlValue;
	}
	
    /**
	 * Get the type of the object
	 * 
	 * @return type
	 */
	public String getType() {
		return TYPE;
	}

	/**
	 * Order with respect to other item files
	 * 
	 * @return int value
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Order with respect to other item files
	 * 
	 * @param order
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		value += urlValue == null ? 0 : urlValue.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Item Link id = ");
		sb.append(id);
		sb.append(" version = " );
		sb.append(version);
		sb.append(" name = " );
		sb.append(name);
		sb.append(" description = " );
		sb.append(description);
		sb.append(" urlValue = " );
		sb.append(urlValue);

		sb.append("]");
		
		return  sb.toString();
	}
	
	/**
	 * Check equality of the object
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ItemLink)) return false;

		final ItemLink other = (ItemLink) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		if( ( urlValue != null && !urlValue.equals(other.getUrlValue()) ) ||
			( urlValue == null && other.getUrlValue() != null ) ) return false;
		
		if( ( item != null && !item.equals(other.getItem()) ) ||
				( item == null && other.getItem() != null ) ) return false;

		return true;
	}

}
